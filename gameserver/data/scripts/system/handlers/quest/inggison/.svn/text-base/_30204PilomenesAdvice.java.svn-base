/*
 * This file is part of aion-unique
 *
 *  aion-engine is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.inggison;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


public class _30204PilomenesAdvice	extends QuestHandler
{
	private final static int questId = 30204;
	
	public _30204PilomenesAdvice()
	{
		super (questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if (targetId == 798926) // Outremus Start
			{
				if (env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return defaultQuestStartDialog(env);
			}
		}
		if (qs == null)
			return false;		
		if (targetId == 798941 && qs.getStatus() == QuestStatus.REWARD) //Pilomenes
		{
			return sendQuestDialog(env, 2375);
		}
		return false;
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(798926).addOnQuestStart(questId); //Outremus
		qe.setNpcQuestData(798941).addOnTalkEvent(questId); //Pilomenes
	}
}