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

import org.openaion.gameserver.model.Gender;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CutSceneBonus")
public class CutSceneBonus extends AbstractInventoryBonus
{

	static final InventoryBonusType type = InventoryBonusType.MOVIE;
	
	@XmlAttribute
	protected Gender gender;

	@XmlAttribute(name = "movieId")
	protected int movieId;
	
	@XmlAttribute()
	protected int checkItem;

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.itemengine.bonus.AbstractInventoryBonus#canApply(org.openaion.gameserver.model.gameobjects.player.Player, int)
	 */
	@Override
	public boolean canApply(Player player, int itemId, int questId)
	{
		PlayerCommonData data = player.getCommonData();
		boolean itemIdValid = false;
		if(itemId != 0) 
		{
			if(checkItem == 0 || count == 0)
				itemIdValid = true;
			else
				itemIdValid = itemId == checkItem;
			if(itemIdValid)
				return player.getInventory().getItemCountByItemId(itemId) >= count &&
					data.getGender().ordinal() == gender.ordinal();
		}
		else
			return player.getInventory().getItemCountByItemId(checkItem) >= count &&
				data.getGender().ordinal() == gender.ordinal();
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.itemengine.bonus.AbstractInventoryBonus#apply(org.openaion.gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public boolean apply(Player player, Item item)
	{
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movieId));
		return true;
	}

	/* (non-Javadoc)
	 * @see org.openaion.gameserver.model.templates.bonus.AbstractInventoryBonus#getType()
	 */
	@Override
	public InventoryBonusType getType()
	{
		return type;
	}
}
