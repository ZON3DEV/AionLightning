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

import org.openaion.gameserver.model.gameobjects.player.Friend;
import org.openaion.gameserver.model.gameobjects.player.FriendList;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * Sends a friend list to the client
 * @author Ben
 *
 */
public class SM_FRIEND_LIST extends AionServerPacket
{
	private FriendList list;
	
	public SM_FRIEND_LIST(FriendList list)
	{
		this.list = list;;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, (0 - list.getSize()));
		writeC(buf, 0); // Unk

        for (Friend friend : list)
        {
            writeS(buf, friend.getName());
            writeD(buf, friend.getLevel());
            writeD(buf, friend.getPlayerClass().getClassId());
            writeC(buf, 1); // Unk
            writeD(buf, friend.getMapId());
            writeD(buf, friend.getLastOnlineTime()); // Date friend was last online as a Unix timestamp.
            writeS(buf, friend.getNote()); // Friend note
            writeC(buf, friend.getStatus().getIntValue());
        }
	}
}
