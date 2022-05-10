/**
 * This file is part of aion-unique <aion-unique.com>.
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


import java.util.HashMap;
import java.util.Map;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_INSTANCE_COOLDOWN;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Lyahim
 * @author Arkshadow
 */
public class CM_INSTANCE_CD_REQUEST extends AionClientPacket
{
	
	public CM_INSTANCE_CD_REQUEST(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		readD();
		readC(); // channel
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		
		PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_COOLDOWN(true)); //clear everything
		Map<Integer, Integer> infos = new HashMap<Integer, Integer>();
		boolean first = true;
		
		if(activePlayer.getPlayerGroup() != null)
		{
			for(Player member: activePlayer.getPlayerGroup().getMembers())
			{
				if(!activePlayer.equals(member))
				{
					infos = InstanceService.getTimeInfo(member);
					for(int i : infos.keySet())
					{
						int time = infos.get(i);
						if(time!=0)
						{
							if(first)
							{
								first = false;
								PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_COOLDOWN(member, i, time, 1, false));
							}
							else
								PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_COOLDOWN(member, i, time, 2, false));
						}
					}
				}
			}
		}
		
		infos = InstanceService.getTimeInfo(activePlayer);
		
		for(int i : infos.keySet())
		{
			int time = infos.get(i);
			if(time!=0)
				PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_COOLDOWN(activePlayer, i, time, 2, true));
		}
	}
}
