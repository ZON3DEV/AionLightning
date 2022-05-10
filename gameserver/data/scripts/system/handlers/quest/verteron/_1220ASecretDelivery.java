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
package quest.verteron;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Balthazar
 */

public class _1220ASecretDelivery extends QuestHandler
{
	private final static int	questId	= 1220;

	public _1220ASecretDelivery()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203172).addOnQuestStart(questId);
		qe.setNpcQuestData(203172).addOnTalkEvent(questId);
		qe.setNpcQuestData(798004).addOnTalkEvent(questId);
		qe.setNpcQuestData(798046).addOnTalkEvent(questId);
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
			if(targetId == 203172)
			{
				if(env.getDialogId() == 26)
				{
					return sendQuestDialog(env, 1011);
				}
				else
					return defaultQuestStartDialog(env);
			}
		}

		if(qs == null)
			return false;

		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 798004:
				{
					switch(env.getDialogId())
					{
						case 26:
							return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
				}
				case 798046:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							return sendQuestDialog(env, 2375);
						}
						case 1009:
						{
							qs.setQuestVar(2);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return defaultQuestEndDialog(env);
						}
						default:
							return defaultQuestEndDialog(env);
					}
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798046)
			{
				switch(env.getDialogId())
				{
					case 1009:
					{
						return sendQuestDialog(env, 5);
					}
					default:
						return defaultQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}