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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.Map;

public class SM_MACRO_LIST extends AionServerPacket {

	private Player player;
	private boolean secondPart;

	/**
	 * Constructs new <tt>SM_MACRO_LIST </tt> packet
	 *
	 * @param secondPart
	 */
	public SM_MACRO_LIST(Player player, boolean secondPart) {
		this.player = player;
		this.secondPart = secondPart;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(player.getObjectId());// player id

		Map<Integer, String> macrosToSend = player.getMacroList().getMarcosPart(secondPart);
		int size = macrosToSend.size();

		if (secondPart) {
			writeC(0);
		} else {
			writeC(0);
		}

		size *= -1;
		writeH(size);

		if (size != 0) {
			for (Map.Entry<Integer, String> entry : macrosToSend.entrySet()) {
				writeC(entry.getKey());// order
				writeS(entry.getValue());// xml
			}
		}
	}
}
