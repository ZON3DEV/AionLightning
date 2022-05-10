/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author Simple
 * 
 */
public class SM_LEGION_UPDATE_EMBLEM extends AionServerPacket
{
	/** Legion emblem information **/
	private int	legionId;
	private int emblemVer;
	private int	color_r;
	private int	color_g;
	private int	color_b;
	private boolean isCustom;

	/**
	 * This constructor will handle legion emblem info
	 * 
	 * @param legion
	 */
	public SM_LEGION_UPDATE_EMBLEM(int legionId, int emblemVer, int color_r, int color_g, int color_b, boolean isCustom)
	{
		this.legionId = legionId;
		this.emblemVer = emblemVer;
		this.color_r = color_r;
		this.color_g = color_g;
		this.color_b = color_b;
		this.isCustom = isCustom;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, legionId);
		writeC(buf, emblemVer);
		writeC(buf, isCustom ? 0x80 : 0x00);
		writeC(buf, 0xFF);//sets transparency 0xFF is default; but 0x00 is much better looking ;o)
		writeC(buf, color_r);
		writeC(buf, color_g);
		writeC(buf, color_b);
	}
}
