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
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.NpcShoutsDAO;
import org.openaion.gameserver.model.NpcShout;
import org.openaion.gameserver.model.ShoutEventType;



/**
 * @author Sylar, modified Rolandas
 *
 */
public class MySQL5NpcShoutsDAO extends NpcShoutsDAO
{

	private static final Logger log = Logger.getLogger(MySQL5NpcShoutsDAO.class);
	
	@Override
	public FastMap<Integer, List<NpcShout>> getShouts()
	{
		final FastMap<Integer, List<NpcShout>> shouts = new FastMap<Integer, List<NpcShout>>();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT message_id, npc_id, event, param FROM npc_shouts");
			ResultSet arg0 = stmt.executeQuery();
			while(arg0.next())
			{
				int npcId = arg0.getInt("npc_id");
				int messageId = arg0.getInt("message_id");
				ShoutEventType event = ShoutEventType.valueOf(arg0.getString("event"));
				String param = arg0.getString("param");
				
				List<NpcShout> npcShouts = null;
				if (shouts.containsKey(npcId))
					npcShouts = shouts.get(npcId);
				else
					npcShouts = new ArrayList<NpcShout>();
				npcShouts.add(new NpcShout(messageId, event, param));
				shouts.put(npcId, npcShouts);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return shouts;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}

}
