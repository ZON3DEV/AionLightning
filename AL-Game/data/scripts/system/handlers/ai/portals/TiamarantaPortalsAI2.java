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

package ai.portals;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.SourceLocation;
import com.aionemu.gameserver.services.SiegeService;

/**
 * @author Cheatkiller
 *
 */
@AIName("tiamarantaportal")
public class TiamarantaPortalsAI2 extends PortalAI2 {

	private boolean checkSourceCount(Player player) {
		int count = 0;
		for (final SourceLocation source : SiegeService.getInstance().getSources().values()) {
			if (source.getRace().getRaceId() == player.getRace().getRaceId()) {
				count++;
			}
		}
		if (count >= 2) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		if (checkSourceCount(player)) {
			super.handleUseItemFinish(player);
		}
	}
}
