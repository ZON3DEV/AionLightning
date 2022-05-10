/*
 * This file is part of aion-unique <aion-unique.org>
 *
 *  aion-unique is free software: you can redistribute it and/or modify
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
package quest.abyss_entry;

import org.openaion.gameserver.model.flyring.FlyRing;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.flyring.FlyRingTemplate;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.world.WorldMapType;


public class _2042TheLastCheckpoint extends QuestHandler
{
	private final static int	questId	= 2042;

	public _2042TheLastCheckpoint()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204301:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							if(var == 0)
								return defaultCloseDialog(env, 0, 1);
					}
				}
				break;
				case 204319:
				{
					switch(env.getDialogId())
					{
						case -1:
							if(player.getQuestTimerOn())
								return sendQuestDialog(env, 3143);
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
							else if(var == 9)
								return sendQuestDialog(env, 1693);
							else if(var == 8)
								return sendQuestDialog(env, 3057);
						case 1353:
							if(var == 1)
							{
								defaultQuestMovie(env, 89);
								return sendQuestDialog(env, 1353);
							}								
						case 10001:
							if (var == 1 || var == 8)
							{
								defaultCloseDialog(env, var, 2);
								QuestService.questTimerStart(env, 60);
								return true;
							}
						case 10255:
							if (var == 9)
								return defaultCloseDialog(env, 9, 8, true, false);
					}
				}
				break;
			}
		}
		return defaultQuestRewardDialog(env, 204301, 10002);
	}
	
	@Override
	public HandlerResult onFlyThroughRingEvent(QuestCookie env, FlyRing ring)
	{
		FlyRingTemplate template = ring.getTemplate();

		if(template.getMap() != WorldMapType.MORHEIM.getId())
			return HandlerResult.UNKNOWN;

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return HandlerResult.FAILED;

		int var = qs.getQuestVarById(0);
		if (var < 2 || var > 7)
			return HandlerResult.FAILED;
		
		if(ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_1") && var == 2
			|| ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_2") && var == 3
			|| ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_3") && var == 4
			|| ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_4") && var == 5
			|| ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_5") && var == 6)
		{
			qs.setQuestVarById(0, var + 1);
			updateQuestStatus(env);
		}
		else if(ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_6") && var == 7)
		{
			QuestService.questTimerEnd(env);
			qs.setQuestVarById(0, 9);
			updateQuestStatus(env);
		}
		return HandlerResult.SUCCESS;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, false);
	}
	@Override
	public boolean onQuestTimerEndEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		qs.setQuestVarById(0, 8);
		updateQuestStatus(env);
		return true;
	}
	
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(204301).addOnTalkEvent(questId);
		qe.setNpcQuestData(204319).addOnTalkEvent(questId);
		qe.addOnQuestTimerEnd(questId);
		qe.addOnFlyThroughRing(questId);
	}
}