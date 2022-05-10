package org.openaion.gameserver.model.gameobjects.player;

import java.util.LinkedHashMap;

import org.openaion.gameserver.model.gameobjects.Item;


/**
 * @author ginho1
 */
public class Repurchase
{
	private LinkedHashMap<Integer, Item> items;

	public Repurchase()
	{
	    items = new LinkedHashMap<Integer, Item>();
	}

	public void addItem(int itemObjId, Item item)
	{
	    items.put(itemObjId, item);
	}

	public void removeItem(int itemObjId)
	{
		if(items.containsKey(itemObjId))
		{
			LinkedHashMap<Integer, Item> newItems = new LinkedHashMap<Integer, Item>();
			for(int itemObjIds : items.keySet())
			{
				if(itemObjId != itemObjIds)
					newItems.put(itemObjIds, items.get(itemObjIds));
			}
			this.items = newItems;
		}
	}

	public Item getItem(int itemObjId)
	{
		if(items.containsKey(itemObjId))
			return items.get(itemObjId);
		
		return null;
	}

	public LinkedHashMap<Integer, Item> getRepurchaseItems()
	{
		return items;
	}
}
