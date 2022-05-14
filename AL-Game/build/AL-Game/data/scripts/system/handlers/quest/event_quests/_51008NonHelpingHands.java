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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Alcapwnd
 */

public class _51008NonHelpingHands extends QuestHandler {

    private final static int questId = 51008;

    public _51008NonHelpingHands() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(831039).addOnQuestStart(questId);
        qe.registerQuestNpc(831039).addOnTalkEvent(questId);
        qe.registerQuestNpc(831037).addOnTalkEvent(questId);
        qe.registerQuestNpc(831037).addOnTalkEvent(questId);
        qe.registerQuestNpc(219291).addOnAddAggroListEvent(questId);
        qe.registerAddOnReachTargetEvent(questId);
    }

    @Override
    public boolean onAddAggroListEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 0 || var == 1) {
                defaultStartFollowEvent(env, (Npc) env.getVisibleObject(), ZoneName.get("Q51008"), var, var);
                return true;
            }
        }
        return false;
    }

	@Override
    public boolean onNpcReachTargetEvent(QuestEnv env) {
            Player player = env.getPlayer();
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            int var = qs.getQuestVarById(0);
            if (var == 0) {
                return defaultFollowEndEvent(env, var, var + 1, false); // 3
            }
            if (var == 1) {
                return defaultFollowEndEvent(env, var, var + 1, true); // 3
            }
        return false;
    }


    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        DialogAction dialog = env.getDialog();
        int targetId = env.getTargetId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 831039) {
                if (dialog == DialogAction.QUEST_SELECT) {
                    return sendQuestDialog(env, 4762);
                }
                else {
                    return sendQuestStartDialog(env);
                }
            }
        }
        else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if(targetId == 831039){
                if  (dialog == DialogAction.USE_OBJECT)
                    return sendQuestDialog(env, 10002);
                else
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }

}