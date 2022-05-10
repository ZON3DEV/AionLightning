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
package org.openaion.gameserver.network.chatserver.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.configs.network.IPConfig;
import org.openaion.gameserver.configs.network.NetworkConfig;
import org.openaion.gameserver.network.chatserver.ChatServerConnection;
import org.openaion.gameserver.network.chatserver.CsServerPacket;


/**
 * @author ATracer
 */
public class SM_CS_AUTH extends CsServerPacket
{

	public SM_CS_AUTH()
	{
		super(0x00);
	}

	@Override
	protected void writeImpl(ChatServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeC(buf, NetworkConfig.GAMESERVER_ID);
		writeC(buf, IPConfig.getDefaultAddress().length);
		writeB(buf, IPConfig.getDefaultAddress());
		writeS(buf, NetworkConfig.CHAT_PASSWORD);
	}
}
