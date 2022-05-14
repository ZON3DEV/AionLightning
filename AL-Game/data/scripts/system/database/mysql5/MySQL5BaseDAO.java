/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.BaseDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.base.BaseLocation;

/**
 * @author Himiko
 */
public class MySQL5BaseDAO extends BaseDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5BaseDAO.class);
	public static final String SELECT_QUERY = "SELECT * FROM `base`";
	public static final String INSERT_QUERY = "INSERT INTO `base` (`mapid`, `id`, `race`) VALUES(?, ?, ?)";
	public static final String UPDATE_QUERY = "UPDATE `base` SET `race` = ?, `last_time` = ? WHERE `id` = ?";

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}

	@Override
	public void updateBase(BaseLocation bl) {
		Connection conn = null;
		try {
			conn = DatabaseFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, bl.getRace().toString());
			stmt.setTimestamp(2, bl.getLastTime());
			stmt.setInt(3, bl.getId());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Can insert new base into database! base id:" + bl.getId() + ". " + e);
		} finally {
			DatabaseFactory.close(conn);
		}
	}

	@Override
	public void LoadBases(Map<Integer, BaseLocation> bases) {
		Connection con = null;
		PreparedStatement stmt = null;
		List<Integer> baseIds = new ArrayList<Integer>();
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				BaseLocation bl = bases.get(id);
				Race race = Race.valueOf(resultSet.getString("race"));
				bl.setRace(race);
				Timestamp lastTime = resultSet.getTimestamp("last_time");
				bl.setLastTime(lastTime);
				baseIds.add(id);
			}
			resultSet.close();
		} catch (Exception e) {
			log.error("error baseId from DB: " + e.getMessage(), e);
		} finally {
			DatabaseFactory.close(stmt, con);
		}
		for (Map.Entry<Integer, BaseLocation> baseLocation : bases.entrySet()) {
			BaseLocation bl = baseLocation.getValue();
			if (!baseIds.contains(bl.getId())) {
				insertBase(bl);
			}
		}
	}

	@Override
	public void insertBase(BaseLocation bl) {
		Connection conn = null;
		try {
			conn = DatabaseFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, bl.getWorldId());
			stmt.setInt(2, bl.getId());
			stmt.setString(3, bl.getRace().toString());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Can insert new base into database! base id:" + bl.getId() + ". " + e);
		} finally {
			DatabaseFactory.close(conn);
		}
	}
}
