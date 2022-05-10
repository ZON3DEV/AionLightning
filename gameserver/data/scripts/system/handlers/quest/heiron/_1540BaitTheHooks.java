/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author XRONOS
 *
 */

public class _1540BaitTheHooks extends QuestHandler
{
	private final static int	questId	= 1540;
	private final static int[]	npcs = {204588, 730189, 730190, 730191};
	
	public _1540BaitTheHooks()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204588).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 204588, 182201822, 1))
			return true;

		if(qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch (env.getTargetId())
			{
				case 730189:
					switch(env.getDialogId())
					{
						case 10000:
							return defaultCloseDialog(env, 0, 1);
						case -1:
							return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
					}
					break;
				case 730190:
					switch(env.getDialogId())
					{
						case 10001:
							return defaultCloseDialog(env, 1, 2);
						case -1:
							return defaultQuestUseNpc(env, 1, 2, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
					}
					break;
				case 730191:
					switch(env.getDialogId())
					{
						case 10002:
							return defaultCloseDialog(env, 2, 3, true, false);
						case -1:
							return defaultQuestUseNpc(env, 2, 3, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 204588, 2375);
	}
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		switch (env.getTargetId())
		{
			case 730189:
				sendQuestDialog(env, 1352);
				break;
			case 730190:
				sendQuestDialog(env, 1693);
				break;
			case 730191:
				sendQuestDialog(env, 2034);
				break;
		}
	}
}
