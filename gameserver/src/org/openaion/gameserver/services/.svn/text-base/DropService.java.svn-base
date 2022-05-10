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

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectProcedure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.configs.main.DropConfig;
import org.openaion.gameserver.dao.DropListDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.DescriptionId;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.NpcType;
import org.openaion.gameserver.model.alliance.PlayerAllianceMember;
import org.openaion.gameserver.model.drop.DropItem;
import org.openaion.gameserver.model.drop.DropList;
import org.openaion.gameserver.model.drop.DropTemplate;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.DropNpc;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.QuestStateList;
import org.openaion.gameserver.model.gameobjects.player.RequestResponseHandler;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.templates.drops.NpcDrop;
import org.openaion.gameserver.model.templates.item.ItemCategory;
import org.openaion.gameserver.model.templates.item.ItemQuality;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_GROUP_LOOT;
import org.openaion.gameserver.network.aion.serverpackets.SM_LOOT_ITEMLIST;
import org.openaion.gameserver.network.aion.serverpackets.SM_LOOT_STATUS;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.utils.stats.DropRewardEnum;
import org.openaion.gameserver.world.World;


/**
 * @author ATracer
 * @author Jego
 */
public class DropService
{
	private static final Logger			log					= Logger.getLogger(DropService.class);

	private DropList					dropList;

	private Map<Integer, Set<DropItem>>	currentDropMap		= new FastMap<Integer, Set<DropItem>>().shared();
	private Map<Integer, DropNpc>		dropRegistrationMap	= new FastMap<Integer, DropNpc>().shared();

	/**
	 * Integer is the group/alliance Id
	 */
	private Map<Integer, DropNpc>		specialDropMap		= new FastMap<Integer, DropNpc>().shared();
	private final ReentrantLock			specialDropLock		= new ReentrantLock();
	
	public static final DropService getInstance()
	{
		return SingletonHolder.instance;
	}

	private DropService()
	{
		dropList = new DropList();
		for(NpcDrop drop : DataManager.DROPLIST_DATA.getDrops())
		{
			for(org.openaion.gameserver.model.templates.drops.DropItem di : drop.getDropItems())
			{
				DropTemplate t = new DropTemplate(drop.getNpcId(), di.getItemId(), di.getMin(), di.getMax(), di.getChance());
				dropList.addDropTemplate(drop.getNpcId(), t);
			}
		}
				
		DropList sqlList = DAOManager.getDAO(DropListDAO.class).load();
		TIntObjectHashMap<Set<DropTemplate>> sqlTemplates = sqlList.getAll();
		sqlTemplates.forEachEntry(new TIntObjectProcedure<Set<DropTemplate>>(){
			
			@Override
			public boolean execute(int arg0, Set<DropTemplate> arg1)
			{
				if(dropList.getDropsFor(arg0) != null)
				{
					Set<DropTemplate> xmlDrops = dropList.getDropsFor(arg0);
					for(DropTemplate sqlDrop : arg1)
					{
						boolean xmlExists = false;
						for(DropTemplate xmlDrop : xmlDrops)
						{
							if(xmlDrop.getItemId() == sqlDrop.getItemId())
							{
								xmlExists = true;
								break;
							}
						}
						if(!xmlExists)
							dropList.addDropTemplate(arg0, sqlDrop);
						else
						{
							if(CustomConfig.GAMESERVER_DROPLIST_MASTER_SOURCE.equals("sql"))
							{
								dropList.removeDrop(arg0, sqlDrop.getItemId());
								dropList.addDropTemplate(arg0, sqlDrop);
							}
						}
					}
				}
				else
				{
					for(DropTemplate t : arg1)
					{
						dropList.addDropTemplate(arg0, t);
					}
				}
				return true;
			}
		});
		
		log.info(dropList.getSize() + " npc drops loaded");
	}

	/**
	 * @return the dropList
	 */
	public DropList getDropList()
	{
		return dropList;
	}

	/**
	 * After NPC dies - it can register arbitrary drop
	 * 
	 * @param npc
	 */
	public void registerDrop(Npc npc, Player player, int lvl)
	{
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		registerDrop(npc, player, lvl, players);
	}

	/**
	 * After NPC dies - it can register arbitrary drop
	 * 
	 * @param npc
	 * @param player
	 * @param lvl
	 * @param players
	 *            List of all the group members in range.
	 */
	public void registerDrop(Npc npc, Player player, int lvl, List<Player> players)
	{
		int npcUniqueId = npc.getObjectId();
		int npcTemplateId = npc.getObjectTemplate().getTemplateId();
		
		Set<DropItem> droppedItems = new HashSet<DropItem>();
		Set<DropTemplate> templates = dropList.getDropsFor(npcTemplateId);
		Set<DropTemplate> worldDrops = npc.getWorldDrops(player);
		if (worldDrops != null)
		{
			if (templates == null)
				templates = worldDrops;
			else
				templates.addAll(worldDrops);
		}
		
		int normalDropPercentage = 100;
		int craftItemDropPercentage = 100;
		if(!DropConfig.DISABLE_DROP_REDUCTION && npc.getObjectTemplate().getNpcType() != NpcType.CHEST)
		{
			normalDropPercentage = DropRewardEnum.dropRewardFrom(npc.getLevel() - lvl);
			// craft items will keep dropping if the player is killing low level mobs:
			craftItemDropPercentage = 100 - ((100 - normalDropPercentage) / 2);
		}

		if(templates != null)
		{
			int OrangeDrops	= 0;
			int	GoldDrops = 0;
			int	BlueDrops = 0;
			int Hearts = 0;
			
			float playerDropRate = player.getRates().getDropRate();
			float normalDropRate = playerDropRate * normalDropPercentage / 100F;
			float craftItemDropRate = playerDropRate * craftItemDropPercentage / 100F;
			Map<ItemCategory, Double> categoryChances = new HashMap<ItemCategory, Double>();

			QuestStateList questStates = player.getQuestStateList();
			for(DropTemplate dropTemplate : templates)
			{
				DropItem dropItem = new DropItem(dropTemplate);
				if (!DropConfig.DISABLE_DROP_REDUCTION && npc.getObjectTemplate().getNpcType() != NpcType.CHEST) 
				{
					TIntArrayList questIds = QuestEngine.getInstance().getQuestsForCollectItem(dropTemplate.getItemId());
					normalDropRate = playerDropRate * normalDropPercentage / 100F;
					craftItemDropRate = playerDropRate * craftItemDropPercentage / 100F;
					
					for (int index = 0; index < questIds.size(); index++)
					{
						int questId = questIds.get(index);
						QuestState qs = questStates.getQuestState(questId);
						int maxRepeat = DataManager.QUEST_DATA.getQuestById(questId).getMaxRepeatCount();
						if (qs == null || qs.getStatus() != QuestStatus.COMPLETE || qs.canRepeat(maxRepeat))
						{
							// set drop rates to usual if quest is not complete
							craftItemDropRate = normalDropRate = playerDropRate;
							break;
						}
					}
				}
				
				float calculatedRate = 0;
				ItemTemplate dropListItems = ItemService.getItemTemplate(dropTemplate.getItemId());
				if (dropListItems == null)
					continue;
				
				ItemCategory category = dropListItems.getItemCategory();
				if (DropConfig.ITEMCATEGORY_RESTRICTION_ENABLED)
				{
					if (categoryChances.containsKey(category))
						calculatedRate = (float)(-categoryChances.get(category)) / 100F;
				}
				
				if(dropTemplate.getItemId() >= 152000000 && dropTemplate.getItemId() < 153000000)
				{
					calculatedRate += craftItemDropRate;
					dropItem.calculateCount(player, npc.getNpcId(), calculatedRate);
				}
				else if (category == ItemCategory.HEART)
				{
					calculatedRate += normalDropRate * 10F;
					dropItem.calculateCount(player, npc.getNpcId(), calculatedRate);					
				}
				else
				{
					calculatedRate += normalDropRate;
					dropItem.calculateCount(player, npc.getNpcId(), calculatedRate);
				}

				if(dropItem.getCount() > 0)
				{
					if (DropConfig.ITEMCATEGORY_RESTRICTION_ENABLED)
					{
						double newChance = dropItem.getLootChance();
						if (categoryChances.containsKey(category))
						{
							newChance += categoryChances.get(category);
							if (newChance > calculatedRate * 100)
								continue;
						}
						categoryChances.put(dropListItems.getItemCategory(), newChance);
					}
					
					if (dropTemplate.getItemId() == 182400001)
					{
						dropItem.setCount(dropItem.getCount() * player.getRates().getKinahRate());
					}
					
					if (category == ItemCategory.HEART)
					{
						// Creatures have only 1 heart ;)
						if (Hearts > 0)
							continue;
						Hearts++;
					}
					
					if(DropConfig.DROPQUANTITY_RESTRICTION_ENABLED)
					{
						if(dropListItems.getItemQuality() == ItemQuality.LEGEND)
						{
							if(BlueDrops >= DropConfig.DROPQUANTITY_RESTRICTION_BLUE)
								continue;
							else
							{
								BlueDrops++;
								droppedItems.add(dropItem);
							}
						}
						else if(dropListItems.getItemQuality() == ItemQuality.UNIQUE)
						{
							if(GoldDrops >= DropConfig.DROPQUANTITY_RESTRICTION_GOLD)
								continue;
							else
							{
								GoldDrops++;
								droppedItems.add(dropItem);
							}
						}
						else if(dropListItems.getItemQuality() == ItemQuality.EPIC)
						{
							if(OrangeDrops >= DropConfig.DROPQUANTITY_RESTRICTION_ORANGE)
								continue;
							else
							{
								OrangeDrops++;
								droppedItems.add(dropItem);
							}
						}
						else
							droppedItems.add(dropItem);
					}
					else
						droppedItems.add(dropItem);
				}
			}
			
			templates.clear();
			templates = null;
		}
		
		QuestService.getQuestDrop(droppedItems, npc, player);
		
		// Now set correct indexes
		int index = 1;
		for (DropItem drop: droppedItems)
			drop.setIndex(index++);
		
		currentDropMap.put(npcUniqueId, droppedItems);

		// TODO: Player should not be null
		if(player != null)
		{
			List<Player> dropPlayers = new ArrayList<Player>();

			if (player.isInAlliance())
			{
				dropRegistrationMap.put(npcUniqueId, new DropNpc(AllianceService.getInstance().getMembersToRegistrateByRules(player,
					player.getPlayerAlliance(), npc), npcUniqueId));
				// Fetch players in range
				DropNpc dropNpc = dropRegistrationMap.get(npcUniqueId);
				dropNpc.setInRangePlayers(players);
				dropNpc.setGroupSize(dropNpc.getInRangePlayers().size());

				for(PlayerAllianceMember member : player.getPlayerAlliance().getMembers())
				{
					Player allianceMember = member.getPlayer();

					if(allianceMember != null)
						if(dropNpc.containsKey(allianceMember.getObjectId()))
							dropPlayers.add(allianceMember);
				}
			}
			else if (player.isInGroup())
			{
				dropRegistrationMap.put(npcUniqueId, new DropNpc(GroupService.getInstance().getMembersToRegistrateByRules(player,
					player.getPlayerGroup(), npc), npcUniqueId));
				//Fetch players in range
				DropNpc dropNpc = dropRegistrationMap.get(npcUniqueId);
				dropNpc.setInRangePlayers(players);
				dropNpc.setGroupSize(dropNpc.getInRangePlayers().size());
				for(Player member : player.getPlayerGroup().getMembers())
				{
					if(member != null && member.isOnline()){
						if(dropNpc.containsKey(member.getObjectId()))
							dropPlayers.add(member);
					}
				}
			}
			else
			{
				List<Integer> singlePlayer = new ArrayList<Integer>();
				singlePlayer.add(player.getObjectId());
				dropPlayers.add(player);
				dropRegistrationMap.put(npcUniqueId, new DropNpc(singlePlayer, npcUniqueId));
			}

			for(Player p : dropPlayers)
			{
				PacketSendUtility.sendPacket(p, new SM_LOOT_STATUS(npcUniqueId, 0));
			}
		}
	}

	/**
	 * After NPC respawns - drop should be unregistered //TODO more correct - on despawn
	 * 
	 * @param npc
	 */
	public void unregisterDrop(Npc npc)
	{
		int npcUniqueId = npc.getObjectId();
		currentDropMap.remove(npcUniqueId);
		if(dropRegistrationMap.containsKey(npcUniqueId))
		{
			dropRegistrationMap.remove(npcUniqueId);
		}
	}

	/**
	 * When player clicks on dead NPC to request drop list
	 * 
	 * @param player
	 * @param npcId
	 */
	public void requestDropList(Player player, int npcId)
	{
		if(player == null || !dropRegistrationMap.containsKey(npcId))
			return;

		DropNpc dropNpc = dropRegistrationMap.get(npcId);
		if(!dropNpc.containsKey(player.getObjectId()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_LOOT_NO_RIGHT());
			return;
		}

		if(dropNpc.isBeingLooted())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_LOOT_FAIL_ONLOOTING());
			return;
		}

		dropNpc.setBeingLooted(player);

		Set<DropItem> dropItems = currentDropMap.get(npcId);
		
		if(dropItems == null)
		{
			dropItems = Collections.emptySet();
		}

		PacketSendUtility.sendPacket(player, new SM_LOOT_ITEMLIST(npcId, dropItems, player));
		// PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(npcId, size > 0 ? size - 1 : size));
		PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(npcId, 2));
		player.unsetState(CreatureState.ACTIVE);
		player.setState(CreatureState.LOOTING);
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, npcId), true);
	}

	/**
	 * This method will change looted corpse to not in use
	 * @param player
	 * @param npcId
	 * @param close
	 */
	public void requestDropList(Player player, int npcId, boolean close)
	{
		if(!dropRegistrationMap.containsKey(npcId) || player == null)
			return;

		DropNpc dropNpc = dropRegistrationMap.get(npcId);
		dropNpc.setBeingLooted(null);

		player.unsetState(CreatureState.LOOTING);
		player.setState(CreatureState.ACTIVE);
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT, 0, npcId), true);

		Set<DropItem> dropItems = currentDropMap.get(npcId);
		AionObject obj = World.getInstance().findAionObject(npcId);
		if (obj instanceof Npc)
		{
			Npc npc = (Npc)obj;
			if(npc != null)
			{
				if(dropItems == null || dropItems.size() == 0)
				{
					npc.getController().onDespawn(true);
					return;
				}

				PacketSendUtility.broadcastPacket(npc, new SM_LOOT_STATUS(npcId, 0));
				dropNpc.setFreeLooting();
			}
		}
	}

	/**
	 * Request an item from a killed mob.
	 * 
	 * @param player
	 *            The player that loots the mob.
	 * @param npcId
	 *            The mob that gets looted.
	 * @param itemIndex
	 *            The index of the looted item.
	 */
	public void requestDropItem(final Player player, int npcId, int itemIndex)
	{
		final Set<DropItem> dropItems = currentDropMap.get(npcId);
		final DropNpc dropNpc = dropRegistrationMap.get(npcId);

		// drop was unregistered
		if(dropItems == null || dropNpc == null)
		{
			return;
		}

		// TODO prevent possible exploits

		DropItem requestedItem = null;

		synchronized(dropItems)
		{
			for(DropItem dropItem : dropItems)
			{
				if(dropItem.getIndex() == itemIndex)
				{
					requestedItem = dropItem;
					break;
				}
			}
		}

		if(requestedItem == null || requestedItem.isProcessed())
			return;

		ItemTemplate itemTemplate = ItemService.getItemTemplate(requestedItem.getDropTemplate().getItemId());
		if(itemTemplate == null)
		{
			log.warn("Item id " + requestedItem.getDropTemplate().getItemId()
				+ " can't be found in the item template.");
			return;
		}

		if(!itemTemplate.isTradeable() && !requestedItem.isQuestDropForEachMemeber() && (player.isInGroup() || player.isInAlliance()))
		{
			final DropItem lootItem = requestedItem;
			RequestResponseHandler rrh = new RequestResponseHandler(player){
				@Override
				public void acceptRequest(Creature requester, Player responder)
				{
					continueRequestDropItem(player, dropItems, dropNpc, lootItem);
				}

				@Override
				public void denyRequest(Creature requester, Player responder)
				{
					// do nothing
				}
			};
			SM_QUESTION_WINDOW question = new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_CONFIRM_LOOT, 0,
				new DescriptionId(itemTemplate.getNameId()));
			player.getResponseRequester().sendRequest(SM_QUESTION_WINDOW.STR_CONFIRM_LOOT, rrh, question);
		}
		else
			continueRequestDropItem(player, dropItems, dropNpc, requestedItem);
	}

	private void continueRequestDropItem(final Player player, Set<DropItem> dropItems, DropNpc dropNpc, final DropItem requestedItem)
	{
		if(requestedItem == null || requestedItem.isProcessed())
			return;

		if(CustomConfig.ANNOUNCE_RAREDROPS && !player.getInventory().isFull())
		{
			final ItemTemplate itemTemplate = ItemService.getItemTemplate(requestedItem.getDropTemplate().getItemId());
			if(itemTemplate.getItemQuality() == ItemQuality.UNIQUE || itemTemplate.getItemQuality() == ItemQuality.EPIC)
			{
				final int pRaceId = player.getCommonData().getRace().getRaceId();
				final int pMap = player.getWorldId();
				final int pRegion = player.getActiveRegion().getRegionId();
				final int pInstance = player.getInstanceId();
				
				World.getInstance().doOnAllPlayers(new Executor<Player>(){
					@Override
					public boolean run(Player other)
					{
						if(other.getObjectId() == player.getObjectId() || !other.isSpawned())
						{
							return true;
						}

						int oRaceId = other.getCommonData().getRace().getRaceId();
						int oMap = other.getWorldId();
						int oRegion = other.getActiveRegion().getRegionId();
						int oInstance = other.getInstanceId();

						if(oRaceId == pRaceId && oMap == pMap && oRegion == pRegion && oInstance == pInstance)
						{
						    PacketSendUtility.sendPacket(other,SM_SYSTEM_MESSAGE.STR_FORCE_ITEM_WIN(player.getCommonData().getName(),new DescriptionId(itemTemplate.getNameId())));
						}
						return true;
					}
				});
			}
		}

		if(requestedItem != null)
		{
			if(requestedItem.isItemWonNotCollected() && player != requestedItem.getWinningPlayer())
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOT_ANOTHER_OWNER_ITEM());
				return;
			}

			long currentDropItemCount = requestedItem.getCount();
			int itemId = requestedItem.getDropTemplate().getItemId();

			ItemTemplate itemTemplate = ItemService.getItemTemplate(itemId);
			ItemQuality quality = ItemQuality.COMMON;
			if(itemTemplate == null)
				log.warn("Item id " + itemId + " can't be found in the item template.");
			else
				quality = itemTemplate.getItemQuality();

			if(!requestedItem.isItemWonNotCollected() && !requestedItem.isFreeForAll())
			{
				if(player.isInGroup() || player.isInAlliance())
				{
					if(player.isInGroup())
						requestedItem.setDistributionType(player.getPlayerGroup().getLootGroupRules().getQualityRule(quality));
					else
						requestedItem.setDistributionType(player.getPlayerAlliance().getLootAllianceRules().getQualityRule(quality));
					
					if(requestedItem.getDistributionType() > 1)
					{
						int groupAllianceId = 0;
						if(player.isInGroup())
						{
							groupAllianceId = player.getPlayerGroup().getObjectId();
						}
						else
						{
							groupAllianceId = player.getPlayerAlliance().getObjectId();
						}

						addSpecialItem(groupAllianceId, dropNpc, requestedItem);
					}
				}
			}

			//If looting player not in Group/Alliance or distribution is set to NORMAL
			//or all party members have passed, making item FFA....
			if((!player.isInGroup() && !player.isInAlliance()) || requestedItem.getDistributionType() == 0
				|| requestedItem.isFreeForAll()
				|| (requestedItem.isItemWonNotCollected() && player == requestedItem.getWinningPlayer()))
			{
				currentDropItemCount = ItemService.addItem(player, itemId, currentDropItemCount);
				requestedItem.setWinningPlayer(null);
			}

			if(currentDropItemCount == 0)
			{
				requestedItem.setProcessed();
				dropItems.remove(requestedItem);
			}
			else
			{
				// If player didn't got all item stack
				requestedItem.setCount(currentDropItemCount);
			}

			// show updated drop list
			resendDropList(dropNpc.getBeingLooted(), dropNpc.getNpcId(), dropItems);
		}
	}
	
	private void resendDropList(Player player, int npcId, Set<DropItem> dropItems)
	{
		if(dropItems.size() != 0)
		{
			if(player != null)
			{
				boolean hasItemsForPlayer = false;
				for(DropItem item : dropItems)
				{
					if(item.hasQuestPlayerObjId(player.getObjectId()))
					{
						hasItemsForPlayer = true;
						break;
					}
				}
				if(hasItemsForPlayer)
				{
					PacketSendUtility.sendPacket(player, new SM_LOOT_ITEMLIST(npcId, dropItems, player));
				}
				else
				{
					PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(npcId, 3));
					player.unsetState(CreatureState.LOOTING);
					player.setState(CreatureState.ACTIVE);
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT, 0, npcId), true);
				}
			}
		}
		else
		{
			if(player != null)
			{
				PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(npcId, 3));
				player.unsetState(CreatureState.LOOTING);
				player.setState(CreatureState.ACTIVE);
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT, 0, npcId), true);
			}
			AionObject obj = World.getInstance().findAionObject(npcId);
			if(obj instanceof Npc)
			{
				Npc npc = (Npc) obj;
				if(npc != null)
				{
					npc.getController().onDespawn(true);
				}
			}
		}
	}

	/**
	 * Add an item that should be rolled/bid on.
	 * 
	 * @param groupAllianceId
	 *            The id of the group or alliance.
	 * @param dropNpc
	 * @param specialItem
	 */
	private void addSpecialItem(int groupAllianceId, DropNpc dropNpc, DropItem specialItem)
	{
		if(!dropNpc.addSpecialItem(specialItem))
			return;

		specialDropLock.lock();
		try
		{
			DropNpc currentSpecialNpc = specialDropMap.get(groupAllianceId);
			if(currentSpecialNpc == null)
			{
				specialDropMap.put(groupAllianceId, dropNpc);
				sendBidRollPackets(groupAllianceId);
			}
			else
			{
				try
				{
					currentSpecialNpc.addSpecialDropNpc(dropNpc);
				}
				catch(StackOverflowError soe)
				{
					// This does NOT fix any errors, it just wraps up the StackOverflowError error so it doesn't take
					// 1000 lines in the error log.
					specialDropMap.remove(groupAllianceId);
					throw new Error("StackOverflowError");
				}
			}
		}
		finally
		{
			specialDropLock.unlock();
		}
	}

	/**
	 * Sends the packets to roll/bid on an item.
	 * 
	 * @param groupAllianceId
	 */
	private void sendBidRollPackets(final int groupAllianceId)
	{
		// FIXME find the players in range when the rolling/bidding starts.
		// Store the npc location in DropNpc.
		final DropNpc dropNpc = getNextSpecialNpc(groupAllianceId);
		if(dropNpc == null)
			return;

		final DropItem requestedItem = dropNpc.getNextSpecialItem();
		if(requestedItem == null)
		{
			// For an unknown reason requestedItem can be null.
			// When this happens assume that the current NPC has no more special items and restart the method to check
			// for other NPC's
			dropNpc.resetSpecialItems();
			specialDropMap.remove(groupAllianceId);
			return;
		}

		int itemId = requestedItem.getDropTemplate().getItemId();

		// Start the timeout task and let it wait 20 seconds of it's rolling or 35 seconds if it's bidding.
		int timeout = 20000;
		if(requestedItem.getDistributionType() == 3)
			timeout = 35000;
		ScheduledFuture<?> future = ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				specialLootTimeout(groupAllianceId, dropNpc, requestedItem);
			}
		}, timeout);
		requestedItem.setSpecialDropTimeout(future);

		// Send the packet to all members
		SM_GROUP_LOOT sgl = new SM_GROUP_LOOT(groupAllianceId, itemId, requestedItem.getIndex(), dropNpc.getNpcId(),
			requestedItem.getDistributionType());
		for(Player member : dropNpc.getInRangePlayers())
		{
			if(member.isOnline())
			{
				requestedItem.addSpecialPlayer(member);
				PacketSendUtility.sendPacket(member, sgl);
			}
		}
	}

	/**
	 * Cancel the current special item loot.
	 * 
	 * @param groupAllianceId
	 */
	private void specialLootTimeout(int groupAllianceId, DropNpc dropNpc, DropItem requestedItem) {
		requestedItem.setSpecialDropTimeout(null);

		distributeSpecialItem(requestedItem, groupAllianceId, dropNpc.getNpcId(), requestedItem.getIndex());
	}

	/**
	 * @param groupAllianceId
	 *            The id of the group or alliance.
	 * @return The DropNpc that has special items to roll/bid on, or null if there is no next Npc.
	 */
	private DropNpc getNextSpecialNpc(int groupAllianceId)
	{
		specialDropLock.lock();
		try
		{
			DropNpc currentSpecialNpc = specialDropMap.get(groupAllianceId);
			if(currentSpecialNpc == null)
				return null;

			if(!currentSpecialNpc.hasSpecialItems())
			{ // changed the special npc to the next npc with roll/bid items or null.
				currentSpecialNpc = currentSpecialNpc.getNextSpecialDropNpc();
				specialDropMap.put(groupAllianceId, currentSpecialNpc);
			}

			if(currentSpecialNpc == null)
			{
				specialDropMap.remove(groupAllianceId);
			}
			return currentSpecialNpc;
		}
		finally
		{
			specialDropLock.unlock();
		}
	}

	/**
	 * Called from CM_GROUP_LOOT to handle rolls
	 * 
	 * @param player
	 * @param groupAllianceId
	 * @param roll
	 * @param itemId
	 * @param itemIndex
	 * @param npcId
	 */
	public void handleRoll(Player player, int groupAllianceId, int roll, int itemId, int itemIndex, int npcId,
		int distibutionType)
	{
		if(dropRegistrationMap.get(npcId) == null)
			return;

		switch(roll)
		{
			case 0:
				SM_GROUP_LOOT sglGiveUp = new SM_GROUP_LOOT(groupAllianceId, itemId, itemIndex, npcId, distibutionType,
					player.getObjectId(), 0);
				PacketSendUtility.sendPacket(player, sglGiveUp);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_GIVEUP_ME());
				if(player.isInGroup() || player.isInAlliance())
				{
					SM_SYSTEM_MESSAGE giveup = SM_SYSTEM_MESSAGE.STR_MSG_DICE_GIVEUP_OTHER(player.getName());
					for(Player member : dropRegistrationMap.get(npcId).getInRangePlayers())
					{
						if(!player.equals(member))
						{
							PacketSendUtility.sendPacket(member, sglGiveUp);
							PacketSendUtility.sendPacket(member, giveup);
						}
					}
				}
				handleSpecialLoot(player, groupAllianceId, 0, itemId, itemIndex, npcId);
				break;
			case 1:
				int luck = Rnd.get(1, 100);
				SM_GROUP_LOOT sglRoll = new SM_GROUP_LOOT(groupAllianceId, itemId, itemIndex, npcId, distibutionType,
					player.getObjectId(), luck);
				PacketSendUtility.sendPacket(player, sglRoll);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_RESULT_ME(luck));
				if(player.isInGroup() || player.isInAlliance())
				{
					SM_SYSTEM_MESSAGE sysMsgRoll = SM_SYSTEM_MESSAGE.STR_MSG_DICE_RESULT_OTHER(player.getName(), luck);
					for(Player member : dropRegistrationMap.get(npcId).getInRangePlayers())
					{
						if(!player.equals(member))
						{
							PacketSendUtility.sendPacket(member, sglRoll);
							PacketSendUtility.sendPacket(member, sysMsgRoll);
						}
					}
				}
				handleSpecialLoot(player, groupAllianceId, luck, itemId, itemIndex, npcId);
				break;
		}
	}

	/**
	 * Called from CM_GROUP_LOOT to handle bids
	 * 
	 * @param player
	 * @param groupAllianceId
	 * @param bid
	 * @param itemId
	 * @param itemIndex
	 * @param npcId
	 */
	public void handleBid(Player player, int groupAllianceId, long bid, int itemId, int itemIndex, int npcId)
	{
		long kinahAmount = player.getInventory().getKinahItem().getItemCount(); 
		if(bid > 0)
		{
			if(kinahAmount < bid)
			{
				bid = 0;// Set BID to 0 if player has bid more KINAH then they have in inventory
			}
			handleSpecialLoot(player, groupAllianceId, bid, itemId, itemIndex, npcId);
		}
		else
			handleSpecialLoot(player, groupAllianceId, 0, itemId, itemIndex, npcId);
	}

	/**
	 * @param Checks all players have Rolled or Bid then Distributes items accordingly
	 */
	private void handleSpecialLoot(Player player, int groupAllianceId, long bidRollValue, int itemId, int itemIndex, int npcId)
	{
		DropNpc dropNpc = dropRegistrationMap.get(npcId);
		
		Set<DropItem> dropItems = currentDropMap.get(npcId);
		if(dropNpc == null || dropItems == null)
			return;

		DropItem requestedItem = null;

		synchronized(dropItems)
		{
			for(DropItem dropItem : dropItems)
			{
				if(dropItem.getIndex() == itemIndex)
				{
					requestedItem = dropItem;
					break;
				}
			}
		}
		if(requestedItem == null || requestedItem.getDropTemplate().getItemId() != itemId
			|| requestedItem.isProcessed())
			return;

		//Removes player from ARRAY once they have rolled or bid
		if(requestedItem.containsSpecialPlayer(player))
		{
			requestedItem.delSpecialPlayer(player);
		}
		else
			return;

		if(bidRollValue > requestedItem.getHighestValue())
		{
			requestedItem.setHighestValue(bidRollValue);
			requestedItem.setWinningPlayer(player);
		}

		if(requestedItem.getSpecialPlayerSize() != 0)
			return;

		// Cancel the timeout task
		requestedItem.cancelTimeoutTask();

		distributeSpecialItem(requestedItem, groupAllianceId, npcId, itemIndex);
	}

	private void distributeSpecialItem(DropItem requestedItem, int groupAllianceId, int npcId, int itemIndex)
	{
		DropNpc dropNpc = dropRegistrationMap.get(npcId);
		
		if(dropNpc == null)
			return;

		//Check if there is a Winning Player registered if not all members must have passed...
		if(requestedItem.getWinningPlayer() == null)
		{
			requestedItem.setFreeForAll(true);
		}
		else
		{
			Player player = requestedItem.getWinningPlayer();
			long currentDropItemCount = requestedItem.getCount();
			int itemId = requestedItem.getDropTemplate().getItemId();

			switch(requestedItem.getDistributionType())
			{
				case 2:
					winningRollActions(groupAllianceId, player, itemId, npcId, requestedItem);
					break;
				case 3:
					winningBidActions(player, itemId, npcId, requestedItem.getHighestValue());
					break;
			}

			// handles distribution of item to correct player and messages accordingly
			if(player.getInventory().isFull())
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
				requestedItem.setItemWonNotCollected(true);
			}
			else
			{
				Set<DropItem> dropItems = currentDropMap.get(npcId);
				currentDropItemCount = ItemService.addItem(player, itemId, currentDropItemCount);

				if(currentDropItemCount != 0)
				{
					requestedItem.setCount(currentDropItemCount);
					requestedItem.setItemWonNotCollected(true);
				}
				else
				{
					requestedItem.setProcessed();
					dropItems.remove(requestedItem);
				}

				// show updated drop list
				resendDropList(dropNpc.getBeingLooted(), npcId, dropItems);
			}
		}

		sendBidRollPackets(groupAllianceId);
	}

	/** 
	 * @param Displays messages when item gained via ROLLED
	 */	
	private void winningRollActions(int groupAllianceId, Player player, int itemId, int npcId, DropItem requestedItem)
	{
		DescriptionId itemNameId = new DescriptionId(ItemService.getItemTemplate(itemId).getNameId());
		SM_GROUP_LOOT sglWinner = new SM_GROUP_LOOT(groupAllianceId, itemId, requestedItem.getIndex(), npcId,
			requestedItem.getDistributionType(), player.getObjectId());
		PacketSendUtility.sendPacket(player, sglWinner);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOT_GET_ITEM_ME(itemNameId));
		
		if(player.isInGroup() || player.isInAlliance())
		{
			SM_SYSTEM_MESSAGE msgWinner = SM_SYSTEM_MESSAGE.STR_MSG_LOOT_GET_ITEM_OTHER(player.getName(), itemNameId);
			for(Player member : dropRegistrationMap.get(npcId).getInRangePlayers())
			{
				if(!player.equals(member))
				{
					PacketSendUtility.sendPacket(member, sglWinner);
					PacketSendUtility.sendPacket(member, msgWinner);
				}
			}
		}
	}

	/**
	 * @param Displays messages/removes and shares kinah when item gained via BID
	 */	
	private void winningBidActions(Player player, int itemId, int npcId, long highestValue)
	{
		DropNpc dropNpc = dropRegistrationMap.get(npcId);
		
		if((player.isInGroup() || player.isInAlliance()) && dropNpc.getGroupSize() > 1)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_PAY_ACCOUNT_ME(highestValue));
			if(!player.getInventory().decreaseKinah(highestValue))
				return;

			long distributeKinah = highestValue / (dropNpc.getGroupSize() - 1);
			for(Player member : dropNpc.getInRangePlayers())
			{
				if(!player.equals(member))
				{
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_PAY_ACCOUNT_OTHER(player.getName(), highestValue));
					member.getInventory().increaseKinah(distributeKinah);
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_PAY_DISTRIBUTE(highestValue, dropNpc.getGroupSize() - 1, distributeKinah));
				}
			}
		}
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DropService instance = new DropService();
	}
}