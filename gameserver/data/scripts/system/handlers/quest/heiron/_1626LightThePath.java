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

public class _1626LightThePath extends QuestHandler
{
	private final static int	questId	= 1626;
	private final static int[]	npcs = {204592, 700221, 700222, 700223, 700224, 700225, 700226, 700227};
	
	public _1626LightThePath()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204592).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 204592, 182201788, 1))
			return true;

		if(qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch (env.getTargetId())
			{
				case 700221:
					return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700222:
					return defaultQuestUseNpc(env, 1, 2, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700223:
					return defaultQuestUseNpc(env, 2, 3, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700224:
					return defaultQuestUseNpc(env, 3, 4, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700225:
					return defaultQuestUseNpc(env, 4, 5, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700226:
					return defaultQuestUseNpc(env, 5, 6, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700227:
					return defaultQuestUseNpc(env, 6, 7, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
			}
		}
		return defaultQuestRewardDialog(env, 204592, 10002);
	}
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		switch(var)
		{
			case 0:
				qs.setQuestVar(1);
				break;
			case 1:
				qs.setQuestVar(2);
				break;
			case 2:
				qs.setQuestVar(3);
				break;
			case 3:
				qs.setQuestVar(4);
				break;
			case 4:
				qs.setQuestVar(5);
				break;
			case 5:
				qs.setQuestVar(6);
				break;
			case 6:
				qs.setStatus(QuestStatus.REWARD);
				break;
		}
		updateQuestStatus(env);
	}
}
