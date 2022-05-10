package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.controllers.attack.AttackUtil;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Effect;




/**
 * @author ATracer
 * @edit kecimis
 *  
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DamageEffect")
public abstract class DamageEffect
extends EffectTemplate
{

	@XmlAttribute(required = true)
	protected int value;

	@XmlAttribute
	protected int delta;
	
	@XmlAttribute(required = true)
	protected int value2;

	@XmlAttribute
	protected int delta2;
	
	@XmlAttribute
	protected int rng = 0;
	
	protected DamageType type;
	
	@Override
	public void applyEffect(Effect effect)
	{
		effect.getEffected().getController().onAttack(effect.getEffector(),
			effect.getSkillId(), TYPE.REGULAR, effect.getReserved1(), effect.getAttackStatus(), true);

		//notify observers
		notifyObservers(effect);
	}
	public void calculate(Effect effect, DamageType damageType, boolean calculate)
	{
		this.calculate(effect, damageType, calculate, true);
	}
	
	public void calculate(Effect effect, DamageType damageType, boolean calculate, boolean applyKnowledge)
	{
		//set type and critical prob
		effect.setDamageType(damageType);
		effect.setCriticalProb(criticalProb);
		
		Creature effector = effect.getEffector();
		
		if (!calculate)
		{
			super.calculate(effect);
			return;
		}
		
		int skillLvl = effect.getSkillLevel();
		int valueWithDelta = value + delta * skillLvl;
		
		if (effector instanceof Player)
		{
			//skills like Break Power are adjusted with weapon damage
			if (((Player)effector).getEquipment().getMainHandWeaponType() != null)
			{
				int max = effector.getGameStats().getCurrentStat(StatEnum.MAIN_MAX_DAMAGES);
				int min = effector.getGameStats().getCurrentStat(StatEnum.MAIN_MIN_DAMAGES);
				float multi = (max+min)/2f * 0.01f;
				valueWithDelta += (multi < 1.00f ? 1 : multi)*(value2 + delta2 * skillLvl);
			}
			else//without weapon
				valueWithDelta += (value2 + delta2 * skillLvl);
		}
		else//modifiers for npcs
			valueWithDelta += (value2 + delta2 * skillLvl);
		
		int bonusDamage = applyActionModifiers(effect);
	
		// apply pvp damage ratio
		if(effect.getEffected() instanceof Player && effect.getPvpDamage() != 0)
			valueWithDelta = Math.round(valueWithDelta * (effect.getPvpDamage() / 100f));
		
		switch(damageType)
		{
			case PHYSICAL:
				AttackUtil.calculatePhysicalSkillAttackResult(effect, valueWithDelta, bonusDamage, rng);
				break;
			case MAGICAL:
				AttackUtil.calculateMagicalSkillAttackResult(effect, valueWithDelta, getElement(), bonusDamage, applyKnowledge);
				break;
			default:
				AttackUtil.calculatePhysicalSkillAttackResult(effect, 0, bonusDamage, rng);
		}	
		
		super.calculate(effect);
	}
	
	public void notifyObservers(Effect effect)
	{
		effect.getEffector().getObserveController().notifyAttackObservers(effect.getEffected());
		effect.getEffected().getObserveController().notifyHittedObservers(effect.getEffector(), effect.getDamageType());
	}
	
}
