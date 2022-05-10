/*
 * This file is part of aion-unique <aion-unique.com>.
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
package org.openaion.gameserver.skill.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.controllers.movement.ActionObserver;
import org.openaion.gameserver.controllers.movement.StartMovingListener;
import org.openaion.gameserver.controllers.movement.ActionObserver.ObserverType;
import org.openaion.gameserver.geo.GeoEngine;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.siege.Artifact;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.item.WeaponType;
import org.openaion.gameserver.network.aion.serverpackets.SM_CASTSPELL;
import org.openaion.gameserver.network.aion.serverpackets.SM_CASTSPELL_END;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_STANCE_STATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.restrictions.RestrictionsManager;
import org.openaion.gameserver.skill.SkillEngine;
import org.openaion.gameserver.skill.action.Action;
import org.openaion.gameserver.skill.action.Actions;
import org.openaion.gameserver.skill.condition.Condition;
import org.openaion.gameserver.skill.condition.Conditions;
import org.openaion.gameserver.skill.effect.EffectId;
import org.openaion.gameserver.skill.effect.EffectTemplate;
import org.openaion.gameserver.skill.effect.SummonSkillAreaEffect;
import org.openaion.gameserver.skill.properties.FirstTargetAttribute;
import org.openaion.gameserver.skill.properties.FirstTargetRangeProperty;
import org.openaion.gameserver.skill.properties.Properties;
import org.openaion.gameserver.skill.properties.Property;
import org.openaion.gameserver.skill.properties.TargetRangeAttribute;
import org.openaion.gameserver.skill.properties.TargetRangeProperty;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;


/**
 * @author ATracer
 * @edit kecimis
 */
public class Skill
{
	//not skillType from skill_template
	private SkillType skillType = SkillType.CAST;
	private TreeSet<CreatureWithDistance> effectedList;
	private List<Effect> effects;
	private int maxEffected = 1;
	private Creature firstTarget;
	private Creature effector;
	private int skillLevel;
	private int skillStackLvl;
	private StartMovingListener conditionChangeListener;
	private SkillTemplate skillTemplate;
	private ItemTemplate itemTemplate;
	private int itemObjectId = 0;
	private int changeMpConsumptionValue;
	private Future<?> castingTask = null;
	private DashParam dash = null;
	/**
	 * 0: playerObjectId as target, normal skills
	 * 1: XYZ as target, point skills
	 * 3: playerObjectId as target, TARGET_NONVISIBLE
	 */
	private int	targetType;
	/**
	 * 0: ???
	 * 16: ???
	 * 32: regular
	 */
	private boolean chainSuccess = false;
	/**
	 * x,y,z for point skills
	 */
	private float x;
	private float y;
	private float z;
	/**
	 * Duration that depends on BOOST_CASTING_TIME
	 */
	private int duration;
	/**
	 * properties related
	 */
	private boolean	firstTargetRangeCheck = true;
	private boolean addWeaponRangeProperty = false;
	private FirstTargetAttribute firstTargetAttribute;
	private TargetRangeAttribute targetRangeAttribute;
	/**
	 * from cm_castspell packet
	 */
	private int time = 0;
	
	//not skillType from skill_template
	public enum SkillType
	{
		CAST,
		ITEM,
		PASSIVE,
		PROVOKED
	}
	
	//logger for debugging
	private Logger log = Logger.getLogger(Skill.class);
	
	/**
	 *  Each skill is a separate object upon invocation
	 *  Skill level will be populated from player SkillList
	 *  
	 * @param skillTemplate
	 * @param effector
	 * @param world
	 */
	public Skill(SkillTemplate skillTemplate, Player effector, Creature firstTarget)
	{
		this(skillTemplate, effector,
			effector.getSkillList().getSkillLevel(skillTemplate.getSkillId()), firstTarget, null);
	}	

	public Skill(SkillTemplate skillTemplate, Creature effector, int skillLvl, Creature firstTarget)
	{
		this(skillTemplate, effector, skillLvl, firstTarget, null);
	}
	/**
	 * 
	 * @param skillTemplate
	 * @param effector
	 * @param skillLvl
	 * @param firstTarget
	 */
	public Skill(SkillTemplate skillTemplate, Creature effector, int skillLvl, Creature firstTarget, ItemTemplate itemTemplate) {
		this.effectedList = new TreeSet<CreatureWithDistance>();
		this.effects = new ArrayList<Effect>();
		this.conditionChangeListener = new StartMovingListener();
		this.firstTarget = firstTarget;
		this.skillLevel = skillLvl;
		this.skillStackLvl = skillTemplate.getLvl();
		this.skillTemplate = skillTemplate;
		this.effector = effector;
		this.itemTemplate = itemTemplate;
		
		if(itemTemplate != null)
			skillType = SkillType.ITEM;
		
		if (skillTemplate.isPassive())
			skillType = SkillType.PASSIVE;
		else if (skillTemplate.isProvoked())
			skillType = SkillType.PROVOKED;
	}

	/**
	 * Check if the skill can be used
	 * 
	 * @return True if the skill can be used
	 */
	public boolean canUseSkill()
	{
		if (effector.getLifeStats().isAlreadyDead())
			return false;
		
		if(!setProperties(skillTemplate.getInitproperties()))
		{
			log.debug("init failed");
			return false;
		}
		if(!setProperties(skillTemplate.getSetproperties()))
		{
			log.debug("set prop failed");
			return false;
		}
		if(!preCastCheck())
		{
			log.debug("precastcheck failed");
			return false;
		}

		effector.setCasting(this);
		Iterator<CreatureWithDistance> effectedIter = effectedList.iterator();
		while(effectedIter.hasNext())
		{
			Creature effected = effectedIter.next().getCreature();
			if(effected == null)
				effected = effector;

			if(effector instanceof Player)
			{
				if (!RestrictionsManager.canAffectBySkill((Player)effector, effected))
					effectedIter.remove();
			}
			else
			{
				if(effector.getEffectController().isAbnormalState(EffectId.CANT_ATTACK_STATE) && !skillTemplate.hasEvadeEffect() )
					effectedIter.remove();
			}
		}
		effector.setCasting(null);
		
		if (getTargetType() != 1 && (effectedList == null || effectedList.size() == 0) && !checkNonTargetAOE())
			return false;
		
		if(getTargetType() == 3 && effector.isInInstance())
			return false;

		return true;
	}

	/**
	 *  Skill entry point
	 */
	public void useSkill()
	{
		if (!canUseSkill())
		{
			log.debug("canUseSkill failed!");
			return;
		}
		if(skillTemplate == null)
		{
			log.debug("No skill template !");
			return;
		}

		if(effector instanceof Player)
		{
			QuestEngine.getInstance().onSkillUse(new QuestCookie(effector, (Player)effector, 0, -1), skillTemplate.getSkillId());
		}
		
		changeMpConsumptionValue = 0;
		
		//notify only casted skills
		if (skillType == SkillType.CAST)
			effector.getObserveController().notifySkilluseObservers(this);
		
		//start casting
		effector.setCasting(this);
		
		int skillDuration = skillTemplate.getDuration();//casting time
		
		if (skillType == SkillType.CAST)//only casted skills are affected
		{
			int currentStat = 0;
			if (effector instanceof Player)
				currentStat = effector.getGameStats().getCurrentStat(StatEnum.BOOST_CASTING_TIME) - 100;

			float finalRate = ((float)effector.getController().getBoostCastingRate(SkillSubType.NONE) + 
				(float)effector.getController().getBoostCastingRate(getSkillTemplate().getSubType()) + 
				(float)currentStat)/100f;
			this.duration = (int)(skillDuration * (1-finalRate));
		}
		else
			this.duration = skillDuration;
		
		int cooldown = skillTemplate.getCooldown();
		int delayId = skillTemplate.getDelayId();
		
		if(cooldown != 0 && !(effector instanceof Summon))
		{
			effector.setSkillCoolDown( delayId, cooldown * 100 + this.duration + System.currentTimeMillis());
		}
		else if (effector instanceof Summon)
		{
			Summon summon = (Summon) effector;
			int orderSkillCooldown = summon.getController().getOrderSkillCooldown();
			summon.setSkillCoolDown(delayId, orderSkillCooldown * 100 + this.duration + System.currentTimeMillis());
		}
		
		if(duration < 0)
			duration = 0;
		
		//send packets to start casting bar
		if(skillType == SkillType.CAST)
			startCast();
		else if (skillType == SkillType.ITEM && duration > 0 && this.itemObjectId != 0 && effector instanceof Player)
		{
			PacketSendUtility.broadcastPacket((Player)effector, new SM_ITEM_USAGE_ANIMATION(effector.getObjectId(), firstTarget.getObjectId(),
				itemObjectId, itemTemplate.getTemplateId(), this.getSkillTemplate().getDuration(), 0, 0), true);
		}
		
		//only 1 toggle skill at the time
		if(skillTemplate.isToggle() && skillTemplate.getSubType() != SkillSubType.CHANT)
		{
			if (effector instanceof Player)
			{
				for(Effect ef : effector.getEffectController().getNoShowEffects())
				{
					SkillTemplate skilTemplate = ef.getSkillTemplate();
					if (skilTemplate != null && skilTemplate.isToggle() && skilTemplate.getSubType() != SkillSubType.CHANT)
						effector.getEffectController().removeNoshowEffect(ef.getSkillId());
				}
			}
		}
		
		effector.getObserveController().attach(conditionChangeListener);
		
		if(this.duration > 0)
			schedule(this.duration);
		else
			endCast();
	}
	
	/**
	 * Penalty success skill
	 */
	private void startPenaltySkill()
	{
		if(skillTemplate.getPenaltySkillId() == 0)
			return;

		Skill skill = SkillEngine.getInstance().getSkill(effector, skillTemplate.getPenaltySkillId(), 1, effector);
		skill.useSkill();
	}
	
	/**
	 *  Start casting of skill
	 */
	private void startCast()
	{
		int targetObjId = firstTarget != null ? firstTarget.getObjectId() : 0;
		
		switch(targetType)
		{
			case 0: // PlayerObjectId as Target
				PacketSendUtility.broadcastPacketAndReceive(effector,
					new SM_CASTSPELL(
						effector.getObjectId(),
						skillTemplate.getSkillId(),
						skillLevel,
						targetType,
						targetObjId,
						this.duration));
				break;
				
			case 1: // XYZ as Target
				PacketSendUtility.broadcastPacketAndReceive(effector,
					new SM_CASTSPELL(
						effector.getObjectId(),
						skillTemplate.getSkillId(),
						skillLevel,
						targetType,
						x, y, z,
						this.duration));
				break;			
			case 3:
				PacketSendUtility.broadcastPacketAndReceive(effector,
					new SM_CASTSPELL(
						effector.getObjectId(),
						skillTemplate.getSkillId(),
						skillLevel,
						targetType,
						0,
						this.duration));
 				break;
		}
	}
	
	public void cancelCast()
	{
		if(castingTask != null)
		{
			castingTask.cancel(true);
			castingTask = null;
		}
	}
	
	/**
	 *  Apply effects and perform actions specified in skill template
	 */
	public void endCast()
	{
		if(!(effector instanceof Artifact) && !effector.isCasting())
			return;
		
		// if target out of range
		if(skillTemplate == null)
			return;
		if (!(effector instanceof Artifact) && checkOutOfTargetRange())
			return;
		
		//stop casting must be before preUsageCheck()
		effector.setCasting(null);
		
		if(!(effector instanceof Artifact) && !preUsageCheck())
			return;
		
		//remove item if it was used through SkillUseAction
		if (skillType == SkillType.ITEM && this.itemObjectId != 0 && effector instanceof Player)
		{
			if(!((Player)effector).getInventory().removeFromBagByObjectId(itemObjectId, 1))
				return;
		}

		//broadcastmessage about abyss transformation
		if (skillTemplate.getStack().contains("ABYSS_RANKERSKILL_DARK_AVATAR") || skillTemplate.getStack().contains("ABYSS_RANKERSKILL_LIGHT_AVATAR"))
		{
			String traceName;
			if (((Player)effector).getCommonData().getRace() == Race.ELYOS)
				traceName = "Elyos";
	    	else
	    		traceName = "Asmodians";


	    	String tzoneName;
	    	if (((Player)effector).getZoneInstance() != null)
	    		tzoneName = ((Player)effector).getZoneInstance().getTemplate().getName().name();
	    	else
	    		tzoneName = "Unknown";
	    	
	    	final String raceName = traceName;
	    	final String zoneName = tzoneName;
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
			    @Override
			    public boolean run(Player p)
			    {
			    	if(p.getWorldId() == effector.getWorldId())
			    		PacketSendUtility.sendPacket(p, SM_SYSTEM_MESSAGE.STR_SKILL_ABYSS_SKILL_IS_FIRED(raceName, ((Player)effector).getName(), zoneName, getSkillTemplate().getNameId()));
			    	
			    	return true;
			    }
			});
		}

		/**
		 * Create effects and precalculate result
		 */

		boolean triggerChainSkill = false;
		boolean pulledEffect = false;
		if(skillTemplate.getEffects() != null)
		{
			for(CreatureWithDistance cre : effectedList)
			{
				Creature effected = cre.getCreature();
				checkSkillSetException(effected);
				Effect effect = new Effect(effector, effected, skillTemplate, skillLevel, 0, itemTemplate);
				effect.initialize();
				
				switch (effect.getAttackStatus())
				{
					case DODGE:
					case CRITICAL_DODGE:
					case RESIST:
					case CRITICAL_RESIST:
						break;
					default:
						triggerChainSkill = true;
						break;
				}
				
				effects.add(effect);
				dash = effect.getDashParam();
				
				//exception for pulledEffect
				if (effect.getSubEffect() != null && effect.getSubEffect().isPulledEffect())
					pulledEffect = true;
			}
			//exception for SummonSkillAreaNpc
			if (getTargetType() == 1 && effectedList.size() == 0)
			{
				for (EffectTemplate et : skillTemplate.getEffects().getEffects())
				{
					if (et instanceof SummonSkillAreaEffect)
					{
						Effect effect = new Effect(effector, null, skillTemplate, skillLevel, 0, itemTemplate);
						effect.initialize();
						effect.setX(getX());
						effect.setY(getY());
						effect.setZ(getZ());
						effects.add(effect);
						break;
					}
				}
			}
		}
				
		//Chain Skill Trigger Rate
		if (triggerChainSkill || checkNonTargetAOE())
		{
            // Check if Chain Skill Trigger Enabled
    		if(CustomConfig.SKILL_CHAIN_TRIGGER){
				int chainProb = skillTemplate.getChainSkillProb();
    	        if (Rnd.get(100) < CustomConfig.SKILL_CHAIN_RATE || chainProb == 100) {
						this.chainSuccess = true;
                } else {
                	this.chainSuccess = false;
				}
    		} else {
                // Check Chain Skill Result
            	int chainProb = skillTemplate.getChainSkillProb();
                if (chainProb != 0) {
                    if (Rnd.get(100) < chainProb) {
					this.chainSuccess = true;
			}else{
    					this.chainSuccess = false;
                    }
                }
			}
		}
		
		/**
		 * send packets to end casting bar
		 */
		if(skillType == SkillType.CAST)
			sendCastspellEnd();
		else if (skillType == SkillType.ITEM && this.itemObjectId != 0 && effector instanceof Player)
		{
			PacketSendUtility.broadcastPacket((Player)effector, new SM_ITEM_USAGE_ANIMATION(effector.getObjectId(), firstTarget.getObjectId(),
				itemObjectId, itemTemplate.getTemplateId(), 0, 1, 0), true);
		}
				
		/**
		 * Perform necessary actions (use mp,dp items etc)
		 */
		Actions skillActions = skillTemplate.getActions();
		if(skillActions != null)
		{
			for(Action action : skillActions.getActions())
			{	
				action.act(this);
			}
		}
		
		/**
		 * clear effectedList
		 */
		effectedList.clear();
				
		//TODO improve this
		if (effector instanceof Player && ((Player)effector).getEquipment().getMainHandWeaponType() == WeaponType.BOW)
			((Player)effector).getEquipment().useArrow();

		if(time == 0 || effects.isEmpty() || pulledEffect)
			applyEffects();
		else
		{
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				public void run()
				{
					applyEffects();
				}
			}, time);
		}
	}

	private void applyEffects()
	{
		boolean combatStarted = false;
		for(Effect effect : effects)
		{
			effect.applyEffect();
			if( effect.isDmgEffect() || skillTemplate.getSubType() == SkillSubType.DEBUFF )
			{
				effect.getEffected().setCombatState(8);
				if(!combatStarted)
					combatStarted = true;
			}
		}
		if(combatStarted)
			effector.setCombatState(8);
		
		/**
		 * send SM_STANCE_STATE if stance="true" and effect was added to effectcontroller
		 */
		if(effector instanceof Player && skillTemplate.isStance())
		{
			boolean applyStance = false;
			
			for (Effect ef : effector.getEffectController().getNoShowEffects())
			{
				if (ef.getSkillId() == this.getSkillTemplate().getSkillId())
					applyStance = true;
			}
			
			if (applyStance)
			{
				final int skillId = skillTemplate.getSkillId();
				final ActionObserver skillObserver = new ActionObserver(ObserverType.SKILLUSE) {
					@Override
					public void skilluse(Skill skill)
					{
						effector.getEffectController().removeNoshowEffect(skillId);
						PacketSendUtility.broadcastPacketAndReceive((Player)effector, new SM_STANCE_STATE(((AionObject)effector).getObjectId(), 0));
					}
				};

				effector.getObserveController().attach(skillObserver);
				PacketSendUtility.broadcastPacketAndReceive((Player)effector, new SM_STANCE_STATE(((AionObject)effector).getObjectId(), 1));
			}
		}
		
		/**
		 * Use penalty skill (now 100% success)
		 */
		startPenaltySkill();
	}
	
	/**
	 * @param spellStatus
	 * @param effects
	 */
	private void sendCastspellEnd()
	{
		PacketSendUtility.broadcastPacketAndReceive(effector, new SM_CASTSPELL_END(this));
	}
	/**
	 *  Schedule actions/effects of skill (channeled skills)
	 */
	private void schedule(int delay)
	{
		castingTask = ThreadPoolManager.getInstance().schedule(new Runnable() 
		{
			public void run() 
			{
				endCast();
			}   
		}, delay);
	}
	
	/**
	 *  Check all conditions before starting cast
	 */
	private boolean preCastCheck()
	{
		Conditions skillConditions = skillTemplate.getStartconditions();
		return checkConditions(skillConditions);
	}
	
	/**
	 *  Check all conditions before using skill
	 */
	private boolean preUsageCheck()
	{
		Conditions skillConditions = skillTemplate.getUseconditions();
		return checkConditions(skillConditions);
	}
	
	private boolean checkConditions(Conditions conditions)
	{
		if(conditions != null)
		{
			for(Condition condition : conditions.getConditions())
			{
				if(!condition.verify(this))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean setProperties(Properties properties)
	{
		if(properties != null)
		{
			for(Property property : properties.getProperties())
			{
				if(!property.set(this))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkOutOfTargetRange()
	{
		if(targetType == 3)
			return false;
		if (!isFirstTargetRangeCheck())
			return false;
		
		if (effector instanceof Player && firstTarget != null && firstTarget != effector && targetType != 1)
		{
			//tolerance
			float distance = 3.0f;
			if(skillTemplate.getSetproperties() != null)
			{
				//add firsttargetrangeproperty
				for(Property prop : skillTemplate.getSetproperties().getProperties())
				{
					if(prop instanceof FirstTargetRangeProperty)
						distance += (float)((FirstTargetRangeProperty) prop).getValue();
				}
			}
			//add weaponrange if needed
			if (getAddWeaponRangeProperty())
				distance += (float)((Player)effector).getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f;
			
			//distance check
			if((float)MathUtil.getDistance(effector, firstTarget) > distance)
			{
				((Player)effector).getController().cancelCurrentSkill();
				PacketSendUtility.sendPacket((Player)effector, SM_SYSTEM_MESSAGE.STR_ATTACK_TOO_FAR_FROM_TARGET());
				return true;
			}
			//geo check
			else if (!GeoEngine.getInstance().canSee(effector, firstTarget))
			{
				((Player)effector).getController().cancelCurrentSkill();
				PacketSendUtility.sendPacket((Player)effector, SM_SYSTEM_MESSAGE.STR_SKILL_OBSTACLE());
				return true;
			}
		}
		
		if (effector instanceof Player && firstTarget != null)
		{
			float range = 0;
			if(skillTemplate.getSetproperties() != null)
			{
				for(Property prop : skillTemplate.getSetproperties().getProperties())
				{
					if (prop instanceof TargetRangeProperty)
					{
						range += ((TargetRangeProperty)prop).getDistance();
					}
				}
			}
			
			//add weaponrange if needed
			if (getAddWeaponRangeProperty())
				range += (float)((Player)effector).getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f;
			
			//remove creatures from effectedlist who are no longer in range of aoe skill
			if (range != 0 && effectedList != null && effectedList.size() > 0)
			{
				//tolerance
				range += 1.5f;//maybe need fix?
				if (targetType == 1)
				{
					for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
					{
						Creature nextEffected = iter.next().getCreature();

						if(MathUtil.getDistance(nextEffected, getX(),getY(),getZ()) > range ||
							!GeoEngine.getInstance().canSee(effector.getWorldId(), getX(), getY(), getZ(), nextEffected.getX(), nextEffected.getY(), nextEffected.getZ())) 
						{
							iter.remove();
						}
					}
				}
				else
				{
					for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
					{
						Creature nextEffected = iter.next().getCreature();

						if(MathUtil.getDistance(firstTarget, nextEffected) > range ||
							!GeoEngine.getInstance().canSee(effector, nextEffected)) 
							iter.remove();
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * Check for skillset_exception
	 */
	private void checkSkillSetException(Creature effected)
	{
		if (effector instanceof Player)
		{
			if (skillTemplate.getSkillSetException() != 0)
			{
				//+4 because of targetrangeproperty
				if (MathUtil.isIn3dRange(effected, effector, skillTemplate.getTargetRangeProperty().getDistance() + 1.5f))
					effected.getEffectController().removeEffectBySetNumber(skillTemplate.getSkillSetException());

			}
		}
	}
	
	/**
	 * @return boolean if its skill applied to party
	 */
	public boolean isPartySkill()
	{
		return (targetRangeAttribute == TargetRangeAttribute.PARTY);
	}
	
	/**
	 * @return true or false
	 */
	public boolean isPassive()
	{
		return skillTemplate.getActivationAttribute() == ActivationAttribute.PASSIVE;
	}
	/**
	 * @return true if the present skill is a non-targeted, non-point AOE skill
	 */
	public boolean checkNonTargetAOE()
	{
		return (firstTargetAttribute == FirstTargetAttribute.ME
			&& targetRangeAttribute == TargetRangeAttribute.AREA);
	}
	
	/**
	* @param FirstTargetAttributethe firstTargetAttribute to set
	*/
	public void setFirstTargetProperty(FirstTargetAttribute firstTargetAttribute)
	{
		this.firstTargetAttribute = firstTargetAttribute;
	}
	
	public FirstTargetAttribute getFirstTargetProperty()
	{
		return firstTargetAttribute;
	}
	/**
	* @param targetRangeAttribute the targetRangeAttribute to set
	*/
	public void setTargetRangeAttribute(TargetRangeAttribute targetRangeAttribute)
	{
		this.targetRangeAttribute = targetRangeAttribute;
	}
	 
	/**
	 * @param value is the changeMpConsumptionValue to set
	 */
	public void setChangeMpConsumption(int value)
	{
		changeMpConsumptionValue = value;
	}
	
	/**
	 * @return the changeMpConsumptionValue
	 */
	public int getChangeMpConsumption()
	{
		return changeMpConsumptionValue;
	}
	
	/**
	 * @return the skillType
	 */
	public SkillType getSkillType()
	{
		return this.skillType;
	}
	
	/**
	 * @return the effectedList
	 */
	public TreeSet<CreatureWithDistance> getEffectedList()
	{
		return effectedList;
	}

	/**
	 * Set the maximum number of effected targets.
	 * 
	 * @param maxEffected
	 */
	public void setMaxEffected(int maxEffected)
	{
		this.maxEffected = maxEffected;
	}

	/**
	 * @return The maximum number of effected targets.
	 */
	public int getMaxEffected()
	{
		return maxEffected;
	}

	/**
	 * @return the effector
	 */
	public Creature getEffector()
	{
		return effector;
	}

	/**
	 * @return the skillLevel
	 */
	public int getSkillLevel()
	{
		return skillLevel;
	}

	/**
	 * @return the skillStackLvl
	 */
	public int getSkillStackLvl()
	{
		return skillStackLvl;
	}

	/**
	 * @return the conditionChangeListener
	 */
	public StartMovingListener getConditionChangeListener()
	{
		return conditionChangeListener;
	}

	/**
	 * @return the skillTemplate
	 */
	public SkillTemplate getSkillTemplate()
	{
		return skillTemplate;
	}

	/**
	 * @return the firstTarget
	 */
	public Creature getFirstTarget()
	{
		return firstTarget;
	}

	/**
	 * @param firstTarget the firstTarget to set
	 */
	public void setFirstTarget(Creature firstTarget)
	{
		this.firstTarget = firstTarget;
	}



	/**
	 * @return the firstTargetRangeCheck
	 */
	public boolean isFirstTargetRangeCheck()
	{
		return firstTargetRangeCheck;
	}
	
	/**
	 * @param firstTargetRangeCheck the firstTargetRangeCheck to set
	 */
	public void setFirstTargetRangeCheck(boolean firstTargetRangeCheck)
	{
		this.firstTargetRangeCheck = firstTargetRangeCheck;
	}

	public ItemTemplate getItemTemplate()
	{
		return this.itemTemplate;
	}
	
	/**
	 * @param itemObjectId
	 */
	public void setItemObjectId(int itemObjectId)
	{
		this.itemObjectId = itemObjectId;
	}
	
	public int getItemObjectId()
	{
		return this.itemObjectId;
	}
	
	/**
	 * @param targetType
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setTargetType(int targetType, float x, float y, float z)
	{
		this.targetType = targetType;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @return the targetType
	 */
	public int getTargetType()
	{
		return targetType;
	}

	/**
	 * @return the x
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY()
	{
		return y;
	}

	/**
	 * @return the z
	 */
	public float getZ()
	{
		return z;
	}
	public void setAddWeaponRangeProperty(boolean bol)
	{
		this.addWeaponRangeProperty = bol;
	}
	public boolean getAddWeaponRangeProperty()
	{
		return this.addWeaponRangeProperty;
	}
	public DashParam getDashParam()
	{
		return this.dash;
	}
	public boolean getChainSuccess()
	{
		return this.chainSuccess;
	}
	public List<Effect> getEffects()
	{
		return this.effects;
	}
	public void setTime(int time)
	{
		this.time = time;
	}
	public int getTime()
	{
		return this.time;
	}
}
