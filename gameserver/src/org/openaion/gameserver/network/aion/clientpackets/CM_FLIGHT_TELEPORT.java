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
package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_WINDSTREAM;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;

/**
 * Packet about player flying teleport movement.
 * 
 * @author -Nemesiss-, Sweetkr
 * 
 */
public class CM_FLIGHT_TELEPORT extends AionClientPacket
{
	float x, y, z;
	int distance;

	/**
	 * Constructs new instance of <tt>CM_FLIGHT_TELEPORT </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_FLIGHT_TELEPORT(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		readD(); // mapId
		x = readF();
		y = readF();
		z = readF();
		readC(); // locationId
		distance = readD();	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();

		if(player != null && player.getEnterWindstream() > 0)
		{
			PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(player.getEnterWindstream(),1));
			player.setEnterWindstream(0);
		}
		
		if(player != null && player.isInState(CreatureState.FLIGHT_TELEPORT))
		{
			player.setFlightDistance(distance);
			World.getInstance().updatePosition(player, x, y, z, (byte)0);
		}
	}
}
