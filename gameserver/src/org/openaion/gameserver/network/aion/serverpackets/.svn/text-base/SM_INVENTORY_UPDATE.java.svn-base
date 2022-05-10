/*
 * This file is part of aion-unique <aionunique.com>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.items.ItemId;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.InventoryPacket;


/**
 * 
 * @author ATracer
 * modified by kosyachok
 */
public class SM_INVENTORY_UPDATE extends InventoryPacket
{
	private List<Item> items;
	private int size;
	private int mode;

	public SM_INVENTORY_UPDATE(List<Item> items)
	{
		this.items = items;
		this.size = items.size();
		this.mode = 25;
	}
	
	public SM_INVENTORY_UPDATE(Item item, boolean isNew)
	{
		this.items = new ArrayList<Item>();
		this.items.add(item);
		this.size = 1;
		this.mode = isNew ? 25 : 17;
	}

	/**
	 * {@inheritDoc} dc
	 */

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{	
		writeH(buf, mode); // Drop to storage animation (bit mask). 1 - min animation; 17 - Like "Drop" to inv without message in chat; 25 - full anim with surrounding "new" border and message in chat 
		writeH(buf, size); // number of entries
		for(Item item : items)
		{
			writeGeneralInfo(buf, item);

			ItemTemplate itemTemplate = item.getItemTemplate();

			if(itemTemplate.getTemplateId() == ItemId.KINAH.value())
			{
				writeKinah(buf, item);
				writeC(buf, 0);
			}
			else if (itemTemplate.isWeapon())
			{
				writeWeaponInfo(buf, item);
				writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
				writeC(buf, 0);
			}
			else if (itemTemplate.isArmor())
			{
				writeArmorInfo(buf, item);
				writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
				writeC(buf, 0);
			}
			else if (itemTemplate.isStigma())
			{
				writeStigmaInfo(buf,item);
			}
			else
			{
				writeGeneralItemInfo(buf, item);
				writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
				writeC(buf, 0);
			}
		}
	}	
}