/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.morheim;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author HellBoy
 * @Reworked by Lonelyheart
 */
public class _2303DaevaWheresMyHerb extends QuestHandler
{

	private final static int	questId	= 2303;
	
	public _2303DaevaWheresMyHerb()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798082).addOnQuestStart(questId);
		qe.setNpcQuestData(798082).addOnTalkEvent(questId);
		qe.setNpcQuestData(211298).addOnKillEvent(questId);
		qe.setNpcQuestData(211297).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
	
		if(defaultQuestNoneDialog(env, 798082, 4762))
			return true;

		if(qs == null)
		return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 798082)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1003);
					case 10009:
						if(defaultCloseDialog(env, 0, 11))
							return sendQuestDialog(env, 1012);
					case 10019:
						if(defaultCloseDialog(env, 0, 21))
							return sendQuestDialog(env, 1097);
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(var == 15)
				return defaultQuestRewardDialog(env, 798082, 1353);
			else if(var == 26)
				return defaultQuestRewardDialog(env, 798082, 1438, 1);
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 211298, 11, 15) || defaultQuestOnKillEvent(env, 211298, 15, true) || defaultQuestOnKillEvent(env, 211297, 21, 25) || defaultQuestOnKillEvent(env, 211297, 25, true))
			return true;
		else
			return false;
	}
}
