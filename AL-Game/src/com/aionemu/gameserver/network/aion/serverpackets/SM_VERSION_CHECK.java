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

import com.aionemu.commons.network.IPRange;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.network.IPConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.network.NetworkController;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.ChatService;
import com.aionemu.gameserver.services.EventService;

/**
 * @author -Nemesiss- CC fix
 * @modified by Novo, cura
 */
public class SM_VERSION_CHECK extends AionServerPacket {

	/**
	 * Aion Client version
	 */
	private int version;
	/**
	 * Number of characters can be created
	 */
	private int characterLimitCount;
	/**
	 * Related to the character creation mode
	 */
	private final int characterFactionsMode;
	private final int characterCreateMode;

	/**
	 * @param chatService
	 */
	public SM_VERSION_CHECK(int version) {
		this.version = version;

		if (MembershipConfig.CHARACTER_ADDITIONAL_ENABLE != 10 && MembershipConfig.CHARACTER_ADDITIONAL_COUNT > GSConfig.CHARACTER_LIMIT_COUNT) {
			characterLimitCount = MembershipConfig.CHARACTER_ADDITIONAL_COUNT;
		} else {
			characterLimitCount = GSConfig.CHARACTER_LIMIT_COUNT;
		}

		characterLimitCount *= NetworkController.getInstance().getServerCount();

		if (GSConfig.CHARACTER_CREATION_MODE < 0 || GSConfig.CHARACTER_CREATION_MODE > 2) {
			characterFactionsMode = 0;
		} else {
			characterFactionsMode = GSConfig.CHARACTER_CREATION_MODE;
		}

		if (GSConfig.CHARACTER_FACTION_LIMITATION_MODE < 0 || GSConfig.CHARACTER_FACTION_LIMITATION_MODE > 3) {
			characterCreateMode = 0;
		} else {
			characterCreateMode = GSConfig.CHARACTER_FACTION_LIMITATION_MODE * 0x04;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		// aion 3.0 = 194
		// aion 3.5 = 196
		// aion 4.0 = 201
		// aion 4.5 = 203
		if (version < 204) {
			// Send wrong client version
			writeC(0x02);
			return;
		}
		writeC(0x00);
		writeC(NetworkConfig.GAMESERVER_ID);
		writeD(141031); // start year month day
		writeD(140820); // start year month day
		writeD(0x00); // spacing
		writeD(140922); // year month day
		writeD(1415179894); // start server time in mili
		writeC(0x00); // unk
		writeC(GSConfig.SERVER_COUNTRY_CODE);// country code;
		writeC(0x00); // unk

		int serverMode = (characterLimitCount * 0x10) | characterFactionsMode;

		if (GSConfig.ENABLE_RATIO_LIMITATION) {
			if (GameServer.getCountFor(Race.ELYOS) + GameServer.getCountFor(Race.ASMODIANS) > GSConfig.RATIO_HIGH_PLAYER_COUNT_DISABLING) {
				writeC(serverMode | 0x0C);
			} else if (GameServer.getRatiosFor(Race.ELYOS) > GSConfig.RATIO_MIN_VALUE) {
				writeC(serverMode | 0x04);
			} else if (GameServer.getRatiosFor(Race.ASMODIANS) > GSConfig.RATIO_MIN_VALUE) {
				writeC(serverMode | 0x08);
			} else {
				writeC(serverMode);
			}
		} else {
			writeC(serverMode | characterCreateMode);
		}

		writeD((int) (System.currentTimeMillis() / 1000));
		writeH(350); // 4.6
		writeH(1281); // 4
		writeH(2575); // 4.6
		writeH(257); // 4.6
		writeH(322); // 4.6
		writeH(0x02); // 4.6
		writeC(GSConfig.CHARACTER_REENTRY_TIME);
		writeC(EventsConfig.ENABLE_DECOR);
		writeC(EventService.getInstance().getEventType().getId());
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeD(0xFFFFF1F0);
		writeD(0x62917804);
		writeC(0x02);
		writeD(0x00);
		writeD(0x00);
		writeC(0x00);
		writeH(0x0BB8);
		writeH(0x0001);
		writeC(0x00);
		writeC(0x01);
		writeD(0x00);
		writeH(0x01); // its loop size
		// for... chat servers?
		{
			writeC(0x00);// spacer
			// if the correct ip is not sent it will not work
			byte[] addr = IPConfig.getDefaultAddress();
			for (IPRange range : IPConfig.getRanges()) {
				if (range.isInRange(con.getIP())) {
					addr = range.getAddress();
					break;
				}
			}
			writeB(addr);
			writeH(ChatService.getPort());
		}
	}
}
