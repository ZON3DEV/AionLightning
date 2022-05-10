/*  
 *  This file is part of aion-unique <aion-unique.org>.
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
package org.openaion.gameserver.ai.desires.impl;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.desires.AbstractDesire;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.siege.FortressGeneral;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;

/**
 * @author ATracer
 *
 */
public class RestoreHealthDesire extends AbstractDesire
{	
	private Creature owner;
	private int restoreHpValue;
	private int restoreMpValue;

	public RestoreHealthDesire(Creature owner, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		restoreHpValue = owner.getLifeStats().getMaxHp() / 5;
		restoreMpValue = owner.getLifeStats().getMaxMp() / 5;
	}
	
	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if(owner == null || owner.getLifeStats().isAlreadyDead() || owner instanceof FortressGeneral)
			return false;
		
		if(!owner.isInCombat())
		{
			owner.getLifeStats().increaseHp(TYPE.NATURAL_HP, restoreHpValue);
			owner.getLifeStats().increaseMp(TYPE.NATURAL_MP, restoreMpValue);
		}

		if(owner.getLifeStats().isFullyRestoredHpMp())
		{
			ai.handleEvent(Event.RESTORED_HEALTH);
			return false;
		}
		return true;
	}


	@Override
	public int getExecutionInterval()
	{
		return 1;
	}

	@Override
	public void onClear()
	{
		// TODO Auto-generated method stub
	}
}
