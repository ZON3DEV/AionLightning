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
package quest.altgard;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author XRONOS
 *
 */
public class _2289RampagingMosbears extends QuestHandler
{

	private final static int	questId	= 2289;
	private final static int[]	mob_ids	= {210564, 210584};
	
	public _2289RampagingMosbears()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203616).addOnQuestStart(questId);
		qe.setNpcQuestData(203616).addOnTalkEvent(questId);
		qe.setNpcQuestData(203618).addOnTalkEvent(questId);
		for(int mob_id : mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
	
		if(defaultQuestNoneDialog(env, 203616))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203616:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 5)
								return sendQuestDialog(env, 1352);
							else if(var == 7)
								return sendQuestDialog(env, 2034);
						case 10001:
							return defaultCloseDialog(env, 5, 6);
						case 34:
							return defaultQuestItemCheck(env, 7, 0, true, 5, 2120);
					}
				}
				break;
				case 203618:
				{
					switch(env.getDialogId())
					{
						case 26:
							if (var == 6)
								return sendQuestDialog(env, 1693);
						case 10002:
							return defaultCloseDialog(env, 6, 7);
					}
				}
				break;
			}
		}
		return defaultQuestRewardDialog(env, 203616, 0);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		return defaultQuestOnKillEvent(env, mob_ids, 0, 5);
	}
}
