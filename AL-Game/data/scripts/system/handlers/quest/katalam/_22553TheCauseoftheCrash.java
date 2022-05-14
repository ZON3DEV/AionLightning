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

package quest.katalam;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Romanz
 */
public class _22553TheCauseoftheCrash extends QuestHandler {

	private final static int questId = 22553;

	public _22553TheCauseoftheCrash() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(801005).addOnQuestStart(questId);
		qe.registerQuestNpc(801005).addOnTalkEvent(questId);
		qe.registerQuestNpc(206318).addOnAtDistanceEvent(questId);
		qe.registerQuestNpc(730778).addOnTalkEvent(questId);
        qe.registerOnEnterZone(ZoneName.get("LDF5A_SENSORYAREA_Q12829_206318_9_600050000"), questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 801005) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 4762);
					}
					case QUEST_ACCEPT_SIMPLE: {
						return sendQuestStartDialog(env);
					}
					default:
						return false;
				}
			}
		}
			else if (targetId == 730778) {
				switch (dialog) {
					case QUEST_SELECT: {
						if (qs.getQuestVarById(0) == 1) {
							return sendQuestDialog(env, 1352);
						}
					}
					case SET_SUCCEED: {
						return defaultCloseDialog(env, 1, 2, true, false);
					}
					default:
						return false;
				}
			}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 801005) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onAtDistanceEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (qs.getQuestVarById(0) == 0) {
				changeQuestStep(env, 0, 1, false);
	  		return true;
			}
  	}
		return false;
	}

    @Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        Player player = env.getPlayer();
        if (player == null)
            return false;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (zoneName == ZoneName.get("LDF5A_SENSORYAREA_Q12829_206318_9_600050000")) {
                if (var == 0) {
                    changeQuestStep(env, 0, 1, false);
                }
            }
        }
        return false;
    }
}
