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
package quest.heiron;

import java.util.Collections;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.ZoneService;
import org.openaion.gameserver.skill.effect.EffectId;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.zone.ZoneName;


/**
 * @author Rhys2002
 * 
 */
public class _1059TheArchonofStorms extends QuestHandler
{

	private final static int	questId	= 1059;
	private final static int[]	npc_ids	= { 204505, 204533, 700282, 204535 };

	public _1059TheArchonofStorms()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setQuestMovieEndIds(193).add(questId);
		qe.setQuestItemIds(182201619).add(questId);		
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

		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204505)
				return defaultQuestEndDialog(env);
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 204505)
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
					return false;
			}
		}
		else if(targetId == 204533)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1352);
					else if(var == 3)
						return sendQuestDialog(env, 2034);						
				case 10001:
					if(var == 1)
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
					return false;
			}
		}
		else if(targetId == 204535)
		{
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
						ItemService.addItems(player, Collections.singletonList(new QuestItems(182201619, 1)));					
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
			}
		}
		else if(targetId == 700282 && var == 2)
		{
			if (env.getDialogId() == -1)
			{
			final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 193));
					}
				}, 3000);
			}
		}				
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestCookie env, int movieId)
	{
		if(movieId != 193)
			return false;
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 2)
			return false;
		qs.setQuestVar(3);
		updateQuestStatus(env);
			player.getEffectController().setAbnormal(EffectId.SHAPECHANGE.getEffectId());
			player.setTransformedModelId(212000);
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player));
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
						public void run()
						{
							player.getEffectController().unsetAbnormal(EffectId.SHAPECHANGE.getEffectId());
							player.setTransformedModelId(0);
							PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player));
						}
				}, 15000);
		
		return true;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if(id != 182201619 || qs == null)
			return HandlerResult.UNKNOWN;
		
		if(!ZoneService.getInstance().isInsideZone(player, ZoneName.PATEMA_GEYSER))
			return HandlerResult.FAILED;

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 192));
				player.getInventory().removeFromBagByItemId(182201619, 1);				
				qs.setQuestVarById(0, 5);
				qs.setStatus(QuestStatus.REWARD);				
				updateQuestStatus(env);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}	
}
