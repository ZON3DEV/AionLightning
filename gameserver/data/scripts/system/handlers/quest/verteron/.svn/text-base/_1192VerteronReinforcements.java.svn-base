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
package quest.verteron;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Balthazar
 */

public class _1192VerteronReinforcements extends QuestHandler
{
	private final static int	questId	= 1192;

	public _1192VerteronReinforcements()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		int npcs[] = {203098, 203701, 203833};
		qe.setNpcQuestData(203098).addOnQuestStart(questId);
		for(int id: npcs)
			qe.setNpcQuestData(id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 203098, 182200556, 1))
			return true;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203701:
				{
					switch(env.getDialogId())
					{
						case -1:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1, 0, 0, 182200556, 1);
					}
				}
				case 203833:
				{
					switch(env.getDialogId())
					{
						case -1:
							if(var == 1)
								return sendQuestDialog(env, 1693);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
					}
				}
				case 203098:
				{
					switch(env.getDialogId())
					{
						case -1:
							if(var == 2)
								return sendQuestDialog(env, 2375);
						case 1009:
							return defaultCloseDialog(env, 2, 3, true, true);
					}
				}
			}
		}
		return defaultQuestRewardDialog(env, 203098, 0);
	}
}