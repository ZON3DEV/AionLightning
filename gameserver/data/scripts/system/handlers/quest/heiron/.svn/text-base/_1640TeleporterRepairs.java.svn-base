/*
 *  This file is part of Aion-Core Extreme <http://www.aion-core.net>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Aion-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Aion-Core Extreme.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import java.util.Collections;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.WorldMapType;

/**
 * @author Orpheo
 */
 
public class _1640TeleporterRepairs extends QuestHandler
{
	private final static int	questId	= 1640;
	
	public _1640TeleporterRepairs()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(730033).addOnQuestStart(questId);
		qe.setNpcQuestData(730033).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 730033)
			{
				switch(env.getDialogId())
				{
					case 26:
					{
						return sendQuestDialog(env, 1011);
					}
					case 10000:
					{
						QuestService.startQuest(env, QuestStatus.START);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
						return true;
					}
					default: return defaultQuestStartDialog(env);
				}
			}
		}
		
		if(qs == null)
			return false;
			
		if (qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 730033 && env.getDialogId() == -1 && player.getInventory().getItemCountByItemId(182201790) >= 1)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.SIT, 0, targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						if(!player.isTargeting(targetObjectId))
							return;
						
						qs.setStatus(QuestStatus.REWARD);
						//updateQuestStatus(env);
					}
				}, 3000);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 730033)
			{
				player.getInventory().removeFromBagByItemId(182201790, 1);
				
				if(qs == null || qs.getStatus() != QuestStatus.REWARD)
				{
					return false;
				}
				int rewardExp = player.getRates().getQuestXpRate() * 538200;
				int rewardKinah = player.getRates().getQuestXpRate() * 10100;
				player.getCommonData().addExp(rewardExp);
				ItemService.addItems(player, Collections.singletonList(new QuestItems(182400001, rewardKinah)));
				qs.setStatus(QuestStatus.COMPLETE);
				qs.setCompliteCount(255);
				updateQuestStatus(env);
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(5, questId, QuestStatus.COMPLETE, 2));
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
		}
		else if (qs.getStatus() == QuestStatus.COMPLETE)
		{
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					TeleportService.teleportTo(player, WorldMapType.HEIRON.getId(), 187.71689f, 2712.14870f, 141.91672f, 195);
				}
			}, 1000);
		}
		return false;
	}
}