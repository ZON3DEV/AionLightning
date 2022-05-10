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
package org.openaion.gameserver.itemengine.actions;

import java.util.List;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.model.templates.item.ItemRace;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;

import gnu.trove.TIntIntHashMap;


/**
 * @author Rolandas
 *
 */
public class UnwrapAction extends AbstractItemAction
{

	@Override
	public void act(final Player player, final Item parentItem, Item targetItem)
	{
		final Storage storage = player.getInventory();
		ItemRace race = player.getCommonData().getRace() == Race.ASMODIANS ?
			ItemRace.ASMODIANS : ItemRace.ELYOS;
		final TIntIntHashMap itemCountMap = 
			DataManager.WRAPPED_ITEM_DATA.rollItems(parentItem.getItemId(), player.getLevel(), race);
		
		int slotsNeeded = 0;
		for (int itemId : itemCountMap.keys())
		{
			int countToAdd = itemCountMap.get(itemId);
			List<Item> items = storage.getItemsByItemId(itemId);
			ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(itemId);
			int stackCount = template.getMaxStackCount();
			
			if (items.size() != 0)
			{
				for (Item stackItem : items)
				{
					int freeSpace = template.getMaxStackCount() - (int)stackItem.getItemCount();
					countToAdd -= freeSpace;
				}
			}
			
			if (countToAdd <= stackCount)
				slotsNeeded++;
			else
			{
				int count = countToAdd / stackCount;
				if (countToAdd % stackCount != 0)
					count++;
				slotsNeeded += count;
			}

		}
		
		if (storage.getNumberOfFreeSlots() < slotsNeeded - 1)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
			return;
		}
		
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
			parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));
		player.getController().cancelTask(TaskId.ITEM_USE);
		player.getController().addNewTask(TaskId.ITEM_USE,
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem
					.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 1, 0));

				storage.removeFromBagByObjectId(parentItem.getObjectId(), 1);
				for (int itemId : itemCountMap.keys())
				{
					ItemService.addItem(player, itemId, itemCountMap.get(itemId));
				}
			}
		}, 5000));

	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return true;
	}

}
