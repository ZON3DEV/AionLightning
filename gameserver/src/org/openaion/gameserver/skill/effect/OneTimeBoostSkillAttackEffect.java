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

import org.openaion.gameserver.controllers.movement.AttackCalcObserver;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.SkillType;


/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OneTimeBoostSkillAttackEffect")
public class OneTimeBoostSkillAttackEffect extends BufEffect
{
	@XmlAttribute
	private int count;
	
	@XmlAttribute
	private SkillType type;
	
	@XmlAttribute
	private int value;
	
	@Override
	public void startEffect(final Effect effect)
	{
		super.startEffect(effect);
		
		final int stopCount = count;
		final float percent = 1.0f + value / 100.0f;
		AttackCalcObserver observer = null;
		
		switch (type)
		{
			case MAGICAL:
				observer = new AttackCalcObserver(){
					private int count = 0;
					@Override
					public float getBaseMagicalDamageMultiplier ()
					{
						if(count++ < stopCount)
						{
							return percent;
						}
						else
							effect.getEffected().getEffectController().removeEffect(effect.getSkillId());
						
						return 1.0f;
					}
				};
				break;
			case PHYSICAL:
				observer = new AttackCalcObserver(){
					private int count = 0;
					@Override
					public float getBasePhysicalDamageMultiplier ()
					{
						if(count++ < stopCount)
						{
							return percent;
						}
						else
							effect.getEffected().getEffectController().removeEffect(effect.getSkillId());
						
						return 1.0f;
					}
				};
				break;
		}
		
		effect.getEffected().getObserveController().addAttackCalcObserver(observer);
		effect.setAttackStatusObserver(observer, position);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		AttackCalcObserver observer = effect.getAttackStatusObserver(position);
		effect.getEffected().getObserveController().removeAttackCalcObserver(observer);
	}
}
