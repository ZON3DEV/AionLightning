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
package quest.daevanion_quest;

import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author HellBoy
 * 
 */
public class _2989CeremonyoftheWise extends QuestHandler
{
	private final static int	questId	= 2989;
	private final static int[]	npcs	= {204146, 204056, 204057, 204058, 204059};

	public _2989CeremonyoftheWise()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204146).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 204146))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if(qs.getStatus() == QuestStatus.START)
		{
			PlayerClass playerClass = player.getCommonData().getPlayerClass();
			switch(env.getTargetId())
			{
				case 204056:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR)
									return sendQuestDialog(env, 1352);
								else
									return sendQuestDialog(env, 1438);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 204057:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.ASSASSIN || playerClass == PlayerClass.RANGER)
									return sendQuestDialog(env, 1693);
								else
									return sendQuestDialog(env, 1779);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 204058:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER)
									return sendQuestDialog(env, 2034);
								else
									return sendQuestDialog(env, 2120);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
				case 204059:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.CLERIC || playerClass == PlayerClass.CHANTER)
									return sendQuestDialog(env, 2375);
								else
									return sendQuestDialog(env, 2461);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 204146:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 2716);
							else if(var == 2)
								return sendQuestDialog(env, 3057);
							else if(var == 3)
							{
								if(player.getCommonData().getDp() < 4000)
									return sendQuestDialog(env, 3484);	
								else
									return sendQuestDialog(env, 3398);
							}
							else if(var == 4)
							{
								if(player.getCommonData().getDp() < 4000)
									return sendQuestDialog(env, 3825);	
								else
									return sendQuestDialog(env, 3739);
							}
						case 1009:
							if(var == 3 || var == 4)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								defaultQuestMovie(env, 137);
								player.getCommonData().setDp(0);
								return sendQuestDialog(env, 5);
							}
						case 10001:
							if(defaultCloseDialog(env, 1, 2))
								return sendQuestDialog(env, 3057);
						case 10003:
							return defaultCloseDialog(env, 2, 3);
						case 10004:
							return defaultCloseDialog(env, 2, 4);
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 204146, 0);
	}
}
