package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.controllers.attack.AttackUtil;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.skill.model.Effect;



/**
 * @author kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpellAtkDrainEffect")
public class SpellAtkDrainEffect extends AbstractOverTimeEffect
{
	@XmlAttribute(name = "hp")
	protected int HPpercent;
	
	@Override
	public void calculate(Effect effect)
	{
		//calculate damage
		int valueWithDelta = value + delta * effect.getSkillLevel();
		int damage = AttackUtil.calculateMagicalOverTimeResult(effect, valueWithDelta, element, this.position, true);
		effect.setReserved4(damage);
		
		super.calculate(effect, null, null); 
	}
	
	@Override
	public void onPeriodicAction(Effect effect)
	{
		effect.getEffected().getController().onAttack(effect.getEffector(), effect.getSkillId(), TYPE.HP, effect.getReserved4(), 130, effect.getAttackStatus(), false, true);
		if(HPpercent > 0)
		{
			effect.getEffector().getLifeStats().increaseHp(TYPE.HP, effect.getReserved4() * HPpercent / 100, effect.getSkillId(), 130);
		}
	}
}
