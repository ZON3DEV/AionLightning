/**
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package org.openaion.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.TradeListTemplate;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.trade.TradeItem;
import org.openaion.gameserver.model.trade.TradeList;
import org.openaion.gameserver.model.trade.TradeListType;
import org.openaion.gameserver.model.trade.TradeRepurchaseList;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.services.PrivateStoreService;
import org.openaion.gameserver.services.PurchaseLimitService;
import org.openaion.gameserver.services.RepurchaseService;
import org.openaion.gameserver.services.TradeService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;


/**
 * 
 * @author orz modified by ATracer modified by ginho1
 */
public class CM_BUY_ITEM extends AionClientPacket
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(CM_BUY_ITEM.class);

	private int					sellerObjId;
	private int					unk1;
	private int					amount;
	private int					itemId;
	private int					count;
	@SuppressWarnings("unused")
	private int					unk2;
	private Player				player;
	private TradeList			tradeList;
	private TradeRepurchaseList	repurchaseList;

	public CM_BUY_ITEM(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		sellerObjId = readD();
		
		if(sellerObjId == 0)
			return;
		
		unk1 = readH();
		amount = readH();
		player = getConnection().getActivePlayer();

		tradeList = new TradeList();
		tradeList.setSellerObjId(sellerObjId);

		repurchaseList = new TradeRepurchaseList();
		tradeList.setSellerObjId(sellerObjId);

		for(int i = 0; i < amount; i++)
		{
			itemId = readD();
			count = readD();
			unk2 = readD();

			// prevent exploit packets
			if(count < 1 || itemId == 0)
				continue;

			if(unk1 == 13 || unk1 == 14 || unk1 == 15)
			{
				tradeList.addBuyItem(itemId, count);
			}
			else if(unk1 == 0 || unk1 == 1)
			{
				tradeList.addSellItem(itemId, count);
			}
			else if(unk1 == 2)
			{
				if(GSConfig.ENABLE_REPURCHASE)
					repurchaseList.addBuyItemRepurchase(itemId, player);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionObject obj = World.getInstance().findAionObject(sellerObjId);
		Player targetPlayer = null;

		try
		{
			player.setTrading(true);
			switch(unk1)
			{
				case 0:
					targetPlayer = (Player) obj;
					targetPlayer.setTrading(true);
					PrivateStoreService.sellStoreItem(targetPlayer, player, tradeList);
					break;
				case 1:
					TradeService.performSellToShop(player, tradeList);
					break;
				case 2:
					if(GSConfig.ENABLE_REPURCHASE)
						RepurchaseService.performBuyFromRepurchase(player, repurchaseList);
					break;
				case 13:
					if(PurchaseLimitService.getInstance().addCache(obj, player, tradeList))
					{
						Npc npc = (Npc) World.getInstance().findAionObject(sellerObjId);
						TradeListTemplate tlist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc.getNpcId());
						if(tlist.getType() == TradeListType.KINAH)
							TradeService.performBuyFromShop(player, tradeList);
						else
							log.info("[CHEAT]Player: "+player.getName()+" abusing CM_BUY_ITEM!");
					}
					else
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400353));
					break;
				case 14:
					Npc npc = (Npc) World.getInstance().findAionObject(sellerObjId);
					TradeListTemplate tlist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc.getNpcId());
					if(tlist.getType() == TradeListType.ABYSS)
						TradeService.performBuyFromAbyssShop(player, tradeList);
					break;
				case 15:
					Npc npc2 = (Npc) World.getInstance().findAionObject(sellerObjId);
					TradeListTemplate elist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc2.getNpcId());
					if(elist.getType() == TradeListType.EXTRA)
						TradeService.performBuyWithExtraCurrency(player, tradeList);
					break;
				default:
					log.info(String.format("Unhandle shop action unk1: %d", unk1));
					break;
			}
		}
		finally
		{
			if(targetPlayer != null)
				targetPlayer.setTrading(false);
			player.setTrading(false);
		}

		VisibleObject visibleObject = null;
		if(obj instanceof VisibleObject)
			visibleObject = (VisibleObject) obj;

		for(TradeItem item : tradeList.getTradeItems())
		{
			ItemTemplate template = item.getItemTemplate();
			if(template != null && player != null)
				QuestEngine.getInstance().onItemSellBuyEvent(
					new QuestCookie(visibleObject, player, template.getItemQuestId(), 0), template.getTemplateId());
		}

	}
}
