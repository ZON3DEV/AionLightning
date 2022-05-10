/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.ai.desires;

import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

import org.openaion.gameserver.ai.desires.impl.WalkDesire;


/**
 * This class represents desire queue, it's thread-safe. Desires can be added and removed. If desire is added - previous
 * desires will be checked, if same desire found then desire previous one will be removed from the queue
 * 
 * @author SoulKeeper
 * @see org.openaion.gameserver.ai.desires.Desire
 * @see org.openaion.gameserver.ai.desires.AbstractDesire
 */
public class DesireQueue extends PriorityBlockingQueue<Desire>
{
	private static final long	serialVersionUID	= 4903258139534394909L;


	/**
	 * Adds desire to the queue.
	 * <p/>
	 * <p/>
	 * When adding object this method checks first for the same object by {@link AbstractDesire#equals(Object)}, if such
	 * object found, next actions will be done:<br>
	 * <br>
	 * 1). Remove old desire instance from the list.<br>
	 * 2). Check if those desires are same instances by "==".<br>
	 * 3). If they are not the same instances, add desire power from old instance to new instance, if they are - do
	 * nothing.<br>
	 * <br>
	 * After all add new desire instance to the list.
	 * 
	 * @param desire
	 *            desire instance to add
	 */
	@Override
	public boolean add(Desire desire)
	{
		// Iterate over the list to find similiar desires
		Iterator<Desire> iterator = super.iterator();
		while(iterator.hasNext())
		{
			Desire iterated = iterator.next();

			// Find similiar desires by #equals method, they can be different instances.
			if(desire.equals(iterated))
			{
				// Remove the old desire from the list
				//iterator.remove();
				super.remove(desire);

				// If current desire instance was not at the list - increase it's power
				// by the value of another instance power
				// and after that add it to the list
				if(desire != iterated)
					desire.increaseDesirePower(iterated.getDesirePower());

				// Break iteration, desire list can't contain two same desires
				break;
			}
		}
	
		return super.add(desire);
	}
	
	/**
	 * Iterates over desires, you have to provide iteration handler and optionally filters.<br>
	 * <br>
	 * Handlers and filters can't call following methods:
	 * <ul>
	 * <li>{@link #addDesire(Desire)}</li>
	 * <li>{@link #poll()}</li>
	 * <li>{@link #removeDesire(Desire)}</li>
	 * </ul>
	 * <p/>
	 * However, method {@link #clear() can be called}.
	 * 
	 * @param handler
	 *            DesireIterationhandler that will be called on the iteration
	 * @param filters
	 *            optional filters that will prevent passing unneede desires to the handler
	 * @throws java.util.ConcurrentModificationException
	 *             only if called handler or filter modified this queue
	 * @see org.openaion.gameserver.ai.desires.DesireIteratorFilter
	 * @see org.openaion.gameserver.ai.desires.DesireIteratorFilter#isOk(Desire)
	 * @see org.openaion.gameserver.ai.desires.DesireIteratorHandler
	 * @see org.openaion.gameserver.ai.desires.DesireIteratorHandler#next(Desire , java.util.Iterator)
	 */
	public void iterateDesires(DesireIteratorHandler handler, DesireIteratorFilter... filters)
	{
		Iterator<Desire> iterator = super.iterator();
		outer: while(iterator.hasNext())
		{
			Desire desire = iterator.next();

			if(filters != null && filters.length > 0)
			{
				for(DesireIteratorFilter filter : filters)
				{
					if(!filter.isOk(desire))
					{
						continue outer;
					}
				}
			}

			handler.next(desire, iterator);
		}
	}

	@Override
	public void clear()
	{
		Desire desire = null;
		while((desire = super.poll()) != null)
			desire.onClear();
	}

	public boolean hasWalkingDesire() 
	{
		Iterator<Desire> iterator = super.iterator();
		while(iterator.hasNext())
		{
			Desire iterated = iterator.next();
			if(iterated instanceof WalkDesire)
				return true;
		}
		return false;
	}
}