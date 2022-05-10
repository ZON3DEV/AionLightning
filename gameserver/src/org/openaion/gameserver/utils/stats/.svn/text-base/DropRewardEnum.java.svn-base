/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package org.openaion.gameserver.utils.stats;

import java.util.NoSuchElementException;


public enum DropRewardEnum
{
	MINUS_11(-11, 0),
	MINUS_10(-10, 1),
	MINUS_9(-9, 10),
	MINUS_8(-8, 20),
	MINUS_7(-7, 30),
	MINUS_6(-6, 40),
	MINUS_5(-5, 50),
	MINUS_4(-4, 60),
	MINUS_3(-3, 90),
	MINUS_2(-2, 100),
	MINUS_1(-1, 100),
	ZERO(0, 100);

	private int dropRewardPercent;
	
	private int levelDifference;
	
	private DropRewardEnum(int levelDifference, int dropRewardPercent)
	{
		this.levelDifference = levelDifference;
		this.dropRewardPercent = dropRewardPercent;
	}
	
	public int rewardPercent()
	{
		return dropRewardPercent;
	}
	
	/**
	 * 
	 * @param levelDifference between two objects
	 * @return Drop reward percentage
	 */
	public static int dropRewardFrom(int levelDifference)
	{
		if(levelDifference < MINUS_11.levelDifference)
		{
			return MINUS_11.dropRewardPercent;
		}
		if(levelDifference > ZERO.levelDifference)
		{
			return ZERO.dropRewardPercent;
		}
	
		for(DropRewardEnum dropReward : values())
		{
			if(dropReward.levelDifference == levelDifference)
			{
				return dropReward.dropRewardPercent;
			}
		}
		
		throw new NoSuchElementException("Drop reward for such level difference was not found");
	}
}
