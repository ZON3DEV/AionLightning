/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.pandaemonium;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;

 
/**
 * @author Hellboy
 *
 */
public class _4938WorkoftheFenrissFangs extends QuestHandler
{
	private final static int	questId	= 4938;
	private final static int[]	npcs = {204053, 798367, 798368, 798369, 798370, 798371, 798372, 798373, 798374, 204075};

	public _4938WorkoftheFenrissFangs()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(204053).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 204053, 4762, 182207114, 1))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 798367:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
				}	break;
				case 798368:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
					}
				} break;
				case 798369:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1693);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
					}
				} break;
				case 798370:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10003:
							return defaultCloseDialog(env, 3, 4);
					}
				} break;
				case 798371:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 2375);
						case 10004:
							return defaultCloseDialog(env, 4, 5);
					}
				} break;
				case 798372:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 5)
								return sendQuestDialog(env, 2716);
						case 10005:
							return defaultCloseDialog(env, 5, 6);
					}
				} break;
				case 798373:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 6)
								return sendQuestDialog(env, 3057);
						case 10006:
							return defaultCloseDialog(env, 6, 7);
					}
				} break;
				case 798374:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 7)
								return sendQuestDialog(env, 3398);
						case 10007:
							return defaultCloseDialog(env, 7, 8, 182207115, 1, 182207114, 1);
					}
				} break;
				case 204075:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 8)
								return sendQuestDialog(env, 3739);
						case 10255:
							if(player.getInventory().getItemCountByItemId(186000084) >= 1)
								return defaultCloseDialog(env, 8, 0, true, false, 0, 0, 186000084, 1);
							else
								return sendQuestDialog(env, 3825);
					}
				} break;
			}
		}
		return defaultQuestRewardDialog(env, 204053, 10002);
	}
}
