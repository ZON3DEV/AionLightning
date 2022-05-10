/*
 * This file is part of aion-unique
 *
 *  aion-engine is free software: you can redistribute it and/or modify
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
package quest.event;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.bonus.AbstractInventoryBonus;
import org.openaion.gameserver.model.templates.bonus.CutSceneBonus;
import org.openaion.gameserver.model.templates.bonus.InventoryBonusType;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;

public class _80018EventSockItToEm extends QuestHandler
{

	private final static int	questId	= 80018;
	
	public _80018EventSockItToEm()
	{
		super(questId);
	}
	
	@Override
    public HandlerResult onBonusApplyEvent(QuestCookie env, int index, AbstractInventoryBonus bonus)
    {
		if(!(bonus instanceof CutSceneBonus))
		    return HandlerResult.UNKNOWN;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null && qs.getStatus() == QuestStatus.REWARD)
		{
			int var1 = qs.getQuestVarById(1);
			int var2 = qs.getQuestVarById(2);
			qs.setQuestVarById(1, var1 + 1);
			// randomize movie
			if ((var1 == 1 && var2 == 0) || var1 == 0 && Rnd.get() * 100 < 50)
			{
				if (qs.getCompleteCount() == 9)
					ItemService.addItems(player, Collections.singletonList(new QuestItems(188051106, 1)));
				qs.setQuestVarById(2, 1);
				return HandlerResult.SUCCESS;
			}
		}
		return HandlerResult.FAILED;
    }
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if ((qs == null || qs.getStatus() == QuestStatus.NONE) && !onLvlUpEvent(env))
			return false;
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE || 
		   qs.getStatus() == QuestStatus.COMPLETE && qs.getCompleteCount() < 10)
		{
			if (env.getTargetId() == 799778)
			{
				switch (env.getDialogId())
				{
					case -1:
						return sendQuestDialog(env, 1011);
					case 1002:
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(1, questId, QuestStatus.START, 0));
						if(qs == null)
							player.getQuestStateList().addQuest(questId, qs);
						else
						{
							qs.setStatus(QuestStatus.START);
							qs.setQuestVar(0);
						}
						player.getController().updateNearbyQuests();
						return sendQuestDialog(env, 1003);
					}
					default:
						return defaultQuestStartDialog(env);
				}
			}
			return false;
		}

		int var = qs.getQuestVarById(0);
		
		if (qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 799778)
			{
				switch(env.getDialogId())
				{
					case -1:
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 2375);
					case 34:
						return defaultQuestItemCheck(env, 0, 1, true, 5, 2716);
				}
			}
		}
		
		return defaultQuestRewardDialog(env, 799778, 0);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Calendar now = Calendar.getInstance();
		Calendar cal1 = new GregorianCalendar(now.get(Calendar.YEAR), 11, 15);
		Calendar cal2 = new GregorianCalendar(now.get(Calendar.YEAR) + 1, 0, 5);
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (cal1.before(now) && cal2.after(now))
		{
			if(!QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel()))
				return false;
			
			// Start once
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
				return QuestService.startQuest(env, QuestStatus.START);
		} 
		else if (qs != null)
		{
			// Set as expired
			QuestEngine.getInstance().deleteQuest(player, questId);
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId));
			player.getController().updateNearbyQuests();
		}
		return false;
	}
	
    @Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(799778).addOnQuestStart(questId);
		qe.setNpcQuestData(799778).addOnTalkEvent(questId);
		qe.setQuestBonusType(InventoryBonusType.MOVIE).add(questId);
	}

}

