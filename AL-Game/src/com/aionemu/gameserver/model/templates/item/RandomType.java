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

package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author vlog
 */
@XmlEnum
public enum RandomType {

	ENCHANTMENT,
	MANASTONE,
	MANASTONE_COMMON_GRADE_10(10),
	MANASTONE_COMMON_GRADE_20(20),
	MANASTONE_COMMON_GRADE_30(30),
	MANASTONE_COMMON_GRADE_40(40),
	MANASTONE_COMMON_GRADE_50(50),
	MANASTONE_COMMON_GRADE_60(60),
	MANASTONE_COMMON_GRADE_70(70),
	MANASTONE_RARE_GRADE_10(10),
	MANASTONE_RARE_GRADE_20(20),
	MANASTONE_RARE_GRADE_30(30),
	MANASTONE_RARE_GRADE_40(40),
	MANASTONE_RARE_GRADE_50(50),
	MANASTONE_RARE_GRADE_60(60),
	MANASTONE_RARE_GRADE_70(70),
	MANASTONE_LEGEND_GRADE_10(10),
	MANASTONE_LEGEND_GRADE_20(20),
	MANASTONE_LEGEND_GRADE_30(30),
	MANASTONE_LEGEND_GRADE_40(40),
	MANASTONE_LEGEND_GRADE_50(50),
	MANASTONE_LEGEND_GRADE_60(60),
	MANASTONE_LEGEND_GRADE_70(70),
	ANCIENTITEMS,
	CHUNK_EARTH,
	CHUNK_ROCK,
	CHUNK_SAND,
	CHUNK_GEMSTONE,
	SCROLLS,
	POTION;
	private int level;

	private RandomType() {
	}

	private RandomType(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
}
