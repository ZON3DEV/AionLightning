/*
* This file is part of aion-unique <aion-unique.org>.
*
*  aion-unique is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*
*  aion-unique is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
*/
package quest.eltnen;

import java.util.Collections;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
* @author Mr.Poke remod by Nephis and quest helper team
*
*/
public class _1311AGermOfHope extends QuestHandler
{

   private final static int   questId   = 1311;

   public _1311AGermOfHope()
   {
      super(questId);
   }

   @Override
   public void register()
   {
      qe.setNpcQuestData(203997).addOnQuestStart(questId);
      qe.setNpcQuestData(203997).addOnTalkEvent(questId);
      qe.setNpcQuestData(700164).addOnTalkEvent(questId);
      qe.setNpcQuestData(203997).addOnTalkEvent(questId);
   }
   
   @Override
   public boolean onDialogEvent(final QuestCookie env)
   {
      final Player player = env.getPlayer();
      int targetId = 0;
      if(env.getVisibleObject() instanceof Npc)
         targetId = ((Npc) env.getVisibleObject()).getNpcId();
      final QuestState qs = player.getQuestStateList().getQuestState(questId);
      if(qs == null || qs.getStatus() == QuestStatus.NONE)
      {
         if(targetId == 203997)
         {
            if(env.getDialogId() == 26)
               return sendQuestDialog(env, 1011);
            else if(env.getDialogId() == 1013)
            {
               if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182201305, 1))))
               return sendQuestDialog(env, 4);
               else
                  return true;
            }
            else
               return defaultQuestStartDialog(env);
         }
      }
      else if (qs.getStatus() == QuestStatus.START)
      {
         switch(targetId)
         {
            case 700164:
            {
               if (qs.getQuestVarById(0) == 0 && env.getDialogId() == -1)
               {
                  final int targetObjectId = env.getVisibleObject().getObjectId();
                  PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
                     1));
                  PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                     targetObjectId), true);
                  ThreadPoolManager.getInstance().schedule(new Runnable(){
                     @Override
                     public void run()
                     {
                    	 if(!player.isTargeting(targetObjectId))
								return;
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
                           targetObjectId, 3000, 0));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                           targetObjectId), true);
                        player.getInventory().removeFromBagByItemId(182201305, 1);
                        qs.setStatus(QuestStatus.REWARD);
                        qs.setQuestVarById(0, 1);
                        updateQuestStatus(env);

                     }
                  }, 3000);
               }
            }
            case 203997:
            {
               if (qs.getQuestVarById(0) == 1)
               {
                  if(env.getDialogId() == 26)
                     return sendQuestDialog(env, 2375);
                  else if(env.getDialogId() == 34)
                     {
                        player.getInventory().removeFromBagByItemId(182201305, 1);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                     }
                     else
                        return defaultQuestEndDialog(env);
               }
            }
         }
      }
      else if (qs.getStatus() == QuestStatus.REWARD)
      {
         if(targetId == 203997)
            return defaultQuestEndDialog(env);
      }
      return false;
   }
}