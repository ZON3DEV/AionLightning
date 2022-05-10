/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.dao.PlayerDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.PlayerInitialData;
import org.openaion.gameserver.dataholders.PlayerInitialData.LocationData;
import org.openaion.gameserver.model.Gender;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.account.PlayerAccountData;
import org.openaion.gameserver.model.gameobjects.player.Mailbox;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.world.WorldPosition;

import com.mysql.jdbc.exceptions.MySQLDataException;

/**
 * Class that that is responsible for loading/storing {@link org.openaion.gameserver.model.gameobjects.player.Player}
 * object from MySQL 5.
 * 
 * @author SoulKeeper, Saelya
 */
public class MySQL5PlayerDAO extends PlayerDAO
{

	/** Logger */
	private static final Logger					log					= Logger.getLogger(MySQL5PlayerDAO.class);

	private FastMap<Integer, PlayerCommonData>	playerCommonData	= new FastMap<Integer, PlayerCommonData>().shared();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNameUsed(final String name)
	{
		Connection con = null;
		boolean used = true;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT count(id) as cnt FROM players WHERE ? = players.name");
			s.setString(1, name);
			ResultSet rs = s.executeQuery();
			rs.next();
			used = rs.getInt("cnt") > 0;
		}
		catch(Exception e)
		{
			log.error(e);
			used = true;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return used;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storePlayer(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(
				"UPDATE players SET name=?, exp=?, recoverexp=?, x=?, y=?, z=?, heading=?, world_id=?, gender=?, player_class=?, last_online=?, cube_size=?, advenced_stigma_slot_size=?, warehouse_size=?, note=?, bind_point=?, title_id=?, mailboxLetters=?, repletionstate=? WHERE id=?");

			log.debug("[DAO: MySQL5PlayerDAO] storing player "+player.getObjectId()+" "+player.getName());

				stmt.setString(1, player.getName());
				stmt.setLong(2, player.getCommonData().getExp());
				stmt.setLong(3, player.getCommonData().getExpRecoverable());
				stmt.setFloat(4, player.getX());
				stmt.setFloat(5, player.getY());
				stmt.setFloat(6, player.getZ());
				stmt.setInt(7, player.getHeading());
				stmt.setInt(8, player.getWorldId());
				stmt.setString(9, player.getGender().toString());
				stmt.setString(10, player.getCommonData().getPlayerClass().toString());
				stmt.setTimestamp(11, player.getCommonData().getLastOnline());
				stmt.setInt(12, player.getCubeSize());
				stmt.setInt(13, player.getCommonData().getAdvencedStigmaSlotSize());
				stmt.setInt(14, player.getWarehouseSize());
				stmt.setString(15,player.getCommonData().getNote());
				stmt.setInt(16, player.getCommonData().getBindPoint());
				stmt.setInt(17, player.getCommonData().getTitleId());
				
				Mailbox mailBox = player.getMailbox();
				int mails = mailBox != null ? mailBox.size() : player.getCommonData().getMailboxLetters();
				stmt.setInt(18, mails);
				stmt.setLong(19, player.getCommonData().getRepletionState());
				stmt.setInt(20, player.getObjectId());
				
				stmt.execute();
				stmt.close();
			}
			catch (Exception e)
			{
				log.error("Error saving player: "+player.getObjectId()+" "+player.getName(), e);
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
	public boolean saveNewPlayer(final PlayerCommonData pcd, final int accountId, final String accountName)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(
			"INSERT INTO players(id, `name`, account_id, account_name, x, y, z, heading, world_id, gender, race, player_class , cube_size, warehouse_size, online, last_online, creation_date) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?)");

			log.debug("[DAO: MySQL5PlayerDAO] saving new player: "+pcd.getPlayerObjId()+" "+pcd.getName());

			preparedStatement.setInt(1, pcd.getPlayerObjId());
			preparedStatement.setString(2, pcd.getName());
			preparedStatement.setInt(3, accountId);
			preparedStatement.setString(4, accountName);
			preparedStatement.setFloat(5, pcd.getPosition().getX());
			preparedStatement.setFloat(6, pcd.getPosition().getY());
			preparedStatement.setFloat(7, pcd.getPosition().getZ());
			preparedStatement.setInt(8, pcd.getPosition().getHeading());
			preparedStatement.setInt(9, pcd.getPosition().getMapId());
			preparedStatement.setString(10, pcd.getGender().toString());
			preparedStatement.setString(11, pcd.getRace().toString());
			preparedStatement.setString(12, pcd.getPlayerClass().toString());
			preparedStatement.setInt(13, pcd.getCubeSize());
			preparedStatement.setInt(14, pcd.getWarehouseSize());
			preparedStatement.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setTimestamp(16, new Timestamp(System.currentTimeMillis()));
			preparedStatement.execute();
			preparedStatement.close();
		}
		catch (Exception e)
		{
			log.error("Error saving new player: "+pcd.getPlayerObjId()+" "+pcd.getName(), e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		playerCommonData.put(pcd.getPlayerObjId(), pcd);
		return true;
	}

	@Override
	public PlayerCommonData loadPlayerCommonDataByName(final String name)
	{
		Player player = World.getInstance().findPlayer(name);
		if (player != null)
			return player.getCommonData();
		int playerObjId = 0;
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM players WHERE name = ?");
			stmt.setString(1, name);
			ResultSet rset = stmt.executeQuery();
			if(rset.next())
				playerObjId = rset.getInt("id");
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore playerId data for player name: " + name + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		if(playerObjId == 0)
			return null;
		else
			return loadPlayerCommonData(playerObjId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerCommonData loadPlayerCommonData(final int playerObjId)
	{

		PlayerCommonData cached = playerCommonData.get(playerObjId);
		if(cached != null)
		{
			log.debug("[DAO: MySQL5PlayerDAO] PlayerCommonData for id: "+playerObjId+" obtained from cache");
			return cached;
		}
		final PlayerCommonData cd = new PlayerCommonData(playerObjId);
		boolean success = false;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM players WHERE id = ?");
			stmt.setInt(1, playerObjId);
			ResultSet resultSet = stmt.executeQuery();
			log.debug("[DAO: MySQL5PlayerDAO] loading from db "+playerObjId);

			if (resultSet.next())
			{
				success = true;
				cd.setName(resultSet.getString("name"));
				//set player class before exp
				cd.setPlayerClass(PlayerClass.valueOf(resultSet.getString("player_class")));
				cd.setExp(resultSet.getLong("exp"));
				cd.setRecoverableExp(resultSet.getLong("recoverexp"));
				cd.setRace(Race.valueOf(resultSet.getString("race")));
				cd.setGender(Gender.valueOf(resultSet.getString("gender")));
				cd.setNote(resultSet.getString("note"));
				cd.setCubesize(resultSet.getInt("cube_size"));
				cd.setAdvencedStigmaSlotSize(resultSet.getInt("advenced_stigma_slot_size"));
				cd.setBindPoint(resultSet.getInt("bind_point"));
				cd.setTitleId(resultSet.getInt("title_id"));
				cd.setWarehouseSize(resultSet.getInt("warehouse_size"));
				cd.setOnline(resultSet.getBoolean("online"));
				cd.setMailboxLetters(resultSet.getInt("mailboxLetters"));
				cd.setRepletionState(resultSet.getLong("repletionstate"));

				float x = resultSet.getFloat("x");
				float y = resultSet.getFloat("y");
				float z = resultSet.getFloat("z");
				byte heading = resultSet.getByte("heading");
				int worldId = resultSet.getInt("world_id");
				PlayerInitialData playerInitialData = DataManager.PLAYER_INITIAL_DATA;
				if(z < -1000 && playerInitialData != null)
				{
					//unstuck unlucky characters :)
					LocationData ld = playerInitialData.getSpawnLocation(cd.getRace());
					x = ld.getX();
					y = ld.getY();
					z = ld.getZ();
					heading = ld.getHeading();
					worldId = ld.getMapId();
				}		

				WorldPosition position = World.getInstance().createPosition(worldId, x, y, z, heading);
				cd.setPosition(position);
				try
				{
					cd.setLastOnline(resultSet.getTimestamp("last_online"));
				}
				catch(Exception e)
				{
					log.error(e);
					cd.setLastOnline(new Timestamp(System.currentTimeMillis()));
				}
			}
			else
				log.info("Missing PlayerCommonData from db "+playerObjId);
			resultSet.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore PlayerCommonData data for player: " + playerObjId + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		if(success)
		{
			playerCommonData.put(playerObjId, cd);
			return cd;
		}
		else
			return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePlayer(int playerId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM players WHERE id = ?");
			stmt.setInt(1, playerId);
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
	public List<Integer> getPlayerOidsOnAccount(final int accountId)
	{
		List<Integer> result = new ArrayList<Integer>();
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT id FROM players WHERE account_id = ?");
			stmt.setInt(1, accountId);
			ResultSet resultSet = stmt.executeQuery();
			while(resultSet.next())
			{
				result.add(resultSet.getInt("id"));
			}
		}
		catch(Exception e)
		{
			log.error(e);
			result = null;
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
	public int getCharacterCountOnAccount(int accountId)
	{
		Connection con = null;
		int count = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS rowCount FROM `players` WHERE `account_id` = ? AND `deletion_date` IS NULL");
			stmt.setInt(1, accountId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				count = rs.getInt("rowCount");
			}
			rs.close();
			stmt.close();
		}
		catch (MySQLDataException mde) { }
		catch (Exception e)
		{
			log.warn("cannot load character count for account #"+accountId);
			log.warn(e.getMessage());
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationDeletionTime(final PlayerAccountData acData)
	{
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT creation_date, deletion_date FROM players WHERE id = ?");
			stmt.setInt(1, acData.getPlayerCommonData().getPlayerObjId());
			ResultSet rset = stmt.executeQuery();
			rset.next();

			acData.setDeletionDate(rset.getTimestamp("deletion_date"));
			acData.setCreationDate(rset.getTimestamp("creation_date"));
		}
		catch(Exception e)
		{
			log.error(e);
			// temp hack to allow players to login with '0' value in DB
			acData.setCreationDate(new Timestamp(System.currentTimeMillis()));
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
	public void updateDeletionTime(final int objectId, final Timestamp deletionDate)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE players set deletion_date = ? where id = ?");
			stmt.setTimestamp(1, deletionDate);
			stmt.setInt(2, objectId);
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
	public void storeCreationTime(final int objectId, final Timestamp creationDate)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement("UPDATE players set creation_date = ? where id = ?");
			preparedStatement.setTimestamp(1, creationDate);
			preparedStatement.setInt(2, objectId);
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

	@Override
	public void storeLastOnlineTime(final int objectId, final Timestamp lastOnline)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement("UPDATE players set last_online = ? where id = ?");
			preparedStatement.setTimestamp(1, lastOnline);
			preparedStatement.setInt(2, objectId);
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
	public int[] getUsedIDs()
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT id FROM players", ResultSet.TYPE_SCROLL_INSENSITIVE,	ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for(int i = 0; i < count; i++)
			{
				rs.next();
				ids[i] = rs.getInt("id");
			}
			return ids;
		}
		catch(SQLException e)
		{
			log.error("Can't get list of id's from players table", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return new int[0];
	}

	/**
	 * {@inheritDoc} - Saelya
	 */
	@Override
	public void onlinePlayer(final Player player, final boolean online)
	{
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE players SET online=? WHERE id=?");
			stmt.setBoolean(1, online);
			stmt.setInt(2, player.getObjectId());
			stmt.execute();
			log.debug("[DAO: MySQL5PlayerDAO] online status "+player.getObjectId()+" "+player.getName());
			stmt.close();
		}
		catch (MySQLDataException mde) { }
		catch (Exception e)
		{
			log.warn("cannot set online status " + player.getObjectId() + " = " + online);
			log.warn(e.getMessage());
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/**
	 * {@inheritDoc} - Nemiroff
	 */
	@Override
	public void setPlayersOffline(final boolean online)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE players SET online=?");
			stmt.setBoolean(1, online);
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
	
	@Override
	public String getPlayerNameByObjId(final int playerObjId)
	{
		Connection con = null;
		String result = "";
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT name FROM players WHERE id = ?");
			stmt.setInt(1, playerObjId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				result = rs.getString("name");
			else
				result = "";
		}
		catch(Exception e)
		{
			log.error(e);
			result = "";
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
	public int getAccountIdByName(final String name)
	{
		Connection con = null;
		int accountId = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `account_id` FROM `players` WHERE `name` = ?");
			s.setString(1, name);
			ResultSet rs = s.executeQuery();
			rs.next();
			accountId = rs.getInt("account_id");
			rs.close();
			s.close();
		}
		catch (Exception e)
		{
			return 0;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return accountId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}

	@Override
	public int getCharacterCountForRace(Race race)
	{
		Connection con = null;
		int count = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT COUNT(DISTINCT(`account_name`)) AS `count` FROM `players` WHERE `race` = ? AND `exp` >= ?");
			s.setString(1, race.name());
			s.setLong(2, DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(GSConfig.FACTIONS_RATIO_LEVEL));
			ResultSet rs = s.executeQuery();
			rs.next();
			count = rs.getInt("count");
			rs.close();
			s.close();
		}
		catch (Exception e)
		{
			return 0;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		return count;
	}

	@Override
	public int getOnlinePlayerCount()
	{
		Connection con = null;
		int count = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT COUNT(*) AS `count` FROM `players` WHERE `online` = ?");
			s.setBoolean(1, true);
			ResultSet rs = s.executeQuery();
			rs.next();
			count = rs.getInt("count");
			rs.close();
			s.close();
		}
		catch (Exception e)
		{
			return 0;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
	return count;
	}
}
