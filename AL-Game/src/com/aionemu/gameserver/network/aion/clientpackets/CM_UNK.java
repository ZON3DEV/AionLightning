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

package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author Rolandas
 */
public class CM_UNK extends AionClientPacket {

	int size;
	int unk0;
	byte unk1;
	byte unk2;
	byte unk3;
	byte unk4;

	int someId;
	int sequence;
	byte[] data;

	public CM_UNK(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		size = readD();
		unk1 = (byte) readC();
		unk2 = (byte) readC();
		unk3 = (byte) readC();
		unk4 = (byte) readC();
		size = readD();
		unk0 = readD();
		unk0 = readD();
		someId = readD();
		unk0 = readD();
		sequence = readD();
		unk0 = readD();
		data = readB(size - 36);
	}

	@Override
	protected void runImpl() {

	}
}
