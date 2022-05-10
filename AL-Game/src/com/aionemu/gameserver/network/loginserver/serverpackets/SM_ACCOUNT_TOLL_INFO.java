/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.network.loginserver.serverpackets;

import com.aionemu.gameserver.network.loginserver.LoginServerConnection;
import com.aionemu.gameserver.network.loginserver.LsServerPacket;

/**
 * @author xTz
 */
public class SM_ACCOUNT_TOLL_INFO extends LsServerPacket {

	private final long toll;
	private final String accountName;
	private final int type;

	public SM_ACCOUNT_TOLL_INFO(long toll, String accountName) {
		super(0x09);
		this.accountName = accountName;
		this.toll = toll;
		this.type = 0;
	}

	public SM_ACCOUNT_TOLL_INFO(long toll, int type, String accountName) {
		super(0x09);
		this.accountName = accountName;
		this.toll = toll;
		this.type = type;
	}

	@Override
	protected void writeImpl(LoginServerConnection con) {
		writeC(type); // type 0 = toll, 10 = point boutique (add only)
		writeQ(toll);
		writeS(accountName);
	}
}
