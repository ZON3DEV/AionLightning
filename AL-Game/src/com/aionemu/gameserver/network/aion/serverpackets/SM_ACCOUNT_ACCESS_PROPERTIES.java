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

package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author pixfid
 * @modified Magenik
 */
public class SM_ACCOUNT_ACCESS_PROPERTIES extends AionServerPacket {

	public SM_ACCOUNT_ACCESS_PROPERTIES() {
	}

	@Override
	protected void writeImpl(AionConnection con) {
    	writeC(con.getAccount().getAccessLevel() >= AdminConfig.GM_LEVEL ? 1 : 0); // active GM Tool
		writeH(0x00);
		writeH(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeC(0x00);
		writeD(0x00);
		writeD(0x00);
	}
}
