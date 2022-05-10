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
package org.openaion.gameserver.ai.state.handler;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.desires.impl.AggressionDesire;
import org.openaion.gameserver.ai.desires.impl.WalkDesire;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.ai.state.AIState;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;


/**
 * @author ATracer
 *
 */
public class ActiveAggroStateHandler extends StateHandler
{
	@Override
	public AIState getState()
	{
		return AIState.ACTIVE;
	}
	
	/**
	 * State ACTIVE
	 * AI AggressiveMonsterAi
	 * AI GuardAi
	 */
	@Override
	public void handleState(AIState state, final AI<?> ai)
	{
		ai.clearDesires();
		final Npc owner = (Npc) ai.getOwner();
		
		owner.getKnownList().doOnAllObjects(new Executor<AionObject>(){
			@Override
			public boolean run(AionObject object)
			{
				if (object instanceof Creature && owner.isAggressiveTo((Creature)object))
				{
					ai.addDesire(new AggressionDesire(owner, AIState.ACTIVE.getPriority()));
					return false;
				}
				
				return true;
			}
		}, true);
		
		if (ai.desireQueueSize() == 0 && owner.hasWalkRoutes())
			ai.addDesire(new WalkDesire(owner, AIState.ACTIVE.getPriority()));	
		
		if(ai.desireQueueSize() == 0)
			ai.handleEvent(Event.NOTHING_TODO);
		else
			ai.schedule();
	}
}
