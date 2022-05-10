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
package org.openaion.gameserver.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.FlightZoneData;
import org.openaion.gameserver.dataholders.ZoneData;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.zone.ZoneTemplate;
import org.openaion.gameserver.task.AbstractFIFOPeriodicTaskManager;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.MapRegion;
import org.openaion.gameserver.world.WorldPosition;
import org.openaion.gameserver.world.zone.FlightZoneInstance;
import org.openaion.gameserver.world.zone.ZoneInstance;
import org.openaion.gameserver.world.zone.ZoneName;


/**
 * @author ATracer
 *
 */
public final class ZoneService extends AbstractFIFOPeriodicTaskManager<Player>
{
	private Map<ZoneName, ZoneInstance> zoneMap;
	private Map<ZoneName, FlightZoneInstance> flightZoneMap;
	private Map<Integer, Collection<ZoneInstance>> zoneByMapIdMap;
	private Map<Integer, Collection<FlightZoneInstance>> flightZoneByMapIdMap;
	
	private ZoneData zoneData;
	private FlightZoneData flightZoneData;
	
	private static final long DROWN_PERIOD = 2000;
	
	public static final ZoneService getInstance()
	{
		return SingletonHolder.instance;
	}

	private ZoneService()
	{
		super(4000);
		this.zoneData = DataManager.ZONE_DATA;
		this.flightZoneData = DataManager.FLIGHT_ZONE_DATA;
		this.zoneMap = new HashMap<ZoneName, ZoneInstance>();
		this.zoneByMapIdMap = new HashMap<Integer, Collection<ZoneInstance>>();
		this.flightZoneMap = new HashMap<ZoneName, FlightZoneInstance>();
		this.flightZoneByMapIdMap = new HashMap<Integer, Collection<FlightZoneInstance>>();
		initializeZones();
		initializeFlightZones();
	}
	

	@Override
	protected void callTask(Player player)
	{
		if(player != null)
		{
			for(byte mask; (mask = player.getController().getZoneUpdateMask()) != 0;)
			{
				for(ZoneUpdateMode mode : VALUES)
				{
					mode.tryUpdateZone(player, mask);
				}
			}
		}
	}
	
	private static final ZoneUpdateMode[]	VALUES	= ZoneUpdateMode.values();
	
	/**
	 *  Zone update can be either partial (ZONE_UPDATE) or complete (ZONE_REFRESH)
	 */
	public static enum ZoneUpdateMode
	{
		ZONE_UPDATE {
			@Override
			public void zoneTask(Player player)
			{
				player.getController().updateZoneImpl();
				player.getController().checkWaterLevel();
			}
		},
		ZONE_REFRESH {
			@Override
			public void zoneTask(Player player)
			{
				player.getController().refreshZoneImpl();
			}
		}
		;

		private final byte	MASK;

		private ZoneUpdateMode()
		{
			MASK = (byte) (1 << ordinal());
		}

		public byte mask()
		{
			return MASK;
		}

		protected abstract void zoneTask(Player player);

		protected final void tryUpdateZone(final Player player, byte mask)
		{
			if((mask & mask()) == mask())
			{
				zoneTask(player);		
				player.getController().removeZoneUpdateMask(this);
			}
		}
	}


	/**
	 *  Initializes zone instances using zone templates from xml
	 *  Adds neighbors to each zone instance using lookup by ZoneName
	 */
	private void initializeZones()
	{
		Iterator<ZoneTemplate> iterator = zoneData.iterator();
		while(iterator.hasNext())
		{
			ZoneTemplate template = iterator.next();
			ZoneInstance instance = new ZoneInstance(template);
			zoneMap.put(template.getName(), instance);

			Collection<ZoneInstance> zoneListForMap = zoneByMapIdMap.get(template.getMapid());
			if(zoneListForMap == null)
			{
				zoneListForMap = createZoneSetCollection();
				zoneByMapIdMap.put(template.getMapid(), zoneListForMap);
			}
			zoneListForMap.add(instance);
		}

		for(ZoneInstance zoneInstance : zoneMap.values())
		{
			ZoneTemplate template = zoneInstance.getTemplate();
			
			Collection<ZoneInstance> neighbors = createZoneSetCollection();
			for(ZoneName zone : template.getLink())
			{
				neighbors.add(zoneMap.get(zone));
			}
			zoneInstance.setNeighbors(neighbors);
		}
	}
	
	/**
	 *  Initializes zone instances using zone templates from xml
	 *  Adds neighbors to each zone instance using lookup by ZoneName
	 */
	private void initializeFlightZones()
	{
		Iterator<ZoneTemplate> iterator = flightZoneData.iterator();
		while(iterator.hasNext())
		{
			ZoneTemplate template = iterator.next();
			FlightZoneInstance instance = new FlightZoneInstance(template);
			flightZoneMap.put(template.getName(), instance);

			Collection<FlightZoneInstance> flightZoneListForMap = flightZoneByMapIdMap.get(template.getMapid());
			if(flightZoneListForMap == null)
			{
				flightZoneListForMap = new ArrayList<FlightZoneInstance>();
				flightZoneByMapIdMap.put(template.getMapid(), flightZoneListForMap);
			}
			flightZoneListForMap.add(instance);
		}
		
		// Add Flight Zones to zones it connects with...?
		/*
		for(ZoneInstance zoneInstance : zoneMap.values())
		{
			ZoneTemplate template = zoneInstance.getTemplate();
			
			Collection<ZoneInstance> neighbors = createZoneSetCollection();
			for(ZoneName zone : template.getLink())
			{
				neighbors.add(zoneMap.get(zone));
			}
			zoneInstance.setNeighbors(neighbors);
		}
		 */
	}
	
	/**
	 * Collection that sorts zone instances according to the template priority
	 * Zone with lower priority has higher importance
	 * 
	 * @return
	 */
	private Collection<ZoneInstance> createZoneSetCollection()
	{
		SortedSet<ZoneInstance> collection = new TreeSet<ZoneInstance>(new Comparator<ZoneInstance>(){

			@Override
			public int compare(ZoneInstance o1, ZoneInstance o2)
			{
				return o1.getPriority() > o2.getPriority() ? 1 : -1;
			}
			
		});
		return collection;
	}
	
	/**
	 *  Will check current zone of player and call corresponding controller methods
	 *  
	 * @param player
	 */
	public void checkZone(Player player)
	{
		ZoneInstance currentInstance = player.getZoneInstance();
		if(currentInstance == null)
		{
			return;
		}

		Collection<ZoneInstance> neighbors = currentInstance.getNeighbors();
		if(neighbors == null)
			return;
		
		for(ZoneInstance zone : neighbors)
		{
			if(checkPointInZone(zone, player.getPosition()))
			{
				player.setZoneInstance(zone);
				player.getController().onEnterZone(zone);
				player.getController().onLeaveZone(currentInstance);
				return;
			}
		}
	}

	/**
	 * @param player
	 */
	public void findZoneInCurrentMap(Player player)
	{
		MapRegion mapRegion = player.getActiveRegion();
		if(mapRegion == null)
			return;
		
		Collection<ZoneInstance> zones = zoneByMapIdMap.get(mapRegion.getMapId());
		if(zones == null)
		{
			player.getController().resetZone();
			return;
		}			
		
		for(ZoneInstance zone : zones)
		{
			if(checkPointInZone(zone, player.getPosition()))
			{
				player.setZoneInstance(zone);
				player.getController().onEnterZone(zone);
				return;
			}
		}
	}
	/**
	 * @param worldPosition
	 */
	public FlightZoneInstance findFlightZoneInCurrentMap(WorldPosition worldPosition)
	{
		if(worldPosition == null)
			return null;
		
		Collection<FlightZoneInstance> zones = flightZoneByMapIdMap.get(worldPosition.getMapId());
		if(zones == null)
		{
			return null;
		}
		
		for(FlightZoneInstance zone : zones)
		{
			if(checkPointInFlightZone(zone, worldPosition))
				return zone;
		}
		return null;
	}
	
	/**
	 * @param worldId
	 * @return
	 */
	public boolean mapHasFightZones(int worldId)
	{
		return (null != flightZoneByMapIdMap.get(worldId));
	}
	
	/**
	 *  Checks whether player is inside specific zone
	 *  
	 * @param player
	 * @param zoneName
	 * @return true if player is inside specified zone
	 */
	public boolean isInsideZone(Player player, ZoneName zoneName)
	{
		ZoneInstance zoneInstance = zoneMap.get(zoneName);
		if(zoneInstance == null)
			return false;
		
		return checkPointInZone(zoneInstance, player.getPosition());
	}
	
	/**
	 *  Checks whether player is inside specific zone
	 *  
	 * @param player
	 * @param zoneName
	 * @return true if player is inside specified zone
	 */
	public boolean isInsideFlightZone(Player player, ZoneName zoneName)
	{
		FlightZoneInstance flightZoneInstance = flightZoneMap.get(zoneName);
		if(flightZoneInstance == null)
			return false;
		
		return checkPointInFlightZone(flightZoneInstance, player.getPosition());
	}

	/**
	 *  Main algorithm that analyzes point-in-polygon
	 *  
	 * @param zone
	 * @param position
	 * @return
	 */
	private boolean checkPointInFlightZone(FlightZoneInstance zone, WorldPosition position)
	{
		int corners = zone.getCorners();
		float[] xCoords = zone.getxCoordinates();
		float[] yCoords = zone.getyCoordinates();
		
		float x = position.getX();
		float y = position.getY();
		
		int i, j = corners-1;
		boolean  inside = false;

		for (i=0; i<corners; i++) 
		{
			if (yCoords[i] < y && yCoords[j] >= y || yCoords[j] < y && yCoords[i] >= y) 
			{
				if (xCoords[i]+(y-yCoords[i])/(yCoords[j]-yCoords[i])*(xCoords[j]-xCoords[i])<x) 
				{
					inside = !inside; 
				}
			}
			j=i; 
		}
		return inside;
	}
	
	/**
	 *  Main algorithm that analyzes point-in-polygon
	 *  
	 * @param zone
	 * @param position
	 * @return
	 */
	private boolean checkPointInZone(ZoneInstance zone, WorldPosition position)
	{
		int corners = zone.getCorners();
		float[] xCoords = zone.getxCoordinates();
		float[] yCoords = zone.getyCoordinates();
		
		float top = zone.getTop();
		float bottom = zone.getBottom();
		
		float x = position.getX();
		float y = position.getY();
		float z = position.getZ();
		
		//first z coordinate is checked
		if(top != 0 || bottom != 0)//not defined
		{
			if(z > top || z < bottom)
				return false;
		}
		
		int i, j = corners-1;
		boolean  inside = false;

		for (i=0; i<corners; i++) 
		{
			if (yCoords[i] < y && yCoords[j] >= y || yCoords[j] < y && yCoords[i] >= y) 
			{
				if (xCoords[i]+(y-yCoords[i])/(yCoords[j]-yCoords[i])*(xCoords[j]-xCoords[i])<x) 
				{
					inside = !inside; 
				}
			}
			j=i; 
		}

		return inside;
	}

	/**
	 * Drowning / immediate death in maps related functionality
	 */
	
	/**
	 * 
	 * @param player
	 */
	public void startDrowning(Player player)
	{
		if(!isDrowning(player))
			scheduleDrowningTask(player);
	}
	
	/**
	 * 
	 * @param player
	 */
	public void stopDrowning(Player player)
	{
		if(isDrowning(player))
		{
			player.getController().cancelTask(TaskId.DROWN);
		}
		
	}
	
	/**
	 * 
	 * @param player
	 * @return
	 */
	private boolean isDrowning(Player player)
	{
		return player.getController().getTask(TaskId.DROWN) == null ? false : true;
	}
	
	/**
	 * 
	 * @param player
	 */
	private void scheduleDrowningTask(final Player player)
	{
		player.getController().addTask(TaskId.DROWN, ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){
			@Override
			public void run()
			{
				int value = Math.round(player.getLifeStats().getMaxHp() / 10);
				//TODO retail emotion, attack_status packets sending
				if(!player.getLifeStats().isAlreadyDead())
				{
					player.getLifeStats().reduceHp(value, null);
					player.getLifeStats().sendHpPacketUpdate();
				}
				else
				{
					stopDrowning(player);
				}
			}
		}, 0, DROWN_PERIOD));
	}


	/* (non-Javadoc)
	 * @see org.openaion.gameserver.task.AbstractFIFOPeriodicTaskManager#getCalledMethodName()
	 */
	@Override
	protected String getCalledMethodName()
	{
		return "zoneService()";
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ZoneService instance = new ZoneService();
	}

}
