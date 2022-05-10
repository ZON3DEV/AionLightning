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
package quest.reshanta;


import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Hilgert
 * 
 */
public class _3701TeachThemaLesson extends QuestHandler
{
	private final static int	questId	= 3701;

	public _3701TeachThemaLesson()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(278533).addOnQuestStart(questId);
		qe.setNpcQuestData(278517).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
		int targetId = 0;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
		    if(targetId == 278533)
		    {
				if(env.getDialogId() == 26)
				     return sendQuestDialog(env, 1011);
			    else 
				     return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 278517)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 1009)
				{
					 qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					 qs.setStatus(QuestStatus.REWARD);
					 updateQuestStatus(env);
					 return defaultQuestEndDialog(env);
				}
			}	
		}
		else if(qs.getStatus() == QuestStatus.REWARD && targetId == 278517)
		{
			return defaultQuestEndDialog(env);
		}
		return false;
    }		
}
