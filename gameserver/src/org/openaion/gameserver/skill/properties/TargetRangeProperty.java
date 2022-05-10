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

import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;
import org.openaion.gameserver.geo.GeoEngine;
import org.openaion.gameserver.model.alliance.PlayerAllianceMember;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.skill.model.CreatureWithDistance;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.utils.MathUtil;



/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetRangeProperty")
public class TargetRangeProperty
extends Property
{

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(TargetRangeProperty.class);
	
	@XmlAttribute(required = true)
	protected TargetRangeAttribute value;

	@XmlAttribute
	protected int distance;
	
	@XmlAttribute
	protected int maxcount;

	/**
	 * Gets the value of the value property.
	 *     
	 */
	public TargetRangeAttribute getValue() {
		return value;
	}
	public int getDistance() {
		return distance;
	}

	@Override
	public boolean set(final Skill skill)
	{
		final TreeSet<CreatureWithDistance> effectedList = skill.getEffectedList();
		
		skill.setTargetRangeAttribute(value);
		switch(value)
		{
			case ONLYONE:
				skill.setMaxEffected(1);
				break;
			case AREA:	
				final Creature firstTarget = skill.getFirstTarget();
				
				skill.setMaxEffected(maxcount);
				
				final float newDistance = (float)distance + (skill.getAddWeaponRangeProperty()?((float)skill.getEffector().getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f):0) + 1.5f;
				
				skill.getEffector().getKnownList().doOnAllObjects(new Executor<AionObject>(){	
					@Override
					public boolean run(AionObject nextObject)
					{
						// firstTarget is already added, look: FirstTargetProperty
						if(firstTarget == nextObject)
							return true;

						if(!(nextObject instanceof Creature))
							return true;
						
						Creature nextCreature = (Creature) nextObject;
						
						// Creature with no life stats are not supposed to be attacked
						if(nextCreature.getLifeStats() == null || nextCreature.getLifeStats().isAlreadyDead())
							return true;
						
						// TODO maybe better controller?
						if (skill.getTargetType() == 1)// point-area skill
						{
							if (MathUtil.getDistance(nextCreature, skill.getX(), skill.getY(), skill.getZ()) <= newDistance &&
								GeoEngine.getInstance().canSee(skill.getEffector().getWorldId(), skill.getX(), skill.getY(), skill.getZ(), nextCreature.getX(), nextCreature.getY(), nextCreature.getZ()))
								effectedList.add(new CreatureWithDistance(nextCreature, (float)MathUtil.getDistance(nextCreature, skill.getX(), skill.getY(), skill.getZ())));
						}
						else 
						{	
							if (MathUtil.isIn3dRange(firstTarget, nextCreature, newDistance) &&
								GeoEngine.getInstance().canSee(firstTarget, nextCreature))
								effectedList.add(new CreatureWithDistance(nextCreature, (float)MathUtil.getDistance(nextCreature, firstTarget)));
						}
						return true;
					}	
				}, true);
				if (skill.getFirstTarget() == null && effectedList.size() > 0)
					skill.setFirstTarget(effectedList.first().getCreature());
				break;
			case PARTY:
				if(skill.getEffector() instanceof Player)
				{
					skill.setMaxEffected((maxcount == 0 ? 6 : maxcount));
					
					if(skill.getMaxEffected() == effectedList.size())
						break;
					
					Player effector = (Player)skill.getEffector();
					if (effector.isInAlliance())
					{
						effectedList.clear();
						for(PlayerAllianceMember allianceMember : effector.getPlayerAlliance().getMembersForGroup(effector.getObjectId()))
						{
							if (!allianceMember.isOnline()) continue;
							Player member = allianceMember.getPlayer();
							if(MathUtil.isIn3dRange(effector, member, (float)(distance + 1.5))&&
								GeoEngine.getInstance().canSee(effector, member))
								effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
						}
					}
					else if (effector.isInGroup())
					{
						effectedList.clear();
						for(Player member : effector.getPlayerGroup().getMembers())
						{
							// TODO maybe better controller?
							if(member != null && MathUtil.isIn3dRange(effector, member, (float)(distance + 1.5))&&
								GeoEngine.getInstance().canSee(effector, member))
								effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
						}
					}
				}
				break;
			case NONE:
			case POINT:
				//no effecteds
				break;
			case PARTY_WITHPET:
				if(skill.getEffector() instanceof Player)
				{
					skill.setMaxEffected((maxcount == 0 ? 12 : maxcount));
					Player effector = (Player)skill.getEffector();
					if (effector.isInAlliance())
					{
						effectedList.clear();
						for(PlayerAllianceMember allianceMember : effector.getPlayerAlliance().getMembersForGroup(effector.getObjectId()))
						{
							if (!allianceMember.isOnline()) continue;
							Player member = allianceMember.getPlayer();
							if(MathUtil.isIn3dRange(effector, member, (float)(distance + 1.5)) &&
								GeoEngine.getInstance().canSee(effector, member))
							{
								effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
								if (member.getSummon() != null && GeoEngine.getInstance().canSee(effector, member.getSummon()))
									effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
							}
						}
					}
					else if (effector.isInGroup())
					{
						effectedList.clear();
						for(Player member : effector.getPlayerGroup().getMembers())
						{
							// TODO maybe better controller?
							if(member != null && MathUtil.isIn3dRange(effector, member, (float)(distance + 1.5)) &&
								GeoEngine.getInstance().canSee(effector, member))
							{
								effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
								if (member.getSummon() != null && GeoEngine.getInstance().canSee(effector, member.getSummon()))
									effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member.getSummon())));
							}
						}
					}
				}
				break;
		}
		return true;
	}
}
