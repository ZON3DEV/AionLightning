/*
 * This file is part of Encom Evolved <Encom Evolved.com>
 *
 *  Encom Evolved is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom Evolved is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom Evolved.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.inggison;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;


public class _11041Marica_Collection extends QuestHandler
{
    private final static int questId = 11041;
	
    public _11041Marica_Collection()
	{
        super(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		
        if(qs == null || qs.getStatus() == QuestStatus.NONE)
        {
	        if(targetId == 798955) //Marica
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
	        if(targetId == 798955) //Marica
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
                case 798955: //Marica
                    switch (env.getDialogId())
					{
                        case 26:
                            return sendQuestDialog(env, 2375);
                         case 2034:
                            return sendQuestDialog(env, 2034);
                        case 34:
                        //Collect Priest Club (10)
						//Collect Assassin Bandana (20)
                        //Collect Ranger Necklace (15)
                            if (QuestService.collectItemCheck(env, true))
	          {                 
			                    player.getInventory().removeFromBagByItemId(182206735, 10); 
			                    player.getInventory().removeFromBagByItemId(182206736, 20);
								player.getInventory().removeFromBagByItemId(182206737, 15);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else
							{
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                //
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 798955) //Marica
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
    @Override
    public void register()
	{
        qe.setNpcQuestData(798955).addOnQuestStart(questId); //Marica
        qe.setNpcQuestData(798955).addOnTalkEvent(questId); //Marica
    }
}