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
package org.openaion.gameserver.skill.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.model.Skill;



/**
 * @author ATracer
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetCondition")
public class TargetCondition
    extends Condition
{

    @XmlAttribute
    protected FlyRestrictionAttribute restriction;

    @Override
	public boolean verify(Skill skill)
	{
		if (skill.getTargetType() == 1)
			return true;
		
		if(skill.getFirstTarget() == null)
			return false;
		
		//0: regular, 1: fly, 2: glide
		if (skill.getFirstTarget() instanceof Player && restriction != null)
		{
			switch (restriction)
			{
				case GROUND:
					if (((Player)skill.getFirstTarget()).getFlyState() != 0)
						return false;
					break;
				case FLY:
					if (((Player)skill.getFirstTarget()).getFlyState() != 1)
						return false;
					break;
			}
		}
		
		return true;
	}
}
