/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 *     Aion-unique is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Aion-unique is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openaion.gameserver.itemengine.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.item.ItemCategory;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.EnchantService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author Nemiroff Date: 16.12.2009
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnchantItemAction")
public class EnchantItemAction extends AbstractItemAction
{
	@XmlAttribute(name = "count")
	protected int sub_enchant_material_many;
	
	public int getEnchantCount()
	{
		return sub_enchant_material_many;
	}
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return canAct(player, parentItem, targetItem, 1);
	}

	public boolean canAct(Player player, Item parentItem, Item targetItem, int targetWeapon)
	{
		if(targetItem == null)
		{ // no item selected.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ERROR);
			return false;
		}
	
		if(!targetItem.getItemTemplate().isWeapon()	&& !targetItem.getItemTemplate().isArmor(true))
		{
			Logger.getLogger(this.getClass()).info("[AUDIT] Player: "+player.getName()+" is trying to enchant/socket non-weapon or non-armor. Hacking!");
			return false;
		}
			
		if(parentItem.getItemTemplate().getItemCategory() == ItemCategory.MAGICSTONE)
		{
			int Manaslots = 0;
			int manaStones = 0;
			if(targetWeapon == 1)
			{
				Manaslots = targetItem.getSockets(false);
				manaStones = targetItem.getItemStones().size();
			}
			else
			{
				Manaslots = targetItem.getSockets(true);
				manaStones = targetItem.getFusionStones().size();
			}

			if(manaStones >= Manaslots)
			{
				Logger.getLogger(this.getClass()).info("[AUDIT] Player: "+player.getName()+" is trying to socket more manastones than manaslots. Hacking!");
				return false;
			}
		}
				


		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem)
	{
		act(player, parentItem, targetItem, null, 1);
	}

	//necessary overloading to not change AbstractItemAction
	public void act(final Player player, final Item parentItem, final Item targetItem, final Item supplementItem, final int targetWeapon)
	{
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
		parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));
		player.getController().cancelTask(TaskId.ITEM_USE);
		player.getController().addNewTask(TaskId.ITEM_USE,
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(parentItem.getItemTemplate().getItemCategory() == ItemCategory.ENCHANTSTONE)
				{
					boolean result = EnchantService.enchantItem(player, parentItem, targetItem, supplementItem);
					PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem
						.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, result ? 1 : 2, 0));
				}
				else
				{
					boolean result = EnchantService.socketManastone(player, parentItem, targetItem, supplementItem, targetWeapon);
					PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem
						.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, result ? 1 : 2, 0));
				}
				
			}
			
		}, 5000));
	}
}
