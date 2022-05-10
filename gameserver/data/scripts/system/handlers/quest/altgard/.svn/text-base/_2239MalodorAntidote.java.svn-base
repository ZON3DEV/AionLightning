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
package quest.altgard;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author HellBoy
 * 
 */
public class _2239MalodorAntidote extends QuestHandler
{
	private final static int	questId	= 2239;
	private final static int[]	npcs = {203613, 203630};

	public _2239MalodorAntidote()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203613).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 203613))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203630)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1352);
						else if(var == 1)
							return sendQuestDialog(env, 1693);
					case 10000:
						return defaultCloseDialog(env, 0, 1);
					case 10001:
						return defaultCloseDialog(env, 1, 3, 182203227, 1, 0, 0);
					case 34:
						return defaultQuestItemCheck(env, 1, 0, false, 1779, 1694);
				}
			}
			else if(env.getTargetId() == 203613)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 3)
							return sendQuestDialog(env, 2034);
					case 10002:
						if(defaultCloseDialog(env, 3, 0, true, false, 0, 0, 182203227, 1))
							return sendQuestDialog(env, 5);
				}
			}
		}
		return defaultQuestRewardDialog(env, 203613, 0);
	}
}