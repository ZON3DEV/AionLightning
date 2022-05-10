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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 * @author Rolandas
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractInventoryBonus")
public abstract class AbstractInventoryBonus
{
	@XmlAttribute
	protected int bonusLevel;

	@XmlAttribute
	protected int count;
	
	public abstract InventoryBonusType getType();
	
	/**
	 * Check if the bonus can be applied.
	 * @param player
	 * @param itemId - item to check in the inventory
	 * @param questId - applies to specific questId, otherwise zero
	 * @return true if the bonus can be applied
	 */
	public abstract boolean canApply(Player player, int itemId, int questId);
	
	/**
	 * @param player
	 * @param item - item to add into the inventory
	 * @return true if successfully applied or ignored
	 */
	public abstract boolean apply(Player player, Item item);

	/**
	 * Gets the value of questLevel.
	 */
	public int getBonusLevel()
	{
		return bonusLevel;
	}

	/**
	 * Gets the value of count of the checked items. Zero if not checked
	 */
	public int getCount()
	{
		return count;
	}
	
}
