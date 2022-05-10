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
package org.openaion.gameserver.quest.handlers.template;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.NpcType;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.NpcTemplate;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.model.templates.quest.CollectItem;
import org.openaion.gameserver.model.templates.quest.CollectItems;
import org.openaion.gameserver.model.templates.quest.QuestDrop;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author MrPoke
 * 
 */
public class ItemCollecting extends QuestHandler
{

	private final int	questId;
	private final int	startNpcId;
	private final int	actionItemId;
	private final int	endNpcId;
	private final int	readableItemId;
	Timestamp f = null;

	private static final Logger	log		= Logger.getLogger(ItemCollecting.class);
	
	/**
	 * @param questId
	 * @param startNpcId
	 * @param endNpcId
	 * @param actionItemId
	 */
	public ItemCollecting(int questId, int startNpcId, int actionItemId, int endNpcId, int readableItemId)
	{
		super(questId);
		this.questId = questId;
		this.startNpcId = startNpcId;
		this.actionItemId = actionItemId;
		if (endNpcId != 0)
			this.endNpcId = endNpcId;
		else
			this.endNpcId = startNpcId;
		this.readableItemId = readableItemId;
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(startNpcId).addOnQuestStart(questId);
		qe.setNpcQuestData(startNpcId).addOnTalkEvent(questId);
		NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(startNpcId);
		if (template == null)
			log.warn("Q" + questId + " has invalid start NPC");
		else if (template.getNpcType() == NpcType.USEITEM)
			qe.setNpcQuestData(startNpcId).addOnActionItemEvent(questId);
		if(actionItemId != 0)
			qe.setNpcQuestData(actionItemId).addOnTalkEvent(questId);
		if (endNpcId != startNpcId)
			qe.setNpcQuestData(endNpcId).addOnTalkEvent(questId);
		if(readableItemId != 0)
			qe.setQuestItemIds(readableItemId).add(questId);
	}
	
	@Override
	public boolean onActionItemEvent(QuestCookie env)
	{
		return (startNpcId == env.getTargetId());
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		boolean daily = false;
		
		if (env.getQuestId() > 90000)
		{
			Npc npc = (Npc)env.getVisibleObject();
			if (npc.getObjectTemplate().getNpcType() == NpcType.USEITEM &&
				env.getDialogId() == -1)
			{
				int openDialogId = 1011;
				return sendQuestDialog(env, openDialogId);
			}
			return false;	
		}
		
		if(qs == null && !QuestService.canStart(env))
			return false;
		
		if(player.getGuild().getCurrentQuest() == env.getQuestId())
			daily = true;
		
		if(defaultQuestStartDaily(env))
			return true;
		if(defaultQuestNoneDialog(env, startNpcId))
			return true;
		
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(env.getQuestVarNum());
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == endNpcId || (env.getTargetId() == startNpcId && daily))
			{
				int actionId = 26;
				Npc npc = (Npc) env.getVisibleObject();
				if (npc.getObjectTemplate().getNpcType() == NpcType.USEITEM)
					actionId = -1;
				if (env.getDialogId() == actionId)
				{
					if(var == 0)
						return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == 34)
				{
					return defaultQuestItemCheck(env, 0, 1, true, 5, 2716);
				}
			}
			else if(env.getTargetId() == actionItemId && env.getTargetId() != 0 && itemCollected(env, actionItemId))
				return true;
		}
		if(defaultQuestRewardDialog(env, endNpcId, 0) || (defaultQuestRewardDialog(env, startNpcId, 0) && daily))
			return true;
		else
			return false;
	}
	
	private boolean itemCollected(QuestCookie env, int actionItemId)
	{
		Player player = env.getPlayer();
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(env.getQuestId());
		List<QuestDrop> drops = template.getQuestDrop();
		int collectedItemId = 0;
		int count = 0;

		if (drops.isEmpty())
			return false;

		for (QuestDrop drop: drops)
		{
			if (drop.getNpcId() == actionItemId)
				collectedItemId = drop.getItemId();
		}

		if (collectedItemId == 0)
			return false;

		CollectItems collectitems = template.getCollectItems();
		if (collectitems != null)
		{
			List<CollectItem> collectitem = collectitems.getCollectItem();
			if (collectitem != null)
			{
				for (CollectItem ci : collectitem)
				{
					if (ci.getItemId() == collectedItemId)
					{
						count = ci.getCount();
					}
				}
			}
		}

		if (player.getInventory().getItemCountByItemId(collectedItemId) < count)
			return true;
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if(item.getItemId() != readableItemId)
			return HandlerResult.UNKNOWN;

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
			}
		}, 1000);
		return HandlerResult.FAILED; // we did that
	}
}
