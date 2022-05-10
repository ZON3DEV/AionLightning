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
package org.openaion.gameserver.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_CHAT_INIT;
import org.openaion.gameserver.network.chatserver.ChatServer;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;


/**
 * @author ATracer
 * 
 */
public class ChatService
{
	private static final Logger		log		= Logger.getLogger(ChatService.class);

	private static Map<Integer, Player>	players	= new HashMap<Integer, Player>();

	private static byte[]					ip		= { 127, 0, 0, 1 };
	private static int						port	= 10241;

	/**
	 * Send token to chat server
	 * 
	 * @param player
	 */
	public static void onPlayerLogin(final Player player)
	{
		ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
				if(!isPlayerConnected(player))
				{
					ChatServer.getInstance().sendPlayerLoginRequst(player);
				}
				else
				{
					log.warn("Player already registered with chat server " + player.getName());
					// TODO do force relog in chat server?
				}
			}
		}, 10000);

	}

	/**
	 * Disonnect from chat server
	 * 
	 * @param player
	 */
	public static void onPlayerLogout(Player player)
	{
		players.remove(player.getObjectId());
		ChatServer.getInstance().sendPlayerLogout(player);
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isPlayerConnected(Player player)
	{
		return players.containsKey(player.getObjectId());
	}

	/**
	 * @param playerId
	 * @param token
	 */
	public static void playerAuthed(int playerId, byte[] token)
	{
		Player player = World.getInstance().findPlayer(playerId);
		if(player != null)
		{
			players.put(playerId, player);
			PacketSendUtility.sendPacket(player, new SM_CHAT_INIT(token));
		}
	}
	
	/**
	 * @return the ip
	 */
	public static byte[] getIp()
	{
		return ip;
	}

	/**
	 * @return the port
	 */
	public static int getPort()
	{
		return port;
	}

	/**
	 * @param ip the ip to set
	 */
	public static void setIp(byte[] _ip)
	{
		ip = _ip;
	}

	/**
	 * @param port the port to set
	 */
	public static void setPort(int _port)
	{
		port = _port;
	}
}
