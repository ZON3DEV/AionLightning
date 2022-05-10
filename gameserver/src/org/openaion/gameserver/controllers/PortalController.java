/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.model.templates.portal.PortalItem;
import org.openaion.gameserver.model.templates.portal.ExitPoint;
import org.openaion.gameserver.model.templates.portal.PortalTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.SiegeService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.world.WorldMap;
import org.openaion.gameserver.world.WorldMapInstance;



/**
 *  @author ATracer, SuneC, Dallas
 * 
 */
public class PortalController extends NpcController
{
	private static final Logger	log	= Logger.getLogger(PortalController.class);

	PortalTemplate				portalTemplate;

	@Override
	public void setOwner(Creature owner)
	{
		super.setOwner(owner);
		portalTemplate = DataManager.PORTAL_DATA.getPortalTemplate(owner.getObjectTemplate().getTemplateId());
	}

	@Override
	public void onDialogRequest(final Player player)
	{
		if(portalTemplate == null)
			return;
			
		if(!CustomConfig.ENABLE_INSTANCES)
			return;

		final int defaultUseTime = 3000;
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
			defaultUseTime, 1));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner()
			.getObjectId()), true);

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
					defaultUseTime, 0));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0,
					getOwner().getObjectId()), true);
				
				analyzePortation(player);
			}
			
			/**
			 * @param player
			 */
			private void analyzePortation(final Player player)
			{
				if(portalTemplate.getIdTitle() !=0 && player.getCommonData().getTitleId() != portalTemplate.getIdTitle())
					return;

				if(portalTemplate.getRace() != null && !portalTemplate.getRace().equals(player.getCommonData().getRace()))
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MOVE_PORTAL_ERROR_INVALID_RACE);
					return;
				}

				if((portalTemplate.getMaxLevel() != 0 && player.getLevel() > portalTemplate.getMaxLevel())
					|| player.getLevel() < portalTemplate.getMinLevel())
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
					return;
				}

				PlayerGroup group = player.getPlayerGroup();
				if(portalTemplate.isGroup() && group == null)
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_PARTY_DON);
					return;
				}
              for (PortalItem portalItem : portalTemplate.getPortalItem()) {
           Item item = player.getInventory().getFirstItemByItemId(portalItem.getItemid());
           if (item == null) {
              // Send message to player that he's missing an item
               PacketSendUtility.sendMessage(player, "You're probably missing the correct items, to do that");
              return;
           }
           
           if (item.getItemCount() < portalItem.getQuantity()) {
              // Send message to player that he does not have enough of the item
                PacketSendUtility.sendMessage(player, "You still have not enough items to do that");
              return;
           }
           
           player.getInventory().decreaseItemCount(item, portalItem.getQuantity());
        }
				
				// check quest requirements
				if(portalTemplate.getQuestReq() != 0)
				{
					QuestState qstp = player.getQuestStateList().getQuestState(portalTemplate.getQuestReq());
					if(qstp == null || qstp.getStatus() != QuestStatus.COMPLETE)
					{
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 27));
						return;
					}
				}

				// check item requirements
				if(portalTemplate.getItemReq() != 0)
				{
					if(player.getInventory().getItemCountByItemId(portalTemplate.getItemReq()) < 1)
					{
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 27));
						return;
					}
				}
				
				int worldId = 0;
				ExitPoint exit = null;
				for (ExitPoint point : portalTemplate.getExitPoint())
				{
					if (point.getRace() == null || point.getRace().equals(player.getCommonData().getRace()))
					{
						worldId = point.getMapId();
						exit = point;
					}
				}
				
				if(!portalTemplate.isInstance() || player.getWorldId() == worldId)
				{
					if (exit != null)
					{
						// Silentera Requirements
						if(worldId == 600010000)
						{
							if(player.getCommonData().getRace().getRaceId() == 0 && 
								(SiegeService.getInstance().getSiegeLocation(2021).getRace().getRaceId() != 0||
								SiegeService.getInstance().getSiegeLocation(2011).getRace().getRaceId() != 0))
								return;
							else if(player.getCommonData().getRace().getRaceId() == 1 && 
								(SiegeService.getInstance().getSiegeLocation(3011).getRace().getRaceId() != 1 ||
								SiegeService.getInstance().getSiegeLocation(3021).getRace().getRaceId() != 1))
								return;
						}
						TeleportService.teleportTo(player, worldId, exit.getX(), exit.getY(), exit.getZ(), 0);
					}
					else
						log.warn("Missing exit for teleport npcid: "+portalTemplate.getNpcId()+" teleporting to worldid: "+worldId);
				}
				else
				{
					if(portalTemplate.isGroup() && group != null)
					{
						WorldMapInstance instance = InstanceService.getRegisteredInstance(worldId, group.getGroupId());
						// register if not yet created
						if(instance == null)
						{
							if(checkInstanceCooldown(player, worldId, 0))
								return;
							if(portalTemplate.getItemReq() != 0)
								deleteReqitems(player);
							instance = registerGroup(group);
							
							setInstanceCooldown(player, worldId, instance.getInstanceId());
							transfer(player, instance);
							return;
						}
						if(checkInstanceCooldown(player, worldId, instance.getInstanceId()))
							return;
						if(!instance.getRegisteredGroup().equals(group))
							return;
						
						setInstanceCooldown(player, worldId, instance.getInstanceId());
						transfer(player, instance);
					}
					else if(!portalTemplate.isGroup())
					{
						WorldMapInstance instance = InstanceService.getRegisteredInstance(worldId, player.getObjectId());
						// if already registered - just teleport
						if(instance != null)
						{
							transfer(player, instance);
							return;
						}
						if(checkInstanceCooldown(player, worldId, 0))
							return;
						if(portalTemplate.getItemReq() != 0)
							deleteReqitems(player);
						port(player);
					}
				}
			}
		}, defaultUseTime);

	}

	/**
	 * @param player
	 * @param worldId
	 * @param instanceId
	 */
	public static void setInstanceCooldown(Player player, int worldId, int instanceId)
	{
		int instanceMapId = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getInstanceMapId();
		
		if(player.getInstanceCD(instanceMapId) == null)
		{
			int cd = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getCooldown();
			Timestamp CDEndTime = new Timestamp(Calendar.getInstance().getTimeInMillis() + cd*60000);
			
			if(player.isInGroup())
				player.addInstanceCD(instanceMapId, CDEndTime, instanceId, player.getPlayerGroup().getGroupId());
			else
				player.addInstanceCD(instanceMapId, CDEndTime, instanceId, 0);
		}
	}

	/**
	 * @param player, worldId
	 */
	protected boolean checkInstanceCooldown(Player player, int worldId, int instanceId)
	{
		int instanceMapId = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getInstanceMapId();
		int mapname = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getMapNameId();
		
		if(!InstanceService.canEnterInstance(player, instanceMapId, instanceId) && CustomConfig.INSTANCE_COOLDOWN && player.getWorldId() != worldId)
		{
			int timeinMinutes = InstanceService.getTimeInfo(player).get(instanceMapId)/60;
			if (timeinMinutes >= 60 )
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_HOUR_CLIENT(mapname, timeinMinutes/60));
			else	
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_MIN_CLIENT(mapname, timeinMinutes));
			
			return true;
		}
		return false;
	}

	/**
	 * @param player
	 */
	private void port(Player requester)
	{
		WorldMapInstance instance = null;
		int worldId = 0;
		for (ExitPoint point : portalTemplate.getExitPoint())
		{
			if (point.getRace() == null || point.getRace().equals(requester.getCommonData().getRace()))
				worldId = point.getMapId();
		}
		
		if(portalTemplate.isInstance())
		{
			instance = InstanceService.getNextAvailableInstance(worldId);
			InstanceService.registerPlayerWithInstance(instance, requester);
			
			setInstanceCooldown(requester, worldId, instance.getInstanceId());
		}
		else
		{
			WorldMap worldMap = World.getInstance().getWorldMap(worldId);
			if(worldMap == null)
			{
				log.warn("There is no registered map with id " + worldId);
				return;
			}
			instance = worldMap.getWorldMapInstance();
		}
		
		transfer(requester, instance);
	}

	/**
	 * @param player
	 */
	private WorldMapInstance registerGroup(PlayerGroup group)
	{
		int worldId = 0;
		for (ExitPoint point : portalTemplate.getExitPoint())
		{
			if (point.getRace() == null || point.getRace().equals(group.getGroupLeader().getCommonData().getRace()))
				worldId = point.getMapId();
		}
		WorldMapInstance instance = InstanceService.getNextAvailableInstance(worldId);
		InstanceService.registerGroupWithInstance(instance, group);
		return instance;
	}

	/**
	 * @param players
	 */
	private void transfer(Player player, WorldMapInstance instance)
	{
		
		ExitPoint exitPoint = null;
		for (ExitPoint point : portalTemplate.getExitPoint())
		{
			if(point.getRace() == null || point.getRace().equals(player.getCommonData().getRace()))
				exitPoint = point;
		}
		
		if(instance.getTimerEnd() != null)
		{
			int timeInSeconds = (int)((instance.getTimerEnd().getTimeInMillis() - System.currentTimeMillis())/1000);
			
			if(timeInSeconds > 0)
			{
				player.setQuestTimerOn(true);
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(4, 0, timeInSeconds));
			}
		}
		
		TeleportService.teleportTo(player, exitPoint.getMapId(), instance.getInstanceId(),
			exitPoint.getX(), exitPoint.getY(), exitPoint.getZ(), 0);
	}
	
	/**
	 * @param player
	 */
	private void deleteReqitems(Player player)
	{
		Item item = player.getInventory().getFirstItemByItemId(portalTemplate.getItemReq());
		if(player.getInventory().removeFromBag(item, true))
			PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(item.getObjectId()));
	}

}
