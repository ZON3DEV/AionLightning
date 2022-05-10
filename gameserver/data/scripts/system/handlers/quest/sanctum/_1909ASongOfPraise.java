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
package quest.sanctum;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
* @author MrPoke remod By Nephis
*
*/
public class _1909ASongOfPraise extends QuestHandler
{
   private final static int   questId   = 1909;

   public _1909ASongOfPraise()
   {
      super(questId);
   }
   
    @Override
   public void register()
   {
      qe.setNpcQuestData(203739).addOnQuestStart(questId);
      qe.setNpcQuestData(203739).addOnTalkEvent(questId);
      qe.setNpcQuestData(203726).addOnTalkEvent(questId);
      qe.setNpcQuestData(203099).addOnTalkEvent(questId);
   }

   @Override
   public boolean onDialogEvent(QuestCookie env)
   {
      final Player player = env.getPlayer();
      int targetId = 0;
      if(env.getVisibleObject() instanceof Npc)
         targetId = ((Npc) env.getVisibleObject()).getNpcId();
      QuestState qs = player.getQuestStateList().getQuestState(questId);
      if(targetId == 203739)
      {
         if(qs == null || qs.getStatus() == QuestStatus.NONE)
         {
            if(env.getDialogId() == 26)
               return sendQuestDialog(env, 1011);
            else
               return defaultQuestStartDialog(env);
         }
      }
      else if(targetId == 203726)
      {
         if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
         {
            if(env.getDialogId() == 26)
               return sendQuestDialog(env, 1352);
            else if(env.getDialogId() == 10000)
            {
            	defaultCloseDialog(env, 0, 1, 182206001, 1, 0, 0);
            	return true;
            }
            else
               return defaultQuestStartDialog(env);
         }
      }
      else if(targetId == 203099)
      {
         if(qs != null)
         {
            if(env.getDialogId() == 26 && qs.getStatus() == QuestStatus.START)
               return sendQuestDialog(env, 2375);
            else if(env.getDialogId() == 1009)
            {
               return defaultCloseDialog(env, 1, 2, true, true, 0, 0, 0, 182206001, 1);
            }
            else
               return defaultQuestEndDialog(env);
         }
      }
      return false;
   }
}