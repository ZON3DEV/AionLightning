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

import java.util.Collections;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
* @author Rhys2002 + Nephis
*
*/
public class _1016SourceOfThePollution extends QuestHandler
{

	private final static int  	 questId   = 1016;
	private final static int[]   npc_ids   = { 203149, 203148, 203832, 203705, 203822, 203761, 203098, 203195 };

	public _1016SourceOfThePollution()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(210318).addOnKillEvent(questId);
		for(int npc_id : npc_ids)	
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203098)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 4080);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 203149:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 2)
								return sendQuestDialog(env, 1693);
							else if(var == 7)
								return sendQuestDialog(env, 3398);
							else if(var == 8)
								{
									if(player.getInventory().getItemCountByItemId(182200015) == 0)
										return sendQuestDialog(env, 3484);	
									else
										return sendQuestDialog(env, 3569);
								}
						case 3400:
							PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 28));
								break;
						case 10000:
						case 10002:
							if(var == 0 || var == 2)
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
								player.getInventory().removeFromBagByItemId(182200013, 1);
								player.getInventory().removeFromBagByItemId(182200014, 1);
								if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(182200015, 2))));//add slime immunity medicine
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						case 10008:
							if(var == 8)
							{
								if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(182200015, 2))));//add slime immunity medicine
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}							
					}
				case 203148:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 10001:
							if(var == 1)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(182200017, 1))));//add contaminated water
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				case 203832:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10003:
							if(var == 3)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(182200013, 1))));//add guide to rare poisons
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				case 203705:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 2375);
						case 10004:
							if(var == 4)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				case 203822:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 5)
								return sendQuestDialog(env, 2716);
						case 10005:
							if(var == 5)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								player.getInventory().removeFromBagByItemId(182200017, 1);
								if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(182200018, 1))));//add letter of introduction								
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				case 203761:
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
								player.getInventory().removeFromBagByItemId(182200018, 1);
								if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(182200014, 1))));//add Yustiel's Tear							
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				case 203195:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 9)
								return sendQuestDialog(env, 3739);
						case 10008:
							if(var == 9)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								player.getInventory().removeFromBagByItemId(182200015, 2);
								if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(182200016, 1))));//add Poison research diary									
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								final Npc npc = (Npc)env.getVisibleObject();
								ThreadPoolManager.getInstance().schedule(new Runnable(){
									@Override
										public void run()
										{
											npc.getController().onDelete();	
										}
						}, 40000);								
								return true;
							}
					}					
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 210318, 8, 9))
		{
			final Player player = env.getPlayer();
			final Npc npc = (Npc)env.getVisibleObject();	
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
					public void run()
					{										
						QuestService.addNewSpawn(210030000, player.getInstanceId(), 203195, (float) npc.getX(), (float) npc.getY(), (float) npc.getZ(), (byte) 0, true);
					}
			}, 5000);
			return true;
		}
		else
			return false;		
	}
}