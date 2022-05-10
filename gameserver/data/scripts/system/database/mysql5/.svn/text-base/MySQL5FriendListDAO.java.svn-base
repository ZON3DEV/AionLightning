/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.FriendListDAO;
import org.openaion.gameserver.dao.PlayerDAO;
import org.openaion.gameserver.model.gameobjects.player.Friend;
import org.openaion.gameserver.model.gameobjects.player.FriendList;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;


/**
 * @author Ben
 *
 */
public class MySQL5FriendListDAO extends FriendListDAO
{
	private static final Logger log = Logger.getLogger(MySQL5FriendListDAO.class);

	public static final String LOAD_QUERY = "SELECT * FROM `friends` WHERE `player`=?";
	
	public static final String ADD_QUERY = "INSERT IGNORE INTO `friends` (`player`,`friend`) VALUES (?, ?)";
	
	public static final String DEL_QUERY = "DELETE FROM friends WHERE player = ? AND friend = ?";

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.dao.FriendListDAO#load(org.openaion.gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public FriendList load(final Player player)
	{
		final List<Friend> friends = new ArrayList<Friend>();
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LOAD_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			PlayerDAO dao = DAOManager.getDAO(PlayerDAO.class);
			while (rset.next()) 
			{
				int objId = rset.getInt("friend");
					
				PlayerCommonData pcd = dao.loadPlayerCommonData(objId);
				if (pcd != null)
				{
					Friend friend = new Friend(pcd);
					friends.add(friend);
				}
			}
		}
		catch (Exception e)
		{
			log.fatal("Could not restore QuestStateList data for player: " + player.getObjectId() + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		return new FriendList(player,friends);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addFriends(final Player player, final Player friend)
	{	
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(ADD_QUERY);
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, friend.getObjectId());
			stmt.addBatch();
			
			stmt.setInt(1, friend.getObjectId());
			stmt.setInt(2, player.getObjectId());
			stmt.addBatch();
			stmt.executeBatch();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}
	
	@Override
	public boolean delFriends(final int playerOid, final int friendOid) 
	{	
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DEL_QUERY);
			stmt.setInt(1, playerOid);
			stmt.setInt(2, friendOid);
			stmt.execute();
			stmt.close();
			
			PreparedStatement stmt2 = con.prepareStatement(DEL_QUERY);
			stmt2.setInt(1, friendOid);
			stmt2.setInt(2, playerOid);
			stmt2.execute();
			stmt2.close();
		}
		catch (Exception e)
		{
			log.error(e);
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
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
