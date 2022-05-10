/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.skill.model;

import org.openaion.gameserver.model.gameobjects.Creature;

/**
 * @author kecimis
 *
 */
public class CreatureWithDistance implements Comparable<CreatureWithDistance>
{
	private Creature creature;
	private float distance = 0;
	
	public CreatureWithDistance(Creature creature, float distance)
	{
		this.creature = creature;
		this.distance = distance;
	}
	
	public Creature getCreature()
	{
		return this.creature;
	}
	
	public float getDistance()
	{
		return this.distance;
	}
	
	public void setCreature(Creature creature)
	{
		this.creature = creature;
	}
	
	public void setDistance(float distance)
	{
		this.distance = distance;
	}
	
	public boolean equals(Object o)
	{
		boolean	result = o!=null;
		result = (result)&&(o instanceof CreatureWithDistance);
		result = (result)&&(((CreatureWithDistance)o).getDistance()==this.distance);
		result = (result)&&(((CreatureWithDistance)o).getCreature().getObjectId()==this.creature.getObjectId());
		return result;
	}
	
	public int compareTo(CreatureWithDistance o)
	{
		int result = Math.round(this.distance - o.getDistance());
		
		if (result == 0)
			result = this.creature.getObjectId() - o.getCreature().getObjectId();
		
		return result;
	}
}
