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
import org.openaion.gameserver.model.templates.item.ItemRace;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.services.ItemService;


/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkOrderBonus")
public class WorkOrderBonus extends SimpleCheckItemBonus
{

	static final InventoryBonusType type = InventoryBonusType.TASK;
	
	@XmlAttribute()
	protected int skillId;
	
	
	public int getSkillId()
	{
		return skillId;
	}
	
	@Override
	public boolean apply(Player player, Item item)
	{
		int skillPoints = player.getSkillList().getSkillLevel(skillId);
		
		int startLvl = ((skillId & 0xF) << 10) | Math.max(0, skillPoints / 100 * 100 - 50);
		int endLvl = ((skillId & 0xF) << 10) | skillPoints;
		
		// materials and products from -50 up to current skill level
		List<Integer> itemIds = DataManager.ITEM_DATA.getBonusItems(type, startLvl, endLvl + 1);
		itemIds.addAll(itemIds); // increase chances twice;
		
		// recipes to skill level + 10, but not exceeding max limit
		startLvl = ((skillId & 0xF) << 10) | Math.max(0, skillPoints / 100 * 100 - 50);
		endLvl = skillPoints + 10;
		endLvl = ((skillId & 0xF) << 10) | Math.min(skillPoints + 100, endLvl);
		List<Integer> recipeIds = DataManager.ITEM_DATA.getBonusItems(InventoryBonusType.RECIPE, 
																	  startLvl, endLvl);
		itemIds.addAll(recipeIds);
		if(itemIds.size() == 0)
			return true;
		
		List<Integer> finalIds = new ArrayList<Integer>();
		for (Integer itemId : itemIds)
		{
			ItemTemplate template = ItemService.getItemTemplate(itemId);
			ItemRace itemRace = template.getOriginRace();
			if(String.valueOf(itemRace) != String.valueOf(player.getCommonData().getRace()) &&
				itemRace != ItemRace.ALL)
				continue;
			finalIds.add(itemId);
		}
		
		Collections.shuffle(finalIds);
		
		int itemId = finalIds.get(Rnd.get(finalIds.size()));
		int itemCount = 1;
		if(itemId >= 169400010 && itemId <= 169405025)
			itemCount = 5;
		return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, itemCount)));
	}

	@Override
	public InventoryBonusType getType()
	{
		return type;
	}

}
