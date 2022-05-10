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
package quest.morheim;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.TeleportService;


/**
 * @author XRONOS
 *
 */
public class _2423ThereAndBackAgain extends QuestHandler
{

	private final static int	questId	= 2423;
	private final static int[]	mob_ids	= {211682};
	
	public _2423ThereAndBackAgain()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204326).addOnQuestStart(questId);
		qe.setNpcQuestData(204375).addOnTalkEvent(questId);
		for(int mob_id : mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
	
		if(defaultQuestNoneDialog(env, 204326, 4762))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204375:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 1)
								return sendQuestDialog(env, 1352);
							else if(var == 2)
								return sendQuestDialog(env, 1693);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
						case 10002:
							if(defaultCloseDialog(env, 2, 3, true, false))
							{
								TeleportService.teleportTo(player, 210020000, 1, 565, 2516, 329, (byte)90, 0);
								return true;
							}
						case 34:
							return defaultQuestItemCheck(env, 1, 2, false, 10000, 10001);
					}
				}
			}
		}
		return defaultQuestRewardDialog(env, 204375, 10002);
	}
}