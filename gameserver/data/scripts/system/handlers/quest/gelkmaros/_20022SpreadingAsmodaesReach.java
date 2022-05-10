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
package quest.gelkmaros;

import java.util.Collections;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author Nephis
 * 
 */
public class _20022SpreadingAsmodaesReach extends QuestHandler
{

	private final static int	questId	= 20022;
	private final static int[]	npc_ids	= {799226, 799282, 700701, 700702, 700703, 700704};

	public _20022SpreadingAsmodaesReach()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(216102).addOnKillEvent(questId);
		qe.setNpcQuestData(216103).addOnKillEvent(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 799226)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 799226)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 700703)
		{
			if (var == 2)
				return true;
		}
		else if(targetId == 700704)
		{
			if (var == 2)
				return true;
		}
		else if(targetId == 799282)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1352);
					else if(var == 2)
						return sendQuestDialog(env, 1693);
					else if(var == 4)
						return sendQuestDialog(env, 2375);
					else if(var == 7)
						return sendQuestDialog(env, 3398);
					else if(var == 9)
						return sendQuestDialog(env, 4080);
				case 34:
					if(var == 2)
					{
						if(QuestService.collectItemCheck(env, true))
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
						else
							return sendQuestDialog(env, 10001);
					}
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}	
					break;	
				case 10004:
					if(var == 4)
					{
						if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182207608, 1))))
							return true;
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					break;	
				case 10007:
					if(var == 7)
					{
						if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182207609, 1))))
							return true;
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					break;	
				case 10255:
					if(var == 9)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}						
			}
		}
		else if(targetId == 700701)
		{
			if (qs.getQuestVarById(0) == 5 && env.getDialogId() == -1)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
					1));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
					targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						if(!player.isTargeting(targetObjectId))
							return;
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
							targetObjectId, 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
							targetObjectId), true);
						final QuestState qs = player.getQuestStateList().getQuestState(questId);
						qs.setQuestVar(6);
						updateQuestStatus(env);
					}
				}, 3000);
			}
			else if (qs.getQuestVarById(0) == 8 && env.getDialogId() == -1)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
					1));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
					targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						if(!player.isTargeting(targetObjectId))
							return;
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
							targetObjectId, 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
							targetObjectId), true);
						final QuestState qs = player.getQuestStateList().getQuestState(questId);
						player.getInventory().removeFromBagByItemId(182207609, 1);
						qs.setQuestVar(9);
						updateQuestStatus(env);
					}
				}, 3000);
			}
		}
		
		else if(targetId == 700702)
		{
			if (qs.getQuestVarById(0) == 6 && env.getDialogId() == -1)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
					1));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
					targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						if(!player.isTargeting(targetObjectId))
							return;
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
							targetObjectId, 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
							targetObjectId), true);
						final QuestState qs = player.getQuestStateList().getQuestState(questId);
						player.getInventory().removeFromBagByItemId(182207608, 1);
						qs.setQuestVar(7);
						updateQuestStatus(env);
					}
				}, 3000);
			}
		}
		return false;
	}	
	
   @Override
   public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
        targetId = ((Npc) env.getVisibleObject()).getNpcId();

		switch(targetId)
		{
			case 216102:
			case 216103:
				if(qs.getQuestVarById(1) < 4 && qs.getQuestVarById(0) == 3)
				{
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
					return true;
				} 
				
				else if(qs.getQuestVarById(1) == 4 && qs.getQuestVarById(0) == 3)
				{
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
		}
			return false;
    }
}
