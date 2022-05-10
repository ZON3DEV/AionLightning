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


/**
 * @author MrPoke
 *
 */
public class _1122DeliveringPernossRobe extends QuestHandler
{
	private final static int	questId	= 1122;

	public _1122DeliveringPernossRobe()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		int[] npcs = {203060, 790001};
		qe.setNpcQuestData(203060).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 203060, 182200216, 1))
			return true;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 790001)
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1352);
					case 10000:
						if(player.getInventory().getItemCountByItemId(182200218) > 0)
						{
							defaultCloseDialog(env, 0, 1, true, false, 0, 0, 182200216, 1);
							defaultQuestRemoveItem(env, 182200218, 1);
							return sendQuestDialog(env, 1523);
						}
						else
							return sendQuestDialog(env, 1608);
					case 10001:
						if (player.getInventory().getItemCountByItemId(182200219) > 0)
						{
							defaultCloseDialog(env, 0, 2, true, false, 0, 0, 182200216, 1);
							defaultQuestRemoveItem(env, 182200219, 1);
							return sendQuestDialog(env, 1438);
						}
						else
							return sendQuestDialog(env, 1608);
					case 10002:
						if (player.getInventory().getItemCountByItemId(182200220) > 0)
						{
							defaultCloseDialog(env, 0, 3, true, false, 0, 0, 182200216, 1);
							defaultQuestRemoveItem(env, 182200220, 1);
							return sendQuestDialog(env, 1353);
						}
						else
							return sendQuestDialog(env, 1608);
				}
			}
		}
		return defaultQuestRewardDialog(env, 790001, 0, var-1);
	}
}
