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
import java.util.Map;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.ItemCooldownsDAO;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.items.ItemCooldown;


/**
 * @author ATracer
 *
 */
public class MySQL5ItemCooldownsDAO extends ItemCooldownsDAO
{
	public static final String INSERT_QUERY = "INSERT INTO `item_cooldowns` (`player_id`, `delay_id`, `use_delay`, `reuse_time`) VALUES (?,?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `item_cooldowns` WHERE `player_id`=?";
	public static final String SELECT_QUERY = "SELECT `delay_id`, `use_delay`, `reuse_time` FROM `item_cooldowns` WHERE `player_id`=?";

	private static final Logger log = Logger.getLogger(MySQL5ItemCooldownsDAO.class);
	
	@Override
	public void loadItemCooldowns(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int delayId = rset.getInt("delay_id");
				int useDelay = rset.getInt("use_delay");
				long reuseTime = rset.getLong("reuse_time");
				
				if(reuseTime > System.currentTimeMillis())
					player.addItemCoolDown(delayId, reuseTime, useDelay);
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
		player.getEffectController().broadCastEffects();
	}

	@Override
	public void storeItemCooldowns(final Player player)
	{
		deleteItemCooldowns(player);
		Map<Integer, ItemCooldown> itemCoolDowns = player.getItemCoolDowns();
		
		if(itemCoolDowns == null)
			return;
		
		for(Map.Entry<Integer, ItemCooldown> entry : itemCoolDowns.entrySet())
		{
			final int delayId = entry.getKey();
			final long reuseTime = entry.getValue().getReuseTime();
			final int useDelay = entry.getValue().getUseDelay();
			
			if(reuseTime - System.currentTimeMillis() < 30000)
				continue;
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, delayId);
				stmt.setInt(3, useDelay);
				stmt.setLong(4, reuseTime);
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
	}
	
	private void deleteItemCooldowns(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, player.getObjectId());
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
	public boolean supports(String arg0, int arg1, int arg2)
	{
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}
