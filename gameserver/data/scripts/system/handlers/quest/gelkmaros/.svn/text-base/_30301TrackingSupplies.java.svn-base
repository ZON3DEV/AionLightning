/*
 * This file is part of Encom Evolved <Encom Evolved.com>
 *
 *  Encom Evolved is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom Evolved is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom Evolved.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.gelkmaros;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


public class _30301TrackingSupplies extends QuestHandler
{
    private final static int questId = 30301;

    public _30301TrackingSupplies()
	{
        super(questId);
    }

    @Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if(player.getCommonData().getLevel() < 55)
			return false;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(targetId == 799225)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
			else if(qs.getStatus() == QuestStatus.START)
			{
				long itemCount;
				if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 0)
				{
					return sendQuestDialog(env, 2375);
				}
				else if(env.getDialogId() == 34 && qs.getQuestVarById(0) == 0)
				{
					itemCount = player.getInventory().getItemCountByItemId(182209701);
					if(itemCount > 0)
					{
						player.getInventory().removeFromBagByItemId(182209701, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					else
					{
						return sendQuestDialog(env, 2716);
					}
				}
				else
					return defaultQuestEndDialog(env);
			}
			else
			{
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}

    @Override
    public void register()
	{
        qe.setNpcQuestData(799225).addOnQuestStart(questId);
        qe.setNpcQuestData(799225).addOnTalkEvent(questId);
    }
}