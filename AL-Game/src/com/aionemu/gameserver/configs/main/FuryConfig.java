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

package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class FuryConfig {

	/**
	 * PvP Gp Reward
	 */
	@Property(key = "gameserver.pvp.gp.reward.quantity", defaultValue = "15")
	public static int PVP_GP_REWARD;
	/**
	 * Daily Map PvP Gp Reward
	 */
	@Property(key = "gameserver.dailypvp.gp.reward.quantity", defaultValue = "20")
	public static int DAILY_PVP_GP_REWARD;
	/**
	 * Crazy daeva PvP Gp Reward
	 */
	@Property(key = "gameserver.crazy.gp.reward.quantity", defaultValue = "25")
	public static int CRAZY_GP_REWARD;
	/**
	 * RvR Gp Reward
	 */
	@Property(key = "gameserver.rvr.gp.siege.quantity", defaultValue = "30")
	public static int GP_SIEGE_QUANTITY;
}
