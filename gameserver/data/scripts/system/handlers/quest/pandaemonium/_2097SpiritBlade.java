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
package quest.pandaemonium;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Manu72, edited by Rolandas
 *
 */
public class _2097SpiritBlade extends QuestHandler
{

	private final static int	questId	= 2097;

	public _2097SpiritBlade()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(203550).addOnQuestStart(questId); //Munin
		qe.setNpcQuestData(203550).addOnTalkEvent(questId); //Munin
		qe.setNpcQuestData(203546).addOnTalkEvent(questId); //Skuld
		qe.setNpcQuestData(279034).addOnTalkEvent(questId); //Baoninerk
		qe.setNpcQuestData(700509).addOnTalkEvent(questId); //Shining Box
		qe.setNpcQuestData(700510).addOnTalkEvent(questId); //Balaur Supply Box
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, false);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 203550) //Munin
		{
			if(qs == null || qs.getStatus() == QuestStatus.START)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
					{
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
						.getObjectId(), 10));
						return true;
					}

				else
					return defaultQuestStartDialog(env);
			}
			else if(qs != null && qs.getStatus() == QuestStatus.REWARD) //Reward
			{
				if (env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if (env.getDialogId() == 1009)
					defaultQuestRemoveItem(env, 182207085, 1);
				return defaultQuestEndDialog(env);
			}
		}
		
		else if(targetId == 203546) //Skuld
		{

			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10001)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}

		}
		
		else if(targetId == 279034) //Baoninerk
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2)
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1693);
					case 34:
						return defaultQuestItemCheck(env, 2, 0, true, 10000, 10001, 182207085, 1);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 700509: //Shining Box
				case 700510: //Balaur Supply Box
				{
					if (qs.getQuestVarById(0) == 2 && env.getDialogId() == -1)
						return true;
				}
			}
		}
		return false;
	}
}
