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
package quest.reshanta;

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
 * @author Hilgert
 * 
 */
 
public class _2722TheComfortsofHome extends QuestHandler
{
	private final static int	questId	= 2722;

	public _2722TheComfortsofHome()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(278047).addOnQuestStart(questId);
		qe.setNpcQuestData(278056).addOnTalkEvent(questId);
		qe.setNpcQuestData(278126).addOnTalkEvent(questId);
		qe.setNpcQuestData(278043).addOnTalkEvent(questId);
		qe.setNpcQuestData(278032).addOnTalkEvent(questId);
		qe.setNpcQuestData(278037).addOnTalkEvent(questId);
		qe.setNpcQuestData(278040).addOnTalkEvent(questId);
		qe.setNpcQuestData(278068).addOnTalkEvent(questId);
		qe.setNpcQuestData(278066).addOnTalkEvent(questId);
		qe.setNpcQuestData(278047).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
		int targetId = 0;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 278047)
			{
				if(env.getDialogId() == -1)
					 return sendQuestDialog(env, 4762);
				else if(env.getDialogId() == 1007)
					return sendQuestDialog(env, 4);
				else 
					 return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVarById(0);
			if(targetId == 278056)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}
			else if(targetId == 278126)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10001)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278043)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1693);					 
				else if(env.getDialogId() == 10002)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}
			else if(targetId == 278032)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 2034);					 
				else if(env.getDialogId() == 10003)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278037)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 2375);					 
				else if(env.getDialogId() == 10004)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278040)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 2716);					 
				else if(env.getDialogId() == 10005)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278068)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 3057);					 
				else if(env.getDialogId() == 10006)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278066)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 3398);
					 
				else if(env.getDialogId() == 10255)
				{
					 qs.setStatus(QuestStatus.REWARD);
					 ItemService.addItems(player, Collections.singletonList(new QuestItems(182205654, 1)));
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD && targetId == 278047)
		{
			return defaultQuestEndDialog(env);
		}
		return false;
    }		
}
