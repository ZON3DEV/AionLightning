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
package quest.greater_stigma_quest;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;

import java.util.Collections;


public class _3931HowToUseStigma extends QuestHandler
{
	private final static int	questId	= 3931;
	private final static int[]	npc_ids	= { 798321, 279005, 203711 };	

  public _3931HowToUseStigma()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
			
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 203711)//Miriya
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else return defaultQuestStartDialog(env);
        
      }  
      return false;
    }
	
    
    int var = qs.getQuestVarById(0);
    
    if(qs.getStatus() == QuestStatus.REWARD)
		{
		 
     if(targetId == 203711 && player.getInventory().getItemCountByItemId(182206081) == 1)//Miriya
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
		    else return defaultQuestEndDialog(env); 
     	}
		 return false;	
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
     if(targetId == 798321)//Koruchinerk
		{
      switch(env.getDialogId())
					{
					case 26:
					 if (var == 0)
            return sendQuestDialog(env, 1011);
           if (var == 1)
            return sendQuestDialog(env, 1352);
          case 34:
           if (var == 1)
            {
             if(QuestService.collectItemCheck(env, true))				
					   {
						  qs.setQuestVarById(0, var + 1);					
						  updateQuestStatus(env);
						  ItemService.addItems(player, Collections.singletonList(new QuestItems(182206080, 1)));
						  return sendQuestDialog(env, 10000);
					   }
					   else
						  return sendQuestDialog(env, 10001);
            }
          case 10000:
				  if (var == 0)
				  qs.setQuestVarById(0, var + 1);
				  updateQuestStatus(env);
          PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
          return true;
          }
		return false;				
    }else if(targetId == 279005 && player.getInventory().getItemCountByItemId(182206080) == 1 )//Kohrunerk
    {
      switch(env.getDialogId())
					{
					case 26:
					 if (var == 2)
					  return sendQuestDialog(env, 1693);
          case 10255:
				   if (var == 2)
				   player.getInventory().removeFromBagByItemId(182206080, 1);
           ItemService.addItems(player, Collections.singletonList(new QuestItems(182206081, 1)));
           PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
           qs.setStatus(QuestStatus.REWARD);
				   updateQuestStatus(env);
           return true;
          }
    
    
    }
    return false;
    }
  return false;
  }

	
	@Override
	public void register()
	{
		qe.setNpcQuestData(203711).addOnQuestStart(questId);	//Miriya
    qe.setQuestItemIds(182206080).add(questId); //Kohrunerks Belt
    qe.setQuestItemIds(182206081).add(questId);	//Stigma Manual	
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}
   
}
