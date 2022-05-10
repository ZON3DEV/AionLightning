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
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.LegionMemberDAO;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.legion.LegionMember;
import org.openaion.gameserver.model.legion.LegionMemberEx;
import org.openaion.gameserver.model.legion.LegionRank;
import org.openaion.gameserver.services.LegionService;


/**
 * Class that that is responsible for loading/storing {@link org.openaion.gameserver.model.legion.LegionMember} object
 * from MySQL 5.
 * 
 * @author Simple
 */
public class MySQL5LegionMemberDAO extends LegionMemberDAO
{
	/** Logger */
	private static final Logger	log								= Logger.getLogger(MySQL5LegionMemberDAO.class);

	/** LegionMember Queries */
	private static final String	INSERT_LEGIONMEMBER_QUERY		= "INSERT INTO legion_members(`legion_id`, `player_id`, `rank`) VALUES (?, ?, ?)";
	private static final String	UPDATE_LEGIONMEMBER_QUERY		= "UPDATE legion_members SET nickname=?, rank=?, selfintro=? WHERE player_id=?";
	private static final String	SELECT_LEGIONMEMBER_QUERY		= "SELECT * FROM legion_members WHERE player_id = ?";
	private static final String	DELETE_LEGIONMEMBER_QUERY		= "DELETE FROM legion_members WHERE player_id = ?";
	private static final String	SELECT_LEGIONMEMBERS_QUERY		= "SELECT player_id FROM legion_members WHERE legion_id = ?";

	/** LegionMemberEx Queries **/
	private static final String	SELECT_LEGIONMEMBEREX_QUERY		= "SELECT players.name, players.exp, players.player_class, players.last_online, players.world_id, legion_members.* FROM players, legion_members WHERE id = ? AND players.id=legion_members.player_id";
	private static final String	SELECT_LEGIONMEMBEREX2_QUERY	= "SELECT players.id, players.exp, players.player_class, players.last_online, players.world_id, legion_members.* FROM players, legion_members WHERE name = ? AND players.id=legion_members.player_id";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIdUsed(final int playerObjId)
	{
		Connection con = null;
		boolean result = false;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT count(id) as cnt FROM legion_members WHERE ? = legion_members.player_id");
			s.setInt(1, playerObjId);
			ResultSet rs = s.executeQuery();
			rs.next();
			result = rs.getInt("cnt") > 0;
		}
		catch(Exception e)
		{
			log.error(e);
			result = true;
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
	public void saveNewLegionMember(final LegionMember legionMember)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(INSERT_LEGIONMEMBER_QUERY);
			preparedStatement.setInt(1, legionMember.getLegion().getLegionId());
			preparedStatement.setInt(2, legionMember.getObjectId());
			preparedStatement.setString(3, legionMember.getRank().toString());
			preparedStatement.execute();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeLegionMember(final int playerId, final LegionMember legionMember)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_LEGIONMEMBER_QUERY);
			stmt.setString(1, legionMember.getNickname());
			stmt.setString(2, legionMember.getRank().toString());
			stmt.setString(3, legionMember.getSelfIntro());
			stmt.setInt(4, playerId);
			stmt.execute();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LegionMember loadLegionMember(final int playerObjId)
	{
		if(playerObjId == 0)
			return null;

		LegionMember legionMember = new LegionMember(playerObjId);
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGIONMEMBER_QUERY);
			stmt.setInt(1, playerObjId);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
			{
				int legionId = resultSet.getInt("legion_id");
				legionMember.setRank(LegionRank.valueOf(resultSet.getString("rank")));
				legionMember.setNickname(resultSet.getString("nickname"));
				legionMember.setSelfIntro(resultSet.getString("selfintro"));
	
				legionMember.setLegion(LegionService.getInstance().getLegion(legionId));
			}
			else
			{
				legionMember = null;
			}
		}
		catch(Exception e)
		{
			log.error(e);
			legionMember = null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return legionMember;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LegionMemberEx loadLegionMemberEx(final int playerObjId)
	{
		LegionMemberEx legionMemberEx = new LegionMemberEx(playerObjId);
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGIONMEMBEREX_QUERY);
			stmt.setInt(1, playerObjId);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
			{
				legionMemberEx.setName(resultSet.getString("players.name"));
				legionMemberEx.setExp(resultSet.getLong("players.exp"));
				legionMemberEx.setPlayerClass(PlayerClass.valueOf(resultSet.getString("players.player_class")));
				legionMemberEx.setLastOnline(resultSet.getTimestamp("players.last_online"));
				legionMemberEx.setWorldId(resultSet.getInt("players.world_id"));

				int legionId = resultSet.getInt("legion_members.legion_id");
				legionMemberEx.setRank(LegionRank.valueOf(resultSet.getString("legion_members.rank")));
				legionMemberEx.setNickname(resultSet.getString("legion_members.nickname"));
				legionMemberEx.setSelfIntro(resultSet.getString("legion_members.selfintro"));

				legionMemberEx.setLegion(LegionService.getInstance().getLegion(legionId));
			}
			else
			{
				legionMemberEx = null;
			}
		}
		catch(Exception e)
		{
			log.error(e);
			legionMemberEx = null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return legionMemberEx;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LegionMemberEx loadLegionMemberEx(final String playerName)
	{
		LegionMemberEx legionMember = new LegionMemberEx(playerName);
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGIONMEMBEREX2_QUERY);
			stmt.setString(1, playerName);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
			{
				legionMember.setObjectId(resultSet.getInt("id"));
				legionMember.setExp(resultSet.getLong("exp"));
				legionMember.setPlayerClass(PlayerClass.valueOf(resultSet.getString("player_class")));
				legionMember.setLastOnline(resultSet.getTimestamp("last_online"));
				legionMember.setWorldId(resultSet.getInt("world_id"));

				int legionId = resultSet.getInt("legion_id");
				legionMember.setRank(LegionRank.valueOf(resultSet.getString("rank")));
				legionMember.setNickname(resultSet.getString("nickname"));
				legionMember.setSelfIntro(resultSet.getString("selfintro"));

				legionMember.setLegion(LegionService.getInstance().getLegion(legionId));
			}
			else
			{
				legionMember = null;
			}
		}
		catch(Exception e)
		{
			log.error(e);
			legionMember = null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return legionMember;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Integer> loadLegionMembers(final int legionId)
	{
		ArrayList<Integer> legionMembers = new ArrayList<Integer>();

		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGIONMEMBERS_QUERY);
			stmt.setInt(1, legionId);
			ResultSet resultSet = stmt.executeQuery();
			while(resultSet.next())
			{
				int playerObjId = resultSet.getInt("player_id");
				legionMembers.add(playerObjId);
			}
		}
		catch(Exception e)
		{
			log.error(e);
			legionMembers = null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}	
		return legionMembers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteLegionMember(int playerObjId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_LEGIONMEMBER_QUERY);
			stmt.setInt(1, playerObjId);
			stmt.execute();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getUsedIDs()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
