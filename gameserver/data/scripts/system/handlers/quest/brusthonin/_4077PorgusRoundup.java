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
package quest.brusthonin;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.MathUtil;


/**
 * @author Nephis
 * 
 */
public class _4077PorgusRoundup extends QuestHandler
{
	private final static int questId = 4077;

	public _4077PorgusRoundup()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(205158).addOnQuestStart(questId); //Holekk
		qe.setNpcQuestData(205158).addOnTalkEvent(questId);
		qe.setNpcQuestData(214732).addOnAttackEvent(questId);	
	}

	@Override
	public boolean onAttackEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(targetId != 214732)
			return false;
		
		final Npc npc = (Npc) env.getVisibleObject();		
		if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
		{
			if (MathUtil.getDistance(1356, 1901, 46,npc.getX(), npc.getY(), npc.getZ()) > 10)
				return false;
			else
				qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
				updateQuestStatus(env);
				npc.getController().onDespawn(true);
				npc.getController().scheduleRespawn();	
			return true;
		}
		
		else if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1)
		{
		
			if (MathUtil.getDistance(1356, 1901, 46,npc.getX(), npc.getY(), npc.getZ()) > 10)
				return false;
			else
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				npc.getController().onDespawn(true);
				npc.getController().scheduleRespawn();	
			return true;
		}
		
		return false;
	}	

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 205158) //Holekk	
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 205158)
				return defaultQuestEndDialog(env);
		}
		return false;		
	}
}
