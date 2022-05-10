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
package org.openaion.gameserver.network.chatserver;

import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;
import org.openaion.commons.network.Dispatcher;
import org.openaion.commons.network.NioServer;
import org.openaion.gameserver.configs.network.NetworkConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_AUTH;
import org.openaion.gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_LOGOUT;
import org.openaion.gameserver.network.factories.CsPacketHandlerFactory;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author ATracer
 */
public class ChatServer
{
	private static final Logger			log				= Logger.getLogger(ChatServer.class);

	private ChatServerConnection		chatServer;
	private NioServer					nioServer;

	private boolean						serverShutdown	= false;
	
	public static final ChatServer getInstance()
	{
		return SingletonHolder.instance;
	}

	private ChatServer()
	{
	}
	
	public void setNioServer(NioServer nioServer)
	{
		this.nioServer = nioServer;
	}

	/**
	 * @return
	 */
	public ChatServerConnection connect()
	{
		SocketChannel sc;
		for(;;)
		{
			chatServer = null;
			log.info("Connecting to ChatServer: " + NetworkConfig.CHAT_ADDRESS);
			try
			{
				sc = SocketChannel.open(NetworkConfig.CHAT_ADDRESS);
				sc.configureBlocking(false);
				Dispatcher d = nioServer.getReadWriteDispatcher();
				CsPacketHandlerFactory csPacketHandlerFactory = new CsPacketHandlerFactory();
				chatServer = new ChatServerConnection(sc, d, csPacketHandlerFactory.getPacketHandler());
				return chatServer;
			}
			catch(Exception e)
			{
				log.info("Cant connect to ChatServer: " + e.getMessage());
			}
			try
			{
				/**
				 * 10s sleep
				 */
				Thread.sleep(10 * 1000);
			}
			catch(Exception e)
			{
			}
		}
	}

	/**
	 * This method is called when we lost connection to ChatServer.
	 */
	public void chatServerDown()
	{
		log.warn("Connection with ChatServer lost...");

		chatServer = null;

		if(!serverShutdown)
		{
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					connect();
				}
			}, 5000);
		}
	}

	/**
	 * @param player
	 * @param token
	 */
	public void sendPlayerLoginRequst(Player player)
	{
		if(chatServer != null)
			chatServer.sendPacket(new SM_CS_PLAYER_AUTH(player.getObjectId(), player.getAcountName()));
	}
	
	/**
	 * 
	 * @param player
	 */
	public void sendPlayerLogout(Player player)
	{
		if(chatServer != null)
			chatServer.sendPacket(new SM_CS_PLAYER_LOGOUT(player.getObjectId()));
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ChatServer instance = new ChatServer();
	}
}
