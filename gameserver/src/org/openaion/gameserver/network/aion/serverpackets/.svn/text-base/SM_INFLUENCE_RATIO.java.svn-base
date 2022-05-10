/*
 * This file is part of aion-emu <aion-emu.com>.
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
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.model.siege.Influence;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.SiegeService;


/**
 * @author Nemiroff
 * Total Influence Ratio
 */
public class SM_INFLUENCE_RATIO extends AionServerPacket
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		Influence inf = Influence.getInstance();
		
		writeD(buf, SiegeService.getInstance().getSiegeTime());
		writeF(buf, inf.getElyos());
		writeF(buf, inf.getAsmos());
		writeF(buf, inf.getBalaur());

		writeH(buf, 3);
		for(int i=0; i<3; i++)
		{
			switch(i)
			{
				case 0:
					writeD(buf, 210050000);
					writeF(buf, inf.getElyosInggison());
					writeF(buf, inf.getAsmosInggison());
					writeF(buf, inf.getBalaurInggison());
					break;
				case 1:
					writeD(buf, 220070000);
					writeF(buf, inf.getElyosGelkmaros());
					writeF(buf, inf.getAsmosGelkmaros());
					writeF(buf, inf.getBalaurGelkmaros());
					break;
				case 2:
					writeD(buf, 400010000);
					writeF(buf, inf.getElyosAbyss());
					writeF(buf, inf.getAsmosAbyss());
					writeF(buf, inf.getBalaurAbyss());
					break;
			}
		}
	}
}
