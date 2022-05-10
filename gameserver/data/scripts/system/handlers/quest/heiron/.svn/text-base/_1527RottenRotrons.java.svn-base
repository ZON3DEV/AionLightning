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
package quest.heiron;

import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author MrPoke remod By Nephis, Rolandas
 * 
 */
public class _1527RottenRotrons extends QuestHandler
{
	private final static int	questId	= 1527;

	public _1527RottenRotrons()
	{
		super(questId);
	}
	
    @Override
	public void register()
	{
		qe.setNpcQuestData(204555).addOnQuestStart(questId);
		qe.setNpcQuestData(204555).addOnTalkEvent(questId);
		qe.setNpcQuestData(204562).addOnTalkEvent(questId);
		qe.setNpcQuestData(730024).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if (defaultQuestNoneDialog(env, 204555, 182201781, 1))
			return true;
		
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204562:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1, false, false, 0, 0, 182201781, 1);
					}
				break;
				case 730024:
					switch(env.getDialogId())
					{
						case -1:
							if(var == 1)
								return sendQuestDialog(env, 2375);
						case 1009:
							return defaultCloseDialog(env, 1, 0, true, true);
					}
				break;	
			}
		}

		return defaultQuestRewardDialog(env, 730024, 0);
	}
}
