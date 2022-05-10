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

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Luzien note: closing door 860 -> collapse room TODO: sniff fight AI
 */
@AIName("debarim")
public class DebarimTheOmnipotentAI2 extends AggressiveNpcAI2 {

	private boolean isStart;

	@Override
	public void handleCreatureAggro(Creature creature) {
		super.handleCreatureAggro(creature);
		if (creature instanceof Player && !isStart) {
			isStart = true;
			getPosition().getWorldMapInstance().getDoors().get(480).setOpen(true); // bugged
																					// door,
																					// displayed
																					// state
																					// is
																					// the
																					// opposite
		}
	}

	@Override
	public void handleDied() {
		super.handleDied();
		getPosition().getWorldMapInstance().getDoors().get(480).setOpen(false);
	}

	@Override
	public void handleBackHome() {
		super.handleBackHome();
		isStart = false;
		getPosition().getWorldMapInstance().getDoors().get(480).setOpen(false);
	}
}
