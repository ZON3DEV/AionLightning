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

import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.model.siege.SourceLocation;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.SiegeService;
import java.util.Map;

public class SM_FORTRESS_STATUS extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		Map<Integer, FortressLocation> fortresses = SiegeService.getInstance().getFortresses();
		Map<Integer, SourceLocation> sources = SiegeService.getInstance().getSources();
		Influence inf = Influence.getInstance();

		writeC(1);
		writeD(SiegeService.getInstance().getSecondsBeforeHourEnd());
		writeF(inf.getGlobalElyosInfluence());
		writeF(inf.getGlobalAsmodiansInfluence());
		writeF(inf.getGlobalBalaursInfluence());
		writeH(4);
		writeD(210050000);
		writeF(inf.getInggisonElyosInfluence());
		writeF(inf.getInggisonAsmodiansInfluence());
		writeF(inf.getInggisonBalaursInfluence());
		writeD(220070000);
		writeF(inf.getGelkmarosElyosInfluence());
		writeF(inf.getGelkmarosAsmodiansInfluence());
		writeF(inf.getGelkmarosBalaursInfluence());
		writeD(400010000);
		writeF(inf.getAbyssElyosInfluence());
		writeF(inf.getAbyssAsmodiansInfluence());
		writeF(inf.getAbyssBalaursInfluence());
		writeD(600030000);
		writeF(inf.getTiamarantaElyosInfluence());
		writeF(inf.getTiamarantaAsmodiansInfluence());
		writeF(inf.getTiamarantaBalaursInfluence());
		writeH(fortresses.size() + sources.size());

		for (FortressLocation fortress : fortresses.values()) {
			writeD(fortress.getLocationId());
			writeC(fortress.getNextState());
		}

		for (SourceLocation source : sources.values()) {
			writeD(source.getLocationId());
			writeC(source.getNextState());
		}
	}
}
