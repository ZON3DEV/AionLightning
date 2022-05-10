/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.inggison;

import java.util.Collections;

import org.openaion.gameserver.controllers.PortalController;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.WorldMapTemplate;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.skill.SkillEngine;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.WorldMapInstance;


/**
 * @author Nephis, modified Vincas and Rolandas
 *
 */
public class _10021FriendsforLife extends QuestHandler
{

	private final static int	questId	= 10021;
	private final static int[]	npc_ids	= { 798927, 798954, 799022, 730008, 730019, 730024 };

	public _10021FriendsforLife()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addOnEnterWorld(questId);
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(215523).addOnKillEvent(questId);
		qe.setNpcQuestData(215522).addOnKillEvent(questId);
		qe.setNpcQuestData(215520).addOnKillEvent(questId);
		qe.setNpcQuestData(215521).addOnKillEvent(questId);
		qe.setQuestItemIds(182206627).add(questId);
		qe.setQuestItemIds(182206628).add(questId);
		qe.setQuestItemIds(164000137).add(questId);
		qe.setQuestItemIds(164000138).add(questId);
		qe.setQuestItemIds(164000139).add(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onEnterWorldEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.COMPLETE)
		{
			if(player.getWorldId() != 300190000)
			{
				player.getInventory().removeFromBagByItemId(182206627, 1);
				player.getInventory().removeFromBagByItemId(182206628, 1);
				player.getInventory().removeFromBagByItemId(164000137, 1);
				player.getInventory().removeFromBagByItemId(164000138, 1);
				player.getInventory().removeFromBagByItemId(164000139, 1);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		switch(env.getTargetId())
		{
			case 215523:
			case 215522:
			case 215520:
			case 215521:
				if(qs.getQuestVarById(1) < 34 && qs.getQuestVarById(0) == 3)
				{
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
					return true;
				}
				break;
			case 215488:
				if(qs.getQuestVarById(0) == 7)
				{
					@SuppressWarnings("unused")
					final int instanceId = player.getInstanceId();
					QuestService.addNewSpawn(300190000, player.getInstanceId(), 799503, (float) 566.1413, (float) 813.3888, (float) 1375.138, (byte) 32, true);
					return true;
				} 
				else if(qs != null && qs.getStatus() == QuestStatus.COMPLETE)
				{
					@SuppressWarnings("unused")
					final int instanceId = player.getInstanceId();
					QuestService.addNewSpawn(300190000, player.getInstanceId(), 799503, (float) 566.1413, (float) 813.3888, (float) 1375.138, (byte) 32, true);
					return true;
				} 
		}
		
		return false;
    }

	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, final Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int var = qs.getQuestVarById(0);

		if (id != 182206627 && id != 182206628 && id != 164000137 && id != 164000138 && id != 164000139)
			return HandlerResult.UNKNOWN;

		if(player.getWorldId() != 300190000)
			return HandlerResult.FAILED;

		if (qs != null && qs.getStatus() == QuestStatus.COMPLETE)
		{
			if (id == 182206628 && !hasItem(player, 182206628))
			{
				if (ItemService.canAddItems(player, Collections.singletonList(new QuestItems(182206628, 1))))
					ItemService.addItem(player, 182206628, 1, 60 * 7200);
			}
			if (id == 182206627 && !hasItem(player, 182206627))
			{
				if (ItemService.canAddItems(player, Collections.singletonList(new QuestItems(182207604, 1))))
					ItemService.addItem(player, 182206627, 1, 60 * 7200);
			}
			if (id == 164000137 && !hasItem(player, 164000137))
			{
				if (ItemService.canAddItems(player, Collections.singletonList(new QuestItems(164000137, 1))))
					ItemService.addItem(player, 164000137, 1, 60 * 7200);
			}
			if (id == 164000138 && !hasItem(player, 164000138))
			{
				if (ItemService.canAddItems(player, Collections.singletonList(new QuestItems(164000138, 1))))
					ItemService.addItem(player, 164000138, 1, 60 * 7200);
			}
			if (id == 164000139 && !hasItem(player, 164000139))
			{
				if (ItemService.canAddItems(player, Collections.singletonList(new QuestItems(164000139, 1))))
					ItemService.addItem(player, 164000139, 1, 60 * 7200);
			}
			useSkill(player, item);
		}
		else if (var == 5 || var == 6 || var == 7)
		{
			if (id == 182206627)
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						int var = qs.getQuestVarById(0);
						PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
						if (var == 5)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
						}
						useSkill(player, item);
					}
				}, 3000);
			}
			else if (id == 182206628 && var > 5)
			{
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						int var2 = qs.getQuestVarById(2);
						if(var < 7 && var2 < 19)
						{
							qs.setQuestVarById(2, var2 + 1);
							if (!hasItem(player, 182206628))
								ItemService.addItem(player, 182206628, 1, 60 * 7200);
							updateQuestStatus(env);
						}
						else if (var == 6 && var2 > 0)
						{
							qs.setQuestVarById(2,0);// Needed to continue
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
						}
						useSkill(player, item);
					}
				}, 100);
			}
		}
		return HandlerResult.FAILED; // don't remove from inventory
	}
	
	private void useSkill(Player player, Item item)
	{
		if (player.isItemUseDisabled(item.getItemTemplate().getDelayId()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANT_USE_UNTIL_DELAY_TIME);
			return;
		}
		
		int useDelay = item.getItemTemplate().getDelayTime();
		player.addItemCoolDown(item.getItemTemplate().getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);

		if (item.getItemId() == 182206627)
		{
			int skillId = 10251;
			int level = 1;

			Skill skill = SkillEngine.getInstance().getSkill(player, skillId, level, player.getTarget(), item.getItemTemplate());
			if(skill != null)
			{
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
				item.getObjectId(), item.getItemTemplate().getTemplateId()), true);
			skill.useSkill();
			}
			return;
		}
		if (item.getItemId() == 182206628)
		{
		int skillId = item.getItemId() == 182206627 ? 10251 : 9831;
		int level = item.getItemId() == 182206627 ? 1 : 4;

		Skill skill = SkillEngine.getInstance().getSkill(player, skillId, level, player.getTarget(), item.getItemTemplate());
		if(skill != null)
		{
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
				item.getObjectId(), item.getItemTemplate().getTemplateId()), true);
			skill.useSkill();
		}
			return;
		}
		else if (item.getItemId() == 164000137)
		{
			int skillId = 164000137 == 182207604 ? 10252 : 9832;
			int level = item.getItemId() == 182207604 ? 1 : 4;

			Skill skill = SkillEngine.getInstance().getSkill(player, skillId, level, player.getTarget(), item.getItemTemplate());
			if(skill != null)
			{
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
				item.getObjectId(), item.getItemTemplate().getTemplateId()), true);
			skill.useSkill();
			}
			return;
		}
		else if (item.getItemId() == 164000138)
		{
			int skillId = 164000138 == 182207604 ? 10252 : 9833;
			int level = item.getItemId() == 182207604 ? 1 : 4;

			Skill skill = SkillEngine.getInstance().getSkill(player, skillId, level, player.getTarget(), item.getItemTemplate());
			if(skill != null)
			{
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
				item.getObjectId(), item.getItemTemplate().getTemplateId()), true);
			skill.useSkill();
			}
			return;
		}
		else if (item.getItemId() == 164000139)
		{
			int skillId = 164000139 == 182207604 ? 10252 : 9834;
			int level = item.getItemId() == 182207604 ? 1 : 4;

			Skill skill = SkillEngine.getInstance().getSkill(player, skillId, level, player.getTarget(), item.getItemTemplate());
			if(skill != null)
			{
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
				item.getObjectId(), item.getItemTemplate().getTemplateId()), true);
			skill.useSkill();
			}
			return;
		}					
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}
	
	private boolean hasItem(Player player, int itemId)
	{
		return player.getInventory().getItemCountByItemId(itemId) > 0;
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798927)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		else if(qs.getStatus() == QuestStatus.COMPLETE)
		{
			if(targetId == 799022)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 2376);
				else if(env.getDialogId() == 10004)
				{
					if(player.getPlayerGroup() == null)
					{
						WorldMapTemplate world = DataManager.WORLD_MAPS_DATA.getTemplate(300190000);
						int mapname = DataManager.WORLD_MAPS_DATA.getTemplate(300190000).getMapNameId();
						if (!InstanceService.canEnterInstance(player, world.getInstanceMapId(), 0))
						{
							int timeinMinutes = InstanceService.getTimeInfo(player).get(world.getInstanceMapId())/60;
							if (timeinMinutes >= 60 )
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_HOUR(mapname, timeinMinutes/60));
							else	
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_MIN(mapname, timeinMinutes));
							
							return false;
						}
						if (!hasItem(player, 182206627))
						{
							if (ItemService.canAddItems(player, Collections.singletonList(new QuestItems(182206627, 1))))
								ItemService.addItem(player, 182206627, 1, 60 * 7200);
							else
								return true;
						}
						if (!hasItem(player, 182206628))
						{
							if (ItemService.canAddItems(player, Collections.singletonList(new QuestItems(182206628, 1))))
								ItemService.addItem(player, 182206628, 1, 60 * 7200);
							else
								return true;
						}
						WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300190000);
						InstanceService.registerPlayerWithInstance(newInstance, player);
						TeleportService.teleportTo(player, 300190000, newInstance.getInstanceId(), 198.82353f, 221.40387f, 1098.4879f, (byte) 41);
						PortalController.setInstanceCooldown(player, 300190000, newInstance.getInstanceId());
						return true;
					}
					else
						return sendQuestDialog(env, 2546);
				}
				else
					return defaultQuestStartDialog(env);
			}
		else if(targetId == 799503)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 4080);
				case 1013:
				case 10000:
					if(var == 1)
					{
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						TeleportService.teleportTo(player, 210050000, 2888.9226f, 359.70108f, 298.73184f, (byte) 56);
						return true;
					}
			}
		}
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 798927) // Versetti
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 798954) // Tialla
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1352);
					else if(var == 8)
						return sendQuestDialog(env, 3057);
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10255:
					if(var == 8)
					{
						qs.setQuestVarById(0, 11);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 799022) // Lothas
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var == 2)
						return sendQuestDialog(env, 1779);
					else if (var > 2 && var < 3)
						return sendQuestDialog(env, 2461);
				break;
				case 26:
					if(var == 3)
					{
						if (qs.getQuestVarById(1) == 0)
							return sendQuestDialog(env, 1693);
						else
							return sendQuestDialog(env, 2375);
					}
					else if(var == 7)
					{
						if(player.getInventory().getItemCountByItemId(182206602) == 0)
							return sendQuestDialog(env, 2461);
						else
							return sendQuestDialog(env, 2716);
					}
					else if(var == 14)
						return sendQuestDialog(env, 4336);
				case 10002:
					if(var == 14)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10003:
					if(var == 2)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
					}
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				case 10004:
					if(var == 3 || var < 7)
					{
						if(player.getPlayerGroup() == null)
						{
							if (!hasItem(player, 182206627))
							{
								if (ItemService.canAddItems(player, Collections.singletonList(new QuestItems(182206627, 1))))
									ItemService.addItem(player, 182206627, 1, 60 * 7200);
								else
									return true;
							}
							if (!hasItem(player, 182206628))
							{
								if (ItemService.canAddItems(player, Collections.singletonList(new QuestItems(182206628, 1))))
									ItemService.addItem(player, 182206628, 1, 60 * 7200);
								else
									return true;
							}
							if (var < 7)
							{
								qs.setQuestVarById(1, 0); // clear killed Brohums
								qs.setQuestVarById(2, 0); // clear used Tears
								qs.setQuestVarById(0, 5);
								updateQuestStatus(env);
							}
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300190000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService.teleportTo(player, 300190000, newInstance.getInstanceId(), 198.82353f, 221.40387f, 1098.4879f, (byte) 41);
							return true;
						}
						else
							return sendQuestDialog(env, 2546);
					}
				case 34:
					if(var == 7)
					{
						if(QuestService.collectItemCheck(env, true))
						{
							long itemCount = player.getInventory().getItemCountByItemId(164000099);
							player.getInventory().removeFromBagByItemId(164000099, itemCount);
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return sendQuestDialog(env, 10000);
						}
						else
							return sendQuestDialog(env, 10001);
					}
			}
		}
		else if(targetId == 730008 && var == 11) // Daminu
		{
			switch(env.getDialogId())
			{
				case 26:
					return sendQuestDialog(env, 3398);
				case 10007:
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
			}
		}
		else if(targetId == 730019 && var == 12) // Lodas
		{
			switch(env.getDialogId())
			{
				case 26:
					return sendQuestDialog(env, 3739);
				case 10008:
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
			}
		}
		else if(targetId == 730024 && var == 13) // Trajanus
		{
			switch(env.getDialogId())
			{
				case 26:
					return sendQuestDialog(env, 4080);
				case 10009:
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
			}
		}

		
		return false;
	}
}
