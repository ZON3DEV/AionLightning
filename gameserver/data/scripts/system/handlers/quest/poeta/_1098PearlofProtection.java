/*
	This file is part of aion-engine <aion-engine.org>.

	aion-engine is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	aion-engine is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with aion-engine. If not, see <http://www.gnu.org/licenses/>.
*/
package quest.poeta;

import java.util.Collections;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Orpheo
 *
 */
public class _1098PearlofProtection extends QuestHandler
{

	private final static int	questId = 1098;

	public _1098PearlofProtection()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(790001).addOnQuestStart(questId); //Pernos
		qe.setNpcQuestData(790001).addOnTalkEvent(questId); //Pernos
		qe.setNpcQuestData(730008).addOnTalkEvent(questId); //Daminu
		qe.setNpcQuestData(730019).addOnTalkEvent(questId); //Lodas
		qe.setNpcQuestData(730133).addOnTalkEvent(questId); //Arbolu
		qe.setNpcQuestData(203183).addOnTalkEvent(questId); //Khidia
		qe.setNpcQuestData(203989).addOnTalkEvent(questId); //Tumblusen
		qe.setNpcQuestData(798155).addOnTalkEvent(questId); //Atropos
		qe.setNpcQuestData(204549).addOnTalkEvent(questId); //Aphesius
		qe.setNpcQuestData(203752).addOnTalkEvent(questId); //Jucleas
		qe.setNpcQuestData(203164).addOnTalkEvent(questId); //Morai
		qe.setNpcQuestData(203917).addOnTalkEvent(questId); //Gaia
		qe.setNpcQuestData(203996).addOnTalkEvent(questId); //Kimeia
		qe.setNpcQuestData(798176).addOnTalkEvent(questId); //Jamanok
		qe.setNpcQuestData(798212).addOnTalkEvent(questId); //Serimnir
		qe.setNpcQuestData(204535).addOnTalkEvent(questId); //Maximus
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.LOCKED)
			return false;

		QuestState qs2 = player.getQuestStateList().getQuestState(1097);
		if(qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
			return false;
		qs.setStatus(QuestStatus.START);
		updateQuestStatus(env);
		return true;
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 790001) //Pernos
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
					{
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
						.getObjectId(), 10));
						if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206062, 1))));
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

		else if(targetId == 730008) //Daminu
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

		else if(targetId == 730019) //Lodas
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

		else if(targetId == 730133) //Arbolu
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 3)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2034);
				else if(env.getDialogId() == 10003)
				{
					player.getInventory().removeFromBagByItemId(182206062, 1);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206063, 1))));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}

		else if(targetId == 203183) //Khidia
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

		else if(targetId == 203989) //Tumblusen
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 5)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2716);
				else if(env.getDialogId() == 10005)
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

		else if(targetId == 798155) //Atropos
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

		else if(targetId == 204549) //Aphesius
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

		else if(targetId == 203752) //Jucleas
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 8)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 3739);
				else if(env.getDialogId() == 10008)
				{
					player.getInventory().removeFromBagByItemId(182206063, 1);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206064, 1))));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}

		else if(targetId == 203164) //Morai
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 9)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4080);
				else if(env.getDialogId() == 10009)
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

		else if(targetId == 203917) //Gaia
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

		else if(targetId == 203996) //Kimeia
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

		else if(targetId == 798176) //Jamanok
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

		else if(targetId == 798212) //Serimnir
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

		else if(targetId == 204535) //Maximus
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 14)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2972);
				else if(env.getDialogId() == 10255)
				{
					player.getInventory().removeFromBagByItemId(182206064, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206065, 1))));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		return false;
	}
}