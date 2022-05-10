/*
 * This file is part of Aion Mythology <www.aionmythology.com>.
 */
package quest.reshanta;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;

import java.util.Collections;

/**
 * @author Orpheo
 */

public class _4205SmacktheShulack extends QuestHandler {
    private final static int questId = 4205;

    public _4205SmacktheShulack() {
        super(questId);
    }

    @Override
    public void register() {
		qe.addQuestLvlUp(questId);
			qe.setNpcQuestData(279010).addOnQuestStart(questId);
			qe.setNpcQuestData(279010).addOnTalkEvent(questId);
			qe.setNpcQuestData(214929).addOnKillEvent(questId);
			qe.setNpcQuestData(204202).addOnTalkEvent(questId);
			qe.setNpcQuestData(204285).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 4200);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 214929, 0, 15))
            return true;
        else
            return false;
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 279010) {
                if (env.getDialogId() == 26)
                    return sendQuestDialog(env, 4762);
                else
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (targetId == 279010) {
                if (env.getDialogId() == 26)
                    return sendQuestDialog(env, 1352);
                else if (env.getDialogId() == 10001) {
                    qs.setQuestVarById(0, var + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                }
            } else if (targetId == 204202) {
                if (env.getDialogId() == 26)
                    return sendQuestDialog(env, 1693);
                else if (env.getDialogId() == 10255) {
                    qs.setQuestVarById(0, var + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                }
            } else if (targetId == 204285) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 1009) {
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD && targetId == 204285) {
            return defaultQuestEndDialog(env); 
        }
        return false;
    }
}