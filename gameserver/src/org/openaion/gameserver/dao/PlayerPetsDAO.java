/*
 * This file is part of aion-unique <aion-unique.org>.
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

import org.openaion.commons.database.dao.DAO;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.ToyPet;


/**
 * @author Sylar - Kamui
 *
 */
public abstract class PlayerPetsDAO implements DAO
{
	@Override
	public final String getClassName()
	{
		return PlayerPetsDAO.class.getName();
	}
	
	public abstract void insertPlayerPet(Player player, int petId, int decoration, String name);
	
	public abstract void removePlayerPet(Player player, int petId);
	
	public abstract List<ToyPet> getPlayerPets(int playerId);
	
	public abstract ToyPet getPlayerPet(int playerId, int petId);
	
	public abstract void renamePlayerPet(Player player, int petId, String name);
	
	public abstract boolean savePet(Player player, ToyPet pet);
	
}
