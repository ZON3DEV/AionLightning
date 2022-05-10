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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.Set;

import org.openaion.gameserver.model.gameobjects.BrokerItem;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.SimpleModifier;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.StatModifier;
import org.openaion.gameserver.model.items.ItemStone;
import org.openaion.gameserver.model.items.ManaStone;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author kosyachok, LokiReborn
 *
 */
public class SM_BROKER_ITEMS extends AionServerPacket
{
	private BrokerItem[] brokerItems;
	private Item buyItem;
	private int itemsCount;
	private int startPage;
	private int brokerFunction;
	private int id;
	private long totalKinah;
		
	public SM_BROKER_ITEMS(BrokerItem[] brokerItems, int itemsCount, int startPage, int brokerFunction)
	{
		this.brokerItems = brokerItems;
		this.itemsCount = itemsCount;
		this.startPage = startPage;
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(BrokerItem[] brokerItems, int brokerFunction)
	{
		this.brokerItems = brokerItems;
		this.brokerFunction = brokerFunction;
	}
	
	public SM_BROKER_ITEMS(BrokerItem[] brokerItems, int id, int brokerFunction)
	{
		this.brokerItems = brokerItems;
		this.id = id;
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(int id, int brokerFunction)
	{
		this.id = id;
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(int brokerFunction)
	{
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(Item buyItem, int brokerFunction)
	{
		this.buyItem = buyItem;
		this.brokerFunction = brokerFunction;
	}

	public SM_BROKER_ITEMS(BrokerItem[] brokerItems, long totalKinah, int id, int brokerFunction)
	{
		this.brokerItems = brokerItems;
		this.totalKinah = totalKinah;
		this.id = id;
		this.brokerFunction = brokerFunction;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{	
		writeC(buf, brokerFunction);
		switch(brokerFunction)
		{
			case 0://responds to CM_BROKER_LIST
				writeD(buf, itemsCount);
				writeC(buf, 0);
				writeH(buf, startPage);
				writeH(buf, brokerItems.length);

				for(BrokerItem item : brokerItems)
				{
					if(item.getItem().getItemTemplate().isArmor() || item.getItem().getItemTemplate().isWeapon())
						writeArmorWeaponInfo(buf, item);
					else
						writeCommonInfo(buf, item);
				}
				break;
				
			case 1://responds to CM_BROKER_REGISTERED
				writeD(buf, 0x00);
				writeH(buf, brokerItems.length);
			case 3://responds to CM_REGISTER_BROKER_ITEM
				for(BrokerItem item : brokerItems)
				{
					if(brokerFunction == 3)
					{
						writeC(buf, 0x00);
						writeC(buf, id);
					}
					writeD(buf, item.getItem().getObjectId());
					writeD(buf, item.getItem().getItemTemplate().getTemplateId());
					writeQ(buf, item.getPrice());
					writeQ(buf, item.getItem().getItemCount());
					writeQ(buf, item.getItem().getItemCount());
					writeH(buf, brokerFunction == 3 ? 0x08 : item.daysLeft()); //days left in vendor
					writeC(buf, item.getItem().getEnchantLevel());
					writeD(buf, item.getItem().getItemSkinTemplate().getTemplateId());
					writeC(buf, item.getItem().hasOptionalSocket() ? item.getItem().getOptionalSocket() : 0);
					
					writeItemStones(buf, item.getItem());
					
					ItemStone god = item.getItem().getGodStone();
					writeD(buf, god == null ? 0 : god.getItemId());
					
					writeC(buf, 0x00); //unk
					writeD(buf, 0x00); //unk
					writeD(buf, 0x00); //unk
					writeS(buf, item.getItem().getCrafterName()); //creator
				}
				break;
				
			case 2://responds to CM_BUY_BROKER_ITEM
				writeC(buf, 0x00);
				writeD(buf, buyItem.getObjectId());
				writeQ(buf, buyItem.getItemCount());				
				break;
				
			case 4://responds to CM_BROKER_CANCEL_REGISTERED
				writeC(buf, 0x00);
				writeD(buf, id);
				break;
				
			case 5://responds to CM_BROKER_SETTLE_LIST
				writeQ(buf, totalKinah); //priceTotal
				writeH(buf, (brokerItems == null || brokerItems.length < 0) ? 0x00 : brokerItems.length);//probably wrong (see bellow)
				writeD(buf, 0x00);//probably has something to do with pages, and also probably missing a packet -.-
				writeC(buf, id);//0 if requested by tab | 1 at login | 2 if item purchased while logged in
				writeH(buf, (brokerItems == null || brokerItems.length < 0) ? 0x00 : brokerItems.length);
				if(brokerItems != null && brokerItems.length > 0)
				{
					for(BrokerItem item : brokerItems)
					{
						writeD(buf, item.getItemId());
						writeQ(buf, item.getPrice());
						writeQ(buf, item.getItemCount());
						writeQ(buf, item.getItemCount());
						writeD(buf, item.getItemUniqueId());//item.getItem().getObjectId());
						writeC(buf, 0x00);
						writeC(buf, 0x00);//item.getItem().getEnchantLevel());
						writeD(buf, item.getItemId());//item.getItem().getItemSkinTemplate().getTemplateId());
						writeC(buf, 0x00);//item.getItem().hasOptionalSocket() ? item.getItem().getOptionalSocket() : 0);
						
						writeB(buf, new byte[24]);//writeItemStones(buf, item.getItem());
						
						//ItemStone god = item.getItem().getGodStone();
						writeD(buf, 0x00);//god == null ? 0 : god.getItemId());
						
						writeD(buf, 0); //unk
						writeD(buf, 0); //unk
						writeC(buf, 0x00); //unk
						writeS(buf, ""); //creator (No need in settled)
					}
				}
				break;
				
			case 6://responds to CM_BROKER_SETTLE_ACCOUNT
				writeC(buf, 0x00);
				break;
				
			default:
				return;
		}
	}
	
	private void writeArmorWeaponInfo(ByteBuffer buf, BrokerItem item)
	{
		writeD(buf, item.getItem().getObjectId());
		writeD(buf, item.getItem().getItemTemplate().getTemplateId());
		writeQ(buf, item.getPrice());
		writeQ(buf, item.getItem().getItemCount());
		writeC(buf, 0);
		writeC(buf, item.getItem().getEnchantLevel());
		writeD(buf, item.getItem().getItemSkinTemplate().getTemplateId());
		writeC(buf, item.getItem().hasOptionalSocket() ? item.getItem().getOptionalSocket() : 0);
		
		writeItemStones(buf, item.getItem());
		
		ItemStone god = item.getItem().getGodStone();
		writeD(buf, god == null ? 0 : god.getItemId());
		
		writeC(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeS(buf, item.getSeller());
		writeS(buf, item.getItem().getCrafterName()); //creator
		
	}
	
	private void writeItemStones(ByteBuffer buf, Item item)
	{
		int count = 0;
		
		if(item.hasManaStones())
		{
			Set<ManaStone> itemStones = item.getItemStones();
			
			for(ManaStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeC(buf, modifier.getStat().getItemStoneMask());
				}
			}
			writeB(buf, new byte[(6-count)*2]);
			count = 0;
			for(ManaStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeH(buf, ((SimpleModifier)modifier).getValue());
				}
			}
			writeB(buf, new byte[(6-count)*2]);
		}
		else
		{
			writeB(buf, new byte[24]);
		}

		//for now max 6 stones - write some junk
	}
	
	private void writeCommonInfo(ByteBuffer buf, BrokerItem item)
	{
		writeD(buf, item.getItem().getObjectId());
		writeD(buf, item.getItem().getItemTemplate().getTemplateId());
		writeQ(buf, item.getPrice());
		writeQ(buf, item.getItem().getItemCount());
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeH(buf, 0);
		writeS(buf, item.getSeller());
		writeS(buf, item.getItem().getCrafterName()); //creator
	}
}
