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

import java.util.Collections;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Manu72
 *
 */
public class _2098ButWhatweMake extends QuestHandler
{

	private final static int	questId	= 2098;

	public _2098ButWhatweMake()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(203550).addOnQuestStart(questId); //Munin
		qe.setNpcQuestData(203550).addOnTalkEvent(questId); //Munin
		qe.setNpcQuestData(204361).addOnTalkEvent(questId); //Hreidmar
		qe.setNpcQuestData(204408).addOnTalkEvent(questId); //Bulagan
		qe.setNpcQuestData(205198).addOnTalkEvent(questId); //Cayron
		qe.setNpcQuestData(204805).addOnTalkEvent(questId); //Vanargand
		qe.setNpcQuestData(204808).addOnTalkEvent(questId); //Esnu
		qe.setNpcQuestData(203546).addOnTalkEvent(questId); //Skuld
		qe.setNpcQuestData(204387).addOnTalkEvent(questId); //Ananta
		qe.setNpcQuestData(205190).addOnTalkEvent(questId); //Seznec
		qe.setNpcQuestData(204207).addOnTalkEvent(questId); //Kasir
		qe.setNpcQuestData(204301).addOnTalkEvent(questId); //Aegir
		qe.setNpcQuestData(205155).addOnTalkEvent(questId); //Heintz
		qe.setNpcQuestData(204784).addOnTalkEvent(questId); //Delris
		qe.setNpcQuestData(278001).addOnTalkEvent(questId); //Votan
		qe.setNpcQuestData(204053).addOnTalkEvent(questId); //Kvasir
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
						if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182207089, 1))));	
						return true;
					}

				else
					return defaultQuestStartDialog(env);
			}
			else if(qs != null && qs.getStatus() == QuestStatus.REWARD) //Reward
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
				{
					qs.setQuestVar(14);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return defaultQuestEndDialog(env);
				}
				else
					return defaultQuestEndDialog(env);
			}
		}
		
		else if(targetId == 204361) //Hreidmar
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
		
		else if(targetId == 204408) //Bulagan
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1693);
				else if(env.getDialogId() == 10002)
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
		
		else if(targetId == 205198) //Cayron
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 3)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2034);
				else if(env.getDialogId() == 10003)
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
		
		else if(targetId == 204805) //Vanargand
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 4)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 10004)
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
		
		else if(targetId == 204808) //Esnu
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 5)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2716);
				else if(env.getDialogId() == 10005)
				{
					player.getInventory().removeFromBagByItemId(182207089, 1);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182207090, 1))));	
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		else if(targetId == 203546) //Skuld
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 6)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 3057);
				else if(env.getDialogId() == 10006)
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

		else if(targetId == 204387) //Ananta
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 7)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 3398);
				else if(env.getDialogId() == 10007)
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
		
		else if(targetId == 205190) //Seznec
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 8)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 3739);
				else if(env.getDialogId() == 10008)
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

		else if(targetId == 204207) //Kasir
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 9)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4080);
				else if(env.getDialogId() == 10009)
				{
					player.getInventory().removeFromBagByItemId(182207090, 1);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182207091, 1))));	
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		else if(targetId == 204301) //Aegir
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 10)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1608);
				else if(env.getDialogId() == 10010)
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
		
		else if(targetId == 205155) //Heintz
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 11)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1949);
				else if(env.getDialogId() == 10011)
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

		else if(targetId == 204784) //Delris
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 12)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2290);
				else if(env.getDialogId() == 10012)
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
		
		else if(targetId == 278001) //Votan
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 13)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2631);
				else if(env.getDialogId() == 10013)
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

		else if(targetId == 204053) //Kvasir
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 14)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2972);
				else if(env.getDialogId() == 10255)
				{
					player.getInventory().removeFromBagByItemId(182207091, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182207092, 1))));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		return false;

	}
}
