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
package org.openaion.gameserver.model.templates.siege;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.siege.SiegeType;


/**
 * @author Sarynth
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiegeLocation")
public class SiegeLocationTemplate
{
	@XmlAttribute(name = "id")
	protected int		id;
	@XmlAttribute(name = "type")
	protected SiegeType	type;
	@XmlAttribute(name = "world")
	protected int		world;
	
	@XmlElement(name = "siege_reward")
	protected List<SiegeRewardTemplate> siegeRewards;
	
	@XmlElement(name = "defense_reward")
	protected DefenseReward defenseReward;
	
	@XmlAttribute(name = "artifact_cooldown")
	protected int artifactCooldown;
	
	/**
	 * @return the artifactCooldown
	 */
	public int getArtifactCooldown()
	{
		return artifactCooldown;
	}

	/**
	 * @return the location id
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * @return the location type
	 */
	public SiegeType getType()
	{
		return this.type;
	}
	
	/**
	 * @return the world id
	 */
	public int getWorldId()
	{
		return this.world;
	}
	
	public List<SiegeRewardTemplate> getSiegeRewards()
	{
		return this.siegeRewards;
	}
	
	public DefenseReward getDefenseReward()
	{
		return this.defenseReward;
	}
	
}
