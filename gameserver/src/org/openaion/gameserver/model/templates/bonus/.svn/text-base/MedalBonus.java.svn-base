/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.model.templates.bonus;

import java.util.Collections;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.model.items.ItemId;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MedalBonus")
public class MedalBonus extends SimpleCheckItemBonus
{

	static final InventoryBonusType type = InventoryBonusType.MEDAL;
	
	@Override
	public boolean canApply(Player player, int itemId, int questId)
	{
		if(!super.canApply(player, itemId, questId))
			return false;

		Storage storage = player.getInventory();
		if(storage.getItemCountByItemId(checkItem) < count)
			return false;
		else if(storage.isFull())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean apply(Player player, Item item)
	{
		int questId = player.getQuestCookie().getQuestId();
		int itemId;
		int count = 1;
		if (Rnd.get(1000) / 10f < 41.8)
		{
			if (Rnd.get(1000) / 10f < 48.4)
			{
				count = 2;
				itemId = checkItem == ItemId.SILVER_MEDAL.value() ? ItemId.SILVER_MEDAL.value()
																  : ItemId.GOLDEN_MEDAL.value();
			}
			else
				itemId = checkItem == ItemId.SILVER_MEDAL.value() ? ItemId.GOLDEN_MEDAL.value()
																  : ItemId.PLATINUM_MEDAL.value();
		}
		else if ((questId == 1717 || questId == 2717) && Rnd.get(100) < 4)
		{
			if (player.getCommonData().getRace() == Race.ASMODIANS)
				itemId = 182205668; // Rusted Spear
			else
				itemId = 182202156; // Quartz of Virtue
		}
		else
			itemId = ItemId.RUSTED_MEDAL.value();

		return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, count)));
	}

	@Override
	public InventoryBonusType getType()
	{
		return type;
	}

}
