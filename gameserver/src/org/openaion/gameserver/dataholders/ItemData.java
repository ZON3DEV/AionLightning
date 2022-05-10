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
package org.openaion.gameserver.dataholders;

import gnu.trove.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openaion.gameserver.model.items.ItemBonus;
import org.openaion.gameserver.model.templates.bonus.InventoryBonusType;
import org.openaion.gameserver.model.templates.item.ItemCategory;
import org.openaion.gameserver.model.templates.item.ItemRace;
import org.openaion.gameserver.model.templates.item.ItemTemplate;


/**
 * @author Luno, modified Rolandas
 *
 */
@XmlRootElement(name = "item_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemData
{
	@XmlElement(name="item_template")
	private List<ItemTemplate> its;
	
	private TIntObjectHashMap<ItemTemplate> items;
	
	// Bonus type, level -> to item list
	private Hashtable<InventoryBonusType, TreeMap<Integer, List<Integer>>> 
		itemsByBonus;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		items = new TIntObjectHashMap<ItemTemplate>();
		itemsByBonus = new Hashtable<InventoryBonusType, 
		TreeMap<Integer, List<Integer>>>();
		for(ItemTemplate it: its)
		{
			items.put(it.getTemplateId(), it);
			ItemBonus bonusInfo = it.getBonusInfo();
			if(bonusInfo != null) {
				InventoryBonusType bonusType = bonusInfo.getBonusType();

				TreeMap<Integer, List<Integer>> map = itemsByBonus.get(bonusType);
				if(map == null) {
					map = new TreeMap<Integer, List<Integer>>();
					itemsByBonus.put(bonusType, map);
				}
				String[] bonusLevels = bonusInfo.getBonusLevels().split(",");
				for(int i = 0; i < bonusLevels.length; i++)
				{
					int bonusLevel = Integer.parseInt(bonusLevels[i]);
					List<Integer> list = map.get(bonusLevel);
					if(list == null)
					{
						list = new ArrayList<Integer>();
						map.put(bonusLevel, list);
					}
					list.add(it.getTemplateId());
				}
			}
			else if (it.getItemCategory() == ItemCategory.HOLYSTONE ||
					 it.getItemCategory() == ItemCategory.ENCHANTSTONE) 
			{
				InventoryBonusType bonusType = it.getItemCategory() == ItemCategory.HOLYSTONE ?
					InventoryBonusType.GODSTONE : InventoryBonusType.ENCHANT;
				mapTemplateBonus(it, bonusType);
			} else if (it.getIsWorldDrop()) {
				if (it.getOriginRace() == ItemRace.ALL)
					mapTemplateBonus(it, InventoryBonusType.WORLD_DROP_B);
				else if (it.getOriginRace() == ItemRace.ELYOS)
					mapTemplateBonus(it, InventoryBonusType.WORLD_DROP_E);
				else
					mapTemplateBonus(it, InventoryBonusType.WORLD_DROP_A);
			}
		}
		its = null;
	}
	
	private void mapTemplateBonus(ItemTemplate template, InventoryBonusType mapType) 
	{
		TreeMap<Integer, List<Integer>> map = itemsByBonus.get(mapType);
		if(map == null) {
			map = new TreeMap<Integer, List<Integer>>();
			itemsByBonus.put(mapType, map);
		}
		List<Integer> list = map.get(template.getLevel());
		if(list == null)
		{
			list = new ArrayList<Integer>();
			map.put(template.getLevel(), list);
		}
		list.add(template.getTemplateId());		
	}
	
	public ItemTemplate getItemTemplate(int itemId)
	{
		return items.get(itemId);
	}

	/**
	 * @return items.size()
	 */
	public int size()
	{
		return items.size();
	}
	
	/*
	 * @param type - bonus type
	 * @param startLevel - start bonus level inclusive
	 * @param endLevel - end bonus level exclusive
	 * @return List of template ids for matching bonus type
	 */
	public List<Integer> getBonusItems(InventoryBonusType type, int startLevel, int endLevel)
	{
		List<Integer> list = new ArrayList<Integer>();
		synchronized(itemsByBonus) {
			TreeMap<Integer, List<Integer>> map = itemsByBonus.get(type);
			if(map == null)
				return list;
			SortedMap<Integer, List<Integer>> submap = map.subMap(startLevel, endLevel);
			if(submap.size() == 0)
				return list;
			for(List<Integer> itemsList : submap.values())
				list.addAll(itemsList);
		}
		return list;
	}
}
