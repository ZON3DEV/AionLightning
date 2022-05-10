/**
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
package org.openaion.gameserver.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.ObjectContainer;
import org.openaion.gameserver.model.templates.WorldMapTemplate;
import org.openaion.gameserver.utils.idfactory.IDFactory;
import org.openaion.gameserver.world.exceptions.AlreadySpawnedException;
import org.openaion.gameserver.world.exceptions.NotSetPositionException;
import org.openaion.gameserver.world.exceptions.WorldMapNotExistException;


/**
 * World object for storing and spawning, despawning etc players and other in-game objects. It also manage WorldMaps and
 * instances.
 * 
 * @author -Nemesiss-
 * 
 */
public class World extends ObjectContainer
{
	/**
	 * Logger for this class.
	 */
	private static final Logger				log			= Logger.getLogger(World.class);

	/**
	 * Container with all players that entered world.
	 */
	
	/**
	 * World maps supported by server.
	 */
	private Map<Integer, WorldMap>	worldMaps;


	/**
	 * Constructor.
	 */
	private World()
	{
		super();
		worldMaps	= Collections.synchronizedMap(new HashMap<Integer, WorldMap> ());

		for(WorldMapTemplate template : DataManager.WORLD_MAPS_DATA)
		{
			worldMaps.put(template.getMapId(), new WorldMap(template, this));
		}
		log.info("World: "+worldMaps.size()+" worlds map created.");
	}

	public static final World getInstance()
	{
		return SingletonHolder.instance;
	}

	/**
	 * Return World Map by id
	 * 
	 * @param id
	 *            - id of world map.
	 * @return World map.
	 */
	public WorldMap getWorldMap(int id)
	{
		WorldMap map = worldMaps.get(id);
		/**
		 * Check if world map exist
		 */
		if(map == null)
			throw new WorldMapNotExistException("Map: " + id + " not exist!");
		return map;
	}

	/**
	 * Update position of VisibleObject [used when object is moving on one map instance]. Check if active map region
	 * changed and do all needed updates.
	 * 
	 * @param object
	 * @param newX
	 * @param newY
	 * @param newZ
	 * @param newHeading
	 */
	public void updatePosition(VisibleObject object, float newX, float newY, float newZ, byte newHeading)
	{
		this.updatePosition(object, newX, newY, newZ, newHeading, true);
	}
	
	/**
	 *  
	 * @param object
	 * @param newX
	 * @param newY
	 * @param newZ
	 * @param newHeading
	 */
	public void updatePosition(VisibleObject object, float newX, float newY, float newZ, byte newHeading, boolean updateKnownList)
	{
		//prevent updating object position in despawned state
		if(!object.isSpawned())
			return;
		
		object.getPosition().setXYZH(newX, newY, newZ, newHeading);

		MapRegion oldRegion = object.getActiveRegion();
		if(oldRegion == null)
		{
			log.warn(String.format("CHECKPOINT: oldregion is null, object coordinates - %d %d %d", object.getX(), object.getY(), object.getY()));
			return;
		}
		
		MapRegion newRegion = oldRegion.getParent().getRegion(object);

		if(newRegion != oldRegion)
		{
			newRegion.storeObject(object);
			oldRegion.removeObject(object);
			object.getPosition().setMapRegion(newRegion);
		}
		
		// Do not update if there is no player who cares around
		if(updateKnownList && (object instanceof Player || object.getKnownList().getPlayersCount() > 0))
			object.updateKnownlist();
	}

	/**
	 * Set position of VisibleObject without spawning [object will be invisible]. If object is spawned it will be
	 * despawned first.
	 * 
	 * @param object
	 * @param mapId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @throws NotSetPositionException
	 *             when object has not set position before.
	 */
	public void setPosition(VisibleObject object, int mapId, float x, float y, float z, byte heading)
	{
		int instanceId = 1;	
		if(object.getWorldId() == mapId)
		{
			instanceId = object.getInstanceId();
		}
		this.setPosition(object, mapId, instanceId, x, y, z, heading);
	}
	
	/**
	 * 
	 * @param object
	 * @param mapId
	 * @param instance
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 */
	public void setPosition(VisibleObject object, int mapId, int instance, float x, float y, float z, byte heading)
	{
		if(object.isSpawned())
			despawn(object);
		object.getPosition().setXYZH(x, y, z, heading);
		object.getPosition().setMapId(mapId);
		object.getPosition().setMapRegion(getWorldMap(mapId).getWorldMapInstanceById(instance).getRegion(object));
	}
		

	/**
	 * Creates and return {@link WorldPosition} object, representing position with given parameters.
	 * 
	 * @param mapId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @return WorldPosition
	 */
	public WorldPosition createPosition(int mapId, float x, float y, float z, byte heading)
	{
		WorldPosition position = new WorldPosition();
		position.setXYZH(x, y, z, heading);
		position.setMapId(mapId);
		position.setMapRegion(getWorldMap(mapId).getWorldMapInstance().getRegion(x, y));
		return position;
	}

	/**
	 * Spawn VisibleObject at current position [use setPosition ]. Object will be visible by others and will see other
	 * objects.
	 * 
	 * @param object
	 * @throws AlreadySpawnedException
	 *             when object is already spawned.
	 */
	public void spawn(VisibleObject object)
	{
		if(object.isSpawned())
			throw new AlreadySpawnedException();

		if (!allObjects.containsKey(object.getObjectId()))
		{
			storeObject(object);
		}
		
		object.getPosition().setIsSpawned(true);
		if(object.getSpawn() != null)
			object.getSpawn().setSpawned(true, object.getInstanceId());
		object.getActiveRegion().getParent().storeObject(object);
		object.getActiveRegion().storeObject(object);

		object.updateKnownlist();
	}
	
	public void despawn(VisibleObject object)
	{
		despawn(object, false);
	}

	/**
	 * Despawn VisibleObject, object will become invisible and object position will become invalid. All others objects
	 * will be noticed that this object is no longer visible.
	 * 
	 * @throws NullPointerException
	 *             if object is already despawned
	 */
	public void despawn(VisibleObject object, boolean instance)
	{	
		if(object.getActiveRegion() != null)
		{ // can be null if an instance gets deleted?
			if(object.getActiveRegion().getParent() != null)
				object.getActiveRegion().getParent().removeObject(object);
			
			if (object.getActiveRegion()!=null)
				object.getActiveRegion().removeObject(object);
		}
		
		object.getPosition().setIsSpawned(false);
		
		if(object.getSpawn() != null)
		{
			object.getSpawn().setSpawned(false, object.getInstanceId());
		}	
		
		object.clearKnownlist();
		
		if(object instanceof Npc && instance)
		{
			Npc npc = (Npc)object;
			npc.stopShoutThread();
			npc.getAi().clearDesires();
			npc.getAi().clearEventHandler();
			npc.getAi().clearStateHandler();
			npc.getEffectController().removeAllEffects();
			npc.getLifeStats().cancelAllTasks();
			npc.getAggroList().clear();
			npc.setPosition(null);
			npc.setObserveController(null);
			npc.setKnownlist(null);
			npc.setGameStats(null);
			npc.setLifeStats(null);
			npc.setMoveController(null);
			npc.setAi(null);
			npc.setEffectController(null);
		}
		
		super.removeObject(object);
	}
	
	@Override
	public void removeObject (AionObject object)
	{
		super.removeObject(object);
		
		if (object instanceof Npc)
			IDFactory.getInstance().releaseId(object.getObjectId());
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final World instance = new World();
	}
}
