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

import java.util.Collections;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.controllers.SummonController.UnsummonType;
import org.openaion.gameserver.controllers.attack.AttackStatus;
import org.openaion.gameserver.controllers.instances.FortressInstanceTimer;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.ShoutEventType;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.alliance.PlayerAllianceEvent;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Gatherable;
import org.openaion.gameserver.model.gameobjects.GroupGate;
import org.openaion.gameserver.model.gameobjects.Kisk;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.StaticObject;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.Trap;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.SkillListEntry;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.state.CreatureVisualState;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.group.GroupEvent;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.model.templates.stats.PlayerStatsTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_GATHERABLE_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_KISK_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_LEVEL_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_NEARBY_QUESTS;
import org.openaion.gameserver.network.aion.serverpackets.SM_NPC_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_PET;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_PRIVATE_STORE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SKILL_CANCEL;
import org.openaion.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import org.openaion.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_SUMMON_PANEL;
import org.openaion.gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.restrictions.RestrictionsManager;
import org.openaion.gameserver.services.AllianceService;
import org.openaion.gameserver.services.ArenaService;
import org.openaion.gameserver.services.ClassChangeService;
import org.openaion.gameserver.services.DredgionInstanceService;
import org.openaion.gameserver.services.DuelService;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.LegionService;
import org.openaion.gameserver.services.NpcShoutsService;
import org.openaion.gameserver.services.PvpService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.SkillLearnService;
import org.openaion.gameserver.services.ToyPetService;
import org.openaion.gameserver.services.ZoneService;
import org.openaion.gameserver.services.ZoneService.ZoneUpdateMode;
import org.openaion.gameserver.skill.SkillEngine;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.skill.model.Skill.SkillType;
import org.openaion.gameserver.skill.properties.FirstTargetAttribute;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.task.impl.PacketBroadcaster.BroadcastMode;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.world.WorldMapInstance;
import org.openaion.gameserver.world.WorldType;
import org.openaion.gameserver.world.zone.ZoneInstance;


/**
 * This class is for controlling players.
 * 
 * @author -Nemesiss-, ATracer (2009-09-29), blakawk, Sarynth
 * @author RotO (Attack-speed hack protection)
 */
public class PlayerController extends CreatureController<Player>
{
	private boolean			isInShutdownProgress = false;
	
	private boolean			canAutoRevive = true;

	/**
	 * Zone update mask
	 */
	private volatile byte	zoneUpdateMask;

	private long lastAttackMilis = 0;
	private long lastSkillMilis = 0;
	private int lastSkillAnimationTime = 0;
	
	private static Logger	log	= Logger.getLogger(PlayerController.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		if(object instanceof Player)
		{
			Player player = (Player)object;
			PacketSendUtility.sendPacket(getOwner(), new SM_PLAYER_INFO(player, getOwner().isEnemyPlayer((Player)object)));
			if(player.getToyPet() != null)
			{
				Logger.getLogger(PlayerController.class).debug("Player " + getOwner().getName() + " sees " + object.getName() + " that has toypet");
				PacketSendUtility.sendPacket(getOwner(), new SM_PET(3, player.getToyPet()));
			}
			getOwner().getEffectController().sendEffectIconsTo((Player) object);
			} else if (object instanceof Trap) {
            Trap trap = (Trap)object;
            PacketSendUtility.sendPacket(getOwner(), new SM_NPC_INFO(trap));
		}
		else if (object instanceof Kisk)
		{
			Kisk kisk = ((Kisk) object);
			PacketSendUtility.sendPacket(getOwner(), new SM_NPC_INFO(getOwner(), kisk));
			if (getOwner().getCommonData().getRace() == kisk.getOwnerRace())
				PacketSendUtility.sendPacket(getOwner(), new SM_KISK_UPDATE(kisk));
		}
		else if (object instanceof GroupGate)
		{
			GroupGate groupgate = ((GroupGate) object);
			PacketSendUtility.sendPacket(getOwner(), new SM_NPC_INFO(getOwner(), groupgate));
		}
		else if(object instanceof Npc)
		{
			boolean update = false;
			Npc npc = ((Npc) object);

			PacketSendUtility.sendPacket(getOwner(), new SM_NPC_INFO(npc, getOwner()));

			for(int questId : QuestEngine.getInstance().getNpcQuestData(npc.getNpcId()).getOnQuestStart())
			{
				if(QuestService.checkNearBy(new QuestCookie(object, getOwner(), questId, 0), 2))
				{
					if(!getOwner().getNearbyQuests().contains(questId))
					{
						update = true;
						getOwner().getNearbyQuests().add(questId);
					}
				}
			}
			
			if(update)
				updateNearbyQuestList();

			if(npc.getNpcId() == 206089) // Siel's / Sulfur fortress instance event trigger 
			{
				WorldMapInstance instance = InstanceService.getRegisteredInstance(getOwner().getWorldId(), getOwner().getPlayerGroup().getGroupId());
				if(instance != null && instance.getTimerEnd() == null)
					FortressInstanceTimer.schedule(getOwner(), 900);
			}
			if(npc.getNpcId() == 206095 || npc.getNpcId() == 206096 || npc.getNpcId() == 206097) // Miren-Krotan-Kysis fortress instance event trigger
			{
				WorldMapInstance instance = InstanceService.getRegisteredInstance(getOwner().getWorldId(), getOwner().getPlayerGroup().getGroupId());
				if(instance != null && instance.getTimerEnd() == null)
					FortressInstanceTimer.schedule(getOwner(), 600);
			}
		}
		else if(object instanceof Summon)
		{
			Summon npc = ((Summon) object);		
			PacketSendUtility.sendPacket(getOwner(), new SM_NPC_INFO(npc));
		}
		else if(object instanceof Gatherable || object instanceof StaticObject)
		{
			PacketSendUtility.sendPacket(getOwner(), new SM_GATHERABLE_INFO(object));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object, isOutOfRange);
		if(object instanceof Npc)
		{
			boolean update = false;
			for(int questId : QuestEngine.getInstance().getNpcQuestData(((Npc) object).getNpcId()).getOnQuestStart())
			{
				if(QuestService.checkNearBy(new QuestCookie(object, getOwner(), questId, 0), 2))
				{
					if(getOwner().getNearbyQuests().contains(questId))
					{
						update = true;
						getOwner().getNearbyQuests().remove(getOwner().getNearbyQuests().indexOf(questId));
					}
				}
			}
			if(update)
				updateNearbyQuestList();
		}

		PacketSendUtility.sendPacket(getOwner(), new SM_DELETE(object, isOutOfRange ? 0 : 15));
	}

	public void updateNearbyQuests()
	{
		getOwner().getNearbyQuests().clear();
		
		getOwner().getKnownList().doOnAllNpcs(new Executor<Npc>(){
			@Override
			public boolean run(Npc obj)
			{
				for(int questId : QuestEngine.getInstance().getNpcQuestData(((Npc) obj).getNpcId()).getOnQuestStart())
				{
					if(QuestService.checkNearBy(new QuestCookie(obj, getOwner(), questId, 0), 2))
					{
						if(!getOwner().getNearbyQuests().contains(questId))
						{
							getOwner().getNearbyQuests().add(questId);
						}
					}
				}
				return true;
			}
		}, true);
		
		updateNearbyQuestList();
	}

	/**
	 * Will be called by ZoneManager when player enters specific zone
	 * 
	 * @param zoneInstance
	 */
	public void onEnterZone(ZoneInstance zoneInstance)
	{
        Player player = getOwner();
		QuestEngine.getInstance().onEnterZone(new QuestCookie(null, player, 0, 0), zoneInstance.getTemplate().getName());
		
        if (player.getFlyController().canFly(true) == false) {
                checkNoFly(player);
        }
	}

	/**
	 * Will be called by ZoneManager when player leaves specific zone
	 * 
	 * @param zoneInstance
	 */
	public void onLeaveZone(ZoneInstance zoneInstance)
	{

	}
	
    public void checkNoFly(final Player player)    {
        if(player.isInState(CreatureState.FLYING))
            player.getFlyController().endFly(); 
    }
	
	/**
	 * Set zone instance as null (Where no zones defined)
	 */
	public void resetZone()
	{
		getOwner().setZoneInstance(null);
	}

	public void onEnterWorld()
	{
		// Display Dark Poeta counter when entering 300040000
		if(getOwner().getWorldId() == 300040000 && !getOwner().getInDarkPoeta() && getOwner().getPlayerGroup() != null){
			PacketSendUtility.sendPacket(getOwner(), new SM_INSTANCE_SCORE(getOwner().getWorldId(), (int)((getOwner().getPlayerGroup().getInstanceStartTime() + 14400000) - System.currentTimeMillis()), 2097152, getOwner().getPlayerGroup().getGroupInstancePoints(), 0, 0, 7));
			getOwner().setInDarkPoeta(true);
		}
		// Remove Dark Poeta Counter on map change
		if(getOwner().getInDarkPoeta() && getOwner().getWorldId() != 300040000){
			PacketSendUtility.sendPacket(getOwner(), new SM_INSTANCE_SCORE(0, 14400000, 2097152, 0, 0, 0, 7));
			getOwner().setInDarkPoeta(true);
		}

		for (Effect ef : getOwner().getEffectController().getAbnormalEffects())
		{
			//remove abyss transformation if worldtype != abyss && worldtype != balaurea
			if (ef.isAvatar())
			{
				if (getOwner().getWorldType() != WorldType.ABYSS && 
					getOwner().getWorldType() != WorldType.BALAUREA ||
					getOwner().isInInstance())
				{
					getOwner().getEffectController().removeEffect(ef.getSkillId());
					getOwner().getEffectController().removeEffect(ef.getLaunchSkillId());
					break;
				}
			}
			//remove Instance Transformation if player is not in instance (Kromede, Taloc....)
			if (ef.getStack().contains("POLYMORPH_CROMEDE"))
			{
				if (getOwner().getWorldId() != 300230000)
				{
					getOwner().getEffectController().removeEffect(ef.getSkillId());
					break;					
				}
			}
			else if (ef.getStack().contains("SHAPE_IDELIM"))
			{
				if (getOwner().getWorldId() != 300190000)
				{
					getOwner().getEffectController().removeEffect(ef.getSkillId());
					break;					
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Should only be triggered from one place (life stats)
	 */
	@Override
	public void onDie(Creature lastAttacker)
	{		
		Player player = this.getOwner();
		
		Creature master = null;
		if(lastAttacker != null)
			master = lastAttacker.getMaster();
		
		if(master instanceof Player)
		{
			if(player.getInArena())
			{
				ArenaService.getInstance().onDie(player, lastAttacker);
				return;
			}
			if(isDueling((Player) master))
			{
				DuelService.getInstance().onDie(player);
				return;
			}
		}
		
		if(lastAttacker instanceof Npc)
		{
			NpcShoutsService.getInstance().handleEvent((Npc)lastAttacker, player, ShoutEventType.WIN);
		}
		
		this.doReward();
		
		// Effects removed with super.onDie()
		boolean hasSelfRezEffect = player.getReviveController().checkForSelfRezEffect(player) && canAutoRevive;
		
		if (player.isInState(CreatureState.FLYING) || player.isInState(CreatureState.GLIDING))
		{
			player.unsetState(CreatureState.FLYING);
			player.unsetState(CreatureState.GLIDING);
			player.setFlyState(0);
		}
		
		super.onDie(lastAttacker);
		
		if(master instanceof Npc || master == player)
		{
			if(player.getLevel() > 4 && !DredgionInstanceService.isDredgion(player.getWorldId()))
				player.getCommonData().calculateExpLoss();
		}
		
		/**
		 * Release summon
		 */
		Summon summon = player.getSummon();
		if(summon != null)
			summon.getController().release(UnsummonType.UNSPECIFIED);
		
		if(player.getToyPet() != null)
			ToyPetService.getInstance().dismissPet(player, player.getToyPet().getPetId());

		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, lastAttacker == null ? 0 :
			lastAttacker.getObjectId()), true);
		
		// SM_DIE Packet
		int kiskTimeRemaining = (player.getKisk() != null ? player.getKisk().getRemainingLifetime() : 0);
		boolean hasSelfRezItem = player.getReviveController().checkForSelfRezItem(player) && canAutoRevive;
		PacketSendUtility.sendPacket(player, new SM_DIE(hasSelfRezEffect, hasSelfRezItem, kiskTimeRemaining));
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.DIE);
		QuestEngine.getInstance().onDie(new QuestCookie(null, player, 0, 0));
		player.getObserveController().notifyDeath(player);
	}




	@Override
	public void doReward()
	{
		Player victim = getOwner();
		PvpService.getInstance().doReward(victim);
		
		// DP reward 
		// TODO: Figure out what DP reward should be for PvP
		//int currentDp = winner.getCommonData().getDp();
		//int dpReward = StatFunctions.calculateSoloDPReward(winner, getOwner());
		//winner.getCommonData().setDp(dpReward + currentDp);
		
	}
	
	@Override
	public void onRespawn()
	{
		if (hasTask(TaskId.SKILL_RESURRECT))
			cancelTask(TaskId.SKILL_RESURRECT, true);
		
		super.onRespawn();
		startProtectionActiveTask();
	}

	public void attackTarget(Creature target, int atknumber, int time, int attackType)
	{
		super.attackTarget(target, atknumber, time, attackType);
	}

	public void onAttack(Creature creature, int skillId, TYPE type, int damage, int logId, AttackStatus status, boolean notifyAttackedObservers, boolean sendPacket)
	{
		if(getOwner().getLifeStats().isAlreadyDead())
			return;

     	if(getOwner().isInvul())
			damage = 0;

		super.onAttack(creature, skillId, type, damage, logId, status, notifyAttackedObservers, sendPacket);
	}
	

	/**
	 * @param skillId
	 * @param targetType
	 * @param x
	 * @param y
	 * @param z
	 */
	public void useSkill(int skillId, int targetType, float x, float y, float z, int time)
	{
		Player player = getOwner();
		
		Skill skill = SkillEngine.getInstance().getSkillFor(player, skillId, player.getTarget());
		
		if(skill != null)
		{
			if (skill.getSkillTemplate() == null || skill.getSkillTemplate().isPassive())
				return;	
			
			skill.setTargetType(targetType, x, y, z);
			skill.setTime(time);
			if(!RestrictionsManager.canUseSkill(player, skill))
				return;
			
			if (player.getSummon() != null)
			{
				int cooldown = skill.getSkillTemplate().getCooldown();
				player.getSummon().getController().setOrderSkillId(skillId);
				player.getSummon().getController().setOrderSkillCooldown(cooldown);
			}
			
			skill.useSkill();
		}
		
		skill = null;
	}
	
	@Override
	public void onMove()
	{
		super.onMove();
		addZoneUpdateMask(ZoneUpdateMode.ZONE_UPDATE);
	}

	@Override
	public void onStopMove()
	{
		cancelCurrentSkill();
		super.onStopMove();
	}

	@Override
	public void onStartMove()
	{
		cancelCurrentSkill();
		super.onStartMove();
	}
	
	/**
	* Perform tasks on Player jumping
	*/
	public void onJump()
	{
		getOwner().getObserveController().notifyJumpObservers();
	}
	
	
	/**
	 * Cancel current skill and remove cooldown
	 */
	@Override
	public void cancelCurrentSkill()
	{
		Player player = getOwner();
		Skill castingSkill = player.getCastingSkill();
		if(castingSkill != null)
		{
			int skillId = castingSkill.getSkillTemplate().getSkillId();
			castingSkill.cancelCast();
			player.removeSkillCoolDown(castingSkill.getSkillTemplate().getDelayId());
			player.setCasting(null);
			if (castingSkill.getSkillType() == SkillType.ITEM)
			{
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), castingSkill.getFirstTarget().getObjectId(), castingSkill.getItemObjectId(), castingSkill.getItemTemplate().getTemplateId(), 0, 3, 0));
				getOwner().removeItemCoolDown(castingSkill.getItemTemplate().getDelayId());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED());
			}
			else
			{	
				PacketSendUtility.sendPacket(player, new SM_SKILL_CANCEL(player, skillId));
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CANCELED());
			}
		}	
	}

	/**
	 * 
	 */
	public void updatePassiveStats()
	{
		Player player = getOwner();
		for(SkillListEntry skillEntry : player.getSkillList().getAllSkills())
		{
			Skill skill = SkillEngine.getInstance().getSkillFor(player, skillEntry.getSkillId(), player.getTarget());
			if(skill != null && skill.isPassive())
			{
				skill.useSkill();
			}
		}
	}

	@Override
	public Player getOwner()
	{
		return (Player) super.getOwner();
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public boolean isDueling(Player player)
	{
		return DuelService.getInstance().isDueling(player.getObjectId(), getOwner().getObjectId());
	}

	public void updateNearbyQuestList()
	{
		getOwner().addPacketBroadcastMask(BroadcastMode.UPDATE_NEARBY_QUEST_LIST);
	}

	public void updateNearbyQuestListImpl()
	{
		PacketSendUtility.sendPacket(getOwner(), new SM_NEARBY_QUESTS(getOwner().getNearbyQuests()));
	}

	public boolean isInShutdownProgress()
	{
		return isInShutdownProgress;
	}

	public void setInShutdownProgress(boolean isInShutdownProgress)
	{
		this.isInShutdownProgress = isInShutdownProgress;
	}

	/**
	 * Handle dialog
	 */
	@Override
	public void onDialogSelect(int dialogId, Player player, int questId)
	{
		switch(dialogId)
		{
			case 2:
				PacketSendUtility.sendPacket(player, new SM_PRIVATE_STORE(getOwner().getStore()));
				break;
		}
	}

	/**
	 * @param level
	 */
	public void upgradePlayer(int level)
	{
		Player player = getOwner();

		PlayerStatsTemplate statsTemplate = DataManager.PLAYER_STATS_DATA.getTemplate(player);
		player.setPlayerStatsTemplate(statsTemplate);
		
		// update stats after setting new template
		player.getGameStats().doLevelUpgrade();
		player.getLifeStats().synchronizeWithMaxStats();
		player.getLifeStats().updateCurrentStats();
		
		PacketSendUtility.broadcastPacket(player, new SM_LEVEL_UPDATE(player.getObjectId(), 0, level), true);

		// Temporal
		ClassChangeService.showClassChangeDialog(player);

		QuestEngine.getInstance().onLvlUp(new QuestCookie(null, player, 0, 0));
		updateNearbyQuests();
		
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));

		if(level == 10 && player.getSkillList().getSkillEntry(30001) != null)
		{
			int skillLevel = player.getSkillList().getSkillLevel(30001);
			player.getSkillList().removeSkill(30001);
			PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player));
			player.getSkillList().addSkill(player, 30002, skillLevel, true);
		}
		// add new skills
		SkillLearnService.addNewSkills(player, false);
		
		/**
		 * Automatic adv.stigmas
		 * level	slots
		 * 45		2
		 * 50		3
		 * 52		4
		 * 55		5		
		 */
		if (CustomConfig.ADVSTIGMA_ONLVLUP)
		{
			switch(level)
			{
				case 45:
					if (player.getCommonData().getAdvencedStigmaSlotSize() != 2)
						player.getCommonData().setAdvencedStigmaSlotSize(2);
					break;
				case 50:
					if (player.getCommonData().getAdvencedStigmaSlotSize() != 3)
						player.getCommonData().setAdvencedStigmaSlotSize(3);
					break;
				case 52:
					if (player.getCommonData().getAdvencedStigmaSlotSize() != 4)
						player.getCommonData().setAdvencedStigmaSlotSize(4);
					break;
				case 55:
					if (player.getCommonData().getAdvencedStigmaSlotSize() != 5)
						player.getCommonData().setAdvencedStigmaSlotSize(5);
					break;
			}
		}
		
		/**
		 * Broadcast Update to all that may care.
		 */
		if (player.isInGroup())
			player.getPlayerGroup().updateGroupUIToEvent(player, GroupEvent.UPDATE);
		if (player.isInAlliance())
			AllianceService.getInstance().updateAllianceUIToEvent(player, PlayerAllianceEvent.UPDATE);
		if(player.isLegionMember())
			LegionService.getInstance().updateMemberInfo(player);
	}

	/**
	 * After entering game player char is "blinking" which means that it's in under some protection, after making an
	 * action char stops blinking. - Starts protection active - Schedules task to end protection
	 */
	public void startProtectionActiveTask()
	{
		getOwner().setVisualState(CreatureVisualState.BLINKING);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_PLAYER_STATE(getOwner()), true);
		Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
				stopProtectionActiveTask();
			}
		}, 60000);
		addTask(TaskId.PROTECTION_ACTIVE, task);
	}

	/**
	 * Stops protection active task after first move or use skill
	 */
	public void stopProtectionActiveTask()
	{
		cancelTask(TaskId.PROTECTION_ACTIVE);
		Player player = getOwner();
		if(player != null && player.isSpawned())
		{
			player.unsetVisualState(CreatureVisualState.BLINKING);
			PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
		}
	}

	/**
	 * When player arrives at destination point of flying teleport
	 */
	public void onFlyTeleportEnd()
	{
		Player player = getOwner();
		player.unsetState(CreatureState.FLIGHT_TELEPORT);
		player.setFlightTeleportId(0);
		player.setFlightDistance(0);
		player.setState(CreatureState.ACTIVE);
		addZoneUpdateMask(ZoneUpdateMode.ZONE_REFRESH);
	}

	/**
	 * Zone update mask management
	 * 
	 * @param mode
	 */
	public final void addZoneUpdateMask(ZoneUpdateMode mode)
	{
		zoneUpdateMask |= mode.mask();
		ZoneService.getInstance().add(getOwner());
	}

	public final void removeZoneUpdateMask(ZoneUpdateMode mode)
	{
		zoneUpdateMask &= ~mode.mask();
	}

	public final byte getZoneUpdateMask()
	{
		return zoneUpdateMask;
	}

	/**
	 * Update zone taking into account the current zone
	 */
	public void updateZoneImpl()
	{
		ZoneService.getInstance().checkZone(getOwner());
	}

	/**
	 * Refresh completely zone irrespective of the current zone
	 */
	public void refreshZoneImpl()
	{
		ZoneService.getInstance().findZoneInCurrentMap(getOwner());
	}

	/**
	 * 
	 */
	public void ban()
	{
		// sp.getTeleportService().teleportTo(this.getOwner(), 510010000, 256f, 256f, 49f, 0);
	}

	/**
	 * Check water level (start drowning) and map death level (die)
	 */
	public void checkWaterLevel()
	{
		Player player = getOwner();
		World world = World.getInstance();
		float z = player.getZ();
		
		if(player.getLifeStats().isAlreadyDead())
			return;
		
		if(z < world.getWorldMap(player.getWorldId()).getDeathLevel())
		{
			die();
			return;
		}
		
		ZoneInstance currentZone = player.getZoneInstance();
		if(currentZone != null && currentZone.isBreath())
			return;
		
		//TODO need fix character height
		float playerheight = player.getPlayerAppearance().getHeight() * 1.6f;
		if(z < world.getWorldMap(player.getWorldId()).getWaterLevel() - playerheight)
			ZoneService.getInstance().startDrowning(player);
		else
			ZoneService.getInstance().stopDrowning(player);
	}

	@Override
	public void createSummon(int npcId, int skillLvl)
	{
		Player master = getOwner();
		
		if (master.getSummon() != null) //check to avoid spawns of multiple summons
			return;
		
		Summon summon = SpawnEngine.getInstance().spawnSummon(master, npcId, skillLvl);
		master.setSummon(summon);
		summon.getObjectTemplate().getStatsTemplate().setFlySpeed(master.getGameStats().getCurrentStat(StatEnum.FLY_SPEED));
		summon.getObjectTemplate().getStatsTemplate().setRunSpeed(master.getGameStats().getCurrentStat(StatEnum.SPEED));
		PacketSendUtility.sendPacket(master, new SM_SUMMON_PANEL(summon));
		PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, EmotionType.START_EMOTE2));
		PacketSendUtility.broadcastPacket(summon, new SM_SUMMON_UPDATE(summon));
	}
	
	public boolean addItems(int itemId, int count)
	{
		return ItemService.addItems(getOwner(), Collections.singletonList(new QuestItems(itemId, count)));
	}

	public void setCanAutoRevive (boolean canAutoRevive)
	{
		this.canAutoRevive = canAutoRevive;
	}
	
	public boolean getCanAutoRevive()
	{
		return this.canAutoRevive;
	}
	
	public boolean checkAttackPacketSpeed()
	{
		int attackSpeed = getOwner().getGameStats().getCurrentStat(StatEnum.ATTACK_SPEED);
		long milis = System.currentTimeMillis();
		if (milis - lastAttackMilis < attackSpeed)
			return false;
		else
		{
			lastAttackMilis = milis;
			return true;
		}					
	}
	
	public boolean checkSkillPacket(int spellid, int time, int targetId)
	{		
		Skill skill = SkillEngine.getInstance().getSkillFor(getOwner(), spellid, getOwner().getTarget());
		if(CustomConfig.LOG_CASTSPELL_TARGETHACK && skill.getFirstTargetProperty() == FirstTargetAttribute.TARGET && (skill.getFirstTarget() == null || skill.getFirstTarget().getObjectId() != targetId))
		{
			log.info("[CHEAT] " + getOwner().getName() + " CM_CASTSPELL packet hack. TARGETID WRONG.");
			return false;
		}
		
		long milis = System.currentTimeMillis();
		long clientDelay = milis - lastSkillMilis;
		long diff = lastSkillAnimationTime - clientDelay;
		if (CustomConfig.LOG_CASTSPELL_SPEEDHACK && diff > lastSkillAnimationTime * 0.25f)
		{
			log.info("[CHEAT] " + getOwner().getName() + " CM_CASTSPELL packet hack. Packet force send. SPEED HACK. Server delay: " + String.valueOf(lastSkillAnimationTime) + "ms. Client delay: " + String.valueOf(clientDelay) + "ms");
			return false;
		}
		else
		{			
			long skillCooldownTime = getOwner().getSkillCoolDown(skill.getSkillTemplate().getDelayId());

			if(CustomConfig.LOG_CASTSPELL_COOLDOWNHACK && milis < skillCooldownTime)
			{
				log.info("[CHEAT] " + getOwner().getName() + " CM_CASTSPELL packet hack. Packet force send. COOLDOWN AVOID. Difference: " + String.valueOf(skillCooldownTime - milis) + "ms");
				return false;
			}
			
			lastSkillMilis = milis;
			lastSkillAnimationTime = time;
			return true;
		}
	}

}