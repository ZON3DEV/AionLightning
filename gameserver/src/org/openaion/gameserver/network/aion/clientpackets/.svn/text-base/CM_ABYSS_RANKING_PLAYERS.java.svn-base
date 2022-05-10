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

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.AbyssRankingResult;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_ABYSS_RANKING_PLAYERS;
import org.openaion.gameserver.services.AbyssRankingService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * In this packets aion client is asking for player abyss rankings
 * 
 * @author Sylar
 * 
 */
public class CM_ABYSS_RANKING_PLAYERS extends AionClientPacket
{

	private Race queriedRace;
	private int raceId;
		
	private static final Logger log = Logger.getLogger(CM_ABYSS_RANKING_PLAYERS.class);
	
	public CM_ABYSS_RANKING_PLAYERS(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		raceId = readC();
		switch(raceId)
		{
			case 0: queriedRace = Race.ELYOS; break;
			case 1: queriedRace = Race.ASMODIANS; break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		// calculate rankings and send packet
		if(queriedRace != null)
		{
			ArrayList<AbyssRankingResult> results = AbyssRankingService.getInstance().getInviduals(queriedRace);
			PacketSendUtility.sendPacket(getConnection().getActivePlayer(), new SM_ABYSS_RANKING_PLAYERS(results,queriedRace, getConnection().getActivePlayer()));
		}
		else
		{
			log.warn("Received invalid raceId: " + raceId);
		}
	}
}
