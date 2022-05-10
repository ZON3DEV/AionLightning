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
 * @author XRONOS
 *
 */
public class _2392BeautifulFeather extends QuestHandler
{

	private final static int	questId	= 2392;
	
	public _2392BeautifulFeather()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798085).addOnQuestStart(questId);
		qe.setNpcQuestData(798085).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
	
		if(defaultQuestNoneDialog(env, 798085, 4762))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 798085)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
					case 10000:
						if(player.getInventory().getItemCountByItemId(182204159) > 0)
						{
							qs.setQuestVar(1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 5);
						}
						else
							return sendQuestDialog(env, 1097);
					case 10001:
						if(player.getInventory().getItemCountByItemId(182204160) > 0)
						{
							qs.setQuestVar(2);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 6);
						}
						else
							return sendQuestDialog(env, 1097);
					case 10002:
						if(player.getInventory().getItemCountByItemId(182204161) > 0)
						{
							qs.setQuestVar(3);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 7);
						}
						else
							return sendQuestDialog(env, 1097);
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(var == 1)
				return defaultQuestRewardDialog(env, 798085, 5, 0);
			else if(var == 2)
				return defaultQuestRewardDialog(env, 798085, 6, 1);
			else if(var == 3)
				return defaultQuestRewardDialog(env, 798085, 7, 2);
		}
		return false;
	}
}