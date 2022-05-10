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
import org.openaion.gameserver.ai.state.AIState;
import org.openaion.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 *
 */
public class ThinkingStateHandler extends StateHandler
{
	@Override
	public AIState getState()
	{
		return AIState.THINKING;
	}
	
	/**
	 * State THINKING
	 * AI MonsterAi
	 * AI AggressiveAi
	 */
	@Override
	public void handleState(AIState state, AI<?> ai)
	{
		ai.clearDesires();
		
		Npc owner = (Npc) ai.getOwner();
		if(owner.getAggroList().getMostHated() != null)
		{
			ai.setAiState(AIState.ATTACKING);
			return;
		}
		if(!owner.isAtSpawnLocation())
		{
			ai.setAiState(AIState.MOVINGTOHOME);
			return;
		}
        if (!owner.getLifeStats().isFullyRestoredHp())
        {
			ai.setAiState(AIState.RESTING);
			return;
		}
		ai.setAiState(AIState.ACTIVE);
	}
}
