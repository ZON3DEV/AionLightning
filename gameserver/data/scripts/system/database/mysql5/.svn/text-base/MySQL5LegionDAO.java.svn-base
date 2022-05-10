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
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.LegionDAO;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.player.StorageType;
import org.openaion.gameserver.model.legion.Legion;
import org.openaion.gameserver.model.legion.LegionEmblem;
import org.openaion.gameserver.model.legion.LegionHistory;
import org.openaion.gameserver.model.legion.LegionHistoryType;
import org.openaion.gameserver.model.legion.LegionWarehouse;


/**
 * Class that that is responsible for loading/storing {@link org.openaion.gameserver.model.legion.Legion} object from
 * MySQL 5.
 * 
 * @author Simple
 */
public class MySQL5LegionDAO extends LegionDAO
{
	/** Logger */
	private static final Logger	log								= Logger.getLogger(MySQL5LegionDAO.class);

	/** Legion Queries */
	private static final String	INSERT_LEGION_QUERY				= "INSERT INTO legions(id, `name`) VALUES (?, ?)";
	private static final String	SELECT_LEGION_QUERY1			= "SELECT * FROM legions WHERE id=?";
	private static final String	SELECT_LEGION_QUERY2			= "SELECT * FROM legions WHERE name=?";
	private static final String	DELETE_LEGION_QUERY				= "DELETE FROM legions WHERE id = ?";
	private static final String UPDATE_LEGION_QUERY             = "UPDATE legions SET name=?, level=?, contribution_points=?, legionary_permission1=?, legionary_permission2=?, centurion_permission1=?, centurion_permission2=?, deputy_permission1=?, deputy_permission2=?, volunteer_permission1=?, volunteer_permission2=?, disband_time=? WHERE id=?";

	/** Legion Ranking Queries **/
	private static final String	SELECT_LEGIONRANKING_QUERY		= "SELECT id, contribution_points FROM legions ORDER BY contribution_points DESC;";

	/** Announcement Queries **/
	private static final String	INSERT_ANNOUNCEMENT_QUERY		= "INSERT INTO legion_announcement_list(`legion_id`, `announcement`, `date`) VALUES (?, ?, ?)";
	private static final String	SELECT_ANNOUNCEMENTLIST_QUERY	= "SELECT * FROM legion_announcement_list WHERE legion_id=? ORDER BY date ASC LIMIT 0,7;";
	private static final String	DELETE_ANNOUNCEMENT_QUERY		= "DELETE FROM legion_announcement_list WHERE legion_id = ? AND date = ?";

	/** Emblem Queries **/
	private static final String	INSERT_EMBLEM_QUERY				= "INSERT INTO legion_emblems(legion_id, emblem_ver, color_r, color_g, color_b, custom, emblem_data) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String	UPDATE_EMBLEM_QUERY				= "UPDATE legion_emblems SET emblem_ver=?, color_r=?, color_g=?, color_b=?, custom=?, emblem_data=? WHERE legion_id=?";
	private static final String	SELECT_EMBLEM_QUERY				= "SELECT * FROM legion_emblems WHERE legion_id=?";
	
	/** Storage Queries **/
	private static final String	SELECT_STORAGE_QUERY			= "SELECT `itemUniqueId`, `itemId`, `itemCount`, `itemOwner`, `itemColor`, `isEquiped`, `slot`, `enchant`, `itemSkin`, `fusionedItem`, `optionalSocket`, `optionalFusionSocket`, `itemCreator`, `itemCreationTime`, `itemExistTime`, `itemTradeTime` FROM `inventory` WHERE `itemOwner`=? AND `itemLocation`=? AND `isEquiped`=?";

	/** History Queries **/
	private static final String	INSERT_HISTORY_QUERY			= "INSERT INTO legion_history(`legion_id`, `date`, `history_type`, `name`) VALUES (?, ?, ?, ?)";
	private static final String	SELECT_HISTORY_QUERY			= "SELECT * FROM `legion_history` WHERE legion_id=? ORDER BY date ASC;";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNameUsed(final String name)
	{
		boolean result = false;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT count(id) as cnt FROM legions WHERE ? = legions.name");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			result = rs.getInt("cnt") > 0;
		}
		catch(Exception e)
		{
			log.error("Can't check if name " + name + ", is used, returning possitive result", e);
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
	public void saveNewLegion(final Legion legion)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(INSERT_LEGION_QUERY);
			preparedStatement.setInt(1, legion.getLegionId());
			preparedStatement.setString(2, legion.getLegionName());
			preparedStatement.execute();
			log.debug("[DAO: MySQL5LegionDAO] saving new legion: " + legion.getLegionId() + " "
				+ legion.getLegionName());
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
	public void storeLegion(final Legion legion)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_LEGION_QUERY);
			log.debug("[DAO: MySQL5LegionDAO] storing player " + legion.getLegionId() + " "
				+ legion.getLegionName());

			stmt.setString(1, legion.getLegionName());
			stmt.setInt(2, legion.getLegionLevel());
			stmt.setInt(3, legion.getContributionPoints());
			stmt.setInt(4, legion.getLegionaryPermission1());
			stmt.setInt(5, legion.getLegionaryPermission2());
			stmt.setInt(6, legion.getCenturionPermission1());
			stmt.setInt(7, legion.getCenturionPermission2());
			stmt.setInt(8, legion.getDeputyPermission1());
			stmt.setInt(9, legion.getDeputyPermission2());
			stmt.setInt(10, legion.getVolunteerPermission1());
			stmt.setInt(11, legion.getVolunteerPermission2());
			stmt.setInt(12, legion.getDisbandTime());
			stmt.setInt(13, legion.getLegionId());
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
	public Legion loadLegion(final String legionName)
	{
		Connection con = null;
		Legion legion = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGION_QUERY2);
			stmt.setString(1, legionName);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
			{
				legion = new Legion();
				legion.setLegionName(legionName);
				legion.setLegionId(resultSet.getInt("id"));
				legion.setLegionLevel(resultSet.getInt("level"));
				legion.addContributionPoints(resultSet.getInt("contribution_points"));

                legion.setLegionPermissions(
                        resultSet.getInt("legionary_permission1"), 
                        resultSet.getInt("legionary_permission2"), 
                        resultSet.getInt("centurion_permission1"), 
                        resultSet.getInt("centurion_permission2"),
                        resultSet.getInt("deputy_permission1"), 
                        resultSet.getInt("deputy_permission2"),
                        resultSet.getInt("volunteer_permission1"), 
                        resultSet.getInt("volunteer_permission2"));

				legion.setDisbandTime(resultSet.getInt("disband_time"));
				log.debug("[MySQL5LegionDAO] Loaded " + legion.getLegionId() + " legion.");
			}
		}
		catch(Exception e)
		{
			log.error(e);
			legion = null;
		}
		finally
		{
			DatabaseFactory.close(con);
			if(legion != null && legion.getLegionId() == 0)
				legion = null;
		}
		return legion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Legion loadLegion(final int legionId)
	{
		Connection con = null;
		Legion legion = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGION_QUERY1);
			stmt.setInt(1, legionId);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
			{
				legion = new Legion();
				legion.setLegionName(resultSet.getString("name"));
				legion.setLegionId(resultSet.getInt("id"));
				legion.setLegionLevel(resultSet.getInt("level"));
				legion.addContributionPoints(resultSet.getInt("contribution_points"));

				legion.setLegionPermissions(
                        resultSet.getInt("legionary_permission1"), 
				        resultSet.getInt("legionary_permission2"), 
				        resultSet.getInt("centurion_permission1"), 
				        resultSet.getInt("centurion_permission2"),
                        resultSet.getInt("deputy_permission1"), 
                        resultSet.getInt("deputy_permission2"),
                        resultSet.getInt("volunteer_permission1"), 
                        resultSet.getInt("volunteer_permission2"));

				legion.setDisbandTime(resultSet.getInt("disband_time"));
				log.debug("[MySQL5LegionDAO] Loaded " + legion.getLegionId() + " legion.");
			}
		}
		catch(Exception e)
		{
			log.error(e);
			legion = null;
		}
		finally
		{
			DatabaseFactory.close(con);
			if(legion != null && legion.getLegionId() == 0)
				legion = null;
		}
		return legion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteLegion(int legionId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement(DELETE_LEGION_QUERY);
			statement.setInt(1, legionId);
			statement.execute();
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
			PreparedStatement statement = con.prepareStatement("SELECT id FROM legions", ResultSet.TYPE_SCROLL_INSENSITIVE,	ResultSet.CONCUR_READ_ONLY);
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
			log.error("Can't get list of id's from legions table", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return new int[0];
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
	public TreeMap<Timestamp, String> loadAnnouncementList(final int legionId)
	{
		TreeMap<Timestamp, String> announcementList = new TreeMap<Timestamp, String>();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement(SELECT_ANNOUNCEMENTLIST_QUERY);
			statement.setInt(1, legionId);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				String message = resultSet.getString("announcement");
				Timestamp date = resultSet.getTimestamp("date");

				announcementList.put(date, message);
			}
		}
		catch(Exception e)
		{
			log.error(e);
			announcementList = null;
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		log.debug("[MySQL5LegionDAO] Loaded announcementList " + legionId + " legion.");
		
		return announcementList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveNewAnnouncement(final int legionId, final Timestamp currentTime, final String message)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(INSERT_ANNOUNCEMENT_QUERY);
			log.debug("[DAO: MySQL5LegionDAO] saving new announcement.");

			preparedStatement.setInt(1, legionId);
			preparedStatement.setString(2, message);
			preparedStatement.setTimestamp(3, currentTime);
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
	public void removeAnnouncement(int legionId, Timestamp unixTime)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement(DELETE_ANNOUNCEMENT_QUERY);

			statement.setInt(1, legionId);
			statement.setTimestamp(2, unixTime);
			statement.execute();
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
	public void storeLegionEmblem(final int legionId, final LegionEmblem legionEmblem)
	{
		switch(legionEmblem.getPersistentState())
		{
			case UPDATE_REQUIRED:
				updateLegionEmblem(legionId, legionEmblem);
				break;
			case NEW:
				createLegionEmblem(legionId, legionEmblem);
				break;
		}
		legionEmblem.setPersistentState(PersistentState.UPDATED);
	}
	
	/**
	 * 
	 * @param legionId
	 * @param legionEmblem
	 * @return
	 */
	private void createLegionEmblem(final int legionId, final LegionEmblem legionEmblem)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(INSERT_EMBLEM_QUERY);
			preparedStatement.setInt(1, legionId);
			preparedStatement.setInt(2, legionEmblem.getEmblemVer());
			preparedStatement.setInt(3, legionEmblem.getColor_r());
			preparedStatement.setInt(4, legionEmblem.getColor_g());
			preparedStatement.setInt(5, legionEmblem.getColor_b());
			preparedStatement.setBoolean(6, legionEmblem.getIsCustom());
			preparedStatement.setBytes(7, legionEmblem.getCustomEmblemData());
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
	 * 
	 * @param legionId
	 * @param legionEmblem
	 */
	private void updateLegionEmblem(final int legionId, final LegionEmblem legionEmblem)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_EMBLEM_QUERY);
			stmt.setInt(1, legionEmblem.getEmblemVer());
			stmt.setInt(2, legionEmblem.getColor_r());
			stmt.setInt(3, legionEmblem.getColor_g());
			stmt.setInt(4, legionEmblem.getColor_b());
			stmt.setBoolean(5, legionEmblem.getIsCustom());
			stmt.setBytes(6, legionEmblem.getCustomEmblemData());
			stmt.setInt(7, legionId);
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
	public LegionEmblem loadLegionEmblem(final int legionId)
	{
		final LegionEmblem legionEmblem = new LegionEmblem();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_EMBLEM_QUERY);
			stmt.setInt(1, legionId);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
			{
				legionEmblem.setEmblem(resultSet.getInt("emblem_ver"), resultSet.getInt("color_r"), resultSet
					.getInt("color_g"), resultSet.getInt("color_b"),resultSet.getBoolean("custom"),resultSet.getBytes("emblem_data"));
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

		legionEmblem.setPersistentState(PersistentState.UPDATED);

		return legionEmblem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LegionWarehouse loadLegionStorage(Legion legion)
	{
		final LegionWarehouse inventory = new LegionWarehouse(legion);
		final int legionId = legion.getLegionId();
		final int storage = StorageType.LEGION_WAREHOUSE.getId();
		final int equipped = 0;
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_STORAGE_QUERY);
			stmt.setInt(1, legionId);
			stmt.setInt(2, storage);
			stmt.setInt(3, equipped);
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int itemUniqueId = rset.getInt("itemUniqueId");
				int itemId = rset.getInt("itemId");
				int itemCount = rset.getInt("itemCount");
				int itemColor = rset.getInt("itemColor");
				int itemOwner = rset.getInt("itemOwner");
				int isEquiped = rset.getInt("isEquiped");
				int slot = rset.getInt("slot");
				int enchant = rset.getInt("enchant");
				int itemSkin = rset.getInt("itemSkin");
				int fusionedItem = rset.getInt("fusionedItem");
				int optionalSocket = rset.getInt("optionalSocket");
				int optionalFusionSocket = rset.getInt("optionalFusionSocket");
				String crafterName = rset.getString("itemCreator");
				long itemCreationTime = rset.getTimestamp("itemCreationTime").getTime();
				long tempItemTime = rset.getLong("itemExistTime");
				int tempTradeTime = rset.getInt("itemTradeTime");
				Item item = new Item(itemUniqueId, itemId, itemCount, itemColor, isEquiped == 1, false, slot, storage, enchant, itemSkin, fusionedItem, optionalSocket, optionalFusionSocket, crafterName, itemOwner, itemCreationTime, tempItemTime, tempTradeTime);
				item.setPersistentState(PersistentState.UPDATED);
				inventory.onLoadHandler(item);
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
		return inventory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<Integer, Integer> loadLegionRanking()
	{
		final HashMap<Integer, Integer> legionRanking = new HashMap<Integer, Integer>();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_LEGIONRANKING_QUERY);
			ResultSet resultSet = stmt.executeQuery();
			int i = 1;
			while(resultSet.next())
			{
				if(resultSet.getInt("contribution_points") > 0)
				{
					legionRanking.put(resultSet.getInt("id"), i);
					i++;
				}
				else
					legionRanking.put(resultSet.getInt("id"), 0);
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

		return legionRanking;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadLegionHistory(final Legion legion)
	{
		final Collection<LegionHistory> history = legion.getLegionHistory();

		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_HISTORY_QUERY);
			stmt.setInt(1, legion.getLegionId());
			ResultSet resultSet = stmt.executeQuery();
			while(resultSet.next())
			{
				history.add(new LegionHistory(LegionHistoryType.valueOf(resultSet.getString("history_type")),
					resultSet.getString("name"), resultSet.getTimestamp("date")));
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveNewLegionHistory(final int legionId, final LegionHistory legionHistory)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(INSERT_HISTORY_QUERY);
			preparedStatement.setInt(1, legionId);
			preparedStatement.setTimestamp(2, legionHistory.getTime());
			preparedStatement.setString(3, legionHistory.getLegionHistoryType().toString());
			preparedStatement.setString(4, legionHistory.getName());
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
}
