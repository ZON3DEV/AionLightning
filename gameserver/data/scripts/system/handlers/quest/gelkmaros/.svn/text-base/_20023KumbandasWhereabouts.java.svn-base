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

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.SystemMessageId;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.WorldMapInstance;



/**
 * @author Nephis
 * 
 */
public class _20023KumbandasWhereabouts extends QuestHandler
{

	private final static int	questId	= 20023;
	private final static int[]	npc_ids	= { 799226, 799292, 700810, 204057, 730243, 799513, 799341, 700706, 799515 };

	public _20023KumbandasWhereabouts()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addOnDie(questId);
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(216592).addOnKillEvent(questId);
		qe.setNpcQuestData(730243).addOnActionItemEvent(questId);
		qe.setQuestMovieEndIds(442).add(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}
	
	@Override
	public boolean onDieEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		int var = qs.getQuestVars().getQuestVars();
		if(var == 8)
		{
			qs.setQuestVar(7);
			updateQuestStatus(env);
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
		}

		return false;
	}
	
	@Override
	public boolean onActionItemEvent(QuestCookie env)
	{
	  return (env.getTargetId() == 730243);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
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
			case 216592:
				if(qs.getQuestVarById(0) == 8)
				{
					@SuppressWarnings("unused")
					final int instanceId = player.getInstanceId();
					QuestService.addNewSpawn(300150000, player.getInstanceId(), 799341, (float) 561.8763, (float) 192.25128, (float) 135.88919, (byte) 30, true);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return true;
				} 
		}
			return false;
    }
	
	@Override
	public boolean onMovieEndEvent(QuestCookie env, int movieId)
	{
		if(movieId != 442)
			return false;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 8)
			return false;
		QuestService.addNewSpawn(300150000, player.getInstanceId(), 216592, (float) 561.8763, (float) 192.25128, (float) 135.88919, (byte) 30, true);	
		return true;
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
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
					else if(var == 3)
						return sendQuestDialog(env, 2034);
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10003:
					if(var == 3)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 799292)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1352);
					else if(var == 2)
						return sendQuestDialog(env, 1693);
					else if(var == 5)
						return sendQuestDialog(env, 2716);
					else if(var == 11)
						return sendQuestDialog(env, 1608);
					break;
				case 34:
					if(QuestService.collectItemCheck(env, true))
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
					}
					else
						return sendQuestDialog(env, 10001);	
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}	
				case 10005:
					if(var == 5)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}	
				case 10255:
					if(var == 11)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}					
			}
		}
		else if(targetId == 700810)
		{
			if (var == 2)
				return true;
		}
		else if(targetId == 204057)
		{
			switch(env.getDialogId())
			{		
				case 26:
					if(var == 4)
						return sendQuestDialog(env, 2375);
				case 10004:
					if(var == 4)
					{
						if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182207611, 1))))
							return true;
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 799341)
		{
			switch(env.getDialogId())
			{		
				case 26:
					if(var == 9)
						return sendQuestDialog(env, 4080);
				case 10009:
					if(var == 9)
					{
						if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182207613, 1))))
							return true;
						player.getInventory().removeFromBagByItemId(182207611, 1);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 799513 || targetId == 799514 || targetId == 799515 || targetId == 799516) //FIXME: Var !
		{
			switch(env.getDialogId())
			{		
				case 26:
					if(var == 7)
						return sendQuestDialog(env, 4080);
				case 10009:
					if(var == 7)	
					{
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 442));
						qs.setQuestVar(8);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		
		else if(targetId == 730243)
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var >= 6)
						return sendQuestDialog(env, 3057);
				break;
				case 26:
					if(var == 6)
						return sendQuestDialog(env, 3057);
				break;
				case 10006:
					if(var == 6)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300150000);
						InstanceService.registerPlayerWithInstance(newInstance, player);
						TeleportService.teleportTo(player, 300150000, newInstance.getInstanceId(), 561.8651f, 221.91483f, 134.53333f, (byte) 90);
								return true;
					}
					else if(var > 6)
					{
						WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300150000);
						InstanceService.registerPlayerWithInstance(newInstance, player);
						TeleportService.teleportTo(player, 300150000, newInstance.getInstanceId(), 561.8651f, 221.91483f, 134.53333f, (byte) 90);
							return true;
					}
			}
		}
		else if(targetId == 700706)
		{
			switch(env.getDialogId())
			{		
				case -1:
					if (var == 10)
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
						qs.setQuestVar(11);
						updateQuestStatus(env);
						TeleportService.teleportTo(player, 220070000, 2572.4648f, 1773.1288f, 315.74646f, (byte) 46);
					}
			}
		}

		return false;
	}	
}
