/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openaion.gameserver.model;

import org.openaion.gameserver.configs.main.GSConfig;
/**
 * 
 * Chat types that are supported by aion.
 * 
 * @author SoulKeeper
 */
public enum ChatType
{

	/**
	 * Normal chat (white)
	 */
	NORMAL(0x00),

	/**
	 * Shout chat (orange)
	 */
	SHOUT(0x03),

	/**
	 * Whisper chat (green)
	 */
	WHISPER(0x04),

	/**
	 * Group chat (blue)
	 */
	GROUP(0x05),

	/**
	 * Group chat (aqua)
	 */
	ALLIANCE(0x06),

	/**
	 * Group Leader chat
	 */
	GROUP_LEADER(0x07),

	/**
	 * [1.9.0.3]
	 * Legion chat (green)
	 * [2.0.0.3]
	 * League chat (dark blue)
	 */
	LEGION(0x08),

	/**
	 * [2.0.0.3]
	 * Legion chat (green)
	 */
	LEGION_2(0x0A),

	/**
	 * Shout chat? (orange)
	 */
	SHOUT_2(0x0C),

	/**
	 * Announce chat (yellow)
	 */
	ANNOUNCEMENTS(0x19, true),

	/**
	 * Periodically Notice chat (white)
	 */
	PERIOD_NOTICE(0x20, true),

	/**
	 * Periodically Announce chat (yellow)
	 */
	PERIOD_ANNOUNCEMENTS(0x1C, true),

	/**
	 * [1.9.0.3]
	 * Notice chat (yellow with box over players head)
	 */
	SYSTEM_NOTICE(0x21, true),

	/**
	 * [2.0.0.3]
	 * Notice chat (yellow with box over players head)
	 */
	SYSTEM_NOTICE_2(0x22, true);

	/**
	 * Chat type storage
	 */
	private final int	intValue;

	/**
	 * Check whether all races can read chat
	 */
	private boolean	sysMsg;

	/**
	 * Constructor
	 * 
	 * @param intValue
	 *            client chat type integer representation
	 */
	private ChatType(int intValue)
	{
		this(intValue, false);
	}

	/**
	 * Converts ChatType value to integer representation
	 * 
	 * @return chat type in client
	 */
	public int toInteger()
	{
		return intValue;
	}

	/**
	 * Returns ChatType by it's integer representation
	 * 
	 * @param integerValue
	 *            integer value of chat type
	 * @return ChatType
	 * @throws IllegalArgumentException
	 *             if can't find suitable chat type
	 */
	public static ChatType getChatTypeByInt(int integerValue) throws IllegalArgumentException
	{
		for(ChatType ct : ChatType.values())
		{
			if(ct.toInteger() == integerValue)
			{
				return ct;
			}
		}

		throw new IllegalArgumentException("Unsupported chat type: " + integerValue);
	}

	private ChatType(int intValue, boolean sysMsg)
	{
		if(GSConfig.SERVER_VERSION.startsWith("2."))
			if(intValue == 0x08)
				intValue = 0x0A;
			else if(intValue == 0x21)
					intValue = 0x22;

		this.intValue = intValue;
		this.sysMsg = sysMsg;
	}

	/**
	 * 
	 * @return true if this is one of system message ( all races can read chat )
	 */
	public boolean isSysMsg()
	{
		return sysMsg;
	}
}
