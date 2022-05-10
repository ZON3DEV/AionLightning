/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.openaion.gameserver.model.AbyssRankingResult;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.AbyssRankingService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Rhys2002, Sylar, LokiReborn
 */
public class SM_ABYSS_RANKING_PLAYERS extends AionServerPacket
{
	
	private ArrayList<AbyssRankingResult> 	data;
	private ArrayList<AbyssRankingResult>	dataTemp;
	private int 							race;
	private int action = 0;
	private int section = 1;
	private Player player;
	
	public SM_ABYSS_RANKING_PLAYERS(ArrayList<AbyssRankingResult> data, Race race, Player player)
	{
		this.data = data;
		dataTemp = new ArrayList<AbyssRankingResult>();
		this.race = race.getRaceId();
		this.player = player;
	}
	
	public SM_ABYSS_RANKING_PLAYERS(ArrayList<AbyssRankingResult> data, int race, int action, int section, Player player)
	{
		this.data = data;
		dataTemp = new ArrayList<AbyssRankingResult>();
		this.race = race;
		this.action = action;
		this.section = section;
		this.player = player;
	}
	
	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		int count = 0;
		
		writeD(buf, race);// 0:Elyos 1:Asmo
		writeD(buf, Math.round(AbyssRankingService.getInstance().getTimeOfUpdate() / 1000));//TODO Date
		writeD(buf, section);
		writeD(buf, action);// 0:Nothing 1:Update Table
		
		if(data.size() > 46)
			writeH(buf, 0x2E);
		else
			writeH(buf, data.size());
			
		
		for (AbyssRankingResult rs : data)
		{
			if(count >= 46)
			{
				dataTemp.add(rs);
			}	
			else
			{
				writeD(buf, rs.getTopRanking());// Current Rank
				writeD(buf, rs.getPlayerRank());// AbyssRank
				writeD(buf, rs.getOldRanking());// Old Rank, TODO: build history table and schedule hourly refresh
				writeD(buf, rs.getPlayerId()); // PlayerID
				writeD(buf, race);
				writeD(buf, rs.getPlayerClass().getClassId());// Class Id
				writeD(buf, 0); // Sex ? 0=male / 1=female
				writeD(buf, rs.getPlayerAP());// Abyss Points
				writeD(buf, 0); // Unk
				writeC(buf, rs.getPlayerLevel());
				writeC(buf, 0);
				
				writeS(buf, rs.getPlayerName());// Player Name

				writeB(buf, new byte[50 - (rs.getPlayerName().length() * 2)]);

				if(rs.getLegionName() == null)
				{
				writeS(buf, "");
				writeB(buf, new byte[80]);
				}
				else
				{
				writeS(buf, rs.getLegionName());// Legion Name
				writeB(buf, new byte[80 - (rs.getLegionName().length() * 2)]);
				}
				count++;
			}
		}
		if (section < 64)
		{
			section *= 2 ;
			if(section == 64)
				action = 127;
			else
				action = 0;
				
			data = null;	
			PacketSendUtility.sendPacket(player, new SM_ABYSS_RANKING_PLAYERS(dataTemp, race, action, section, player));
		}
		data = null;
		
	}
}
