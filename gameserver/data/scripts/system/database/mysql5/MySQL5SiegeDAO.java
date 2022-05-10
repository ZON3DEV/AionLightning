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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.SiegeDAO;
import org.openaion.gameserver.model.siege.SiegeLocation;
import org.openaion.gameserver.model.siege.SiegeRace;


/**
 * @author Sarynth
 */
public class MySQL5SiegeDAO extends SiegeDAO
{
	public static final String SELECT_QUERY = "SELECT `id`, `race`, `legion_id` FROM `siege_locations`";
	public static final String INSERT_QUERY = "INSERT INTO `siege_locations` (`id`, `race`, `legion_id`) VALUES(?, ?, ?)";
	public static final String UPDATE_QUERY = "UPDATE `siege_locations` SET  `race` = ?, `legion_id` = ? WHERE `id` = ?";

	/** Logger */
	private static final Logger					log					= Logger.getLogger(MySQL5PlayerDAO.class);

	@Override
	public boolean loadSiegeLocations(final Map<Integer, SiegeLocation> locations)
	{
		boolean success = true;
		Connection con = null;
		List<Integer> loaded = new ArrayList<Integer>();
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			ResultSet resultSet = stmt.executeQuery();
			while(resultSet.next())
			{
				SiegeLocation loc = locations.get(resultSet.getInt("id"));
				loc.setRace(SiegeRace.valueOf(resultSet.getString("race")));
				loc.setLegionId(resultSet.getInt("legion_id"));
				loaded.add(loc.getLocationId());
			}
			resultSet.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.warn("Error loading Siege informaiton from database: " + e.getMessage(), e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		// Insert locations that are not entered to DB yet.
		for(SiegeLocation sLoc : locations.values())
		{
			if (!loaded.contains(Integer.valueOf(sLoc.getLocationId())))
			{
				insertSiegeLocation(sLoc);
			}
		}
		
		return success;
	}

	/**
	 * @param siegeLocation
	 * @return success
	 */
	public boolean updateSiegeLocation(final SiegeLocation siegeLocation)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, siegeLocation.getRace().toString());
			stmt.setInt(2, siegeLocation.getLegionId());
			stmt.setInt(3, siegeLocation.getLocationId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error update Siege Location: " + siegeLocation.getLocationId() + " to race: " + siegeLocation.getRace().toString(), e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}

	/**
	 * @param siegeLocation
	 * @return success
	 */
	private boolean insertSiegeLocation(final SiegeLocation siegeLocation)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, siegeLocation.getLocationId());
			stmt.setString(2, siegeLocation.getRace().toString());
			stmt.setInt(3, siegeLocation.getLegionId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error insert Siege Location: " + siegeLocation.getLocationId(), e);
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
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
	
	@Override
	public void insertSiegeLogEntry(String legionName, String action, long tstamp, int locationId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO siege_log(legion_name, action, tstamp, siegeloc_id) VALUES (?,?,?,?)");
			stmt.setString(1, legionName);
			stmt.setString(2, action);
			stmt.setLong(3, tstamp);
			stmt.setInt(4, locationId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error storing Abyss Log entry: ",e);
		}
		finally
		{
			DatabaseFactory.close(con);
	
		}
	}


}
