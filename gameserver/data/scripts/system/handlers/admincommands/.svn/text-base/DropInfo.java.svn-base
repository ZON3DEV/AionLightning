/*
 * This file is part of Aion-Extreme <aion-core.net>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import java.util.Set;

import org.openaion.gameserver.model.drop.DropItem;
import org.openaion.gameserver.model.drop.DropList;
import org.openaion.gameserver.model.drop.DropTemplate;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.services.DropService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Oliver
 */
public class DropInfo extends AdminCommand {

	private DropList dropList;

	public DropInfo() {
		super("dropinfo");
	}

	@Override
	public void executeCommand(Player player, String[] params) {

		VisibleObject visibleObject = player.getTarget();

		if (visibleObject == null) {
			PacketSendUtility.sendMessage(player, "You should select target npc first.");
			return;
		}

		if (visibleObject instanceof Npc) {
			dropList = DropService.getInstance().getDropList();

			Set<DropTemplate> templates = dropList.getDropsFor(((Npc) visibleObject).getNpcId());

			if (templates != null) {
				PacketSendUtility.sendMessage(player, "[Drop Info about npc]\n");
				for (DropTemplate dropTemplate : templates) {
					DropItem dropItem = new DropItem(dropTemplate);
					PacketSendUtility.sendMessage(player, "[item:" + dropItem.getDropTemplate().getItemId() + "]" + "	Rate: "
						+ dropItem.getDropTemplate().getChance());
				}
			}
		}

	}

}
