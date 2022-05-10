/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.configs.main;

import org.openaion.commons.configuration.Property;

/**
 * @author rolandas
 *
 */
public class DropConfig
{
	/**
	 * formula type
	 */
	@Property(key = "gameserver.drop.chance.formula", defaultValue = "0")
	public static int		FORMULA_TYPE;
	
	/**
	 * Number of NPCs to store in player's kill history to use in more precise drop calculations
	 */
	@Property(key = "gameserver.drop.player.history", defaultValue = "20")
	public static int		NPC_DROP_HISTORY_COUNT;
	
	/**
	 * Minutes until NPC kill stats are reset for each player
	 */
	@Property(key = "gameserver.drop.history.expire", defaultValue = "15")
	public static int		NPC_DROP_EXPIRE_MINUTES;
	
	/**
	 * Disable drop rate reduction based on level difference between players and mobs
	 */
	@Property(key = "gameserver.disable.drop.reduction", defaultValue = "false")
	public static boolean		DISABLE_DROP_REDUCTION;
	
	/**
	 * World drop chance for common quality items in %
	 */
	@Property(key = "gameserver.world.drop.common", defaultValue = "0.01")
	public static float		WORLD_DROP_CHANCE_COMMON;
	
	/**
	 * World drop chance for rare quality items in %
	 */
	@Property(key = "gameserver.world.drop.rare", defaultValue = "0.005")
	public static float		WORLD_DROP_CHANCE_RARE;
	
	/**
	 * World drop chance for legendary quality items in %
	 */
	@Property(key = "gameserver.world.drop.legendary", defaultValue = "0.003")
	public static float		WORLD_DROP_CHANCE_LEGENDARY;
	
	/**
	 * World drop chance for unique quality items in %
	 */
	@Property(key = "gameserver.world.drop.unique", defaultValue = "0.003")
	public static float		WORLD_DROP_CHANCE_UNIQUE;
	
	/**
	 * Drop Quantity restriction depending item quality
	 */
	@Property(key = "gameserver.dropquantity.restriction.enabled", defaultValue = "false")
	public static boolean	DROPQUANTITY_RESTRICTION_ENABLED;
	
	/**
	 * Drop Quantity restriction for Blue items
	 */
	@Property(key = "gameserver.dropquantity.restriction.blue", defaultValue = "3")
	public static int		DROPQUANTITY_RESTRICTION_BLUE;

	/**
	 * Drop Quantity restriction for Gold items
	 */
	@Property(key = "gameserver.dropquantity.restriction.gold", defaultValue = "2")
	public static int		DROPQUANTITY_RESTRICTION_GOLD;

	/**
	 * Drop Quantity restriction for Orange items
	 */
	@Property(key = "gameserver.dropquantity.restriction.orange", defaultValue = "1")
	public static int		DROPQUANTITY_RESTRICTION_ORANGE;
	
	/**
	 * Chances are lowered with next item from the same item category
	 */
	@Property(key = "gameserver.itemcategory.restriction.enabled", defaultValue = "false")
	public static boolean	ITEMCATEGORY_RESTRICTION_ENABLED;
}
