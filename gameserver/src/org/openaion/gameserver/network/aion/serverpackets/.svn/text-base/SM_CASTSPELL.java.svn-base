/*
 * This file is part of aion-unique <aionunique.smfnew.com>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author alexa026
 * @author rhys2002
 */
public class SM_CASTSPELL extends AionServerPacket
{
	private int attackerObjectId;
	private int	spellId;
	private int	level;
	private int	targetType; 
	private int duration;
	
	private int	targetObjectId;
	
	private float x;
	private float y;
	private float z;
	
	public SM_CASTSPELL(int attackerObjectId, int spellId, int level, int targetType, int targetObjectId, int duration)
	{
		this.attackerObjectId = attackerObjectId;
		this.spellId = spellId;
		this.level = level;
		this.targetType = targetType;
		this.targetObjectId = targetObjectId;
		this.duration = duration;
	}
	
	public SM_CASTSPELL(int attackerObjectId, int spellId, int level, int targetType, float x, float y, float z, int duration)
	{
		this(attackerObjectId, spellId, level, targetType, 0, duration);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, attackerObjectId);
		writeH(buf, spellId); 
		writeC(buf, level);
		
		writeC(buf, targetType);
		switch(targetType)
		{
			case 0:
				writeD(buf, targetObjectId); 
				break;
			case 1:
				writeF(buf, x);
				writeF(buf, y);
				writeF(buf, z);
				break;
			case 3:
				writeD(buf, targetObjectId);
				break;
		}
		
		writeH(buf, duration);
		writeD(buf, 0);
	}
}
