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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import org.openaion.gameserver.model.NpcType;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.items.NpcEquippedGear;
import org.openaion.gameserver.model.templates.stats.KiskStatsTemplate;
import org.openaion.gameserver.model.templates.stats.NpcRank;
import org.openaion.gameserver.model.templates.stats.NpcStatsTemplate;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Felas
 * 
 */
public class LocationTemplate
{
	private int mapId;
	private float x = 0;
	private float y = 0;
	private float z = 0;

	public void setLocation(int mapID, float X, float Y, float Z)
	{
		this.mapId = mapID;
		this.x = X;
		this.y = Y;
		this.z = Z;
	}
	
	public int getMapId()
	{
		return this.mapId;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public float getZ()
	{
		return this.z;
	}
}
