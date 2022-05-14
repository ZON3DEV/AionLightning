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

package quest.event_quests;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * Author Rinzler
 */
public class _80512AHigherCalling26thEdition extends QuestHandler {

	private final static int questId = 80512;

	public _80512AHigherCalling26thEdition() {
		super(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(831031).addOnQuestStart(questId); // Nebrith.
		qe.registerQuestNpc(831031).addOnTalkEvent(questId); // Nebrith.
		qe.registerQuestNpc(831031).addOnTalkEvent(questId); // Nebrith.
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 831031) { // Nebrith.
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE:
						return sendQuestStartDialog(env);
					default:
						break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 831031: { // Nebrith.
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_QUEST_REWARD: {
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
						case AIRLINE_SERVICE:
							break;
						case AP_SELL:
							break;
						case ASK_QUEST_ACCEPT:
							break;
						case BUY:
							break;
						case BUY_AGAIN:
							break;
						case BUY_BY_AP:
							break;
						case CHANGE_ITEM_SKIN:
							break;
						case CHARGE_ITEM_MULTI:
							break;
						case CHARGE_ITEM_MULTI2:
							break;
						case CHARGE_ITEM_SINGLE:
							break;
						case CHARGE_ITEM_SINGLE2:
							break;
						case CHECK_USER_HAS_QUEST_ITEM:
							break;
						case CHECK_USER_HAS_QUEST_ITEM_SIMPLE:
							break;
						case CLOSE_LEGION_WAREHOUSE:
							break;
						case COMBINE_SKILL_LEVELUP:
							break;
						case COMPOUND_WEAPON:
							break;
						case CRAFT:
							break;
						case CREATE_LEGION:
							break;
						case DECOMPOUND_WEAPON:
							break;
						case DEPOSIT_ACCOUNT_WAREHOUSE:
							break;
						case DEPOSIT_CHAR_WAREHOUSE:
							break;
						case DIC:
							break;
						case DISPERSE_LEGION:
							break;
						case EDIT_CHARACTER:
							break;
						case EDIT_GENDER:
							break;
						case EDIT_GENDER_CONFIRM:
							break;
						case ENTER_PVP:
							break;
						case EXCHANGE_COIN:
							break;
						case EXTEND_ACCOUNT_WAREHOUSE:
							break;
						case EXTEND_CHAR_WAREHOUSE:
							break;
						case EXTEND_INVENTORY:
							break;
						case FACTION_JOIN:
							break;
						case FACTION_SEPARATE:
							break;
						case FINISH_DIALOG:
							break;
						case FUNC_PET_H_ABANDON:
							break;
						case FUNC_PET_H_ADOPT:
							break;
						case GATHER_SKILL_LEVELUP:
							break;
						case GIVEUP_CRAFT_EXPERT:
							break;
						case GIVEUP_CRAFT_MASTER:
							break;
						case GIVE_ITEM_PROC:
							break;
						case HOUSING_BUILD:
							break;
						case HOUSING_CANCEL_GIVEUP:
							break;
						case HOUSING_CHANGE_BUILDING:
							break;
						case HOUSING_CONFIG:
							break;
						case HOUSING_CREATE_PERSONAL_INS:
							break;
						case HOUSING_DESTRUCT:
							break;
						case HOUSING_FRIENDLIST:
							break;
						case HOUSING_GIVEUP:
							break;
						case HOUSING_GUESTBOOK:
							break;
						case HOUSING_KICK:
							break;
						case HOUSING_LIKE:
							break;
						case HOUSING_PAY_RENT:
							break;
						case HOUSING_PERSONAL_AUCTION:
							break;
						case HOUSING_PERSONAL_INS_TELEPORT:
							break;
						case HOUSING_RANDOM_TELEPORT:
							break;
						case HOUSING_RECREATE_PERSONAL_INS:
							break;
						case HOUSING_SCRIPT:
							break;
						case INSTANCE_ENTRY:
							break;
						case INSTANCE_PARTY_MATCH:
							break;
						case LEAVE_PVP:
							break;
						case LEGION_CHANGE_EMBLEM:
							break;
						case LEGION_CREATE_EMBLEM:
							break;
						case LEGION_LEVELUP:
							break;
						case MAKE_MERCENARY:
							break;
						case MATCH_MAKER:
							break;
						case NULL:
							break;
						case OPEN_INSTANCE_RECRUIT:
							break;
						case OPEN_LEGION_WAREHOUSE:
							break;
						case OPEN_PERSONAL_WAREHOUSE:
							break;
						case OPEN_POSTBOX:
							break;
						case OPEN_QUEST_WINDOW:
							break;
						case OPEN_STIGMA_WINDOW:
							break;
						case OPEN_VENDOR:
							break;
						case PASS_DOORMAN:
							break;
						case PET_ABANDON:
							break;
						case PET_ADOPT:
							break;
						case QUEST_ACCEPT:
							break;
						case QUEST_ACCEPT_1:
							break;
						case QUEST_ACCEPT_SIMPLE:
							break;
						case QUEST_REFUSE:
							break;
						case QUEST_REFUSE_1:
							break;
						case QUEST_REFUSE_2:
							break;
						case QUEST_REFUSE_SIMPLE:
							break;
						case RECOVERY:
							break;
						case RECREATE_LEGION:
							break;
						case REMOVE_MANASTONE:
							break;
						case RESURRECT_BIND:
							break;
						case RESURRECT_PET:
							break;
						case RETRIEVE_ACCOUNT_WAREHOUSE:
							break;
						case RETRIEVE_CHAR_WAREHOUSE:
							break;
						case SELECTED_QUEST_NOREWARD:
							break;
						case SELECTED_QUEST_REWARD1:
							break;
						case SELECTED_QUEST_REWARD10:
							break;
						case SELECTED_QUEST_REWARD11:
							break;
						case SELECTED_QUEST_REWARD12:
							break;
						case SELECTED_QUEST_REWARD13:
							break;
						case SELECTED_QUEST_REWARD14:
							break;
						case SELECTED_QUEST_REWARD15:
							break;
						case SELECTED_QUEST_REWARD2:
							break;
						case SELECTED_QUEST_REWARD3:
							break;
						case SELECTED_QUEST_REWARD4:
							break;
						case SELECTED_QUEST_REWARD5:
							break;
						case SELECTED_QUEST_REWARD6:
							break;
						case SELECTED_QUEST_REWARD7:
							break;
						case SELECTED_QUEST_REWARD8:
							break;
						case SELECTED_QUEST_REWARD9:
							break;
						case SELECT_ACTION_1011:
							break;
						case SELECT_ACTION_1012:
							break;
						case SELECT_ACTION_1013:
							break;
						case SELECT_ACTION_1014:
							break;
						case SELECT_ACTION_1015:
							break;
						case SELECT_ACTION_1016:
							break;
						case SELECT_ACTION_1017:
							break;
						case SELECT_ACTION_1018:
							break;
						case SELECT_ACTION_1019:
							break;
						case SELECT_ACTION_1097:
							break;
						case SELECT_ACTION_1182:
							break;
						case SELECT_ACTION_1267:
							break;
						case SELECT_ACTION_1352:
							break;
						case SELECT_ACTION_1353:
							break;
						case SELECT_ACTION_1354:
							break;
						case SELECT_ACTION_1438:
							break;
						case SELECT_ACTION_1609:
							break;
						case SELECT_ACTION_1693:
							break;
						case SELECT_ACTION_1694:
							break;
						case SELECT_ACTION_1695:
							break;
						case SELECT_ACTION_1779:
							break;
						case SELECT_ACTION_2034:
							break;
						case SELECT_ACTION_2035:
							break;
						case SELECT_ACTION_2036:
							break;
						case SELECT_ACTION_2037:
							break;
						case SELECT_ACTION_2376:
							break;
						case SELECT_ACTION_2377:
							break;
						case SELECT_ACTION_2546:
							break;
						case SELECT_ACTION_2717:
							break;
						case SELECT_ACTION_2718:
							break;
						case SELECT_ACTION_2720:
							break;
						case SELECT_ACTION_3058:
							break;
						case SELECT_ACTION_3143:
							break;
						case SELECT_ACTION_3399:
							break;
						case SELECT_ACTION_3400:
							break;
						case SELECT_ACTION_3570:
							break;
						case SELECT_ACTION_3740:
							break;
						case SELECT_ACTION_3911:
							break;
						case SELECT_ACTION_4081:
							break;
						case SELECT_ACTION_4763:
							break;
						case SELL:
							break;
						case SETPRO1:
							break;
						case SETPRO10:
							break;
						case SETPRO11:
							break;
						case SETPRO12:
							break;
						case SETPRO13:
							break;
						case SETPRO14:
							break;
						case SETPRO15:
							break;
						case SETPRO16:
							break;
						case SETPRO17:
							break;
						case SETPRO18:
							break;
						case SETPRO19:
							break;
						case SETPRO2:
							break;
						case SETPRO20:
							break;
						case SETPRO21:
							break;
						case SETPRO22:
							break;
						case SETPRO23:
							break;
						case SETPRO24:
							break;
						case SETPRO25:
							break;
						case SETPRO26:
							break;
						case SETPRO27:
							break;
						case SETPRO28:
							break;
						case SETPRO29:
							break;
						case SETPRO3:
							break;
						case SETPRO30:
							break;
						case SETPRO31:
							break;
						case SETPRO32:
							break;
						case SETPRO33:
							break;
						case SETPRO34:
							break;
						case SETPRO35:
							break;
						case SETPRO36:
							break;
						case SETPRO37:
							break;
						case SETPRO38:
							break;
						case SETPRO39:
							break;
						case SETPRO4:
							break;
						case SETPRO40:
							break;
						case SETPRO41:
							break;
						case SETPRO5:
							break;
						case SETPRO6:
							break;
						case SETPRO7:
							break;
						case SETPRO8:
							break;
						case SETPRO9:
							break;
						case SET_SUCCEED:
							break;
						case SHOW_MOVIE:
							break;
						case TELEPORT_SIMPLE:
							break;
						case TOWN_CHALLENGE:
							break;
						case TRADE_IN:
							break;
						case TRADE_SELL_LIST:
							break;
						case USE_OBJECT:
							break;
						default:
							break;
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 831031) { // Nebrith.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
