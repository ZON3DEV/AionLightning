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
	package quest.altgard;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;


/**
 * @author HellBoy
 * 
 */
public class _2252ChasingTheLegend extends QuestHandler
{
	private final static int	questId	= 2252;
	private Npc npc;

	public _2252ChasingTheLegend()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203646).addOnQuestStart(questId);
		qe.setNpcQuestData(203646).addOnTalkEvent(questId);
		qe.setNpcQuestData(700060).addOnTalkEvent(questId);
		qe.setNpcQuestData(210634).addOnKillEvent(questId);
		qe.addOnDie(questId);
	}
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 203646, 182203235, 1))
			return true;
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203646)
			{
				if(env.getDialogId() == 26)
					if(var == 0)
						return sendQuestDialog(env, 2034);
					else if(var == 1)
					{
						qs.setQuestVar(0);
						updateQuestStatus(env);
						return sendQuestDialog(env, 1693);
					}
			}
			else if(env.getTargetId() == 700060 && env.getDialogId() == -1)
				return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
		}
		return defaultQuestRewardDialog(env, 203646, 1352);
	}
	
	@Override
	public boolean onDieEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		if(qs.getQuestVars().getQuestVars() == 0)
		{
			qs.setQuestVar(1);
			updateQuestStatus(env);
			npc.getController().onDelete();
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 210634, 0, 1))
		{
			QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		else
			return false;
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		npc = (Npc)QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 210634, 2407.54f, 1267f, 257.33f, (byte) 47, true);
	}
}
