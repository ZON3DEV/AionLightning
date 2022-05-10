/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.utils;

import org.openaion.commons.objects.filter.ObjectFilter;
import org.openaion.gameserver.model.ChatType;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.legion.Legion;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_MESSAGE;
import org.openaion.gameserver.utils.Util;

/**
 * This class contains static methods, which are utility methods, all of them are interacting only with objects passed
 * as parameters.<br>
 * These methods could be placed directly into Player class, but we want to keep Player class as a pure data holder.<br>
 * 
 * @author Luno
 * 
 */
public class PacketSendUtility
{
	/**
	 * Sends message to player (used for system messages)
	 * 
	 * @param player
	 * @param msg
	 */
	public static void sendMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.ANNOUNCEMENTS));
	}

	/**
	 * Sends message to player (used for system notices)
	 * 
	 * @param player
	 * @param msg
	 */
	public static void sendSysMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.SYSTEM_NOTICE));
	}

	/**
	 * Send packet to this player.
	 * 
	 * @param player
	 * @param packet
	 */
	public static void sendPacket(Player player, AionServerPacket packet)
	{
		if(player != null && player.getClientConnection() != null)
			player.getClientConnection().sendPacket(packet);
	}

	/**
	 * Broadcast packet to all visible players.
	 * 
	 * @param player
	 * 
	 * @param packet
	 *            ServerPacket that will be broadcast
	 * @param toSelf
	 *            true if packet should also be sent to this player
	 */
	public static void broadcastPacket(Player player, AionServerPacket packet, boolean toSelf)
	{
		if(toSelf)
			sendPacket(player, packet);

		broadcastPacket(player, packet);
	}

	/**
	 * Broadcast packet to all visible players.
	 * 
	 * @param visibleObject
	 * @param packet
	 */
	public static void broadcastPacketAndReceive(VisibleObject visibleObject, AionServerPacket packet)
	{
		if(visibleObject instanceof Player)
			sendPacket((Player)visibleObject, packet);

		broadcastPacket(visibleObject, packet);
	}

	/**
	 * Broadcast packet to all Players from knownList of the given visible object.
	 * 
	 * @param visibleObject
	 * @param packet
	 */
	public static void broadcastPacket(VisibleObject visibleObject, final AionServerPacket packet)
	{
		visibleObject.getKnownList().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player obj)
			{
				sendPacket(obj, packet);
				return true;
			}
		}, true);
	}
	
	/**
	 * Broadcasts packet to all Players from knownList of the given visible object within the specified distance in meters
	 * 
	 * @param visibleObject
	 * @param packet
	 * @param distance
	 */
	public static void broadcastPacket(final VisibleObject visibleObject, final AionServerPacket packet, final int distance)
	{
		visibleObject.getKnownList().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player p)
			{
				if(MathUtil.getDistance(visibleObject, p) <= distance)
				{
					sendPacket(p, packet);
				}
				return true;
			}
		}, true);
	}

	/**
	 * Broadcasts packet to all visible players matching a filter
	 * 
	 * @param player
	 * 
	 * @param packet
	 *            ServerPacket to be broadcast
	 * @param toSelf
	 *            true if packet should also be sent to this player
	 * @param filter
	 *            filter determining who should be messaged
	 */
	public static void broadcastPacket(Player player, final AionServerPacket packet, boolean toSelf, final ObjectFilter<Player> filter)
	{
		if(toSelf)
		{
			sendPacket(player, packet);
		}

		player.getKnownList().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run (Player target)
			{
				if(filter.acceptObject(target))
					sendPacket(target, packet);
				return true;
			}
		}, true);
	}

	/**
	 * Broadcasts packet to all legion members of a legion
	 * 
	 * @param legion
	 *            Legion to broadcast packet to
	 * @param packet
	 *            ServerPacket to be broadcast
	 */
	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet)
	{
		for(Player onlineLegionMember : legion.getOnlineLegionMembers())
		{
			sendPacket(onlineLegionMember, packet);
		}
	}

	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet, int playerObjId)
	{
		for(Player onlineLegionMember : legion.getOnlineLegionMembers())
		{
			if(onlineLegionMember.getObjectId() != playerObjId)
				sendPacket(onlineLegionMember, packet);
		}
	}
}
