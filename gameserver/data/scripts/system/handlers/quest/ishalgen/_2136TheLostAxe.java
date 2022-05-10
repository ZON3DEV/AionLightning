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
package quest.ishalgen;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author Rhys2002, modified by Hellboy
 * 
 */
public class _2136TheLostAxe extends QuestHandler
{
	private final static int	questId	= 2136;
	private final static int[]	npc_ids	= { 700146, 790009 };

	public _2136TheLostAxe()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(env.getDialogId() == 1002)
			{
				QuestService.startQuest(env, QuestStatus.START);				
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
			else
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
		}
			
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 790009)
			{
				final Npc npc = (Npc)env.getVisibleObject();
				ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
					public void run()
						{
							npc.getController().onDelete();	
						}
				}, 10000);
				return defaultQuestEndDialog(env, qs.getQuestVarById(1));
			}
		}
		else if(qs.getStatus() != QuestStatus.START)
			return false;

		if(targetId == 790009)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1011);
				case 10000: // one axe, reward index 0
					if(var == 1)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						player.getInventory().removeFromBagByItemId(182203130, 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return sendQuestDialog(env, 6);
					}						
				case 10001: // 3 axes, reward index 1
					if(var == 1)
					{
						qs.setStatus(QuestStatus.REWARD);
						qs.setQuestVarById(1, 1);
						updateQuestStatus(env);
						player.getInventory().removeFromBagByItemId(182203130, 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return sendQuestDialog(env, 5);
					}
			}
		}
		else if(targetId == 700146)
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var == 0)
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						final int instanceId = player.getInstanceId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
								public void run()
							{
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
								PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 59));
								qs.setQuestVarById(0, 1);
								updateQuestStatus(env);
								QuestService.addNewSpawn(220010000, instanceId, 790009, 1088.5f, 2371.8f, 258.375f, (byte) 87, true);
							}
						}, 3000);
					}	
				return true;
			}
		}
		return false;
	}
}