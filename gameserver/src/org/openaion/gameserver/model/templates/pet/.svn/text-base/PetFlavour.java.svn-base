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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PetFlavour", propOrder = { "foods" })
@XmlSeeAlso({ org.openaion.gameserver.dataholders.PetFeedData.Flavour.class })

public class PetFlavour 
{
	@XmlElement(name = "food", required = true)
	protected List<PetFlavour.Food> foods;

	@XmlAttribute(name = "id", required = true)
	protected int id;

	@XmlAttribute(name = "count", required = true)
	protected int count;

	@XmlAttribute(name = "love_count")
	protected Integer loveCount;

	@XmlAttribute(name = "cd", required = true)
	protected int cd;

	@XmlAttribute(name = "desc", required = true)
	protected String desc;
	
	@XmlTransient
	private EnumMap<FoodType, List<PetRewards>> rewardsByFoodType;
	
	@XmlTransient
	static Set<Integer> rewardIds = new HashSet<Integer>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		Map<FoodType, List<PetRewards>> map = new HashMap<FoodType, List<PetRewards>>();
		
		// single flavour foods
		for (Food food : this.foods)
		{
			List<PetRewards> list = null;
			
			if (map.containsKey(food.getType()))
				list = map.get(food.getType());
			else
			{
				list = new ArrayList<PetRewards>();
				map.put(food.getType(), list);
			}
			list.add(food);
			
			for (PetRewardDescription result : food.getResults())
			{
				// create a global rewards set
				rewardIds.add(result.getItem());
			}
			
			// add first not loved food for the Healthy Food processing
			if (!food.isLoved() && !map.containsKey(FoodType.HEALTHY_1))
			{
				map.put(FoodType.HEALTHY_1, list);
			}
		}
		
		rewardsByFoodType = new EnumMap<FoodType, List<PetRewards>>(map);
		
		map.clear();
		map = null;
		
		this.foods.clear();
		this.foods = null;
	}
	
	public List<PetRewards> getRewards(FoodType foodType)
	{
		if (rewardsByFoodType.containsKey(foodType))
			return rewardsByFoodType.get(foodType);
		return new ArrayList<PetRewards>();
	}
	
	public static boolean isReward(int itemId)
	{
		return rewardIds.contains(itemId);
	}

	/**
	 * Gets the value of the id property.
	 * 
	 */
	public int getId() 
	{
		return id;
	}

	/**
	 * Gets the value of the count property.
	 */
	public int getCount() 
	{
		return count;
	}

	/**
	 * Gets the value of the loveCount property.
	 */
	public int getLoveCount() 
	{
		if (loveCount == null) 
			return 0;
		else
			return loveCount;
	}

	/**
	 * Gets the value of the cd property.
	 */
	public int getCd() 
	{
		return cd;
	}

	@Override
	public String toString() 
	{
		return desc;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Food extends PetRewards
	{
	}

}
