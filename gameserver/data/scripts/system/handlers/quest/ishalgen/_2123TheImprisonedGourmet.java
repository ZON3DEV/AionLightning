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
package quest.ishalgen;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


public class _2123TheImprisonedGourmet extends QuestHandler
{
   private final static int   questId   = 2123;

   public _2123TheImprisonedGourmet()
   {
      super(questId);
   }

   @Override
   public void register()
   {
      qe.setNpcQuestData(203550).addOnQuestStart(questId);
      qe.setNpcQuestData(203550).addOnTalkEvent(questId);
	  qe.setNpcQuestData(700128).addOnTalkEvent(questId);
   }

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if(player.getCommonData().getLevel() < 7)
			return false;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(targetId == 203550)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
         {
            if(env.getDialogId() == 26)
               return sendQuestDialog(env, 1011);
            else
               return defaultQuestStartDialog(env);
         }
			}
			else if(qs.getStatus() == QuestStatus.START)
			{
				long itemCount;
				if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 0)
				{
					return sendQuestDialog(env, 1352);
				}
				else if(env.getDialogId() == 10000 && qs.getQuestVarById(0) == 0)
				{
					itemCount = player.getInventory().getItemCountByItemId(182004687);
					if(itemCount > 0)
					{
						player.getInventory().removeFromBagByItemId(182004687, 1);
						qs.setQuestVar(5);
						updateQuestStatus(env);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					else
					{
						return sendQuestDialog(env, 1693);
					}
				}
				else if(env.getDialogId() == 10001 && qs.getQuestVarById(0) == 0)
				{
					itemCount = player.getInventory().getItemCountByItemId(182203122);
					if(itemCount > 0)
					{	
						player.getInventory().removeFromBagByItemId(182203122, 1);
						qs.setQuestVar(6);
						updateQuestStatus(env);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 6);
					}
					else
					{
						return sendQuestDialog(env, 1693);
					}
				}
				else if(env.getDialogId() == 10002 && qs.getQuestVarById(0) == 0)
				{
					itemCount = player.getInventory().getItemCountByItemId(182203123);
					if(itemCount > 0)
					{
						player.getInventory().removeFromBagByItemId(1822031236, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						qs.setQuestVar(7);
						updateQuestStatus(env);
						return sendQuestDialog(env, 7);
					}
					else
					{
						return sendQuestDialog(env, 1693);
					}
				}
				else
					return defaultQuestEndDialog(env);
			}
			else if(qs.getStatus() == QuestStatus.REWARD)
			{
				if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 5)
				{
					return sendQuestDialog(env, 5);
				}
				else if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 6)
				{
					return sendQuestDialog(env, 6);
				}
				else if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 7)
				{
					return sendQuestDialog(env, 7);
				}
				else
				{
				return defaultQuestEndDialog(env);
				}
			}
			else
			{
				return defaultQuestEndDialog(env);
			}
		}
		else if(targetId == 700128)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						qs.setQuestVar(0);
						updateQuestStatus(env);
					}
				}, 3000);
				return true;
			}
			else
			{
				return defaultQuestEndDialog(env);
			}
		}
		else
		{
		return defaultQuestEndDialog(env);
		}
	}
}
