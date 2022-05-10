/*
* This file is part of aion-unique <aion-unique.org>.
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
package quest.reshanta;

import java.util.Collections;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
* @author MrPoke
*
*/
public class _1721MeetingwiththeBrigadeGeneral extends QuestHandler
{
   private final static int   questId   = 1721;

   public _1721MeetingwiththeBrigadeGeneral()
   {
      super(questId);
   }
   
    @Override
   public void register()
   {
		qe.setNpcQuestData(278501).addOnQuestStart(questId);
		qe.setNpcQuestData(278501).addOnTalkEvent(questId);
		qe.setNpcQuestData(278503).addOnTalkEvent(questId);
		qe.setNpcQuestData(278502).addOnTalkEvent(questId);	
		qe.setNpcQuestData(278518).addOnTalkEvent(questId);	  
   }

   @Override
   public boolean onDialogEvent(QuestCookie env)
   {
      final Player player = env.getPlayer();
      int targetId = 0;
      if(env.getVisibleObject() instanceof Npc)
         targetId = ((Npc) env.getVisibleObject()).getNpcId();
      QuestState qs = player.getQuestStateList().getQuestState(questId);
      if(targetId == 278501)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182202151, 1))))
						return defaultQuestStartDialog(env);
					else
						return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
			else if(qs.getStatus() == QuestStatus.START)
			{
            if(env.getDialogId() == 26)
               return sendQuestDialog(env, 2375);
		}
      }
      else if(targetId == 278503)
      {
         if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
         {
            if(env.getDialogId() == 26)
               return sendQuestDialog(env, 1352);
            else if(env.getDialogId() == 10000)
            {
               qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
               updateQuestStatus(env);
               PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
               return true;
            }
            else
               return defaultQuestStartDialog(env);
		}
      }
      else if(targetId == 278502)
      {
         if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1)
         {
            if(env.getDialogId() == 26)
               return sendQuestDialog(env, 1693);
            else if(env.getDialogId() == 10001)
            {
               qs.setQuestVar(2);
			   qs.setStatus(QuestStatus.REWARD);
               updateQuestStatus(env);
			   player.getInventory().removeFromBagByItemId(182202151, 1);
               PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
               return true;
            }
            else
               return defaultQuestStartDialog(env);
         }
      }
      else if(targetId == 278518)
      {
         if(qs != null && qs.getStatus() == QuestStatus.REWARD)
         {
            return defaultQuestEndDialog(env);
         }
      }	  	  
      return false;
   }
}