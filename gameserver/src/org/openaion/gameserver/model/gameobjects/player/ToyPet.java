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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.pet.FoodType;
import org.openaion.gameserver.model.templates.pet.PetFlavour;
import org.openaion.gameserver.model.templates.pet.PetRewardDescription;
import org.openaion.gameserver.model.templates.pet.PetRewards;
import org.openaion.gameserver.utils.InterruptableTask;


/**
 * @author Sylar
 *
 */
public class ToyPet
{

	private Player master;
	private int decoration;
	private String name;
	private int petId;
	private int feedCount;
	private int loveCount;
	private int exp;
	private PetFeedState feedState = PetFeedState.HUNGRY;
	private long cdStarted = 0;
	
	private Timestamp birthDay;
	
	private float x1 = 0;
	private float y1 = 0;
	private float z1 = 0;
	
	private int h = 0;
	
	private float x2 = 0;
	private float y2 = 0;
	private float z2 = 0;
	
	private boolean isFeeding = false;
	private InterruptableTask feedingTask;
	
	private PetFlavour flavour;

	public ToyPet()
	{
		
	}
	
	public Player getMaster()
	{
		return master;
	}
	
	public void setMaster(Player player)
	{
		this.master = player;
	}
	
	/**
	 * @return the databaseIndex
	 */
	public int getUid()
	{
		int hashCode = 0;
		hashCode += 1000000007 * master.getObjectId().hashCode();
		hashCode += 1000000009 * ((Integer)petId).hashCode();
		return hashCode;
	}
	
	/**
	 * For feed counts 10, 20, 40, 50, 100, 200
	 */
	static final byte[] progressBytes = new byte[] { (byte)128, 64, 32, 8, 8, 8 };
	
	public static short getFeedStep(int itemLevel, int maxCount)
	{
		int index = (int)(Math.log10(maxCount / 10) / Math.log10(2));
		byte smallest = progressBytes[index];
		
		int[] levelSteps = new int[6];
		for (int i = 0; i < 6; i++)
			levelSteps[i] = smallest + smallest * i * 2;
		
		return (short)levelSteps[itemLevel / 10 - 1];
	}

	/**
	 * @return the petId
	 */
	public int getPetId()
	{
		return petId;
	}

	/**
	 * @param petId the petId to set
	 */
	public void setPetId(int petId)
	{
		this.petId = petId;
	}

	/**
	 * @return the decoration
	 */
	public int getDecoration()
	{
		return decoration;
	}

	/**
	 * @param decoration the decoration to set
	 */
	public void setDecoration(int decoration)
	{
		this.decoration = decoration;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the x1
	 */
	public float getX1()
	{
		return x1;
	}

	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(float x1)
	{
		this.x1 = x1;
	}

	/**
	 * @return the y1
	 */
	public float getY1()
	{
		return y1;
	}

	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(float y1)
	{
		this.y1 = y1;
	}

	/**
	 * @return the z1
	 */
	public float getZ1()
	{
		return z1;
	}

	/**
	 * @param z1 the z1 to set
	 */
	public void setZ1(float z1)
	{
		this.z1 = z1;
	}

	/**
	 * @return the h
	 */
	public int getH()
	{
		return h;
	}

	/**
	 * @param h the h to set
	 */
	public void setH(int h)
	{
		this.h = h;
	}

	/**
	 * @return the x2
	 */
	public float getX2()
	{
		return x2;
	}

	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(float x2)
	{
		this.x2 = x2;
	}

	/**
	 * @return the y2
	 */
	public float getY2()
	{
		return y2;
	}

	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(float y2)
	{
		this.y2 = y2;
	}

	/**
	 * @return the z2
	 */
	public float getZ2()
	{
		return z2;
	}

	/**
	 * @param z2 the z2 to set
	 */
	public void setZ2(float z2)
	{
		this.z2 = z2;
	}
	
	/**
	 * @return the isFeeding
	 */
	public boolean isFeeding()
	{
		return isFeeding;
	}

	/**
	 * @param isFeeding the isFeeding to set
	 */
	public void setFeeding(boolean isFeeding)
	{
		this.isFeeding = isFeeding;
	}

	/**
	 * @return the feedingTask
	 */
	public InterruptableTask getFeedingTask()
	{
		return feedingTask;
	}

	/**
	 * @param feedingTask the feedingTask to set
	 */
	public void setFeedingTask(InterruptableTask feedingTask)
	{
		this.feedingTask = feedingTask;
	}

	/**
	 * @param feedCount the feedCount to set
	 */
	public void setFeedCount(int feedCount)
	{
		this.feedCount = feedCount;
	}

	/**
	 * @return the feedCount
	 */
	public int getFeedCount()
	{
		return feedCount;
	}

	/**
	 * @param loveCount the loveCount to set
	 */
	public void setLoveCount(int loveCount)
	{
		this.loveCount = loveCount;
	}

	/**
	 * @return the loveCount
	 */
	public int getLoveCount()
	{
		return loveCount;
	}

	/**
	 * @param birthDay the birthDay to set
	 */
	public void setBirthDay(Timestamp birthDay)
	{
		this.birthDay = birthDay;
	}

	/**
	 * @return the birthDay
	 */
	public Timestamp getBirthDay()
	{
		return birthDay;
	}

	/**
	 * @param exp the exp to set
	 */
	public void setExp(int exp)
	{
		this.exp = exp;
	}

	/**
	 * @return the exp
	 */
	public int getExp()
	{
		return exp;
	}
	
	public void addExp(int steps)
	{
		exp += steps;
		while (exp > 255)
		{
			exp -= 256;
			loveCount++;
		}
		if (exp == 0 && feedState != PetFeedState.FULL)
		{
			feedState = PetFeedState.valueOf(feedState.ordinal() + 1);
		}
	}

	/**
	 * @param feedState the feedState to set
	 */
	public void setFeedState(PetFeedState feedState)
	{
		this.feedState = feedState;
	}

	/**
	 * @return the feedState
	 */
	public PetFeedState getFeedState()
	{
		if (getFullRemainingTime() > 0)
			return PetFeedState.FULL;
		return feedState;
	}

	/**
	 * @return the remaining time in seconds until the feed cool down ends
	 */
	public int getFullRemainingTime()
	{
		if (feedState == PetFeedState.FULL && !isFeeding)
		{
			long stop = getCdStarted() + 600000;
			long remains = stop - Calendar.getInstance().getTimeInMillis();
			if (remains <= 0)
			{
				setFeedState(PetFeedState.HUNGRY);
				setCdStarted(0);
				return 0;
			}
			else
			{
				return (int) (remains / 1000);
			}
		}
		return 0;
	}

	/**
	 * @param cdStarted the cdStarted to set
	 */
	public void setCdStarted(long cdStarted)
	{
		this.cdStarted = cdStarted;
	}

	/**
	 * @return the cdStarted
	 */
	public long getCdStarted()
	{
		return cdStarted;
	}

	/**
	 * @param flavour the flavour to set
	 */
	public void setFlavour(PetFlavour flavour)
	{
		this.flavour = flavour;
	}

	/**
	 * @return the flavour
	 */
	public PetFlavour getFlavour()
	{
		return flavour;
	}

	public int getRewardId(ItemTemplate feedItemTemplate)
	{
		PetFlavour f = getFlavour();

		List<FoodType> foodTypes = DataManager.PET_FEED_DATA.getFoodGroups().getFoodTypes(feedItemTemplate.getTemplateId());
		List<PetRewards> rewards = null;
		FoodType foodGroup = FoodType.NOT_FOOD;
		
		for (FoodType foodType : foodTypes)
		{
			rewards = f.getRewards(foodType);
			if (rewards.size() != 0 && rewards.get(0).getResults().size() != 0)
			{
				foodGroup = foodType;
				break;
			}
			rewards = null;
		}
		
		if (rewards == null)
			return 0;
		
		// multiple rewards are not handled, maybe they have to be level dependant (???)
		PetRewards pr = rewards.get(0);
		PetRewardDescription random = pr.getRandomReward();
		if (random != null)
			return random.getItem();
		
		List<? extends PetRewardDescription> results = pr.getResults();
		for (int index = results.size() - 1; index >= 0; index--)
		{
			PetRewardDescription prd = results.get(index);
			if (prd.getPrice() == -1 || prd.getChance() != 0)
				continue; // price -1 not handled yet
			
			if (foodGroup == FoodType.HEALTHY_1)
			{
				int matchedIndex = feedItemTemplate.getLevel() / 10 - 1;
				if (matchedIndex >= index)
					return prd.getItem();
			}
			else if (prd.getPrice() <= this.getLoveCount())
				return prd.getItem();
		}
		
		return 0;
	}

}
