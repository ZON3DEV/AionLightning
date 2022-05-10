/*
 * This file is part of aion-unique <aion-unique.com>.
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
package org.openaion.gameserver.network.aion;

import java.nio.ByteBuffer;
import java.util.Set;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.SimpleModifier;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.StatModifier;
import org.openaion.gameserver.model.items.FusionStone;
import org.openaion.gameserver.model.items.ItemSlot;
import org.openaion.gameserver.model.items.ItemStone;
import org.openaion.gameserver.model.items.ManaStone;
import org.openaion.gameserver.model.templates.item.ItemTemplate;


/**
 * @author ATracer
 * modified by kosyachok, Steve [JS]Folio
 */
public abstract class InventoryPacket extends AionServerPacket
{
	/**
	 *  The header of every item block
	 * @param buf
	 * @param item
	 */
	protected void writeGeneralInfo(ByteBuffer buf, Item item)
	{
		writeD(buf, item.getObjectId());
		ItemTemplate itemTemplate = item.getItemTemplate();
		writeD(buf, itemTemplate.getTemplateId());
		writeH(buf, 0x24);
		writeD(buf, itemTemplate.getNameId());
		writeH(buf, 0);
	}
	
	protected void writeMailGeneralInfo(ByteBuffer buf, Item item)
	{
		writeD(buf, item.getObjectId());
		ItemTemplate itemTemplate = item.getItemTemplate();
		writeD(buf, itemTemplate.getTemplateId());
		writeD(buf, 1);
		writeD(buf, 0);
		writeH(buf, 0x24);
		writeD(buf, itemTemplate.getNameId());
		writeH(buf, 0);
	}

	/**
	 *  All misc items
	 * @param buf
	 * @param item
	 */
	protected void writeGeneralItemInfo(ByteBuffer buf, Item item)
	{
		short byteCount = 32;
		
		byteCount += item.getCrafterName().length() * 2;
		
		writeH(buf, byteCount); //length of details 26 + crafter string
		writeC(buf, 0);
		writeH(buf, item.getItemMask());
		writeQ(buf, item.getItemCount());
		writeS(buf, item.getCrafterName());
		writeC(buf, 0);
		writeQ(buf, item.getTempItemTimeLeft());	//no display in mail and pStore
		writeD(buf, item.getTempTradeTimeLeft());
		writeD(buf, 0);
		writeH(buf, 0);
	}
	
	protected void writeStigmaInfo(ByteBuffer buf, Item item)
	{
		writeH(buf, 341); //length of details 55 01
		writeC(buf, 0x6);
                writeD(buf, item.isEquipped() ? item.getEquipmentSlot() : 0) ;
		writeC(buf, 0x7);
		writeH(buf, item.getItemTemplate().getStigma().getSkillid()); // SkillId
		writeD(buf, 0);
		writeH(buf, 0);
		writeD(buf, item.getItemTemplate().getStigma().getShard());  // Shard
                
                writeB(buf, new byte[160]);
                
		writeD(buf, 1); // unk 1
                
                writeB(buf, new byte[82]);

		writeH(buf, 0x0b); // unk 0B 00
		writeC(buf, 0);
		writeD(buf, item.getItemTemplate().getTemplateId());
                writeB(buf, new byte[39]);
	
		writeD(buf, 66110); //3E 02 01 00
                
                writeB(buf, new byte[27]);
                
                writeH(buf, item.getEquipmentSlot()); // Item slot
                writeC(buf, 0);
	}

	/**
	 * 
	 * @param buf
	 * @param item
	 */
	protected void writeKinah(ByteBuffer buf, Item item)
	{
		writeH(buf, 0x20); //length of details
		writeC(buf, 0);
		writeH(buf, item.getItemMask());
		writeQ(buf, item.getItemCount());
		writeD(buf, 0);
		writeD(buf, 0);
		writeH(buf, 0);
		writeC(buf, 0);
		writeQ(buf, 0);
		writeH(buf, 0);
		writeH(buf, 255); // FF FF equipment
	}
	
	/**
	 *  For all weapon. Weapon is identified by weapon_type in xml
	 *  
	 * @param buf
	 * @param item
	 */
	protected void writeWeaponInfo(ByteBuffer buf, Item item)
	{
		int itemSlotId = item.getEquipmentSlot();
		int placeHolder, sizeLoc, size;
		
		writeH(buf, 0x63);
		
		sizeLoc = buf.position();
		
		if (item.getItemTemplate().getWeaponType().getRequiredSlots() == 2)
		{
			writeC(buf, 0x0E);
			writeD(buf, item.hasFusionedItem() ? item.getFusionedItem() : 0);
			writeFusionStones(buf,item);
			writeC(buf, item.hasOptionalFusionSocket() ? item.getOptionalFusionSocket() : 0x00);
		}
		
		writeC(buf, 0x06);
		
		writeD(buf, item.isEquipped() ? itemSlotId : 0x00);

		writeC(buf, 0x01);
		
		writeD(buf, ItemSlot.getSlotsFor(item.getItemTemplate().getItemSlot()).get(0).getSlotIdMask());
		writeD(buf, item.hasFusionedItem() ? 0x00 : 0x02);

		writeC(buf, 0x0B); //? some details separator 
		
		writeC(buf, item.isSoulBound() ? 1 : 0);
		writeC(buf, item.getEnchantLevel());//enchant (1-15)
		writeD(buf, item.getItemSkinTemplate().getTemplateId());
		writeC(buf, item.hasOptionalSocket() ? item.getOptionalSocket() : 0x00);

		writeItemStones(buf, item);
			
		ItemStone god = item.getGodStone();
		writeD(buf, god == null ? 0 : god.getItemId());			
		writeD(buf, 0);
		writeD(buf, 0);//unk 1.5.1.9
		/*
		writeC(buf, 0);
		writeC(buf, 0x0A);
		writeH(buf, 0x19);
		writeD(buf, 0x07);
		*/	
			/*
			 * This is where item bonuses should be inserted.
			 * The format is as follows:
			 * writeH(buf, 2560); 0x000A
			 * writeH(buf, bonusType); //ex. 0x12 is +HP
			 * writeD(buf, bonusAmount); //ex. 0xC4 is 196
			 */
			
		writeH(buf, 0x00);//seperator between item bonus and item mask
		writeH(buf, item.getItemMask());
		writeQ(buf, item.getItemCount());
		writeS(buf, item.getCrafterName());
		writeC(buf, 0);
		writeQ(buf, item.getTempItemTimeLeft());// //no display in mail and pStore
		writeD(buf, item.getTempTradeTimeLeft());
		writeD(buf, 0);//unk 2.5
		writeH(buf, 0);//unk 2.5
		
		size = buf.position() - sizeLoc;
		placeHolder = buf.position();
		buf.position(sizeLoc - 2);
		
		writeH(buf, size);
		
		buf.position(placeHolder);
	}
	
	protected void writeWeaponSwitch(ByteBuffer buf, Item item)
	{
		writeH(buf, 0x05);
		writeC(buf, 0x06);
		writeD(buf, item.isEquipped() ? item.getEquipmentSlot() : 0x00);
	}

	/**
	 *  Writes manastones : 6C - statenum mask, 6H - value
	 * @param buf
	 * @param item
	 */
	private void writeItemStones(ByteBuffer buf, Item item)
	{
		int count = 0;
		//TODO implement new area to store the values for the manastones
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
					writeH(buf, modifier.getStat().getItemStoneMask());
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

	private void writeFusionStones(ByteBuffer buf, Item item)
	{
		int count = 0;
		
		if(item.hasFusionStones())
		{
			Set<FusionStone> itemStones = item.getFusionStones();
			
			for(FusionStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeH(buf, modifier.getStat().getItemStoneMask());
					writeH(buf, ((SimpleModifier)modifier).getValue());
				}
			}
			writeB(buf, new byte[(6-count)*4]);
		}
		else
		{
			writeB(buf, new byte[24]);
		}

		//for now max 6 stones - write some junk
	}

	/**
	 *  For all armor. Armor is identified by armor_type in xml
	 * @param buf
	 * @param item
	 */
	protected void writeArmorInfo(ByteBuffer buf, Item item)
	{
		int itemSlotId = item.getEquipmentSlot();
		
		short byteCount = 103;
		
		byteCount += item.getCrafterName().length() * 2;

		writeH(buf, byteCount); //83 + crafter string
		writeC(buf, 0x06);
		writeD(buf, item.isEquipped() ? itemSlotId : 0);
		writeC(buf, 0x02);
		writeD(buf,	ItemSlot.getSlotsFor(item.getItemTemplate().getItemSlot()).get(0).getSlotIdMask());
		writeD(buf, 0);
		writeD(buf, 0);
		writeC(buf, 0x0B); //? some details separator
		writeC(buf, item.isSoulBound() ? 1 : 0);
		writeC(buf, item.getEnchantLevel()); //enchant (1-15)
		writeD(buf, item.getItemSkinTemplate().getTemplateId());

		writeC(buf, item.hasOptionalSocket() ? item.getOptionalSocket() : 0x00);

		writeItemStones(buf, item);

		writeC(buf, 0);
		writeD(buf, item.getItemColor());
		writeD(buf, 0);

		writeD(buf, 0);//unk 1.5.1.9
		writeC(buf, 0x0A);
		writeH(buf, 0x12);
		writeC(buf, 0x2B);
		writeD(buf, 0x00);
		writeC(buf, 0);//unk 1.5.1.9

		writeH(buf, item.getItemMask());
		writeQ(buf, item.getItemCount());
		writeS(buf, item.getCrafterName());// Crafter
		writeC(buf, 0);
		writeQ(buf, item.getTempItemTimeLeft()); //no display in mail and pStore
		writeD(buf, item.getTempTradeTimeLeft());
		writeD(buf, 0);
		writeH(buf, 0);
	}
}
