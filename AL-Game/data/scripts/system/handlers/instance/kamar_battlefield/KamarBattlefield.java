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

package instance.kamar_battlefield;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ranastic, Rinzler, Eloann
 *         http://aionisuber.blogspot.fr/2013/08/kamar-
 *         battlefield-walkthrough-and.html
 */
@InstanceID(301120000)
public class KamarBattlefield extends KamarBattlefieldInstance {

	private List<Integer> movies = new ArrayList<Integer>();

	@Override
	public void onEnterInstance(Player player) {
		if (isInstanceStarted.compareAndSet(false, true)) {
			startInstanceTask();
		}
		super.onEnterInstance(player);
		switch (player.getRace()) {
			case ELYOS:
				break;
			case ASMODIANS:
				break;
			default:
				break;
		}
	}

	@Override
	public void onDie(Npc npc) {
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
		if (mostPlayerDamage == null) {
			return;
		}
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 000000: // find it
				updateScore(mostPlayerDamage, npc, 200, false);
				break;
			case 232853: // General Varga
				if (!kamarBattlefieldReward.isRewarded()) {
					updateScore(mostPlayerDamage, npc, 3050, false);
					Race winningRace = kamarBattlefieldReward.getWinningRaceByScore();
					stopInstance(winningRace);
				}
				break;
		}
		super.onDie(npc);
	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21403);
		effectController.removeEffect(21404);
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
	}

	@Override
	protected void openFirstDoors() {
		openDoor(4);
		openDoor(8);
	}

	@SuppressWarnings("unused")
	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
}
