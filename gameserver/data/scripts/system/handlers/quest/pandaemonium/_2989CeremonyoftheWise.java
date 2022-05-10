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

import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Rhys2002
 */
public class _2989CeremonyoftheWise extends QuestHandler {
    private final static int questId = 2989;

    public _2989CeremonyoftheWise() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(204056).addOnQuestStart(questId);
        qe.setNpcQuestData(204057).addOnQuestStart(questId);
        qe.setNpcQuestData(204058).addOnQuestStart(questId);
        qe.setNpcQuestData(204059).addOnQuestStart(questId);
        qe.setNpcQuestData(204146).addOnQuestStart(questId);
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
            if (targetId == 204146) {
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
            PlayerClass playerClass = player.getCommonData().getPlayerClass();
            switch (targetId) {
                case 204056://Traufnir
                    switch (env.getDialogId()) {
                        case 26:
                            if (playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR)
                                return sendQuestDialog(env, 1352);
                            else
                                return sendQuestDialog(env, 1438);
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 204057://Sigyn
                    switch (env.getDialogId()) {
                        case 26:
                            if (playerClass == PlayerClass.ASSASSIN || playerClass == PlayerClass.RANGER)
                                return sendQuestDialog(env, 1693);
                            else
                                return sendQuestDialog(env, 1779);
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 204058://Sif
                    switch (env.getDialogId()) {
                        case 26:
                            if (playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER)
                                return sendQuestDialog(env, 2034);
                            else
                                return sendQuestDialog(env, 2120);
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 204059://Freyr
                    switch (env.getDialogId()) {
                        case 26:
                            if (playerClass == PlayerClass.CLERIC || playerClass == PlayerClass.CHANTER)
                                return sendQuestDialog(env, 2375);
                            else
                                return sendQuestDialog(env, 2461);
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 204146:
                    switch (env.getDialogId()) {
                        case 26:
                            if (var == 1)
                                return sendQuestDialog(env, 2716);
                            else if (var == 2)
                                return sendQuestDialog(env, 3057);
                            else if (var == 3) {
                                if (player.getCommonData().getDp() < 4000)
                                    return sendQuestDialog(env, 3484);
                                else
                                    return sendQuestDialog(env, 3398);
                            } else if (var == 4) {
                                if (player.getCommonData().getDp() < 4000)
                                    return sendQuestDialog(env, 3825);
                                else
                                    return sendQuestDialog(env, 3739);
                            }
                        case 1009:
                            if (var == 3) {
                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 137));
                                player.getCommonData().setDp(0);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else if (var == 4) {
                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 137));
                                player.getCommonData().setDp(0);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else
                                return this.defaultQuestEndDialog(env);
                        case 10001:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 3057);
                        case 10003:
                            qs.setQuestVarById(0, 3);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        case 10004:
                            qs.setQuestVarById(0, 4);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204146)
                return defaultQuestEndDialog(env);
        }
        return false;
    }
}
