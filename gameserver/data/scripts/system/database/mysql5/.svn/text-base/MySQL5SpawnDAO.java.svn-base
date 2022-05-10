package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.dao.SpawnDAO;
import org.openaion.gameserver.model.templates.spawn.SpawnGroup;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;


/**
 * @author blakawk
 *
 */
public class MySQL5SpawnDAO extends SpawnDAO
{
	private static final String LIST_SPAWNS_QUERY = "SELECT * FROM `spawns` WHERE `admin_id` = ? AND `group_name` = ?";
	private static final String LIST_SPAWN_GROUP_QUERY = "SELECT * FROM `spawn_groups` WHERE `admin_id` = ?";
	private static final String COUNT_ALL_SPAWNS_QUERY = "SELECT COUNT(*) AS spawn_count FROM `spawns` WHERE `group_name` = ? AND `admin_id` = ?";
	private static final String ADD_SPAWN_QUERY = "INSERT INTO `spawns`(`admin_id`, `group_name`, `npc_id`, `respawn`, `map_id`, `x`, `y`, `z`, `h`, `object_id`, `spawned`,`staticid` ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String ADD_GROUP_QUERY = "INSERT INTO `spawn_groups`(`admin_id`, `group_name`, `spawned`) VALUES (?,?,?)";
	private static final String GROUP_SPAWNED_QUERY = "SELECT `spawned` FROM `spawn_groups` WHERE `admin_id` = ? AND `group_name` = ?";
	private static final String SET_GROUP_SPAWNED_QUERY = "UPDATE `spawn_groups` SET `spawned` = ? WHERE `admin_id` = ? and `group_name` = ?";
	private static final String IS_IN_DB_QUERY = "SELECT `spawn_id`,`x`,`y` FROM `spawns` WHERE `npc_id`= ?";
	private static final String UPDATE_HEADING = "UPDATE `spawns` SET `h` = ? WHERE `spawn_id` = ?";
	private static final String SET_SPAWNED_QUERY = "UPDATE `spawns` SET `spawned` = ?, `object_id` = ? WHERE `spawn_id` = ?";
	private static final String GROUP_EXISTS = "SELECT * FROM `spawn_groups` WHERE `admin_id` = ? AND `group_name` = ?";
	private static final String ALL_SPAWNS_QUERY = "SELECT * FROM `spawns` WHERE `spawned` = ?";
	private static final String DELETE_SPAWN_QUERY = "DELETE FROM `spawns` WHERE `spawn_id` = ?";
	private static final String DELETE_SPAWN_GROUP_QUERY = "DELETE FROM `spawn_groups` WHERE `admin_id` = ? AND `group_name` = ?";
	private static final String GET_SPAWN_OBJID_QUERY = "SELECT `object_id` FROM `spawns` WHERE `spawn_id` = ? AND `spawned` = ?";
	private static final String GET_SPAWN_QUERY = "SELECT * FROM `spawns` WHERE `spawn_id` = ?";
	
	private static final Logger log = Logger.getLogger(MySQL5SpawnDAO.class);
	
	private SpawnTemplate createSpawnTemplate (ResultSet rs)
	{
		SpawnTemplate t = null;
		
		try
		{
			t = new SpawnTemplate(rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getByte("h"), 0, 0, 0);
			t.setSpawnId(rs.getInt("spawn_id"));
			t.setNoRespawn(!rs.getBoolean("respawn"), 1);
			SpawnGroup g = new SpawnGroup(rs.getInt("map_id"),rs.getInt("npc_id"), 295, 1);
			t.setSpawnGroup(g);
			g.getObjects().add(t);
			t.setSpawned(rs.getBoolean("spawned"), 1);
		}
		catch (Exception e)
		{
			log.error("Cannot fetch spawn template: "+e.getMessage());
			e.printStackTrace();
			t = null;
		}
		
		return t;
	}
	
	@Override
	public int addSpawn(int npcId, int adminObjectId, String group, boolean respawn, int mapId, float x, float y, float z, byte h, int objectId, int staticid)
	{
		Connection con = null;
		int spawnId = 0;
		
		group = group == null ? "default" : group;
		group = "default".equalsIgnoreCase(group)?"default":group;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = null;
			
			if (group != null)
			{
				stmt = con.prepareStatement(GROUP_EXISTS);
				stmt.setInt(1, adminObjectId);
				stmt.setString(2, group);
				ResultSet rs = stmt.executeQuery();
			
				if (!rs.next())
				{
					rs.close();
					stmt.close();
					stmt = con.prepareStatement(ADD_GROUP_QUERY);
					stmt.setInt(1, adminObjectId);
					stmt.setString(2, group);
					stmt.setBoolean(3, true);
					stmt.execute();
					stmt.close();
				}
				else
				{
					rs.close();
					stmt.close();
					stmt = con.prepareStatement(SET_GROUP_SPAWNED_QUERY);
					stmt.setBoolean(1, true);
					stmt.setInt(2, adminObjectId);
					stmt.setString(3, group);
					stmt.execute();
					stmt.close();
				}
			}
			
			stmt = con.prepareStatement(ADD_SPAWN_QUERY, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, adminObjectId);
			stmt.setString(2, group);
			stmt.setInt(3, npcId);
			stmt.setBoolean(4, respawn);
			stmt.setInt(5, mapId);
			stmt.setFloat(6, x);
			stmt.setFloat(7, y);
			stmt.setFloat(8, z);
			stmt.setByte(9, h);
			stmt.setInt(10, objectId);
			stmt.setBoolean(11, true);
			stmt.setInt(12, staticid);
			stmt.execute();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next())
			{
				spawnId = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Cannot add spawn #"+npcId+" for admin #"+adminObjectId+": "+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return spawnId;
	}
	
	public boolean unSpawnGroup (int adminObjectId, String group)
	{
		Connection con = null;
		boolean result = false;
		
		group = group == null ? "default" : group;
		group = "default".equalsIgnoreCase(group)?"default":group;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LIST_SPAWNS_QUERY);
			stmt.setInt(1, adminObjectId);
			stmt.setString(2, group);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
				PreparedStatement stmt2 = con.prepareStatement(SET_SPAWNED_QUERY);
				stmt2.setBoolean(1, true);
				stmt2.setInt(2, rs.getInt("object_id"));
				stmt2.execute();
				stmt2.close();
			}
			rs.close();
			stmt.close();
			stmt = con.prepareStatement(SET_GROUP_SPAWNED_QUERY);
			stmt.setBoolean(1, true);
			stmt.setInt(2, adminObjectId);
			stmt.setString(3, group);
			stmt.execute();
			stmt.close();
			result = true;
		}
		catch (Exception e)
		{
			log.error("Cannot spawn group "+group+" for admin #"+adminObjectId+": "+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		return result;
	}

	@Override
	public boolean isSpawned(int adminObjectId, String group)
	{
		Connection con = null;
		boolean result = false;
		
		group = group == null ? "default" : group;
		group = "default".equalsIgnoreCase(group)?"default":group;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(GROUP_SPAWNED_QUERY);
			stmt.setInt(1, adminObjectId);
			stmt.setString(2, group);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
			{
				if (rs.getBoolean("spawned"))
				{
					result = true;
				}
			}
		}
		catch (Exception e)
		{
			log.error("Cannot know if group "+group+" for admin #"+adminObjectId+" is spawned: "+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean setGroupSpawned(int adminObjectId, String group, boolean isSpawned)
	{
		Connection con = null;
		boolean result = false;
		
		group = group == null ? "default" : group;
		group = "default".equalsIgnoreCase(group) ? "default" : group;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SET_GROUP_SPAWNED_QUERY);
			stmt.setBoolean(1, isSpawned);
			stmt.setInt(2, adminObjectId);
			stmt.setString(3, group);
			stmt.execute();
			stmt.close();
			con.close();
			result = true;
		}
		catch (Exception e)
		{
			log.error("Cannot set is spawned to "+isSpawned+" if group "+group+" for admin #"+adminObjectId+" is spawned: "+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return result;
	}

	@Override
	public Map<Integer, SpawnTemplate> listSpawns(int adminObjectId, String group, SpawnType type)
	{
		Connection con = null;
		Map<Integer, SpawnTemplate> spawns = null;
		
		group = group == null ? "default" : group;
		group = "default".equalsIgnoreCase(group) ? "default" : group;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LIST_SPAWNS_QUERY);
			stmt.setInt(1, adminObjectId);
			stmt.setString(2, group);
			ResultSet rs = stmt.executeQuery();
			
			if (!rs.next())
			{
				rs.close();
				stmt.close();
				con.close();
				return null;
			}
			
			spawns = new FastMap<Integer, SpawnTemplate>();
			do
			{
				SpawnTemplate t = createSpawnTemplate(rs);
				if (t != null)
				{
					switch (type)
					{
						case SPAWNED:
							if (rs.getInt("object_id") != -1)
							{
								spawns.put(rs.getInt("object_id"), t);
							}
							break;
						case DESPAWNED:
							if (!rs.getBoolean("spawned"))
							{
								spawns.put(rs.getInt("object_id"), t);
							}
							break;
						case REMOVED:
							if (rs.getInt("object_id") == -1)
							{
								spawns.put(rs.getInt("spawn_id"), t);
							}
							break;
						case ALL:
							spawns.put((rs.getInt("object_id")==-1)?rs.getInt("spawn_id"):rs.getInt("object_id"), t);
							break;
					}
				}
			}
			while(rs.next());
			
			rs.close();
			stmt.close();
			con.close();
			return spawns;
		}
		catch (Exception e)
		{
			log.error("Cannot list spawn group "+group+" for admin #"+adminObjectId+": "+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return null;
	}
	
	public Map<Integer, SpawnTemplate> getAllSpawns()
	{
		Connection con = null;
		Map<Integer, SpawnTemplate> spawns = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(ALL_SPAWNS_QUERY);
			stmt.setBoolean(1, true);
			ResultSet rs = stmt.executeQuery();
			spawns = new FastMap<Integer, SpawnTemplate>();
			while (rs.next())
			{
				SpawnTemplate t = createSpawnTemplate(rs);
				if (t != null)
					spawns.put(rs.getInt("object_id"), t);
			}
			rs.close();
			stmt.close();
			con.close();
		}
		catch (Exception e)
		{
			log.error("Cannot retrieve spawns: "+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		return spawns;
	}
	
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
	
	@Override
	public int isInDB(int npcId, float x, float y)
	 {
	  Connection con = null;
	  int result = 0;
	  
	  try
	  {
	   con = DatabaseFactory.getConnection();
	   PreparedStatement stmt = con.prepareStatement(IS_IN_DB_QUERY);
	   stmt.setInt(1, npcId);
	   ResultSet rs = stmt.executeQuery();
	   while (rs.next())
	   { 
		   if(Math.abs(rs.getFloat("x")-x)<0.1 && Math.abs(rs.getFloat("y")-y)<0.1)
			   result = rs.getInt("spawn_id");
	   }
	  }
	  catch (Exception e)
	  {
	   log.error("Cannot know if npcid "+npcId+" coords: x = "+x+", y = "+y+" is spawned: "+e.getMessage());
	   e.printStackTrace();
	  }
	  finally
	  {
	   if(con!=null)
	   {
	    try
	    {
	     con.close();
	    } catch (Exception e) {}
	   }
	  }
	  
	  return result;
	 }
	
	@Override
	public boolean updateHeading(int spawnId, int heading)
	{
		Connection con = null;
		boolean result = false;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_HEADING);
			stmt.setInt(1, heading);
			stmt.setInt(2, spawnId);
			stmt.execute();
			stmt.close();
			result = true;
		}
		catch (Exception e)
		{
			log.error("Cannot set heading to "+heading+"for spawn #"+spawnId+": "+e.getMessage(), e);
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean setSpawned(int spawnId, int objectId, boolean isSpawned)
	{
		Connection con = null;
		boolean result = false;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SET_SPAWNED_QUERY);
			stmt.setBoolean(1, isSpawned);
			stmt.setInt(2, objectId);
			stmt.setInt(3, spawnId);
			stmt.execute();
			stmt.close();
			result = true;
		}
		catch (Exception e)
		{
			log.error("Cannot set spawned to "+isSpawned+"for spawn #"+spawnId+": "+e.getMessage(), e);
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Integer> listSpawnGroups(int adminObjectId)
	{
		Connection con = null;
		Map<String, Integer> groups = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LIST_SPAWN_GROUP_QUERY);
			stmt.setInt(1, adminObjectId);
			ResultSet rs = stmt.executeQuery();
			groups = new FastMap<String, Integer> ();
			while (rs.next())
			{
				PreparedStatement stmt2 = con.prepareStatement(COUNT_ALL_SPAWNS_QUERY);
				stmt2.setString(1, rs.getString("group_name"));
				stmt2.setInt(2, adminObjectId);
				ResultSet rs2 = stmt2.executeQuery();
				if (rs2.next())
				{
					groups.put(rs.getString("group_name"), rs2.getInt("spawn_count"));
				}
				rs2.close();
				stmt2.close();
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Cannot list groups for admin #"+adminObjectId+": "+e.getMessage(), e);
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return groups;
	}
	
	@Override
	public boolean deleteSpawn (int spawnId)
	{
		Connection con = null;
		boolean result = false;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_SPAWN_QUERY);
			stmt.setInt(1, spawnId);
			stmt.execute();
			stmt.close();
			result = true;
		}
		catch (Exception e)
		{
			log.warn("Cannot delete spawn #"+spawnId+": "+e.getMessage(), e);
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean deleteSpawnGroup (int adminObjectId, String groupName)
	{
		Connection con = null;
		boolean result = false;
		
		try
		{
			con = DatabaseFactory.getConnection();
			Map<Integer, SpawnTemplate> spawns = listSpawns(adminObjectId, groupName, SpawnType.ALL);
			
			result = true;
			
			if (spawns != null && spawns.size() > 0)
			{		
				for (Entry<Integer, SpawnTemplate> spawn : spawns.entrySet())
				{
					result = deleteSpawn(spawn.getValue().getSpawnId()) && result;
				}
			}
			
			PreparedStatement stmt = con.prepareStatement(DELETE_SPAWN_GROUP_QUERY);
			stmt.setInt(1, adminObjectId);
			stmt.setString(2, groupName);
			stmt.execute();
			stmt.close();
			result = true;
		}
		catch (Exception e)
		{
			log.warn("Cannot delete spawn group \""+groupName+"\" for admin #"+adminObjectId+": "+e.getMessage(), e);
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return result;
	}
	
	@Override
	public int getSpawnObjectId (int spawnId, boolean isSpawned)
	{
		Connection con = null;
		
		int objectId = 0;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(GET_SPAWN_OBJID_QUERY);
			stmt.setInt(1, spawnId);
			stmt.setBoolean(2, isSpawned);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
			{
				objectId = rs.getInt("object_id");
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.warn("Cannot retrieve object id for spawn #"+spawnId+": "+e.getMessage(), e);
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return objectId;
	}
	
	@Override
	public SpawnTemplate getSpawnTemplate (int spawnId)
	{
		Connection con = null;
		SpawnTemplate spawn = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(GET_SPAWN_QUERY);
			stmt.setInt(1, spawnId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
			{
				spawn = createSpawnTemplate(rs);
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.warn("Cannot retrieve spawn #"+spawnId+": "+e.getMessage(), e);
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				} catch (Exception e) {}
			}
		}
		
		return spawn;
	}
}
