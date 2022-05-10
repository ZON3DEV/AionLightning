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
package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.itemengine.actions.EnchantItemAction;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.item.ItemCategory;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;

/**
 * @author ATracer
 */
public class CM_MANASTONE extends AionClientPacket
{
	
	private int npcObjId;
	private int slotNum;
	
	private int actionType;
	private int targetFusedSlot;
	private int stoneUniqueId;
	private int targetItemUniqueId;
	private int supplementUniqueId;
	private ItemCategory actionCategory;
	
	/**
	 * @param opcode
	 */
	public CM_MANASTONE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		actionType = readC();
		targetFusedSlot = readC();
		//This is specifing which part of the weapon to socket with fused weapons, 
		//1 is primary weapon 2 is fused weapon. System needs to be rewritten accordingly. 
		targetItemUniqueId = readD();
		switch(actionType)
		{
			case 1:
			case 2:
				stoneUniqueId = readD();
				supplementUniqueId = readD();
				break;
			case 3:
				slotNum = readC();
				readC();
				readH();
				npcObjId = readD();
				break;
		}
	}

	@Override
	protected void runImpl()
	{
		AionObject npc = World.getInstance().findAionObject(npcObjId);
		Player player = getConnection().getActivePlayer();
		
		switch(actionType)
		{
			case 1: //enchant stone
			case 2: //add manastone
				EnchantItemAction action = new EnchantItemAction();
				Item manastone = player.getInventory().getItemByObjId(stoneUniqueId);
				Item targetItem = player.getInventory().getItemByObjId(targetItemUniqueId);
				if(targetItem == null)
				{
					targetItem = player.getEquipment().getEquippedItemByObjId(targetItemUniqueId);
				}
				
				if(actionType == 1)
					actionCategory = ItemCategory.ENCHANTSTONE;
				else
					actionCategory = ItemCategory.MAGICSTONE;
				
				if(manastone != null && manastone.getItemTemplate().getItemCategory() != actionCategory)
					return;
				
				if(manastone != null && targetItem != null && action.canAct(player, manastone, targetItem, targetFusedSlot))
				{
					Item supplement = player.getInventory().getItemByObjId(supplementUniqueId);
					action.act(player, manastone, targetItem, supplement, targetFusedSlot);
				}
				break;
			case 3: // remove manastone
				long price = player.getPrices().getPriceForService(500, player.getCommonData().getRace());
				if (player.getInventory().getKinahItem().getItemCount() < price)
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.NOT_ENOUGH_KINAH(price));
					return;
				}
				if(npc != null)
				{
					if(!player.getInventory().decreaseKinah(price))
						return;
					if(targetFusedSlot == 1)
						ItemService.removeManastone(player, targetItemUniqueId, slotNum);
					else 
						ItemService.removeFusionstone(player, targetItemUniqueId, slotNum);
				}
		}
	}
}
