/**
 * This file is part of aion-unique <aion-unique.org>.
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
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.AnnouncementsDAO;
import org.openaion.gameserver.model.Announcement;


/**
 * AccountTime DAO implementation for MySQL5
 * 
 * @author Divinity
 * 
 */
public class MySQL5AnnouncementsDAO extends AnnouncementsDAO
{
	private static final Logger	log				= Logger.getLogger(MySQL5AnnouncementsDAO.class);
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Announcement> getAnnouncements()
	{
		final Set<Announcement> result = new HashSet<Announcement>();
		Connection con = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM announcements ORDER BY id");
			
			ResultSet resultSet = stmt.executeQuery();
					
			while (resultSet.next())
		 	{
				result.add(new Announcement(resultSet.getInt("id"), resultSet.getString("announce"), resultSet.getString("faction"), resultSet.getString("type"), resultSet.getInt("delay")));
		 	}
			
			resultSet.close();
			stmt.close();
		}
		catch(SQLException e)
		{
			log.error(e);
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
	public void addAnnouncement(final Announcement announce)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO announcements (announce, faction, type, delay) VALUES (?, ?, ?, ?)");
			stmt.setString(1, announce.getAnnounce());
			stmt.setString(2, announce.getFaction());
			stmt.setString(3, announce.getType());
			stmt.setInt(4, announce.getDelay());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error(e);
			return;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delAnnouncement(final int idAnnounce)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM announcements WHERE id = ?");
			stmt.setInt(1, idAnnounce);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
