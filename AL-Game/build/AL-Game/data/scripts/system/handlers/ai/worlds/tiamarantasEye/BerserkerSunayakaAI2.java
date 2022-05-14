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

package ai.worlds.tiamarantasEye;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Luzien TODO: fight AI
 */
@AIName("berserker_sunayaka")
public class BerserkerSunayakaAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);

	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			if (getOwner().getNpcId() == 219311) {
				SkillEngine.getInstance().getSkill(getOwner(), 20651, 1, getOwner()).useNoAnimationSkill(); // ragetask
			}
		}
	}

	@Override
	public void handleBackHome() {
		super.handleBackHome();
		isHome.set(true);
		if (getOwner().getNpcId() == 219311) {
			getEffectController().removeEffect(20651);
			getEffectController().removeEffect(8763);
		}
		// SkillEngine.getInstance().getSkill(getOwner(), 20652, 1,
		// getOwner()).useNoAnimationSkill();
	}
}
