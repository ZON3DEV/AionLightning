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
package ai.worlds.enshar;

import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

@AIName("grimreoff")
// 804839
public class GrimreoffAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		if (player.getInventory().getFirstItemByItemId(185000233) != null) { // Arden Territory Village Infiltration Rift Corridor Key
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
		}
	}

	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000 && player.getInventory().decreaseByItemId(185000233, 1)) { // Arden Territory Village Infiltration Rift Corridor Key
			switch (getNpcId()) {
				case 804839: // Grimreoff
					announceDarkLegionPortal();
					spawn(702721, 1818.7255f, 2550.4365f, 300.012f, (byte) 71);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							despawnNpc(702721);
						}
					}, 300000); // 5 Minutes
					break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}

	private void announceDarkLegionPortal() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DARK_SIDE_LEGION_DIRECT_PORTAL_OPEN);
			}
		});
	}

	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc : npcs) {
				npc.getController().onDelete();
			}
		}
	}
}
