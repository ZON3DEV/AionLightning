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

import org.openaion.gameserver.model.Petition;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.PetitionService;


/**
 * @author Sylar
 *
 */
public class SM_PETITION extends AionServerPacket
{
	private Petition petition;
	
    public SM_PETITION()
    {
        this.petition = null;
    }
    
    public SM_PETITION(Petition petition)
    {
    	this.petition = petition;
    }

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(petition == null)
		{
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeH(buf, 0x00);
			writeC(buf, 0x00);
		}
		else
		{
			writeC(buf, 0x01); // Action ID ?
			writeD(buf, 100); // unk (total online players ?)
			writeH(buf, PetitionService.getInstance().getWaitingPlayers(con.getActivePlayer().getObjectId())); // Users waiting for Support
			writeS(buf, Integer.toString(petition.getPetitionId())); // Ticket ID
			writeH(buf, 0x00);
			writeC(buf, 50); // Total Petitions
			writeC(buf, 49); // Remaining Petitions
			writeH(buf, PetitionService.getInstance().calculateWaitTime(petition.getPlayerObjId())); // Estimated minutes before GM reply
			writeD(buf, 0x00);
		}
	}
}
