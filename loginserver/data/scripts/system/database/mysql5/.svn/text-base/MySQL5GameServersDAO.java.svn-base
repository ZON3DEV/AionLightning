package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DB;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.commons.database.ReadStH;
import org.openaion.loginserver.GameServerInfo;
import org.openaion.loginserver.dao.GameServersDAO;


/**
 * GameServers DAO implementation for MySQL5
 * 
 * @author -Nemesiss-
 * 
 */
public class MySQL5GameServersDAO extends GameServersDAO
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Byte, GameServerInfo> getAllGameServers()
	{

		final Map<Byte, GameServerInfo> result = new HashMap<Byte, GameServerInfo>();
		DB.select("SELECT * FROM gameservers", new ReadStH() {
			@Override
			public void handleRead(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					byte id = resultSet.getByte("id");
					String ipMask = resultSet.getString("mask");
					String password = resultSet.getString("password");
					GameServerInfo gsi = new GameServerInfo(id, ipMask, password);
					result.put(id, gsi);
				}
			}
		});
		return result;
	}
	
	@Override
	public void writeGameServerStatus(GameServerInfo gsi)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			String query = "UPDATE gameservers SET status = ? WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			
			stmt.setInt(1, gsi.isOnline() ? 1 : 0);
			stmt.setInt(2, gsi.getId());
			
			stmt.execute();
		}
		catch(Exception e)
		{
			Logger.getLogger(MySQL5GameServersDAO.class).error("Cannot write GS " + gsi.getId() + " status", e);
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
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
