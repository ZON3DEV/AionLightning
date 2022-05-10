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
package org.openaion.gameserver.model.legion;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.InventoryDAO;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.model.gameobjects.player.StorageType;


/**
 * @author Simple
 */
public class LegionWarehouse extends Storage
{
	private Legion	legion;
	private int	user = 0;
	
	public LegionWarehouse(Legion legion)
	{
		super(StorageType.LEGION_WAREHOUSE);
		this.legion = legion;
		this.setLimit(legion.getWarehouseSlots());
	}

	/**
	 * @return the legion
	 */
	public Legion getLegion()
	{
		return this.legion;
	}

	/**
	 * @param legion
	 *            the legion to set
	 */
	public void setOwnerLegion(Legion legion)
	{
		this.legion = legion;
	}
	
	/**
	 *  Used to put item into storage cube at first avaialble slot (no check for existing item)
	 *  During unequip/equip process persistImmediately should be false
	 *  
	 * @param item
	 * @param persistImmediately
	 * @return Item
	 */
	@Override
	public Item putToBag(Item item)
	{
		Item resultItem = storage.putToNextAvailableSlot(item);
		if(resultItem != null)
		{
			resultItem.setItemLocation(storageType);
			DAOManager.getDAO(InventoryDAO.class).store(resultItem, resultItem.getOwnerId());
		}
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return resultItem;
	}
	
	
	public void setUser(int user)
	{
		this.user = user;
	}
	public int getUser()
	{
		return user;
	}
	public boolean isInUse()
	{
		return user != 0;
	}

	
	
}
