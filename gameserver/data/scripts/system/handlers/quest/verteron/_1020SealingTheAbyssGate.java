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
package quest.verteron;



import java.util.Collection;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.WorldMapInstance;
import org.openaion.gameserver.world.WorldMapType;


/**
 * @author Atomics
 * 
 */
public class _1020SealingTheAbyssGate extends QuestHandler
{
	
	private final static int	questId	= 1020;
	private final static int[]	npcIds	= { 203098, 700089, 700142, 700141 };
	private boolean spawned = false;
	private int instanceId = 0;
	
	public _1020SealingTheAbyssGate()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addOnEnterWorld(questId);
		qe.addQuestLvlUp(questId);
		qe.addOnDie(questId);
		qe.setNpcQuestData(700089).addOnActionItemEvent(questId);//Abyss gate
		qe.setNpcQuestData(700142).addOnActionItemEvent(questId);//Generator
		qe.setNpcQuestData(700141).addOnActionItemEvent(questId);
		for(int npcId : npcIds)
			qe.setNpcQuestData(npcId).addOnTalkEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		int[] quests = {1023, 1022, 1021, 1019, 1018, 1017, 1016, 1015, 1014, 1013, 1012, 1011};
		return defaultQuestOnLvlUpEvent(env, quests, true);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		long itemCount=0;

		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if( qs.getStatus() == QuestStatus.REWARD )
		{
			if( env.getTargetId() == 203098 ) {
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 1352);
				return defaultQuestEndDialog(env);
			}
		}
		else if( qs.getStatus() != QuestStatus.START )
			return false;
		switch( env.getTargetId() )
		{
			case 203098:
				switch( env.getDialogId() )
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011 );
					case 10000:
						return defaultCloseDialog(env, 0, 1);
					default:
						return false;
				}
			case 700089:
				if(var == 3)
				{
					itemCount = player.getInventory().getItemCountByItemId(182200024);
					if (itemCount >= 1)
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));

						qs.setQuestVarById(0, 4);
						//update status
						qs.setStatus(QuestStatus.REWARD);

						ThreadPoolManager.getInstance().schedule(new Runnable()
						{
							@Override
							public void run()
							{
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
								player.getInventory().removeFromBagByItemId(182200024, 1);
								updateQuestStatus(env);
								TeleportService.teleportTo(player, WorldMapType.VERTERON.getId(), 2684.308f, 1068.7382f, 199.375f, 0);
							}
						}, 3000);
						return true;
					}
				}
			break;
			case 700141:
				if(var == 1 && player.getPlayerGroup() != null)
				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
					//create instance
					WorldMapInstance newInstance = InstanceService.getRegisteredInstance(310030000, player.getPlayerGroup().getGroupId());
					if (newInstance == null)
					{
						newInstance = InstanceService.getNextAvailableInstance(310030000);
						InstanceService.registerGroupWithInstance(newInstance, player.getPlayerGroup());
						instanceId = newInstance.getInstanceId();
						spawned = false;
					}
					
					//update var
					qs.setQuestVarById( 0, var + 1 );
					
					ThreadPoolManager.getInstance().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
							updateQuestStatus(env);
							TeleportService.teleportTo(player, 310030000, instanceId,(float) 270.5,(float) 174.3,(float) 204.3, 0);
						}
					}, 3000);
					return true;
				}
				//TODO: find proper message, just temp fix
				else if (var == 1 && player.getPlayerGroup() == null)
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_PARTY_DON);

				if(var == 2)
				{
					itemCount = player.getInventory().getItemCountByItemId(182200024);
					if (itemCount >= 1)
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						final Npc npc = (Npc)env.getVisibleObject();
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
						ThreadPoolManager.getInstance().schedule(new Runnable()
						{
							@Override
							public void run()
							{
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
								//update quest for all players in instance
								Collection<Player> members = player.getPlayerGroup().getMembers();
								for (Player member : members)
								{
									if (instanceId == member.getInstanceId() && member.getWorldId() == 310030000)
									{
										QuestState qs1 = member.getQuestStateList().getQuestState(questId);
										if (qs1 != null && qs1.getStatus() == QuestStatus.START && qs1.getQuestVarById(0) == 2)
										{
											qs1.setQuestVarById( 0, 3 );
											updateQuestStatus(env);
											PacketSendUtility.sendPacket(member, new SM_PLAY_MOVIE(0, 153));
										}
									}
								}
								npc.getController().onDespawn(false);
							}
						}, 3000);
						return true;
					}
				}
				return true;
			case 700142:
				if(var == 2 && spawned == false)
				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
					spawned = true;
					ThreadPoolManager.getInstance().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
							PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT, 0, targetObjectId), true);
							QuestService.addNewSpawn(310030000, player.getInstanceId(), 210753, (float) 258.89917, (float) 237.20166, (float) 217.06035, (byte) 0, true);
							updateQuestStatus(env);
						}
					}, 3000);
					return true;
					
				}
				return false;
		}
		return false;
	}

	@Override
	public boolean onDieEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		int var = qs.getQuestVars().getQuestVars();
		if(var == 2 || var == 3)
		{
			qs.setQuestVar(1);
			player.getInventory().removeFromBagByItemId(182200024, 1);
			updateQuestStatus(env);
		}

		return false;
	}
		
	@Override
	public boolean onEnterWorldEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVars().getQuestVars();
			if(var == 2 || var == 3)
			{
				if(player.getWorldId() != 310030000)
				{
					qs.setQuestVar(1);
					player.getInventory().removeFromBagByItemId(182200024, 1);
					updateQuestStatus(env);
				}
			}
		}
		
		return false;
	}
	public boolean onActionItemEvent(QuestCookie env)
	{
	  return true;
	}
}
