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

import java.util.regex.Pattern;

import org.openaion.commons.configuration.Property;


public class GSConfig
{
	/**
	 * Server name
	 */
	@Property(key = "gameserver.name", defaultValue = "aion private")
	public static String		SERVER_NAME;

	/**
	 * Character name pattern (checked when character is being created)
	 */
	@Property(key = "gameserver.character.name.pattern", defaultValue = "[a-zA-Z]{2,16}")
	public static Pattern		CHAR_NAME_PATTERN;

	/**
	 * Pet name pattern (checked when pet is being created or renamed)
	 */
	@Property(key = "gameserver.pet.name.pattern", defaultValue = "[a-zA-Z]{2,16}")
	public static Pattern		PET_NAME_PATTERN;

	/**
	 * Server Country Code
	 */
	@Property(key = "gameserver.country.code", defaultValue = "1")
	public static int		SERVER_COUNTRY_CODE;
	
	/**
	 * Server Version
	 */
	@Property(key = "gameserver.version", defaultValue = "1.9.0.3")
	public static String		SERVER_VERSION;

	/**
	 * Server Mode
	 */
	@Property(key = "gameserver.mode", defaultValue = "1")
	public static int		SERVER_MODE;
	
	@Property(key = "gameserver.motd.revision.display", defaultValue = "false")
	public static boolean		SERVER_MOTD_DISPLAYREV;
	
	/**
	 * Disable chat server connection
	 */
	@Property(key = "gameserver.disable.chatserver", defaultValue = "true")
	public static boolean		DISABLE_CHAT_SERVER;
	
	@Property(key = "gameserver.log.chat", defaultValue = "false")
	public static boolean		LOG_CHAT;
	
	@Property(key = "gameserver.log.item", defaultValue = "false")
	public static boolean		LOG_ITEM;
	
	@Property(key = "gameserver.factions.ratio.limited", defaultValue = "false")
	public static boolean		FACTIONS_RATIO_LIMITED;
	
	@Property(key = "gameserver.factions.ratio.value", defaultValue = "50")
	public static int		FACTIONS_RATIO_VALUE;
	
	@Property(key = "gameserver.factions.ratio.level", defaultValue = "10")
	public static int		FACTIONS_RATIO_LEVEL;
	
	@Property(key = "gameserver.factions.ratio.minimum", defaultValue = "50")
	public static int		FACTIONS_RATIO_MINIMUM;
	
	@Property(key = "gameserver.lang", defaultValue = "en")
	public static String		LANG;

	@Property(key = "gameserver.disable.rdcserver", defaultValue = "false")
	public static boolean		DISABLE_RDC_SERVER;

	@Property(key = "gameserver.enable.freefly", defaultValue = "true")
    public static boolean FREEFLY;

	@Property(key = "gameserver.enable.repurchase", defaultValue = "true")
	public static boolean		ENABLE_REPURCHASE;

	@Property(key = "gameserver.enable.purchaselimit", defaultValue = "true")
	public static boolean		ENABLE_PURCHASE_LIMIT;

	@Property(key = "gameserver.purchaselimit.restock.time", defaultValue = "4")
	public static int		PURCHASE_LIMIT_RESTOCK_TIME;

	@Property(key = "gameserver.passkey.enable", defaultValue = "false")
	public static boolean		PASSKEY_ENABLE;

	@Property(key = "gameserver.passkey.wrong.maxcount", defaultValue = "5")
	public static int		PASSKEY_WRONG_MAXCOUNT;
	
	@Property(key = "gameserver.enable.geo", defaultValue = "false")
	public static boolean		GEODATA_ENABLED;
}