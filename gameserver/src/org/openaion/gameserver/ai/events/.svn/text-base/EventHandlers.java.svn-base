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
package org.openaion.gameserver.ai.events;

import org.openaion.gameserver.ai.events.handler.AttackedEventHandler;
import org.openaion.gameserver.ai.events.handler.BackHomeEventHandler;
import org.openaion.gameserver.ai.events.handler.DespawnEventHandler;
import org.openaion.gameserver.ai.events.handler.DiedEventHandler;
import org.openaion.gameserver.ai.events.handler.EventHandler;
import org.openaion.gameserver.ai.events.handler.NotSeePlayerEventHandler;
import org.openaion.gameserver.ai.events.handler.NothingTodoEventHandler;
import org.openaion.gameserver.ai.events.handler.RespawnedEventHandler;
import org.openaion.gameserver.ai.events.handler.RestoredHealthEventHandler;
import org.openaion.gameserver.ai.events.handler.SeePlayerEventHandler;
import org.openaion.gameserver.ai.events.handler.TalkEventHandler;
import org.openaion.gameserver.ai.events.handler.TiredAttackingEventHandler;

/**
 * @author ATracer
 *
 */
public enum EventHandlers
{
	ATTACKED_EH(new AttackedEventHandler()),
	TIREDATTACKING_EH(new TiredAttackingEventHandler()),
	MOST_HATED_CHANGED_EH(new TiredAttackingEventHandler()),
	SEEPLAYER_EH(new SeePlayerEventHandler()),
	NOTSEEPLAYER_EH(new NotSeePlayerEventHandler()),
	RESPAWNED_EH(new RespawnedEventHandler()),
	BACKHOME_EH(new BackHomeEventHandler()),
	TALK_EH(new TalkEventHandler()),
	RESTOREDHEALTH_EH(new RestoredHealthEventHandler()),
	NOTHINGTODO_EH(new NothingTodoEventHandler()),
	DESPAWN_EH(new DespawnEventHandler()),
	DIED_EH(new DiedEventHandler());
	
	private EventHandler eventHandler;
	
	private EventHandlers(EventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
	}
	
	public EventHandler getHandler()
	{
		return eventHandler;
	}
}
