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
package ai.events;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI2;

/**
 * @author Falke_34
 */
@AIName("ice_sculptures")
public class IceSculptureAI2 extends GeneralNpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		switch (getNpcId()) {
			case 837649: // Kromede's Minion Ice Statue
			case 837650: // Grendal's Minion Ice Statue
			case 837651: // Weda's Minion Ice Statue
			case 837652: // Kromede's Minion Ice Statue
			case 837653: // Grendal's Minion Ice Statue
			case 837654: { // Weda's Minion Ice Statue
				super.handleDialogStart(player);
				break;
			}
			default: {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
				break;
			}
		}
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		if (QuestEngine.getInstance().onDialog(env) && dialogId != DialogAction.SETPRO1.id()) {
			return true;
		}
		if (dialogId == DialogAction.SETPRO1.id()) {
			int skillId = 0;
			int RemoveSkillId1 = 0;
			int RemoveSkillId2 = 0;
			switch (getNpcId()) {
				case 837650: // Grendal's Minion Ice Statue
				case 837653: {
					RemoveSkillId1 = 5334;
					RemoveSkillId2 = 5335;
					skillId = 5333;
					break;
				}
				case 837649: // Kromede's Minion Ice Statue
				case 837652: {
					RemoveSkillId1 = 5333;
					RemoveSkillId2 = 5335;
					skillId = 5334;
					break;
				}
				case 837651: // Weda's Minion Ice Statue
				case 837654: {
					RemoveSkillId1 = 5333;
					RemoveSkillId2 = 5334;
					skillId = 5335;
					break;
				}
			}
			// only one buff at the same time
			player.getEffectController().removeEffect(RemoveSkillId1);
			player.getEffectController().removeEffect(RemoveSkillId2);
			SkillEngine.getInstance().getSkill(getOwner(), skillId, 1, player).useWithoutPropSkill();
		}
		else if (dialogId == DialogAction.QUEST_SELECT.id() && questId != 0) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
		}
		return true;
	}
}
