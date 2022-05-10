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
package quest.inggison;

import java.util.Collections;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.WorldMapInstance;


/**
 * @author Nephis
 * 
 */
public class _10023SullasStartlingDiscovery extends QuestHandler
{

	private final static int	questId	= 10023;
	private final static int[]	npc_ids	= {798928, 798975, 798981, 730226, 730227, 730228, 798513, 798225, 798979, 798990, 730295, 700604, 730229};

	public _10023SullasStartlingDiscovery()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(216531).addOnKillEvent(questId);
		qe.setNpcQuestData(730295).addOnActionItemEvent(questId);
		qe.setQuestItemIds(182206614).add(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}
	
	@Override
	public boolean onActionItemEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null)
		{
			int var = qs.getQuestVarById(0);
			return (var == 12 && env.getTargetId() == 730295 || var == 16 && env.getTargetId() == 730229);
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null || id != 182206614)
			return HandlerResult.UNKNOWN;
		if(qs.getQuestVarById(0) != 16)
			return HandlerResult.FAILED;
		
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
				player.getInventory().removeFromBagByItemId(182206614, 1);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
			}
		}, 1000);
		return HandlerResult.SUCCESS;
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
			if(targetId == 798928)
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
		if(targetId == 798928)
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
		else if(targetId == 798975)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1352);
					else if(var == 6)
						return sendQuestDialog(env, 3057);
					else if(var == 9)
						return sendQuestDialog(env, 4080);
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					break;
				case 10006:
					if(var == 6)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}	
				case 10009:
					if(var == 9)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}						
			}
		}
		
		else if(targetId == 798981)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 2)
						return sendQuestDialog(env, 1693);
				case 10002:
					if(var == 2)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;						
					}					
			}
		}
		
		else if(targetId == 798513)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 7)
						return sendQuestDialog(env, 3398);
				case 10007:
					if(var == 7)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;						
					}					
			}
		}
		
		else if(targetId == 798225)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 8)
						return sendQuestDialog(env, 3739);
				case 10008:
					if(var == 8)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;						
					}					
			}
		}
		
		else if(targetId == 798979)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 10)
						return sendQuestDialog(env, 1608);
				case 10010:
					if(var == 10)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;						
					}					
			}
		}
		
		else if(targetId == 798990)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 11)
						return sendQuestDialog(env, 1949);
				case 10011:
					if(var == 11)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;						
					}					
			}
		}
		
		else if(targetId == 730295)
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var >= 12)
						return sendQuestDialog(env, 3995);
				break;
				case 26:
					if(var == 12)
						return sendQuestDialog(env, 3995);
				break;
				case 10012:
					if(var == 12)
					{
						if(player.getInventory().getItemCountByItemId(182206613) > 0)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							player.getInventory().removeFromBagByItemId(182206613, 1);
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300160000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService.teleportTo(player, 300160000, newInstance.getInstanceId(), 744.761f, 940.3738f, 149.28333f, (byte) 91);
								return true;
						}
						else
							return sendQuestDialog(env, 10001);	
					}
					else if(var > 12)
					{
						if(player.getInventory().getItemCountByItemId(182206613) > 0)
						{
							player.getInventory().removeFromBagByItemId(182206613, 1);
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300160000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService.teleportTo(player, 300160000, newInstance.getInstanceId(), 744.761f, 940.3738f, 149.28333f, (byte) 91);
								return true;
						}
						else
							return sendQuestDialog(env, 10001);	
					}
			}
		}
		
		else if(targetId == 730229)
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var == 15)
					{
						if(ItemService.addItems(player, Collections.singletonList(new QuestItems(182206614, 1))))
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
								return true;
						}
					}
				break;
			}
		}
		
		else if(targetId == 730226)
		{
			if (qs.getQuestVarById(0) == 3 && env.getDialogId() == -1)
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
						qs.setQuestVar(4);
						updateQuestStatus(env);
					}
				}, 3000);
			}
		}
		else if(targetId == 730227)
		{
			if (qs.getQuestVarById(0) == 4 && env.getDialogId() == -1)
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
						qs.setQuestVar(5);
						updateQuestStatus(env);
					}
				}, 3000);
			}
		}
		else if(targetId == 730228)
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
		}
		else if(targetId == 700604)
		{
			if (qs.getQuestVarById(0) == 13 && env.getDialogId() == -1)
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
						qs.setQuestVar(14);
						updateQuestStatus(env);
					}
				}, 3000);
			}
		}
		else if(targetId == 700603)
		{
			if (qs.getQuestVarById(0) == 16 && env.getDialogId() == -1)
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
						TeleportService.teleportTo(player, 210050000, 349.332f, 1368.0781f, 336.43332f, 100);
					}
				}, 3000);
			}
		}
		return false;
	}	
	
	@Override
	public boolean onKillEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final int instanceId = player.getInstanceId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		final Npc npc = (Npc)env.getVisibleObject();

		switch(env.getTargetId())
		{
			case 216531:
				if(qs.getQuestVarById(0) == 14 || qs.getQuestVarById(0) == 15)
				{
					final float x = npc.getX();
					final float y = npc.getY();
					final float z = npc.getZ();
					ThreadPoolManager.getInstance().schedule(new Runnable(){
						@Override
						public void run()
						{
							if(qs.getQuestVarById(0) == 14 || qs.getQuestVarById(0) == 15)
							{
								QuestService.addNewSpawn(300160000, instanceId, 730229, x, y, z, (byte)71, true);
								qs.setQuestVarById(0, 15);
								updateQuestStatus(env);
							}
						}
					}, 3000);
					return true;
				} 
		}
			return false;
    }
}
