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
package quest.pandaemonium;

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

public class _2916ManInTheLongBlackRobe extends QuestHandler
{
	private final static int	questId	= 2916;
	private final static int[]	npcs = {204141, 204152, 204150, 204151, 798033, 203673, 700211};
	
	public _2916ManInTheLongBlackRobe()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204141).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 204141))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204152:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 204150:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1693);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
					}
					break;
				case 204151:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 2034);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
					}
					break;
				case 798033:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2375);
						case 10003:
							return defaultCloseDialog(env, 3, 4);
					}
					break;
				case 203673:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 2716);
						case 10004:
							return defaultCloseDialog(env, 4, 5);
					}
					break;
				case 204141:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 6)
								return sendQuestDialog(env, 3057);
						case 34:
							return defaultQuestItemCheck(env, 6, 7, true, 5, 3143);
					}
					break;
				case 700211:
					return defaultQuestUseNpc(env, 5, 6, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
			}
		}
		return defaultQuestRewardDialog(env, 204141, 0);
	}
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		switch(var)
		{
			case 5:
				qs.setQuestVar(6);
				updateQuestStatus(env);
				break;
		}
	}
}
