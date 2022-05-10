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

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.ToyPet;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_PET_MOVE;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * 
 * @author Sylar
 * 
 */
public class CM_PET_MOVE extends AionClientPacket
{
	
	private int actionId;
	
	private float x1;
	private float y1;
	private float z1;
	
	private int h;
	
	private float x2;
	private float y2;
	private float z2;
	
	public CM_PET_MOVE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		actionId = readC();
		switch(actionId)
		{
			case 0:
				x1 = readF();
				y1 = readF();
				z1 = readF();
				h = readC();
				break;
			case 12:
				x1 = readF();
				y1 = readF();
				z1 = readF();
				h = readC();
				x2 = readF();
				y2 = readF();
				z2 = readF();
				break;
			default:
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if(player == null)
			return;
		ToyPet pet = player.getToyPet();
		if (pet == null)
			return;
		
		switch(actionId)
		{
			case 0:
				pet.setX1(x1);
				pet.setY1(y1);
				pet.setZ1(z1);
				pet.setX2(x1);
				pet.setY2(y1);
				pet.setZ2(z1);
				pet.setH(h);
			case 12:
				pet.setX1(x1);
				pet.setY1(y1);
				pet.setZ1(z1);
				pet.setX2(x2);
				pet.setY2(y2);
				pet.setZ2(z2);
				pet.setH(h);
			default:
				PacketSendUtility.broadcastPacket(player, new SM_PET_MOVE(actionId, pet), true);
				break;
		}
	}
}
