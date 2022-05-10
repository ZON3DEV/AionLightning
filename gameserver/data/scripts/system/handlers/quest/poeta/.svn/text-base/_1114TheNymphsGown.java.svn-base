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
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Rhys2002
 * 
 */
public class _1114TheNymphsGown extends QuestHandler
{
	private final static int	questId	= 1114;
	private final static int[]	npc_ids	= { 203075, 203058, 700008 };

	public _1114TheNymphsGown()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(env.getTargetId() == 0)
			return defaultQuestStartItem(env, 182200226, 1, 182200214, 1);

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203075:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 2)
								return sendQuestDialog(env, 1693);
							else if(var == 3)
								return sendQuestDialog(env, 2375);	
						case 1009:
							if(var == 2)
								return defaultCloseDialog(env, 2, 4, true, true, 1, 0, 0, 182200217, 1);
							else if(var == 3)
								return defaultCloseDialog(env, 3, 4, true, true, 1, 0, 0, 182200217, 1);
						case 10000:
							return defaultCloseDialog(env, 0, 1, 0, 0, 182200226, 1);
						case 10001:
							return defaultCloseDialog(env, 2, 3);
					}
					break;
				case 700008:
					if(env.getDialogId() == -1)
						return defaultQuestUseNpc(env, 1, 2, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
					break;
				case 203058:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10001:
							return defaultCloseDialog(env, 3, 0);
						case 10002:
							if(defaultCloseDialog(env, 3, 0, true, false, 0, 0, 182200217, 1))
								return sendQuestDialog(env, 5);
					}
					break;
			}
		}
		if(var == 3)
			return defaultQuestRewardDialog(env, 203058, 0);
		if(var == 4)
			return defaultQuestRewardDialog(env, 203075, 0, 1);
		return false;
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		player.getKnownList().doOnAllNpcs(new Executor<Npc>(){
			@Override
			public boolean run(Npc obj)
			{
				if (obj.getNpcId() != 203175)
					return true;
				
				obj.getAggroList().addDamage(player, 50);
				return true;
			}
		}, true);
		if(defaultQuestGiveItem(env, 182200217, 1))
		{
			qs.setQuestVarById(0, 2);
			updateQuestStatus(env);
		}
	}
}