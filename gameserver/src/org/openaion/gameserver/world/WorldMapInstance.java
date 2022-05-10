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

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.ObjectContainer;
import org.openaion.gameserver.model.group.PlayerGroup;


/**
 * World map instance object.
 *
 * @author -Nemesiss-
 *
 */
public class WorldMapInstance extends ObjectContainer
{
	/**
	 * Size of region
	 */
	public static final int			regionSize			= 500;
	/**
	 * Max world size - actually it must be some value bigger than world size. Used only for id generation.
	 */
	private static final int		maxWorldSize		= 10000;
	/**
	 * WorldMap witch is parent of this instance.
	 */
	private WorldMap				parent;
	/**
	 * Map of active regions.
	 */
	private Map<Integer, MapRegion>	regions				= Collections.synchronizedMap(new HashMap<Integer, MapRegion> ());

	private Set<Integer>			registeredObjects	= Collections.newSetFromMap(Collections.synchronizedMap(new HashMap<Integer, Boolean>()));

	private PlayerGroup				registeredGroup		= null;

	private Future<?>				emptyInstanceTask	= null;

	/**
	 * Id of this instance (channel)
	 */
	private int						instanceId;
	/**
	 * timer end time in case instance have timer
	 */
	private Calendar 				timerEnd 			= null;

	/**
	 * Constructor.
	 *
	 * @param parent
	 */
	public WorldMapInstance(WorldMap parent, int instanceId)
	{
		super();
		this.parent = parent;
		this.instanceId = instanceId;
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

	/**
	 * Returns WorldMap witch is parent of this instance
	 *
	 * @return parent
	 */
	public WorldMap getParent()
	{
		return parent;
	}

	/**
	 * Returns MapRegion that contains coordinates of VisibleObject. If the region doesn't exist, it's created.
	 *
	 * @param object
	 *
	 * @return a MapRegion
	 */
	MapRegion getRegion(VisibleObject object)
	{
		return getRegion(object.getX(), object.getY());
	}

	/**
	 * Returns MapRegion that contains given x,y coordinates. If the region doesn't exist, it's created.
	 *
	 * @param x
	 * @param y
	 * @return a MapRegion
	 */
	MapRegion getRegion(float x, float y)
	{
		Integer regionId = getRegionId(x, y);
		MapRegion region = regions.get(regionId);
		if(region == null)
		{
			synchronized(this)
			{
				region = regions.get(regionId);
				if(region == null)
				{
					region = createMapRegion(regionId);
				}
			}
		}
		return region;
	}

	/**
	 * Calculate region id from cords.
	 *
	 * @param x
	 * @param y
	 * @return region id.
	 */
	private Integer getRegionId(float x, float y)
	{
		return ((int) x) / regionSize * maxWorldSize + ((int) y) / regionSize;
	}

	/**
	 * Create new MapRegion and add link to neighbours.
	 *
	 * @param regionId
	 * @return newly created map region
	 */
	private MapRegion createMapRegion(Integer regionId)
	{
		MapRegion r = new MapRegion(regionId, this);
		regions.put(regionId, r);

		int rx = regionId / maxWorldSize;
		int ry = regionId % maxWorldSize;

		for(int x = rx - 1; x <= rx + 1; x++)
		{
			for(int y = ry - 1; y <= ry + 1; y++)
			{
				if(x == rx && y == ry)
					continue;
				int neighbourId = x * maxWorldSize + y;

				MapRegion neighbour = regions.get(neighbourId);
				if(neighbour != null)
				{
					r.addNeighbourRegion(neighbour);
					neighbour.addNeighbourRegion(r);
				}
			}
		}
		return r;
	}

	/**
	 * Returs {@link World} instance to which belongs this WorldMapInstance
	 * @return World
	 */
	public World getWorld()
	{
		return getParent().getWorld();
	}

	/**
	 * @return the instanceIndex
	 */
	public int getInstanceId()
	{
		return instanceId;
	}
	
	/**
	 *  Check player is in instance
	 *  
	 * @param objId
	 * @return
	 */
	public boolean isInInstance(int objId)
	{
		return allObjects.containsKey(objId);
	}
	
	public void registerGroup(PlayerGroup group) {
		registeredGroup = group;
		register(group.getGroupId());
	}

	/**
	 * @param objectId
	 */
	public void register(int objectId)
	{
		registeredObjects.add(objectId);
	}
	
	public void removeRegisteredGroup()
	{
		registeredGroup = null;
	}

	/**
	 * @param objectId
	 * @return
	 */
	public boolean isRegistered(int objectId)
	{
		return registeredObjects.contains(objectId);
	}
	
	/**
	 * @return the emptyInstanceTask
	 */
	public Future<?> getEmptyInstanceTask()
	{
		return emptyInstanceTask;
	}

	/**
	 * @param emptyInstanceTask the emptyInstanceTask to set
	 */
	public void setEmptyInstanceTask(Future<?> emptyInstanceTask)
	{
		this.emptyInstanceTask = emptyInstanceTask;
	}

	/**
	 * @return the registeredGroup
	 */
	public PlayerGroup getRegisteredGroup()
	{
		return registeredGroup;
	}

	/**
	 * @param timeInSeconds
	 */
	public void setTimerEnd(int timeInSeconds)
	{
		timerEnd = Calendar.getInstance();
		timerEnd.setTimeInMillis(Calendar.getInstance().getTimeInMillis() + timeInSeconds*1000);
	}

	/**
	 * @return
	 */
	public Calendar getTimerEnd()
	{
		return timerEnd;
	}
}
