package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.PlayerPasskeyDAO;


/**
 * @author ginho1
 */
public class MySQL5PlayerPasskeyDAO extends PlayerPasskeyDAO
{
	private static final Logger log			= Logger.getLogger(MySQL5PlayerPasskeyDAO.class);

	public static final String  INSERT_QUERY	= "INSERT INTO `player_passkey` (`account_id`, `passkey`) VALUES (?,?)";
	public static final String  UPDATE_QUERY	= "UPDATE `player_passkey` SET `passkey`=? WHERE `account_id`=? AND `passkey`=?";
	public static final String  UPDATE_FORCE_QUERY	= "UPDATE `player_passkey` SET `passkey`=? WHERE `account_id`=?";
	public static final String  CHECK_QUERY		= "SELECT COUNT(*) cnt FROM `player_passkey` WHERE `account_id`=? AND `passkey`=?";
	public static final String  EXIST_CHECK_QUERY	= "SELECT COUNT(*) cnt FROM `player_passkey` WHERE `account_id`=?";

	/*
	 * (non-Javadoc)
	 * @see org.openaion.gameserver.dao.PlayerPasskeyDAO#insertPlayerPasskey(int, java.lang.String)
	 */
	@Override
	public void insertPlayerPasskey(int accountId, String passkey)
	{
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);

			stmt.setInt(1, accountId);
			stmt.setString(2, passkey);

			stmt.execute();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error saving PlayerPasskey. accountId: " + accountId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/*
	* (non-Javadoc)
	* @see org.openaion.gameserver.dao.PlayerPasskeyDAO#updatePlayerPasskey(int, java.lang.String, java.lang.String)
	*/
	@Override
	public boolean updatePlayerPasskey(int accountId, String oldPasskey, String newPasskey)
	{
		boolean result = false;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);

			stmt.setString(1, newPasskey);
			stmt.setInt(2, accountId);
			stmt.setString(3, oldPasskey);

			if (stmt.executeUpdate() > 0)
				result = true;
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error updating PlayerPasskey. accountId: " + accountId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

	    return result;
	}

	/*
	* (non-Javadoc)
	* @see org.openaion.gameserver.dao.PlayerPasskeyDAO#updateForcePlayerPasskey(int, java.lang.String)
	*/
	@Override
	public boolean updateForcePlayerPasskey(int accountId, String newPasskey)
	{
		boolean result = false;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_FORCE_QUERY);

			stmt.setString(1, newPasskey);
			stmt.setInt(2, accountId);

			if (stmt.executeUpdate() > 0)
				result = true;
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error updaing PlayerPasskey. accountId: " + accountId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

	    return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openaion.gameserver.dao.PlayerPasskeyDAO#checkPlayerPasskey(int, java.lang.String)
	 */
	@Override
	public boolean checkPlayerPasskey(int accountId, String passkey)
	{
		boolean passkeyChecked = false;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(CHECK_QUERY);

			stmt.setInt(1, accountId);
			stmt.setString(2, passkey);

			ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				if (rset.getInt("cnt") == 1)
				passkeyChecked = true;
			}

			rset.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error loading PlayerPasskey. accountId: " + accountId, e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return passkeyChecked;
	}

	/*
	* (non-Javadoc)
	* @see org.openaion.gameserver.dao.PlayerPasskeyDAO#existCheckPlayerPasskey(int)
	*/
	@Override
	public boolean existCheckPlayerPasskey(int accountId)
	{
		boolean existPasskeyChecked = false;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(EXIST_CHECK_QUERY);

			stmt.setInt(1, accountId);

			ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				if (rset.getInt("cnt") == 1)
					existPasskeyChecked = true;
			}

			rset.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error loading PlayerPasskey. accountId: " + accountId, e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}

	    return existPasskeyChecked;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openaion.commons.database.dao.DAO#supports(java.lang.String, int, int)
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}