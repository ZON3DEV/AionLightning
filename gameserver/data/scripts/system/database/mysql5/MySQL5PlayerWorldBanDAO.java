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

import java.util.Date;

import com.mysql.jdbc.exceptions.MySQLDataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.PlayerWorldBanDAO;
import org.openaion.gameserver.model.gameobjects.player.Player;

/**
 * @author blakawk
 *
 */
public class MySQL5PlayerWorldBanDAO extends PlayerWorldBanDAO
{
	private static final Logger log = Logger.getLogger(MySQL5PlayerWorldBanDAO.class);
	
	@Override
	public void loadWorldBan(Player player)
	{
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_world_bans WHERE `player` = ?");
			stmt.setInt(1, player.getObjectId());
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				player.setBannedFromWorld(rs.getString("by"), rs.getString("reason"), rs.getLong("duration"), new Date(rs.getLong("date")));
			}
			rs.close();
			stmt.close();
		}
		catch (MySQLDataException mde) { }
		catch (Exception e)
		{
			log.error("cannot load world ban for player #"+player.getObjectId());
			e.printStackTrace();
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public boolean addWorldBan(int playerObjId, String by, long duration, Date date, String reason)
	{
		String query = "SELECT * FROM player_world_bans WHERE `player` = ?";
		Connection con = null;
		boolean result = false;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, playerObjId);
			ResultSet rset = stmt.executeQuery();
			if(!rset.next())
			{
				query = "INSERT INTO player_world_bans(`player`, `by`, `duration`, `date`, `reason`) VALUES (?,?,?,?,?)";
				stmt = con.prepareStatement(query);
				stmt.setInt(1, playerObjId);
				stmt.setString(2, by);
				stmt.setLong(3, duration);
				stmt.setLong(4, date.getTime());
				stmt.setString(5, reason);
				stmt.execute();
				stmt.close();
				result = true;
			}
			else
			{
				log.warn("player #"+playerObjId+" already banned");
				result = false;
			}
		}
		catch(MySQLDataException mde) { result = false; }
		catch (Exception e) 
		{
			log.error("cannot insert world ban for player #"+playerObjId);
			e.printStackTrace();
			result = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return result;
	}

	@Override
	public void removeWorldBan(int playerObjId)
	{
		String query = "DELETE FROM player_world_bans WHERE `player` = ?";
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, playerObjId);
			stmt.execute();
			stmt.close();
		}
		catch (MySQLDataException mde) { }
		catch (Exception e)
		{
			log.error("cannot delete world ban for player #"+playerObjId);
			e.printStackTrace();
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public boolean supports(String arg0, int arg1, int arg2)
	{
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}
