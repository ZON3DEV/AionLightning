package org.openaion.gameserver.model.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 *
 * @author ginho1
 */
public class TradeRepurchaseList
{
	private int sellerObjId;
	private List<Item> tradeItems = new ArrayList<Item>();
	private long requiredKinah;
	private int currencyId;
	private int requiredAp;
	private Map<Integer, Integer> requiredItems  = new HashMap<Integer, Integer>();

	public void addBuyItemRepurchase(int itemObjectId, Player player)
	{
		Item item = player.getRepurchase().getItem(itemObjectId);

		if(item != null)
		{
			player.getRepurchase().removeItem(itemObjectId);
			tradeItems.add(item);
		}
	}

	/**
	 * @return price TradeList sum price
	 */
	public boolean calculateBuyListPrice(Player player)
	{
		long availableKinah = player.getInventory().getKinahItem().getItemCount();
		long priceCheck = 0;
		requiredKinah = 0;

		for(Item tradeItem : tradeItems)
		{
			priceCheck = player.getPrices().getKinahForBuy(tradeItem.getItemTemplate().getPrice(), player.getCommonData().getRace());
			if(priceCheck <= 0) // Avoid giving out free items by tax reduction (ie arrows)
				priceCheck = 1;
			requiredKinah +=  priceCheck * tradeItem.getItemCount();
		}

		return availableKinah >= requiredKinah;
	}

	/**
	 * @return true or false
	 */
	public boolean calculateAbyssBuyListPrice(Player player)
	{
		int ap = player.getAbyssRank().getAp();

		this.requiredAp = 0;
		this.requiredItems.clear();

		for(Item tradeItem : tradeItems)
		{
			requiredAp += tradeItem.getItemTemplate().getAbyssPoints() * tradeItem.getItemCount();
			int itemId = tradeItem.getItemTemplate().getAbyssItem();

			Integer alreadyAddedCount = requiredItems.get(itemId);
			if(alreadyAddedCount == null)
				requiredItems.put(itemId, tradeItem.getItemTemplate().getAbyssItemCount());
			else
				requiredItems.put(itemId, alreadyAddedCount + tradeItem.getItemTemplate().getAbyssItemCount());
		}

		if(ap < requiredAp || requiredAp < 0) //Abyss shop exploit fix by Asanka
			return false;

		for(Integer itemId : requiredItems.keySet())
		{
			long count = player.getInventory().getItemCountByItemId(itemId);
			if(count < requiredItems.get(itemId))
				return false;
		}

		return true;
	}

	/**
	 * @return true or false
	 */
	public boolean calculateExtraCurrencyBuyListPrice(Player player)
	{
		this.requiredItems.clear();
		this.currencyId = 0;

		for(Item tradeItem : tradeItems)
		{
			if (currencyId == 0)
				currencyId = tradeItem.getItemTemplate().getExtraCurrencyItem();
			else if (tradeItem.getItemTemplate().getExtraCurrencyItem() != currencyId)
				continue; // currency mismatch

			Integer alreadyAddedCount = requiredItems.get(currencyId);
			if(alreadyAddedCount == null)
				requiredItems.put(currencyId, tradeItem.getItemTemplate().getExtraCurrencyItemCount());
			else
				requiredItems.put(currencyId, alreadyAddedCount + tradeItem.getItemTemplate().getExtraCurrencyItemCount());
		}

		for(Integer itemId : requiredItems.keySet())
		{
			long count = player.getInventory().getItemCountByItemId(itemId);
			if(count < requiredItems.get(itemId))
				return false;
		}

		return true;
	}

	/**
	 * @return the tradeItems
	 */
	public List<Item> getTradeItems()
	{
		return tradeItems;
	}

	public int size()
	{
		return tradeItems.size();
	}

	/**
	 * @return the npcId
	 */
	public int getSellerObjId()
	{
		return sellerObjId;
	}

	/**
	 * @param sellerObjId the npcId to set
	 */
	public void setSellerObjId(int npcObjId)
	{
		this.sellerObjId = npcObjId;
	}

	/**
	 * @return the requiredAp
	 */
	public int getRequiredAp()
	{
		return requiredAp;
	}

	/**
	 * @return the requiredKinah
	 */
	public long getRequiredKinah()
	{
		return requiredKinah;
	}

	/**
	 * @return the currencyId
	 */
	public int getCurrencyId()
	{
		return currencyId;
	}

	/**
	 * @return the requiredItems
	 */
	public Map<Integer, Integer> getRequiredItems()
	{
		return requiredItems;
	}
}
