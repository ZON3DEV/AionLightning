/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.GuildDAO;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.player.Guild;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 * @author Hellboy
 *
 */
public class MySQL5GuildDAO extends GuildDAO
{
	public static final String SELECT_QUERY = "SELECT `guild_id`, `last_quest`, `complete_time`, `current_quest` FROM `guilds` WHERE `player_id`=?";
	public static final String INSERT_QUERY = "INSERT INTO `guilds` (`player_id`, `guild_id`, `last_quest`, `complete_time`, `current_quest`) VALUES(?,?,?,?,?)";
	public static final String UPDATE_QUERY = "UPDATE guilds SET guild_id=?, last_quest=?, complete_time=?, current_quest=? WHERE player_id=?";
	
	private static final Logger log = Logger.getLogger(MySQL5GuildDAO.class);
	
	@Override
	public void loadGuild(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			if(rset.next())
			{
				int guildId = rset.getInt("guild_id");
				int lastQuest = rset.getInt("last_quest");
				Timestamp completeTime = rset.getTimestamp("complete_time");
				int currentQuest = rset.getInt("current_quest");
				Guild guild = new Guild(guildId, lastQuest, completeTime, currentQuest);
				guild.setPersistentState(PersistentState.UPDATED);
				player.setGuild(guild);
			}
			else
			{
				Guild guild = new Guild(0, 0, null, 0);
				guild.setPersistentState(PersistentState.NEW);
				player.setGuild(guild);
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
	}

	@Override
	public void storeGuild(Player player)
	{
		Guild guild = player.getGuild();
		switch(guild.getPersistentState())
		{
			case NEW:
				addGuild(player.getObjectId(), guild);
				break;
			case UPDATE_REQUIRED:
				updateGuild(player.getObjectId(), guild);
				break;
		}
		guild.setPersistentState(PersistentState.UPDATED);
	}

	/**
	 * @param objectId
	 * @param event
	 * @return
	 */
	private void addGuild(final int objectId, final Guild guild)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, objectId);
			stmt.setInt(2, guild.getGuildId());
			stmt.setInt(3, guild.getLastQuest());
			stmt.setTimestamp(4, guild.getCompleteTime());
			stmt.setInt(5, guild.getCurrentQuest());
			stmt.execute();
		}
		catch(SQLException e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/**
	 * @param objectId
	 * @param rank
	 * @return
	 */
	private void updateGuild(final int objectId, final Guild guild)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, guild.getGuildId());
			stmt.setInt(2, guild.getLastQuest());
			stmt.setTimestamp(3, guild.getCompleteTime());
			stmt.setInt(4, guild.getCurrentQuest());
			stmt.setInt(5, objectId);
			stmt.execute();
		}
		catch(SQLException e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
