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

package com.aionemu.gameserver.model.base;

import java.sql.Timestamp;

/**
 * @author Himiko
 */
public class Base {

	private int mapid;
	private int baseId;
	private BaseRace race;
	private int legionId;
	private Timestamp lastTime;

	public Base(int mapid, int baseId, BaseRace race, int legionId, Timestamp lastTime) {

	}

	public int getMapId() {
		return mapid;
	}

	public int getBaseId() {
		return baseId;
	}

	public BaseRace getBaseRace() {
		return race;
	}

	public int getLegionId() {
		return legionId;
	}

	public Timestamp lastTime() {
		return lastTime;
	}

	public void setBaseRace(BaseRace race) {
		return;
	}

	public void setLegionId(int legionId) {

	}

	public void updata() {
	}
}
