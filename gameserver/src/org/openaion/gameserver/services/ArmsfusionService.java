/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.services;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.InventoryDAO;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.items.ManaStone;
import org.openaion.gameserver.model.templates.item.ItemQuality;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;

import java.util.Set;


/**
 * This class is responsible of Armsfusion-related tasks (fusion,breaking)
 * 
 * @author Sylar
 * 
 */
public class ArmsfusionService
{
	private static final Logger			log			= Logger.getLogger(ArmsfusionService.class);
		
	public static void fusionWeapons(Player player, int firstItemUniqueId, int secondItemUniqueId)
	{
		Item firstItem = player.getInventory().getItemByObjId(firstItemUniqueId);
		if(firstItem == null)
			firstItem = player.getEquipment().getEquippedItemByObjId(firstItemUniqueId);
		
		Item secondItem = player.getInventory().getItemByObjId(secondItemUniqueId);
		if(secondItem == null)
			secondItem = player.getEquipment().getEquippedItemByObjId(secondItemUniqueId);
		
		/*
		 * Need to have items in bag, and target the fusion NPC
		 */
		if(firstItem == null || secondItem == null || !(player.getTarget() instanceof Npc))
			return;
		if (!MathUtil.isIn3dRange(player.getTarget(), player, 10))
			return;
		/*
		 * Both have to be 2h weapons
		 */
		if (!firstItem.getItemTemplate().isWeapon() || !secondItem.getItemTemplate().isWeapon())
		{
			Logger.getLogger(ArmsfusionService.class).info("[AUDIT]Player: "+player.getName()+" trying to fusion non-weapon. Hacking!");
			return;
		}
		if (firstItem.getItemTemplate().getWeaponType() == null || secondItem.getItemTemplate().getWeaponType() == null)
			return;
		else
		{
			switch(firstItem.getItemTemplate().getWeaponType())
			{
				case DAGGER_1H:
				case MACE_1H:
				case SWORD_1H:
				case TOOLHOE_1H:
					Logger.getLogger(ArmsfusionService.class).info("[AUDIT]Player: "+player.getName()+" trying to fusion 1h weapon. Hacking!");
					return;
			}
			switch(secondItem.getItemTemplate().getWeaponType())
			{
				case DAGGER_1H:
				case MACE_1H:
				case SWORD_1H:
				case TOOLHOE_1H:
					Logger.getLogger(ArmsfusionService.class).info("[AUDIT]Player: "+player.getName()+" trying to fusion 1h weapon. Hacking!");
					return;
			}
		}
		
		//check if both items are fusionable
		if (!firstItem.getItemTemplate().isCanFuse() || !secondItem.getItemTemplate().isCanFuse())
			return;
		
		double rarity = rarityRate(firstItem.getItemTemplate().getItemQuality());
		double priceRate = player.getPrices().getGlobalPrices(player.getCommonData().getRace()) * .01;
		double taxRate = player.getPrices().getTaxes(player.getCommonData().getRace()) * .01;
		int priceMod = player.getPrices().getGlobalPricesModifier() * 2;
		int level = firstItem.getItemTemplate().getLevel();
		int price = (int)(priceMod * priceRate * taxRate * rarity * level * level);
		log.debug("Rarity: " + rarity + " Price Rate: " + priceRate + " Tax Rate: " + taxRate + " Price Mod: " + priceMod + " Weapon Level: " + level);
		log.debug("Price: " + price);
		
		if(player.getInventory().getKinahItem().getItemCount() < price)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUND_ERROR_NOT_ENOUGH_MONEY(firstItem.getNameID(), secondItem.getNameID()));
			return;
		}
		
		/*
		 * Fusioned weapons must have same type
		 */		
		if(firstItem.getItemTemplate().getWeaponType() != secondItem.getItemTemplate().getWeaponType())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUND_ERROR_DIFFERENT_TYPE);
			return;
		}
		
		/*
		 * Second weapon must have inferior or equal lvl. in relation to first weapon
		 */
		if(secondItem.getItemTemplate().getLevel() > firstItem.getItemTemplate().getLevel())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUND_ERROR_MAIN_REQUIRE_HIGHER_LEVEL);
			return;
		}
		
		boolean decreaseResult = player.getInventory().decreaseKinah(price);
		if(!decreaseResult)
			return;
		
		boolean removeResult = player.getInventory().removeFromBagByObjectId(secondItemUniqueId, 1);
		if(!removeResult)
			return;
		
		firstItem.setFusionedItem(secondItem.getItemTemplate().getTemplateId());

		ItemService.removeAllFusionStone(player,firstItem);

		if (secondItem.hasOptionalSocket())
		{
			firstItem.setOptionalFusionSocket(secondItem.getOptionalSocket());
		}
		else
		{
			firstItem.setOptionalFusionSocket(0);
		}
		
		Set<ManaStone> manastones = secondItem.getItemStones();
		for(ManaStone stone : manastones)
		ItemService.addFusionStone(firstItem, stone.getItemId());
		
		if (firstItem.getPersistentState() != PersistentState.NEW && firstItem.getPersistentState() != PersistentState.UPDATE_REQUIRED)
			firstItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
		
		DAOManager.getDAO(InventoryDAO.class).store(firstItem, player.getObjectId());		
		
		PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(secondItemUniqueId));
		
		PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(firstItem));		
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUND_SUCCESS(firstItem.getNameID(), secondItem.getNameID()));
		
	}
	
	private static double rarityRate(ItemQuality rarity)
	{
		switch(rarity)
		{
		case COMMON: return 1.0;
		case RARE: return 1.25;
		case LEGEND: return 1.5;
		case UNIQUE: return 2.0;
		case EPIC: return 2.5;
		default: return 1.0;
		}
	}

	public static void breakWeapons(Player player, int weaponToBreakUniqueId)
	{
		Item weaponToBreak = player.getInventory().getItemByObjId(weaponToBreakUniqueId);
		if(weaponToBreak == null)
			weaponToBreak = player.getEquipment().getEquippedItemByObjId(weaponToBreakUniqueId);
		
		if(weaponToBreak == null || !(player.getTarget() instanceof Npc))
			return;

		if(!weaponToBreak.hasFusionedItem())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOUND_ERROR_NOT_AVAILABLE(weaponToBreak.getNameID()));
			return;
		}
	
		weaponToBreak.setFusionedItem(0);
		ItemService.removeAllFusionStone(player,weaponToBreak);
		DAOManager.getDAO(InventoryDAO.class).store(weaponToBreak, player.getObjectId());
		
		PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(weaponToBreak));
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUNDED_ITEM_DECOMPOUND_SUCCESS(weaponToBreak.getNameID()));
		
	}
	
}
