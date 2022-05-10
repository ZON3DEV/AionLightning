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

public class _1163ArachnaAntidote extends QuestHandler
{
	private final static int	questId	= 1163;

	public _1163ArachnaAntidote()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203096).addOnQuestStart(questId);
		qe.setNpcQuestData(203096).addOnTalkEvent(questId);
		qe.setNpcQuestData(203151).addOnTalkEvent(questId);
		qe.setNpcQuestData(203155).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 203096, 182200564, 1))
			return true;
		
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203151:
				{
					switch(env.getDialogId())
					{
						case -1:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							defaultCloseDialog(env, 0, 1);
					}
				}
				case 203155:
				{
					switch(env.getDialogId())
					{
						case -1:
							if(var == 1)
								return sendQuestDialog(env, 2375);
						case 1009:
							return defaultCloseDialog(env, 1, 2, true, true, 0, 0, 182200564, 1);
					}
				}
			}
		}
		return defaultQuestRewardDialog(env, 203155, 0);
	}
}