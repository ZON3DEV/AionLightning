/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * Sends Survey HTML data to the client.
 * This packet can be splitted over max 255 packets
 * The max length of the HTML may therefore be 255 * 65525 byte
 * 
 * @author lhw, Kaipo and ginho1
 */
public class SM_QUESTIONNAIRE extends AionServerPacket
{
	private int	messageId;
	private byte	chunk;
	private byte	count;
	private String	html;

	public SM_QUESTIONNAIRE(int messageId, byte chunk, byte count, String html)
	{
		this.messageId = messageId;
		this.chunk = chunk;
		this.count = count;
		this.html = html;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, messageId);
		writeC(buf, chunk);
		writeC(buf, count);
		writeH(buf, html.length() * 2);
		writeS(buf, html);
	}
}
