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
package quest.ascension;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_TELEPORT_LOC;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author MrPoke
 *
 */
public class _2009ACeremonyinPandaemonium extends QuestHandler
{
	private final static int	questId	= 2009;

	public _2009ACeremonyinPandaemonium()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		if(CustomConfig.ENABLE_SIMPLE_2NDCLASS)
			return;
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(203550).addOnTalkEvent(questId);
		qe.setNpcQuestData(204182).addOnTalkEvent(questId);
		qe.setNpcQuestData(204075).addOnTalkEvent(questId);
		qe.setNpcQuestData(204080).addOnTalkEvent(questId);
		qe.setNpcQuestData(204081).addOnTalkEvent(questId);
		qe.setNpcQuestData(204082).addOnTalkEvent(questId);
		qe.setNpcQuestData(204083).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVars().getQuestVars();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 203550)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
					case 10000:
						if (var == 0)
						{
							qs.setQuestVar(1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(),0));
							
							PacketSendUtility.sendPacket(player, new SM_TELEPORT_LOC(120010000, 1685, 1400, 195));
							TeleportService.scheduleTeleportTask(player, 120010000, 1685, 1400, 195);
							return true;
						}
				}
			}
			else if (targetId == 204182)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 1)
							return sendQuestDialog(env, 1352);
					case 1353:
						if(var == 1)
						{
							PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 121));
							return false;
						}
					case 10001:
						if(var == 1)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
				}
			}
			else if (targetId == 204075)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 2)
							return sendQuestDialog(env, 1693);
					case 1694:
						if(var == 2)
						{
							PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 122));
							return false;
						}
					case 10002:
						if(var == 2)
						{
							PlayerClass playerClass = PlayerClass.getStartingClassFor(player.getCommonData().getPlayerClass());
							if (playerClass == PlayerClass.WARRIOR)
								qs.setQuestVar(10);
							else if (playerClass == PlayerClass.SCOUT)
								qs.setQuestVar(20);
							else if (playerClass == PlayerClass.MAGE)
								qs.setQuestVar(30);
							else if (playerClass == PlayerClass.PRIEST)
								qs.setQuestVar(40);
							updateQuestStatus(env);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204080 && var == 10)
			{
				switch (env.getDialogId())
				{
					case -1:
						return sendQuestDialog(env, 2034);
					case 1009:
						return sendQuestDialog(env, 5);
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
					case 16:
					case 17:
					case 18:
						if (QuestService.questFinish(env, 0))
						{
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 2200, env.getDialogId()), QuestStatus.START);
							QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 29004, env.getDialogId()), QuestStatus.START);
							return true;
						}
				}
			}
			else if (targetId == 204081 && var == 20)
			{
				switch (env.getDialogId())
				{
					case -1:
						return sendQuestDialog(env, 2375);
					case 1009:
						return sendQuestDialog(env, 6);
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
					case 16:
					case 17:
					case 18:
						if (QuestService.questFinish(env, 1))
						{
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 2200, env.getDialogId()), QuestStatus.START);
							QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 29004, env.getDialogId()), QuestStatus.START);
							return true;
						}
				}
			}
			else if (targetId == 204082 && var == 30)
			{
				switch (env.getDialogId())
				{
					case -1:
						return sendQuestDialog(env, 2716);
					case 1009:
						return sendQuestDialog(env, 7);
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
					case 16:
					case 17:
					case 18:
						if (QuestService.questFinish(env, 2))
						{
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 2200, env.getDialogId()), QuestStatus.START);
							QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 29004, env.getDialogId()), QuestStatus.START);
							return true;
						}
				}
			}
			else if (targetId == 204083 && var == 40)
			{
				switch (env.getDialogId())
				{
					case -1:
						return sendQuestDialog(env, 3057);
					case 1009:
						return sendQuestDialog(env, 8);
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
					case 16:
					case 17:
					case 18:
						if (QuestService.questFinish(env, 3))
						{
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 2200, env.getDialogId()), QuestStatus.START);
							QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), 29004, env.getDialogId()), QuestStatus.START);
							return true;
						}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, false);
	}
}
