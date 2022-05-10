package org.openaion.loginserver.network;

import org.openaion.commons.network.NioServer;
import org.openaion.commons.network.ServerCfg;
import org.openaion.loginserver.configs.Config;
import org.openaion.loginserver.network.aion.AionConnectionFactoryImpl;
import org.openaion.loginserver.network.gameserver.GsConnectionFactoryImpl;
import org.openaion.loginserver.utils.ThreadPoolManager;


/**
 * This class create NioServer and store its instance.
 * 
 * @author -Nemesiss-
 */
public class IOServer
{
	/**
	 * NioServer instance that will handle io.
	 */
	private final static NioServer	instance;

	static
	{
		ServerCfg aion = new ServerCfg(Config.LOGIN_BIND_ADDRESS, Config.LOGIN_PORT, "Aion Connections",
			new AionConnectionFactoryImpl());

		ServerCfg gs = new ServerCfg(Config.GAME_BIND_ADDRESS, Config.GAME_PORT, "Gs Connections",
			new GsConnectionFactoryImpl());

		instance = new NioServer(Config.NIO_READ_THREADS, ThreadPoolManager.getInstance(), gs, aion);
	}

	/**
	 * @return NioServer instance.
	 */
	public static NioServer getInstance()
	{
		return instance;
	}
}
