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

package ai.instance.kamarBattlefield;

import ai.ActionItemNpcAI2;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

/**
 * @author Eloann
 */
@AIName("kamarsiegeweapon")
public class KamarSiegeWeaponAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		super.handleDialogStart(player);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
			case 701898: // Kamar Tank Elyos.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701899: // Kamar Tank Asmodians.
				SkillEngine.getInstance().getSkill(player, 21404, 1, player).useNoAnimationSkill();
				break;
			case 701909: // IDKamar_LightSiegeWeapon_B1_65_Ae.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701910: // IDKamar_DarkSiegeWeapon_B2_65_Ae.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701911: // IDKamar_DarkSiegeWeapon_B1_65_Ae.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701912: // IDKamar_LightSiegeWeapon_B2_65_Ae.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701925: // IDKamar_IdgelMachine_On_Light_25.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701926: // IDKamar_IdgelMachine_On_Dark_25.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701927: // IDKamar_IdgelMachine_On_Light_160.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701928: // IDKamar_IdgelMachine_On_Dark_160.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701929: // IDKamar_IdgelMachine_On_Light_205.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701930: // IDKamar_IdgelMachine_On_Dark_205.
				SkillEngine.getInstance().getSkill(player, 21403, 1, player).useNoAnimationSkill();
				break;
		}
		AI2Actions.scheduleRespawn(this);
		AI2Actions.deleteOwner(this);
	}
}
