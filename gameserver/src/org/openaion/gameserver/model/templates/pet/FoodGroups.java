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
package org.openaion.gameserver.model.templates.pet;

import gnu.trove.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.templates.item.ItemQuality;
import org.openaion.gameserver.model.templates.item.ItemTemplate;


/**
 * @author Rolandas
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FoodGroups", propOrder = { "groups" })

public class FoodGroups 
{
	@XmlElement(name="group")
	protected List<FoodGroups.Group> groups;

	@XmlTransient
	TIntObjectHashMap<Set<FoodType>> allFood;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		allFood = new TIntObjectHashMap<Set<FoodType>>();

		for (Group group : this.groups)
		{
			String[] ids = group.getValue().split(",");

			for (int i = 0; i < ids.length; i++)
			{
				try
				{
					int value = Integer.parseInt(ids[i]);
					Set<FoodType> set;
					if (allFood.containsKey(value))
						set = allFood.get(value);
					else
					{
						set = new HashSet<FoodType>();
						allFood.put(value, set);
					}
					set.add(group.getType());
				}
				catch (Exception e)
				{
				}
			}
		}
		
		this.groups.clear();
		this.groups = null;
	}

	/*
	 * returns Food groups in which item id was defined
	 */
	public List<FoodType> getFoodTypes(int itemId)
	{
		List<FoodType> list = new ArrayList<FoodType>();
		if (allFood.containsKey(itemId))
		{
			list.addAll(allFood.get(itemId));
		}
		
		ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(itemId);
		if (template.getItemQuality() == ItemQuality.JUNK)
		{
			if (template.getItemQuestId() == 0)
			{
				// MISC items not listed
				list.add(FoodType.MISC);
				return list;
			}
		}

		return list;
	}
	
	public 
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "value" })
	static class Group 
	{
		@XmlValue
		protected String value;

		@XmlAttribute(name = "type", required = true)
		protected FoodType type;

		public String getValue()
		{
			return value;
		}

		public FoodType getType() 
		{
			return type;
		}
	}

}