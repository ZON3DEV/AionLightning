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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Cx3
 * @category Got sended with every SM_NPC_INFO Packet. It sends the current
 *           state of npc and emtion type
 */
public class SM_EMOTION_SWITCH extends AionServerPacket {

	private Creature npc;
	private int state = 0; // Need to figure out if it's can change
	private int emotionType = 0; // Need to figure out if it's can change

	public SM_EMOTION_SWITCH(Npc npc) {
		this.npc = npc;
		this.state = 3;
		this.emotionType = 13;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(npc.getObjectId());
		writeC(state);
		writeD(emotionType);
		writeD(0);
	}
}
