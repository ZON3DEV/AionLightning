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
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.AbyssRankDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.AbyssRankingResult;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.player.AbyssRank;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.utils.stats.AbyssRankEnum;


/**
 * @author ATracer, Divinity
 *
 */
public class MySQL5AbyssRankDAO extends AbyssRankDAO
{
	public static final String SELECT_QUERY			= "SELECT daily_ap, weekly_ap, ap, rank, top_ranking, daily_kill, weekly_kill, all_kill, max_rank, last_kill, last_ap, last_update FROM abyss_rank WHERE player_id = ?";
	public static final String SELECT_PLAYER_RANKINGS	= "SELECT ar.player_id, ar.ap, ar.top_ranking FROM abyss_rank AS ar LEFT JOIN players ON players.id = ar.player_id WHERE players.race = ? AND (ar.ap >=? OR ar.top_ranking > 0) ORDER BY ar.ap DESC";
	public static final String INSERT_QUERY			= "INSERT INTO abyss_rank (player_id, daily_ap, weekly_ap, ap, rank, top_ranking, daily_kill, weekly_kill, all_kill, max_rank, last_kill, last_ap, last_update) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_QUERY			= "UPDATE abyss_rank SET  daily_ap = ?, weekly_ap = ?, ap = ?, rank = ?, top_ranking = ?, daily_kill = ?, weekly_kill = ?, all_kill = ?, max_rank = ?, last_kill = ?, last_ap = ?, last_update = ? WHERE player_id = ?";
	public static final String UPDATE_PLAYER_RANK		= "UPDATE abyss_rank SET  top_ranking = ?, rank = ?, old_ranking = ? WHERE player_id = ?";
	public static final String SELECT_PLAYERS_RANKING	= "SELECT abyss_rank.ap,abyss_rank.top_ranking,abyss_rank.old_ranking,abyss_rank.rank, players.name, players.id, players.player_class, players.exp FROM abyss_rank,players WHERE players.race = ? AND abyss_rank.player_id = players.id AND abyss_rank.ap > 0 ORDER BY abyss_rank.ap DESC LIMIT 0,300";
	public static final String SELECT_LEGIONS_RANKING	= "SELECT legions.id, legions.name, legions.rank, legions.oldrank, legions.contribution_points, legions.level as lvl FROM legions,legion_members,players WHERE players.race = ? AND legion_members.rank = 'BRIGADE_GENERAL' AND legion_members.player_id = players.id AND legion_members.legion_id = legions.id ORDER BY legions.contribution_points DESC LIMIT 0,50";
	public static final String SELECT_LEGION		= "SELECT legions.name FROM legions,legion_members WHERE legion_members.player_id = ? AND legion_members.legion_id = legions.id";
	public static final String UPDATE_LEGION_RANK		= "UPDATE legions SET rank = ?, oldrank = ? WHERE id = ?";
	public static final String SELECT_LEGION_RANKINGS	= "SELECT legions.id, legions.rank FROM legions,legion_members,players WHERE players.race = ? AND legion_members.rank = 'BRIGADE_GENERAL' AND legion_members.player_id = players.id AND legion_members.legion_id = legions.id ORDER BY legions.contribution_points DESC";
	/**
	 * Logger for this class.
	 */
	private static final Logger	log				= Logger.getLogger(MySQL5AbyssRankDAO.class);

	
	@Override
	public void loadAbyssRank(final Player player)
	{
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
					
			stmt.setInt(1, player.getObjectId());
					
			ResultSet resultSet = stmt.executeQuery();
					
			if (resultSet.next())
		 	{
				int		daily_ap		= resultSet.getInt("daily_ap");
				int		weekly_ap		= resultSet.getInt("weekly_ap");
				int		ap				= resultSet.getInt("ap");
				int		rank			= resultSet.getInt("rank");
				int		top_ranking		= resultSet.getInt("top_ranking");
				int		daily_kill		= resultSet.getInt("daily_kill");
				int		weekly_kill		= resultSet.getInt("weekly_kill");
				int		all_kill		= resultSet.getInt("all_kill");
				int		max_rank		= resultSet.getInt("max_rank");
				int		last_kill		= resultSet.getInt("last_kill");
				int		last_ap			= resultSet.getInt("last_ap");
				long	last_update		= resultSet.getLong("last_update");
						
				AbyssRank abyssRank = new AbyssRank(daily_ap, weekly_ap, ap, rank, top_ranking, daily_kill, weekly_kill, all_kill, max_rank, last_kill, last_ap, last_update);
				abyssRank.setPersistentState(PersistentState.UPDATED);
				player.setAbyssRank(abyssRank);
		 	}
			else
	 		{
				AbyssRank abyssRank = new AbyssRank(0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, System.currentTimeMillis());
				abyssRank.setPersistentState(PersistentState.NEW);
				player.setAbyssRank(abyssRank);
					 			
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
	}

	@Override
	public boolean storeAbyssRank(Player player)
	{
		AbyssRank rank = player.getAbyssRank();
		boolean result = false;
		switch(rank.getPersistentState())
		{
			case NEW:
				result =  addRank(player.getObjectId(), rank);
				break;
			case UPDATE_REQUIRED:
				result = updateRank(player.getObjectId(), rank);
				break;
		}
		rank.setPersistentState(PersistentState.UPDATED);
		return result;
	}

	/**
	 * @param objectId
	 * @param rank
	 * @return
	 */
	private boolean addRank(final int objectId, final AbyssRank rank)
	{
		Connection con = null;
				
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
					
			stmt.setInt(1, objectId);
			stmt.setInt(2, rank.getDailyAP());
			stmt.setInt(3, rank.getWeeklyAP());
			stmt.setInt(4, rank.getAp());
			stmt.setInt(5, rank.getRank().getId());
			stmt.setInt(6, rank.getTopRanking());
			stmt.setInt(7, rank.getDailyKill());
			stmt.setInt(8, rank.getWeeklyKill());
			stmt.setInt(9, rank.getAllKill());
			stmt.setInt(10, rank.getMaxRank());
			stmt.setInt(11, rank.getLastKill());
			stmt.setInt(12, rank.getLastAP());
			stmt.setLong(13, rank.getLastUpdate());
			stmt.execute();
					
			return true;
		}
		catch(SQLException e)
		{
			log.error(e);					
			return false;
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
	private boolean updateRank(final int objectId, final AbyssRank rank)
	{
		Connection con = null;
				
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
					
			stmt.setInt(1, rank.getDailyAP());
			stmt.setInt(2, rank.getWeeklyAP());
			stmt.setInt(3, rank.getAp());
			stmt.setInt(4, rank.getRank().getId());
			stmt.setInt(5, rank.getTopRanking());
			stmt.setInt(6, rank.getDailyKill());
			stmt.setInt(7, rank.getWeeklyKill());
			stmt.setInt(8, rank.getAllKill());
			stmt.setInt(9, rank.getMaxRank());
			stmt.setInt(10, rank.getLastKill());
			stmt.setInt(11, rank.getLastAP());
			stmt.setLong(12, rank.getLastUpdate());
			stmt.setInt(13, objectId);
			stmt.execute();
					
			return true;
		}
		catch(SQLException e)
		{
			log.error(e);
			return false;
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
	private boolean updatePlayerRank(final int objectId, final int topRanking, final int rank,final int oldRanking,Connection con)
	{
		try
		{
			PreparedStatement stmt = con.prepareStatement(UPDATE_PLAYER_RANK);
					
			stmt.setInt(1, topRanking);
			stmt.setInt(2, rank);
			stmt.setInt(3, oldRanking);
			stmt.setInt(4, objectId);
			stmt.execute();
			stmt.close();
					
			return true;
		}
		catch(SQLException e)
		{
			log.error(e);
			return false;
		}
		finally
		{
		}
	}
	
	@Override
	public void updatePlayerRanking()
	{
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			
			//update asmo ranking
			PreparedStatement stmt = con.prepareStatement(SELECT_PLAYER_RANKINGS);
			
			stmt.setString(1,"ASMODIANS");//race
			stmt.setInt(2,AbyssRankEnum.getLastRankWithQuota().getRequired());//required ap for being in rank
			
			ResultSet resultSet = stmt.executeQuery();
			
			//player_id, ap, top_ranking
			int newTopRanking = 0;
			while (resultSet.next())
		 	{
				int		player_id		= resultSet.getInt("player_id");
				int		ap				= resultSet.getInt("ap");
				int		top_ranking		= resultSet.getInt("top_ranking");
				
				newTopRanking++;
				
				if (newTopRanking != top_ranking)
				{
					AbyssRankEnum finalrank = AbyssRankEnum.getRankForPosition(newTopRanking,ap);
					
					if (newTopRanking > AbyssRankEnum.getLastRankWithQuota().getQuota() || ap < AbyssRankEnum.getLastRankWithQuota().getRequired())
						newTopRanking = 0;
					//updatetoprankings
					updatePlayerRank(player_id, newTopRanking, finalrank.getId(), top_ranking, con);
				}
				else//top_ranking == newTopRanking
				{
					AbyssRankEnum finalrank = AbyssRankEnum.getRankForPosition(newTopRanking,ap);
					
					updatePlayerRank(player_id, top_ranking, finalrank.getId(), top_ranking, con);
				}
		 	}
			resultSet.close();
			stmt.close();
			
			//update elyos ranking
			stmt = con.prepareStatement(SELECT_PLAYER_RANKINGS);
			
			stmt.setString(1,"ELYOS");
			stmt.setInt(2,AbyssRankEnum.getLastRankWithQuota().getRequired());//required ap for being in rank
			
			resultSet = stmt.executeQuery();
			
			//player_id, ap, top_ranking
			newTopRanking = 0;
			while (resultSet.next())
		 	{
				int		player_id		= resultSet.getInt("player_id");
				int		ap				= resultSet.getInt("ap");
				int		top_ranking		= resultSet.getInt("top_ranking");
				
				newTopRanking++;
				
				if (newTopRanking != top_ranking)
				{
					AbyssRankEnum finalrank = AbyssRankEnum.getRankForPosition(newTopRanking,ap);
					
					if (newTopRanking > AbyssRankEnum.getLastRankWithQuota().getQuota() || ap < AbyssRankEnum.getLastRankWithQuota().getRequired())
						newTopRanking = 0;
					//updatetoprankings
					updatePlayerRank(player_id, newTopRanking, finalrank.getId(), top_ranking, con);
				}
				else//top_ranking == newTopRanking
				{
					AbyssRankEnum finalrank = AbyssRankEnum.getRankForPosition(newTopRanking,ap);
					
					updatePlayerRank(player_id, top_ranking, finalrank.getId(), top_ranking, con);
				}
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
	}
	
	private boolean updateLegionRank(int legionId, int rank, int oldrank, Connection con)
	{
	
		try
		{
			PreparedStatement stmt = con.prepareStatement(UPDATE_LEGION_RANK);
					
			stmt.setInt(1, rank);
			stmt.setInt(2, oldrank);
			stmt.setInt(3, legionId);
			stmt.execute();
			stmt.close();
					
			return true;
		}
		catch(SQLException e)
		{
			log.error(e);
			return false;
		}
		finally
		{
		}
	}
	
	@Override
	public void updateLegionRanking()
	{
		Connection con = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGION_RANKINGS);
			/* UPDATE ASMO RANKING*/		
			stmt.setString(1, "ASMODIANS");
			ResultSet resultSet = stmt.executeQuery();
			
			int newRank = 0;
			while(resultSet.next())
			{
				int legionId = resultSet.getInt("legions.id");
				int rank = resultSet.getInt("legions.rank");
							
				newRank++;
				updateLegionRank(legionId, newRank, rank, con);
			}
			resultSet.close();
			stmt.close();
			
			stmt = con.prepareStatement(SELECT_LEGION_RANKINGS);
			/* UPDATE ELYOS RANKING*/
			stmt.setString(1, "ELYOS");
			resultSet = stmt.executeQuery();
			
			newRank = 0;
			while(resultSet.next())
			{
				int legionId = resultSet.getInt("legions.id");
				int rank = resultSet.getInt("legions.rank");
							
				newRank++;
				updateLegionRank(legionId, newRank, rank, con);
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
	}
	
	@Override
	public ArrayList<AbyssRankingResult> getAbyssRankingPlayers(final Race race)
	{
		Connection con = null;
		ArrayList<AbyssRankingResult> results = new ArrayList<AbyssRankingResult>();
		try
		{
			con = DatabaseFactory.getConnection();
			//abyss_rank.ap,abyss_rank.top_ranking,abyss_rank.old_ranking,abyss_rank.rank, players.name, players.id, players.player_class, players.exp, legions.name WHERE race
			PreparedStatement stmt = con.prepareStatement(SELECT_PLAYERS_RANKING);
					
			stmt.setString(1, race.toString());
					
			ResultSet resultSet = stmt.executeQuery();

			int rank = 1;
					
			while (resultSet.next())
		 	{
				int	ap		= resultSet.getInt("abyss_rank.ap");
				//int	top_ranking	= resultSet.getInt("abyss_rank.top_ranking");
				int	top_ranking	= rank;
				int	old_ranking	= resultSet.getInt("abyss_rank.old_ranking");
				int	abyssRank	= resultSet.getInt("abyss_rank.rank");
				String	playerName	= resultSet.getString("players.name");
				int	playerId	= resultSet.getInt("players.id");
				String 	playerClassStr	= resultSet.getString("players.player_class");
				int 	playerLevel	= DataManager.PLAYER_EXPERIENCE_TABLE.getLevelForExp(resultSet.getLong("players.exp"));

				//find out if player has legion:
				PreparedStatement stmt1 = con.prepareStatement(SELECT_LEGION);
				stmt1.setInt(1, playerId);
				ResultSet resultSet1 = stmt1.executeQuery();
				String legionName = null;
				if (resultSet1.next())
				{
					legionName = resultSet1.getString("legions.name");
				}
				resultSet1.close();
				stmt1.close();
				
				PlayerClass playerClass = PlayerClass.getPlayerClassByString(playerClassStr);
				if(playerClass == null)
					continue;
				//AbyssRankingResult(String playerName, int playerId, int ap, int abyssRank, int topRanking, int oldRanking, PlayerClass playerClass, int playerLevel, String legionName)
				AbyssRankingResult rsl = new AbyssRankingResult(playerName, playerId, ap, abyssRank, top_ranking, old_ranking, playerClass, playerLevel, legionName);
				results.add(rsl);

				rank++;
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
		
		return results;
	}
	
	
	
	@Override
	public ArrayList<AbyssRankingResult> getAbyssRankingLegions(final Race race)
	{
		Connection con = null;
		final ArrayList<AbyssRankingResult> results = new ArrayList<AbyssRankingResult>();
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGIONS_RANKING);
					
			stmt.setString(1, race.toString());
					
			ResultSet resultSet = stmt.executeQuery();
					
			while (resultSet.next())
		 	{
				String name = resultSet.getString("legions.name");
				int cp = resultSet.getInt("legions.contribution_points");
				int legionId = resultSet.getInt("legions.id");
				int legionLevel = resultSet.getInt("lvl");
				int legionMembers = getLegionMembersCount(legionId);
				int rank = resultSet.getInt("legions.rank");
				int oldrank = resultSet.getInt("legions.oldrank");
				AbyssRankingResult rsl = new AbyssRankingResult(cp, name, legionId, legionLevel, legionMembers, rank, oldrank);
				results.add(rsl);
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
		
		return results;
	}
	
	private int getLegionMembersCount(final int legionId)
	{
		int result = 0;
		Connection con = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(player_id) as players FROM legion_members WHERE legion_id = ?");
					
			stmt.setInt(1, legionId);
					
			ResultSet resultSet = stmt.executeQuery();
					
			while (resultSet.next())
		 	{
				result += resultSet.getInt("players");
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

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
