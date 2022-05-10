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
package quest.poeta;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author MrPoke
 * 
 */
public class _1111InsomniaMedicine extends QuestHandler
{
	private final static int	questId	= 1111;

	public _1111InsomniaMedicine()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203075).addOnQuestStart(questId);
		int[] npcs = {203075, 203061};
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 203075))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203061)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1352);
						else if(var == 1)
							return sendQuestDialog(env, 1353);
					case 34:
						return defaultQuestItemCheck(env, 0, 1, false, 1353, 1693);
					case 10000:
						return defaultCloseDialog(env, 1, 2, true, false, 182200222, 1, 0, 0);
					case 10001:
						return defaultCloseDialog(env, 1, 3, true, false, 182200221, 1, 0, 0);
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(env.getTargetId() == 203075)
			{
				if(env.getDialogId() == -1)
				{
					if(var == 2)
						return sendQuestDialog(env, 2375, 0, 0, 182200222, 1);
					else if(var == 3)
						return sendQuestDialog(env, 2716, 0, 0, 182200221, 1);
				}
				else
					return defaultQuestEndDialog(env, var - 2);
			}
		}
		return false;
	}
}
