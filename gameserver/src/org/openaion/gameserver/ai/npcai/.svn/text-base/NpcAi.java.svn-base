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
package org.openaion.gameserver.ai.npcai;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.ai.events.EventHandlers;
import org.openaion.gameserver.ai.events.handler.EventHandler;
import org.openaion.gameserver.ai.state.StateHandlers;
import org.openaion.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 *
 */
public class NpcAi extends AI<Npc>
{
	public NpcAi()
	{
		/**
		 * Event Handlers
		 */
		this.addEventHandler(EventHandlers.NOTHINGTODO_EH.getHandler());
		this.addEventHandler(EventHandlers.TIREDATTACKING_EH.getHandler());
		this.addEventHandler(EventHandlers.MOST_HATED_CHANGED_EH.getHandler());
		this.addEventHandler(EventHandlers.RESPAWNED_EH.getHandler());
		this.addEventHandler(EventHandlers.DIED_EH.getHandler());
		this.addEventHandler(EventHandlers.DESPAWN_EH.getHandler());
		this.addEventHandler(EventHandlers.TALK_EH.getHandler());
		this.addEventHandler(EventHandlers.BACKHOME_EH.getHandler());
		this.addEventHandler(EventHandlers.ATTACKED_EH.getHandler());
		
		/**
		 * State Handlers
		 */
		this.addStateHandler(StateHandlers.MOVINGTOHOME_SH.getHandler());
		this.addStateHandler(StateHandlers.ACTIVE_NPC_SH.getHandler());
		this.addStateHandler(StateHandlers.TALKING_SH.getHandler());
		this.addStateHandler(StateHandlers.ATTACKING_SH.getHandler());
		this.addStateHandler(StateHandlers.THINKING_SH.getHandler());
		this.addStateHandler(StateHandlers.RESTING_SH.getHandler());
	}

	@Override
	public void handleEvent(Event event)
	{
		super.handleEvent(event);
		
		//allow only handling event Event.DIED in dead stats
		//probably i need to define rules for which events could be handled in which state
		if(event != Event.DIED && owner.getLifeStats().isAlreadyDead())
			return;
		
		EventHandler eventHandler = eventHandlers.get(event);
		if(eventHandler != null)
			eventHandler.handleEvent(event, this);
	}
}
