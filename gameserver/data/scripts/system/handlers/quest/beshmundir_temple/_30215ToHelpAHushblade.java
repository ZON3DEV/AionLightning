/*
 * This file is part of aion-unique <aion-unique.org>
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.beshmundir_temple;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;


public class _30215ToHelpAHushblade extends QuestHandler
{
    private final static int questId = 30215;

    public _30215ToHelpAHushblade()
	{
        super(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        // Instanceof
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        // ------------------------------------------------------------
        // NPC Quest :
        // Start To Yulia.
        if(qs == null || qs.getStatus() == QuestStatus.NONE) 
        {
	        if(targetId == 798928) //Yulia
	        {
		       if(env.getDialogId() == 26)
		       {
			      return sendQuestDialog(env, 1011);
		       }
		       else
			     return defaultQuestStartDialog(env);
	        }
        }
		
        if (qs == null)
            return false;

        if(qs == null || qs.getStatus() == QuestStatus.COMPLETE) 
        {
	        if(targetId == 798928) //Yulia
	        {
		       if(env.getDialogId() == 26)
		       {
			      return sendQuestDialog(env, 1011);
		       }
		       else
			     return defaultQuestStartDialog(env);
	        }
        }
		
        if (qs.getStatus() == QuestStatus.START)
		{
            switch (targetId)
			{
                // 1 - Report The Result To Yulia.
                case 798928: //Yulia
                    switch (env.getDialogId())
					{
                        // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                        case 26:
                        // Send select1 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 2375);
                         case 2034:
                        // Send select2 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 2034);
                        // Get HACTION_CHECK_USER_HAS_QUEST_ITEM in the eddit-HyperLinks.xml
                        case 34:
                        // Collect Decree Of Valor (200)
                            if (QuestService.collectItemCheck(env, true))
	          {
                                player.getInventory().removeFromBagByItemId(186000098, 200);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else {
                                // Send check_user_item_fail to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                // No match
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 798928) //Yulia
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
		if(qs != null || !lvlCheck)
			return false;
		QuestService.startQuest(env, QuestStatus.START);
		return true;
	}

    @Override
    public void register()
	{
        qe.setNpcQuestData(798928).addOnQuestStart(questId); //Yulia
        qe.setNpcQuestData(798928).addOnTalkEvent(questId); //Yulia
    }
}