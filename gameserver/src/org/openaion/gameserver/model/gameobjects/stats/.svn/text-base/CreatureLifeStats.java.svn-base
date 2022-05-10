/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package org.openaion.gameserver.model.gameobjects.stats;

import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.services.LifeStatsRestoreService;
import org.openaion.gameserver.skill.effect.EffectId;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author ATracer
 *
 */
public abstract class CreatureLifeStats<T extends Creature>
{
	private static final Logger log = Logger.getLogger(CreatureLifeStats.class);

	protected int currentHp;
	protected int currentMp;

	protected boolean alreadyDead = false;
	
	protected boolean calledOnDie = false;
	
	protected Creature owner;

	private ReentrantLock hpLock = new ReentrantLock();
	private ReentrantLock mpLock = new ReentrantLock();
	
	protected Future<?> lifeRestoreTask;

	public CreatureLifeStats(Creature owner, int currentHp, int currentMp)
	{
		super();
		this.owner = owner;
		this.currentHp = currentHp;
		this.currentMp = currentMp;
	}
	
	/**
	 * @return the owner
	 */
	public Creature getOwner()
	{
		return owner;
	}

	/**
	 * @return the currentHp
	 */
	public int getCurrentHp()
	{
		return currentHp;
	}

	/**
	 * @return the currentMp
	 */
	public int getCurrentMp()
	{
		return currentMp;
	}

	/**
	 * @return maxHp of creature according to stats
	 */
	public int getMaxHp()
	{
		int maxHp =  this.getOwner().getGameStats().getCurrentStat(StatEnum.MAXHP);
		if(maxHp == 0)
		{
			maxHp = 1;
			log.warn("CHECKPOINT: maxhp is 0 :" + this.getOwner().getGameStats());
		}
		return maxHp;
	}
	
	/**
	 * @return maxMp of creature according to stats
	 */
	public int getMaxMp()
	{
		return this.getOwner().getGameStats().getCurrentStat(StatEnum.MAXMP);
	}
	
	/**
	 * @return the alreadyDead
	 * There is no setter method cause life stats should be completely renewed on revive
	 */
	public boolean isAlreadyDead()
	{
		return alreadyDead;
	}

	/**
	 *  This method is called whenever caller wants to absorb creatures's HP
	 * @param value
	 * @param attacker
	 * @return currentHp
	 */
	public int reduceHp(int value, Creature attacker)
	{
		return this.reduceHp(value, attacker, true);
	}
	
	public int reduceHp(int value, Creature attacker, boolean callOnDie)
	{
		hpLock.lock();
		try
		{
			int newHp = this.currentHp - value;

			if(newHp < 0)
			{
				newHp = 0;
				if(!alreadyDead)
				{
					alreadyDead = true;
				}			
			}
			this.currentHp = newHp;
		}
		finally
		{
			hpLock.unlock();
		}
		
		onReduceHp();

		if(alreadyDead && callOnDie && !calledOnDie)
		{
			calledOnDie = true;
			getOwner().getController().onDie(attacker);
		}

		return currentHp;
	}

	/**
	 *  This method is called whenever caller wants to absorb creatures's HP
	 * @param value
	 * @return currentMp
	 */
	public int reduceMp(int value)
	{
		mpLock.lock();
		try
		{
			int newMp = this.currentMp - value;
				
			if(newMp < 0)
				newMp = 0;

			this.currentMp = newMp;	
		}
		finally
		{
			mpLock.unlock();
		}
		
		onReduceMp();

		return currentMp;
	}
	
	
	protected void sendAttackStatusPacketUpdate(TYPE type, int value, int skillId, int logId)
	{
		if(owner == null)
		{
			return;
		}
		
		PacketSendUtility.broadcastPacket(owner, new SM_ATTACK_STATUS(owner, type, value, skillId, logId));		
	}

	/**
	 *  This method is called whenever caller wants to restore creatures's HP
	 * @param value
	 * @return currentHp
	 */
	public int increaseHp(TYPE type, int value)
	{
		return this.increaseHp(type, value, 0, 170);
	}
	public int increaseHp(TYPE type, int value, int skillId, int logId)
	{
		if(this.getOwner().getEffectController().isAbnormalSet(EffectId.DISEASE))
			return currentHp;
		
		hpLock.lock();
		try
		{
			if(isAlreadyDead())
			{
				return 0;
			}
			int newHp = this.currentHp + value;
			if(newHp > getMaxHp())
			{
				newHp = getMaxHp();
			}
			if(currentHp != newHp)
			{
				this.currentHp = newHp;		
			}
		}
		finally
		{
			hpLock.unlock();
		}
		
		onIncreaseHp(type, value, skillId, logId);
		
		return currentHp;
	}

	/**
	 * This method is called whenever caller wants to restore creatures's MP
	 * @param value
	 * @return currentMp
	 */
	public int increaseMp(TYPE type, int value)
	{
		return this.increaseMp(type, value, 0, 170);
	}
	public int increaseMp(TYPE type, int value, int skillId, int logId)
	{
		mpLock.lock();
		try
		{
			if(isAlreadyDead())
			{
				return 0;
			}
			int newMp = this.currentMp + value;

			if(newMp > getMaxMp())
			{
				newMp = getMaxMp();
			}
			if(currentMp != newMp)
			{
				this.currentMp = newMp;
			}
		}	
		finally
		{
			mpLock.unlock();
		}

		onIncreaseMp(type, value, skillId, logId);

		return currentMp;
	}
	
	/**
	 *  Restores HP with value set as HP_RESTORE_TICK
	 */
	public void restoreHp()
	{
		increaseHp(TYPE.NATURAL_HP, getOwner().getGameStats().getCurrentStat(StatEnum.REGEN_HP));
	}
	
	/**
	 *  Restores HP with value set as MP_RESTORE_TICK
	 */
	public void restoreMp()
	{
		increaseMp(TYPE.NATURAL_MP, getOwner().getGameStats().getCurrentStat(StatEnum.REGEN_MP));
	}

	/**
	 *  Will trigger restore task if not already
	 */
	protected void triggerRestoreTask()
	{
		if(lifeRestoreTask == null && !alreadyDead)
		{
			this.lifeRestoreTask = LifeStatsRestoreService.getInstance().scheduleRestoreTask(this);
		}
	}

	/**
	 *  Cancel currently running restore task
	 */
	public void cancelRestoreTask()
	{
		if(lifeRestoreTask != null && !lifeRestoreTask.isCancelled())
		{
			lifeRestoreTask.cancel(false);
			this.lifeRestoreTask = null;
		}
	}
	
	/**
	 * 
	 * @return true or false
	 */
	public boolean isFullyRestoredHpMp()
	{
		return getMaxHp() == currentHp && getMaxMp() == currentMp;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFullyRestoredHp()
	{
		return getMaxHp() == currentHp;
	}
	
	public boolean isFullyRestoredMp()
	{
		return getMaxMp() == currentMp;
	}
	
	/**
	 * The purpose of this method is synchronize current HP and MP with updated MAXHP and MAXMP stats 
	 * This method should be called only on creature load to game or player level up
	 */
	public void synchronizeWithMaxStats()
	{
		int maxHp = getMaxHp();
		if(currentHp != maxHp)
			currentHp = maxHp;
		int maxMp = getMaxMp();
		if(currentMp != maxMp)
			currentMp = maxMp;
	}
	
	/**
	 * The purpose of this method is synchronize current HP and MP with MAXHP and MAXMP when max
	 * stats were decreased below current level
	 * 
	 * 
	 */
	public void updateCurrentStats()
	{
		int maxHp = getMaxHp();
		if(maxHp < currentHp)
			currentHp = maxHp;
		
		int maxMp = getMaxMp();
		if(maxMp < currentMp)
			currentMp = maxMp;
		
		if(!isFullyRestoredHpMp())
			triggerRestoreTask();
	}
	
	/**
	 * 
	 * @return HP percentage 0 - 100
	 */
	public int getHpPercentage()
	{
		return (int) (100L * currentHp / getMaxHp());
	}
	
	/**
	 * 
	 * @return MP percentage 0 - 100
	 */
	public int getMpPercentage()
	{
		return 100 * currentMp / getMaxMp();
	}
	
	protected abstract void onIncreaseMp(TYPE type, int value, int skillId, int logId);
	
	protected abstract void onReduceMp();
	
	protected abstract void onIncreaseHp(TYPE type, int value, int skillId, int logId);
	
	protected abstract void onReduceHp();

	/**
	 * 
	 * @param value
	 * @return
	 */
	public int increaseFp(TYPE type, int value)
	{
		return 0;
	}

	/**
	 * @return
	 */
	public int getCurrentFp()
	{
		return 0;
	}
	
	/**
	 * Cancel all tasks when player logout
	 */
	public void cancelAllTasks()
	{
		cancelRestoreTask();
	}
	
	/**
	 * This method can be used for Npc's to fully restore its HP
	 * and remove dead state of lifestats
	 * 
	 * @param hpPercent
	 */
	public void setCurrentHpPercent(int hpPercent)
	{
		hpLock.lock();
		try
		{
			int maxHp = getMaxHp();
			this.currentHp = (int)((long)maxHp * hpPercent / 100);
			
			if(this.currentHp > 0)
			{
				this.alreadyDead = false;
				this.calledOnDie = false;
			}
		}
		finally
		{
			hpLock.unlock();
		}
	}
	
	/**
	 * @param hp
	 */
	public void setCurrentHp(int hp)
	{
		boolean callOnReduceHp = false;
		
		hpLock.lock();
		try
		{
			this.currentHp = hp;
			
			if(this.currentHp > 0)
			{
				this.alreadyDead = false;
				this.calledOnDie = false;
			}
			
			if(this.currentHp < getMaxHp())
				callOnReduceHp = true;
		}
		finally
		{
			hpLock.unlock();
		}
		
		if (callOnReduceHp)
			onReduceHp();
	}
	
	public int setCurrentMp(int value)
	{
		mpLock.lock();
		try
		{
			int newMp = value;

			if(newMp < 0)
				newMp = 0;

			this.currentMp = newMp;	
		}
		finally
		{
			mpLock.unlock();
		}
		
		onReduceMp();

		return currentMp;
	}

	/**
	 * This method can be used for Npc's to fully restore its MP
	 * 
	 * @param mpPercent
	 */
	public void setCurrentMpPercent(int mpPercent)
	{
		mpLock.lock();
		try
		{
			int maxMp = getMaxMp();
			this.currentMp = maxMp * mpPercent / 100;
		}
		finally
		{
			mpLock.unlock();
		}
	}

	/**
	 * This method should be called after creature's revival
	 * For creatures - trigger hp regeneration
	 * For players - trigger hp/mp/fp regeneration (in overriding method)
	 */
	public void triggerRestoreOnRevive()
	{
		this.triggerRestoreTask();
	}
	
}
