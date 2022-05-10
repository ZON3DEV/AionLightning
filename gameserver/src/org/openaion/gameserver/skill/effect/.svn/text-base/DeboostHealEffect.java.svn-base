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

import org.openaion.gameserver.skill.model.Effect;


/**
 * @author ViAl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeboostHealEffect")
public class DeboostHealEffect extends EffectTemplate
{
	@XmlAttribute(required = true)
	protected float percent;

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();		
	}
	
	@Override
	public void startEffect(Effect effect)
	{
		float healRate = effect.getEffected().getController().getHealRate();
		float newRate = healRate-percent/100;
		effect.getEffected().getController().setHealRate(newRate);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		float healRate = effect.getEffected().getController().getHealRate();
		effect.getEffected().getController().setHealRate(healRate+percent/100);
		super.endEffect(effect);
	}
}