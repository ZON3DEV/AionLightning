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
package quest.daevanion_quest;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author HellBoy
 * 
 */
public class _2993AnotherBeginning extends QuestHandler
{
	private final static int	questId	= 2993;
	private final static int	Items[][] =
	{{1013, 1034, 1055, 1076, 5103,
	  1098, 1119, 1140, 1161, 5104,
	  1183, 1204, 1225, 1246, 5105,
	  1268, 1289, 1310, 1331, 5106},
	{110600834, 113600800, 114600794, 112600785, 111600813,
	 110300881, 113300860, 114300893, 112300784, 111300834,
	 110100931, 113100843, 114100866, 112100790, 111100831,
	 110500849, 113500827, 114500837, 112500774, 111500821}};

	public _2993AnotherBeginning()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204076).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		int	removeItem = 0;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = 0;
		
		if(qs != null)
			var = qs.getQuestVarById(0);
		
		if(qs == null || qs.getStatus() == QuestStatus.COMPLETE)
		{
			if(env.getTargetId() == 204076)
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1011);
					case 1013:
					case 1034:
					case 1055:
					case 1076:
					case 5103:
					case 1098:
					case 1119:
					case 1140:
					case 1161:
					case 5104:
					case 1183:
					case 1204:
					case 1225:
					case 1246:
					case 5105:
					case 1268:
					case 1289:
					case 1310:
					case 1331:
					case 5106:
						int i = 0;
						for(int id: Items[0])
						{
							if(id == env.getDialogId())
								break;
							i++;
						}
						removeItem = Items[1][i];
						if(qs == null)
						{
							qs = new QuestState(questId, QuestStatus.COMPLETE, 0, 0);
							player.getQuestStateList().addQuest(questId, qs);
						}
						else
							qs.setStatus(QuestStatus.COMPLETE);
						qs.setQuestVar(i);
						
						if(player.getInventory().getItemCountByItemId(removeItem) > 0)
							return sendQuestDialog(env, 1013);
						else
							return sendQuestDialog(env, 1352);
					case 10000:
					case 10001:
					case 10002:
					case 10003:
						if(player.getInventory().getItemCountByItemId(186000041) > 0)
						{
							if(qs == null)
							{
								qs = new QuestState(questId, QuestStatus.REWARD, 0, 0);
								player.getQuestStateList().addQuest(questId, qs);
							}
							else
								qs.setStatus(QuestStatus.REWARD);
							defaultQuestRemoveItem(env, Items[1][var], 1);
							defaultQuestRemoveItem(env, 186000041, 1);
							var = env.getDialogId() - 10000;
							qs.setQuestVar(var);
							return sendQuestDialog(env, var + 5);
						}
						else
							return sendQuestDialog(env, 1009);
				}
			}
		}
		
		if(qs == null)
			return false;
		
		return defaultQuestRewardDialog(env, 204076, 0, var);
	}
}
