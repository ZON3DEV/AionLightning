/*
 *  This file is part of Aion-Core Extreme <http://www.aion-core.net>.
 *
 *  Aion-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Aion-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Aion-Core Extreme.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.inggison;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.QuestsData;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;

/**
 *
 * @author Orpheo
 */
public class _11018Scout_Mithril_Reward_Coin51 extends QuestHandler {

    static QuestsData questsData = DataManager.QUEST_DATA;
    private final static int questId = 11018;

    public _11018Scout_Mithril_Reward_Coin51() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(798915).addOnQuestStart(questId);
        qe.setNpcQuestData(798915).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        if (player.getLevel() <= 50)
            return sendQuestDialog(env, 1008);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestTemplate	template = questsData.getQuestById(questId);

        if (targetId == 798915)
        {
            if (qs == null || qs.getStatus() == QuestStatus.NONE)
            {
                if (env.getDialogId() == 2)
                {
                    PlayerClass playerClass = player.getCommonData().getPlayerClass();
                    if (playerClass == PlayerClass.ASSASSIN || playerClass == PlayerClass.RANGER || playerClass == PlayerClass.SCOUT)
                    {
                         env.setQuestId(questId);
		         QuestService.startQuest(env, QuestStatus.START);
		         return true;
                    }
                    else
                    {
                       return sendQuestDialog(env, 3739);
                    }
                }
            }
            else if (qs != null && qs.getStatus() == QuestStatus.START)
            {
                if (env.getDialogId() == 2)
                {
                    return sendQuestDialog(env, 1011);
                }
                else if (env.getDialogId() == 10000)
                {
                    if(player.getInventory().getItemCountByItemId(186000018) >= 1800)
                    {
                        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                        player.getInventory().removeFromBagByItemId(186000018, 1800);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env); 
                        return sendQuestDialog(env, 5);
                    }
                    else
                    {
                       return sendQuestDialog(env, 1009);
                    }
                }
                else if (env.getDialogId() == 10001)
                {
                    if(player.getInventory().getItemCountByItemId(186000018) >= 1200)
                    {
                        qs.setQuestVarById(0, qs.getQuestVarById(0) + 2);
                        player.getInventory().removeFromBagByItemId(186000018, 1200);
                        qs.setStatus(QuestStatus.REWARD);
                         updateQuestStatus(env);
                       return sendQuestDialog(env, 6);
                    }
                    else
                    {
                        return sendQuestDialog(env, 1009);
                    }
                }
                else if (env.getDialogId() == 10002)
                {
                    if(player.getInventory().getItemCountByItemId(186000018) >= 900)
                    {
                        qs.setQuestVarById(0, qs.getQuestVarById(0) + 3);
                       player.getInventory().removeFromBagByItemId(186000018, 900);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);   
                       return sendQuestDialog(env, 7);
                    }
                    else
                    {
                         return sendQuestDialog(env, 1009);
                    }
                }
                else if (env.getDialogId() == 10003)
                {
                    if(player.getInventory().getItemCountByItemId(186000018) >= 600)
                    {
                        qs.setQuestVarById(0, qs.getQuestVarById(0) + 4);
                        player.getInventory().removeFromBagByItemId(186000018, 600);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env); 
                        return sendQuestDialog(env, 8);
                    }
                    else
                    {
                        return sendQuestDialog(env, 1009);
                    }
                }
            }
            else if (qs.getStatus() == QuestStatus.COMPLETE)
            {
                if (env.getDialogId() == 2)
                {
                    if ((qs.getCompleteCount() <= template.getMaxRepeatCount()))
                    {
                         env.setQuestId(questId);
		         QuestService.startQuest(env, QuestStatus.START);
		         return true;
                    }
                    else
                       return sendQuestDialog(env, 1008);
                }
            }
            else if (qs != null && qs.getStatus() == QuestStatus.REWARD)
            {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
