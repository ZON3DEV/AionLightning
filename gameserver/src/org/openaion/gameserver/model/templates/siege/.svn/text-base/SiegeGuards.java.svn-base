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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sylar
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiegeGuards")
public class SiegeGuards
{
	
	@XmlElement(name = "siege_guard")
	protected List<SiegeGuardTemplate> siegeGuards;
	
	@XmlElement(name = "peace_guard")
	protected List<SiegeGuardTemplate> peaceGuards;
	
	@XmlElement(name = "rf_guard")
	protected List<SiegeGuardTemplate> reinforcementsGuards;

	/**
	 * @return the siegeGuards
	 */
	public List<SiegeGuardTemplate> getSiegeGuards()
	{
		return siegeGuards;
	}

	/**
	 * @return the peaceGuards
	 */
	public List<SiegeGuardTemplate> getPeaceGuards()
	{
		return peaceGuards;
	}

	/**
	 * @return the reinforcementsGuards
	 */
	public List<SiegeGuardTemplate> getReinforcementsGuards()
	{
		return reinforcementsGuards;
	}
	
}
