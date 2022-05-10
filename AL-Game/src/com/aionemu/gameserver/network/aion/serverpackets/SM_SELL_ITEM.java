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

import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate.TradeTab;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author orz, Sarynth, modified by Artur
 */
public class SM_SELL_ITEM extends AionServerPacket {

	private int targetObjectId;
	private TradeListTemplate plist;
	private int sellPercentage;

	public SM_SELL_ITEM(int targetObjectId, TradeListTemplate plist, int sellPercentage) {

		this.targetObjectId = targetObjectId;
		this.plist = plist;
		this.sellPercentage = sellPercentage;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		if ((plist != null) && (plist.getNpcId() != 0) && (plist.getCount() != 0)) {
			writeD(targetObjectId);
			writeC(plist.getTradeNpcType().index());
			writeD(sellPercentage);// Buy Price * (sellPercentage / 100) = Display price.
			writeH(256);
			writeH(plist.getCount());
			for (TradeTab tradeTabl : plist.getTradeTablist()) {
				writeD(tradeTabl.getId());
			}
		} else {
			writeD(targetObjectId);
			writeD(5121); // Buy Price * (sellPercentage / 100) = Display price.
			writeD(65792);
			writeC(0);
		}
	}
}
