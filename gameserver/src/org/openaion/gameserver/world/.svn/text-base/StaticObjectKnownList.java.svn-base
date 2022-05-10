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
package org.openaion.gameserver.world;

import java.util.ArrayList;
import java.util.List;

import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;


/**
 * @author Mr. Poke
 *
 */
public class StaticObjectKnownList extends NpcKnownList
{
	/**
	 * @param owner
	 */
	public StaticObjectKnownList(VisibleObject owner)
	{
		super(owner);
	}

	/**
	 * Find objects that are in visibility range.
	 */
	@Override
	protected void findVisibleObjects()
	{
		if(owner == null || !owner.isSpawned())
			return;
		
		final List<VisibleObject> objectsToAdd = new ArrayList<VisibleObject>();
		
		for (MapRegion r : owner.getActiveRegion().getNeighbours())
		{
			r.doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player newObject)
				{
					if(newObject == owner || newObject == null)
						return true;
					
					if(!checkObjectInRange(owner, newObject))
						return true;
					
					objectsToAdd.add(newObject);

					return true;
				}
			}, true);
		}
		
		for (VisibleObject object : objectsToAdd)
		{
			owner.getKnownList().storeObject(object);
			object.getKnownList().storeObject(owner);
		}
		
		objectsToAdd.clear();
	}
}
