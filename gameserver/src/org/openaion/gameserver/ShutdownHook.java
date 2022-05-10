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
package org.openaion.gameserver;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.ExitCode;
import org.openaion.commons.utils.concurrent.RunnableStatsManager;
import org.openaion.commons.utils.concurrent.RunnableStatsManager.SortBy;
import org.openaion.gameserver.configs.main.ShutdownConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.loginserver.LoginServer;
import org.openaion.gameserver.services.PeriodicSaveService;
import org.openaion.gameserver.services.PlayerService;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.utils.gametime.GameTimeManager;
import org.openaion.gameserver.world.World;


/**
 * @author lord_rex
 * 
 */
public class ShutdownHook extends Thread
{
	private static final Logger	log	= Logger.getLogger(ShutdownHook.class);

	public static ShutdownHook getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	@Override
	public void run()
	{
		if(ShutdownConfig.HOOK_MODE == 1)
		{
			shutdownHook(ShutdownConfig.HOOK_DELAY, ShutdownConfig.ANNOUNCE_INTERVAL, ShutdownMode.SHUTDOWN);
		}
		else if(ShutdownConfig.HOOK_MODE == 2)
		{
			shutdownHook(ShutdownConfig.HOOK_DELAY, ShutdownConfig.ANNOUNCE_INTERVAL, ShutdownMode.RESTART);
		}
		
		GameServer.nioServer.close();
	}

	public static enum ShutdownMode
	{
		NONE("terminating"),
		SHUTDOWN("shutting down"),
		RESTART("restarting");

		private final String	text;

		private ShutdownMode(String text)
		{
			this.text = text;
		}

		public String getText()
		{
			return text;
		}
	}

	private void sendShutdownMessage(final int seconds)
	{
		try
		{
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{					
					if(player != null && player.getClientConnection() != null && player.isSpawned())
					{
						player.getClientConnection().sendPacket(SM_SYSTEM_MESSAGE.SERVER_SHUTDOWN(seconds));
					}

					return true;
				}
			}, true);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}

	private void sendShutdownStatus(final boolean status)
	{
		try
		{
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{
					if(player != null && player.getClientConnection() != null)
						player.getController().setInShutdownProgress(status);
					return true;
				}
			}, true);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}

	private void shutdownHook(int duration, int interval, ShutdownMode mode)
	{
		// Disconnect login server from game.
		LoginServer.getInstance().gameServerDisconnected();
		
		for(int i = duration; i >= interval; i -= interval)
		{
			try
			{
				if(World.getInstance().getPlayersCount() > 0)
				{
					log.info("Runtime is " + mode.getText() + " in " + i + " seconds.");
					sendShutdownMessage(i);
					sendShutdownStatus(ShutdownConfig.SAFE_REBOOT);
				}
				else
				{
					log.info("Runtime is " + mode.getText() + " now ...");
					break; // fast exit.
				}

				if(i > interval)
				{
					sleep(interval * 1000);
				}
				else
				{
					sleep(i * 1000);
				}
			}
			catch(InterruptedException e)
			{
				return;
			}
		}
		
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			
			@Override
			public boolean run(Player activePlayer)
			{
				try
				{
					PlayerService.playerLoggedOut(activePlayer);					
				}
				catch(Exception e)
				{
					log.error("Error while saving player " + e.getMessage());
				}
				return true;
			}
		}, true);
		
		log.info("All players are disconnected...");
		
		RunnableStatsManager.dumpClassStats(SortBy.AVG);
		PeriodicSaveService.getInstance().onShutdown();

		// Save game time.
		GameTimeManager.saveTime();
		// ThreadPoolManager shutdown
		ThreadPoolManager.getInstance().shutdown();

		// Do system exit.
		if(mode == ShutdownMode.RESTART)
			Runtime.getRuntime().halt(ExitCode.CODE_RESTART);
		else
			Runtime.getRuntime().halt(ExitCode.CODE_NORMAL);

		log.info("Runtime is " + mode.getText() + " now...");
	}

	/**
	 * 
	 * @param delay
	 * @param announceInterval
	 * @param mode
	 */
	public void doShutdown(int delay, int announceInterval, ShutdownMode mode)
	{
		shutdownHook(delay, announceInterval, mode);
	}

	private static final class SingletonHolder
	{
		private static final ShutdownHook INSTANCE = new ShutdownHook();
	}
}
