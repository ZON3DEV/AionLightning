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

package com.aionemu.gameserver.network.aion.iteminfo;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.IdianStone;
import com.aionemu.gameserver.model.items.ItemStone;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import java.nio.ByteBuffer;
import java.util.Set;
import org.slf4j.LoggerFactory;

public class ManaStoneInfoBlobEntry extends ItemBlobEntry {

  ManaStoneInfoBlobEntry() {
    super(ItemInfoBlob.ItemBlobType.MANA_SOCKETS);
  }

  @Override
public void writeThisBlob(ByteBuffer buf) {
    Item item = this.ownerItem;

			writeC(buf, item.isSoulBound() ? 1 : 0);
			writeC(buf, item.getEnchantLevel());
			writeD(buf, item.getItemSkinTemplate().getTemplateId());
			writeC(buf, item.getOptionalSocket());
			writeC(buf, 0);
			writeC(buf, 0);
			
			writeItemStones(buf);
        ItemStone god = item.getGodStone();
			writeD(buf, god != null ? god.getItemId() : 0);

        int itemColor = item.getItemColor();
        int dyeExpiration = item.getColorTimeLeft();
        if (((dyeExpiration > 0) && (item.getColorExpireTime() > 0)) || ((dyeExpiration == 0) && (item.getColorExpireTime() == 0) && (item.getItemTemplate().isItemDyePermitted()))) {

            writeC(buf, itemColor != 0 ? 1 : 0);
            writeD(buf, itemColor);
            writeD(buf, 0);
            writeD(buf, dyeExpiration);
        } else {
            writeC(buf, 0);
            writeD(buf, 0);
            writeD(buf, 0);
            writeD(buf, 0);
        }

        IdianStone idianStone = item.getIdianStone();
        if ((idianStone != null) && (idianStone.getPolishNumber() > 0)) {
            writeD(buf, idianStone.getItemId());
            writeC(buf, idianStone.getPolishNumber());
        } else {
            writeD(buf, 0);
            writeC(buf, 0);
        }
            writeC(buf, item.getAuthorize());
            writeD(buf, 0);
        if (item.getItemTemplate().getArmorType() == ArmorType.FEATHER) {
            writeD(buf, 42);
            writeD(buf, item.getAuthorize() * 150);
            writeQ(buf, 0L);
        if (item.getItemTemplate().getAwakenId() == 52) {
            writeD(buf, 30);
            writeD(buf, item.getAuthorize() * 4);
        } else {
            writeD(buf, 35);
            writeD(buf, item.getAuthorize() * 20);
            }
            writeQ(buf, 0L);
            writeB(buf, new byte[16]);
        } else {
            writeB(buf, new byte[48]);
            }
        }

  private void writeItemStones(ByteBuffer buf) {
    Item item = this.ownerItem;
    if (item.hasManaStones()) {
      Set<ManaStone> manaStone = item.getItemStones();
      int count = item.getItemTemplate().getSpecialSlots();
      int temp = count;
      for (ManaStone stone : manaStone) {
        if (stone.isSpecial()) {
          writeD(buf, stone.getItemId());
          temp--;
        }
      }
      if (temp > 0) {
        writeB(buf, new byte[temp * 4]);
      }
      for (ManaStone stone : manaStone) {
        if (!stone.isSpecial()) {
          if (count > item.getSockets(false)) {
           LoggerFactory.getLogger(ManaStoneInfoBlobEntry.class);
          } else {
            writeD(buf, stone.getItemId());
            count++;
          }
        }
      }
      if (count < 12) {
        writeB(buf, new byte[(12 - count) * 4]);
      }
    } else {
      writeB(buf, new byte[48]);
    }
  }

  @Override
public int getSize() {
    return 132;
  }
}
