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

package ai.worlds.idian_depth;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;

/**
 * @author Eloann
 */
@AIName("blackened_grave")
public class BlackenedGraveAI2 extends NpcAI2 {

	@Override
	protected void handleDied() {
		spawn(284262, 394.15338f, 893.5626f, 559.375f, (byte) 0);
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	@Override
	public int modifyDamage(int damage) {
		return super.modifyDamage(1);
	}
}
