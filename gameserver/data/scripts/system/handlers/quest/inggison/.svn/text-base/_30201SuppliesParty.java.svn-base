/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package quest.inggison;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;

/**
 * @author Fennek, PZIKO333
 * 
 */

public class _30201SuppliesParty extends QuestHandler
{
	private final static int questId = 30201;

	public _30201SuppliesParty()
	{
		super (questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798926).addOnQuestStart(questId);
		qe.setNpcQuestData(798926).addOnTalkEvent(questId);
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
			if (targetId == 798926)
			{
				if (env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
			return false;
		} else if (targetId == 798926) {
			if (qs != null || qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if (env.getDialogId() == 26)
					return sendQuestDialog(env, 2375);
				else if (env.getDialogId() == 1007)
					return sendQuestDialog(env, 2375);
				else if (env.getDialogId() == 34)
				{
					if(player.getInventory().getItemCountByItemId(182209601) == 0)
					{
						return sendQuestDialog(env, 2716);
					} else {
						qs.setStatus(QuestStatus.REWARD);
						return sendQuestDialog(env, 5);						
					}
				}
				else if (env.getDialogId() == 17)
				{
					player.getInventory().removeFromBagByItemId(182209601, 1);
					updateQuestStatus(env);
					return defaultQuestEndDialog(env);
				}
			}
			if (qs != null || qs.getStatus() == QuestStatus.REWARD )
			{
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}

}
