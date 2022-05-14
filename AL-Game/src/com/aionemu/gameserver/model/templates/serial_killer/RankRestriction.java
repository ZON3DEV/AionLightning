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

package com.aionemu.gameserver.model.templates.serial_killer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Dtem
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RankRestriction", propOrder = { "penaltyAttr" })
public class RankRestriction {

	@XmlElement(name = "penalty_attr")
	protected List<RankPenaltyAttr> penaltyAttr;
	@XmlAttribute(name = "rank_num", required = true)
	protected int rankNum;
	@XmlAttribute(name = "restrict_direct_portal", required = true)
	protected boolean restrictDirectPortal;
	@XmlAttribute(name = "restrict_dynamic_bindstone", required = true)
	protected boolean restrictDynamicBindstone;

	/**
	 * @return the restrictDirectPortal
	 */
	public boolean isRestrictDirectPortal() {
		return restrictDirectPortal;
	}

	/**
	 * @param restrictDirectPortal
	 *            the restrictDirectPortal to set
	 */
	public void setRestrictDirectPortal(boolean restrictDirectPortal) {
		this.restrictDirectPortal = restrictDirectPortal;
	}

	/**
	 * @return the restrictDynamicBindstone
	 */
	public boolean isRestrictDynamicBindstone() {
		return restrictDynamicBindstone;
	}

	/**
	 * @param restrictDynamicBindstone
	 *            the restrictDynamicBindstone to set
	 */
	public void setRestrictDynamicBindstone(boolean restrictDynamicBindstone) {
		this.restrictDynamicBindstone = restrictDynamicBindstone;
	}

	public List<RankPenaltyAttr> getPenaltyAttr() {
		if (penaltyAttr == null) {
			penaltyAttr = new ArrayList<RankPenaltyAttr>();
		}
		return this.penaltyAttr;
	}

	public int getRankNum() {
		return rankNum;
	}

	public void setRankNum(int value) {
		this.rankNum = value;
	}
}
