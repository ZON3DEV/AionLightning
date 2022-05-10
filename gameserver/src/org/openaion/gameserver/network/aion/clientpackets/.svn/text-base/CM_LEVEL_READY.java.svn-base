/**
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
package org.openaion.gameserver.network.aion.clientpackets;


import org.apache.log4j.Logger;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.windstreams.Location2D;
import org.openaion.gameserver.model.templates.windstreams.WindstreamTemplate;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_WINDSTREAM_LOCATIONS;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.services.WeatherService;
import org.openaion.gameserver.spawn.RiftSpawnManager;
import org.openaion.gameserver.world.World;


/**
 * Client is saying that level[map] is ready.
 * 
 * @author -Nemesiss-
 * @author Kwazar
 */
public class CM_LEVEL_READY extends AionClientPacket
{
	private static Logger	log	= Logger.getLogger(CM_LEVEL_READY.class);	
	
	/**
	 * Constructs new instance of <tt>CM_LEVEL_READY </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_LEVEL_READY(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		// empty
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		
		if(activePlayer.isSpawned() && activePlayer.getOldWorldId()==activePlayer.getWorldId())
		{
			log.info("[ANTICHEAT] Fake CM_LEVEL_READY sent by player: " + activePlayer.getName());
			return;
		}

			
		WindstreamTemplate template = DataManager.WINDSTREAM_DATA.
			getStreamTemplate(activePlayer.getPosition().getMapId());
		Location2D location;
		if(template != null)
			for(int i = 0; i < template.getLocations().getLocation().size(); i++)
			{
				location = template.getLocations().getLocation().get(i);
				sendPacket(new SM_WINDSTREAM_LOCATIONS(location.getBidirectional(), 
					template.getMapid(), location.getId(), location.getBoost()));
			}
		location = null;
		template = null;
		sendPacket(new SM_PLAYER_INFO(activePlayer, false));
		activePlayer.getController().startProtectionActiveTask();

		/**
		 * Spawn player into the world.
		 */
		 // If already spawned, despawn before spawning into the world
		if(activePlayer.isSpawned())
			World.getInstance().despawn(activePlayer);
		World.getInstance().spawn(activePlayer);
		
		activePlayer.getController().refreshZoneImpl();
		
		/**
		 * Loading weather for the player's region
		 */
		WeatherService.getInstance().loadWeather(activePlayer);

		QuestEngine.getInstance().onEnterWorld(new QuestCookie(null, activePlayer, 0, 0));
		
		activePlayer.getController().onEnterWorld();
		// zone channel message
		sendPacket(new SM_SYSTEM_MESSAGE(1390122, activePlayer.getPosition().getInstanceId()));
		
		RiftSpawnManager.sendRiftStatus(activePlayer);
		
		activePlayer.getEffectController().updatePlayerEffectIcons();
		
	}
}
