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

package ai.worlds.sarpan;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;

/**
 * @author Cheatkiller
 *
 */
@AIName("giantastriclox")
public class GiantAstricloxAI2 extends NpcAI2 {

	@Override
	public int modifyDamage(int damage) {
		return 1;
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		spawn();
	}

	private void spawn() {
		spawn(730495, 796.7448f, 867.9318f, 675.22473f, (byte) 36);
		spawn(730495, 794.9168f, 869.0062f, 675.06616f, (byte) 34);
		spawn(730495, 796.2312f, 871.0012f, 674.43726f, (byte) 25);
		spawn(730495, 799.8763f, 869.46265f, 674.75934f, (byte) 44);
		spawn(730495, 802.3064f, 867.8118f, 675.19116f, (byte) 46);
		spawn(730495, 798.8771f, 870.45953f, 674.51013f, (byte) 39);
	}
}
