/*
 * This file is part of aion-unique.
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
package quest.master_craft;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


public class _19038CooksPotential extends QuestHandler
{
	private final static int	questId	= 19038;
	private final static int	questStartNpc = 203784;
	private final static int	secondNpc = 203785;
	private final static int[]	recipesItemIds = {152202200, 152202201, 152202202, 152202203};
	private final static int[]	recipesIds = {155002239, 155002240, 155002241, 155002242};
	private final static int[]	products = {182206773, 182206774, 182206775, 182206776};

	public _19038CooksPotential()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, questStartNpc, 4762))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case secondNpc:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 1)
								return sendQuestDialog(env, 4081);
							else if(var == 3)
								return sendQuestDialog(env, 1352);
							else if(var == 4)
								return sendQuestDialog(env, 4166);
							else if(var == 6)
								return sendQuestDialog(env, 1693);
							else if(var == 7)
								return sendQuestDialog(env, 4251);
							else if(var == 9)
								return sendQuestDialog(env, 2034);
							else if(var == 10)
								return sendQuestDialog(env, 4336);
						case 10009:
							if(player.getInventory().decreaseKinah(6500))
								return defaultCloseDialog(env, var, 2, recipesItemIds[0], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
						case 10019:
							if(player.getInventory().decreaseKinah(6500))
								return defaultCloseDialog(env, var, 5, recipesItemIds[1], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
						case 10029:
							if(player.getInventory().decreaseKinah(6500))
								return defaultCloseDialog(env, var, 8, recipesItemIds[2], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
						case 10039:
							if(player.getInventory().decreaseKinah(6500))
								return defaultCloseDialog(env, var, 11, recipesItemIds[3], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
					}
					break;
				case questStartNpc:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1097);
							else if(var == 5)
								return sendQuestDialog(env, 1438);
							else if(var == 8)
								return sendQuestDialog(env, 1779);
							else if(var == 11)
								return sendQuestDialog(env, 2120);
						case 10010:
							if(var == 2)
							{
								if(player.getInventory().getItemCountByItemId(products[0]) > 0)
								{
									player.getInventory().removeFromBagByItemId(products[0], 1);
									qs.setQuestVar(3);
									updateQuestStatus(env);
									return sendQuestDialog(env, 1182);
								}
								else
								{
									int checkFailId = 3398;
									if(player.getRecipeList().isRecipePresent(recipesIds[0]))
										checkFailId = 2716;
									else if(player.getInventory().getItemCountByItemId(recipesItemIds[0]) > 0)
										checkFailId = 3057;
									
									if(checkFailId == 3398)
									{
										qs.setQuestVar(1);
										updateQuestStatus(env);
									}
									return sendQuestDialog(env, checkFailId);
								}
							}
							break;
						case 10020:
							if(var == 5)
							{
								if(player.getInventory().getItemCountByItemId(products[1]) > 0)
								{
									player.getInventory().removeFromBagByItemId(products[1], 1);
									qs.setQuestVar(6);
									updateQuestStatus(env);
									return sendQuestDialog(env, 1523);
								}
								else
								{
									int checkFailId = 3398;
									if(player.getRecipeList().isRecipePresent(recipesIds[1]))
										checkFailId = 2716;
									else if(player.getInventory().getItemCountByItemId(recipesItemIds[1]) > 0)
										checkFailId = 3057;
									
									if(checkFailId == 3398)
									{
										qs.setQuestVar(4);
										updateQuestStatus(env);
									}
									return sendQuestDialog(env, checkFailId);
								}
							}
							break;
						case 10030:
							if(var == 8)
							{
								if(player.getInventory().getItemCountByItemId(products[2]) > 0)
								{
									player.getInventory().removeFromBagByItemId(products[2], 1);
									qs.setQuestVar(9);
									updateQuestStatus(env);
									return sendQuestDialog(env, 1864);
								}
								else
								{
									int checkFailId = 3398;
									if(player.getRecipeList().isRecipePresent(recipesIds[2]))
										checkFailId = 2716;
									else if(player.getInventory().getItemCountByItemId(recipesItemIds[2]) > 0)
										checkFailId = 3057;
									
									if(checkFailId == 3398)
									{
										qs.setQuestVar(7);
										updateQuestStatus(env);
									}
									return sendQuestDialog(env, checkFailId);
								}
							}
							break;
						case 10040:
							if(var == 11)
							{
								if(player.getInventory().getItemCountByItemId(products[3]) > 0)
								{
									player.getInventory().removeFromBagByItemId(products[3], 1);
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);
									return sendQuestDialog(env, 5);
								}
								else
								{
									int checkFailId = 3398;
									if(player.getRecipeList().isRecipePresent(recipesIds[3]))
										checkFailId = 2716;
									else if(player.getInventory().getItemCountByItemId(recipesItemIds[3]) > 0)
										checkFailId = 3057;
									
									if(checkFailId == 3398)
									{
										qs.setQuestVar(10);
										updateQuestStatus(env);
									}
									return sendQuestDialog(env, checkFailId);
								}
							}
							break;
					}
			}
		}
		return defaultQuestRewardDialog(env, questStartNpc, 0);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(questStartNpc).addOnQuestStart(questId);
		qe.setNpcQuestData(questStartNpc).addOnTalkEvent(questId);
		qe.setNpcQuestData(secondNpc).addOnTalkEvent(questId);
	}
}