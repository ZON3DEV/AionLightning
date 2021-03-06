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

/**
 * @author QuestGenerator by Mariella
 */
public class _60502TheVanishedResearcherAtis extends QuestHandler {

	private final static int questId = 60502;
	private final static int[] mobs = { 703550, 655438, 703551 };

	public _60502TheVanishedResearcherAtis() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(836544).addOnTalkEvent(questId); // Bitonos
		qe.registerQuestNpc(836545).addOnTalkEvent(questId); // Injured Atis
		qe.registerQuestNpc(703550).addOnTalkEvent(questId); // Asmodian Trap
		qe.registerQuestItem(182216281, questId);			 // Emergency Medicine

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 60506, false);
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
				case 836544: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SETPRO1: {
						//  giveQuestItem(env, 182216281, 1);
							qs.setQuestVar(1);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 836545: {
					switch (dialog) {
						case QUEST_SELECT: {
							switch (var) {
								case 1: {
									return sendQuestDialog(env, 1352);		
								}
								case 3: {
									return sendQuestDialog(env, 2034);		
								}
								default:
									break;
							}
						}
						case SETPRO2: {
						//  giveQuestItem(env, 182216281, 1);
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						case SETPRO4: {
						//  giveQuestItem(env, 182216281, 1);
							qs.setQuestVar(4);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 703550: {
					switch (dialog) {
						// ToDo: check correct action for this npc
						case USE_OBJECT: {
							qs.setQuestVar(5);
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
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 836544) {
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

			// (0) Step: 4, Count: 5, Mobs : 703550
			// (1) Step: 6, Count: 5, Mobs : 655438
			// (2) Step: 6, Count: 3, Mobs : 703551

			switch (var) {
				case 4: {
				// ???
				}
				case 6: {
				// ???
				}
				case 6: {
				// ???
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
