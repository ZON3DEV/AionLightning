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
package org.openaion.gameserver.configs.main;

import org.openaion.commons.configuration.Property;

public class FallDamageConfig
{
	/**
	 * Fly damage activator
	 */
	@Property(key = "gameserver.fall.damage.active", defaultValue = "true")
	public static boolean	ACTIVE_FALL_DAMAGE;

	/**
	 * Percentage of damage per meter.
	 */
	@Property(key = "gameserver.fall.damage.percentage", defaultValue = "1.0")
	public static float		FALL_DAMAGE_PERCENTAGE;

	/**
	 * Minimum fall damage range
	 */
	@Property(key = "gameserver.fall.damage.distance.minimum", defaultValue = "10")
	public static int		MINIMUM_DISTANCE_DAMAGE;

	/**
	 * Maximum fall distance after which you will die after hitting the ground.
	 */
	@Property(key = "gameserver.fall.damage.distance.maximum", defaultValue = "50")
	public static int		MAXIMUM_DISTANCE_DAMAGE;

	/**
	 * Maximum fall distance after which you will die in mid air.
	 */
	@Property(key = "gameserver.fall.damage.distance.midair", defaultValue = "200")
	public static int		MAXIMUM_DISTANCE_MIDAIR;
}
