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
package org.openaion.gameserver.model.account;

import java.sql.Timestamp;
import java.util.List;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.PlayerAppearance;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.model.legion.Legion;
import org.openaion.gameserver.model.legion.LegionMember;


/**
 * This class is holding information about player, that is displayed on char selection screen, such as: player
 * commondata, player's appearance and creation/deletion time.
 * 
 * @see PlayerCommonData
 * @see PlayerAppearance
 * @author Luno
 * 
 */
public class PlayerAccountData
{
	private PlayerCommonData	playerCommonData;
	private PlayerAppearance	appereance;
	private List<Item>			equipment;
	private Timestamp			creationDate;
	private Timestamp			deletionDate;
	private LegionMember		legionMember;

	public PlayerAccountData(PlayerCommonData playerCommonData, PlayerAppearance appereance,
		List<Item> equipment, LegionMember legionMember)
	{
		this.playerCommonData = playerCommonData;
		this.appereance = appereance;
		this.equipment = equipment;
		this.legionMember = legionMember;
	}

	public Timestamp getCreationDate()
	{
		return creationDate;
	}

	/**
	 * Sets deletion date.
	 * 
	 * @param deletionDate
	 */
	public void setDeletionDate(Timestamp deletionDate)
	{
		this.deletionDate = deletionDate;
	}

	/**
	 * Get deletion date.
	 * 
	 * @return Timestamp date when char should be deleted.
	 */
	public Timestamp getDeletionDate()
	{
		return deletionDate;
	}

	/**
	 * Get time in seconds when this player will be deleted ( 0 if player was not set to be deleted )
	 * 
	 * @return deletion time in seconds
	 */
	public int getDeletionTimeInSeconds()
	{
		return deletionDate == null ? 0 : (int) (deletionDate.getTime() / 1000);
	}

	/**
	 * @return the playerCommonData
	 */
	public PlayerCommonData getPlayerCommonData()
	{
		return playerCommonData;
	}

	/**
	 * @param playerCommonData the playerCommonData to set
	 */
	public void setPlayerCommonData(PlayerCommonData playerCommonData)
	{
		this.playerCommonData = playerCommonData;
	}

	public PlayerAppearance getAppereance()
	{
		return appereance;
	}

	/**
	 * @param timestamp
	 */
	public void setCreationDate(Timestamp creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
	 * @return the legionMember
	 */
	public Legion getLegion()
	{
		return legionMember.getLegion();
	}
	
	/**
	 * Returns true if player is a legion member
	 * @return true or false
	 */
	public boolean isLegionMember()
	{
		return legionMember != null;
	}

	/**
	 * @return the equipment
	 */
	public List<Item> getEquipment()
	{
		return equipment;
	}

	/**
	 * @param equipment the equipment to set
	 */
	public void setEquipment(List<Item> equipment)
	{
		this.equipment = equipment;
	}
}
