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
import java.util.List;
import java.util.Set;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PrivateStore;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.items.GodStone;
import org.openaion.gameserver.model.items.ManaStone;
import org.openaion.gameserver.model.trade.TradeItem;
import org.openaion.gameserver.model.trade.TradeList;
import org.openaion.gameserver.model.trade.TradePSItem;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_PRIVATE_STORE_NAME;
import org.openaion.gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Simple
 * 
 */
public class PrivateStoreService
{

	/**
	 * @param activePlayer
	 * @param itemObjId
	 * @param itemId
	 * @param itemAmount
	 * @param itemPrice
	 */
	public static void addItem(Player activePlayer, TradePSItem[] tradePSItems)
	{
		/**
		 * Check if player try to get super speed exploit
		 */
		if(CreatureState.ACTIVE.getId() != activePlayer.getState())
			return;
		/**
		 * Check if player already has a store, if not create one
		 */
		if(activePlayer.getStore() == null)
			createStore(activePlayer);

		/**
		 * Define store to make things easier
		 */
		PrivateStore store = activePlayer.getStore();

		/**
		 * Check if player owns itemObjId else don't add item
		 */
		for(int i = 0; i < tradePSItems.length; i++)
		{
			Item item = getItemByObjId(activePlayer, tradePSItems[i].getItemObjId());
			if(item != null && item.getItemTemplate().isTradeable())
			{
				if(!validateItem(item, tradePSItems[i].getItemId(), tradePSItems[i].getCount()))
					return;
				
				//check if item isnt soul bounded already
				if (item.isSoulBound())
					return;
				/**
				 * Add item to private store
				 */
				store.addItemToSell(tradePSItems[i].getItemObjId(), tradePSItems[i]);
			}
		}
	}

	/**
	 * A check isn't really needed.....
	 * 
	 * @return
	 */
	private static boolean validateItem(Item item, int itemId, long itemAmount)
	{
		if (item.getItemTemplate().getTemplateId() != itemId || itemAmount > item.getItemCount() || itemAmount < 1)
		{
			return false;
		}
		return true;
    }

	/**
	 * This method will create the player's store
	 * 
	 * @param activePlayer
	 */
	private static void createStore(Player activePlayer)
	{
		activePlayer.setStore(new PrivateStore(activePlayer));
		activePlayer.setState(CreatureState.PRIVATE_SHOP);
		PacketSendUtility.broadcastPacket(activePlayer, new SM_EMOTION(activePlayer, EmotionType.OPEN_PRIVATESHOP, 0, 0), true);

		/**
		 * Check if player try to get super speed exploit
		 */
		if(CreatureState.PRIVATE_SHOP.getId() != activePlayer.getState())
			return;
	}

	/**
	 * This method will destroy the player's store
	 * 
	 * @param activePlayer
	 */
	public static void closePrivateStore(Player activePlayer)
	{
		activePlayer.setStore(null);
		activePlayer.unsetState(CreatureState.PRIVATE_SHOP);
		PacketSendUtility.broadcastPacket(activePlayer, new SM_EMOTION(activePlayer, EmotionType.CLOSE_PRIVATESHOP, 0, 0), true);
	}

	/**
	 * This method will move the item to the new player and move kinah to item owner
	 */
	public static void sellStoreItem(Player seller, Player buyer, TradeList tradeList)
	{
		/**
		 * 1. Check if we are busy with two valid participants
		 */
		if(!validateParticipants(seller, buyer))
			return;

		/**
		 * Define store to make life easier
		 */
		PrivateStore store = seller.getStore();

		/**
		 * 2. Load all item object id's and validate if seller really owns them
		 */
		tradeList = loadObjIds(seller, tradeList);
		if(tradeList == null)
			return; // Invalid items found or store was empty

		/**
		 * 3. Check free slots
		 */
		Storage inventory = buyer.getInventory();
		int freeSlots = inventory.getLimit() - inventory.getAllItems().size() + 1;
		if(freeSlots < tradeList.size())
			return; // TODO message
		
		/**
		 * Create total price and items
		 */
		long price = getTotalPrice(store, tradeList);

		/**
		 * Check if player has enough kinah and remove it
		 */
		if(getKinahAmount(buyer) >= price)
		{
			/**
			 * Decrease kinah for buyer and Increase kinah for seller
			 */
			boolean decreaseResult = inventory.decreaseKinah(price);
			if(!decreaseResult)
				return;
			
			seller.getInventory().increaseKinah(price);

			List<Item> newItems = new ArrayList<Item>();
			for(TradeItem tradeItem : tradeList.getTradeItems())
			{
				Item item = getItemByObjId(seller, tradeItem.getItemId());
				if(item != null)
				{
					TradePSItem storeItem = store.getTradeItemById(tradeItem.getItemId());
					//Fix "Private store stackable items dupe" by Asanka
					if(item.getItemCount() < tradeItem.getCount()) 
					{	
						PacketSendUtility.sendMessage(buyer, "You cannot buy more than player can sell.");
						return;
					}
					Set<ManaStone> manaStones = null;
					if(item.hasManaStones())
						manaStones = item.getItemStones();
					
					GodStone godStone = item.getGodStone();
					// Decrease kinah for buyer and Increase kinah for seller
					decreaseKinahAmount(buyer, price);
					increaseKinahAmount(seller, price);
					// Decrease/remove item from store and add them to buyer
					decreaseItemFromPlayer(seller, item, tradeItem);
					ItemService.addFullItem(buyer, item.getItemTemplate().getTemplateId(), tradeItem.getCount(), manaStones, godStone, item.getEnchantLevel(), item.getCrafterName(), item.getTempItemTimeLeft(), item.getTempTradeTimeLeft());
					if(storeItem.getCount() == tradeItem.getCount())
						store.removeItem(storeItem.getItemObjId());
				}
			}

			/**
			 * Add item to buyer's inventory
			 */
			if(newItems.size() > 0)
				PacketSendUtility.sendPacket(buyer, new SM_INVENTORY_UPDATE(newItems));

			/**
			 * Remove item from store and check if last item
			 */
			if(store.getSoldItems().size() == 0)
				closePrivateStore(seller);
			return;
		}
	}

	/**
	 * Decrease item count and update inventory
	 * 
	 * @param seller
	 * @param item
	 */
	private static void decreaseItemFromPlayer(Player seller, Item item, TradeItem tradeItem)
	{
		seller.getInventory().decreaseItemCount(item, tradeItem.getCount());
		PacketSendUtility.sendPacket(seller, new SM_UPDATE_ITEM(item));
		PrivateStore store = seller.getStore();
		store.getTradeItemById(item.getObjectId()).decreaseCount(tradeItem.getCount());
	}

	/**
	 * @param seller
	 * @param tradeList
	 * @return
	 */
	private static TradeList loadObjIds(Player seller, TradeList tradeList)
	{
		PrivateStore store = seller.getStore();
		TradeList newTradeList = new TradeList();

		for(TradeItem tradeItem : tradeList.getTradeItems())
		{
			int i = 0;
			for(int itemObjId : store.getSoldItems().keySet())
			{
				if(i == tradeItem.getItemId())
					newTradeList.addPSItem(itemObjId, tradeItem.getCount());
				i++;
			}
		}

		/**
		 * Check if player still owns items
		 */
		if(!validateBuyItems(seller, newTradeList))
			return null;

		return newTradeList;
	}

	/**
	 * @param player1
	 * @param player2
	 */
	private static boolean validateParticipants(Player itemOwner, Player newOwner)
	{
		return itemOwner != null && newOwner != null && itemOwner.isOnline() && newOwner.isOnline();
	}

	/**
	 * @param tradeList
	 */
	private static boolean validateBuyItems(Player seller, TradeList tradeList)
	{
		for(TradeItem tradeItem : tradeList.getTradeItems())
		{
			Item item = seller.getInventory().getItemByObjId(tradeItem.getItemId());

			// 1) don't allow to sell fake items;
			if(item == null)
				return false;
			// check amount of item, if amount to buy is not higher than amount available
			if(!validateItem(item, item.getItemId(), tradeItem.getCount()))
				return false;
		}
		return true;
	}

	/**
	 * This method will return the amount of kinah of a player
	 * 
	 * @param newOwner
	 * @return
	 */
	private static long getKinahAmount(Player player)
	{
		return player.getInventory().getKinahItem().getItemCount();
	}

	@Deprecated
	/**
	 * This method will decrease the kinah amount of a player
	 * 
	 * @param player
	 * @param price
	 */
	private static void decreaseKinahAmount(Player player, long price)
	{
		player.getInventory().decreaseKinah(price);
	}

	@Deprecated
	/**
	 * This method will increase the kinah amount of a player
	 * 
	 * @param player
	 * @param price
	 */
	private static void increaseKinahAmount(Player player, long price)
	{
		player.getInventory().increaseKinah(price);
	}

	/**
	 * This method will return the item in a inventory by object id
	 * 
	 * @param player
	 * @param tradePSItems
	 * @return
	 */
	private static Item getItemByObjId(Player seller, int itemObjId)
	{
		return seller.getInventory().getItemByObjId(itemObjId);
	}

	/**
	 * This method will return the total price of the tradelist
	 * 
	 * @param store
	 * @param tradeList
	 * @return
	 */
	private static long getTotalPrice(PrivateStore store, TradeList tradeList)
	{
		long totalprice = 0;
		for(TradeItem tradeItem : tradeList.getTradeItems())
		{
			TradePSItem item = store.getTradeItemById(tradeItem.getItemId());
			totalprice += item.getPrice() * tradeItem.getCount();
		}
		return totalprice;
	}

	/**
	 * @param activePlayer
	 */
	public static void openPrivateStore(Player activePlayer, String name)
	{
		if(name != null)
		{
			activePlayer.getStore().setStoreMessage(name);
			PacketSendUtility.broadcastPacket(activePlayer,
				new SM_PRIVATE_STORE_NAME(activePlayer.getObjectId(), name), true);
		}
		else
		{
			PacketSendUtility.broadcastPacket(activePlayer, new SM_PRIVATE_STORE_NAME(activePlayer.getObjectId(), ""),
				true);
		}
	}
}
