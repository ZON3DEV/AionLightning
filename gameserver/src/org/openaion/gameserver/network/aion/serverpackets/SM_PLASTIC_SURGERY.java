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

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author IlBuono
 */
public class SM_PLASTIC_SURGERY extends AionServerPacket
{
	private int		playerObjId;
    private boolean    check_ticket; // 1 have ticket, 2 no ticket, 3 spend ticket (2.5)
    private boolean    change_sex; //0 plastic surgery, 1 gender switch

	public SM_PLASTIC_SURGERY(Player player, boolean check_ticket, boolean change_sex)
	{
            this.playerObjId = player.getObjectId();
            this.check_ticket = check_ticket;
            this.change_sex = change_sex;
	}


	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
            writeD(buf, playerObjId);
            writeC(buf, check_ticket ? 1 : 2);
            writeC(buf, change_sex ? 1 : 0);
	}
}