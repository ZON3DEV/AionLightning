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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PetRewardDescription")
@XmlSeeAlso({ org.openaion.gameserver.model.templates.pet.PetRewards.Result.class })
public class PetRewardDescription 
{

	@XmlAttribute(name = "item", required = true)
	protected int item;
	
	@XmlAttribute(name = "price", required = true)
	protected int price;
	
	@XmlAttribute(name = "chance")
	private float chance = 0;	
	
	@XmlAttribute(name = "name")
	protected String name;

	/**
	 * Gets the value of the item property.
	 * 
	 */
	public int getItem() 
	{
		return item;
	}

	/**
	 * Gets the value of the price property in love count
	 * 
	 */
	public int getPrice() 
	{
		return price;
	}
	
	/**
	 * @return the chance
	 */
	public float getChance()
	{
		return chance;
	}	

	@Override
	public String toString() 
	{
		return name;
	}

}
