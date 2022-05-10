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
package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Effect;


/**
 * @author ATracer
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillAtkDrainInstantEffect")
public class SkillAtkDrainInstantEffect extends DamageEffect
{
	@XmlAttribute(name = "hp")
	protected int HPpercent;
	@XmlAttribute(name = "mp")
	protected int MPpercent;

	@Override
	public void applyEffect(Effect effect)
	{
		super.applyEffect(effect);
		int value = 0;
		//TODO figure out logId
		if(HPpercent > 0)
		{
			value = effect.getReserved1() * HPpercent / 100;
			effect.getEffector().getLifeStats().increaseHp(TYPE.HP, value, effect.getSkillId(), 170);
		}
		
		if(MPpercent > 0)
		{
			value = effect.getReserved1() * MPpercent / 100;
			effect.getEffector().getLifeStats().increaseMp(TYPE.MP, value, effect.getSkillId(), 170);
		}
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect, DamageType.PHYSICAL, true);
	}
}
