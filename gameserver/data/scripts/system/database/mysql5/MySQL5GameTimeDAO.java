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
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.GameTimeDAO;


/**
 * @author Ben
 * 
 */
public class MySQL5GameTimeDAO extends GameTimeDAO
{
	private static Logger log = Logger.getLogger(MySQL5GameTimeDAO.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int load()
	{
		Connection con = null;
		int result = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT `value` FROM `server_variables` WHERE `key`='time'");
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
				result = Integer.parseInt(resultSet.getString("value"));
			else
				result = 0;
		 				
			resultSet.close();
			stmt.close();
		}
		catch(SQLException e)
		{
			log.error(e);
			result = 0;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean store(int time)
	{
		boolean success = false;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("REPLACE INTO `server_variables` (`key`,`value`) VALUES (?,?)");
			ps.setString(1, "time");
			ps.setString(2, String.valueOf(time));
			success = ps.executeUpdate() > 0;
		}
		catch(SQLException e)
		{
			log.error("Error storing server time", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
