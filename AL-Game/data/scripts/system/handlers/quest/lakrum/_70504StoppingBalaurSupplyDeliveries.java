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
package quest.lakrum;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author QuestGenerator by Mariella
 */
public class _70504StoppingBalaurSupplyDeliveries extends QuestHandler {

	private final static int questId = 70504;
	private final static int[] mobs = { 655458, 655459, 655460, 703558 };

	public _70504StoppingBalaurSupplyDeliveries() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(836564).addOnTalkEvent(questId); // Nolte
		qe.registerQuestNpc(703559).addOnTalkEvent(questId); // Balaur Scheme
		qe.registerQuestItem(182216290, questId);			 // Agent's Support Items

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 70506, false);
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
			int var = qs.getQuestVarById(0);

			switch (targetId) {
				case 836564: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1352);
							} else {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO2: {
						//  giveQuestItem(env, 182216290, 1);
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							if (QuestService.collectItemCheck(env,true)) {
							//  removeQuestItem(env, 182216290, 1);
								qs.setQuestVar(8);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							} else {
								return sendQuestDialog(env, 10001);
							}
						}
						default: 
							break;
					}
					break;
				}
				case 703559: {
					switch (dialog) {
						// ToDo: check correct action for this npc
						case USE_OBJECT: {
							qs.setQuestVar(7);
							updateQuestStatus(env);
							return false;
						}
						default: 
							break;
					}
					break;
				}
				default:
					break;
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

			// (0) Step: 3, Count: 3, Mobs : 655458
			// (1) Step: 3, Count: 1, Mobs : 655459
			// (2) Step: 5, Count: 3, Mobs : 655460
			// (3) Step: 5, Count: 1, Mobs : 703558

			switch (var) {
				case 3: {
				// ???
				}
				case 3: {
					return defaultOnKillEvent(env, 655459, 3, 4, 0);
				}
				case 5: {
				// ???
				}
				case 5: {
					qs.setQuestVar(6);
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
