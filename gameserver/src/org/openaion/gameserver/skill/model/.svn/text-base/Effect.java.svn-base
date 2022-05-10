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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.controllers.attack.AttackStatus;
import org.openaion.gameserver.controllers.movement.ActionObserver;
import org.openaion.gameserver.controllers.movement.AttackCalcObserver;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_SKILL_ACTIVATION;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.effect.DamageEffect;
import org.openaion.gameserver.skill.effect.DelayDamageEffect;
import org.openaion.gameserver.skill.effect.EffectId;
import org.openaion.gameserver.skill.effect.EffectTemplate;
import org.openaion.gameserver.skill.effect.Effects;
import org.openaion.gameserver.skill.effect.MpHealEffect;
import org.openaion.gameserver.skill.effect.PulledEffect;
import org.openaion.gameserver.skill.effect.SimpleRootEffect;
import org.openaion.gameserver.skill.effect.SkillLauncherEffect;
import org.openaion.gameserver.skill.effect.TransformEffect;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;

import javolution.util.FastMap;


/**
 * @author ATracer
 * @edit kecimis
 *
 */
public class Effect
{
	private SkillTemplate skillTemplate;
	private int skillLevel;
	private int duration;
	private int endTime;

	private Creature effected;
	private Creature effector;
	private Future<?> checkTask = null;
	private Future<?> task = null;
	private Future<?>[] periodicTasks = null;
	private Future<?> mpUseTask = null;
	
	private boolean isDmgEffect = false;
	private boolean isDelayDamage = false;
	private boolean isMpHeal = false;
	private boolean isPulledEffect = false;
	private boolean isSimpleRootEffect = false;
	
	/**
	 * Used for damage/heal values
	 */
	private int reserved1;
	/**
	 * Used for shield total hit damage;
	 */
	private int reserved2;
	/**
	 * Used for shield hit damage
	 */
	private int reserved3;
	/**
	 * Used for damage over time
	 * totaly custom doesnt fit client_skills
	 */
	private int reserved4;
	
	/**
	 * Spell Status
	 * 
	 * 1 : stumble
	 * 2 : knockback
	 * 4 : open aerial
	 * 8 : close aerial
	 * 16 : spin
	 * 32 : block
	 * 64 : parry
	 * 128 : dodge
	 * 256 : resist
	 */
	private SpellStatus spellStatus = SpellStatus.NONE;
	
	private AttackStatus attackStatus = AttackStatus.NORMALHIT;
	/**
	 * 1 : reflector
	 * 2 : normal shield
	 * 4 : protect
	 */
	
	private int shieldType;
	
	private boolean addedToController;
	private boolean forbidAdding = false;
	private AttackCalcObserver[] attackStatusObserver;
	
	private AttackCalcObserver[] attackShieldObserver;
	
	private boolean launchSubEffect = true;
	private Effect subEffect = null;
	
	private boolean isStopped;
	
	private ItemTemplate itemTemplate;
	
	/**
	 * Hate that will be placed on effected list
	 */
	private int tauntHate;
	/**
	 * Total hate that will be broadcasted
	 */
	private int effectHate;
	
	private Map<Integer,EffectTemplate> sucessEffects = Collections.synchronizedMap(new FastMap<Integer,EffectTemplate>());
	
	protected int abnormals;

	/**
	 * Action observer that should be removed after effect end
	 */
	private ActionObserver[] actionObserver;
	
	private int signetBursted = 0;
	private int carvedSignet = 0;
	
	private int reflectedDamage = 0;
	private int reflectorSkillId = 0;
	
	/**
	 * coordinates for point skills or dashtype skills
	 */
	private float x;
	private float y;
	private float z;
	
	private DashParam dashType = null;
	
	/**
	 * healvalues
	 */
	private int healValue;
	private int mpValue;
	private int hotValue;
	private int motValue;
	
	/**
	 * DamageType of skill
	 */
	private DamageType damageType = DamageType.MAGICAL;
	
	private float criticalProb = 1.0f; 
	
	//custom boost for signetbursteffect
	private int accModBoost = 0;
	
	
	/**
	 *  ABNORMAL EFFECTS
	 */

	public void setAbnormal(int mask)
	{
		abnormals |= mask;
	}

	public int getAbnormals()
	{
		return abnormals;
	}
	
	/**
	 *  Used for checking unique abnormal states
	 *  
	 * @param effectId
	 * @return
	 */
	public boolean isAbnormalSet(EffectId effectId)
	{
		return (abnormals & effectId.getEffectId()) == effectId.getEffectId();
	}
	
	public Effect(Creature effector, Creature effected, SkillTemplate skillTemplate, int skillLevel, int duration)
	{
		this.effector = effector;
		this.effected = effected;
		this.skillTemplate = skillTemplate;
		this.skillLevel = skillLevel;
		this.duration = duration;
	}
	
	public Effect(Creature effector, Creature effected, SkillTemplate skillTemplate, int skillLevel, int duration, ItemTemplate itemTemplate)
	{
		this(effector, effected, skillTemplate, skillLevel, duration);
		this.itemTemplate = itemTemplate;
	}
	
	/**
	 * @return the effectorId
	 */
	public int getEffectorId()
	{
		return effector.getObjectId();
	}

	/**
	 * @return the skillId
	 */
	public int getSkillId()
	{
		return skillTemplate.getSkillId();
	}
	
	/**
	 * @return the SkillTemplate
	 */
	public SkillTemplate getSkillTemplate()
	{
		return skillTemplate;
	}

	/**
	 * @return the SkillSetException
	 */
	public int getSkillSetException()
	{
		return skillTemplate.getSkillSetException();
	}
	
	/**
	 * @return the stack
	 */
	public String getStack()
	{
		return skillTemplate.getStack();
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
		return skillTemplate.getLvl();
	}
	
	/**
	 * 
	 * @return
	 */
	public SkillType getSkillType()
	{
		return skillTemplate.getType();
	}

	/**
	 * @return the duration
	 */
	public int getDuration()
	{
		return duration;
	}
	
	/**
	 * @param newDuration
	 */
	public void setDuration(int newDuration)
	{
		this.duration = newDuration;
	}

	/**
	 * @return the effected
	 */
	public Creature getEffected()
	{
		return effected;
	}

	/**
	 * @return the effector
	 */
	public Creature getEffector()
	{
		return effector;
	}

	/**
	 * @return the isPassive
	 */
	public boolean isPassive()
	{
		return skillTemplate.isPassive();
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Future<?> task)
	{
		this.task = task;
	}

	/**
	 * @return the periodicTask
	 */
	public Future<?> getPeriodicTask(int i)
	{
		return periodicTasks[i-1];
	}

	/**
	 * @param periodicTask the periodicTask to set
	 * @param i
	 */
	public void setPeriodicTask(Future<?> periodicTask, int i)
	{
		if(periodicTasks == null)
			periodicTasks = new Future<?>[4];
		this.periodicTasks[i-1] = periodicTask;
	}

	/**
	 * @return the mpUseTask
	 */
	public Future<?> getMpUseTask()
	{
		return mpUseTask;
	}

	/**
	 * @param mpUseTask the mpUseTask to set
	 */
	public void setMpUseTask(Future<?> mpUseTask)
	{
		this.mpUseTask = mpUseTask;
	}

	/**
	 * @return the reserved1
	 */
	public int getReserved1()
	{
		return reserved1;
	}

	/**
	 * @param reserved1 the reserved1 to set
	 */
	public void setReserved1(int reserved1)
	{
		this.reserved1 = reserved1;
	}

	/**
	 * @return the reserved2
	 */
	public int getReserved2()
	{
		return reserved2;
	}

	/**
	 * @param reserved2 the reserved2 to set
	 */
	public void setReserved2(int reserved2)
	{
		this.reserved2 = reserved2;
	}

	/**
	 * @return the reserved3
	 */
	public int getReserved3()
	{
		return reserved3;
	}

	/**
	 * @param reserved3 the reserved3 to set
	 */
	public void setReserved3(int reserved3)
	{
		this.reserved3 = reserved3;
	}
	
	/**
	 * @return the reserved3
	 */
	public int getReserved4()
	{
		return reserved4;
	}

	/**
	 * @param reserved3 the reserved3 to set
	 */
	public void setReserved4(int reserved4)
	{
		this.reserved4 = reserved4;
	}

	/**
	 * @return the attackStatus
	 */
	public AttackStatus getAttackStatus()
	{
		return attackStatus;
	}

	/**
	 * @param attackStatus the attackStatus to set
	 */
	public void setAttackStatus(AttackStatus attackStatus)
	{
		this.attackStatus = attackStatus;
	}
	
	public List<EffectTemplate> getEffectTemplates()
	{
		return skillTemplate.getEffects().getEffects();
	}
	
	public boolean isFood()
	{
		Effects effects = skillTemplate.getEffects();
		return effects != null && effects.isFood();
	}
	

	public boolean isToggle()
	{
		return skillTemplate.getActivationAttribute() == ActivationAttribute.TOGGLE;
	}
	
	public int getTargetSlot()
	{
		return skillTemplate.getTargetSlot().ordinal();
	}
	
	public int getTargetSlotLevel()
	{
		return skillTemplate.getTargetSlotLevel();
	}
	public DispelCategoryType getDispelCat()
	{
		return skillTemplate.getDispelCategory();
	}

	/**
	 * @param i
	 * @return attackStatusObserver for this effect template
	 */
	public AttackCalcObserver getAttackStatusObserver(int i)
	{
		if (attackStatusObserver!=null && i <= attackStatusObserver.length)
			return attackStatusObserver[i-1];
		else
			return null;
	}

	/**
	 * @param attackStatusObserver the attackCalcObserver to set
	 */
	public void setAttackStatusObserver(AttackCalcObserver attackStatusObserver, int i)
	{
		if(this.attackStatusObserver == null)
			this.attackStatusObserver = new AttackCalcObserver[4];
		this.attackStatusObserver[i-1] = attackStatusObserver;
	}

	/**
	 * @param i
	 * @return attackShieldObserver for this effect template
	 */
	public AttackCalcObserver getAttackShieldObserver(int i)
	{
		return attackShieldObserver[i-1];
	}

	/**
	 * @param attackShieldObserver the attackShieldObserver to set
	 */
	public void setAttackShieldObserver(AttackCalcObserver attackShieldObserver, int i)
	{
		if(this.attackShieldObserver == null)
			this.attackShieldObserver = new AttackCalcObserver[4];
		this.attackShieldObserver[i-1] = attackShieldObserver;
	}

	/**
	 * @return the launchSubEffect
	 */
	public boolean isLaunchSubEffect()
	{
		return launchSubEffect;
	}

	/**
	 * @param launchSubEffect the launchSubEffect to set
	 */
	public void setLaunchSubEffect(boolean launchSubEffect)
	{
		this.launchSubEffect = launchSubEffect;
	}

	/**
	 * @return the shieldDefense
	 */
	public int getShieldType()
	{
		return shieldType;
	}

	/**
	 * @param set shielddefense
	 */
	public void setShieldType(int value)
	{
		this.shieldType|= value;
	}

	/**
	 * @return the spellStatus
	 */
	public SpellStatus getSpellStatus()
	{
		return spellStatus;
	}

	/**
	 * @param spellStatus the spellStatus to set
	 */
	public void setSpellStatus(SpellStatus spellStatus)
	{
		this.spellStatus = spellStatus;
	}

	/**
	 * @return the subEffect
	 */
	public Effect getSubEffect()
	{
		return subEffect;
	}

	/**
	 * @param subEffect the subEffect to set
	 */
	public void setSubEffect(Effect subEffect)
	{
		this.subEffect = subEffect;
	}

	/**
	 * 
	 * @param effectId
	 * @return true or false
	 */
	public boolean containsEffectId(int effectId)
	{
		synchronized (sucessEffects)
		{
			for(EffectTemplate template : sucessEffects.values())
			{
				if(template.getEffectid() == effectId)
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Correct lifecycle of Effect
	 *  - INITIALIZE
	 *  - APPLY
	 *  - START
	 *  - END
	 */
	
	
	/**
	 * Do initialization with proper calculations
	 */
	public void initialize()
	{
		if(skillTemplate.getEffects() == null)
			return;

		for(EffectTemplate template : getEffectTemplates())
		{
			template.calculate(this);
			
			if (template instanceof DamageEffect)
				isDmgEffect = true;
			if (template instanceof DelayDamageEffect)
				isDelayDamage = true;
			if (template instanceof MpHealEffect)
				isMpHeal = true;
			if (template instanceof PulledEffect)
				isPulledEffect = true;
			if (template instanceof SimpleRootEffect)
				isSimpleRootEffect = true;
		}

		synchronized (sucessEffects)
		{
			for(EffectTemplate template : sucessEffects.values())
			{
				template.calculateSubEffect(this);
				template.calculateHate(this);
			}
		}
		
		if (sucessEffects.isEmpty())
		{
			if (getSkillType() == SkillType.PHYSICAL)
			{
				if (getAttackStatus() == AttackStatus.CRITICAL)	
					setAttackStatus(AttackStatus.CRITICAL_DODGE);
				else
					setAttackStatus(AttackStatus.DODGE);
			}
			else
			{
				if (getAttackStatus() == AttackStatus.CRITICAL)	
					setAttackStatus(AttackStatus.CRITICAL_RESIST);
				else
					setAttackStatus(AttackStatus.RESIST);
			}
			
			this.setReserved1(0);
		}
		
		//set spellstatus for sm_castspell_end packet
		switch(getAttackStatus())
		{
			case DODGE:
			case CRITICAL_DODGE:
				setSpellStatus(SpellStatus.DODGE);
				break;
			case PARRY:
			case CRITICAL_PARRY:
				if (getSpellStatus() == SpellStatus.NONE)
					setSpellStatus(SpellStatus.PARRY);
				break;
			case BLOCK:
			case CRITICAL_BLOCK:
				if (getSpellStatus() == SpellStatus.NONE)
					setSpellStatus(SpellStatus.BLOCK);
				break;
			case RESIST:
			case CRITICAL_RESIST:
				setSpellStatus(SpellStatus.RESIST);
				break;
		}
	}
	
	/**
	 * Apply all effect templates
	 */
	public void applyEffect()
	{
		if(skillTemplate.getEffects() == null || sucessEffects.isEmpty())
			return;
		
		synchronized (sucessEffects)
		{
			for(EffectTemplate template : sucessEffects.values())
			{
				if (getEffected() != null && getEffected().getLifeStats().isAlreadyDead() && !skillTemplate.hasResurrectEffect())
					continue;
				template.applyEffect(this);
				template.startSubEffect(this);
			}
		}
		
		/**
		 * broadcast final hate to all visible objects
		 */
		if(effectHate != 0)
		{
			if (getEffected() != null && effector.isEnemy(getEffected()) && !this.isDelayDamage())
				getEffected().getAggroList().addHate(effector, 1);
			effector.getController().broadcastHate(effectHate);
		}
	}
	/**
	 * Start effect which includes:
	 * - start effect defined in template
	 * - start subeffect if possible
	 * - activate toogle skill if needed
	 * - schedule end of effect
	 */
	public void startEffect(boolean restored)
	{
		this.isStopped = false;
		
		if (sucessEffects.isEmpty())
			return;
		
		synchronized (sucessEffects)
		{
			for(EffectTemplate template : sucessEffects.values())
			{
				template.startEffect(this);
			}
		}
		
		if(isToggle() && effector instanceof Player)
		{
			activateToggleSkill();			
		}
		
		if (!restored)
			duration = getEffectsDuration();
		if(duration == 0)
			return;
		
		endTime = (int) System.currentTimeMillis() + duration;

		task = ThreadPoolManager.getInstance().scheduleEffect((new Runnable()
		{
			@Override
			public void run()
			{
				endEffect();
			}
		}), duration);
	}
	
	/**
	 * Will activate toggle skill and start checking task
	 */
	private void activateToggleSkill()
	{
		PacketSendUtility.sendPacket((Player) effector, new SM_SKILL_ACTIVATION(getSkillId(), true));
	}
	
	/**
	 * Will deactivate toggle skill and stop checking task
	 */
	private void deactivateToggleSkill()
	{
		PacketSendUtility.sendPacket((Player) effector, new SM_SKILL_ACTIVATION(getSkillId(), false));
	}
	
	/**
	 * End effect and all effect actions
	 * This method is synchronized and prevented to be called several times
	 * which could cause unexpected behavior
	 */
	public synchronized void endEffect()
	{
		if(isStopped)
			return;

		if (sucessEffects == null)
		{
			Logger.getLogger(this.getClass()).warn("sucessEffects null for skillId: "+this.getSkillId());
			return;
		}
		synchronized (sucessEffects)
		{
			for(EffectTemplate template : sucessEffects.values())
			{
				template.endEffect(this);
			}
		}
		
		if(isToggle() && effector instanceof Player)
		{
			deactivateToggleSkill();
		}
		stopTasks();
		effected.getEffectController().clearEffect(this);	
		this.isStopped = true;
		this.addedToController = false;
	}

	/**
	 * Stop all scheduled tasks
	 */
	public void stopTasks()
	{
		if(task != null)
		{
			task.cancel(true);
			task = null;
		}
		
		if(checkTask != null)
		{
			checkTask.cancel(true);
			checkTask = null;
		}
		
		if(periodicTasks != null)
		{
			for(Future<?> periodicTask : this.periodicTasks)
			{
				if(periodicTask != null)
				{
					periodicTask.cancel(true);
					periodicTask = null;
				}
			}
			this.periodicTasks = null;
		}
		
		if(mpUseTask != null)
		{
			mpUseTask.cancel(true);
			mpUseTask = null;
		}
	}
	/**
	 * Time till the effect end
	 * 
	 * @return
	 */
	public int getElapsedTime()
	{
		int elapsedTime = endTime - (int)System.currentTimeMillis();
		return elapsedTime > 0 ? elapsedTime : 0;
	}
	
	/**
	 * PVP damage ration
	 * 
	 * @return
	 */
	public int getPvpDamage()
	{
		return skillTemplate.getPvpDamage();
	}
	
	public ItemTemplate getItemTemplate()
	{
		return itemTemplate;
	}

	/**
	 * Try to add this effect to effected controller
	 */
	public void addToEffectedController()
	{
		if (forbidAdding)
			return;
		if(!addedToController && effected != null && effected.getEffectController() != null)
		{
			effected.getEffectController().addEffect(this);
			addedToController = true;
		}
	}
	public void setForbidAdding(boolean bol)
	{
		this.forbidAdding = bol;
	}
	public void setAddedToController(boolean bol)
	{
		this.addedToController = bol;
	}

	/**
	 * @return the effectHate
	 */
	public int getEffectHate()
	{
		return effectHate;
	}

	/**
	 * @param effectHate the effectHate to set
	 */
	public void setEffectHate(int effectHate)
	{
		this.effectHate = effectHate;
	}

	/**
	 * @return the tauntHate
	 */
	public int getTauntHate()
	{
		return tauntHate;
	}

	/**
	 * @param tauntHate the tauntHate to set
	 */
	public void setTauntHate(int tauntHate)
	{
		this.tauntHate = tauntHate;
	}

	/**
	 * @param i
	 * @return actionObserver for this effect template
	 */
	public ActionObserver getActionObserver(int i)
	{
		if(actionObserver == null || actionObserver[i-1] == null)
			return null;
		return actionObserver[i-1];
	}

	/**
	 * @param observer the observer to set
	 */
	public void setActionObserver(ActionObserver observer, int i)
	{
		if(actionObserver == null)
			actionObserver = new ActionObserver[4];
		actionObserver[i-1] = observer;
	}

	public void addSucessEffect(EffectTemplate effect)
	{
		sucessEffects.put(effect.getPosition(), effect);
	}

	public boolean isInSuccessEffects(int position)
	{
		if (sucessEffects.get(position) != null)
			return true;
		
		return false;
	}
	/**
	 * @return
	 */
	public Collection<EffectTemplate> getSuccessEffect()
	{
		return sucessEffects.values();
	}
	
	public void clearSucessEffects()
	{
		this.sucessEffects.clear();
	}
	
	public void addAllEffectToSucess()
	{
		for(EffectTemplate template : getEffectTemplates())
		{
			sucessEffects.put(template.getPosition(), template);
		}
	}
	
	public int getEffectsDuration()
	{
		int duration = 0;
		
		synchronized (sucessEffects)
		{
			for(EffectTemplate template : sucessEffects.values())
			{
				int effectDuration = template.getDuration();
				if (template.getRandomTime() > 0)
					effectDuration -= Rnd.get(template.getRandomTime());
				duration = duration > effectDuration ? duration : effectDuration;
			}
		}
		if(effected instanceof Player && skillTemplate.getPvpDuration() != 0)
			duration = duration * skillTemplate.getPvpDuration() / 100;
		return duration;
	}
	
	public boolean isDmgEffect()
	{
		return this.isDmgEffect;
	}
	public boolean isDelayDamage()
	{
		return isDelayDamage;
	}
	public boolean isMpHeal()
	{
		return isMpHeal;
	}
	public boolean isPulledEffect()
	{
		return isPulledEffect;
	}
	public boolean isSimpleRootEffect()
	{
		return isSimpleRootEffect;
	}
	 
	public boolean isStance()
	{
		return skillTemplate.isStance();
	}
	
	public boolean isAvatar()
	{
		for (EffectTemplate et : skillTemplate.getEffects().getEffects())
		{
			if (et instanceof TransformEffect && ((TransformEffect)et).getTransformType() == TransformType.AVATAR)
				return true;
		}
		return false;
	}

	public TransformType getTransformType()
	{
		for (EffectTemplate et : skillTemplate.getEffects().getEffects())
		{
			if (et instanceof TransformEffect)
				return ((TransformEffect) et).getTransformType();
		}
		return null;
	}
	
	public int getLaunchSkillId()
	{
		for (EffectTemplate et :getEffectTemplates())
		{
			if (et instanceof SkillLauncherEffect)
				return ((SkillLauncherEffect)et).getLaunchSkillId();
		}
		return 0;
	}
	/**
	 * coordinates for point skills
	 * @return
	 */
	public float getX()
	{
		return this.x;
	}
	public void setX(float x)
	{
		this.x = x;
	}
	public float getY()
	{
		return this.y;
	}
	public void setY(float y)
	{
		this.y = y;
	}
	public float getZ()
	{
		return this.z;
	}
	public void setZ(float z)
	{
		this.z = z;
	}
	public int getHealValue()
	{
		return this.healValue;
	}
	public void setHealValue(int healValue)
	{
		this.healValue = healValue;
	}
	public int getMpValue()
	{
		return this.mpValue;
	}
	public void setMpValue(int mpValue)
	{
		this.mpValue = mpValue;
	}
	public int gethotValue()
	{
		return this.hotValue;
	}
	public void sethotValue(int hotValue)
	{
		this.hotValue = hotValue;
	}
	public int getmotValue()
	{
		return this.motValue;
	}
	public void setmotValue(int motValue)
	{
		this.motValue = motValue;
	}
	
	/**
	 * Number of signets bursted with SignetBurstEffect
	 * @return
	 */
	public int getSignetBursted()
	{
		return this.signetBursted;
	}
	public void setSignetBursted(int value)
	{
		this.signetBursted = value;
	}
	/**
	 * Number of signets carved on target
	 * @return
	 */
	public int getCarvedSignet()
	{
		return this.carvedSignet;
	}
	public void setCarvedSignet(int value)
	{
		this.carvedSignet = value;
	}
	
	/**
	 * reflected damage
	 * @return
	 */
	public int getReflectorDamage()
	{
		return this.reflectedDamage;
	}
	public void setReflectorDamage(int value)
	{
		this.reflectedDamage = value;
	}
	public int getReflectorSkillId()
	{
		return this.reflectorSkillId;
	}
	public void setReflectorSkillId(int value)
	{
		this.reflectorSkillId = value;
	}
	
	
	public DamageType getDamageType()
	{
		return this.damageType;
	}
	public void setDamageType(DamageType damageType)
	{
		this.damageType = damageType;
	}
	/**
	 * adjust chance to crit
	 * @return
	 */
	public float getCriticalProb()
	{
		return this.criticalProb;
	}
	public void setCriticalProb(float criticalProb)
	{
		this.criticalProb = criticalProb;
	}
	public int getAccModBoost()
	{
		return this.accModBoost;
	}
	public void setAccModBoost(int accModBoost)
	{
		this.accModBoost = accModBoost;
	}
	public void setDashParam(DashParam dashParam)
	{
		this.dashType = dashParam;
	}
	public DashParam getDashParam()
	{
		return this.dashType;
	}
}