package org.openaion.gameserver.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.TradeListData;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.model.templates.item.ItemQuality;
import org.openaion.gameserver.model.trade.TradeRepurchaseList;
import org.openaion.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author ginho1
 *
 */
public class RepurchaseService
{
	@SuppressWarnings("unused")
	private static final Logger	log	= Logger.getLogger(TradeService.class);

	private static final TradeListData		tradeListData = DataManager.TRADE_LIST_DATA;

	/**
	 *
	 * @param Player
	 * @param TradeRepurchaseList
	 * @return true or false
	 */
	public static boolean performBuyFromRepurchase(Player player, TradeRepurchaseList tradeList)
	{
		Storage inventory = player.getInventory();
		Item kinahItem = inventory.getKinahItem();

		// 1. check kinah
		if(!tradeList.calculateBuyListPrice(player))
			return false;

		// 2. check free slots, need to check retail behaviour
		int freeSlots = inventory.getLimit() - inventory.getAllItems().size() + 1;
		if(freeSlots < tradeList.size())
			return false; // TODO message

		long tradeListPrice = player.getPrices().getKinahForSell(tradeList.getRequiredKinah(), player.getCommonData().getRace());

		List<Item> addedItems = new ArrayList<Item>();
		
		if(!inventory.decreaseKinah(tradeListPrice))
			return false;
		
		for(Item tradeItem : tradeList.getTradeItems())
		{
			if(tradeItem.getItemTemplate().getItemQuality() != ItemQuality.JUNK)
				ItemService.addRepurchaseItem(player, tradeItem);
		}		

		PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(kinahItem));
		PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(addedItems));
		// TODO message
		return true;
	}

	/**
	 * @return the tradeListData
	 */
	public static TradeListData getTradeListData()
	{
		return tradeListData;
	}

}
