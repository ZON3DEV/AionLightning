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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.BrokerDAO;
import org.openaion.gameserver.dao.InventoryDAO;
import org.openaion.gameserver.model.DescriptionId;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.broker.BrokerItemMask;
import org.openaion.gameserver.model.broker.BrokerPlayerCache;
import org.openaion.gameserver.model.broker.BrokerRace;
import org.openaion.gameserver.model.gameobjects.BrokerItem;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_BROKER_ITEMS;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import org.openaion.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import org.openaion.gameserver.task.AbstractFIFOPeriodicTaskManager;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;


/**
 * @author kosyachok
 * @author ATracer
 * 
 */
public class BrokerService
{
	private Map<Integer, BrokerItem>		elyosBrokerItems		= new FastMap<Integer, BrokerItem>().shared();
	private Map<Integer, BrokerItem>		elyosSettledItems		= new FastMap<Integer, BrokerItem>().shared();
	private Map<Integer, BrokerItem>		asmodianBrokerItems		= new FastMap<Integer, BrokerItem>().shared();
	private Map<Integer, BrokerItem>		asmodianSettledItems	= new FastMap<Integer, BrokerItem>().shared();

	private static final Logger				log						= Logger.getLogger(BrokerService.class);

	private final int						DELAY_BROKER_SAVE		= 6000;
	private final int						DELAY_BROKER_CHECK		= 60000;

	private BrokerPeriodicTaskManager		saveManager;

	private Map<Integer, BrokerPlayerCache>	playerBrokerCache		= new FastMap<Integer, BrokerPlayerCache>().shared();

	public static final BrokerService getInstance()
	{
		return SingletonHolder.instance;
	}

	public BrokerService()
	{
		initBrokerService();
		
		saveManager = new BrokerPeriodicTaskManager(DELAY_BROKER_SAVE);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				checkExpiredItems();
			}
		}, DELAY_BROKER_CHECK, DELAY_BROKER_CHECK);
	}

	private void initBrokerService()
	{
		log.info("Loading broker...");
		int loadedBrokerItemsCount = 0;
		int loadedSettledItemsCount = 0;

		List<BrokerItem> brokerItems = DAOManager.getDAO(BrokerDAO.class).loadBroker();

		for(BrokerItem item : brokerItems)
		{
			if(item.getItemBrokerRace() == BrokerRace.ASMODIAN)
			{
				if(item.isSettled())
				{
					asmodianSettledItems.put(item.getItemUniqueId(), item);
					loadedSettledItemsCount++;
				}
				else
				{
					asmodianBrokerItems.put(item.getItemUniqueId(), item);
					loadedBrokerItemsCount++;
				}
			}
			else if(item.getItemBrokerRace() == BrokerRace.ELYOS)
			{
				if(item.isSettled())
				{
					elyosSettledItems.put(item.getItemUniqueId(), item);
					loadedSettledItemsCount++;
				}
				else
				{
					elyosBrokerItems.put(item.getItemUniqueId(), item);
					loadedBrokerItemsCount++;
				}
			}
		}

		log.info("Broker loaded with " + loadedBrokerItemsCount + " broker items, " + loadedSettledItemsCount
			+ " settled items.");
	}

	/**
	 * 
	 * @param player
	 * @param clientMask
	 * @param sortType
	 * @param startPage
	 */
	public void showRequestedItems(Player player, int clientMask, int sortType, int startPage)
	{
		BrokerItem[] searchItems = null;
		int playerBrokerMaskCache = getPlayerMask(player);
		BrokerItemMask brokerMaskById = BrokerItemMask.getBrokerMaskById(clientMask);
		boolean isChidrenMask = brokerMaskById.isChildrenMask(playerBrokerMaskCache);
		if(getFilteredItems(player).length == 0 || !isChidrenMask)
		{
			searchItems = getItemsByMask(player, clientMask, false);
		}
		else if(isChidrenMask)
		{
			searchItems = getItemsByMask(player, clientMask, true);
		}
		else
			searchItems = getFilteredItems(player);

		if(searchItems == null || searchItems.length < 0)
			return;

		int totalSearchItemsCount = searchItems.length;

		getPlayerCache(player).setBrokerSortTypeCache(sortType);
		getPlayerCache(player).setBrokerStartPageCache(startPage);

		sortBrokerItems(searchItems, sortType);
		searchItems = getRequestedPage(searchItems, startPage);

		PacketSendUtility.sendPacket(player, new SM_BROKER_ITEMS(searchItems, totalSearchItemsCount, startPage, 0));
	}

	/**
	 *
	 * @param player
	 * @param clientMask
	 * @param sortType
	 * @param startPage
	 */
	public void showRequestedItems(Player player, int clientMask, int sortType, int startPage, List<Integer> searchItemsId, boolean search)
	{
		BrokerItem[] searchItems = null;
		int playerBrokerMaskCache = getPlayerMask(player);
		BrokerItemMask brokerMaskById = BrokerItemMask.getBrokerMaskById(clientMask);
		boolean isChidrenMask = brokerMaskById.isChildrenMask(playerBrokerMaskCache);
		if (search && searchItemsId != null && clientMask == 0)
		{
			Map<Integer, BrokerItem> brokerItems = getRaceBrokerItems(player.getCommonData().getRace());
			if(brokerItems == null)
				return;
			searchItems = brokerItems.values().toArray(new BrokerItem[brokerItems.values().size()]);
		}
		else if(getFilteredItems(player).length == 0 || !isChidrenMask)
		{
			searchItems = getItemsByMask(player, clientMask, false);
		}
		else if(isChidrenMask)
		{
			searchItems = getItemsByMask(player, clientMask, true);
		}
		else
			searchItems = getFilteredItems(player);

		if(searchItems == null || searchItems.length < 0)
			return;

		int totalSearchItemsCount = searchItems.length;

		getPlayerCache(player).setBrokerSortTypeCache(sortType);
		getPlayerCache(player).setBrokerStartPageCache(startPage);

		if (search && searchItemsId != null)
		{
			List<BrokerItem> itemsFound = new ArrayList<BrokerItem>();
			for (BrokerItem item : searchItems)
			{
				if (searchItemsId.contains(item.getItemId()))
					itemsFound.add(item);
			}
			getPlayerCache(player).setSearchItemsId(searchItemsId);
			searchItems = itemsFound.toArray(new BrokerItem[itemsFound.size()]);
		}
		else
			getPlayerCache(player).setSearchItemsId(null);

		sortBrokerItems(searchItems, sortType);
		searchItems = getRequestedPage(searchItems, startPage);

		PacketSendUtility.sendPacket(player, new SM_BROKER_ITEMS(searchItems, totalSearchItemsCount, startPage));
	}

	/**
	 * 
	 * @param player
	 * @param clientMask
	 * @return
	 */
	private BrokerItem[] getItemsByMask(Player player, int clientMask, boolean cached)
	{
		List<BrokerItem> searchItems = new ArrayList<BrokerItem>();

		BrokerItemMask brokerMask = BrokerItemMask.getBrokerMaskById(clientMask);

		if(cached)
		{
			BrokerItem[] brokerItems = getFilteredItems(player);
			if(brokerItems == null)
				return null;

			for(BrokerItem item : brokerItems)
			{
				if(item == null || item.getItem() == null || item.isSold())
					continue;

				if(brokerMask.isMatches(item.getItem()))
				{
					searchItems.add(item);
				}
			}
		}
		else
		{
			Map<Integer, BrokerItem> brokerItems = getRaceBrokerItems(player.getCommonData().getRace());
			if(brokerItems == null)
				return null;
			for(BrokerItem item : brokerItems.values())
			{
				if(item == null || item.getItem() == null || item.isSold())
					continue;

				if(brokerMask.isMatches(item.getItem()))
				{
					searchItems.add(item);
				}
			}
		}

		BrokerItem[] items = searchItems.toArray(new BrokerItem[searchItems.size()]);
		getPlayerCache(player).setBrokerListCache(items);
		getPlayerCache(player).setBrokerMaskCache(clientMask);

		return items;
	}

	/**
	 * Perform sorting according to sort type
	 * 
	 * @param brokerItems
	 * @param sortType
	 */
	private void sortBrokerItems(BrokerItem[] brokerItems, int sortType)
	{
		Arrays.sort(brokerItems, BrokerItem.getComparatoryByType(sortType));
	}

	/**
	 * 
	 * @param brokerItems
	 * @param startPage
	 * @return
	 */
	private BrokerItem[] getRequestedPage(BrokerItem[] brokerItems, int startPage)
	{
		List<BrokerItem> page = new ArrayList<BrokerItem>();
		int startingElement = startPage * 9;

		for(int i = startingElement, limit = 0; i < brokerItems.length && limit < 45; i++, limit++)
		{
			page.add(brokerItems[i]);
		}

		return page.toArray(new BrokerItem[page.size()]);
	}

	/**
	 * 
	 * @param race
	 * @return
	 */
	private Map<Integer, BrokerItem> getRaceBrokerItems(Race race)
	{
		switch(race)
		{
			case ELYOS:
				return elyosBrokerItems;
			case ASMODIANS:
				return asmodianBrokerItems;
			default:
				return null;
		}
	}

	/**
	 * 
	 * @param race
	 * @return
	 */
	private Map<Integer, BrokerItem> getRaceBrokerSettledItems(Race race)
	{
		switch(race)
		{
			case ELYOS:
				return elyosSettledItems;
			case ASMODIANS:
				return asmodianSettledItems;
			default:
				return null;
		}
	}

	/**
	 * 
	 * @param player
	 * @param itemUniqueId
	 */
	public void buyBrokerItem(Player player, int itemUniqueId)
	{
		boolean isEmptyCache = getFilteredItems(player).length == 0;
		Race playerRace = player.getCommonData().getRace();

		BrokerItem buyingItem = getRaceBrokerItems(playerRace).get(itemUniqueId);
		
		if(buyingItem == null)
			return;
		
		synchronized (buyingItem)
		{
			if(buyingItem == null || buyingItem.isSold() || buyingItem.isSettled())
			{
				if(buyingItem != null && buyingItem.getItem() != null)
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300647, new DescriptionId(buyingItem.getItem().getNameID())));
				return;
			}

			if(player.getObjectId() == buyingItem.getSellerId())
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400750));
				return;
			}
			
			if(player.getInventory().isFull())
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300654));
				return;
			}

			long price = buyingItem.getPrice();

			if(player.getInventory().getKinahItem().getItemCount() < price)
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300759));
				return;
			}
			
			getRaceBrokerItems(playerRace).remove(itemUniqueId);
			putToSettled(playerRace, buyingItem, true);

			if(!isEmptyCache)
			{
				BrokerItem[] newCache = (BrokerItem[]) ArrayUtils.removeElement(getFilteredItems(player), buyingItem);
				getPlayerCache(player).setBrokerListCache(newCache);
			}

			boolean decreaseResult = player.getInventory().decreaseKinah(price);
			if(!decreaseResult)
				return;
			
			Item boughtItem = player.getInventory().putToBag(buyingItem.getItem());

			// create save task
			BrokerOpSaveTask bost = new BrokerOpSaveTask(buyingItem, boughtItem, player.getInventory().getKinahItem(),
				player.getObjectId());
			saveManager.add(bost);

			PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(boughtItem, true));
			PacketSendUtility.sendPacket(player, new SM_BROKER_ITEMS(boughtItem, 2));

			showRequestedItems(player, getPlayerCache(player).getBrokerMaskCache(), getPlayerCache(player)
				.getBrokerSortTypeCache(), getPlayerCache(player).getBrokerStartPageCache());
		}
		
	}

	/**
	 * 
	 * @param race
	 * @param brokerItem
	 * @param isSold
	 */
	private void putToSettled(Race race, BrokerItem brokerItem, boolean isSold)
	{
		if(isSold)
			brokerItem.removeItem();
		else
			brokerItem.setSettled();

		brokerItem.setPersistentState(PersistentState.UPDATE_REQUIRED);

		switch(race)
		{
			case ASMODIANS:
				asmodianSettledItems.put(brokerItem.getItemUniqueId(), brokerItem);
				break;

			case ELYOS:
				elyosSettledItems.put(brokerItem.getItemUniqueId(), brokerItem);
				break;
		}

		Player seller = World.getInstance().findPlayer(brokerItem.getSellerId());

		saveManager.add(new BrokerOpSaveTask(brokerItem));

		if(seller != null)
		{
			Map<Integer, BrokerItem> brokerSettledItems = getRaceBrokerSettledItems(seller.getCommonData().getRace());

			int playerId = seller.getObjectId();

			List<BrokerItem> settledItems = new ArrayList<BrokerItem>();

			long totalKinah = 0;

			for(BrokerItem item : brokerSettledItems.values())
			{
				if(item != null && playerId == item.getSellerId())
				{
					settledItems.add(item);

					if(item.isSold())
						totalKinah += item.getPrice();
				}
			}

			DescriptionId desId = new DescriptionId(brokerItem.getItem().getNameID());
			PacketSendUtility.sendPacket(seller, new SM_SYSTEM_MESSAGE(1301047, desId));
			showRegisteredItems(seller);
			PacketSendUtility.sendPacket(seller, new SM_BROKER_ITEMS(null, totalKinah, 2, 5));
		}
	}

	/**
	 * 
	 * @param player
	 * @param itemUniqueId
	 * @param price
	 */
	public void registerItem(Player player, int itemUniqueId, long price, int itemCount)
	{
		Map<Integer, BrokerItem> brokerItems = getRaceBrokerItems(player.getCommonData().getRace());

		List<BrokerItem> registeredItems = new ArrayList<BrokerItem>();
		int playerId = player.getObjectId();

		for(BrokerItem item : brokerItems.values())
		{
			if(item != null && item.getItem() != null && item.isSold() != true &&
				item.isSettled() != true && playerId == item.getSellerId())
				registeredItems.add(item);
		}

		if(registeredItems.size() >= 15)
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300653));
			return;
		}
		
		Item itemToRegister = player.getInventory().getItemByObjId(itemUniqueId);
		Race playerRace = player.getCommonData().getRace();

		if(itemToRegister == null)
			return;
		
		// Check Trade Hack
		if (!itemToRegister.getItemTemplate().isTradeable())
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300652));
			return;
		}
		
		//check if item isnt soul bounded already
		if (itemToRegister.isSoulBound())
			return;
		
		BrokerRace brRace;

		if(playerRace == Race.ASMODIANS)
			brRace = BrokerRace.ASMODIAN;
		else if(playerRace == Race.ELYOS)
			brRace = BrokerRace.ELYOS;
		else
			return;

		double priceRate = player.getPrices().getGlobalPrices(player.getCommonData().getRace()) * .01;
		double taxRate = player.getPrices().getTaxes(player.getCommonData().getRace()) * .01;
		double priceMod = player.getPrices().getGlobalPricesModifier() * .01;
		double priceFactor = 0.02 * priceRate * taxRate * priceMod;
		long registrationCommition = (long)(price * priceFactor * (registeredItems.size() >= 10 ? 2 : 1));
		
		if(registrationCommition < 10)
			registrationCommition = 10;

		if(player.getInventory().getKinahItem().getItemCount() < registrationCommition)
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300647));
			return;
		}

		if(itemCount < itemToRegister.getItemCount() && itemCount > 0)
		{
			Item newItem = ItemService.newItem(itemToRegister.getItemTemplate().getTemplateId(), itemCount, itemToRegister.getCrafterName(), playerId, itemToRegister.getTempItemTimeLeft(), itemToRegister.getTempTradeTimeLeft());
			player.getInventory().decreaseItemCount(itemToRegister, itemCount);
			PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(itemToRegister));
			itemToRegister = newItem;
		}
		else
		{
			boolean removeResult = player.getInventory().removeFromBag(itemToRegister, false);
			if(!removeResult)
				return;
			PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(itemToRegister.getObjectId()));			
		}
		
		boolean decreaseResult = player.getInventory().decreaseKinah(registrationCommition);
		if(!decreaseResult)
			return;

		itemToRegister.setItemLocation(126);

		BrokerItem newBrokerItem = new BrokerItem(itemToRegister, price, player.getName(), player.getObjectId(), brRace);

		switch(brRace)
		{
			case ASMODIAN:
				asmodianBrokerItems.put(newBrokerItem.getItemUniqueId(), newBrokerItem);
				break;

			case ELYOS:
				elyosBrokerItems.put(newBrokerItem.getItemUniqueId(), newBrokerItem);
				break;
		}

		BrokerOpSaveTask bost = new BrokerOpSaveTask(newBrokerItem, itemToRegister, player.getInventory()
			.getKinahItem(), player.getObjectId());
		saveManager.add(bost);

		BrokerItem[] brokerItem = new BrokerItem[1];
		brokerItem[0] = newBrokerItem;
		PacketSendUtility.sendPacket(player, new SM_BROKER_ITEMS(brokerItem, registeredItems.size() + 1, 3));
		
	}

	/**
	 * 
	 * @param player
	 */
	public void showRegisteredItems(Player player)
	{
		Map<Integer, BrokerItem> brokerItems = getRaceBrokerItems(player.getCommonData().getRace());

		List<BrokerItem> registeredItems = new ArrayList<BrokerItem>();
		int playerId = player.getObjectId();

		for(BrokerItem item : brokerItems.values())
		{
			if(item != null && item.getItem() != null && item.isSold() != true && item.isSettled() != true && playerId == item.getSellerId())
				registeredItems.add(item);
		}
		
		PacketSendUtility.sendPacket(player, new SM_BROKER_ITEMS(registeredItems.toArray(new BrokerItem[registeredItems.size()]), 1));
	}

	/**
	 * 
	 * @param player
	 * @param brokerItemId
	 */
	public void cancelRegisteredItem(Player player, int brokerItemId)
	{
		if(player.getInventory().isFull())
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(901298));
			return;
		}
		int playerId = player.getObjectId();
		Map<Integer, BrokerItem> brokerItems = getRaceBrokerItems(player.getCommonData().getRace());
		BrokerItem brokerItem = brokerItems.get(brokerItemId);
		
		if(brokerItem == null)
			return;
        if (playerId != brokerItem.getSellerId()){
    	  	Logger.getLogger(this.getClass()).info("[AUDIT]Player: "+player.getName()+" is trying to steal: " +brokerItem.getItemId()+ " => Hacking!");
    	  	return;
        }
		synchronized(brokerItem)
		{
			if(brokerItem != null)
			{
				Item item = player.getInventory().putToBag(brokerItem.getItem());
				PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(item, true));
				PacketSendUtility.sendPacket(player, new SM_BROKER_ITEMS(brokerItemId, 4));
				brokerItem.setPersistentState(PersistentState.DELETED);
				saveManager.add(new BrokerOpSaveTask(brokerItem));
				brokerItems.remove(brokerItemId);
			}
			showRegisteredItems(player);
		}
		
	}

	/**
	 * 
	 * @param player
	 */
	public void showSettledItems(Player player)
	{
		Map<Integer, BrokerItem> brokerSettledItems = getRaceBrokerSettledItems(player.getCommonData().getRace());

		List<BrokerItem> settledItems = new ArrayList<BrokerItem>();

		int playerId = player.getObjectId();
		long totalKinah = 0;

		for(BrokerItem item : brokerSettledItems.values())
		{
			if(item != null && playerId == item.getSellerId())
			{
				settledItems.add(item);

				if(item.isSold())
					totalKinah += item.getPrice();
			}
		}

		PacketSendUtility.sendPacket(player, new SM_BROKER_ITEMS(settledItems.toArray(new BrokerItem[settledItems.size()]), totalKinah, 0, 5));
	}

	/**
	 * 
	 * @param player
	 */
	public void settleAccount(Player player)
	{
		Race playerRace = player.getCommonData().getRace();
		Map<Integer, BrokerItem> brokerSettledItems = getRaceBrokerSettledItems(playerRace);
		List<BrokerItem> collectedItems = new ArrayList<BrokerItem>();
		int playerId = player.getObjectId();
		long kinahCollect = 0;
		boolean itemsLeft = false;

		for(BrokerItem item : brokerSettledItems.values())
		{
			if(item.getSellerId() == playerId)
				collectedItems.add(item);
		}

		for(BrokerItem item : collectedItems)
		{
			if(item.isSold())
			{
				boolean result = false;
				switch(playerRace)
				{
					case ASMODIANS:
						result = asmodianSettledItems.remove(item.getItemUniqueId()) != null;
						break;
					case ELYOS:
						result = elyosSettledItems.remove(item.getItemUniqueId()) != null;
						break;
				}

				if(result)
				{
					item.setPersistentState(PersistentState.DELETED);
					saveManager.add(new BrokerOpSaveTask(item));
					kinahCollect += item.getPrice();
				}
			}
			else
			{
				if(item.getItem() != null)
				{
					Item resultItem = player.getInventory().putToBag(item.getItem());
					if(resultItem != null)
					{
						boolean result = false;
						switch(playerRace)
						{
							case ASMODIANS:
								result = asmodianSettledItems.remove(item.getItemUniqueId()) != null;
								break;
							case ELYOS:
								result = elyosSettledItems.remove(item.getItemUniqueId()) != null;
								break;
						}

						if(result)
						{
							item.setPersistentState(PersistentState.DELETED);
							saveManager.add(new BrokerOpSaveTask(item));
							PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(Collections
								.singletonList(resultItem)));
						}
					}
					else
						itemsLeft = true;

				}
				else
					log.warn("Broker settled item missed. ObjID: " + item.getItemUniqueId());
			}
		}

		player.getInventory().increaseKinah(kinahCollect);

		showSettledItems(player);

		if(!itemsLeft)
			PacketSendUtility.sendPacket(player, new SM_BROKER_ITEMS(6));
	}

	private void checkExpiredItems()
	{
		Map<Integer, BrokerItem> asmoBrokerItems = getRaceBrokerItems(Race.ASMODIANS);
		Map<Integer, BrokerItem> elyosBrokerItems = getRaceBrokerItems(Race.ELYOS);

		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());

		for(BrokerItem item : asmoBrokerItems.values())
		{
			if(item != null && item.getExpireTime().getTime() <= currentTime.getTime())
			{
				expireItem(Race.ASMODIANS, item);
				asmodianBrokerItems.remove(item.getItemUniqueId());
			}
		}

		for(BrokerItem item : elyosBrokerItems.values())
		{
			if(item != null && item.getExpireTime().getTime() <= currentTime.getTime())
			{
				expireItem(Race.ELYOS, item);
				this.elyosBrokerItems.remove(item.getItemUniqueId());
			}
		}
	}
	
	private void expireItem(Race race, BrokerItem item)
	{
		if(MailService.getInstance().sendSystemMail("VENDOR_RETURN_MAIL", "", "", item.getSellerId(), item.getItem(), 0))
		{
			item.setPersistentState(PersistentState.DELETED);
			saveManager.add(new BrokerOpSaveTask(item));
		}
		else
		{ // Don't let the item get lost when mailing fails.
			putToSettled(race, item, false);
		}
	}

	/**
	 * 
	 * @param player
	 */
	public void onPlayerLogin(Player player)
	{
		Map<Integer, BrokerItem> brokerSettledItems = getRaceBrokerSettledItems(player.getCommonData().getRace());

		int playerId = player.getObjectId();

		List<BrokerItem> settledItems = new ArrayList<BrokerItem>();

		long totalKinah = 0;

		for(BrokerItem item : brokerSettledItems.values())
		{
			if(item != null && playerId == item.getSellerId())
			{
				settledItems.add(item);

				if(item.isSold())
					totalKinah += item.getPrice();
			}
		}

		PacketSendUtility.sendPacket(player, new SM_BROKER_ITEMS(null, totalKinah, 1, 5));
		if(totalKinah > 0)
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400131));
	}
	
	/**
	 * 
	 * @param player
	 * @return
	 */
	private BrokerPlayerCache getPlayerCache(Player player)
	{
		BrokerPlayerCache cacheEntry = playerBrokerCache.get(player.getObjectId());
		if(cacheEntry == null)
		{
			cacheEntry = new BrokerPlayerCache();
			playerBrokerCache.put(player.getObjectId(), cacheEntry);
		}
		return cacheEntry;
	}

	public void removePlayerCache(Player player)
	{
		playerBrokerCache.remove(player.getObjectId());
	}
	/**
	 * 
	 * @param player
	 * @return
	 */
	private int getPlayerMask(Player player)
	{
		return getPlayerCache(player).getBrokerMaskCache();
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	private BrokerItem[] getFilteredItems(Player player)
	{
		return getPlayerCache(player).getBrokerListCache();
	}

	/**
	 * Frequent running save task
	 */
	public static final class BrokerPeriodicTaskManager extends AbstractFIFOPeriodicTaskManager<BrokerOpSaveTask>
	{
		private static final String	CALLED_METHOD_NAME	= "brokerOperation()";

		/**
		 * @param period
		 */
		public BrokerPeriodicTaskManager(int period)
		{
			super(period);
		}

		@Override
		protected void callTask(BrokerOpSaveTask task)
		{
			task.run();
		}

		@Override
		protected String getCalledMethodName()
		{
			return CALLED_METHOD_NAME;
		}
	}

	/**
	 * This class is used for storing all items in one shot after any broker operation
	 */
	public static final class BrokerOpSaveTask implements Runnable
	{
		private BrokerItem	brokerItem;
		private Item		item;
		private Item		kinahItem;
		private int			playerId;

		/**
		 * 
		 * @param brokerItem
		 * @param item
		 * @param kinahItem
		 * @param playerId
		 */
		private BrokerOpSaveTask(BrokerItem brokerItem, Item item, Item kinahItem, int playerId)
		{
			this.brokerItem = brokerItem;
			this.item = item;
			this.kinahItem = kinahItem;
			this.playerId = playerId;
		}

		/**
		 * @param brokerItem
		 */
		public BrokerOpSaveTask(BrokerItem brokerItem)
		{
			this.brokerItem = brokerItem;
		}

		@Override
		public void run()
		{
			if(brokerItem != null)
				DAOManager.getDAO(BrokerDAO.class).store(brokerItem);
			if(item != null)
				DAOManager.getDAO(InventoryDAO.class).store(item, playerId);
			if(kinahItem != null)
				DAOManager.getDAO(InventoryDAO.class).store(kinahItem, playerId);
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final BrokerService instance = new BrokerService();
	}
}
