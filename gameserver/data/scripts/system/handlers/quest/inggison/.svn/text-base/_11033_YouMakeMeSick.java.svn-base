package quest.inggison;

import java.util.Collections;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author Assholes
 */
    public class _11033_YouMakeMeSick extends QuestHandler {
    private final static int questId = 11033;

    public _11033_YouMakeMeSick() {
        super(questId);
       }

       @Override
       public void register() {
        int[] npcs = {798959};
        for (int npc : npcs)
        qe.setNpcQuestData(npc).addOnTalkEvent(questId);
         qe.setQuestItemIds(182206728).add(questId);
        qe.setNpcQuestData(798959).addOnQuestStart(questId);
    }

       @Override
        public boolean onDialogEvent(QuestCookie env) {
        if (defaultQuestNoneDialog(env, 798959, 4762))
        return true;

        final Player player = env.getPlayer();

        QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        int var = qs.getQuestVarById(0);
         if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getTargetId() == 798959) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else return defaultQuestEndDialog(env);
            }
            return false;
        }
        if (qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 798959:
                    switch (env.getDialogId()) {
                        case 26:
                            if (var == 0)
                                return sendQuestDialog(env, 1011);
                             else if (var == 1)
                            return sendQuestDialog(env, 1352);
                        case 34:
                        if (var == 0) {
                        if (QuestService.collectItemCheck(env, true)) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        } else
                            return sendQuestDialog(env, 10001);
                    }
                         case 10001:
                            if (var == 1){
                            if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182206728, 1))))
                             return true;
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            }
                            return true;
                    }
            }
        }
          return false;
    }

       @Override
       public HandlerResult onItemUseEvent(final QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != 182206728)
            return HandlerResult.UNKNOWN;
        
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                defaultQuestRemoveItem(env, 182206728, 1);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
            }
        }, 1000);
        return HandlerResult.SUCCESS;
    }
}
