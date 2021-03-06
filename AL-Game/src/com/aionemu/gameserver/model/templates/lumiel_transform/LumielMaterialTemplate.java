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
package com.aionemu.gameserver.model.templates.lumiel_transform;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "LumielMaterialTemplate")
public class LumielMaterialTemplate {

	@XmlAttribute(name = "id")
	protected int id;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "lumiel_id")
	protected int lumielId;
	@XmlAttribute(name = "item_id")
	protected int itemId;
	@XmlAttribute(name = "point")
	protected int point;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getLumielId() {
		return lumielId;
	}

	public int getItemId() {
		return itemId;
	}

	public int getPoint() {
		return point;
	}
}
