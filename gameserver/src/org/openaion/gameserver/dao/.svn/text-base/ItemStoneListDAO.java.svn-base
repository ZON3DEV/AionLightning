/*
 * This file is part of aion-unique <aionu-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.dao;

import java.util.List;
import java.util.Set;

import org.openaion.commons.database.dao.DAO;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.items.FusionStone;
import org.openaion.gameserver.model.items.GodStone;
import org.openaion.gameserver.model.items.ManaStone;


/**
 * @author ATracer
 *
 */
public abstract class ItemStoneListDAO implements DAO
{
	/**
	 *  Loads stones of item
	 *  
	 * @param item
	 */
	public abstract void load(List<Item> items);
	
	/**
	 * @param itemStones
	 */
	public abstract void storeManaStone(Set<ManaStone> itemStones);
	
	/**
	 * @param fusionStones
	 */
	public abstract void storeFusionStone(Set<FusionStone> fusionStones);

	/**
	 *  Saves stones of player
	 *  
	 * @param itemStoneList
	 */
	public abstract void save(Player player);
	
	/**
	 * @param godStone
	 */
	public abstract void store(GodStone godStone);
	
	@Override
	public String getClassName()
	{
		return ItemStoneListDAO.class.getName();
	}
}
