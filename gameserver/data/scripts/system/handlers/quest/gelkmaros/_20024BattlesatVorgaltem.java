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

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Nephis
 * 
 */
public class _20024BattlesatVorgaltem extends QuestHandler
{

	private final static int	questId	= 20024;
	private final static int[]	npc_ids	= { 799226, 799308, 799298, 798713, 700707 };

	public _20024BattlesatVorgaltem()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(216104).addOnKillEvent(questId); 
		qe.setNpcQuestData(216101).addOnKillEvent(questId); 
		qe.setNpcQuestData(216104).addOnKillEvent(questId); 
		qe.setNpcQuestData(216109).addOnKillEvent(questId); 
		qe.setNpcQuestData(216112).addOnKillEvent(questId); 
		qe.setNpcQuestData(216107).addOnKillEvent(questId); 	
		qe.setNpcQuestData(216033).addOnKillEvent(questId); 
		qe.setNpcQuestData(216034).addOnKillEvent(questId); 
		qe.setNpcQuestData(216448).addOnKillEvent(questId);		
		qe.setNpcQuestData(216450).addOnKillEvent(questId);
		qe.setNpcQuestData(216451).addOnKillEvent(questId);
		qe.setNpcQuestData(216108).addOnKillEvent(questId);
		qe.setNpcQuestData(216449).addOnKillEvent(questId);
		qe.setNpcQuestData(700811).addOnKillEvent(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
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
			case 216034:
			case 216033:
				if(qs.getQuestVarById(1) < 10 && qs.getQuestVarById(0) == 2)
				{
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
					return true;
				} 
				else if(qs.getQuestVarById(1) == 10 && qs.getQuestVarById(2) == 30 && qs.getQuestVarById(0) == 2)
				{
					qs.setQuestVar(3);
					updateQuestStatus(env);
					return true;
				}
				break;
			case 216448:	
			case 216450:	
			case 216451:	
			case 216108:	
			case 216449:	
			case 216107:	
			case 216112:	
			case 216109:	
			case 216104:	
			case 216101:				
				if(qs.getQuestVarById(2) < 30 && qs.getQuestVarById(0) == 2)
				{
					qs.setQuestVarById(2, qs.getQuestVarById(2) + 1);
					updateQuestStatus(env);
					return true;
				}
				else if(qs.getQuestVarById(1) == 10 && qs.getQuestVarById(2) == 30 && qs.getQuestVarById(0) == 2)
				{
					qs.setQuestVar(3);
					updateQuestStatus(env);
					return true;
				}
				break;
			case 700811:				
				if(qs.getQuestVarById(0) == 8)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return true;
				}
		}
			return false;
    }

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
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
		else if(targetId == 799308)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1352);
					else if(var == 2)
						return sendQuestDialog(env, 2375);
					else if(var == 3)
						return sendQuestDialog(env, 2034);
				break;
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				break;
				case 10003:
					if(var == 3)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				break;
				case 10004:
					if(var == 2)
					{
						qs.setQuestVar(3);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				break;
			}
		}
		else if(targetId == 799298)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 4)
						return sendQuestDialog(env, 2375);
					else if(var == 5)
						return sendQuestDialog(env, 2716);
					else if(var == 7)
						return sendQuestDialog(env, 3398);
					else if(var == 9)
						return sendQuestDialog(env, 4080);
					else if(var == 11)
						return sendQuestDialog(env, 1608);
				break;
				case 34:
					if(var == 5)
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
				case 10004:
					if(var == 4)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10007:
					if(var == 7)
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
				case 10255:
					if(var == 11)
					{
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 554));
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		
		else if(targetId == 798713)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 6)
						return sendQuestDialog(env, 3057);
				case 10006:
					if(var == 6)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		
		else if(targetId == 700707)
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
					}
			}
		}
		
		return false;
	}	
}
