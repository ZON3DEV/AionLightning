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
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author MrPoke remod By Sylar, Rolandas
 * 
 */
public class _1483HarumonerksRequest extends QuestHandler
{
	private final static int	questId	= 1483;

	public _1483HarumonerksRequest()
	{
		super(questId);
	}
	
    @Override
	public void register()
	{
		qe.setNpcQuestData(798126).addOnQuestStart(questId);
		qe.setNpcQuestData(798126).addOnTalkEvent(questId);
		qe.setNpcQuestData(203940).addOnTalkEvent(questId);
		qe.setNpcQuestData(203944).addOnTalkEvent(questId);
		qe.setNpcQuestData(798127).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if (defaultQuestNoneDialog(env, 798126))
			return true;
		
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		Player player = env.getPlayer();
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203940:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1, false, false, 182201401, 1, 0, 0);
					}
				break;
				case 203944:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1693);
						case 10001:
							return defaultCloseDialog(env, 1, 2, false, false, 182201402, 1, 0, 0);
					}
				break;
				case 798127:
					switch(env.getDialogId())
					{
						case -1:
							if(var == 2)
								return sendQuestDialog(env, 2375);
						case 1009:
							if (player.getInventory().getItemCountByItemId(182201401) > 0 &&
								player.getInventory().getItemCountByItemId(182201402) > 0)
							{
								defaultQuestRemoveItem(env, 182201401, 1);
								defaultQuestRemoveItem(env, 182201402, 1);
								return defaultCloseDialog(env, 2, 0, true, true);
							}
							return sendQuestDialog(env, 2375);
					}
				break;	
			}
		}

		return defaultQuestRewardDialog(env, 798127, 0);
	}
}
