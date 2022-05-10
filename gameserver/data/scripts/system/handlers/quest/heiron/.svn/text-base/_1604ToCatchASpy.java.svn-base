/*
 * This file is part of aion-engine <aion-engine.org>.
 *
 * aion-engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Orpheo 
 */
 
public class _1604ToCatchASpy extends QuestHandler
{
	private final static int	questId	= 1604;
	
	public _1604ToCatchASpy()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(204576).addOnQuestStart(questId);
		qe.setNpcQuestData(204576).addOnTalkEvent(questId);
		qe.setNpcQuestData(212615).addOnAttackEvent(questId);	
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 204576)
			{			
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204576)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 10002);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		return false;
	}
	
	@Override
	public boolean onAttackEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 0)
		{
			return false;
		}
			
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		
		if(targetId != 212615)
		{
			return false;
		}
		
		if (MathUtil.getDistance(env.getVisibleObject(), 717.78f, 623.50f, 130) < 8)
		{
			((Npc) env.getVisibleObject()).getController().onDie(null);
			qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
		}
		return false;
	}
}