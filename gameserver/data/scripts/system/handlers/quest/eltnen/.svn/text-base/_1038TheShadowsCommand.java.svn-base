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
package quest.eltnen;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;


/**
 * @author Rhys2002
 * fixed by kecimis
 * 
 */
public class _1038TheShadowsCommand extends QuestHandler
{
	private final static int	questId	= 1038;
	private final static int[]	npc_ids	= {203933, 700172, 203991, 700162};
	
	public _1038TheShadowsCommand()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(204005).addOnKillEvent(questId);	
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 700162:
					if(env.getDialogId() == -1)
						return (defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false));
					break;
				case 203933:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
							else if (var == 2)
								return sendQuestDialog(env, 1693);
							else if (var == 3)
								return sendQuestDialog(env, 1694);
							else if (var == 4)
								return sendQuestDialog(env, 2034);
						case 34:
							return defaultQuestItemCheck(env, 4, 0, false, 2035, 2120);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
						case 10002:
							return defaultCloseDialog(env, 3, 4);
						case 10003:
							return defaultCloseDialog(env, 4, 6);
					}
					break;
				case 700172:
					if(env.getDialogId() == -1)
						return (defaultQuestUseNpc(env, 2, 3, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false));
					break;
				case 203991:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 6)
								return sendQuestDialog(env, 2375);
						case 10004:
							if(defaultCloseDialog(env, 6, 7))
							{
								defaultQuestMovie(env, 35);
								QuestService.addNewSpawn(210020000, 1, 204005, (float) 1746.6863, (float) 916, (float) 422, (byte) 11, true);
								return true;
							}
							return false;
					}
					break;
			}			
		}
		return defaultQuestRewardDialog(env, 203991, 2716);
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 204005, 7, true))
			return true;
		else
			return false;
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(env.getTargetId() == 700162 && var == 0)
		{
			defaultQuestMovie(env, 34);
			qs.setQuestVar(1);
			updateQuestStatus(env);
		}
		else if(env.getTargetId() == 700172 && var == 2)
		{
			defaultQuestGiveItem(env, 182201007, 1);
			qs.setQuestVar(3);
			updateQuestStatus(env);
		}
	}
}
