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

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.network.aion.serverpackets.SM_GAME_TIME;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;


/**
 * @author ATracer
 * 
 */
public class GameTimeService
{
	private static Logger	log	= Logger.getLogger(GameTimeService.class);
	
	public static final GameTimeService getInstance()
	{
		return SingletonHolder.instance;
	}

	private final static int GAMETIME_UPDATE = 3 * 60000;

	private GameTimeService()
	{
		/**
		 * Update players with current game time
		 */
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				log.debug("Sending current game time to all players");
				World.getInstance().doOnAllPlayers(new Executor<Player>(){
					@Override
					public boolean run(Player object)
					{
						PacketSendUtility.sendPacket(object, new SM_GAME_TIME());
						return true;
					}
				});
			}
		}, GAMETIME_UPDATE, GAMETIME_UPDATE);
		
		log.info("GameTimeService started. Update interval:" + GAMETIME_UPDATE);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final GameTimeService instance = new GameTimeService();
	}
}
