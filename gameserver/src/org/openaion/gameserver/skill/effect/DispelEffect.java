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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.skill.model.DispelType;
import org.openaion.gameserver.skill.model.Effect;


/**
 * @author ATracer
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DispelEffect")
public class DispelEffect extends EffectTemplate
{
	@XmlElement(type = Integer.class)
	protected List<Integer>	effectids;
	@XmlElement
	protected List<String> effecttype;
	@XmlAttribute
	protected DispelType	dispeltype;
	@XmlAttribute
	protected Integer		value;

	@Override
	public void applyEffect(Effect effect)
	{
		if(effect.getEffected() == null || effect.getEffected().getEffectController() == null)
			return;
		
		if(dispeltype == null)
			return;
		
		if(dispeltype == DispelType.EFFECTID && effectids == null)
			return;
		
		if(dispeltype == DispelType.EFFECTTYPE && effecttype == null)
			return;

		switch(dispeltype)
		{
			case EFFECTID:
				if (effectids == null)
					return;
				
				for(Integer effectId : effectids)
				{
					effect.getEffected().getEffectController().removeEffectByEffectId(effectId);
				}
				break;
			case EFFECTTYPE:
				if (effecttype == null)
					return;
				
				for(String type : effecttype)
				{
					EffectId abnormalType = EffectId.getEffectIdByName(type);
					if(abnormalType != null && effect.getEffected().getEffectController().isAbnormalSet(abnormalType))
					{
						for (Effect ef : effect.getEffected().getEffectController().getAbnormalEffects())
						{
							if ((ef.getAbnormals() & abnormalType.getEffectId()) == abnormalType.getEffectId())
								ef.endEffect();
						}
					}
				}
				break;
		}
	}
}
