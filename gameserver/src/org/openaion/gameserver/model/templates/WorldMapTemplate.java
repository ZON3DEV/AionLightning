/*
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
package org.openaion.gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.openaion.gameserver.world.WorldType;

/**
 * @author Luno
 * 
 */
@XmlRootElement(name="map")
@XmlAccessorType(XmlAccessType.NONE)
public class WorldMapTemplate
{
	/**
	 * Map name.
	 */
	@XmlAttribute(name = "name")
	private String	name	= "";

	/**
	 * Map nameid.
	 */
	@XmlAttribute(name = "nameid")
	private int	mapnameId = 0;

	/**
	 * Map Id.
	 */
	@XmlAttribute(name = "id", required = true)
	private Integer		mapId;

	/**
	 * Number of twin instances [players will be balanced so every one could exp easy]
	 */
	@XmlAttribute(name = "twin_count")
	private int		twinCount;

	/**
	 * Max user at twin instance.
	 */
	@XmlAttribute(name = "max_user")
	private int		maxUser;

	/**
	 * True if this map is a prison.
	 */
	@XmlAttribute(name = "prison")
	private boolean	prison	= false;

	/**
	 * True if this map is a instance.
	 */
	@XmlAttribute(name = "instance")
	private boolean	instance	= false;
	
	/**
	 * Return instanceMapId, 0 if not an instance
	 */
	@XmlAttribute(name = "instanceMapId")
	private int instanceMapId = 0;
	
	@XmlAttribute(name = "cooldown")
	private int cooldown = 0;

	/**
	 * The minimum Z coord, under this player die immediately
	 */
	@XmlAttribute(name = "death_level", required = true)
	private int	deathlevel	= 0;

	/**
	 * water level on map
	 */
	@XmlAttribute(name = "water_level", required = true)
	private int	waterlevel	= 16;
	
	/**
	 * world type of map
	 */
	@XmlAttribute(name = "world_type")
	private WorldType worldType = WorldType.NONE;
	
	/**
	 * world size - boundaries for geo engine
	 */
	@XmlAttribute(name = "world_size", required = true)
	private int			worldSize	= 0;

	public String getName()
	{
		return name;
	}
	
	public Integer getMapNameId()
	{
		return mapnameId;
	}

	public Integer getMapId()
	{
		return mapId;
	}

	public int getTwinCount()
	{
		return twinCount;
	}

	public int getMaxUser()
	{
		return maxUser;
	}

	public boolean isPrison()
	{
		return prison;
	}

	/**
	 * @return the instance
	 */
	public boolean isInstance()
	{
		return instance;
	}
	/**
	 * @return the waterlevel
	 */
	public int getWaterLevel()
	{
		return waterlevel;
	}
	/**
	 * @return the level of death :)
	 */
	public int getDeathLevel()
	{
		return deathlevel;
	}
	
	/**
	 * @return the WorldType
	 */
	public WorldType getWorldType()
	{
		return worldType;
	}

	/**
	 * @return the instanceMapId
	 */
	public int getInstanceMapId()
	{
		return instanceMapId;
	}
	
	public int getCooldown()
	{
		return cooldown;
	}
	
	public int getWorldSize()
	{
		return worldSize;
	}
}
