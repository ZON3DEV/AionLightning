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

package ai.instance.rentusBase;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author xTz
 */
@AIName("fallen_reian")
public class FallenReianAI2 extends NpcAI2 {

	private AtomicBoolean isCollapsed = new AtomicBoolean(false);
	private int doorId;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		doorId = getNpcId() == 799661 ? 16 : 54;
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			if (MathUtil.getDistance(getOwner(), player) <= doorId) {
				if (MathUtil.getDistance(getOwner(), getPosition().getWorldMapInstance().getDoors().get(doorId)) <= 30) {
					if (isCollapsed.compareAndSet(false, true)) {
						getPosition().getWorldMapInstance().getDoors().get(doorId).setOpen(true);
					}
				}
			}
		}
	}
}
