package org.openaion.gameserver.dao;

import java.util.Map;

import org.openaion.commons.database.dao.DAO;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;


/**
 * @author blakawk
 *
 */
public abstract class SpawnDAO implements DAO
{
	public enum SpawnType {
		SPAWNED(1),
		DESPAWNED(2),
		REMOVED(3),
		ALL(0);
		
		private int type;
		
		private SpawnType (int type)
		{
			this.type = type;
		}
		
		public int getType ()
		{
			return type;
		}
	}
	
	@Override
	public String getClassName()
	{
		return SpawnDAO.class.getName();
	}
	
	public abstract int addSpawn (int npcId, int adminObjectId, String group, boolean noRespawn, int mapId, float x, float y, float z, byte h, int objectId,int staticid);
	
	public abstract boolean unSpawnGroup (int adminObjectId, String group);
	
	public abstract boolean isSpawned (int adminObjectId, String group);
	
	public abstract int isInDB(int npcId, float x, float y);
	
	public abstract boolean updateHeading(int spawnId, int heading);
	
	public abstract boolean setSpawned (int spawnId, int objectId, boolean isSpawned);
	
	public abstract boolean setGroupSpawned (int adminObjectId, String group, boolean isSpawned);

	public abstract Map<Integer, SpawnTemplate> listSpawns (int adminObjectId, String group, SpawnType type);
	
	public abstract Map<String, Integer> listSpawnGroups (int adminObjectId);
	
	public abstract Map<Integer, SpawnTemplate> getAllSpawns();
	
	public abstract boolean deleteSpawn (int spawnId);
	
	public abstract boolean deleteSpawnGroup (int adminObjectId, String groupName);
	
	public abstract int getSpawnObjectId (int spawnId, boolean isSpawned);
	
	public abstract SpawnTemplate getSpawnTemplate (int spawnId);
}
