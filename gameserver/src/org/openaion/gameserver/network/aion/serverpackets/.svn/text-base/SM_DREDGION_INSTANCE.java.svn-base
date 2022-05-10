/**
 * This file is part of aion-emu <aion-unique.com>.
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
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author Dns, ginho1
 * 
 */
public class SM_DREDGION_INSTANCE extends AionServerPacket
{
	private byte		dredgiontype;
	private int		players;
	private int		instanceid;
	private int		allowed;
	private int		timer = 0;
	private boolean		close = false;

	public SM_DREDGION_INSTANCE(byte dredgiontype, int players, int allowed, int timer)
	{
		this.dredgiontype = dredgiontype;
		this.players = players;
		this.allowed = allowed;
		this.timer = timer;
	}

	public SM_DREDGION_INSTANCE(int instanceid)
	{
		this.players = 5;
		this.allowed = 0;
		this.timer = 0;
		this.close = true;

		if(instanceid == 300110000)
			this.dredgiontype = 1;
		else
			this.dredgiontype = 2;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		/**
		 * Use : on retail two packets are sent. One for chantra, one for regular dredgion. If player lvl is 55, then
		 * chantra dredgion is automaticaly selected, but both packets are still sent.
		 */
		if(dredgiontype == 1)
			instanceid = 300110000;
		else
			instanceid = 300210000;

		writeD(buf, dredgiontype); // 1 if regular dredgion, 2 if chantra
		writeC(buf, players); // players ?
		writeD(buf, instanceid); // dredgion or chantra dredgion

		if(timer < 1 && dredgiontype == 1 && !close){
			writeD(buf, 401193);
			writeD(buf, 401197);
		}else if(timer < 1 && dredgiontype == 2 && !close){
			writeD(buf, 401675);
			writeD(buf, 401677);
		}
		else{
			writeD(buf, 0);
			writeD(buf, 0);
		}
		writeD(buf, allowed); // 1 if player is allowed to join
		writeH(buf, timer); // 21248 = retail timer value (when registrating on dredgion, you get a max time)
		writeC(buf, 0);// unk

	}
}
