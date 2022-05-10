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
package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.alliance.PlayerAlliance;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.AllianceService;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Sarynth
 *
 */
public class CM_ALLIANCE_GROUP_CHANGE extends AionClientPacket
{
	private int allianceGroupId;
	private int playerObjectId;
	private int secondObjectId;
	
	public CM_ALLIANCE_GROUP_CHANGE(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{
		playerObjectId = readD();
		allianceGroupId = readD();
		secondObjectId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		PlayerAlliance alliance = player.getPlayerAlliance();
		
		if (alliance == null)
		{
			// Huh... packet spoofing?
			PacketSendUtility.sendMessage(player, "You are not in an alliance.");
			return;
		}
		
		if (!alliance.hasAuthority(player.getObjectId()))
		{
			// You are not the leader!
			PacketSendUtility.sendMessage(player, "You do not have the authority for that.");
			return;
		}
		
		AllianceService.getInstance().handleGroupChange(alliance, playerObjectId, allianceGroupId, secondObjectId);
	}
}
