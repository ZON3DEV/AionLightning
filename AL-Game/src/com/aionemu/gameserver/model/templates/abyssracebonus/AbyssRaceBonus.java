/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.model.templates.abyssracebonus;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

/**
 * @author Eloann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbyssRaceBonus", propOrder = { "bonus" })
public class AbyssRaceBonus {

	@XmlElement(name = "bonus")
	protected List<AbyssRacePenalty> bonus;
	@XmlAttribute(name = "id", required = true)
	protected int Id;
	@XmlAttribute(name = "name", required = true)
	private String name;
	@XmlAttribute(name = "race", required = true)
	private Race race;

	public List<AbyssRacePenalty> getPenalty() {
		if (bonus == null) {
			bonus = new ArrayList<AbyssRacePenalty>();
		}
		return bonus;
	}

	public int getId() {
		return Id;
	}

	public void setId(int value) {
		Id = value;
	}

	public String getName() {
		return name;
	}

	public Race getRace() {
		return race;
	}
}
