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
package org.openaion.gameserver.model.gameobjects.player;

import java.util.Calendar;

import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.utils.stats.AbyssRankEnum;


/**
 * @author ATracer, Divinity
 *
 */
public class AbyssRank
{
	private int				dailyAP;
	private int				weeklyAP;
	private int				ap;
	private AbyssRankEnum	rank;
	private int				topRanking;
	private PersistentState	persistentState;
	private int				dailyKill;
	private	int				weeklyKill;
	private int				allKill;
	private int				maxRank;
	private int				lastKill;
	private int				lastAP;
	private long			lastUpdate;


	/**
	 * 
	 * @param dailyAP
	 * @param weeklyAP
	 * @param ap
	 * @param rank
	 * @param dailyKill
	 * @param weeklyKill
	 * @param allKill
	 * @param maxRank
	 * @param lastKill
	 * @param lastAP
	 * @param lastUpdate
	 */
	public AbyssRank(int dailyAP, int weeklyAP, int ap, int rank, int topRanking, int dailyKill, int weeklyKill, int allKill, int maxRank, int lastKill, int lastAP, long lastUpdate)
	{
		super();
		
		this.dailyAP	= dailyAP;
		this.weeklyAP	= weeklyAP;
		this.ap			= ap;
		this.rank		= AbyssRankEnum.getRankById(rank);
		this.topRanking	= topRanking;
		this.dailyKill	= dailyKill;
		this.weeklyKill	= weeklyKill;
		this.allKill	= allKill;
		this.maxRank	= maxRank;
		this.lastKill	= lastKill;
		this.lastAP		= lastAP;
		this.lastUpdate	= lastUpdate;
		
		doUpdate();
	}
	
	/**
	 * Add AP to a player (current player AP + added AP)
	 * 
	 * @param ap
	 */
	public void addAp(int ap)
	{	
		this.setAp(this.ap + ap);
	}
	
	public void addDWAp(int ap)
	{	
		dailyAP += ap;
		if (dailyAP < 0)
			dailyAP = 0;
				
		weeklyAP += ap;
		if (weeklyAP < 0)
			weeklyAP = 0;
	}
	
	/**
	 * @return The daily Abyss Pointn count
 	 */
	public int getDailyAP()
	{
		return dailyAP;
	}
	
	/**
	 * @return The weekly Abyss Point count
	 */
	public int getWeeklyAP()
	{
		return weeklyAP;
	}
	
	/**
	 * @return The all time Abyss Point count
	 */
	public int getAp()
	{
		return ap;
	}
	
	/**
	 * Set a new AP count
	 * 
	 * @param ap The ap to set
	 */
	public void setAp(int ap)
	{
		if(ap < 0)
			ap = 0;
		this.ap = ap;
		
		AbyssRankEnum newRank = AbyssRankEnum.getRankForAp(this.ap);
		if(newRank != this.rank)
			setRank(newRank);
		
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * @return the rank
	 */
	public AbyssRankEnum getRank()
	{
		return rank;
	}

	/**
	 * @return The top ranking of the current rank
	 */
	public int getTopRanking()
	{
		return topRanking;
	}
	
	/**
	 * @param topRanking
	 */
	public void setTopRanking(int topRanking)
	{
		this.topRanking = topRanking;
	}
	
	/**
	 * @return The daily count kill
	 */
	public int getDailyKill()
	{
		return dailyKill;
	}
	
	/**
	 * @return The weekly count kill
	 */
	public int getWeeklyKill()
	{
		return weeklyKill;
	}
	
	/**
     * @return all Kill
     */
    public int getAllKill()
    {
        return allKill;
    }
    /**
	 * Add one kill to a player
	 */
    public void setAllKill()
    {
 	   	this.dailyKill	+= 1;
       	this.weeklyKill	+= 1;
        this.allKill	+= 1;
    }
    
    /**
     * @return The last week count kill
     */
    public int getLastKill()
    {
    	return lastKill;
    }
   
    /**
    * @return The last week Abyss Point count
     */
    public int getLastAP()
    {
    	return lastAP;
    }
    
    /**
     * @return max Rank
     */
    public int getMaxRank()
    {
        return maxRank;
    }

	/**
	 * @param rank the rank to set
	 */
	public void setRank(AbyssRankEnum rank)
	{
        //set maxrank
		if(rank.getId() > this.maxRank)
            this.maxRank = rank.getId();
		
		if (rank.getQuota() != 0)
		{
			if(topRanking == 0)
			{
				rank = AbyssRankEnum.getRankById(AbyssRankEnum.getLastRankWithQuota().getId()-1);
			}
			else
			{
				while(topRanking > rank.getQuota())
				{
					rank = AbyssRankEnum.getRankById(rank.getId()-1);
				}
			}
		}
		
		this.rank = rank;
		
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
	
	/**
	 * @return The last update of the AbyssRank
	 */
	public long getLastUpdate()
	{
		return lastUpdate;
	}
	
	/**
	 * Make an update for the daily/weekly/last kill & ap counts
	 */
	public void doUpdate()
	{
		boolean needUpdate = false;
		Calendar lastCal = Calendar.getInstance();
		lastCal.setTimeInMillis(lastUpdate);
		
		Calendar curCal	 = Calendar.getInstance();
		curCal.setTimeInMillis(System.currentTimeMillis());
		
		// Checking the day - month & year are checked to prevent if a player come back after 1 month, the same day
		if (lastCal.get(Calendar.DAY_OF_MONTH) != curCal.get(Calendar.DAY_OF_MONTH) ||
			lastCal.get(Calendar.MONTH) != curCal.get(Calendar.MONTH) ||
			lastCal.get(Calendar.YEAR) != curCal.get(Calendar.YEAR))
		{
			this.dailyAP	= 0;
			this.dailyKill	= 0;
			needUpdate		= true;
		}
		
		// Checking the week - year is checked to prevent if a player come back after 1 year, the same week
		if (lastCal.get(Calendar.WEEK_OF_YEAR) != curCal.get(Calendar.WEEK_OF_YEAR) ||
			lastCal.get(Calendar.YEAR) != curCal.get(Calendar.YEAR))
		{
			this.lastKill	= this.weeklyKill;
			this.lastAP		= this.weeklyAP;
			this.weeklyKill	= 0;
			this.weeklyAP	= 0;
			needUpdate		= true;
		}
		
		// Finally, update the the last update
		this.lastUpdate = System.currentTimeMillis();
		
		if (needUpdate)
			setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
}

