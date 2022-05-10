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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.controllers.attack.AttackResult;
import org.openaion.gameserver.controllers.attack.AttackStatus;
import org.openaion.gameserver.controllers.attack.AttackUtil;
import org.openaion.gameserver.controllers.movement.MovementType;
import org.openaion.gameserver.geo.GeoEngine;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.NpcWithCreator;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.stats.CreatureGameStats;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.templates.stats.NpcRank;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import org.openaion.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import org.openaion.gameserver.network.aion.serverpackets.SM_MOVE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SKILL_CANCEL;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.restrictions.RestrictionsManager;
import org.openaion.gameserver.skill.SkillEngine;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.skill.model.SkillSubType;
import org.openaion.gameserver.skill.model.SkillType;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;

import javolution.util.FastMap;


/**
 * This class is for controlling Creatures [npc's, players etc]
 * 
 * @author -Nemesiss-, ATracer(2009-09-29), Sarynth
 * 
 */
public abstract class CreatureController<T extends Creature> extends VisibleObjectController<Creature>
{
	private FastMap<Integer, Future<?>> tasks = new FastMap<Integer, Future<?>>().shared();
	
	private float healRate = 1.00f;
	
	/**
	 * Boostskillcastingtimeeffect
	 * -SUMMONTRAP,
	 * -SUMMON,
	 * -SUMMONHOMING,
	 * -HEAL,
	 * -ATTACK,
	 * -NONE //general, for all skills, example: Boon of Quickness
	 */
	private HashMap<SkillSubType, Integer> boostCastingRates = new HashMap<SkillSubType, Integer>();
	
	/**
	 * extend aura range
	 */
	private float auraRangeRate = 1.00f;
	
	private long lastAttackMilis = 0;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object, isOutOfRange);
		if(object == getOwner().getTarget())
		{
			getOwner().setTarget(null);
			PacketSendUtility.broadcastPacket(getOwner(), new SM_LOOKATOBJECT(getOwner()));
		}
	}

	/**
	 * Perform tasks on Creature starting to move
	 */
	public void onStartMove()
	{
		getOwner().getObserveController().notifyMoveObservers();
	}

	/**
	 * Perform tasks on Creature move in progress
	 */
	public void onMove()
	{
		getOwner().getObserveController().notifyMoveObservers();
	}

	/**
	 * Perform tasks on Creature stop move
	 */
	public void onStopMove()
	{
		getOwner().getObserveController().notifyMoveObservers();
	}

	/**
	 * Perform tasks on Creature death
	 */
	public void onDie(Creature lastAttacker)
	{
		this.cancelCurrentSkill();
		this.getOwner().setCasting(null);
		this.getOwner().setTarget(null);
		this.getOwner().getAi().clearDesires();
		this.getOwner().getEffectController().removeAllEffects();
		this.getOwner().getMoveController().stop();
		this.getOwner().getController().cancelAllTasks();
		this.getOwner().setState(CreatureState.DEAD);
		this.getOwner().getObserveController().notifyDeath(getOwner());
	}
	
	/**
	 * Perform tasks on Creature respawn
	 */
	@Override
	public void onRespawn()
	{
		getOwner().unsetState(CreatureState.DEAD);
		getOwner().getAggroList().clear();
	}

	/**
	 * Perform tasks when Creature was attacked //TODO may be pass only Skill object - but need to add properties in it
	 */
	public void onAttack(Creature creature, int skillId, TYPE type, int damage, int logId, AttackStatus status, boolean notifyAttackedObservers, boolean sendPacket)
	{
		// Reduce the damage to exactly what is required to ensure death. 
		// - Important that we don't include 7k worth of damage when the 
		//   creature only has 100 hp remaining. (For AggroList dmg count.) 
		if (damage > getOwner().getLifeStats().getCurrentHp()) 
			damage = getOwner().getLifeStats().getCurrentHp() + 1;
		
		Skill skill = getOwner().getCastingSkill();
		if (skill != null && damage > 0)
		{
			int cancelRate = skill.getSkillTemplate().getCancelRate();
		
			//default cancel rate 90 for magical skills of players
			if(cancelRate == 0 && skill.getSkillTemplate().getType()== SkillType.MAGICAL && getOwner() instanceof Player)
				cancelRate = 90;
			else if (getOwner() instanceof Npc)
			{
				Npc npc = (Npc)getOwner();
				NpcRank rank = npc.getObjectTemplate().getRank();
				if (rank == NpcRank.HERO || rank == NpcRank.LEGENDARY)
					cancelRate = 0;
			}
		
			if (cancelRate > 0)
			{
				int conc = getOwner().getGameStats().getCurrentStat(StatEnum.CONCENTRATION)/10;
				float maxHp = getOwner().getGameStats().getCurrentStat(StatEnum.MAXHP);
				float cancel = (cancelRate - conc)+(((float)damage)/maxHp*50);

				if(Rnd.get(100) < cancel)
				{
					if (creature instanceof Player)
						PacketSendUtility.sendPacket((Player)creature, SM_SYSTEM_MESSAGE.STR_SKILL_TARGET_SKILL_CANCELED());
					
					cancelCurrentSkill();
				}
			}
		}

		//set involved creatures to combat state
		creature.setCombatState(8);//8s taken from client's hide combat restriction
		getOwner().setCombatState(8);
		
		if (notifyAttackedObservers)
		{
			switch(status)
			{
				case DODGE:
				case CRITICAL_DODGE:
				case RESIST:
				case CRITICAL_RESIST:
					break;
				default:
					if (damage > 0)
						getOwner().getObserveController().notifyAttackedObservers(creature);
					break;
			}
		}
		
		//transfer hate from SkillAreaNpc,Homing,Trap,Totem,Servant to master
		if (creature instanceof NpcWithCreator)
			creature = creature.getActingCreature();
		
		getOwner().getAggroList().addDamage(creature, damage);
		
		//send SM_ATTACK_STATUS
		//for now send every time, because it updates hp of mob
		//if (sendPacket)
			sendOnAttackPacket(type, damage, skillId, logId);
		
		getOwner().getLifeStats().reduceHp(damage, creature);
	}
	
	public void onAttack(Creature creature, int damage, AttackStatus status, boolean notifyAttackedObservers) 
	{
		this.onAttack(creature, 0, TYPE.REGULAR, damage, 0, status, notifyAttackedObservers, false);
	}
	public void onAttack(Creature creature, int skillId, TYPE type, int damage, AttackStatus status, boolean notifyAttackedObservers)
	{
		this.onAttack(creature, skillId, type, damage, 0, status, notifyAttackedObservers, false);
	}
	
	/**
	 * send onAttackPacket
	 */
	protected void sendOnAttackPacket(TYPE type, int damage, int skillId, int logId)
	{
		PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_ATTACK_STATUS(getOwner(), type, -damage, skillId, logId));
	}
	
	/**
	 * Perform reward operation
	 * 
	 */
	public void doReward()
	{

	}

	/**
	 * This method should be overriden in more specific controllers
	 */
	public void onDialogRequest(Player player)
	{

	}

	
	/**
	 * 
	 * @param target
	 */
	public void attackTarget(Creature target)
	{
		this.attackTarget(target, 0, 274, 0);
	}
	
	@SuppressWarnings("rawtypes")
	public void attackTarget(final Creature target, int atknumber, int time, int attackType)
	{
		final Creature attacker = getOwner();
		
		/**
		 * Check all prerequisites
		 */
		//npc specific checks
		if(attacker instanceof Npc) 
		{
			if (!attacker.isSpawned())
				return;
			if(target == null || target.getLifeStats().isAlreadyDead())
			{
				((Npc)attacker).getAi().handleEvent(Event.MOST_HATED_CHANGED);
				return;
			}
		}
		//player specific checks
		else if (attacker.getActingCreature() instanceof Player)
		{
			if(Math.abs(attacker.getZ() - target.getZ()) > 6)
				return;
			if(!RestrictionsManager.canAttack((Player)attacker.getActingCreature(), target))
				return;

			//temporary hack check
			int attackSpeed = attacker.getGameStats().getCurrentStat(StatEnum.ATTACK_SPEED);
			long milis = System.currentTimeMillis();
			if (milis - lastAttackMilis < attackSpeed)
			{
				/**
				 * Hack!
				 */
				return;
			}
			lastAttackMilis = milis;
		}
		
		if(target == null || target.getLifeStats().isAlreadyDead())
			return;
		if (!attacker.isEnemy(target) || attacker.getLifeStats().isAlreadyDead() || !attacker.canAttack())
			return;

		if(!GeoEngine.getInstance().canSee(attacker, target))
		{
			if(attacker instanceof Player)
				PacketSendUtility.sendPacket((Player) attacker, SM_SYSTEM_MESSAGE.STR_ATTACK_OBSTACLE_EXIST);
			return;
		}
		

		CreatureGameStats gameStats = attacker.getGameStats();

		/**
		 * Calculate and apply damage
		 */
		List<AttackResult> attackResult = new ArrayList<AttackResult>();
		
		if (attacker.getAttackType().isMagic())
			attackResult = AttackUtil.calculateMagicalAttackResult(attacker, target);
		else	
			attackResult = AttackUtil.calculatePhysicalAttackResult(attacker, target);

		int damage = 0;
		AttackStatus status = AttackStatus.NORMALHIT;
		for(AttackResult result : attackResult)
		{
			damage += result.getDamage();
			status = result.getAttackStatus();
		}

		PacketSendUtility.broadcastPacketAndReceive(attacker, new SM_ATTACK(attacker, target, gameStats.getAttackCounter(),
			time, attackType, attackResult));
		
		//TODO synchronize attackcounter with packet atknumber
		gameStats.increaseAttackCounter();
		
		/**
		 * notify attack observers
		 */
		switch(status)
		{
			case DODGE:
			case CRITICAL_DODGE:
			case RESIST:
			case CRITICAL_RESIST:
				break;
			default:
				//observers
				attacker.getObserveController().notifyAttackObservers(target);
				target.getObserveController().notifyHittedObservers(getOwner(), DamageType.PHYSICAL);
				break;
		}
		
		final int finalDamage = damage;
		final AttackStatus finalStatus = status;
		if (time == 0)
		{
			target.getController().onAttack(attacker, finalDamage, finalStatus, true);
		}
		else
		{
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				public void run()
				{
					target.getController().onAttack(attacker, finalDamage, finalStatus, true);
				}
				
			}, time);

		}
	}
	
	/**
	 * Stops movements
	 */
	public void stopMoving()
	{
		Creature owner = getOwner();
		
		int ownerWorld = owner.getWorldId();
		float ownerX = owner.getX();
		float ownerY = owner.getY();
		float ownerZ = GeoEngine.getInstance().getZ(ownerWorld, ownerX, ownerY, owner.getZ());
		byte ownerH = owner.getHeading();
		
		World.getInstance().updatePosition(owner, ownerX, ownerY, ownerZ, ownerH);
		PacketSendUtility.broadcastPacket(owner, new SM_MOVE(owner.getObjectId(), ownerX, ownerY, ownerZ,
			ownerH, MovementType.MOVEMENT_STOP));
	}

	/**
	 * Handle Dialog_Select
	 * 
	 * @param dialogId
	 * @param player
	 * @param questId
	 */
	public void onDialogSelect(int dialogId, Player player, int questId)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public Future<?> getTask(TaskId taskId)
	{
		return tasks.get(taskId.ordinal());
	}
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public boolean hasTask(TaskId taskId)
	{
		return tasks.containsKey(taskId.ordinal());
	}

	/**
	 * 
	 * @param taskId
	 */
	public void cancelTask(TaskId taskId)
	{
		cancelTask(taskId, false);
	}
	/**
	 * 
	 * @param taskId
	 * @param forced
	 */
	public void cancelTask(TaskId taskId, boolean forced)
	{
		Future<?> task = tasks.remove(taskId.ordinal());
		if(task != null)
		{
			task.cancel(forced);
		}
	}
	
	/**
	 *  If task already exist - it will be canceled
	 * @param taskId
	 * @param task
	 */
	public void addTask(TaskId taskId, Future<?> task)
	{
		cancelTask(taskId);
		tasks.put(taskId.ordinal(), task);
	}
	
	/**
	 *  If task already exist - it will not be replaced
	 * @param taskId
	 * @param task
	 */
	public void addNewTask(TaskId taskId, Future<?> task)
	{
		tasks.putIfAbsent(taskId.ordinal(), task);
	}

	/**
	 * Cancel all tasks associated with this controller
	 * (when deleting object)
	 */
	public void cancelAllTasks()
	{
		for(Future<?> task : tasks.values())
		{
			if(task != null)
			{
				task.cancel(true);
			}
		}
		// FIXME: This can fill error logs with NPE if left null. Should never happen...
		tasks = new FastMap<Integer, Future<?>>().shared();
	}

	@Override
	public void delete()
	{
		cancelAllTasks();
		super.delete();
	}

	/**
	 * Die by reducing HP to 0
	 */
	public void die()
	{
		getOwner().getLifeStats().reduceHp(getOwner().getLifeStats().getCurrentHp() + 1, null);
	}
	
	/**
	 * 
	 * @param skillId
	 */
	public void useSkill(int skillId)
	{
		Creature creature = getOwner();

		Skill skill = SkillEngine.getInstance().getSkill(creature, skillId, 1, creature.getTarget());
		Logger.getLogger(getClass()).debug(creature.getName()+"using skill #"+skillId+":"+skill);
		if(skill != null)
		{
			skill.useSkill();
		}
	}
	
	/**
	 *  Notify hate value to all visible creatures
	 *  
	 * @param value
	 */
	public void broadcastHate(final int value)
	{
		getOwner().getKnownList().doOnAllObjects(new Executor<AionObject>(){
			@Override
			public boolean run(AionObject visibleObject)
			{
				if(visibleObject instanceof Creature)
				{
					((Creature)visibleObject).getAggroList().notifyHate(getOwner(), value);
				}
				return true;
			}
		});
	}
	
   public void abortCast() 
	{ 
		Creature creature = getOwner(); 
	    Skill skill = creature.getCastingSkill(); 
	    if (skill == null) 
			return; 
	    creature.setCasting(null); 
	} 
	
	/** 
 	* Cancel current skill and remove cooldown 
 	*/ 
 	public void cancelCurrentSkill() 
 	{ 
		Creature creature = getOwner(); 
		Skill castingSkill = creature.getCastingSkill(); 
		if(castingSkill != null) 
		{
			castingSkill.cancelCast();
			creature.removeSkillCoolDown(castingSkill.getSkillTemplate().getDelayId()); 
			creature.setCasting(null); 
			PacketSendUtility.broadcastPacketAndReceive(creature, new SM_SKILL_CANCEL(creature, castingSkill.getSkillTemplate().getSkillId())); 
		}        
 	} 
	
	/**
	 * @param npcId
	 */
	public void createSummon(int npcId, int skillLvl)
	{
		// TODO Auto-generated method stub
		
	}
	
	public float getHealRate() 
	{
		return healRate;
	}

	public void setHealRate(float healRate) 
	{
		this.healRate = healRate;
	}
	
	/**
	 * BoostCastingTimeEffects rates
	 * 
	 */
	public void addBoostCastingRate(SkillSubType type, int value)
	{
		if (boostCastingRates.containsKey(type))
		{
			int temp = boostCastingRates.get(type);
			boostCastingRates.put(type, temp + value);
		}
		else
			boostCastingRates.put(type, value);
	}
	
	public void removeBoostCastingRate(SkillSubType type, int value)
	{
		if (boostCastingRates.containsKey(type))
		{
			int temp = boostCastingRates.get(type);
			boostCastingRates.put(type, temp - value);
		}
	}
	
	public int getBoostCastingRate(SkillSubType type)
	{
		if (boostCastingRates.containsKey(type))
			return boostCastingRates.get(type);
		else
			return 0;
	}
	
	//extend aura range
	public float getAuraRangeRate() 
	{
		return auraRangeRate;
	}
	public void setAuraRangeRate(float auraRangeRate) 
	{
		this.auraRangeRate = auraRangeRate;
	}
}
