/*
 * This file is part of Aion Mythology <www.aionmythology.com>.
 */
 
 /*
  * Created by Orpheo
 */
 
package quest.sanctum;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;

import java.util.Collections;

public class _3976LegionaryLetters extends QuestHandler {

    private final static int questId = 3976;

    public _3976LegionaryLetters() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setQuestItemIds(182206136).add(questId);
        qe.setNpcQuestData(203723).addOnQuestStart(questId);
		qe.setNpcQuestData(203723).addOnTalkEvent(questId);
		qe.setNpcQuestData(798222).addOnTalkEvent(questId);
    }

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 203723)
		{
			if(qs == null)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 1011);
				else if (env.getDialogId() == 1002) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206136, 1))))
                        return defaultQuestStartDialog(env);
                    else
                        return true;
                } else
                    return defaultQuestStartDialog(env);
            }
		}
		else if(targetId == 798222)
		{
			if(qs != null)
			{
				if(env.getDialogId() == 26 && qs.getStatus() == QuestStatus.START)
					return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 1009)
				{
					player.getInventory().removeFromBagByItemId(182206136, 1);
					qs.setQuestVar(1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return defaultQuestEndDialog(env);
				}
				else
					return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}
