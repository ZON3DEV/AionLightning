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
package org.openaion.gameserver.model.gameobjects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.openaion.gameserver.model.drop.DropItem;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 * @author Simple
 * @author Jego
 */
public class DropNpc
{
	private List<Integer>			allowedList			= new ArrayList<Integer>();
	private List<Player>			inRangePlayers		= new ArrayList<Player>();
	private Player					lootingPlayer		= null;
	private int						groupSize			= 0;
	private int						npcId				= 0;
	private LinkedList<DropItem>	specialItemList		= null;
	private DropNpc					nextSpecialDropNpc	= null;

	public DropNpc(List<Integer> allowedList, int npcId)
	{
		this.allowedList = allowedList;
		this.npcId = npcId;
	}

	/**
	 * Everyone is allowed to loot
	 */
	public void setFreeLooting()
	{
		allowedList = null;
	}

	/**
	 * @return true if playerObjId is found in list
	 */
	public boolean containsKey(int playerObjId)
	{
		if(allowedList == null)
			return true;
		return allowedList.contains(playerObjId);
	}

	/**
	 * @param player
	 *            the lootingPlayer to set
	 */
	public void setBeingLooted(Player player)
	{
		this.lootingPlayer = player;
	}

	/**
	 * @return lootingPlayer
	 */
	public Player getBeingLooted()
	{
		return lootingPlayer;
	}	
	
	/**
	 * @return the beingLooted
	 */
	public boolean isBeingLooted()
	{
		return lootingPlayer != null;
	}

	/**
	 * @param groupSize
	 */
	public void setGroupSize(int groupSize)
	{
		this.groupSize = groupSize;
	}

	/**
	 * @return groupSize
	 */
	public int getGroupSize()
	{
		return groupSize;
	}

	/**
	 * @param inRangePlayers
	 */
	public void setInRangePlayers(List<Player> inRangePlayers)
	{
		this.inRangePlayers = inRangePlayers;
	}

	/**
	 * @return the inRangePlayers
	 */
	public List<Player> getInRangePlayers()
	{
		return inRangePlayers;
	}

	/**
	 * Adds an item to the list of items that should be rolled/bid on.
	 * 
	 * @param item
	 * @return True if the item is added to the list of special drops.
	 */
	public boolean addSpecialItem(DropItem item)
	{
		if(item.isRegisteredSpecial())
			return false;
		item.setRegisteredSpecial();

		if(specialItemList == null)
		{
			specialItemList = new LinkedList<DropItem>();
		}
		specialItemList.add(item);
		return true;
	}

	/**
	 * @return True if there are items to roll/bid on.
	 */
	public boolean hasSpecialItems()
	{
		if(specialItemList == null)
		{
			return false;
		}
		return specialItemList.size() != 0;
	}

	/**
	 * Delete all the special items from the special items list.
	 */
	public void resetSpecialItems()
	{
		if(specialItemList != null)
		{
			specialItemList.clear();
		}
		specialItemList = null;
	}

	/**
	 * @return The next item to roll/bid on, or null if there is no item.
	 */
	public DropItem getNextSpecialItem()
	{
		if(specialItemList == null)
		{
			return null;
		}
		return specialItemList.poll();
	}

	/**
	 * Add the next DropNpc object that has special items for the group/alliance.
	 * 
	 * @param dropNpc
	 */
	public void addSpecialDropNpc(DropNpc dropNpc)
	{
		if(npcId == dropNpc.getNpcId())
		{
			return;
		}
		
		if(nextSpecialDropNpc != null)
		{
			if (nextSpecialDropNpc.getNpcId() != dropNpc.getNpcId())
			{
				nextSpecialDropNpc.addSpecialDropNpc(dropNpc);
			}
			
			return;
		}
		
		nextSpecialDropNpc = dropNpc;
	}

	/**
	 * @return The next DropNpc object with special items that have to be rolled/bid on, or null if there is no next npc.
	 */
	public DropNpc getNextSpecialDropNpc()
	{
		return nextSpecialDropNpc;
	}

	/**
	 * @return The id of the Npc this drop is linked to.
	 */
	public int getNpcId()
	{
		return npcId;
	}
}
