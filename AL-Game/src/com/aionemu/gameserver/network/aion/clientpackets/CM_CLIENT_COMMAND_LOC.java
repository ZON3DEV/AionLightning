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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Antraxx
 */
public class CM_CLIENT_COMMAND_LOC extends AionClientPacket {
    private static final Logger log = LoggerFactory.getLogger(CM_CLIENT_COMMAND_LOC.class);

	public CM_CLIENT_COMMAND_LOC(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}

        log.info("Received \"/loc\" command");
		player.getPosition();
		sendPacket(SM_SYSTEM_MESSAGE.STR_CMD_LOCATION_DESC(player.getPosition().getMapId(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
	}
}
