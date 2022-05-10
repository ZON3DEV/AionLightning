/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.beluslan;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;


/**
 * @author XRONOS
 *
 */
public class _2620SummoningPhagrasul extends QuestHandler
{

	private final static int	questId	= 2620;
	private final static int[]	npcs = {204787, 700323, 204824};
	private final static int[]	mob_ids	= {213109, 213111};
	private Npc npc;
	
	public _2620SummoningPhagrasul()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204787).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
		for (int mob_id : mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 204787, 4762))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204824:
					switch(env.getDialogId())
					{
						case 26:
							if (var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							if(defaultCloseDialog(env, 0, 1))
								npc.getController().onDelete();
					}
					break;
				case 700323:
					return defaultQuestUseNpc(env, 0, 0, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
			}
		}
		return defaultQuestRewardDialog(env, 204787, 10002);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		if(qs.getQuestVarById(0) == 1)
		{
			if(defaultQuestOnKillEvent(env, 213109, 0, 5, 1) || defaultQuestOnKillEvent(env, 213111, 0, 5, 2))
			{
				if(qs.getQuestVarById(1) == 5 && qs.getQuestVarById(2) == 5)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		npc = (Npc)QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 204824, 2854.90f, 154.49f, 304.28f, (byte) 47, false);
	}
}
