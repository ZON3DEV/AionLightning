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

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.world.zone.ZoneName;


/**
 * @author MrPoke
 * 
 */
public class _1100KaliosCall extends QuestHandler
{

	private final static int	questId	= 1100;

	public _1100KaliosCall()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203067).addOnTalkEvent(questId);
		qe.setQuestEnterZone(ZoneName.AKARIOS_VILLAGE).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203067)
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1011);
					case 1009:
						return defaultCloseDialog(env, 0, 0, true, true);
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(env.getTargetId() == 203067)
			{
				if(env.getDialogId() == 18)
				{
					int[] ids = {1003, 1004, 1005};
					for (int id : ids)
					{
						QuestService.startQuest(new QuestCookie(env.getVisibleObject(), player, id, env.getDialogId()), QuestStatus.LOCKED);
					}
				}
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(zoneName != ZoneName.AKARIOS_VILLAGE)
			return false;
		if(qs != null)
			return false;
		QuestService.startQuest(env, QuestStatus.START);
		return true;
	}
}
