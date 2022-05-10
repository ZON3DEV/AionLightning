/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */


package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Avol modified by ATracer
 */
public class SM_UPDATE_PLAYER_APPEARANCE extends AionServerPacket {

    public int playerId;
    public int size;
    public List<Item> items;

    public SM_UPDATE_PLAYER_APPEARANCE(int playerId, List<Item> items) {
        this.playerId = playerId;
        this.items = items;
        this.size = items.size();
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(playerId);

		int mask = 0;
		for (Item item : items) {
			if (item.getItemTemplate().isTwoHandWeapon()) {
				ItemSlot[] slots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
				mask = ((int)(mask | slots[0].getSlotIdMask()));
			} else {
				mask = ((int)(mask | item.getEquipmentSlot()));
			  }
		  }

        writeD(mask);

        for (Item item : items) {
            writeD(item.getItemSkinTemplate().getTemplateId());
            GodStone godStone = item.getGodStone();
            writeD(godStone != null ? godStone.getItemId() : 0);
            writeD(item.getItemColor());
	        if(item.getAuthorize() > 0) {
               if(item.getItemTemplate().getArmorType() == ArmorType.FEATHER) {
                    float authorize = item.getAuthorize() / 5;
                    if(item.getAuthorize() >= 5) {
                        authorize = authorize > 2.0F ? 2.0F : authorize;
                        writeD((int)authorize << 3);
                   }
                   else {
                        writeD(0);
                   }
                }
                else {
                    writeD(item.getAuthorize() >= 5 ? 2 : 0);
              }
           }
           else {
                writeD(item.getEnchantLevel() >= 15 ? 1 : 0);
           }
       }
   }
}