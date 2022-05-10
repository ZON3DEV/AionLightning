/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DB;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.commons.database.IUStH;
import org.openaion.commons.database.ParamReadStH;
import org.openaion.gameserver.ShutdownHook;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;


/**
 * 
 * @author Mrakobes
 * 
 */

/**
 * Class for Bookmark, whitch represents: 
 * name - name of bookmark 
 * x,y,z - coordinates 
 * world_id - world_id for teleport
 * 
 */
class Bookmark
{
	private String	name;
	private float	x;
	private float	y;
	private float	z;
	private int		world_id;

	public Bookmark(float x, float y, float z, int world_id, String name)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.world_id = world_id;
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the x
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY()
	{
		return y;
	}

	/**
	 * @return the z
	 */
	public float getZ()
	{
		return z;
	}

	/**
	 * @return the world_id
	 */
	public int getWorld_id()
	{
		return world_id;
	}
}

public class Bk extends AdminCommand
{
	ArrayList<Bookmark>			bookmarks		= new ArrayList<Bookmark>();
	private static final Logger	log				= Logger.getLogger(ShutdownHook.class);
	private String				bookmark_name	= "";

	Player						admin;

	public Bk()
	{
		super("bk");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{

		if (admin.getAccessLevel() < AdminConfig.COMMAND_BK)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //bk <add | del | tele | list>");
			return;
		}

		// set local class admin
		this.admin = admin;

		if (params[0].equals("add"))
		{
			try
			{
				bookmark_name = params[1].toLowerCase();
				if (isBookmarkExists(bookmark_name))
				{
					PacketSendUtility.sendMessage(admin, "Bookmark " + bookmark_name + " already exists !");
					return;
				}

				final float x = admin.getX();
				final float y = admin.getY();
				final float z = admin.getZ();
				final int char_id = admin.getObjectId();
				final int world_id = admin.getWorldId();

				DB.insertUpdate("INSERT INTO bookmark (" + "`name`,`char_id`, `x`, `y`, `z`,`world_id` )" + " VALUES "
					+ "(?, ?, ?, ?, ?, ?)", new IUStH()
				{
					@Override
					public void handleInsertUpdate(PreparedStatement ps) throws SQLException
					{
						ps.setString(1, bookmark_name);
						ps.setInt(2, char_id);
						ps.setFloat(3, x);
						ps.setFloat(4, y);
						ps.setFloat(5, z);
						ps.setInt(6, world_id);
						ps.execute();
					}
				});

				PacketSendUtility.sendMessage(admin, "Bookmark " + bookmark_name
					+ " sucessfully added to your bookmark list!");

				updateInfo();
			}
			catch (Exception e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //bk <add | del | tele> <bookmark name>");
				return;
			}
		}
		else if (params[0].equals("del"))
		{
			Connection con = null;
			try
			{
				bookmark_name = params[1].toLowerCase();
				con = DatabaseFactory.getConnection();

				PreparedStatement statement = con.prepareStatement("DELETE FROM bookmark WHERE name = ?");
				statement.setString(1, bookmark_name);
				statement.executeUpdate();
				statement.close();
			}
			catch (Exception e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //bk <add|del|tele> <bookmark name>");
				return;
			}
			finally
			{
				DatabaseFactory.close(con);
				PacketSendUtility.sendMessage(admin, "Bookmark " + bookmark_name
					+ " sucessfully removed from your bookmark list!");
				updateInfo();
			}
		}
		else if (params[0].equals("tele"))
		{
			try
			{

				if (params[1].equals("") || params[1] == null)
				{
					PacketSendUtility.sendMessage(admin, "syntax //bk <add|del|tele> <bookmark name>");
					return;
				}

				updateInfo();

				bookmark_name = params[1].toLowerCase();
				Bookmark tele_bk = null;
				try
				{
					tele_bk = selectByName(bookmark_name);
				}
				finally
				{
					if (tele_bk != null)
					{
						TeleportService.teleportTo(admin, tele_bk.getWorld_id(), tele_bk.getX(), tele_bk.getY(),
							tele_bk.getZ(), 0);
						PacketSendUtility.sendMessage(admin, "Teleported to bookmark " + tele_bk.getName()
							+ " location");
					}
				}

			}
			catch (Exception e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //bk <add|del|tele> <bookmark name>");
				return;
			}

		}
		else if (params[0].equals("list"))
		{
			updateInfo();
			PacketSendUtility.sendMessage(admin, "=====Bookmark list begin=====");
			for (Bookmark b : bookmarks)
			{
				PacketSendUtility.sendMessage(admin, " = " + b.getName() + " =  -  ( " + b.getX() + " ," + b.getY()
					+ " ," + b.getZ() + " )");
			}
			PacketSendUtility.sendMessage(admin, "=====Bookmark list end=======");
		}
	}

	/**
	 * Reload bookmark list from db
	 */
	public void updateInfo()
	{
		bookmarks.clear();

		DB.select("SELECT * FROM `bookmark` where char_id= ?", new ParamReadStH()
		{
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, admin.getObjectId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException
			{
				while (rset.next())
				{
					String name = rset.getString("name");
					float x = rset.getFloat("x");
					float y = rset.getFloat("y");
					float z = rset.getFloat("z");
					int world_id = rset.getInt("world_id");
					bookmarks.add(new Bookmark(x, y, z, world_id, name));
				}
			}
		});
	}

	/**
	 * @param bk_name - bookmark name
	 * @return Bookmark from bookmark name
	 */
	public Bookmark selectByName(String bk_name)
	{
		for (Bookmark b : bookmarks)
		{
			if (b.getName().equals(bk_name))
			{
				return b;
			}
		}
		return null;
	}

	/**
	 * @param bk_name - bookmark name
	 * @return true if bookmark exists
	 */
	public boolean isBookmarkExists(final String bk_name)
	{
		Connection con = null;
		int bkcount = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT count(id) as bkcount FROM bookmark WHERE ? = name AND char_id = ?");
			statement.setString(1, bk_name);
			statement.setInt(2, admin.getObjectId());
			ResultSet rset = statement.executeQuery();
			while (rset.next())
				bkcount = rset.getInt("bkcount");
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			log.error("Error in reading db", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return bkcount > 0;
	}
}
