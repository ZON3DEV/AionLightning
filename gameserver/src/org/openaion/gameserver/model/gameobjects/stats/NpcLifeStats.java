/*
 * This file is part of aion-unique <aion-unique.com>.
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
package org.openaion.gameserver.model.gameobjects.stats;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.services.LifeStatsRestoreService;

/**
 * @author ATracer
 *
 */
public class NpcLifeStats extends CreatureLifeStats<Npc>
{
	/**
	 * 
	 * @param owner
	 */
	public NpcLifeStats(Npc owner)
	{
		super(owner, owner.getGameStats().getCurrentStat(StatEnum.MAXHP), owner.getGameStats().getCurrentStat(
			StatEnum.MAXMP));
	}

	@Override
	protected void onIncreaseHp(TYPE type, int value, int skillId, int logId)
	{
		sendAttackStatusPacketUpdate(type, value, skillId, logId);
	}

	@Override
	protected void onIncreaseMp(TYPE type, int value, int skillId, int logId)
	{
		// nothing todo
	}

	@Override
	protected void onReduceHp()
	{
		// nothing todo		
	}

	@Override
	protected void onReduceMp()
	{
		// nothing todo	
	}
	
	@Override
	protected void triggerRestoreTask()
	{
		if(lifeRestoreTask == null && !alreadyDead)
		{
			this.lifeRestoreTask = LifeStatsRestoreService.getInstance().scheduleHpRestoreTask(this);
		}
	}
}
