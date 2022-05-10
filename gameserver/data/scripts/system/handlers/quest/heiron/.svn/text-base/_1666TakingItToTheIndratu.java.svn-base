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

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Bio/Dreamworks
 * 
 */
public class _1666TakingItToTheIndratu extends QuestHandler
{
	private final static int	questId	= 1666;
	private final static int[]	npc_ids1	= { 213963, 214042, 214043, 214040, 214041};
	private final static int[]	npc_ids2	= { 213985, 213986, 214057, 214056, 214059, 214058};

	public _1666TakingItToTheIndratu()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204601).addOnQuestStart(questId); //Brosia
		for(int ids1 : npc_ids1)
			qe.setNpcQuestData(ids1).addOnKillEvent(questId);
		for(int ids2 : npc_ids2)
			qe.setNpcQuestData(ids2).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);

		int targetId = 0;

		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(targetId == 204601)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
				
			}
			else if(qs.getStatus() == QuestStatus.REWARD)
			{
				return defaultQuestRewardDialog(env, 204601, 2716);
			}
			else if( qs.getQuestVarById(0) == 15 && qs.getQuestVarById(1) == 15 )
			{
				if(env.getDialogId() == 26)
				{
					return sendQuestDialog(env, 1352);
				}
				if(env.getDialogId() == 1009)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestDialog(env, 5);
				}
			}

		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, npc_ids1, 0, 15, 0))
			return true;
		else if(defaultQuestOnKillEvent(env, npc_ids2, 0, 15, 1))
			return true;
		return false;
	}
}