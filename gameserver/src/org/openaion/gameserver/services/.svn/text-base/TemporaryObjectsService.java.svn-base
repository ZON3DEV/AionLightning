/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.StorageType;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE_WAREHOUSE_ITEM;
import org.openaion.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;


import javolution.util.FastMap;

/**
 * @author kosyachok
 *
 */
public class TemporaryObjectsService
{
	private FastMap<Integer, AionObject> temporaryObjects = new FastMap<Integer, AionObject>().shared();
	//private FastMap<Long, Integer> objectsTimeCollection = new FastMap<Long, Integer>().shared().setKeyComparator(timeComparator());
	private SortedMap<Long, Integer> objectsTimeCollection = new TreeMap<Long, Integer>();
	
	private int CHECK_TIME_PERIOD = 1000;
	
	private static final Logger	log			= Logger.getLogger(TemporaryObjectsService.class);
	
	private boolean isStarted = false;
	
	private Future<?> checkTask = null;
	
	public static final TemporaryObjectsService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public void start()
	{
		checkTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				checkExpiredObjects();
			}
		}, CHECK_TIME_PERIOD, CHECK_TIME_PERIOD);
		
		isStarted = true;
	}
	
	public void addObject(AionObject object)
	{		
		if(!temporaryObjects.containsKey(object.getObjectId()))
		{		
			if(object instanceof Item)
			{	
				temporaryObjects.put(object.getObjectId(), object);
				objectsTimeCollection.put(((Item)object).getTempItemExpireTime(), object.getObjectId());
			}
			else
				log.warn("Temporary unknown object ADD operation. ObjectId: " + String.valueOf(object.getObjectId()));
		}
		
		if(!isStarted)
			start();
	}
	
	public void removeObject(AionObject object)
	{
		if(temporaryObjects.containsKey(object.getObjectId()))	
			temporaryObjects.remove(object.getObjectId());
	}
	
	
	private void checkExpiredObjects()
	{
		if(objectsTimeCollection.isEmpty() && temporaryObjects.isEmpty())
		{
			if(isStarted)
			{
				checkTask.cancel(false);
				checkTask = null;
				isStarted = false;
			}
			return;
		}
		
		else if(objectsTimeCollection.isEmpty() && !temporaryObjects.isEmpty())
		{
			List<AionObject>objectsToRemove = new ArrayList<AionObject>();
			
			for(AionObject obj : temporaryObjects.values())
			{
				objectsToRemove.add(obj);
			}
			
			for(AionObject obj : objectsToRemove)
			{
				if(obj instanceof Item)
				{
					Item tempItem = (Item)obj;
					
					int ownerId = tempItem.getOwnerId();
					int storageType = tempItem.getItemLocation();
					
					Player player = World.getInstance().findPlayer(ownerId);
					
					if(player != null)
					{
						if(player.getStorage(storageType).removeFromBag(tempItem, true))						
							if(storageType == StorageType.CUBE.getId())
								PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(tempItem.getObjectId()));
							else
								PacketSendUtility.sendPacket(player, new SM_DELETE_WAREHOUSE_ITEM(storageType, tempItem.getObjectId()));
						return;
					}
				}
				
				temporaryObjects.remove(obj.getObjectId());
			}
			
			if(isStarted)
			{
				checkTask.cancel(false);
				checkTask = null;
				isStarted = false;
			}
			return;
		}
		else if(!objectsTimeCollection.isEmpty() && temporaryObjects.isEmpty())
		{
			objectsTimeCollection.clear();
			
			if(isStarted)
			{
				checkTask.cancel(false);
				checkTask = null;
				isStarted = false;
			}			
			return;
		}

		
		SortedMap<Long, Integer> head = objectsTimeCollection.headMap(System.currentTimeMillis() + 1000);
		
		if(head.size() <= 0)
			return;
		
		while(head.size() > 0)
		{			
			long headTime = head.firstKey();
			int headObjId = head.get(headTime);

			if(!temporaryObjects.containsKey(headObjId))
			{
				objectsTimeCollection.remove(headTime);
				head.remove(headTime);
				continue;
			}
		
			AionObject obj = temporaryObjects.get(headObjId);
		
			if(obj == null)
			{
				temporaryObjects.remove(headObjId);
				objectsTimeCollection.remove(headTime);
				head.remove(headTime);
				continue;
			}
		
			if(obj instanceof Item)
			{
				Item tempItem = (Item)obj;
				if(tempItem.getTempItemTimeLeft() <= 0)
				{
					destroyItem(tempItem, headTime, headObjId);
					head.remove(headTime);
				}
			}
			else
			{
				log.warn("Unk object in temp service. ID: " + String.valueOf(obj.getObjectId()));
				temporaryObjects.remove(headObjId);
				objectsTimeCollection.remove(headTime);
				head.remove(headTime);
			}
		}
	}
	
	
	private void destroyItem(Item tempItem, long headTime, int headObjId)
	{		
		int ownerId = tempItem.getOwnerId();
		int storageType = tempItem.getItemLocation();
		
		Player player = World.getInstance().findPlayer(ownerId);
		
		if(player != null)
		{
			boolean canRemove = true;
			
			if(tempItem.isEquipped())
			{
				canRemove = player.getEquipment().removeTemporaryItem(tempItem.getObjectId(), tempItem.getEquipmentSlot());
				
				PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(),
					player.getEquipment().getEquippedItemsWithoutStigma()), true);	
			}
			else
				canRemove = player.getStorage(storageType).removeFromBag(tempItem, true);
			
			if(storageType == StorageType.CUBE.getId())
				PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(tempItem.getObjectId()));
			else
				PacketSendUtility.sendPacket(player, new SM_DELETE_WAREHOUSE_ITEM(storageType, tempItem.getObjectId()));
			
			if(canRemove)
			{
				objectsTimeCollection.remove(headTime);
				temporaryObjects.remove(headObjId);
			}
		}	
		else
		{
			objectsTimeCollection.remove(headTime);
			temporaryObjects.remove(headObjId);
		}
	}


	public void onPlayerLogout(Player player)
	{
		for(Item item : player.getEquipment().getEquippedItems())
		{
			if(item.getTempItemSettedTime() > 0)
				removeObject(item);
		}
		
		for(Item item : player.getInventory().getAllItems())
		{
			if(item.getTempItemSettedTime() > 0)
				removeObject(item);
		}
		
		for(Item item : player.getWarehouse().getAllItems())
		{
			if(item.getTempItemSettedTime() > 0)
				removeObject(item);
		}
	}
	
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final TemporaryObjectsService instance = new TemporaryObjectsService();
	}
}
