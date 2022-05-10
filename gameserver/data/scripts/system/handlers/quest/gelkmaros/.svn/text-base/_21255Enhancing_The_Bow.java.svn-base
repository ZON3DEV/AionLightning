/*
 * This file is part of aion-unique.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.gelkmaros;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;

import java.util.Collections;


public class _21255Enhancing_The_Bow extends QuestHandler
{
	private final static int questId = 21255;

	public _21255Enhancing_The_Bow()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 799340) //Athana
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 4762);
					case 1007:
						return sendQuestDialog(env, 4);
					case 1002:
						if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182207931, 1)))); //Piece Of Zikel
						if (QuestService.startQuest(env, QuestStatus.START))
							return sendQuestDialog(env, 1003);
						else 
							return false;
					case 1003:
						return sendQuestDialog(env, 1004);
				}
			}
		}
		if(qs == null || qs.getStatus() == QuestStatus.COMPLETE) 
        {
	        if(targetId == 798317) //Usener
	        {
		       if(env.getDialogId() == 26)
		       {
			      return sendQuestDialog(env, 1352);
		       }
		       else
			     return defaultQuestStartDialog(env);
	        }
        }
		if (qs.getStatus() == QuestStatus.START)
		{
            switch (targetId)
			{
                //Usener
                case 798317:
                    switch (env.getDialogId())
					{   
					        //Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                        case 26:
						    //Send select1 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 2375);
                         case 2034:
						    //Send select2 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 2034);
							//Get HACTION_CHECK_USER_HAS_QUEST_ITEM in the eddit-HyperLinks.xml
                        case 34:
                            //Piece Of Zikel (1)
						    //Athana's Bow (1)
                            //Kinah (8000000)
                            if (QuestService.collectItemCheck(env, true))
	          {                 
                                player.getInventory().removeFromBagByItemId(182207931, 1); //Piece Of Zikel
                                player.getInventory().removeFromBagByItemId(101700760, 1); //Athana's Bow
								player.getInventory().removeFromBagByItemId(182400001, 8000000); //Kinah
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else {
                                //
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                //No match
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 798317) //Usener
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(799340).addOnQuestStart(questId); //Athana
		qe.setNpcQuestData(798317).addOnTalkEvent(questId); //Usener
	}
}