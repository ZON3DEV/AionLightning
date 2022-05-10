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
package org.openaion.gameserver.network.loginserver.serverpackets;

import java.nio.ByteBuffer;
import java.util.List;

import org.openaion.commons.network.IPRange;
import org.openaion.gameserver.configs.network.IPConfig;
import org.openaion.gameserver.configs.network.NetworkConfig;
import org.openaion.gameserver.network.loginserver.LoginServerConnection;
import org.openaion.gameserver.network.loginserver.LsServerPacket;


/**
 * This is authentication packet that gs will send to login server for registration.
 * 
 * @author -Nemesiss-
 * 
 */
public class SM_GS_AUTH extends LsServerPacket
{
	/**
	 * Constructs new instance of <tt>SM_GS_AUTH </tt> packet.
	 * 
	 */
	public SM_GS_AUTH()
	{
		super(0x00);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeC(buf, NetworkConfig.GAMESERVER_ID);
		writeC(buf, IPConfig.getDefaultAddress().length);
		writeB(buf, IPConfig.getDefaultAddress());

		List<IPRange> ranges = IPConfig.getRanges();
		int size = ranges.size();
		writeD(buf, size);
		for(int i = 0; i < size; i++)
		{
			IPRange ipRange = ranges.get(i);
			byte[] min = ipRange.getMinAsByteArray();
			byte[] max = ipRange.getMaxAsByteArray();
			writeC(buf, min.length);
			writeB(buf, min);
			writeC(buf, max.length);
			writeB(buf, max);
			writeC(buf, ipRange.getAddress().length);
			writeB(buf, ipRange.getAddress());
		}

		writeH(buf, NetworkConfig.GAME_PORT);
		writeD(buf, NetworkConfig.MAX_ONLINE_PLAYERS);
		writeD(buf, NetworkConfig.REQUIRED_ACCESS);
		writeS(buf, NetworkConfig.LOGIN_PASSWORD);
	}
}
