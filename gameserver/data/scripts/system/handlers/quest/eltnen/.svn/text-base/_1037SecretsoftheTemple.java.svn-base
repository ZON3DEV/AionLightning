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
package quest.eltnen;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Rhys2002
 * fixed by kecimis
 * 
 */
public class _1037SecretsoftheTemple extends QuestHandler
{
	private final static int	questId	= 1037;
	private final static int[]	npc_ids	= {203965, 203967, 700151, 700154, 700150, 700153, 700152};

	public _1037SecretsoftheTemple()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203965:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 203967:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
							else if(var == 2)
								return sendQuestDialog(env, 1693);
							else if(var == 3)
								return sendQuestDialog(env, 1695);
						case 1694:
							return defaultQuestItemCheck(env, 2, 3, false, 1694, 1779);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
						case 10002:
							Storage storage = player.getInventory();
							if (storage.getItemCountByItemId(182201027) == 0)
								return defaultCloseDialog(env, 3, 0, 182201027, 1, 0, 0);
							else
								return defaultCloseDialog(env, 3, 0);
					}
					break;
				case 700150:
				case 700151:
				case 700152:
				case 700153:
				case 700154:
					if(var < 3 || var > 7)
						break;
					if(env.getDialogId() == -1)
						return (defaultQuestUseNpc(env, var, var + 1, EmotionType.NEUTRALMODE2, 
												   EmotionType.START_LOOT, false));
					break;
			}			
		}
		return defaultQuestRewardDialog(env, 203965, 2034);
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		boolean update = false;
		
		if(targetId == 700151 && var == 3 || targetId == 700154 && var == 4 ||
		   targetId == 700150 && var == 5 || targetId == 700153 && var == 6)
		{
			qs.setQuestVar(++var);
			update = true;
		}
		else if(targetId == 700152 && var == 7)
		{
			defaultQuestRemoveItem(env, 182201027, 1);
			qs.setStatus(QuestStatus.REWARD);
			update = true;
		}
		
		if (update)
		{
			updateQuestStatus(env);
		}
	}
}