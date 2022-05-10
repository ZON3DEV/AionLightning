/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.world;

import java.util.ArrayList;
import java.util.List;

import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.ObjectContainer;


/**
 * Just some part of map.
 * 
 * @author -Nemesiss-
 * 
 */
public class MapRegion extends ObjectContainer
{
	/**
	 * Region id of this map region [NOT WORLD ID!]
	 */
	private Integer				regionId;
	/**
	 * WorldMapInstance witch is parent of this map region.
	 */
	private WorldMapInstance		parent;
	/**
	 * Surrounding regions + self.
	 */
	private ArrayList<MapRegion>		neighbours	= new ArrayList<MapRegion>(9);
	
	private boolean regionActive = false;
	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param parent
	 */
	MapRegion(Integer id, WorldMapInstance parent)
	{
		this.regionId = id;
		this.parent = parent;
		this.neighbours.add(this);
	}

	/**
	 * Return World map id.
	 * 
	 * @return world map id
	 */
	public Integer getMapId()
	{
		return getParent().getMapId();
	}

	/** Return an instance of {@link World}, which keeps map, to which belongs this region */
	public World getWorld()
	{
		return getParent().getWorld();
	}
	/**
	 * Returns region id of this map region. [NOT WORLD ID!]
	 * 
	 * @return region id.
	 */
	public Integer getRegionId()
	{
		return regionId;
	}

	/**
	 * Returns WorldMapInstance witch is parent of this instance
	 * 
	 * @return parent
	 */
	public WorldMapInstance getParent()
	{
		return parent;
	}

	/**
	 * @return the neighbours
	 */
	public List<MapRegion> getNeighbours()
	{
		return neighbours;
	}

	/**
	 * Add neighbour region to this region neighbours list.
	 * 
	 * @param neighbour
	 */
	void addNeighbourRegion(MapRegion neighbour)
	{
		neighbours.add(neighbour);
	}

	@Override
	public void storeObject (AionObject object)
	{
		super.storeObject(object);
		
		if (!regionActive && object instanceof Player)
			regionActive = true;
	}

	@Override
	public void removeObject(AionObject object)
	{
		super.removeObject(object);
		
		if (getPlayersCount() == 0)
			regionActive = false;
	}
	
	public boolean isMapRegionActive()
	{
		for (MapRegion r : neighbours)
		{
			if (r.regionActive)
				return true;
		}
		return false;
	}
}
