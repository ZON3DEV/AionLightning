/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openaion.gameserver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.ai.npcai.DummyAi;
import org.openaion.gameserver.configs.main.LegionConfig;
import org.openaion.gameserver.controllers.attack.AttackStatus;
import org.openaion.gameserver.dao.SpawnDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.ShoutEventType;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.alliance.PlayerAlliance;
import org.openaion.gameserver.model.alliance.PlayerAllianceMember;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.QuestStateList;
import org.openaion.gameserver.model.gameobjects.player.RequestResponseHandler;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.model.templates.TradeListTemplate;
import org.openaion.gameserver.model.templates.quest.NpcQuestData;
import org.openaion.gameserver.model.templates.teleport.TelelocationTemplate;
import org.openaion.gameserver.model.templates.teleport.TeleportLocation;
import org.openaion.gameserver.model.templates.teleport.TeleporterTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import org.openaion.gameserver.network.aion.serverpackets.SM_PET;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLASTIC_SURGERY;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_REPURCHASE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SELL_ITEM;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_TRADELIST;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.restrictions.RestrictionsManager;
import org.openaion.gameserver.services.CraftSkillUpdateService;
import org.openaion.gameserver.services.CubeExpandService;
import org.openaion.gameserver.services.GuildService;
import org.openaion.gameserver.services.LegionService;
import org.openaion.gameserver.services.NpcShoutsService;
import org.openaion.gameserver.services.RespawnService;
import org.openaion.gameserver.services.SiegeService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.services.TradeService;
import org.openaion.gameserver.services.WarehouseService;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.exceptionhandlers.exception_enums;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.world.WorldType;


/**
 * This class is for controlling Npc's
 * 
 * @author -Nemesiss-, ATracer (2009-09-29), Sarynth
 */
public class NpcController extends CreatureController<Npc>
{
	private static final Logger	log	= Logger.getLogger(NpcController.class);

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object, isOutOfRange);
		if(object instanceof Creature)
			getOwner().getAggroList().remove((Creature) object);
		if(object instanceof Player || object instanceof Summon)
			getOwner().getAi().handleEvent(Event.NOT_SEE_PLAYER);			
	}

	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		Npc owner = getOwner();
		owner.getAi().handleEvent(Event.SEE_CREATURE);
		if(object instanceof Player)
		{
			owner.getAi().handleEvent(Event.SEE_PLAYER);		
			if(owner.hasWalkRoutes())
				PacketSendUtility.sendPacket((Player) object, new SM_EMOTION(owner, EmotionType.START_EMOTE2));
			NpcShoutsService.getInstance().handleEvent(owner, (Player)object, ShoutEventType.SEEUSER);
		}	
		else if(object instanceof Summon)
		{
			owner.getAi().handleEvent(Event.SEE_PLAYER);
		}
	}

	@Override
	public void onRespawn()
	{
		super.onRespawn();

		cancelTask(TaskId.DECAY);

		Npc owner = getOwner();

		if (owner != null && owner.isCustom())
		{
			DAOManager.getDAO(SpawnDAO.class).setSpawned(owner.getSpawn().getSpawnId(),owner.getObjectId(), true);
		}

		//set state from npc templates
		if(owner.getObjectTemplate().getState() != 0)
			owner.setState(owner.getObjectTemplate().getState());
		else
			owner.setState(CreatureState.NPC_IDLE);

		owner.getLifeStats().setCurrentHpPercent(100);
		owner.getAi().handleEvent(Event.RESPAWNED);

		if(owner.getSpawn().getNpcFlyState() != 0)
		{
			owner.setState(CreatureState.FLYING);
		}
		
	}

	public void onDespawn(boolean forced)
	{
		if(forced)
			cancelTask(TaskId.DECAY);

		Npc owner = getOwner();

		if(owner == null || !owner.isSpawned())
			return;

		if (owner != null && owner.isCustom())
		{
			DAOManager.getDAO(SpawnDAO.class).setSpawned(owner.getSpawn().getSpawnId(), owner.getSpawn().isNoRespawn(1)?-1:owner.getObjectId(), false);
		}

		owner.getAi().handleEvent(Event.DESPAWN);
		
		if(owner.getTarget() != null && owner.getTarget() instanceof Creature)
		{
			NpcShoutsService.getInstance().handleEvent(owner, (Creature)owner.getTarget(), ShoutEventType.DESPAWN);
		}
		
		World.getInstance().despawn(owner);
	}

	@Override
	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);

		Npc owner = getOwner();

		addTask(TaskId.DECAY, RespawnService.scheduleDecayTask(this.getOwner()));
		scheduleRespawn();

		PacketSendUtility.broadcastPacket(owner,
			new SM_EMOTION(owner, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()));

		// Monster Controller overrides this method.
		this.doReward();
		
		if((owner.getWorldType() == WorldType.ABYSS || owner.getWorldType() == WorldType.BALAUREA) && lastAttacker instanceof Player)
		{
			int relatedLocId = SiegeService.getInstance().getSiegeNpcLocation(owner.getObjectId());
			if(relatedLocId > 0)
			{
				AionObject winner = getOwner().getAggroList().getMostDamage();
				if(winner != null)
				{
					ArrayList<Player> toReward = new ArrayList<Player>();
					if (winner instanceof PlayerAlliance)
					{
						PlayerAlliance alliance = (PlayerAlliance)winner;
						for(PlayerAllianceMember m : alliance.getMembers())
						{
							if(m.getPlayer().getWorldType() == WorldType.ABYSS || m.getPlayer().getWorldType() == WorldType.BALAUREA)
								toReward.add(m.getPlayer());
						}
					}
					else if (winner instanceof PlayerGroup)
					{
						PlayerGroup group = (PlayerGroup)winner;
						for(Player p : group.getMembers())
						{
							if(p.getWorldType() == WorldType.ABYSS || p.getWorldType() == WorldType.BALAUREA)
								toReward.add(p);
						}
					}
					else if(winner instanceof Player)
					{
						Player p = (Player)winner;
						if(p.isInGroup() && p.getPlayerGroup() != null)
						{
							for(Player pl : p.getPlayerGroup().getMembers())
							{
								if(pl.getWorldType() == WorldType.ABYSS || pl.getWorldType() == WorldType.BALAUREA)
									toReward.add(pl);
							}
						}
						else
						{
							toReward.add(p);
						}
					}
					for(Player trp : toReward)
					{
						if(!trp.isOnline() || trp.getLifeStats().isAlreadyDead())
							continue;
						// Do some AP reward and record it
						trp.getCommonData().addAp(10);
						SiegeService.getInstance().recordAPGain(relatedLocId, trp.getObjectId(), 10);
					}
				}
			}
		}
		
		owner.getAi().handleEvent(Event.DIED);
		
		if(lastAttacker != null && lastAttacker instanceof Creature)
		{
			NpcShoutsService.getInstance().handleEvent(owner, (Creature)lastAttacker, ShoutEventType.DIE);
		}

		// deselect target at the end
		owner.setTarget(null);
		PacketSendUtility.broadcastPacket(owner, new SM_LOOKATOBJECT(owner));
	}

	@Override
	public Npc getOwner()
	{
		return (Npc) super.getOwner();
	}

	@Override
	public void onDialogRequest(Player player)
	{
		if(player.isTrading())
		{
			log.warn("[AUDIT] Trying to use trade exploit: " + player.getName());
			return;
		}

		getOwner().getAi().handleEvent(Event.TALK);

		if(QuestEngine.getInstance().onDialog(new QuestCookie(getOwner(), player, 0, -1)))
			return;
		
		// Zephyr Deliveryman
		if(getOwner().getObjectId() == player.getZephyrObjectId())
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 18));
			return;
		}

		int titleId = getOwner().getObjectTemplate().getTitleId();
		if
		(
			(
				// title ids of npcs
				titleId == 315018
				|| titleId == 350474 
				|| titleId == 350473 
				|| titleId == 350212 
				|| titleId == 350304 
				|| titleId == 350305
				|| titleId == 370000
				|| titleId == 370003
				// aerolinks
				|| (getOwner().getNpcId() >= exception_enums.NPC_TELEPORT_AERO_RANGE_I && getOwner().getNpcId() <= exception_enums.NPC_TELEPORT_AERO_RANGE_II && getOwner().getNpcId() != exception_enums.NPC_TELEPORT_REGULAR_ASMO_I)
				// balaurea gates
				|| (getOwner().getNpcId() >= 730296 && getOwner().getNpcId() <= 730303)
				|| getOwner().getNpcId() == 730428 // Marchutan Priory => Gelkmaros
				|| getOwner().getNpcId() == 205322 // Gelkmaros		   => Marchutan Priory
				|| getOwner().getNpcId() == 730427 // Marchutan Priory => Pandaemonium
				|| getOwner().getNpcId() == 730424 // Kaisinel Academy => Sanctum
				|| getOwner().getNpcId() == 730425 // Kaisinel Academy => Inggison
				|| getOwner().getNpcId() == 205314 // Inggison         => Kaisinel Academy
			)
		)
		{			
			NpcQuestData npcQD = QuestEngine.getInstance().getNpcQuestData(getOwner().getNpcId());
			QuestStateList list = player.getQuestStateList();
			List<Integer> events = npcQD.getOnTalkEvent();
			boolean hasQuestFromNpc = false;
			for(int e : events)
			{
				QuestState qs = list.getQuestState(e);
				if(qs != null && qs.getStatus() != QuestStatus.COMPLETE)
				{
					hasQuestFromNpc = true;
					break;
				}
				else
				{
					continue;
				}
			}
			if(hasQuestFromNpc)
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 10));
			else
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
			
		}
		else
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 10));
	}

	/**
	 * This method should be called to make forced despawn of NPC and delete it from the world
	 */
	public void onDelete()
	{
		if(getOwner().isInWorld())
		{
			this.getOwner().getAi().clearDesires();
			this.onDespawn(true);
			this.delete();
		}
	}

	/**
	 * Handle dialog
	 */
	@Override
	public void onDialogSelect(int dialogId, final Player player, int questId)
	{

		Npc npc = getOwner();
		int targetObjectId = npc.getObjectId();

		if(QuestEngine.getInstance().onDialog(new QuestCookie(npc, player, questId, dialogId)))
			return;

		//Eracus Temple, Check if the player did the quest to enter. Teleport: Meneus
		if (dialogId == 30 && (npc.getNpcId() == 203981))
		{
			int completedquestid = 0;
			completedquestid = 1346;
			QuestState qstel = player.getQuestStateList().getQuestState(completedquestid);
			if (qstel == null || qstel.getStatus() != QuestStatus.COMPLETE)
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
				return;
			}
			TeleportService.teleportTo(player, 210020000, 1, 439.3f, 422.2f, 274.3f, 0);
				return;
		}

		switch(dialogId)
		{
			case 2:
				// buy
				TradeListTemplate tradeList = TradeService.getTradeListData().getTradeListTemplate(npc.getNpcId());
				if (tradeList == null)
					log.warn("Missing tradelist for NPC : " + npc.getNpcId());
				else
					PacketSendUtility.sendPacket(player, new SM_TRADELIST(npc, tradeList, player.getPrices().getVendorBuyModifier()));
				break;
			case 3:
				// sell
				PacketSendUtility.sendPacket(player, new SM_SELL_ITEM(targetObjectId, player.getPrices().getVendorSellModifier(player.getCommonData().getRace())));
				break;
			case 4:
				// stigma
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1));
				break;
			case 5:
				// create legion
				if(MathUtil.isInRange(npc, player, 10)) // avoiding exploit with sending fake dialog_select packet
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 2));
				}
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.LEGION_CREATE_TOO_FAR_FROM_NPC());
				}
				break;
			case 6:
				// disband legion
				if(MathUtil.isInRange(npc, player, 10)) // avoiding exploit with sending fake dialog_select packet
				{
					LegionService.getInstance().requestDisbandLegion(npc, player);
				}
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.LEGION_DISPERSE_TOO_FAR_FROM_NPC());
				}
				break;
			case 7:
				// recreate legion
				if(MathUtil.isInRange(npc, player, 10)) // avoiding exploit with sending fake dialog_select packet
				{
					LegionService.getInstance().recreateLegion(npc, player);
				}
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.LEGION_DISPERSE_TOO_FAR_FROM_NPC());
				}
				break;
			case 20:
			    // TODO this is 2.1 warehouse
			    // moved to 21 in 2.5, so 20 should be something new
			    break;
			case 21:
				// warehouse
				if(MathUtil.isInRange(npc, player, 10)) // avoiding exploit with sending fake dialog_select packet
				{
					if(!RestrictionsManager.canUseWarehouse(player))
						return;
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 26));
					WarehouseService.sendWarehouseInfo(player, true);
				}
				break;
			case 26:
				// TODO hotfix to prevent opening the legion wh when a quest returns false.
				break;
			case 28:
				// trade broker
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 13));
				break;
			case 30:
				// soul healing
				final long expLost = player.getCommonData().getExpRecoverable();
				if (expLost == 0)
				player.getEffectController().removeEffect(8291);
				final double factor = (expLost < 1000000 ?
					0.25 - (0.00000015 * expLost) 
					: 0.1);
				final int price = (int) (expLost * factor);

				RequestResponseHandler responseHandler = new RequestResponseHandler(npc){
					@Override
					public void acceptRequest(Creature requester, Player responder)
					{
						if(player.getInventory().getKinahItem().getItemCount() >= price)
						{
							if(!player.getInventory().decreaseKinah(price))
								return;
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXP(String.valueOf(expLost)));
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.SOUL_HEALED());							
							player.getCommonData().resetRecoverableExp();							
							player.getEffectController().removeEffect(8291);
						}
						else
						{
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.NOT_ENOUGH_KINAH(price));
						}
					}

					@Override
					public void denyRequest(Creature requester, Player responder)
					{
						// no message
					}
				};
				if(player.getCommonData().getExpRecoverable() > 0)
				{
					boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_SOUL_HEALING,
						responseHandler);
					if(result)
					{
						PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(
							SM_QUESTION_WINDOW.STR_SOUL_HEALING, 0, String.valueOf(price)
						));
					}
				}
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.DONT_HAVE_RECOVERED_EXP());
				}
				break;
			case 31:
				// arena teleporter
				switch(npc.getNpcId())
				{
					case exception_enums.NPC_ARENA_TELEPORT_ASMO_IN:
						player.setInArena(true);
						TeleportService.teleportTo(player, 120010000, 1, 984f, 1543f, 222.1f, 0);
						break;
					case exception_enums.NPC_ARENA_TELEPORT_ELYOS_IN:
						player.setInArena(true);
						TeleportService.teleportTo(player, 110010000, 1, 1462.5f, 1326.1f, 564.1f, 0);
						break;
					case exception_enums.NPC_AREA_TELEPORT_ERACUS_IN:
						// TODO : move me to portal controller so I can be quest restricted
						TeleportService.teleportTo(player, 210020000, 1, 439.3f, 422.2f, 274.3f, 0);
						break;
				}
				break;
			case 32:
				// arena teleporter
				switch(npc.getNpcId())
				{
					case exception_enums.NPC_ARENA_TELEPORT_ASMO_OUT:
						player.setInArena(true);
						TeleportService.teleportTo(player, 120010000, 1, 1005.1f, 1528.9f, 222.1f, 0);
						break;
					case exception_enums.NPC_ARENA_TELEPORT_ELYOS_OUT:
						player.setInArena(true);
						TeleportService.teleportTo(player, 110010000, 1, 1470.3f, 1343.5f, 563.7f, 21);
						break;
					case exception_enums.NPC_AREA_TELEPORT_ERACUS_OUT:
						TeleportService.teleportTo(player, 210020000, 1, 446.2f, 431.1f, 274.5f, 0);
						break;
				}
				break;
			case 36:
				// Godstone socketing
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 21));
				break;
			case 37:
				// remove mana stone
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 20));
				break;
			case 38:
				// modify appearance
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 19));
				break;
			case 39:
				// flight and teleport
				TeleportService.showMap(player, targetObjectId, npc.getNpcId());
				break;
			case 40:
				// Essencetapping
				CraftSkillUpdateService.getInstance().learnSkill(player, npc);
				break;
			case 41:
				// learn tailoring armor smithing etc...
				CraftSkillUpdateService.getInstance().learnSkill(player, npc);
				break;
			case 42:
				// expand cube
				CubeExpandService.expandCube(player, npc);
				break;
			case 43:
				// expand warehouse
				WarehouseService.expandWarehouse(player, npc);
				break;
			case 48:
				// legion warehouse
				if(LegionConfig.LEGION_WAREHOUSE)
					if(MathUtil.isInRange(npc, player, 10)) // avoiding exploit with sending fake dialog_select packet
						LegionService.getInstance().openLegionWarehouse(player);
				break;
			case 50:
				// WTF??? Quest dialog packet
				break;
			case 53:
				if(MathUtil.isInRange(npc, player, 10)) // avoiding exploit with sending fake dialog_select packet
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 28));
				}
				break;
			case 54:
				// coin reward
				QuestEngine.getInstance().onDialog(new QuestCookie(npc, player, questId, 26));
				break;
			case 56:
				//Plastic Surgery
				if (player.getInventory().getItemCountByItemId(169650000) > 0 || player.getInventory().getItemCountByItemId(169650001) > 0)
					PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, true, false));  
				else
					PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, false, false));  
				player.setEditMode(true);
				break;
			case 57:
				//Gender Switch
				if (player.getInventory().getItemCountByItemId(169660000) > 0 || player.getInventory().getItemCountByItemId(169660001) > 0)
					PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, true, true));  
				else
					PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, false, true));  
				player.setEditMode(true);
				break;
			case 58:
				// dredgion npc
				break;	
			case 61:
				// armsfusion
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 29));
				break;
			case 62:
				// armsbreaking
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 30));
				break;
			case 63:
				// join guild
				GuildService.getInstance().enterGuild(player, npc);
				break;
			case 64:
				// leave guild
				GuildService.getInstance().exitGuild(player, npc);
				break;
			case 65:
				// repurchase
				PacketSendUtility.sendPacket(player, new SM_REPURCHASE(npc, player));
				break;
			case 66:
				// adopt pet
				PacketSendUtility.sendPacket(player, new SM_PET(6));
				break;
			case 67:
				// surrender pet
				PacketSendUtility.sendPacket(player, new SM_PET(7));
				break;
			case 68:
				// housing build
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 32));
				break;
			case 69:
				// housing destruct
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 33));
				break;
			case 70:
				// condition an individual item
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 35));
				break;
			case 71:
				// condition all equiped items 
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 35));
				break;
			case 10000:
				// generic npc reply (most are teleporters)
				TeleporterTemplate template = DataManager.TELEPORTER_DATA.getTeleporterTemplate(npc.getNpcId());
				if(template != null)
				{
					TeleportLocation loc = template.getTeleLocIdData().getTelelocations().get(0);
					if(loc != null)
					{

						if( template.getRace() != null && !player.getCommonData().getRace().equals(template.getRace()) )
						{
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MOVE_PORTAL_ERROR_INVALID_RACE);
								return;
						}
						
						if(!player.getInventory().decreaseKinah(loc.getPrice()))
							return;
						
						TelelocationTemplate tlt = DataManager.TELELOCATION_DATA.getTelelocationTemplate(loc.getLocId());
						TeleportService.teleportTo(player, tlt.getMapId(), tlt.getX(), tlt.getY(), tlt.getZ(), 1000);
					}
				}
				else if(getOwner().getNpcId() >= 730296 && getOwner().getNpcId() <= 730303){
					double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
					int worldId = getOwner().getWorldId();
					float x = (float)(player.getX() + (10 * Math.cos(radian)));
					float y = (float)(player.getY() + (10 * Math.sin(radian)));
					float z = player.getZ() + 0.2f;
					TeleportService.teleportTo(player, worldId, x, y, z, 1);
				}
				// Kaisinel Academy => Sanctum
                else if(getOwner().getNpcId() == 730424)
                    TeleportService.teleportTo(player, 110010000, 1366.8f, 1539.5f, 569.0f, 1000);
				// Kaisinel Academy => Inggison
                else if(getOwner().getNpcId() == 730425)
                {
                    if(player.getInventory().getKinahCount() < 9300)
                    {
                        PacketSendUtility.sendMessage(player, "You don't have enough kinah to travel to Inggison.");
                        return;
                    }
                    else if (player.getLevel() < 51)
				    {
				        PacketSendUtility.sendMessage(player, "You don't have the necesary level to travel to Inggison.");
                        return;
                    }
                    TeleportService.teleportTo(player, 210050000, 1335.0f, 276.0f, 589.3662f, 1000);
                }
                // Inggison => Kaisinel Academy
                else if(getOwner().getNpcId() == 205314)
                {
                    if(player.getInventory().getKinahCount() < 9300)
                    {
                        PacketSendUtility.sendMessage(player, "You don't have enough kinah to travel to Kaisinel Academy.");
                        return;
                    }
                    TeleportService.teleportTo(player, 110070000, 444.98f, 251.62f, 129.97f, 1000);
                }
				// Marchutan Priory => Pandaemonium
				else if(getOwner().getNpcId() == 730427)
				    TeleportService.teleportTo(player, 120010000, 1609.9f, 1390.7f, 193.1f, 1000);
				// Marchutan Priory => Gelkmaros
				else if(getOwner().getNpcId() == 730428)
				{
				    if(player.getInventory().getKinahCount() < 9300)
				    {
				        PacketSendUtility.sendMessage(player, "You don't have enough kinah to travel to Gelkmaros.");
				        return;
				    }
				    else if (player.getLevel() < 51)
				    {
				        PacketSendUtility.sendMessage(player, "You don't have the necesary level to travel to Gelkmaros.");
				        return;
				    }
                    TeleportService.teleportTo(player, 220070000, 1763.0f, 2911.0f, 554.7982f, 1000);
				}
				// Gelkmaros => Marchutan Priory
				else if(getOwner().getNpcId() == 205322)
				{
				    if(player.getInventory().getKinahCount() < 9300)
				    {
				        PacketSendUtility.sendMessage(player, "You don't have enough kinah to travel to Gelkmaros.");
				        return;
				    }
                    TeleportService.teleportTo(player, 120080000, 548.65f, 200.5f, 93.48f, 1000);
				}
				else
				{
				    log.warn("No action defined for dialogId 10000 from npcId " + getOwner().getNpcId());				
				}				
				break;
			default:
				if(questId > 0)
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, dialogId, questId));
				else
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, dialogId));
				break;
			case 10001:
				//arena lobby 2.5
				switch(npc.getNpcId())
				{
					case 730265:
						TeleportService.teleportTo(player, 110070000, 1, 444.98f, 251.62f, 129.97f, 0);
						break;
					case 730268:
						TeleportService.teleportTo(player, 120080000, 1, 548.65f, 200.5f, 93.48f, 31);
						break;
				}
		}
	}

	@Override
	public void onAttack(final Creature creature, int skillId, TYPE type, int damage, int logId, AttackStatus status, boolean notifyAttackedObservers, boolean sendPacket)
	{
		if(getOwner().getLifeStats().isAlreadyDead())
			return;

		super.onAttack(creature, skillId, type, damage, logId, status, notifyAttackedObservers, sendPacket);

		Npc npc = getOwner();

		Creature actingCreature = creature.getActingCreature();
		if(actingCreature instanceof Player)
			if(QuestEngine.getInstance().onAttack(new QuestCookie(npc, (Player) actingCreature, 0, 0)))
				return;

		AI<?> ai = npc.getAi();
		if(ai instanceof DummyAi)
		{
			log.warn("CHECKPOINT: npc attacked without ai " + npc.getObjectTemplate().getTemplateId());
			return;
		}
		
		getOwner().getKnownList().doOnAllNpcs(new Executor<Npc>(){
			@Override
			public boolean run(Npc tmp)
			{
				if (getOwner().isSupportFrom(tmp) && MathUtil.isInRange(getOwner(), tmp, 10))
				{
					if (getOwner().getWorldId() != 300100000 && (Math.abs(getOwner().getZ() - tmp.getZ()) < 3))
						tmp.getAggroList().addHate(creature, 1);
				}
				return true;
			}
		}, true);
		
		NpcShoutsService.getInstance().handleEvent(npc, creature, ShoutEventType.WOUNDED);
		
		if(npc.getLifeStats().getHpPercentage() <= 35)
		{
			NpcShoutsService.getInstance().handleEvent(npc, creature, ShoutEventType.ATK);
		}
		
	}

	@Override
	public void onStartMove()
	{
		super.onStartMove();
	}

	@Override
	public void onMove()
	{
		super.onMove();
	}

	@Override
	public void onStopMove()
	{
		super.onStopMove();
	}

	/**
	 * Schedule respawn of npc
	 * In instances - no npc respawn
	 */
	public void scheduleRespawn()
	{	
		if(getOwner().isInInstance())
			return;

		int instanceId = getOwner().getInstanceId();
		if(!getOwner().getSpawn().isNoRespawn(instanceId))
		{
			Future<?> respawnTask = RespawnService.scheduleRespawnTask(getOwner());
			addTask(TaskId.RESPAWN, respawnTask);
		}
	}
	public void onCreatureDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);
	}
}