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
package org.openaion.gameserver.network.loginserver.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.loginserver.LoginServerConnection;
import org.openaion.gameserver.network.loginserver.LsServerPacket;


/**
 * @author blakawk
 *
 */
public class SM_LS_CHARACTER_COUNT extends LsServerPacket
{
	private int accountId;
	private int characterCount;
	
	public SM_LS_CHARACTER_COUNT(int accountId, int characterCount)
	{
		super(0x07);
		
		this.accountId = accountId;
		this.characterCount = characterCount;
	}

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.network.loginserver.LsServerPacket#writeImpl(org.openaion.gameserver.network.loginserver.LoginServerConnection, java.nio.ByteBuffer)
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
		writeC(buf, characterCount);
	}

}
