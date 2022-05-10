/*
 * This file is part of Aion X EMU <aionxemu.com>.
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.pandaemonium;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Rhys2002
 */
public class _2988TheWiseinDisguise extends QuestHandler {
    private final static int questId = 2988;

    public _2988TheWiseinDisguise() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204182).addOnQuestStart(questId);
        qe.setNpcQuestData(204338).addOnQuestStart(questId);
        qe.setNpcQuestData(204213).addOnQuestStart(questId);
        qe.setNpcQuestData(204146).addOnQuestStart(questId);
        qe.setNpcQuestData(204182).addOnTalkEvent(questId);
        qe.setNpcQuestData(204146).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204182) {
                if (env.getDialogId() == 26)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            }
        }

        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 204338 && var == 0) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1352);
                    case 10000:
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                }
            } else if (targetId == 204213 && var == 1) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 1693);
                    case 10001:
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                }
            } else if (targetId == 204146 && var == 2) {
                switch (env.getDialogId()) {
                    case 26:
                        return sendQuestDialog(env, 2034);
                    case 2035:
                        if (player.getInventory().getItemCountByItemId(186000039) == 1) {
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            player.getInventory().removeFromBagByItemId(186000039, 1);
                            return sendQuestDialog(env, 2035);
                        } else
                            return sendQuestDialog(env, 2120);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204146)
                return defaultQuestEndDialog(env);
        }
        return false;
    }
}
