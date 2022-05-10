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

import org.apache.log4j.Logger;


/**
 * Position of object in the world.
 * 
 * @author -Nemesiss-
 * 
 */
public class WorldPosition
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(WorldPosition.class);

	/**
	 * Map id.
	 */
	private int mapId;
	/**
	 * Map Region.
	 */
	private MapRegion	mapRegion;
	/**
	 * World position x
	 */
	private float		x;
	/**
	 * World position y
	 */
	private float		y;
	/**
	 * World position z
	 */
	private float		z;

	/**
	 * Value from 0 to 120 (120==0 actually)
	 */
	private byte		heading;
	/**
	 * indicating if object is spawned or not.
	 */
	private boolean		isSpawned	= false;

	/**
	 * Return World map id.
	 * 
	 * @return world map id
	 */
	public int getMapId()
	{
		if (mapId == 0)
			log.warn("WorldPosition has (mapId == 0) " + this.toString());
		return mapId;
	}

	/**
	 * @param mapId the mapId to set
	 */
	public void setMapId(int mapId)
	{
		this.mapId = mapId;
	}

	/**
	 * Return World position x
	 * 
	 * @return x
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * Return World position y
	 * 
	 * @return y
	 */
	public float getY()
	{
		return y;
	}

	/**
	 * Return World position z
	 * 
	 * @return z
	 */
	public float getZ()
	{
		return z;
	}

	/**
	 * Return map region
	 * 
	 * @return Map region
	 */
	public MapRegion getMapRegion()
	{
		return isSpawned ? mapRegion : null;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getInstanceId()
	{
		return mapRegion.getParent().getInstanceId();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getInstanceCount()
	{
		return mapRegion.getParent().getParent().getInstanceCount();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isInstanceMap()
	{
		return mapRegion.getParent().getParent().isInstanceType();
	}
	/**
	 * Return heading.
	 * 
	 * @return heading
	 */
	public byte getHeading()
	{
		return heading;
	}

	/**
	 * Returns the {@link World} instance in which this position is located. :D
	 * @return World
	 */
	public World getWorld()
	{
		return mapRegion.getWorld();
	}
	/**
	 * Check if object is spawned.
	 * 
	 * @return true if object is spawned.
	 */
	public boolean isSpawned()
	{
		return isSpawned;
	}

	/**
	 * Set isSpawned to given value.
	 * 
	 * @param val
	 */
	void setIsSpawned(boolean val)
	{
		isSpawned = val;
	}

	/**
	 * Set map region
	 * 
	 * @param r
	 *            - map region
	 */
	void setMapRegion(MapRegion r)
	{
		mapRegion = r;
	}

	/**
	 * Set world position.
	 * 
	 * @param newX
	 * @param newY
	 * @param newZ
	 * @param newHeading
	 *            Value from 0 to 120 (120==0 actually)
	 */
	void setXYZH(float newX, float newY, float newZ, byte newHeading)
	{
		x = newX;
		y = newY;
		z = newZ;
		heading = newHeading;
	}

	@Override
	public String toString()
	{
		return "WorldPosition [heading=" + heading + ", isSpawned=" + isSpawned + ", mapRegion=" + mapRegion + ", x="
			+ x + ", y=" + y + ", z=" + z + "]";
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof WorldPosition))
			return false;

		WorldPosition pos = (WorldPosition) o;
		return this.x == pos.x && this.y == pos.y && this.z == pos.z && this.isSpawned == pos.isSpawned
			&& this.heading == pos.heading && this.mapRegion == pos.mapRegion;
	}

	@Override
	public WorldPosition clone()
	{
		WorldPosition pos = new WorldPosition();
		pos.heading = this.heading;
		pos.isSpawned = this.isSpawned;
		pos.mapRegion = this.mapRegion;
		pos.mapId = this.mapId;
		pos.x = this.x;
		pos.y = this.y;
		pos.z = this.z;
		return pos;
	}
	
}
