/**
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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

import org.openaion.gameserver.model.siege.Influence;
import org.openaion.gameserver.model.siege.SiegeLocation;
import org.openaion.gameserver.model.siege.SiegeType;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.SiegeService;

import javolution.util.FastList;


/**
 * 
 * @author Sylar
 * 
 */
public class SM_FORTRESS_STATUS extends AionServerPacket
{
	public SM_FORTRESS_STATUS()
	{
	}
	
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		FastList<SiegeLocation> validLocations = new FastList<SiegeLocation>();
		
		for(SiegeLocation loc : SiegeService.getInstance().getSiegeLocations().values())
		{
			if(loc.getSiegeType() == SiegeType.FORTRESS)
			{
				validLocations.add(loc);
			}
		}
		
		writeC(buf, 1); //unk
		writeD(buf, SiegeService.getInstance().getSiegeTime());
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeH(buf, 3); //map count
		
		writeD(buf, 210050000);
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeD(buf, 220070000);
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeD(buf, 400010000);
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		
		writeH(buf, validLocations.size());
		
		for(SiegeLocation loc : validLocations)
		{
			writeD(buf, loc.getLocationId());
			writeC(buf, 1); //unk
		}
	}
}
