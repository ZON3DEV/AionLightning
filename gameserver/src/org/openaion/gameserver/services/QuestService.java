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
package org.openaion.gameserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.configs.main.GroupConfig;
import org.openaion.gameserver.dataholders.BonusData;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.QuestsData;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.drop.DropItem;
import org.openaion.gameserver.model.drop.DropTemplate;
import org.openaion.gameserver.model.drop.NpcDropStat;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.model.gameobjects.player.SkillListEntry;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.model.items.ItemId;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.model.templates.bonus.AbstractInventoryBonus;
import org.openaion.gameserver.model.templates.bonus.BonusTemplate;
import org.openaion.gameserver.model.templates.bonus.SimpleCheckItemBonus;
import org.openaion.gameserver.model.templates.quest.CollectItem;
import org.openaion.gameserver.model.templates.quest.CollectItems;
import org.openaion.gameserver.model.templates.quest.QuestDrop;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.model.templates.quest.QuestWorkItems;
import org.openaion.gameserver.model.templates.quest.Rewards;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestStartConditions;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;

/**
 * @author Mr. Poke
 *
 */
public final class QuestService
{
	static QuestsData		questsData = DataManager.QUEST_DATA;
	static BonusData		bonusData = DataManager.BONUS_DATA;
	private static final int[]		ElyosCraftQuest = {1972, 1974, 1976, 1978, 1980, 1982, 3941, 3944, 3947, 3950, 3953, 3956, 19008, 19014, 19020, 19026, 19032, 19038};
	private static final int[]		AsmoCraftQuest = {2972, 2974, 2976, 2978, 2980, 2982, 4945, 4948, 4951, 4954, 4957, 4960, 29008, 29014, 29020, 29026, 29032, 29038};

	public static boolean questFinish(QuestCookie env)
	{
		return questFinish(env, 0);
	}

	public static boolean questFinish(QuestCookie env, int reward)
	{
		Player player = env.getPlayer();
		synchronized(player)
		{
			int id = env.getQuestId();

			QuestState qs = player.getQuestStateList().getQuestState(id);
			if(qs == null || qs.getStatus() != QuestStatus.REWARD)
				return false;

			QuestTemplate template = questsData.getQuestById(id);
			Storage inventory = player.getInventory();

			BonusTemplate bonusTemplate = bonusData.getBonusInfoByQuestId(id);
			List<SimpleCheckItemBonus> appliedBonuses = null;

			if(bonusTemplate != null)
			{
				int failedChecks = 0;
				QuestTemplate	questTemplate = questsData.getQuestById(env.getQuestId());
				// if collectItems not null, the bonus for SimpleCheckItemBonus classes
				// was already applied, then skip them.
				boolean useCheckItems = questTemplate.getCollectItems() == null;

				List<AbstractInventoryBonus> bi = bonusTemplate.getItemBonuses();

				for (int i = 0; i < bi.size(); i++)
				{
					AbstractInventoryBonus bonus = bi.get(i);
					HandlerResult result = QuestEngine.getInstance().onBonusApply(env, i, bonus);
					if(result == HandlerResult.FAILED)
						continue; // bonus can not be applied (reason: not that bonus or failed)

					if(bonus instanceof SimpleCheckItemBonus)
					{
						if(!useCheckItems)
							continue;

						SimpleCheckItemBonus scb = (SimpleCheckItemBonus)bonus;
						if(!scb.canApply(player, env.getQuestId()))
						{
							// inventory full or other reasons
							failedChecks++;
							break;
						}

						if (appliedBonuses == null)
							appliedBonuses = new ArrayList<SimpleCheckItemBonus>();
						appliedBonuses.add(scb);
					}
					else
					{
						bonus.apply(player, null);
					}
				}
				if (failedChecks > 0)
					return false;
				else if (appliedBonuses != null)
				{	
					for (SimpleCheckItemBonus scb : appliedBonuses)
					{
						player.getInventory().removeFromBagByItemId(scb.getCheckedItemId(), scb.getCount());
						scb.apply(player, null);
					}
				}
			}

			Rewards rewards = null;
			if(reward < template.getRewards().size())
				rewards = template.getRewards().get(reward);
			else
				rewards = new Rewards();

			Rewards extRewards = null;
			if (template.getExtRewards() != null &&
				qs.getCompleteCount() == template.getMaxRepeatCount() - 1)
				extRewards = template.getExtRewards().get(0);

			List<QuestItems> questItems = new ArrayList<QuestItems>();
			questItems.addAll(rewards.getRewardItem());
			if (extRewards != null)
				questItems.addAll(extRewards.getRewardItem());

			int dialogId = env.getDialogId();
			if(dialogId != 17 && dialogId != 0)
			{
				if (template.isUseClassReward() == 1)
				{
					QuestItems classRewardItem = null;
					PlayerClass playerClass = player.getCommonData().getPlayerClass();
					switch (playerClass)
					{
						case ASSASSIN :
							classRewardItem = template.getAssassinSelectableReward().get(dialogId - 8);
							break;
						case CHANTER :
							classRewardItem = template.getChanterSelectableReward().get(dialogId - 8);
							break;
						case CLERIC :
							classRewardItem = template.getPriestSelectableReward().get(dialogId - 8);
							break;
						case GLADIATOR :
							classRewardItem = template.getFighterSelectableReward().get(dialogId - 8);
							break;
						case RANGER :
							classRewardItem = template.getRangerSelectableReward().get(dialogId - 8);
							break;
						case SORCERER :
							classRewardItem = template.getWizardSelectableReward().get(dialogId - 8);
							break;
						case SPIRIT_MASTER :
							classRewardItem = template.getElementalistSelectableReward().get(dialogId - 8);
							break;
						case TEMPLAR :
							classRewardItem = template.getKnightSelectableReward().get(dialogId - 8);
							break;
					}
					if (classRewardItem != null)
						questItems.add(classRewardItem);
				}
				else
				{
					QuestItems selectebleRewardItem = null;
					if(rewards != null && !rewards.getSelectableRewardItem().isEmpty())
					{
						selectebleRewardItem = rewards.getSelectableRewardItem().get(dialogId - 8);
						if(selectebleRewardItem != null)
							questItems.add(selectebleRewardItem);
					}
					if (extRewards != null && !extRewards.getSelectableRewardItem().isEmpty())
					{
						selectebleRewardItem = extRewards.getSelectableRewardItem().get(dialogId - 8);
						if(selectebleRewardItem != null)
							questItems.add(selectebleRewardItem);
					}
				}
			}
			if (ItemService.addItems(player, questItems))
			{
				if(rewards.getGold() != null)
				{
					inventory.increaseKinah((player.getRates().getQuestKinahRate() * rewards.getGold()));
				}
				if(rewards.getExp() != null && !player.isNoExperienceGain())
				{
					int rewardExp = (player.getRates().getQuestXpRate() * rewards.getExp());
					if (rewardExp > Integer.MAX_VALUE || rewardExp < 0) rewardExp = Integer.MAX_VALUE;
					player.getCommonData().addExp(rewardExp);
				}

				if(rewards.getTitle() != null)
				{
					player.getTitleList().addTitle(rewards.getTitle());
				}

				if (rewards.getRewardAbyssPoint() != null)
				{
					player.getCommonData().addAp(rewards.getRewardAbyssPoint());
				}

				if (rewards.getExtendInventory() != null)
				{
					if (rewards.getExtendInventory() == 1)
						CubeExpandService.expand(player);
					else if (rewards.getExtendInventory() == 2)
						WarehouseService.expand(player);
				}

				if (rewards.getExtendStigma() != null)
				{
					PlayerCommonData pcd = player.getCommonData();
					pcd.setAdvencedStigmaSlotSize(pcd.getAdvencedStigmaSlotSize()+1);
					PacketSendUtility.sendPacket(player, new SM_CUBE_UPDATE(player, 6, pcd.getAdvencedStigmaSlotSize()));
				}

				if (extRewards != null)
				{
					if(extRewards.getGold() != null)
					{
						inventory.increaseKinah((player.getRates().getQuestKinahRate() * extRewards.getGold()));
					}
					if(extRewards.getExp() != null)
					{
						int rewardExp = (player.getRates().getQuestXpRate() * extRewards.getExp());
						player.getCommonData().addExp(rewardExp);
					}

					if(extRewards.getTitle() != null)
					{
						player.getTitleList().addTitle(extRewards.getTitle());
					}				
				}

				//remove all worker list item if finished.
				QuestWorkItems qwi = questsData.getQuestById(id).getQuestWorkItems();

				if(qwi != null)
				{
					long count = 0;
					for(QuestItems qi : qwi.getQuestWorkItem())
					{
						if(qi != null)
						{	
							count = player.getInventory().getItemCountByItemId(qi.getItemId());
							if(count > 0)
								if(!player.getInventory().removeFromBagByItemId(qi.getItemId(), count))
									return false;
						}
					}
				}

				QuestEngine.getInstance().onQuestFinish(env);
				qs.setStatus(QuestStatus.COMPLETE);
				// save the rewardNo which is used to check quest start conditions
				qs.setQuestVarById(0, reward + 1);
				qs.setCompliteCount(qs.getCompleteCount() + 1);
				qs.setQuestVarById(0, reward + 1);
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(2, id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
				player.getController().updateNearbyQuests();
				QuestEngine.getInstance().onLvlUp(env);
				GuildService.getInstance().deleteDaily(player, id);
				return true;
			}
			return true;
		}
	}

	public static boolean checkNearBy(QuestCookie env, int levelDiff)
	{
		Player player = env.getPlayer();
		QuestTemplate	template = questsData.getQuestById(env.getQuestId());

		if(template == null)
			return false;

		if(template.getRacePermitted() != null)
		{
			if(template.getRacePermitted().ordinal() != player.getCommonData().getRace().ordinal())
				return false;
		}

		// min level - 2 so that the gray quest arrow shows when quest is almost available 
		if(player.getLevel() < template.getMinlevelPermitted() - levelDiff)
			return false;
		
		if(template.getMaxlevelPermitted() != 0 && player.getLevel() > template.getMaxlevelPermitted())
			return false;
		
		if(!template.isWeeklyActive())
			return false;

		if(template.getClassPermitted().size() != 0)
		{
			if(!template.getClassPermitted().contains(player.getCommonData().getPlayerClass()))
				return false;
		}

		if(template.getGenderPermitted() != null)
		{
			if(template.getGenderPermitted().ordinal() != player.getGender().ordinal())
				return false;
		}

		QuestState qs = player.getQuestStateList().getQuestState(template.getId());
		if(qs != null) {
			if (qs.canRepeat(template.getMaxRepeatCount()))
				return true;
			else if(qs.getStatus() != QuestStatus.LOCKED && qs.getStatus() != QuestStatus.NONE)
				return false;
		}
		return true;
	}

	public static boolean canStart(QuestCookie env)
	{
		if (!checkNearBy(env, 0))
			return false;
		Player player = env.getPlayer();
		QuestTemplate	template = questsData.getQuestById(env.getQuestId());

		if(!checkStartConditions(player, template))
			return false;

		if(template.getCombineSkill() != null)
		{
			SkillListEntry skill = player.getSkillList().getSkillEntry(template.getCombineSkill());
			if(skill == null)
				return false;
			if(skill.getSkillLevel() < template.getCombineSkillPoint())
				return false;
			if(template.getCombineSkillPoint() == 449 || template.getCombineSkillPoint() == 499)
				return true;
			if(skill.getSkillLevel()-40 > template.getCombineSkillPoint())
				return false;
			return true;
		}

		QuestState qs = player.getQuestStateList().getQuestState(template.getId());
		if(qs != null && qs.getStatus().value() > 0) {
			return qs.canRepeat(template.getMaxRepeatCount());
		}
		return true;
	}

	/**
	 * @param template
	 * @return
	 */
	public static boolean checkStartConditions(Player player, QuestTemplate template)
	{
		QuestStartConditions startConditions = template.getFinishedQuestConds();
		if(startConditions != null &&
			!startConditions.Check(player.getQuestStateList(), new QuestStartConditions.Finished()))
			return false;

		if(CheckCraftQuestConfig(player, template))
		{
			startConditions = template.getUnfinishedQuestConds();
			if(startConditions != null && 
				!startConditions.Check(player.getQuestStateList(), new QuestStartConditions.Unfinished()))
				return false;

			startConditions = template.getAcquiredQuestConds();
			if(startConditions != null &&
				!startConditions.Check(player.getQuestStateList(), new QuestStartConditions.Acquired()))
				return false;

			startConditions = template.getNoacquiredQuestConds();
			if(startConditions != null &&
				!startConditions.Check(player.getQuestStateList(), new QuestStartConditions.NoAcquired()))
				return false;
		}
		return true;
	}

	/**
	 * @param player
	 * @return
	 */
	private static boolean CheckCraftQuestConfig(Player player, QuestTemplate template)
	{
		int questId = template.getId();

		if(CustomConfig.MASTERCRAFT_LIMIT_DISABLE)
		{
			if(player.getCommonData().getRace() == Race.ELYOS)
			{
				for(int id: ElyosCraftQuest)
				{
					if(questId == id)
						return false;
				}
			}
			else if(player.getCommonData().getRace() == Race.ASMODIANS)
			{
				for(int id: AsmoCraftQuest)
				{
					if(questId == id)
						return false;
				}
			}
		}

		return true;
	}

	public static boolean startQuest(QuestCookie env, QuestStatus questStatus)
	{
		Player player = env.getPlayer();
		int id = env.getQuestId();
		QuestTemplate template = questsData.getQuestById(env.getQuestId());
		if(id < 80000 && questStatus != QuestStatus.LOCKED)
		{
			if(!canStart(env))
				return false;
		}
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(1, id, questStatus, 0));
		QuestState qs = player.getQuestStateList().getQuestState(id);
		if(qs == null)
		{
			qs = new QuestState(template.getId(), questStatus, 0, 0);
			player.getQuestStateList().addQuest(id, qs);
		}
		else
		{
			if(template.getMaxRepeatCount() >= qs.getCompleteCount())
			{
				qs.setStatus(questStatus);
				qs.setQuestVar(0);
			}
		}

		player.getController().updateNearbyQuests();
		return true;
	}

	public boolean questComplite(QuestCookie env)
	{
		Player player = env.getPlayer();
		int id = env.getQuestId();
		QuestState qs = player.getQuestStateList().getQuestState(id);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
		qs.setStatus(QuestStatus.REWARD);
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(2, id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
		player.getController().updateNearbyQuests();
		return true;
	}

	public static boolean collectItemCheck(QuestCookie env, boolean removeItem)
	{
		Player player = env.getPlayer();
		int id = env.getQuestId();
		QuestState qs = player.getQuestStateList().getQuestState(id);
		if(qs == null)
			return false;

		QuestTemplate	template = questsData.getQuestById(env.getQuestId());
		CollectItems collectItems = template.getCollectItems();
		if(collectItems == null)
			return true;

		for(CollectItem collectItem : collectItems.getCollectItem())
		{
			int itemId = collectItem.getItemId();
			long count = itemId == ItemId.KINAH.value() ?
				player.getInventory().getKinahCount() : 
					player.getInventory().getItemCountByItemId(itemId);
				if(collectItem.getCount() > count)
					return false;
		}
		if (removeItem)
		{
			BonusTemplate bonusTemplate = bonusData.getBonusInfoByQuestId(id);
			List<SimpleCheckItemBonus> appliedBonuses = null;
			if(bonusTemplate != null)
			{
				List<AbstractInventoryBonus> bi = bonusTemplate.getItemBonuses();
				int checks = 0;
				for (int i = 0; i < bi.size(); i++)
				{
					AbstractInventoryBonus bonus = bi.get(i);
					if(!(bonus instanceof SimpleCheckItemBonus))
						continue;
					checks++;
					SimpleCheckItemBonus scb = (SimpleCheckItemBonus)bonus;
					HandlerResult result = QuestEngine.getInstance().onBonusApply(env, i, bonus);
					if(result == HandlerResult.UNKNOWN)
					{
						if(scb.canApply(player, env.getQuestId()))
						{
							if(appliedBonuses == null)
								appliedBonuses = new ArrayList<SimpleCheckItemBonus>();
							appliedBonuses.add(scb);
						}
					}
					else if(result == HandlerResult.SUCCESS)
					{
						if(appliedBonuses == null)
							appliedBonuses = new ArrayList<SimpleCheckItemBonus>();
						appliedBonuses.add(scb);
					}
				}
				// If check constraints added (like in redeems), returns no bonus
				if (checks > 0 && appliedBonuses == null)
					return false;
			}

			for (CollectItem collectItem : collectItems.getCollectItem())
			{
				if(collectItem.getItemId() == 182400001)
					player.getInventory().decreaseKinah(collectItem.getCount());
				else
				{
					boolean removeResult = player.getInventory().removeFromBagByItemId(collectItem.getItemId(), collectItem.getCount());
					if(!removeResult)
						return false;
				}
			}

			if(appliedBonuses != null) 
			{
				for (AbstractInventoryBonus bonus : appliedBonuses)
				{
					// TODO: we can not check if the inventory is full
					// when called from the script
					bonus.apply(player, null);
				}
			}
		}
		return true;
	}

	public static VisibleObject addNewSpawn(int worldId, int instanceId, int templateId, float x, float y, float z, byte heading, boolean noRespawn)
	{
		SpawnTemplate spawn = SpawnEngine.getInstance().addNewSpawn(worldId, instanceId, templateId, x, y, z, heading, 0, 0, noRespawn);
		return SpawnEngine.getInstance().spawnObject(spawn, instanceId);
	}

	public static void getQuestDrop(Set<DropItem> dropItems, Npc npc, Player player)
	{
		List<QuestDrop> drops = QuestEngine.getInstance().getQuestDrop(npc.getNpcId());
		if(drops.isEmpty() || player.isInAlliance())
			return;
		List<Player> players = new ArrayList<Player>();
		if (player.isInGroup())
		{
			players.add(player); // add the first
			for (Player member : player.getPlayerGroup().getMembers())
			{
				if(player.equals(member))
					continue;
				if(MathUtil.isInRange(member, npc, GroupConfig.GROUP_MAX_DISTANCE))
				{
					players.add(member);
				}
			}
		}
		else
		{
			players.add(player);
		}
		for (QuestDrop drop: drops)
		{
			boolean droppedOnce = false;
			boolean isItemDropped = false;
			DropItem droppedOnceItem = null;

			// create new DropItem with player not set, so we could compare hashes
			DropItem item = new DropItem(new DropTemplate(drop.getNpcId(), drop.getItemId(), 1, 1, drop.getChance()));
			// protection against overriding chances from DB
			if(dropItems.contains(item)) // for this player from DB data
				dropItems.remove(item); // removes DB drop

			for(Player member : players)
			{
				if(!hasQuestForDrop(member, drop))
					continue;

				if(!drop.isDropEachMember())
				{
					if(!droppedOnce)
					{
						isItemDropped = isDrop(member, drop);
						droppedOnce = true;
					}
					else
					{
						if(isItemDropped)
							droppedOnceItem.addQuestPlayerObjId(member.getObjectId());
						continue;
					}
				}
				else
				{
					isItemDropped = isDrop(player, drop);
				}

				// create a new DropItem if its null
				if(item == null)
					item = new DropItem(new DropTemplate(drop.getNpcId(), drop.getItemId(), 1, 1, drop.getChance()));

				if(drop.isDropEachMember())
					item.setQuestDropForEachMemeber();
				if(droppedOnce && isItemDropped)
					droppedOnceItem = item;

				item.addQuestPlayerObjId(member.getObjectId());
				item.setCount(1);

				if(isItemDropped && !droppedOnce)
					dropItems.add(item);
				item = null;
			}
			if(droppedOnce && isItemDropped)
				dropItems.add(droppedOnceItem);
		}
	}

	private static boolean isDrop(Player player, QuestDrop drop)
	{
		NpcDropStat stats = player.getNpcDropStats(drop.getNpcId());
		double chance = stats.getItemLootChance(drop.getItemId());
		// if not in collection, set initial value
		if (chance == 0)
		{
			stats.setItemLootChance(drop.getItemId(), drop.getChance());
			chance = drop.getChance();
		}
		boolean result = true;
		if(Rnd.get() * 100 > chance)
			result = false;
		stats.updateStat(drop.getItemId(), result);
		return result;
	}

	/**
	 * Check if a player is doing a quest for the given drop.
	 * 
	 * @param player
	 * @param drop
	 * @return
	 */
	private static boolean hasQuestForDrop(Player player, QuestDrop drop)
	{
		int questId = drop.getQuestId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		QuestTemplate template = questsData.getQuestById(questId);
		CollectItems collectItems = template.getCollectItems();
		if(collectItems == null)
			return true;

		for(CollectItem collectItem : collectItems.getCollectItem())
		{
			int collectItemId = collectItem.getItemId();
			int dropItemId = drop.getItemId();
			if(collectItemId != dropItemId)
				continue;
			long count = player.getInventory().getItemCountByItemId(collectItemId);
			if(collectItem.getCount() > count)
				return true;
		}
		return false;
	}

	/**
	 * @param id
	 * @param playerLevel
	 * @return false if player is 2 or more levels below quest level 
	 */
	public static boolean checkLevelRequirement(int questId, int playerLevel)
	{
		QuestTemplate template = questsData.getQuestById(questId);
		return (playerLevel >= template.getMinlevelPermitted() &&
				(template.getMaxlevelPermitted() == 0 || playerLevel <= template.getMaxlevelPermitted()));
	}

	public static boolean questTimerStart(QuestCookie env, int timeInSeconds)
	{
		final Player player = env.getPlayer();
		final int id = env.getQuestId();

		if(!player.getQuestTimerOn())
		{
			// Schedule Action When Timer Finishes
			Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable(){

				@Override
				public void run()
				{
					player.setQuestTimerOn(false);
					QuestEngine.getInstance().onQuestTimerEnd(new QuestCookie(null, player, 0, 0));
					if(QuestEngine.getInstance().deleteQuest(player, id))
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(id));
					player.getController().updateNearbyQuests();
				}
			}, timeInSeconds * 1000);
			player.setQuestTimerOn(true);
			player.getController().addTask(TaskId.QUEST_TIMER, task);
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(4, id, timeInSeconds));
			return true;
		}
		return false;
	}

	public static boolean questTimerEnd(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final int id = env.getQuestId();

		player.setQuestTimerOn(false);
		player.getController().cancelTask(TaskId.QUEST_TIMER);
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(4, id, 0));
		return true;
	}
}
