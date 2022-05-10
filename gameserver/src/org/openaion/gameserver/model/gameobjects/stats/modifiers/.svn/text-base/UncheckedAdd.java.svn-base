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
package org.openaion.gameserver.model.gameobjects.stats.modifiers;

import org.openaion.gameserver.model.gameobjects.stats.StatEnum;

/**
 * @author blakawk
 *
 */
/**
 * @author blakawk
 *
 */
public class UncheckedAdd extends AddModifier
{
	@Override
	public int apply(int baseValue, int currentValue)
	{
		int newValue = Math.round(value);
		
		if (isBonus())
		{
			return newValue;
		}
		
		return Math.round(currentValue + value);
	}
	
	public static UncheckedAdd newInstance (StatEnum stat, int value, boolean isBonus)
	{
		UncheckedAdd m = new UncheckedAdd ();
		m.setStat(stat);
		m.setValue(value);
		m.setBonus(isBonus);
		m.nextId();
		return m;
	}
}
