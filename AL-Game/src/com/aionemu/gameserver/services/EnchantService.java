/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatEnchantFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.stats.listeners.ItemEquipmentListener;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.EnchantType;
import com.aionemu.gameserver.model.templates.item.EquipType;
import com.aionemu.gameserver.model.templates.item.ItemEnchantTemplate;
import com.aionemu.gameserver.model.templates.item.ItemQuality;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.actions.EnchantItemAction;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.item.ItemSocketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantService {

	private static final Logger log = LoggerFactory.getLogger(EnchantService.class);

	public static boolean breakItem(Player player, Item targetItem, Item parentItem) {
		Storage inventory = player.getInventory();
		if (inventory.getItemByObjId(targetItem.getObjectId().intValue()) == null) {
			return false;
		}
		if (inventory.getItemByObjId(parentItem.getObjectId().intValue()) == null) {
			return false;
		}
		ItemTemplate itemTemplate = targetItem.getItemTemplate();
		int quality = itemTemplate.getItemQuality().getQualityId();
		if ((!itemTemplate.isArmor()) && (!itemTemplate.isWeapon())) {
			AuditLogger.info(player, "Player try break dont compatible item type.");
			return false;
		}
		if ((!itemTemplate.isArmor()) && (!itemTemplate.isWeapon())) {
			AuditLogger.info(player, "Break item hack, armor/weapon iD changed.");
			return false;
		}
		if ((itemTemplate.isSoulBound()) && (!itemTemplate.isArmor())) {
			quality++;
		} else if ((!itemTemplate.isSoulBound()) && (itemTemplate.isArmor())) {
			quality--;
		}
		int maxcount = itemTemplate.getEquipmentType().ordinal() + quality + 2;
		int mincount = quality + 1 - (4 - itemTemplate.getEquipmentType().ordinal());
		int rndcount = Rnd.get(2);
		int number = Rnd.get(mincount, maxcount) + rndcount;
		int level = 1;
		switch (quality) {
		case 0:
		case 1:
			level = Rnd.get(-4, 10);
			break;
		case 2:
			level = Rnd.get(-3, 20);
			break;
		case 3:
			level = Rnd.get(-2, 30);
			break;
		case 4:
			level = Rnd.get(-1, 40);
			break;
		case 5:
			level = Rnd.get(0, 50);
			break;
		case 6:
			level = Rnd.get(0, 60);
		}
		if (level < 1) {
			level = 1;
		}
		int enchantItemLevel = targetItem.getItemTemplate().getLevel() + level;
		int enchantItemId = 166000000 + enchantItemLevel;
		if (inventory.delete(targetItem) != null) {
			if (inventory.decreaseByObjectId(parentItem.getObjectId().intValue(), 1L)) {
				ItemService.addItem(player, enchantItemId, number);
			}
		} else {
			AuditLogger.info(player, "Possible break item hack, do not remove item.");
		}
		return true;
	}

	public static boolean enchantItem(Player player, Item parentItem, Item targetItem, Item supplementItem) {
		ItemTemplate enchantStone = parentItem.getItemTemplate();
		int enchantStoneLevel = enchantStone.getLevel();
		int targetItemLevel = targetItem.getItemTemplate().getLevel();
		int enchantitemLevel = targetItem.getEnchantLevel() + 1;
		int qualityId = targetItem.getItemTemplate().getItemQuality().getQualityId();


		int qualityCap = 0;

		ItemQuality quality = targetItem.getItemTemplate().getItemQuality();
		switch (quality) {
		case JUNK:
		case COMMON:
			qualityCap = 5;
			break;
		case RARE:
			qualityCap = 10;
			break;
		case LEGEND:
			qualityCap = 15;
			break;
		case UNIQUE:
			qualityCap = 20;
			break;
		case EPIC:
			qualityCap = 25;
			break;
		case MYTHIC:
			qualityCap = 30;
		}
		float success = EnchantsConfig.ENCHANT_STONE;

		int levelDiff = enchantStoneLevel - targetItemLevel;
		success += (levelDiff > 0 ? levelDiff * 3.0F / qualityCap : 0.0F);

		success += levelDiff - qualityCap;

		success -= targetItem.getEnchantLevel() * qualityCap / (enchantitemLevel > 10 ? qualityId : qualityId + 1);
		if (supplementItem != null) {
			int supplementUseCount = 1;

			ItemTemplate supplementTemplate = supplementItem.getItemTemplate();

			float addSuccessRate = 0.0F;

			EnchantItemAction action = supplementTemplate.getActions().getEnchantAction();
			if (action != null) {
				if (action.isManastoneOnly()) {
					return false;
				}
				addSuccessRate = action.getChance() * 2.0F;
			}
			action = enchantStone.getActions().getEnchantAction();
			if (action != null) {
				supplementUseCount = action.getCount();
			}
			if (enchantitemLevel > 10) {
				supplementUseCount *= 2;
			}
			if (player.getInventory().getItemCountByItemId(supplementTemplate.getTemplateId()) < supplementUseCount) {
				return false;
			}
			success += addSuccessRate;

			player.subtractSupplements(supplementUseCount, supplementTemplate.getTemplateId());
		}
		if (success >= 95.0F) {
			success = 95.0F;
		}
		boolean result = false;
		float random = Rnd.get(1, 1000) / 10.0F;
		if (random <= success) {
			result = true;
		}
		if (player.getAccessLevel() > 2) {
			PacketSendUtility.sendMessage(player, (result ? "Success" : "Fail") + " Rnd:" + random + " Luck:" + success);
		}
		return result;
	}

	public static void enchantItemAct(Player player, Item parentItem, Item targetItem, Item supplementItem, int currentEnchant, boolean result) {
		if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId().intValue(), 1L)) {
			AuditLogger.info(player, "Possible enchant hack, do not remove enchant stone.");
			return;
		}
		int cheakLevel = parentItem.getItemTemplate().getLevel() - targetItem.getItemTemplate().getLevel();
		int rndLevel = 1;
		if (cheakLevel > 15)
		{
			int level = Rnd.get(1, 100);
			if (level < 10) {
				rndLevel = 2;
			}
			if (level < 5) {
				rndLevel = 3;
			}
		}
		player.updateSupplements();
		int maxLevel = targetItem.getItemTemplate().getMaxEnchantLevel() + targetItem.getItemTemplate().getMaxEnchantBonus();
		if (result) {
			if (currentEnchant >= maxLevel) {
				AuditLogger.info(player, "Possible enchant hack, send fake packet for enchant up more posible.");
				return;
			}
			if (currentEnchant + rndLevel <= maxLevel) {
				currentEnchant += rndLevel;
			} else {
				rndLevel = maxLevel - currentEnchant;
				currentEnchant = maxLevel;
			}
		}
		else if (currentEnchant > 10) {
			currentEnchant = 10;
		}
		else if (currentEnchant > 0)
		{
			currentEnchant--;
		}
		targetItem.setEnchantLevel(currentEnchant);
		if (targetItem.isEquipped()) {
			player.getGameStats().updateStatsVisually();
		}
		ItemPacketService.updateItemAfterInfoChange(player, targetItem);
		if (targetItem.isEquipped()) {
			player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
		} else {
			player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
		if (result) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_ITEM_SUCCEED_NEW(new DescriptionId(targetItem.getNameId()), rndLevel));
		} else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_FAILED(new DescriptionId(targetItem.getNameId())));
		}
	}

	public static boolean socketManastone(Player player, Item parentItem, Item targetItem, Item supplementItem, int targetWeapon) {
		int targetItemLevel = 1;
		if (targetWeapon == 1) {
			targetItemLevel = targetItem.getItemTemplate().getLevel();
		} else {
			targetItemLevel = targetItem.getFusionedItemTemplate().getLevel();
		}
		int stoneLevel = parentItem.getItemTemplate().getLevel();
		int slotLevel = (int)(10.0D * Math.ceil((targetItemLevel + 10) / 10.0D));
		boolean result = false;

		float success = EnchantsConfig.MANA_STONE;
		if (stoneLevel > slotLevel) {
			return false;
		}
		int stoneCount;
		//int stoneCount;
		if (targetWeapon == 1) {
			stoneCount = targetItem.getItemStones().size();
		} else {
			stoneCount = targetItem.getFusionStones().size();
		}
		if (targetWeapon == 1) {
			if (stoneCount >= targetItem.getSockets(false))
			{
				AuditLogger.info(player, "Manastone socket overload");
				return false;
			}
		}
		else if ((!targetItem.hasFusionedItem()) || (stoneCount >= targetItem.getSockets(true))) {
			AuditLogger.info(player, "Manastone socket overload");
			return false;
		}
		success += (parentItem.getItemTemplate().getItemQuality() == ItemQuality.COMMON ? 25.0F : 15.0F);

		float socketDiff = stoneCount * 1.25F + 1.75F;

		success += (slotLevel - stoneLevel) / socketDiff;
		if (supplementItem != null) {
			int supplementUseCount = 0;
			ItemTemplate manastoneTemplate = parentItem.getItemTemplate();
			int manastoneCount;
			//int manastoneCount;
			if (targetWeapon == 1) {
				manastoneCount = targetItem.getItemStones().size() + 1;
			} else {
				manastoneCount = targetItem.getFusionStones().size() + 1;
			}
			ItemTemplate supplementTemplate = supplementItem.getItemTemplate();
			float addSuccessRate = 0.0F;

			boolean isManastoneOnly = false;
			EnchantItemAction action = manastoneTemplate.getActions().getEnchantAction();
			if (action != null) {
				supplementUseCount = action.getCount();
			}
			action = supplementTemplate.getActions().getEnchantAction();
			if (action != null) {
				addSuccessRate = action.getChance();
				isManastoneOnly = action.isManastoneOnly();
			}
			if (isManastoneOnly) {
				supplementUseCount = 1;
			} else if (stoneCount > 0) {
				supplementUseCount *= manastoneCount;
			}
			if (player.getInventory().getItemCountByItemId(supplementTemplate.getTemplateId()) < supplementUseCount) {
				return false;
			}
			success += addSuccessRate;

			player.subtractSupplements(supplementUseCount, supplementTemplate.getTemplateId());
		}
		float random = Rnd.get(1, 1000) / 10.0F;
		if (random <= success) {
			result = true;
		}
		if (player.getAccessLevel() > 2) {
			PacketSendUtility.sendMessage(player, (result ? "Success" : "Fail") + " Rnd:" + random + " Luck:" + success);
		}
		return result;
	}

	public static void socketManastoneAct(Player player, Item parentItem, Item targetItem, Item supplementItem, int targetWeapon, boolean result) {
		player.updateSupplements();
		if ((player.getInventory().decreaseByObjectId(parentItem.getObjectId().intValue(), 1L)) && (result)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTION_SUCCEED(new DescriptionId(targetItem.getNameId())));
			if (targetWeapon == 1) {
				ManaStone manaStone = ItemSocketService.addManaStone(targetItem, parentItem.getItemTemplate().getTemplateId());
				if (targetItem.isEquipped())
				{
					ItemEquipmentListener.addStoneStats(targetItem, manaStone, player.getGameStats());
					player.getGameStats().updateStatsAndSpeedVisually();
				}
			} else {
				ManaStone manaStone = ItemSocketService.addFusionStone(targetItem, parentItem.getItemTemplate().getTemplateId());
				if (targetItem.isEquipped())
				{
					ItemEquipmentListener.addStoneStats(targetItem, manaStone, player.getGameStats());
					player.getGameStats().updateStatsAndSpeedVisually();
				}
			}
		} else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTION_FAILED(new DescriptionId(targetItem.getNameId())));
			if (targetWeapon == 1) {
				Set<ManaStone> manaStones = targetItem.getItemStones();
				if (targetItem.isEquipped()) {
					ItemEquipmentListener.removeStoneStats(manaStones, player.getGameStats());
					player.getGameStats().updateStatsAndSpeedVisually();
				}
				ItemSocketService.removeAllManastone(player, targetItem);
			} else {
				Set<ManaStone> manaStones = targetItem.getFusionStones();
				if (targetItem.isEquipped()) {
					ItemEquipmentListener.removeStoneStats(manaStones, player.getGameStats());
					player.getGameStats().updateStatsAndSpeedVisually();
				}
				ItemSocketService.removeAllFusionStone(player, targetItem);
			}
		}
		ItemPacketService.updateItemAfterInfoChange(player, targetItem);
	}

	public static void onItemEquip(Player player, Item item) {
		List<IStatFunction> modifiers = new ArrayList<IStatFunction>();
		try {
			if (item.getItemTemplate().isWeapon()) {
				switch (item.getItemTemplate().getWeaponType()) {
				case BOOK_2H:
				case ORB_2H:
				case HARP_2H:
				case GUN_1H:
				case CANNON_2H:
				case KEYBLADE_2H:
					modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
					modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ATTACK));
					break;
				case MACE_1H:
				case STAFF_2H:
					modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
				case DAGGER_1H:
				case BOW:
				case POLEARM_2H:
				case SWORD_1H:
				case SWORD_2H:
					if (item.getEquipmentSlot() == ItemSlot.MAIN_HAND.getSlotIdMask()) {
						modifiers.add(new StatEnchantFunction(item, StatEnum.MAIN_HAND_POWER));
					} else {
						modifiers.add(new StatEnchantFunction(item, StatEnum.OFF_HAND_POWER));
					}
					break;
				default:
					break;
				}
			} else if (item.getItemTemplate().isAccessory()) {
				if (item.getItemTemplate().getAwakenId() > 0) {
					ItemEnchantTemplate ie = DataManager.ITEM_ENCHANT_DATA.getEnchantTemplate(EnchantType.AUTHORIZE, item.getItemTemplate().getAwakenId());
					if (item.getAuthorize() > 0) {
						modifiers.addAll(ie.getStats(item.getAuthorize()));
					}
				} else if (item.getEquipmentType() != EquipType.ARMOR) {
					switch (item.getEquipmentType()) {
					case HEAD:
					case EAR:
					case NECK:
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_ATTACK_RATIO));
						break;
					case FINGER:
					case WAIST:
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_DEFEND_RATIO));
					default:
						break;
					}
				} else {
					switch (item.getItemTemplate().getCategory()) {
					case HELMET:
					case EARRINGS:
					case NECKLACE:
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_ATTACK_RATIO));
						break;
					case RINGS:
					case BELT:
						modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_DEFEND_RATIO));
					default:
						break;
					}
				}
			} else if (item.getItemTemplate().isArmor()) {
				if (item.getItemTemplate().getArmorType() == ArmorType.SHIELD) {
					modifiers.add(new StatEnchantFunction(item, StatEnum.DAMAGE_REDUCE));
					modifiers.add(new StatEnchantFunction(item, StatEnum.BLOCK));
				}
				if (item.getItemTemplate().isFeather()) {
					int id = item.getItemTemplate().getAwakenId();
					switch (id)
					{
					case 52:
						modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK));
						modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
						break;
					case 53:
						modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL));
						modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
					}
				} else {
					modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_DEFENSE));
					modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_DEFEND));
					modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP));
					modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL_RESIST));
				}
			}
			if (!modifiers.isEmpty()) {
				player.getGameStats().addEffect(item, modifiers);
			}
		} catch (Exception ex) {
			log.error("Error on item equip.", ex);
		}
	}
}
