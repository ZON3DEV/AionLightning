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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author Rhys2002
 *
 */
public class SM_NAME_CHANGE extends AionServerPacket
{

	private int playerObjectId;
	private String oldName;
	private String newName;

	public SM_NAME_CHANGE(int playerObjectId, String oldName, String newName)
	{
		this.playerObjectId = playerObjectId;
		this.oldName = oldName;
		this.newName = newName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, 0); //unk
		writeD(buf, 0); //unk - 0 or 3
		writeD(buf, playerObjectId);
		writeS(buf, oldName);
		writeS(buf, newName);
	}
}
