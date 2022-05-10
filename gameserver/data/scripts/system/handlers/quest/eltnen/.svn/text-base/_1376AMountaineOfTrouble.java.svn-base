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

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Atomics
 * 
 */
public class _1376AMountaineOfTrouble extends QuestHandler
{

	private final static int	questId	= 1376;

	public _1376AMountaineOfTrouble()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203947).addOnQuestStart(questId); //Beramones
		qe.setNpcQuestData(203947).addOnTalkEvent(questId); //Beramones
		qe.setNpcQuestData(203964).addOnTalkEvent(questId); //Agrips
		qe.setNpcQuestData(210976).addOnKillEvent(questId); // Kerubien Hunter
		qe.setNpcQuestData(210986).addOnKillEvent(questId); // Kerubien Hunter
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 203947) //Beramones
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203964) //Agrips
			{
			    if (env.getDialogId() == -1)
			    	return sendQuestDialog(env, 1352);
			    return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		int[] mobs = {210976, 210986};
		if(defaultQuestOnKillEvent(env, mobs, 0, 6) || defaultQuestOnKillEvent(env, mobs, 6, true))
			return true;
		else
			return false;
	}
}