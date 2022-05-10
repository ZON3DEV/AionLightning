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
package org.openaion.gameserver.quest.handlers.template;

import java.util.List;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.model.templates.bonus.AbstractInventoryBonus;
import org.openaion.gameserver.model.templates.bonus.BonusTemplate;
import org.openaion.gameserver.model.templates.quest.CollectItem;
import org.openaion.gameserver.model.templates.quest.CollectItems;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.model.templates.quest.QuestWorkItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.handlers.models.WorkOrdersData;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Mr. Poke
 *
 */
public class WorkOrders extends QuestHandler
{
	private final WorkOrdersData workOrdersData;
	/**
	 * @param questId
	 */
	public WorkOrders(WorkOrdersData workOrdersData)
	{
		super(workOrdersData.getId());
		this.workOrdersData = workOrdersData;
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(workOrdersData.getStartNpcId()).addOnQuestStart(workOrdersData.getId());
		qe.setNpcQuestData(workOrdersData.getStartNpcId()).addOnTalkEvent(workOrdersData.getId());
		qe.addOnQuestAbort(workOrdersData.getId());
		qe.addOnQuestFinish(workOrdersData.getId());
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		if(env.getTargetId() != workOrdersData.getStartNpcId())
			return false;
		QuestState qs = player.getQuestStateList().getQuestState(workOrdersData.getId());
		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE)
		{
			switch(env.getDialogId())
			{
				case 26:
					return sendQuestDialog(env, 4);
				case 1002:
					if(player.getInventory().isFull())
					{
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
					else if (QuestService.startQuest(env, QuestStatus.START))
					{
						if (ItemService.addItems(player, workOrdersData.getGiveComponent()))
						{
							player.getRecipeList().addRecipe(player, DataManager.RECIPE_DATA.getRecipeTemplateById(workOrdersData.getRecipeId()));
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						}
						return true;
					}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START)
		{
			if (env.getDialogId() == 26)
				return sendQuestDialog(env, 5);
			else if (env.getDialogId() == 18)
			{
				int questId = env.getQuestId();
				QuestWorkItems qwi = DataManager.QUEST_DATA.getQuestById(questId).getQuestWorkItems();

				if(player.getInventory().isFull())
				{
					boolean failed = true;
					if(qwi != null)
					{
						long count = 0;
						for(QuestItems qi : qwi.getQuestWorkItem())
						{
							count = player.getInventory().getItemCountByItemId(qi.getItemId());
							if(qi.getCount() <= count)
							{
								failed = false; // we can remove all and free a slot
								break;
							}
						}
					}
					if(failed)
					{
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
				}
				if (QuestService.collectItemCheck(env, false))
				{
					QuestTemplate	template = DataManager.QUEST_DATA.getQuestById(questId);
					CollectItems collectItems = template.getCollectItems();
					
					// remove crafted items only when the work order is removed from the dialog;
					// otherwise, leave them for bonus exchange
					int playerSkillPoints = player.getSkillList().getSkillLevel(template.getCombineSkill());
					int craftSkillPoints = template.getCombineSkillPoint();
					if(craftSkillPoints == 1)
						craftSkillPoints = 0;
					long removeCount = 0;

					for (CollectItem collectItem : collectItems.getCollectItem())
					{
						if(!CustomConfig.WORK_ORDER_BONUS || (craftSkillPoints <= playerSkillPoints / 10 * 10 - 4 * 10))
							removeCount = player.getInventory().getItemCountByItemId(collectItem.getItemId());
						else
							removeCount = collectItem.getCount();
						if(!player.getInventory().removeFromBagByItemId(collectItem.getItemId(), removeCount))
							return false;
					}
					
					//remove all worker list item if finished.
					if(qwi != null)
					{
						long count = 0;
						for(QuestItems qi : qwi.getQuestWorkItem())
						{
							if(qi != null)
							{	
								count = player.getInventory().getItemCountByItemId(qi.getItemId());
								if(count > 0)
									player.getInventory().removeFromBagByItemId(qi.getItemId(), count);	
							}
						}
					}
					// always apply bonus, don't check items, unless the bonus depends on the count 
					// of the crafted products
					BonusTemplate bonusTemplate = DataManager.BONUS_DATA.getBonusInfoByQuestId(questId);
					if(bonusTemplate != null)
					{
						List<AbstractInventoryBonus> bi = bonusTemplate.getItemBonuses();
						for (int i = 0; i < bi.size(); i++)
							bi.get(i).apply(player, null);
					}
					qs.setStatus(QuestStatus.COMPLETE);
					abortQuest(env);
					qs.setCompliteCount(qs.getCompleteCount() + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean onQuestFinishEvent(QuestCookie env)
	{
		return true;
	}

	public boolean onQuestAbortEvent(QuestCookie env)
	{
		abortQuest(env);
		return true;
	}
	
	private void abortQuest(QuestCookie env)
	{
		env.getPlayer().getRecipeList().deleteRecipe(env.getPlayer(), workOrdersData.getRecipeId());
	}
}
