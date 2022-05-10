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
package org.openaion.gameserver.skill.effect;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.SkillElement;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.siege.FortressGate;
import org.openaion.gameserver.model.templates.stats.NpcRank;
import org.openaion.gameserver.skill.change.Change;
import org.openaion.gameserver.skill.effect.modifier.ActionModifier;
import org.openaion.gameserver.skill.effect.modifier.ActionModifiers;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.HopType;
import org.openaion.gameserver.skill.model.PreeffectsMasks;
import org.openaion.gameserver.skill.model.SkillTemplate;
import org.openaion.gameserver.skill.model.SkillType;
import org.openaion.gameserver.skill.model.SpellStatus;
import org.openaion.gameserver.utils.stats.StatFunctions;

import javolution.util.FastList;


/**
 * @author ATracer
 * @edit kecimis
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Effect")
public abstract class EffectTemplate 
{

	protected ActionModifiers modifiers;
    protected List<Change> change;
    @XmlAttribute
    protected int effectid;
	@XmlAttribute(required = true)
	protected int duration;
	@XmlAttribute(name = "randomtime")
	protected int randomTime;
	@XmlAttribute(name = "e")
	protected int position;
	@XmlAttribute(name = "basiclvl")
	protected int basicLvl;
	@XmlAttribute(name = "element")
	protected SkillElement element = SkillElement.NONE;
	@XmlElement(name = "subeffect")
	protected SubEffect subEffect;
	@XmlAttribute(name = "hoptype")
	protected HopType hopType;
	@XmlAttribute(name = "hopa")
	protected int hopA;
	@XmlAttribute(name = "hopb")
	protected int hopB;
	@XmlAttribute(name = "onfly")
	protected boolean onFly;
	@XmlAttribute(name = "noresist")
	protected boolean noresist;
	@XmlAttribute(name = "preeffects_mask")
	protected int preeffectsMask;
	@XmlAttribute(name = "preeffect_prob")
	protected float preeffectProb = 1.0f;;
	@XmlAttribute(name = "critical_prob")
	protected float criticalProb = 1.0f;
	@XmlAttribute(name = "acc_mod")
	protected int accMod;
	@XmlAttribute(name = "cond_effect")
	protected String condEffect;
	
	/**
	 * @return the duration
	 */
	public int getDuration()
	{
		return duration;
	}
	
	/**
	 * @return the randomtime
	 */
	public int getRandomTime()
	{
		return randomTime;
	}
	

	/**
	 * @return the modifiers
	 */
	public ActionModifiers getModifiers()
	{
		return modifiers;
	}


	/**
	 * @return the change
	 */
	public List<Change> getChange()
	{
		return change;
	}

	/**
	 * @return the effectid
	 */
	public int getEffectid()
	{
		return effectid;
	}

	/**
	 * @return the position
	 */
	public int getPosition()
	{
		return position;
	}

	/**
	 * @return the basicLvl
	 */
	public int getBasicLvl()
	{
		return basicLvl;
	}

	/**
	 * @return the element
	 */
	public SkillElement getElement()
	{
		return element;
	}

	/**
	 * @return the onFly
	 */
	public boolean isOnFly()
	{
		return onFly;
	}
	
	/**
	 * @return the noresist
	 */
	public boolean isNoresist()
	{
		return noresist;
	}
	
	/**
	 * @return the preeffectsMask
	 */
	public int getPreeffectsMask()
	{
		return preeffectsMask;
	}
	
	/**
	 * @return the preeffectProb
	 */
	public float getPreeffectProb()
	{
		return preeffectProb;
	}
	
	/**
	 * @return the criticalProb
	 */
	public float getCriticalProb()
	{
		return criticalProb;
	}
	
	/**
	 * @return the accMod
	 * 
	 * bonus stat for accurancy or magical accurancy, gives higher or lower chance to hit
	 */
	public int getAccMod()
	{
		return accMod;
	}
	
	/**
	 * @return the condEffect
	 * 
	 * some effecttemplates are applied only if given target is under given abnormalstate
	 */
	public String getCondEffect()
	{
		return condEffect;
	}


	/**
	 * @param value
	 * @return only bonus damage from modifier
	 */
	protected int applyActionModifiers(Effect effect)
	{	
		if(modifiers == null)
			return 0;
		
		/**
		 * Only one of modifiers will be applied now
		 */
		for(ActionModifier modifier : modifiers.getActionModifiers())
		{
			if(modifier.check(effect))
				return modifier.analyze(effect);
		}
		
		return 0;
	}

	/**
	 *  Calculate effect result
	 *  
	 * @param effect
	 */
	public void calculate(Effect effect)
	{
		this.calculate(effect, null, null);
	}
	
	/**
	 *  Apply effect to effected 
	 *  
	 * @param effect
	 */
	public abstract void applyEffect(Effect effect);
	/**
	 *  Start effect on effected
	 *  
	 * @param effect
	 */
	public void startEffect(Effect effect){};
	
	
	/**
	 * method calculate common for all effecttemplates
	 * excluded effecttemplates or with exception
	 * summoneffects
	 * reflectoreffect
	 * movebehindattack
	 * 
	 * 
	 * @param effect
	 * @param statEnum
	 * @param spellStatus
	 */
	public void calculate(Effect effect, StatEnum statEnum, SpellStatus spellStatus)
	{
		//exception for passive skills and returnpoint effecttemplate (return scrolls)
		if (effect.getSkillTemplate().isPassive() 
			|| this instanceof ReturnPointEffect
			|| this instanceof RandomMoveLocEffect)
		{
			this.addSuccessEffect(effect, spellStatus);
			return;
		}
		
		//check resistance
		if (!calculateEffectResistRate(effect, statEnum))
		{
			if(!effect.isDmgEffect())
				effect.clearSucessEffects();
			
			return;
		}

		//check for given condition effect
		if (this.getCondEffect() != null && !checkCondEffect(effect))
			return;//do not apply effecttemplate if target is not in abnormalstate
		
		SkillType skillType = effect.getSkillType();
		
		//certain effects are magical by default
		if (isMagicalEffectTemp())
			skillType = SkillType.MAGICAL;
		
		//check dodge/resist
		if (!isNoresist())
		{
			int accMod = getAccMod();
			if (this instanceof SignetBurstEffect)
				accMod += effect.getAccModBoost();
			
			switch(skillType)
			{
				case PHYSICAL:
					//compute dodge rate
					if(Rnd.get( 0, 100 ) < StatFunctions.calculatePhysicalDodgeRate(effect.getEffector(), effect.getEffected(), accMod))
						return;

					break;
				default:
					//compute magical resist rate
					if(Rnd.get( 0, 100 ) < StatFunctions.calculateMagicalResistRate(effect.getEffector(), effect.getEffected(), accMod))
						return;
					break;
			}
		}

		//switch according to effecttemplate position
		switch(getPosition())
		{
			case 1:
			break;
			default:
				//check preeffects
				FastList<Integer> positions = PreeffectsMasks.getPositions(this.getPreeffectsMask());
				if (positions != null)
				{
					for(int pos : positions)
					{
						if (!effect.isInSuccessEffects(pos))
							return;
					}
				}
				
				//check preeffect probability
				if (Rnd.get(0, 100) > this.getPreeffectProb() * 100)
					return;
			break;	
		}

		this.addSuccessEffect(effect, spellStatus);

	}
	
	private void addSuccessEffect(Effect effect, SpellStatus spellStatus)
	{
		effect.addSucessEffect(this);
		if (spellStatus != null)
			effect.setSpellStatus(spellStatus);
	}
	
	private boolean checkCondEffect(Effect effect)
	{
		if (this.getCondEffect().contains("NON_FLYING"))
		{
			if (effect.getEffected() instanceof Player)
				return ((Player)effect.getEffected()).getFlyState() == 0;
		}
		else if (this.getCondEffect().contains("FLYING"))
		{
			if (effect.getEffected() instanceof Player)
				return ((Player)effect.getEffected()).getFlyState() == 1;
		}
		else
		{
			EffectId effectId = EffectId.valueOf(this.getCondEffect());
			if (effect.getEffected().getEffectController().isAbnormalSet(effectId))
				return true;
		}
		
		return false;
	}
	
	/**
	 * certain effects are magical even when used in physical skills
	 * it includes stuns from chanter/sin/ranger etc
	 * these effects(effecttemplates) are dependent on magical accuracy and magical resist
	 * @return
	 */
	private boolean isMagicalEffectTemp()
	{
		if (this instanceof SilenceEffect ||
			this instanceof SleepEffect ||
			this instanceof RootEffect ||
			this instanceof SnareEffect ||
			this instanceof StunEffect ||
			this instanceof PoisonEffect ||
			this instanceof BindEffect ||
			this instanceof BleedEffect ||
			this instanceof BlindEffect ||
			this instanceof DeboostHealEffect ||
			this instanceof ParalyzeEffect ||
			this instanceof SlowEffect
			)
			return true;
		
		return false;
	}
	
	
	
	/**
	 * 
	 * @param effect
	 */
	public void calculateSubEffect(Effect effect)
	{
		if(subEffect == null)
			return;
		
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(subEffect.getSkillId());
		int duration = template.getEffectsDuration();
		Effect newEffect = new Effect(effect.getEffector(), effect.getEffected(), template, (effect.getSignetBursted() > 0 ? effect.getSignetBursted():template.getLvl()), duration);
		newEffect.initialize();
		effect.setSpellStatus(newEffect.getSpellStatus());
		effect.setSubEffect(newEffect);
	}
	
	/**
	 *  Hate will be added to result value only if particular
	 *  effect template has success result
	 *  
	 * @param effect
	 */
	public void calculateHate(Effect effect)
	{	
		if(hopType == null)
			return;
		
		if(effect.getSuccessEffect().isEmpty())
			return;
		
		int currentHate = effect.getEffectHate();
		if(hopType != null)
		{
			switch(hopType)
			{
				case DAMAGE:
					currentHate += effect.getReserved1(); 
					break;
				case SKILLLV:
					int skillLvl = effect.getSkillLevel();
					currentHate += hopB + hopA * skillLvl; 
				default:
					break;
			}
		}
		if (currentHate == 0)
			currentHate = 1;
		effect.setEffectHate(StatFunctions.calculateHate(effect.getEffector(), currentHate));
	}
	
	/**
	 * 
	 * @param effect
	 */
	public void startSubEffect(Effect effect)
	{
		if(subEffect == null)
			return;
		
		effect.getSubEffect().applyEffect();
	}
	/**
	 *  Do periodic effect on effected
	 *  
	 * @param effect
	 */
	public void onPeriodicAction(Effect effect){};
	/**
	 *  End effect on effected
	 *  
	 * @param effect
	 */
	public void endEffect(Effect effect){};
		
	public boolean calculateEffectResistRate(Effect effect, StatEnum statEnum ) 
 	{
		int effectPower = 1000;

 		//first resist 
 		if (statEnum != null) 
 		{
 			if (effect.getEffected() == null || effect.getEffected().getGameStats() == null
 				|| effect.getEffector() == null || effect.getEffector().getGameStats() == null)
 			{
 				return false;
 			}

			if(statEnum == StatEnum.MAGICAL_RESIST)
			{
				Logger.getLogger(this.getClass()).debug("Et: "+this.toString()+" is calling calculateeffectresist rate for Magical_resist");
				return false;
			}
			
			switch (statEnum)
			{
				case BLIND_RESISTANCE:
				case CHARM_RESISTANCE:
				case CONFUSE_RESISTANCE:
				case CURSE_RESISTANCE:
				case DISEASE_RESISTANCE:
				case FEAR_RESISTANCE:
				case OPENAREIAL_RESISTANCE:
				case PARALYZE_RESISTANCE:
				case PERIFICATION_RESISTANCE:
				case ROOT_RESISTANCE:
				case SILENCE_RESISTANCE:
				case SLEEP_RESISTANCE:
				case SLOW_RESISTANCE:
				case SNARE_RESISTANCE:
				case SPIN_RESISTANCE:
				case STAGGER_RESISTANCE:
				case STUMBLE_RESISTANCE:
				case STUN_RESISTANCE:
				{
					//resist for bosses	// one of these is not needed?	
					if (effect.getEffected() instanceof Npc)
				    {
						NpcRank rank = ((Npc)effect.getEffected()).getObjectTemplate().getRank();
						if (rank == NpcRank.HERO || rank == NpcRank.LEGENDARY)
							return false;
						if(effect.getEffected() instanceof FortressGate)
							return false;
				    }
					//resist for bosses // one of these is not needed?		
			 		if(effect.getEffected().getGameStats().getCurrentStat(StatEnum.ALLRESIST) > 0)
			 			return false;
					if (effect.getEffected().getGameStats().getCurrentStat(StatEnum.ABNORMAL_RESISTANCE_ALL) > 0)
						effectPower -=effect.getEffected().getGameStats().getCurrentStat(StatEnum.ABNORMAL_RESISTANCE_ALL);	
				}
				case BLEED_RESISTANCE:
				case POISON_RESISTANCE:
				break;
			}
			
			int stat = effect.getEffected().getGameStats().getCurrentStat(statEnum);
			effectPower -= stat;
			
			//add boosts
			switch (statEnum)
			{
				case STAGGER_RESISTANCE:
					effectPower += effect.getEffector().getGameStats().getCurrentStat(StatEnum.STAGGER_BOOST);
					break;
				case STUMBLE_RESISTANCE:
					effectPower += effect.getEffector().getGameStats().getCurrentStat(StatEnum.STUMBLE_BOOST);
					break;
				case STUN_RESISTANCE:
					effectPower += effect.getEffector().getGameStats().getCurrentStat(StatEnum.STUN_BOOST);
					break;
			}
			
			int attackerLevel = effect.getEffector().getLevel();
 	 		int targetLevel = effect.getEffected().getLevel();
 	 		
 	 		/*                 
 	 		float multipler = 0.0f; 
 		    int differ = (targetLevel - attackerLevel); 
 		    //lvl mod 
 	 	    if(differ > 0 && differ < 8 ) 
 		    { 
 		        multipler = differ / 10f; 
 	 	        effectPower -= Math.round((effectPower * multipler)); 
 	        } 
 	        else if (differ >= 8) 
 	 	    { 
 		        effectPower -= Math.round((effectPower * 0.80f)); 
 		    } 
 		    if (effect.getEffected() instanceof Npc) 
 		    { 
 		        float hpGaugeMod = ((Npc) effect.getEffected()).getObjectTemplate().getHpGauge(); 
 		        effectPower -= (200*(1+(hpGaugeMod/10))); 
 		    } 
 		    */
 	 		if(targetLevel > attackerLevel)
 	 		{
 	 			int differ = targetLevel - attackerLevel;
 	 			effectPower -= effectPower * (differ / 10);
 	 		}
 	 		
			boolean result = (Rnd.get(0, 1000) < effectPower);
			
	 		return result;
 		}
 		else //statenum == null
 		{
 			//resist for bosses	
			if (effect.getEffected() instanceof Npc)
		    {
				//bosses are resistent to these effects
				if (this instanceof PulledEffect ||
					this instanceof BindEffect ||
					this instanceof DeformEffect)
				{
					NpcRank rank = ((Npc)effect.getEffected()).getObjectTemplate().getRank();
					if (rank == NpcRank.HERO || rank == NpcRank.LEGENDARY)
						return false;
					if(effect.getEffected() instanceof FortressGate)
						return false;
				}
		    }
 		}

		//full resist for overpowered mobs
		if(effect.getEffected().getGameStats().getCurrentStat(StatEnum.MAGICAL_DEFEND) >= 99999)
			return false;
		//full resist for overpowered mobs
		if(effect.getEffected().getGameStats().getCurrentStat(StatEnum.PHYSICAL_DEFENSE) >= 99999)
			return false;

 		return true;
	} 
}
