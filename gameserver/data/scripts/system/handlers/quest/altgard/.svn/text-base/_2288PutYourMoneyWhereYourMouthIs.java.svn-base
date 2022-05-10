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
package quest.altgard;

import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;


/**
 * @author HellBoy
 *
 */
public class _2288PutYourMoneyWhereYourMouthIs extends QuestHandler
{

	private final static int	questId	= 2288;
	private final static int [] mob_ids = {210564, 210584, 210581, 201047, 210436, 210437, 210440};

	public _2288PutYourMoneyWhereYourMouthIs()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203621).addOnQuestStart(questId);
		qe.setNpcQuestData(203621).addOnTalkEvent(questId);
		for(int mob_id: mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		return defaultQuestOnKillEvent(env, mob_ids, 1, 4);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(env.getTargetId() == 203621)
			{
				switch (env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1011);
					case 1007:
						return sendQuestDialog(env, 4);
					case 1002:
						return sendQuestDialog(env, 1003);
					case 1003:
						return sendQuestDialog(env, 1004);
					case 10000:
						if(!env.getPlayer().getQuestTimerOn() && QuestService.startQuest(env, QuestStatus.START))
						{
							QuestService.questTimerStart(env, 600);
							return defaultCloseDialog(env, 0, 1);
						}
						else
							return false;
				}
			}
		}	
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203621)
			{
				if(var == 4)
				{
					if(env.getDialogId() == 26)
						return sendQuestDialog(env, 1352);
					if(env.getDialogId() == 1009)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						QuestService.questTimerEnd(env);
						return defaultQuestEndDialog(env);
					}
				}
			}
		}
		return defaultQuestRewardDialog(env, 203621, 0);
	}
}
