/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.sanctum;

import java.util.Collections;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;


/* @author edynamic90 */

public class _1901KrallicPotion extends QuestHandler
{
	private final static int	questId	= 1901;

	public _1901KrallicPotion()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203830).addOnQuestStart(questId);
		qe.setNpcQuestData(203830).addOnTalkEvent(questId);
		qe.setNpcQuestData(798026).addOnTalkEvent(questId);
		qe.setNpcQuestData(798025).addOnTalkEvent(questId);
		qe.setNpcQuestData(203131).addOnTalkEvent(questId);
		qe.setNpcQuestData(798003).addOnTalkEvent(questId);
		qe.setNpcQuestData(203864).addOnTalkEvent(questId);
		/* qe.setQuestItemIds(182204115).add(questId); */
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 203830) // marmeia
		{
			if(env.getDialogId() == 26)
				return sendQuestDialog(env, 1011);
			else
				return defaultQuestStartDialog(env);
		}
		else
		{
			if(targetId == 203864)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 1009)
				{
					qs.setQuestVar(7);
					updateQuestStatus(env);
					qs.setStatus(QuestStatus.REWARD);
					return defaultQuestEndDialog(env);
				}
				else if(qs != null && qs.getStatus() == QuestStatus.REWARD)
				{
					if(env.getDialogId() == -1)
						return sendQuestDialog(env, 3398);
					return defaultQuestEndDialog(env);
				}
			}
			else if(qs != null)
			{
				if(qs.getStatus() == QuestStatus.START)
				{
					int var = qs.getQuestVarById(0);
					switch(targetId)
					{
						case 798026:// kunberunerk
							switch(env.getDialogId())
							{
								case 26:
									if(var == 0)
										return sendQuestDialog(env, 1352);
									else if(var == 5)
										return sendQuestDialog(env, 3057);
								case 1438:
									Storage inventory = player.getInventory();
									Item KinahsItemPlayer = inventory.getKinahItem();
									long KinahsPlayer = KinahsItemPlayer.getItemCount();
									PacketSendUtility.sendMessage(player, Long.toString(KinahsPlayer));
									if(KinahsPlayer >= 10000)
									{
										KinahsItemPlayer.decreaseItemCount(10000);
										PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(KinahsItemPlayer));
										qs.setQuestVarById(0, var + 1);
										updateQuestStatus(env);
										PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env
											.getVisibleObject().getObjectId(), 10));
										return true;
									}
									else
										return sendQuestDialog(env, 1523);
								case 10000:// oui 10000
									qs.setQuestVarById(0, var + 1);// var==1
									updateQuestStatus(env);
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
										.getObjectId(), 10));
									return true;
								case 10001:// non
									qs.setQuestVarById(0, var + 1);// var==1
									updateQuestStatus(env);
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
										.getObjectId(), 10));
									return true;
								case 10006:
									qs.setQuestVarById(0, var + 1);// var==5
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
										.getObjectId(), 10));
									return true;
								default:
									return defaultQuestStartDialog(env);
							}
						case 798025:// mapireck
							switch(env.getDialogId())
							{
								case 26:
									if(var == 1)
										return sendQuestDialog(env, 1693);
									else if(var == 4)
										return sendQuestDialog(env, 2716);
								case 10002:
									qs.setQuestVarById(0, var + 1);// var==2
									updateQuestStatus(env);
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
										.getObjectId(), 10));
									return true;
								case 10005:
									player.getInventory().removeFromBagByItemId(182206000, 1);
									qs.setQuestVarById(0, var + 1);// var==5
									updateQuestStatus(env);
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
										.getObjectId(), 10));
									return true;
							}
						case 203131:// maniparas
							switch(env.getDialogId())
							{
								case 26:
									return sendQuestDialog(env, 2034);
								case 10003:
									qs.setQuestVarById(0, var + 1);// var==3
									updateQuestStatus(env);
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
										.getObjectId(), 10));
									return true;
							}
						case 798003:// gaphyrk
							switch(env.getDialogId())
							{
								case 26:
									return sendQuestDialog(env, 2375);
								case 10004:
									if(player.getInventory().getItemCountByItemId(182206000) == 0)
										if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(
											182206000, 1))))
											return true;
									qs.setQuestVarById(0, var + 1);// var==4
									updateQuestStatus(env);
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
										.getObjectId(), 10));
									return true;
							}
					}
				}
			}
			return false;
		}
	}
}
