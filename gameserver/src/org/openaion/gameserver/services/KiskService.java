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

import org.openaion.gameserver.model.gameobjects.Kisk;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_LEVEL_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.container.KiskContainer;

/**
 * @author Sarynth
 *
 */
public class KiskService
{
	private static final KiskContainer		kiskContainer = new KiskContainer();
	
	/**
	 * 
	 * @param player
	 * @return kisk
	 */
	public Kisk getKisk(Player player)
	{
		return kiskContainer.get(player);
	}
	
	/**
	 * Remove kisk references and containers.
	 * @param kisk
	 */
	public static void removeKisk(Kisk kisk)
	{
		for (Player member : kisk.getCurrentMemberList())
		{
			kiskContainer.remove(member);
			
			if (member.getKisk() == kisk)
				member.setKisk(null);
			
			if (member.getKisk() == null)
				TeleportService.sendSetBindPoint(member);

			if (member.getLifeStats().isAlreadyDead())
				PacketSendUtility.sendPacket(member, new SM_DIE(false, false, 0));
			// TODO: Figure out what happens when kisk expires, but player has self-rez stone or Rebirth effect.
		}
	}
	
	/**
	 * 
	 * @param kisk
	 * @param player
	 */
	public static void onBind(Kisk kisk, Player player)
	{
		if (player.getKisk() != null)
		{
			kiskContainer.remove(player);
			player.getKisk().removePlayer(player);
		}
		
		kiskContainer.add(kisk, player);
		kisk.addPlayer(player);
		
		// Send Bind Point Data
		TeleportService.sendSetBindPoint(player);
		
		// Send System Message
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_BINDSTONE_REGISTER);
		
		// Send Animated Bind Flash
		PacketSendUtility.broadcastPacket(player, new SM_LEVEL_UPDATE(player.getObjectId(),
			2, player.getCommonData().getLevel()), true);
	}
	
	/**
	 * @param player
	 */
	public static void onLogin(Player player)
	{
		Kisk kisk = kiskContainer.get(player);
		if (kisk != null)
			kisk.reAddPlayer(player);
	}

}
