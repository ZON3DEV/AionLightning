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
package org.openaion.gameserver.model.gameobjects.player;
import java.sql.Timestamp;

import org.openaion.gameserver.model.gameobjects.PersistentState;


/**
 * @author HellBoy
 *
 */
public class Guild
{
	private int guildId;
	private int lastQuest;
	private Timestamp completeTime;
	private int currentQuest;
	
	private PersistentState persistentState;


    public Guild(int guildId, int lastQuest, Timestamp completeTime, int currentQuest)
	{
		super();
		this.guildId = guildId;
		this.lastQuest = lastQuest;
		this.completeTime = completeTime;
		this.currentQuest = currentQuest;
	}
	/**
	 * @return the guildId
	 */
	public int getGuildId()
	{
		return guildId;
	}
	
	/**
	 * @param guildId the guildId to set
	 */
	public void setGuildId(int guildId)
	{
		this.guildId = guildId;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * @return lastQuest
	 */
	public int getLastQuest()
	{
		return lastQuest;
	}
	
	/**
	 * @param lastQuest the lastQuest to set
	 */
	public void setLastQuest(int lastQuest)
	{
		this.lastQuest = lastQuest;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * @return completeTime
	 */
	public Timestamp getCompleteTime()
	{
		return completeTime;
	}
	
	/**
	 * @param completeTime the completeTime to set
	 */
	public void setCompleteTime(Timestamp completeTime)
	{
		this.completeTime = completeTime;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * @return currentQuest
	 */
	public int getCurrentQuest()
	{
		return currentQuest;
	}
	
	/**
	 * @param currentQuest the currentQuest to set
	 */
	public void setCurrentQuest(int currentQuest)
	{
		this.currentQuest = currentQuest;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * @return the persistentState
	 */
	public PersistentState getPersistentState()
	{
		return persistentState;
	}
	
	/**
	 * @param persistentState the persistentState to set
	 */
	public void setPersistentState(PersistentState persistentState)
	{
		switch(persistentState)
		{
			case UPDATE_REQUIRED:
				if(this.persistentState == PersistentState.NEW)
					break;
			default:
				this.persistentState = persistentState;
		}
	}	
}
