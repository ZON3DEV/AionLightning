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

import org.openaion.gameserver.model.siege.SiegeRace;


/**
 * @author Sylar
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Guard")
public class SiegeGuardTemplate
{
	@XmlAttribute(name = "npcid_dr")
	protected int		npcid_Drakan;
	@XmlAttribute(name = "npcid_da")
	protected int		npcid_Asmodians;
	@XmlAttribute(name = "npcid_li")
	protected int 		npcid_Elyos;
	
	@XmlElement(name = "loc")
	protected List<SiegeSpawnLocationTemplate> spawnLocations;

	/**
	 * @return the spawnLocations
	 */
	public List<SiegeSpawnLocationTemplate> getSpawnLocations()
	{
		return spawnLocations;
	}
	
	public int getNpcId(SiegeRace race)
	{
		switch(race)
		{
			case ASMODIANS: return npcid_Asmodians;
			case BALAUR: return npcid_Drakan;
			case ELYOS: return npcid_Elyos;
			default: return 0;
		}
	}
	
}
