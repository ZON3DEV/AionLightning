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

/**
 * @author Rolandas
 *
 */
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "FoodType")
@XmlEnum
public enum FoodType {

	NOT_FOOD("NOT_FOOD"),
	DOPING("DOPING"),		// 2.5 version
	@XmlEnumValue("MISC1")
	MISC_1("MISC1"),
	@XmlEnumValue("MISC2")
	MISC_2("MISC2"),
	MISC("MISC"),
	FLUID("FLUID"),
	ARMOR("ARMOR"),
	THORN("THORN"),
	BONE("BONE"),
	BALAUR("BALAUR"),
	SOUL("SOUL"),
	@XmlEnumValue("HEALTHY1")
	HEALTHY_1("HEALTHY1"),
	@XmlEnumValue("HEALTHY2")
	HEALTHY_2("HEALTHY2"), // 2.5 version
	@XmlEnumValue("CASH1")
	CASH_1("CASH1"),
	@XmlEnumValue("CASH2")
	CASH_2("CASH2"),
	@XmlEnumValue("CASH3")
	CASH_3("CASH3"),
	@XmlEnumValue("CASH4")
	CASH_4("CASH4"),
	POWDER("POWDER"),
	CRYSTAL("CRYSTAL"),
	GEM("GEM");

	private final String value;

	FoodType(String v) 
	{
		value = v;
	}

	public String value() 
	{
		return value;
	}

	public static FoodType fromValue(String v) 
	{
		for (FoodType c: FoodType.values()) 
		{
			if (c.value.equals(v))
				return c;
		}

		throw new IllegalArgumentException(v);
	}

	public static boolean isLoved(FoodType foodType)
	{
		return foodType == POWDER || foodType == CRYSTAL || foodType == GEM;
	}

}
