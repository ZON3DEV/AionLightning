/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.controllers;

import org.apache.log4j.Logger;
import org.openaion.gameserver.controllers.movement.FlyRingObserver;
import org.openaion.gameserver.model.flyring.FlyRing;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;

import javolution.util.FastMap;


/**
 * @author blakawk
 *
 */
public class FlyRingController extends CreatureController<FlyRing>
{
	FastMap<Player, FlyRingObserver> observed = new FastMap<Player, FlyRingObserver>().shared();
	
	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		
		if(!(object instanceof Player))
		{
			return;
		}
		
		Player p = (Player)object;
		FlyRingObserver observer = new FlyRingObserver ((FlyRing)getOwner(),p);
		p.getObserveController().addObserver(observer);
		observed.put(p, observer);
		Logger.getLogger(FlyRingController.class).debug(getOwner().getName()+" sees "+p.getName());
	}
	
	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object,isOutOfRange);
		if (isOutOfRange && object instanceof Player)
		{
			Player p = (Player)object;
			if (observed.containsKey(p))
			{
				FlyRingObserver observer = observed.remove(p);
				observer.moved();
				p.getObserveController().removeObserver(observer);
			}
			Logger.getLogger(FlyRingController.class).debug(getOwner().getName()+" not sees "+p.getName());
		}
	}
}
