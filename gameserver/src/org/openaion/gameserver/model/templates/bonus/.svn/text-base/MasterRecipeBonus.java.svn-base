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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.Storage;
import org.openaion.gameserver.model.items.ItemBonus;
import org.openaion.gameserver.model.templates.item.ItemQuality;
import org.openaion.gameserver.model.templates.item.ItemRace;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MasterRecipeBonus")
public class MasterRecipeBonus extends SimpleCheckItemBonus
{
	
	static final InventoryBonusType type = InventoryBonusType.MASTER_RECIPE;
	
	@XmlAttribute()
	protected int skillId;
	
	public int getSkillId()
	{
		return skillId;
	}
	
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
		
		return player.getSkillList().isSkillPresent(skillId);
	}
	
	@Override
	public boolean apply(Player player, Item item)
	{
		// if explicitly given, check only if matched types
		if(item != null)
		{
			ItemBonus bonusInfo = item.getItemTemplate().getBonusInfo();
			if(bonusInfo == null)
				return true;
			
			List<QuestItems> qi = Collections.singletonList(new QuestItems(item.getItemId(), 1));
			return ItemService.addItems(player, qi);
		}
		
		int heartLevel = DataManager.ITEM_DATA.getItemTemplate(checkItem).getLevel();
		
		// Randomize item quality for the bonus (default is rare)
		ItemQuality quality = ItemQuality.RARE;
		if(Rnd.get() * 100 < 30)
		{
			quality = ItemQuality.LEGEND;
			if (heartLevel == 60 && Rnd.get() * 100 < 30)
			{
				quality = ItemQuality.UNIQUE;
			}
		}
		
		int bonusLevel = (skillId & 0xF) << 10 | ((quality.ordinal() << 7) | heartLevel);
			
		List<Integer> designIds = DataManager.ITEM_DATA.getBonusItems(InventoryBonusType.MASTER_RECIPE, 
			bonusLevel, bonusLevel + 1);
		
		if(designIds.size() == 0)
			return true;
		
		List<Integer> finalIds = new ArrayList<Integer>();
		for (Integer id : designIds)
		{
			ItemTemplate template = ItemService.getItemTemplate(id);
			ItemRace itemRace = template.getRace();
			if(String.valueOf(itemRace) != String.valueOf(player.getCommonData().getRace()))
				continue;
			finalIds.add(id);
		}
		
		if(finalIds.size() == 0)
			return true;		
		
		int itemId = finalIds.get(Rnd.get(finalIds.size()));
		
		return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, 1)));
	}

	@Override
	public InventoryBonusType getType()
	{
		return type;
	}
}
