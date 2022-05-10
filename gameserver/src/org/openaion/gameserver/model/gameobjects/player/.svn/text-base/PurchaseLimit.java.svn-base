package org.openaion.gameserver.model.gameobjects.player;

import java.util.LinkedHashMap;

/**
 * @author ginho1
 */
public class PurchaseLimit
{
	private LinkedHashMap<Integer, Integer> items;

	public PurchaseLimit()
	{
		items = new LinkedHashMap<Integer, Integer>();
	}

	public void addItem(int itemId, int itemCount)
	{
		if(items.containsKey(itemId))
		{
			LinkedHashMap<Integer, Integer> newItems = new LinkedHashMap<Integer, Integer>();
			for(int itemIds : items.keySet())
			{
				if(itemIds != itemId)
					newItems.put(itemIds, items.get(itemIds));
				else
					newItems.put(itemIds, items.get(itemIds) + itemCount);
			}
			this.items = newItems;

		}else{
			items.put(itemId, itemCount);
		}
	}

	public void removeItem(int itemId)
	{
		if(items.containsKey(itemId))
		{
			this.items.remove(itemId);
		}
	}

	public void reset()
	{
		items = new LinkedHashMap<Integer, Integer>();
	}

	public void setItems(LinkedHashMap<Integer, Integer> items)
	{
		this.items = items;
	}

	public int getItemLimitCount(int itemId)
	{
		if(items.containsKey(itemId))
			return items.get(itemId);

		return 0;
	}

	public LinkedHashMap<Integer, Integer> getItems()
	{
		return items;
	}
}
