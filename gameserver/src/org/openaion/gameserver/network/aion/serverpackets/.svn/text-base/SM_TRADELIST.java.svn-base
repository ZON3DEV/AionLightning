package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.PurchaseLimit;
import org.openaion.gameserver.model.templates.TradeListTemplate;
import org.openaion.gameserver.model.templates.TradeListTemplate.TradeTab;
import org.openaion.gameserver.model.templates.goods.GoodsList;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.PurchaseLimitService;


/**
 * 
 * @author alexa026
 * modified by ATracer, Sarynth, ginho1
 */
public class SM_TRADELIST extends AionServerPacket
{
	private int targetObjectId;
	private int npcTemplateId;
	private TradeListTemplate tlist;
	private int buyPriceModifier;
	private int finalPriceModifier;
		
	public SM_TRADELIST(Npc npc, TradeListTemplate tlist, int buyPriceModifier)
	{
		if(tlist == null)
			return;
		this.targetObjectId = npc.getObjectId();
		this.npcTemplateId = npc.getNpcId();
		this.tlist = tlist;
		this.buyPriceModifier = buyPriceModifier;
		finalPriceModifier = Math.round((buyPriceModifier + (tlist.getBuyRate() * buyPriceModifier)) * tlist.getSellRate());
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if ((tlist != null)&&(tlist.getNpcId()!=0)&&(tlist.getCount()!=0))
		{
			writeD(buf, targetObjectId);
			switch (tlist.getType()) {
				case ABYSS:
					writeC(buf, 2);
					break;
				case COUPON:
					writeC(buf, 3);
					break;
				case EXTRA:
					writeC(buf, 4);
					break;
				default:
					writeC(buf, 1);
			}
			
			writeD(buf, finalPriceModifier); // Vendor Buy Price Modifier
			writeH(buf, tlist.getCount());

			List<GoodsList> list = new ArrayList<GoodsList>();
			boolean isLimited = false;
			int countItems = 0;

			for(TradeTab tradeTabl : tlist.getTradeTablist())
			{
				writeD(buf, tradeTabl.getId());

				if(GSConfig.ENABLE_PURCHASE_LIMIT)
				{
					GoodsList goodsListAdd = DataManager.GOODSLIST_DATA.getGoodsListById(tradeTabl.getId());

					if(goodsListAdd.isLimited())
					{
						isLimited = true;
						countItems += goodsListAdd.getItemsList().size();

						list.add(goodsListAdd);
					}
				}
			}

			if(isLimited)
			{
				PurchaseLimit purchaseLimit = con.getActivePlayer().getPurchaseLimit();

				writeH(buf, countItems);

				for(GoodsList goodsList : list)
				{
					if(goodsList != null && goodsList.getItemsList() != null && purchaseLimit != null)
					{
						for(GoodsList.Item item : goodsList.getItemsList())
						{
							writeD(buf, item.getId());
							writeH(buf, purchaseLimit.getItemLimitCount(item.getId()));
							writeH(buf, (item.getSelllimit() - PurchaseLimitService.getInstance().getCountItem(item.getId())));
						}
					}
				}
			}
		}
		else if(tlist == null)
		{
			Logger.getLogger(SM_TRADELIST.class).warn("Empty TradeListTemplate for NpcId: " + npcTemplateId);
			writeD(buf, targetObjectId);
			writeC(buf, 1);
			writeD(buf, buyPriceModifier);
			writeH(buf, 0);
		}
	}
}
