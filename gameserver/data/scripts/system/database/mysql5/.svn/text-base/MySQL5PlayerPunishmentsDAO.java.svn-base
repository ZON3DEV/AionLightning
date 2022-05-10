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

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.PlayerPunishmentsDAO;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 * @author lord_rex
 * 
 */
public class MySQL5PlayerPunishmentsDAO extends PlayerPunishmentsDAO
{
	public static final String	SELECT_QUERY	= "SELECT `player_id`, `punishment_status`, `punishment_timer` FROM `player_punishments` WHERE `player_id`=?";
	public static final String	UPDATE_QUERY	= "UPDATE `player_punishments` SET `punishment_status`=?, `punishment_timer`=? WHERE `player_id`=?";
	public static final String	REPLACE_QUERY	= "REPLACE INTO `player_punishments` VALUES (?,?,?)";
	public static final String	DELETE_QUERY	= "DELETE FROM `player_punishments` WHERE `player_id`=?";

	private static final Logger log = Logger.getLogger(MySQL5PlayerPunishmentsDAO.class);
	
	@Override
	public void loadPlayerPunishments(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				player.setPrisonTimer(rs.getLong("punishment_timer"));

				if(player.isInPrison())
					player.setPrisonTimer(rs.getLong("punishment_timer"));
				else
					player.setPrisonTimer(0);
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
	public void storePlayerPunishments(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_QUERY);
			ps.setInt(1, player.isInPrison() ? 1 : 0);
			ps.setLong(2, player.getPrisonTimer());
			ps.setInt(3, player.getObjectId());
			ps.execute();
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
	public void punishPlayer(final Player player, final int mode)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(REPLACE_QUERY);
			ps.setInt(1, player.getObjectId());
			ps.setInt(2, mode);
			ps.setLong(3, player.getPrisonTimer());
			ps.execute();
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
	public void unpunishPlayer(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_QUERY);
			ps.setInt(1, player.getObjectId());
			ps.execute();
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
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
