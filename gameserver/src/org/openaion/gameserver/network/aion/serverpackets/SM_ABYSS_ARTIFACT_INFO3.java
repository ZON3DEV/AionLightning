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
import java.util.Collection;

import org.openaion.gameserver.model.siege.SiegeLocation;
import org.openaion.gameserver.model.siege.SiegeType;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

import javolution.util.FastList;


/**
 * 
 * @author Sylar
 * 
 */
public class SM_ABYSS_ARTIFACT_INFO3 extends AionServerPacket
{
	
	private Collection<SiegeLocation> locations;

	public SM_ABYSS_ARTIFACT_INFO3(Collection<SiegeLocation> locations)
	{
		this.locations = locations;
	}
	
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		FastList<SiegeLocation> validLocations = new FastList<SiegeLocation>();
		
		for(SiegeLocation loc : locations)
		{
			if(loc.getSiegeType() == SiegeType.ARTIFACT || loc.getSiegeType() == SiegeType.FORTRESS)
			{
				if(loc.getLocationId() >= 1011 && loc.getLocationId() < 2000)
				{
					validLocations.add(loc);
				}
			}
		}
		
		writeH(buf, validLocations.size()); // Artifact Count
		
		for(SiegeLocation loc : validLocations)
		{
			String locIdStr = String.valueOf(loc.getLocationId());
			locIdStr += "1";			
			writeD(buf, Integer.parseInt(locIdStr));
			writeD(buf, 0);
			writeC(buf, 0);
		}
	}
}
