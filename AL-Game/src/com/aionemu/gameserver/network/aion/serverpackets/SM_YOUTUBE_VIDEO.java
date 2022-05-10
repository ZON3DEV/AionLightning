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

import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author FrozenKiller
 */
public class SM_YOUTUBE_VIDEO extends AionServerPacket {
	
	private static byte[] data;
	
	static {
		data = hex2Byte("F070A316010000000000FFFFFFFFFFFFC0651002000000000000000000000000C50100C4000000"
						+"0005000000000000006CF275E6FE070000C0AD100200000000C0AD10020000000005000000000000008067100200000000000000000000000053000053000000"
						+"00380000000000000005000000FE07000000000000000000005300005300000000000075E6000000000000000000000000000000000100000001000000000000"
						+"00120000000000000005000000000000000000000000000000000000000000000000000000000000000000000000000000D0B010020000000000000000000000"
						+"00507C100200000000F070A316010000000000920B010000000100000000000000000000000000000000000000000000000000000000000000F070A316010000"
						+"00C0AD100200000000000000000000000080441002000000000A1ACE760000000000001002000000000100000000000000000000000000000080441002000000"
						+"00C0AD1002000000008A3A76E6FE070000FFFFFFFFFFFFFFFFA0EB75E6FE07000005000000000000000000000000000000B084A31601000000691675E6FE0700"
						+"00C0AD100200000000441775E6FE0700000000000000000000010000000000000000000000000000001000000000000000C0AD100200000000C13576E6FE0700"
						+"000B2000000000000000000000000000000300000000000000C0AD100200000000D03E5A2201000000F7299EE7FE070000F0918321010000000FEA75E6FE0700"
						+"0000000000000000001000000000000000D03E5A2201000000C3FC9FE7FE07000000000000000000000300000000000000F0EA430000000000F7299EE7FE0700"
						+"0000000000000000000C6D00400100000000000000000000000C6D00400100000048E32B4001000000B09D2A400100000000000000000000009CBAA4E7FE0700"
						+"0000004F0200000000449B00400100000001000000000000000C6D0040010000008021540200000000CFD1014001000000ACC31A00000000002BA30140010000"
						+"0000004F0200000000449B004001000000FFFFFFFFFFFFFFFF69FB5E030100000000000000000000003625000000000000D03E5A2201000000E98B0E40010000"
						+"000100000000000000060000000000000069FB5E0301000000010000000000000004000000000000000000000000000000ACC31A0001000000D03E5A22010000"
						+"00060000000100000050CE670B01000000000000000000000005000000D00700000000000001000000602BFF0100000000602BFF01000000000C6D0040010000"
						+"000100000000000000709E0D5C00000000B474F15D00000000");
	}

	@Override
	protected void writeImpl(AionConnection con) {
		String videoString = EventsConfig.EVENT_YOUTUBE_VIDEO;
		writeH(1);
		writeH(-5110);
		writeH(12130);
		writeS(videoString);
		writeB(data);
	}
	
	private static byte[] hex2Byte(String str) {
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}
}
