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
package quest.beluslan;

import java.util.Collections;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.ZoneService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.zone.ZoneName;


/**
 * @author Rhys2002
 * 
 */
public class _2056ThawingKurngalfberg extends QuestHandler
{
	
	private final static int	questId	= 2056;
	private final static int[]	npc_ids	= { 204753, 790016, 730036, 279000 };

	public _2056ThawingKurngalfberg()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setQuestItemIds(182204313).add(questId);
		qe.setQuestItemIds(182204314).add(questId);		
		qe.setQuestItemIds(182204315).add(questId);
		qe.addQuestLvlUp(questId);
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
			if(targetId == 204753)
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
		if(targetId == 204753)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
					else if(var == 1)
						return sendQuestDialog(env, 2375);
				case 1012:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 242));
						break;
				case 2376:
					if(QuestService.collectItemCheck(env, false))				
						return sendQuestDialog(env, 2376);
					else
						return sendQuestDialog(env, 2461);						
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10004:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}					
			}
		}
		else if(targetId == 790016)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 2034);
				case 2035:
					if(var == 1 && player.getInventory().getItemCountByItemId(182204315) != 1)
					{
						ItemService.addItems(player, Collections.singletonList(new QuestItems(182204315, 1)));
						return sendQuestDialog(env, 2035);
					}
					else 
						return sendQuestDialog(env, 2120);	
			}
		}
		else if(targetId == 730036)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1352);				
				case 1353:
					if(var == 1 && player.getInventory().getItemCountByItemId(182204313) != 1)
					{
						ItemService.addItems(player, Collections.singletonList(new QuestItems(182204313, 1)));
						return sendQuestDialog(env, 1353);
					}
					else 
						return sendQuestDialog(env, 1438);						
			}
		}		
		else if(targetId == 279000)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1693);
				case 1694:
					if(var == 1 && player.getInventory().getItemCountByItemId(182204314) != 1)
					{
						ItemService.addItems(player, Collections.singletonList(new QuestItems(182204314, 1)));
						return sendQuestDialog(env, 1694);
					}
					else 
						return sendQuestDialog(env, 1779);						
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		
		if (id != 182204313 && id != 182204314 && id != 182204315)
			return HandlerResult.UNKNOWN;
		
		if(!ZoneService.getInstance().isInsideZone(player, ZoneName.THE_SACRED_ORCHARD_220040000))
			return HandlerResult.FAILED;
			
		if(id != 182204313 && qs.getQuestVarById(0) == 2 || id != 182204314 && 
		   qs.getQuestVarById(0) == 3 || id != 182204315 && qs.getQuestVarById(0) == 4)
			return HandlerResult.FAILED;

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 2000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
				if(qs.getQuestVarById(0) == 2)
				{
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 243));
					player.getInventory().removeFromBagByItemId(id, 1);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
				}
				else if(qs.getQuestVarById(0) == 3)
				{
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 244));
					player.getInventory().removeFromBagByItemId(id, 1);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
				}
				else if(qs.getQuestVarById(0) == 4)
				{
					player.getInventory().removeFromBagByItemId(id, 1);
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 245));
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			}
		}, 2000);
		return HandlerResult.SUCCESS;
	}	
}
