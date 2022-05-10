/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.ishalgen;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Mr. Poke
 *
 */
public class _2114TheInsectProblem extends QuestHandler
{
	private final static int	questId	= 2114;

	public _2114TheInsectProblem()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203533).addOnQuestStart(questId);
		qe.setNpcQuestData(203533).addOnTalkEvent(questId);
		qe.setNpcQuestData(210734).addOnKillEvent(questId);
		qe.setNpcQuestData(210380).addOnKillEvent(questId);
		qe.setNpcQuestData(210381).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 203533)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				switch (env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1011);
					case 10000:
						if (QuestService.startQuest(env, QuestStatus.START))
						{
							qs = player.getQuestStateList().getQuestState(questId);
							qs.setQuestVar(1);
							this.updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					case 10001:
						if (QuestService.startQuest(env, QuestStatus.START))
						{
							qs = player.getQuestStateList().getQuestState(questId);
							qs.setQuestVar(11);
							this.updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
				}
			}
			else if (qs.getStatus() == QuestStatus.REWARD)
			{
				int var = qs.getQuestVarById(0);
				switch (env.getDialogId())
				{
					case -1:
						if (var == 10)
							return sendQuestDialog(env, 5);
						else if (var == 20)
							return sendQuestDialog(env, 6);
					case 17:
						if (QuestService.questFinish(env, var/10-1))
						{
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		int[] mobs = {210380, 210381};
		if(defaultQuestOnKillEvent(env, 210734, 1, 10) || defaultQuestOnKillEvent(env, 210734, 10, true) || defaultQuestOnKillEvent(env, mobs, 11, 20) || defaultQuestOnKillEvent(env, mobs, 20, true))
			return true;
		else
			return false;
	}
}
