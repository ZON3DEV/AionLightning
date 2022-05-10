package org.openaion.loginserver.configs;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.openaion.commons.configuration.ConfigurableProcessor;
import org.openaion.commons.configuration.Property;
import org.openaion.commons.utils.PropertiesUtils;
import org.openaion.loginserver.utils.Util;


/**
 * @author -Nemesiss-
 * @author SoulKeeper
 */
public class Config
{
	/**
	 * Logger for this class.
	 */
	protected static final Logger	log	= Logger.getLogger(Config.class);

	/**
	 * Login Server port
	 */
	@Property(key = "loginserver.network.client.port", defaultValue = "2106")
	public static int				LOGIN_PORT;

	/**
	 * Login Server bind ip
	 */
	@Property(key = "loginserver.network.client.host", defaultValue = "*")
	public static String			LOGIN_BIND_ADDRESS;

	/**
	 * Login Server port
	 */
	@Property(key = "loginserver.network.gameserver.port", defaultValue = "9014")
	public static int				GAME_PORT;

	/**
	 * Login Server bind ip
	 */
	@Property(key = "loginserver.network.gameserver.host", defaultValue = "*")
	public static String			GAME_BIND_ADDRESS;

	/**
	 * Number of trys of login before ban
	 */
	@Property(key = "loginserver.network.client.logintrybeforeban", defaultValue = "5")
	public static int				LOGIN_TRY_BEFORE_BAN;

	/**
	 * Ban time in minutes
	 */
	@Property(key = "loginserver.network.client.bantimeforbruteforcing", defaultValue = "15")
	public static int				WRONG_LOGIN_BAN_TIME;

	/**
	 * Number of Threads that will handle io read (>= 0)
	 */
	@Property(key = "loginserver.network.nio.threads.read", defaultValue = "0")
	public static int				NIO_READ_THREADS;

	/**
	 * Number of Threads that will handle io write (>= 0)
	 */
	@Property(key = "loginserver.network.nio.threads.write", defaultValue = "0")
	public static int				NIO_WRITE_THREADS;

	/**
	 * Should server automaticly create accounts for users or not?
	 */
	@Property(key = "loginserver.accounts.autocreate", defaultValue = "true")
	public static boolean			ACCOUNT_AUTO_CREATION;
	
	/**
	 * Flood controller
	 */
	@Property(key = "loginserver.floodcontrol.maxconnection", defaultValue = "10")
	public static int			FLOOD_CONTROLLER_MAX_CONNECTION;
	
	@Property(key = "loginserver.floodcontrol.interval", defaultValue = "5")
	public static int			FLOOD_CONTROLLER_INTERVAL;
	
	@Property(key = "loginserver.floodcontrol.exceptions", defaultValue = "127.0.0.1")
	public static String			FLOOD_CONTROLLER_EXCEPTIONS;

	/**
	 * Load configs from files.
	 */
	public static void load()
	{
		try
		{
			Util.printSection("Network");
			String network = "./config/network";
			Properties[] props = PropertiesUtils.loadAllFromDirectory(network);
			
			ConfigurableProcessor.process(Config.class, props);
			log.info("Loading: " + network + "/network.properties");
			log.info("Loading: " + network + "/database.properties");
			log.info("Loading: " + network + "/floodcontroller.properties");
		}
		catch (Exception e)
		{
			log.fatal("Can't load loginserver configuration", e);
			throw new Error("Can't load loginserver configuration", e);
		}
	}
}
