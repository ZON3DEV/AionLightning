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
package org.openaion.gameserver.skill.properties;

import java.util.Iterator;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.alliance.PlayerAllianceGroup;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.SkillAreaNpc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.model.CreatureWithDistance;
import org.openaion.gameserver.skill.model.Skill;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetRelationProperty")
public class TargetRelationProperty extends Property
{

	@XmlAttribute(required = true)
	protected TargetRelationAttribute	value;

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link TargetRelationAttribute }
	 * 
	 */
	public TargetRelationAttribute getValue()
	{
		return value;
	}

	@Override
	public boolean set(Skill skill)
	{
		TreeSet<CreatureWithDistance> effectedList = skill.getEffectedList();
		Creature effector = skill.getEffector();
		
		switch(value)
		{
			case ALL:
				break;
			case ENEMY:
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();

					if(effector.isEnemy(nextEffected))
						continue;

					iter.remove();
				}
				break;
			case FRIEND:
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();
					
					if (nextEffected instanceof SkillAreaNpc)
					{
						iter.remove();
						continue;
					}
					
					if(!effector.isEnemy(nextEffected))
						continue;
					
					iter.remove();
				}

				if(effectedList.size() == 0)
				{
					skill.setFirstTarget(skill.getEffector());
					effectedList.add(new CreatureWithDistance(skill.getEffector(), 0));
				}
				break;
			case MYPARTY:
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();

					Player skillEffector = null;
					if (effector.getActingCreature() instanceof Player)
						skillEffector = (Player)effector.getActingCreature();
										
					
					if (nextEffected instanceof Player && skillEffector != null)
					{
						Player player = (Player) nextEffected;
						if(skillEffector.isInAlliance())
						{
							PlayerAllianceGroup pag = skillEffector.getPlayerAlliance()
								.getPlayerAllianceGroupForMember(skillEffector.getObjectId());
							if(pag != null
								&& pag.isInSamePlayerAllianceGroup(skillEffector.getObjectId(), player.getObjectId()))
								continue;
						}
						else if(skillEffector.isInGroup())
						{
							if((skillEffector).getPlayerGroup() != null && player.getPlayerGroup() != null)
							{
								if((skillEffector).getPlayerGroup().getGroupId() == player.getPlayerGroup()
									.getGroupId())
									continue;
							}
						}
						else if(player == skillEffector)
							continue;

					}
					

					iter.remove();
				}

				if(effectedList.size() == 0)
				{
					skill.setFirstTarget(skill.getEffector());
					effectedList.add(new CreatureWithDistance(skill.getEffector(), 0));
				}
				break;
		}
		
		if(effectedList.size() > skill.getMaxEffected())
		{
			Iterator<CreatureWithDistance> iter = effectedList.iterator();
			int i = 1;
			while(iter.hasNext())
			{
				iter.next();
				if (i > skill.getMaxEffected())
					iter.remove();

				i++;
			}
		}
		return true;
	}
}
