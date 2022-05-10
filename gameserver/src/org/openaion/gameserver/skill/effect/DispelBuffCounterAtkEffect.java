/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAttribute;

import org.openaion.gameserver.controllers.attack.AttackUtil;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.SkillTargetSlot;


/**
 * @author ViAl
 *
 */
public class DispelBuffCounterAtkEffect extends DamageEffect
{
	@XmlAttribute
	protected int count;

	private int i = 0;
	
	@Override
	public void applyEffect(Effect effect)
	{
		effect.getEffected().getEffectController().removeEffectByTargetSlot(SkillTargetSlot.BUFF, i);
		super.applyEffect(effect);
	}

	@Override
	public void calculate(Effect effect)
	{
		i = 0;
		for (Effect ef : effect.getEffected().getEffectController().getAbnormalEffects())
		{
			if (ef.getTargetSlot() == SkillTargetSlot.BUFF.ordinal() && ef.getTargetSlotLevel() == 0)
			{
				if (i == count)
					break;
				i++;
			}
		}
		
		int newValue = 0;
		if (i == 1)
			newValue = value;
		else if (i > 1)
			newValue = value + ((value/2)*(i-1));

		int valueWithDelta = newValue + delta * effect.getSkillLevel();
				
		AttackUtil.calculateMagicalSkillAttackResult(effect, valueWithDelta, getElement(), applyActionModifiers(effect), true);
		
		super.calculate(effect, DamageType.MAGICAL, false);
	}

}
