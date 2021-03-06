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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34
 */
public class SM_ABYSS_RANK_POINTS extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		writeB(new byte[12]);
		writeD(1200);
		writeB(new byte[8]);
		writeD(4220);
		writeB(new byte[8]);
		writeD(10990);
		writeB(new byte[8]);
		writeD(23500);
		writeB(new byte[8]);
		writeD(42780);
		writeB(new byte[8]);
		writeD(69700);
		writeB(new byte[8]);
		writeD(105600);
		writeB(new byte[8]);
		writeD(150800);
		writeB(new byte[16]);
		writeD(1260);
		writeB(new byte[8]);
		writeD(1368);
		writeB(new byte[8]);
		writeD(1915);
		writeB(new byte[8]);
		writeD(3064);
		writeB(new byte[8]);
		writeD(463236);
		writeB(new byte[8]);
		writeD(970795);
		writeB(new byte[8]);
		writeD(1286076);
		writeB(new byte[8]);
		writeD(1711117);
		writeB(new byte[8]);
		writeD(2424692);
	}
}
