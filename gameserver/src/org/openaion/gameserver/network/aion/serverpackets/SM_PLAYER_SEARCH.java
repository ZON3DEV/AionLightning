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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * Sent to fill the search panel of a players social window<br />
 * I.E.: In response to a <tt>CM_PLAYER_SEARCH</tt>
 * @author Ben
 *
 */
public class SM_PLAYER_SEARCH extends AionServerPacket
{
	private static final Logger log = Logger.getLogger(SM_PLAYER_SEARCH.class);
	
	private List<Player> players;
	private int region;
	private int status;
	
	/**
	 * Constructs a new packet that will send these players
	 * @param players List of players to show
	 * @param region of search - should be passed as parameter
	 * to prevent null in player.getActiveRegion()
	 * 
	 */
	public SM_PLAYER_SEARCH(List<Player> players, int region)
	{
		this.players = new ArrayList<Player>(players);
		this.region = region;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, players.size());
		for (Player player : players)
		{
			if(player.getActiveRegion() == null)
			{
				log.warn("CHECKPOINT: null active region for " + player.getObjectId() 
					+ "-" + player.getX() + "-" + player.getY() + "-" + player.getZ());
			}
			writeD(buf, player.getActiveRegion() == null ? region : player.getActiveRegion().getMapId());
			writeF(buf, player.getPosition().getX());
			writeF(buf, player.getPosition().getY());
			writeF(buf, player.getPosition().getZ());
			writeC(buf, player.getPlayerClass().getClassId());
			writeC(buf, player.getGender().getGenderId());
			writeC(buf, player.getLevel());
			if(player.isLookingForGroup())
				status = 2;
			else if(player.isInGroup())
				status = 3;
			else
				status = 0;
			writeC(buf, status); // Status. 2 = LFG, 3 = In group, others = solo
			writeS(buf, player.getName());
			byte[] unknown = new byte[52 - (player.getName().length()*2 +2)]; // What on earth is this nonsense?
			writeB(buf, unknown);
			
		}
	}
	
}
