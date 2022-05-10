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
package org.openaion.gameserver.dataholders;

import gnu.trove.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openaion.gameserver.model.templates.siege.SiegeSpawnList;


/**
 * @author Sylar
 */
@XmlRootElement(name = "siege_spawns")
@XmlAccessorType(XmlAccessType.FIELD)
public class SiegeSpawnData
{
	@XmlElement(name = "siege_spawn")
	private List<SiegeSpawnList> siegeLocationSpawnList;
	
	/**
	 *  Map that contains skillId - SkillTemplate key-value pair
	 */
	private TIntObjectHashMap<SiegeSpawnList> siegeLists = new TIntObjectHashMap<SiegeSpawnList>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		siegeLists.clear();
		for (SiegeSpawnList list : siegeLocationSpawnList)
		{
			siegeLists.put(list.getLocationId(), list);
		}
	}
	
	public int size()
	{
		return siegeLists.size();
	}
	
	public SiegeSpawnList getSpawnsForLocation(int locationId)
	{
		return siegeLists.get(locationId);
	}
}
