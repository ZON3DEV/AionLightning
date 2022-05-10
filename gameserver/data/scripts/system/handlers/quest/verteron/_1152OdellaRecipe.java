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

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;


/**
 * @author Rolandas
 * 
 */
public class _1152OdellaRecipe extends QuestHandler
{
	private final static int	questId	= 1152;

	public _1152OdellaRecipe()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203132).addOnQuestStart(questId);
		qe.setNpcQuestData(203132).addOnTalkEvent(questId);
		qe.setNpcQuestData(203130).addOnTalkEvent(questId);
		qe.setQuestSellBuyItemIds(169400112).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(env.getTargetId() == 203132)
		{
			if(qs == null)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if (env.getDialogId() == 1007)
					return sendQuestDialog(env, 4);
				else
					return defaultQuestStartItem(env, 182200526, 1, 0, 0);
			}
		}
		
		if (qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(env.getTargetId() == 203130)
		{
			if(qs != null)
			{
				var = qs.getQuestVarById(0);
				if (qs.getStatus() == QuestStatus.START && var == 0)
				{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10000)
					return defaultCloseDialog(env, 0, 1, 0, 0, 182200526, 1);
				else
					return defaultQuestStartDialog(env);
				} 
				else if (qs.getStatus() == QuestStatus.REWARD)
				{
					if(env.getDialogId() == 26)
						return sendQuestDialog(env, 2375);
					else if(env.getDialogId() == 1009)
						return defaultCloseDialog(env, 2, 2, true, true, 0, 0, 0, 169400112, 1);
					else
						return defaultQuestEndDialog(env);
				}
				else
					return defaultQuestItemCheck(env, 1, 2, false, 2375, 2716);
			}
		}
		return false;
	}
	
	@Override
	public boolean onItemSellBuyEvent(QuestCookie env, int itemId)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		if (itemId != 169400112)
			return false;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		env.setQuestId(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE)
			return false;
		
		if (QuestService.collectItemCheck(env, false))
		{
			qs.setQuestVar(2);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
		}
		else
		{
			qs.setQuestVar(1);
			qs.setStatus(QuestStatus.START);
			updateQuestStatus(env);
		}
		return true;
	}
}
