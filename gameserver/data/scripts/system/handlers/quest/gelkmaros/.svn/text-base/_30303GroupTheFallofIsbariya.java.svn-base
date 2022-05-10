package quest.gelkmaros;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Nephis @modifie by Assholes
 */
public class _30303GroupTheFallofIsbariya extends QuestHandler {

    private final static int questId = 30303;
    private final static int[] mob_ids = {216255, 216257, 216259, 216261, 216263};

    public _30303GroupTheFallofIsbariya() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(799225).addOnQuestStart(questId);
        qe.setNpcQuestData(799225).addOnTalkEvent(questId);
        for (int mob_id : mob_ids)
            qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 216255, 0, 1, 0) || defaultQuestOnKillEvent(env, 216257, 0, 1, 1) || defaultQuestOnKillEvent(env, 216259, 0, 1, 2) || defaultQuestOnKillEvent(env, 216261, 0, 1, 3) || defaultQuestOnKillEvent(env, 216263, 0, true, 4))
            return true;
        else
            return false;
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 799225) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26)
                    return sendQuestDialog(env, 4762);
                else
                    return defaultQuestStartDialog(env);
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD)
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else
                    return defaultQuestEndDialog(env);
        }

        return false;
    }
}
