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
package quest.inggison;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


public class _11227EasyAs4321 extends QuestHandler
{
	private final static int questId = 11227;
	
	public _11227EasyAs4321()
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
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 799076) //Shaorunerk
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		else if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2)
		{
			if(env.getDialogId() == 26)
				return sendQuestDialog(env, 2375);
			else if(env.getDialogId() == 1009)
			{
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
					return sendQuestDialog(env,  5);
			}
			else
				return defaultQuestStartDialog(env);
		}
			return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 217071, 0, 1, 0) || defaultQuestOnKillEvent(env, 217070, 0, 1, 1) || defaultQuestOnKillEvent(env, 217069, 0, 1, 0) || defaultQuestOnKillEvent(env, 217068, 0, 1, 1))
			return true;
		else
			return false;
	}
	
	@Override
	public void register()
	{
	    qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(799076).addOnQuestStart(questId); //Shaorunerk
		qe.setNpcQuestData(217071).addOnKillEvent(questId); //Esalki The Fourth
		qe.setNpcQuestData(217070).addOnKillEvent(questId); //Basalki The Third
		qe.setNpcQuestData(217069).addOnKillEvent(questId); //Susalki The Second
		qe.setNpcQuestData(217068).addOnKillEvent(questId); //Disalki The Elder
	}
}