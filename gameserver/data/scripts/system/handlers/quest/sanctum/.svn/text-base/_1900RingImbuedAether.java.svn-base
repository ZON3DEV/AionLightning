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
package quest.sanctum;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author MrPoke + Dune11, edited Rolandas
 * 
 */
public class _1900RingImbuedAether extends QuestHandler
{
	private final static int	questId	= 1900;

	public _1900RingImbuedAether()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203757).addOnQuestStart(questId);
		qe.setNpcQuestData(203757).addOnTalkEvent(questId);
		qe.setNpcQuestData(203739).addOnTalkEvent(questId);
		qe.setNpcQuestData(203766).addOnTalkEvent(questId);
		qe.setNpcQuestData(203797).addOnTalkEvent(questId);
		qe.setNpcQuestData(203795).addOnTalkEvent(questId);
		qe.setNpcQuestData(203830).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if (defaultQuestNoneDialog(env, 203757, 182206003, 1))
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
				case 203739:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 203766:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1693);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
					}
					break;
				case 203797:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 2034);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
					}
					break;
				case 203795:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2375);
						case 10003:
							return defaultCloseDialog(env, 3, 0, true, false);
					}
					break;
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if (env.getTargetId() == 203830)
			{
				switch(env.getDialogId())
				{
					case -1:
						return sendQuestDialog(env, 2716);
					case 1009:
						defaultQuestRemoveItem(env, 182206003, 1);
						return sendQuestDialog(env, 5);
				}
			}
		}

		return defaultQuestRewardDialog(env, 203830, 0);
	}
}
