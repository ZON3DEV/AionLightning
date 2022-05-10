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
import java.sql.Timestamp;
import java.util.Map;

import org.openaion.gameserver.model.legion.Legion;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * 
 * @author Simple
 * 
 */
public class SM_LEGION_INFO extends AionServerPacket
{
	/** Legion information **/
	private Legion	legion;

	/**
	 * This constructor will handle legion info
	 * 
	 * @param legion
	 */
	public SM_LEGION_INFO(Legion legion)
	{
		this.legion = legion;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeS(buf, legion.getLegionName());
		writeC(buf, legion.getLegionLevel());
		writeD(buf, legion.getLegionRank());
	    writeC(buf, legion.getDeputyPermission1());
	    writeC(buf, legion.getDeputyPermission2());
		writeC(buf, legion.getCenturionPermission1());
		writeC(buf, legion.getCenturionPermission2());
		writeC(buf, legion.getLegionaryPermission1());
		writeC(buf, legion.getLegionaryPermission2());
	    writeC(buf, legion.getVolunteerPermission1());
	    writeC(buf, legion.getVolunteerPermission2());
		writeD(buf, legion.getContributionPoints());
		writeD(buf, 0x00); // unk
		writeD(buf, 0x00); // unk
		writeD(buf, 0x00); // unk

		/** Get Announcements List From DB By Legion **/
		Map<Timestamp, String> announcementList = legion.getAnnouncementList().descendingMap();

		/** Show max 7 announcements **/
		int i = 0;
		for(Timestamp unixTime : announcementList.keySet())
		{
			writeS(buf, announcementList.get(unixTime));
			writeD(buf, (int) (unixTime.getTime() / 1000));
			i++;
			if(i >= 7)
				break;
		}

		if(announcementList.size() > 0)
		    writeH(buf, 0);// unk 2.5		
		
		if(legion.getLegionEmblem().getCustomEmblemData() == null)
			writeH(buf, 105);
		else
		  	writeH(buf, 108);
	}
}
