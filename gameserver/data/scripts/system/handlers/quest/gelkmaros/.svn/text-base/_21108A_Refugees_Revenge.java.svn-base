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
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


public class _21108A_Refugees_Revenge extends QuestHandler
{
	private final static int questId = 21108;
	
	public _21108A_Refugees_Revenge()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(player.getCommonData().getLevel() < 53)
			return false;
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 799276) //Chenkiki
			{
				if(env.getDialogId() == 26)
				   return sendQuestDialog(env, 1011);
				else
				   return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 799276) //Chenkiki
			{
				if(env.getDialogId() == 26 && qs.getQuestVarById(1) == 20 && qs.getQuestVarById(0) == 15)
				{
				    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				    return sendQuestDialog(env, 1352);
				}
				else
				   return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 799276) //Chenkiki
			{
				if(env.getDialogId() == 26)
				   return sendQuestDialog(env, 5);
				   else if(env.getDialogId() == 1009)
				   return sendQuestDialog(env, 5);
				else
				   return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 216079, 0, 10, 0) || defaultQuestOnKillEvent(env, 216082, 0, 10, 1) || defaultQuestOnKillEvent(env, 216085, 0, 10, 0) || defaultQuestOnKillEvent(env, 216089, 0, 10, 1))
			return true;
		else
			return false;
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(799276).addOnQuestStart(questId); //Chenkiki
		qe.setNpcQuestData(799276).addOnTalkEvent(questId); //Chenkiki
		qe.setNpcQuestData(216079).addOnKillEvent(questId); //Naduka Cracker
		qe.setNpcQuestData(216082).addOnKillEvent(questId); //Naduka Tracker
		qe.setNpcQuestData(216085).addOnKillEvent(questId); //Naduka Scratcher
		qe.setNpcQuestData(216089).addOnKillEvent(questId); //Naduka Healer
	}
}