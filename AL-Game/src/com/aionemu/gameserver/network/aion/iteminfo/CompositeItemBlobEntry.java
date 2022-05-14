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

import java.nio.ByteBuffer;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ManaStone;

import org.slf4j.LoggerFactory;

public class CompositeItemBlobEntry extends ItemBlobEntry {

	CompositeItemBlobEntry() {
		super(ItemInfoBlob.ItemBlobType.COMPOSITE_ITEM);
	}

	public void writeThisBlob(ByteBuffer buf) {
		Item item = this.ownerItem;

		writeD(buf, item.getFusionedItemId());
		writeFusionStones(buf);
		writeC(buf, item.hasOptionalFusionSocket() ? item.getOptionalFusionSocket() : 0);
		writeC(buf, 2);
		writeC(buf, 0);
	}

	private void writeFusionStones(ByteBuffer buf) {
		Item item = this.ownerItem;
		if (item.hasFusionStones()) {
			Set<ManaStone> manaStone = item.getFusionStones();
			int count = item.getFusionedItemTemplate().getSpecialSlots();
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
					if (count > item.getSockets(true)) {
						LoggerFactory.getLogger(CompositeItemBlobEntry.class);
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
			skip(buf, 48);
		}
	}

	public int getSize() {
		return 55;
	}
}
