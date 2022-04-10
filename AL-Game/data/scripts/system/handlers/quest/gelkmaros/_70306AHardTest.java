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
package quest.gelkmaros;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator by Mariella
 */
public class _70306AHardTest extends QuestHandler {

	private final static int questId = 70306;
	private final static int[] mobs = { 655342, 655343, 655344, 837022, 837023, 837024, 836863 };

	public _70306AHardTest() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(799323).addOnTalkEvent(questId); // Mastarius
		qe.registerQuestNpc(799290).addOnTalkEvent(questId); // Ricaldo
		qe.registerQuestNpc(730260).addOnTalkEvent(questId); // Silentera Eastgate
		qe.registerQuestNpc(806909).addOnTalkEvent(questId); // Centurion Vergelan
		qe.registerQuestNpc(820081).addOnTalkEvent(questId); // Centurion Lania
		qe.registerQuestNpc(806910).addOnTalkEvent(questId); // Exhausted Vergelan

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 70200, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 799323: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1352);
						}
						case SETPRO2: {
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 799290: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1693);
						}
						case SETPRO3: {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 730260: {
					switch (dialog) {
						case SETPRO5: {
							qs.setQuestVar(5);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 806909: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2716);
						}
						case SETPRO6: {
							qs.setQuestVar(6);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 820081: {
					switch (dialog) {
						// ToDo: check correct action for this npc
						case USE_OBJECT: {
							qs.setQuestVar(11);
							updateQuestStatus(env);
							return false;
						}
						default: 
							break;
					}
					break;
				}
				case 806910: {
					switch (dialog) {
						case SET_SUCCEED: {
							qs.setQuestVar(12);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				default:
					break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799323) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}
	/*
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);

			// (0) Step: 3, Count: 5, Mobs : 655342, 655343, 655344
			// (1) Step: 8, Count: 9, Mobs : 837022, 837023, 837024
			// (2) Step: 10, Count: 1, Mobs : 836863

			switch (var) {
				case 3: {
				// ???
				}
				case 8: {
				// ???
				}
				case 10: {
					qs.setQuestVar(11);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
				default:
					break;
			}
			return false;
		}
		return false;
	}
	*/
}
