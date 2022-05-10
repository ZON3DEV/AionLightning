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
package quest.sanctum;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author Ritsu
 * 
 */
public class _1099AnImportantChoice extends QuestHandler
{	
	private final static int	questId	= 1099;
	private final static int[]	mob_ids	= { 215399, 215398, 215397, 215396, 211043 }; //Orissan Legionary, Orissan
	
	public _1099AnImportantChoice()
	{
		super(questId);
	}
	@Override
	public void register()
	{
		qe.setNpcQuestData(790001).addOnTalkEvent(questId); //Pernos
		qe.setNpcQuestData(700551).addOnTalkEvent(questId); //Fissure of Destiny to open gate
		qe.setNpcQuestData(205119).addOnTalkEvent(questId); //Hermione
		qe.setNpcQuestData(700552).addOnTalkEvent(questId); //Artifact Of Memory
		qe.setNpcQuestData(203700).addOnTalkEvent(questId); //Fasimedes
		qe.addQuestLvlUp(questId);
		for(int mob_id : mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}
		
		@Override
		public boolean onLvlUpEvent(QuestCookie env)
		{
			Player player = env.getPlayer();
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			QuestState qs2 = player.getQuestStateList().getQuestState(1098);
			
			if(qs == null)
			{
				if(player.getCommonData().getLevel() < 50)
					return false;
				
				else if(qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
					return false;	
			
				env.setQuestId(questId);
				QuestService.startQuest(env, QuestStatus.START);
				return true;
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

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		Npc npc = null;
		if(env.getVisibleObject() instanceof Npc)
		{
			npc = (Npc) env.getVisibleObject();
			targetId = npc.getNpcId();
		}
		switch(targetId)
		{
			case 215399:
			case 215398:
			case 215397:
			case 215396:
				if(var >= 2 && var < 51)
				{
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					return true;
				}
				
				else if(var >= 51)
				{
					QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 211043, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true);
					qs.setQuestVar(52);
					updateQuestStatus(env);
					return true;
				}
		}
		
		switch(targetId)
		{
			case 211043:
				if(var == 52) //Orissan
				{
					qs.setQuestVar(53);
					updateQuestStatus(env);
					return true;
				}
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
		return false;
		
		if(targetId == 790001) //Pernos
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
				{
					qs.setQuestVar(1);
               		updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
					.getObjectId(), 10));
						return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		else if(targetId == 205119) //Hermione
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10001)
				{
					qs.setQuestVar(2); 
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}	
			else if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) > 1)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10001)
				{
					PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}				

		}
		
		else if(targetId == 205118) //Lephar
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 54)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2716);
				else if(env.getDialogId() == 10005)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				
				else if(env.getDialogId() == 10006)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}					

		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203700) //Fasimedes
				return defaultQuestEndDialog(env);
		}
	
		else if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 53)
		{
			switch(targetId)
			{
				case 700552: //Artifact Of Memory
				{
					if (qs.getQuestVarById(0) == 53 && env.getDialogId() == -1)
					{

						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
							1));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
							targetObjectId), true);
						ThreadPoolManager.getInstance().schedule(new Runnable(){
						final QuestState qs = player.getQuestStateList().getQuestState(questId);
							@Override
							public void run()
							{	
								@SuppressWarnings("unused")
								Npc npc = (Npc)player.getTarget();		
								QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 205118, (float) 303.1931, (float) 291.6575, (float) 207.6108, (byte) 70, true);									
								qs.setQuestVar(54); 
								updateQuestStatus(env);
								if(player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
									return;
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
									targetObjectId, 3000, 0));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
									targetObjectId), true);
							}
						}, 3000);
					}
				}
			}
		}
		return false;
	}
}
