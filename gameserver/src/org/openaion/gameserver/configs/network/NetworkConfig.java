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
package org.openaion.gameserver.configs.network;

import java.net.InetSocketAddress;

import org.openaion.commons.configuration.Property;


public class NetworkConfig
{
	/**
	 * Game Server port
	 */
	@Property(key = "gameserver.network.client.port", defaultValue = "7777")
	public static int				GAME_PORT;

	/**
	 * Game Server bind ip
	 */
	@Property(key = "gameserver.network.client.host", defaultValue = "*")
	public static String			GAME_BIND_ADDRESS;
	
	/**
	 * RDC bind ip
	 */
	@Property(key = "gameserver.network.rdc.client.host", defaultValue = "*")
	public static String			RDC_BIND_ADDRESS;
	
	/**
	 * RDC bind port
	 */
	@Property(key = "gameserver.network.rdc.client.port", defaultValue = "732")
	public static int				RDC_BIND_PORT;
	
	@Property(key = "gameserver.network.geoserver.address", defaultValue = "localhost:5550")
	public static InetSocketAddress	GEOSERVER_ADDRESS;
	
	@Property(key = "gameserver.network.geoserver.password", defaultValue = "password")
	public static String			GEOSERVER_PASSWORD;
	
	/**
	 * Max allowed online players
	 */
	@Property(key = "gameserver.network.client.maxplayers", defaultValue = "100")
	public static int				MAX_ONLINE_PLAYERS;

	/**
	 * Required access level to login
	 */
	@Property(key = "gameserver.network.client.requiredlevel", defaultValue = "0")
	public static int				REQUIRED_ACCESS;

	/**
	 * LoginServer address
	 */
	@Property(key = "gameserver.network.login.address", defaultValue = "localhost:9014")
	public static InetSocketAddress	LOGIN_ADDRESS;
	
	/**
	 * ChatServer address
	 */
	@Property(key = "gameserver.network.chat.address", defaultValue = "localhost:9021")
	public static InetSocketAddress	CHAT_ADDRESS;
	
	/**
	 * Password for this GameServer ID for authentication at ChatServer.
	 */
	@Property(key = "gameserver.network.chat.password", defaultValue = "")
	public static String			CHAT_PASSWORD;

	/**
	 * GameServer id that this GameServer will request at LoginServer.
	 */
	@Property(key = "gameserver.network.login.gsid", defaultValue = "0")
	public static int				GAMESERVER_ID;

	/**
	 * Password for this GameServer ID for authentication at LoginServer.
	 */
	@Property(key = "gameserver.network.login.password", defaultValue = "")
	public static String			LOGIN_PASSWORD;

	/**
	 * Enabled debug information from network layer
	 */
	@Property(key = "gameserver.network.nio.debug", defaultValue = "false")
	public static boolean          NIO_DEBUG;
	
	/**
	 * Number of Threads that will handle io read (> 0)
	 */
	@Property(key = "gameserver.network.nio.threads.read", defaultValue = "1")
	public static int				NIO_READ_THREADS;

	/**
	 * Number of retries to do when reading a packet
	 */
	@Property(key = "gameserver.network.nio.threads.read.retries", defaultValue = "8")
	public static int                NIO_READ_RETRIES;
	
	/**
	 * Number of Threads that will handle io write (> 0)
	 */
	@Property(key = "gameserver.network.nio.threads.write", defaultValue = "1")
	public static int				NIO_WRITE_THREADS;

	/**
	 * Number of retries to do when writing a packet
	 */
	@Property(key = "gameserver.network.nio.threads.write.retries", defaultValue = "8")
	public static int                NIO_WRITE_RETRIES;
	
	@Property(key = "gameserver.network.display.unknownpackets", defaultValue = "false")
	public static boolean			DISPLAY_UNKNOWNPACKETS;

	/**
	 * Enable worker threads that will handle data processing
	 */
	@Property(key = "gameserver.network.nio.threads.workers.enable", defaultValue = "false")
	public static boolean	NIO_ENABLE_WORKERS;
	
	/**
	 * Number of worker threads by reader
	 */
	@Property(key = "gameserver.network.nio.threads.workers", defaultValue = "1")
	public static int NIO_WORKER_THREADS;
	
	/**
	 * Buffer count for workers
	 */
	@Property(key = "gameserver.network.nio.threads.workers.buffers", defaultValue = "16")
	public static int NIO_WORKER_THREAD_BUFFERS;
}
