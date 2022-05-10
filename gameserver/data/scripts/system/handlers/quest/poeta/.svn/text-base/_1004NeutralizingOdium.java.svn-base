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

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author MrPoke
 *
 */
public class _1004NeutralizingOdium extends QuestHandler
{
	private final static int	questId	= 1004;
	private final static int[]	npcs = {203082, 700030, 790001, 203067};

	public _1004NeutralizingOdium()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203082:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if (var == 5)
								return sendQuestDialog(env, 2034);
						case 1013:
							if(var == 0)
								return defaultQuestMovie(env, 19);
						case 10000:	
							return defaultCloseDialog(env, 0, 1);
						case 10002:
							return defaultCloseDialog(env, 5, 0, true, false);
					}
					break;
				case 700030:
					if(env.getDialogId() == -1)
						return (defaultQuestUseNpc(env, 1, 2, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false) || defaultQuestUseNpc(env, 4, 5, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false));
					break;
				case 790001:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1352);
							else if (var == 3)
								return sendQuestDialog(env, 1693);
							else if (var == 11)
								return sendQuestDialog(env, 1694);
						case 10001:
							return defaultCloseDialog(env, 2, 3);
						case 10002:
							return defaultCloseDialog(env, 11, 4, 182200006, 1, 182200005, 1);
						case 34:
							return defaultQuestItemCheck(env, 3, 11, false, 1694, 1779);
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 203067, 0);
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(var == 1)
		{
			if(defaultQuestGiveItem(env, 182200005, 1))
				qs.setQuestVar(2);
		}
		if(var == 4)
		{
			defaultQuestRemoveItem(env, 182200006, 1);
			qs.setQuestVar(5);
		}
		updateQuestStatus(env);
	}
}