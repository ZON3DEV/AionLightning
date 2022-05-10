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

package com.aionemu.gameserver.utils.stats;

import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author ATracer
 * @author Sarynth
 * @author Imaginary
 * @rework Ever' for 4.5
 */
@XmlEnum
public enum AbyssRankEnum {

	// Abyss Points
	GRADE9_SOLDIER(1, 300, 10, 90, 0, 0, 0, 1802431),
	GRADE8_SOLDIER(2, 414, 10, 103, 1200, 0, 0, 1802433),
	GRADE7_SOLDIER(3, 475, 10, 118, 4220, 0, 0, 1802435),
	GRADE6_SOLDIER(4, 546, 10, 136, 10990, 0, 0, 1802437),
	GRADE5_SOLDIER(5, 627, 10, 156, 23500, 0, 0, 1802439),
	GRADE4_SOLDIER(6, 721, 10, 180, 42780, 0, 0, 1802441),
	GRADE3_SOLDIER(7, 865, 10, 216, 69700, 0, 0, 1802443),
	GRADE2_SOLDIER(8, 1038, 10, 259, 105600, 0, 0, 1802445),
	GRADE1_SOLDIER(9, 1245, 10, 311, 150800, 0, 0, 1802447),
	// Glory Points
	STAR1_OFFICER(10, 1868, 10, 467, 1244, 1000, 14, 1802449),
	STAR2_OFFICER(11, 2241, 10, 560, 1368, 700, 27, 1802451),
	STAR3_OFFICER(12, 2577, 10, 644, 1915, 500, 55, 1802453),
	STAR4_OFFICER(13, 2964, 10, 741, 3064, 300, 98, 1802455),
	STAR5_OFFICER(14, 4446, 10, 1511, 5210, 100, 213, 1802457),
	GENERAL(15, 4890, 10, 1662, 8335, 30, 237, 1802459),
	GREAT_GENERAL(16, 5378, 10, 1828, 10002, 10, 244, 1802461),
	COMMANDER(17, 5916, 10, 2011, 11503, 3, 254, 1802463),
	SUPREME_COMMANDER(18, 7099, 10, 2413, 12437, 1, 294, 1802465);

	private int id;
	private int pointsGained;
	private int gpGained;
	private int pointsLost;
	private int required;
	private int quota;
	private int dailyReduceGp;
	private int descriptionId;

	/**
	 * @param id
	 * @param pointsGained
	 * @param pointsLost
	 * @param required
	 * @param quota
	 */
	private AbyssRankEnum(int id, int pointsGained, int gpGained, int pointsLost, int required, int quota, int dailyReduceGp, int descriptionId) {
		this.id = id;
		this.pointsGained = pointsGained;
		this.gpGained = gpGained;
		this.pointsLost = pointsLost;
		this.required = required * RateConfig.ABYSS_RANK_RATE;
		this.quota = quota;
		this.dailyReduceGp = dailyReduceGp;
		this.descriptionId = descriptionId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the pointsLost
	 */
	public int getPointsLost() {
		return pointsLost;
	}

	/**
	 * @return the pointsGained
	 */
	public int getPointsGained() {
		return pointsGained;
	}

	/**
	 * @return the gpGained
	 */
	public int getGpGained() {
		return gpGained;
	}

	/**
	 * @return AP required for Rank
	 */
	public int getRequired() {
		return required;
	}

	/**
	 * @return The quota is the maximum number of allowed player to have the
	 *         rank
	 */
	public int getQuota() {
		return quota;
	}

	public int getDailyReduceGp() {
		return dailyReduceGp;
	}

	public int getDescriptionId() {
		return descriptionId;
	}

	public static DescriptionId getRankDescriptionId(Player player) {
		int pRankId = player.getAbyssRank().getRank().getId();
		for (AbyssRankEnum rank : values()) {
			if (rank.getId() == pRankId) {
				int descId = rank.getDescriptionId();
				return (player.getRace() == Race.ELYOS) ? new DescriptionId(descId) : new DescriptionId(descId + 36);
			}
		}
		throw new IllegalArgumentException("No rank Description Id found for player: " + player);
	}

	/**
	 * @param id
	 * @return The abyss rank enum by his id
	 */
	public static AbyssRankEnum getRankById(int id) {
		for (AbyssRankEnum rank : values()) {
			if (rank.getId() == id) {
				return rank;
			}
		}
		throw new IllegalArgumentException("Invalid abyss rank provided" + id);
	}

	/**
	 * @param ap
	 * @return The abyss rank enum for his needed ap
	 */
	public static AbyssRankEnum getRankForAp(int ap) {
		AbyssRankEnum r = AbyssRankEnum.GRADE9_SOLDIER;
		for (AbyssRankEnum rank : values()) {
			if (rank.getRequired() <= ap) {
				r = rank;
			} else {
				break;
			}
		}
		return r;
	}

	/**
	 * @param gp
	 * @return The abyss rankGp enum for his needed gp
	 */
	public static AbyssRankEnum getRankForGp(int gp) {
		AbyssRankEnum rgp = AbyssRankEnum.STAR1_OFFICER;
		for (AbyssRankEnum rank : values()) {
			if (rank.getRequired() <= gp) {
				rgp = rank;
			} else {
				break;
			}
		}
		return rgp;
	}
}
