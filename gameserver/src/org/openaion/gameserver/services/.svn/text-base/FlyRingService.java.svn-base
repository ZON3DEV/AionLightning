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
package org.openaion.gameserver.services;

import org.apache.log4j.Logger;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.flyring.FlyRing;
import org.openaion.gameserver.model.templates.flyring.FlyRingTemplate;


/**
 * @author blakawk
 *
 */
public class FlyRingService
{
	Logger log = Logger.getLogger(FlyRingService.class);
	
	private static class SingletonHolder 
	{
		protected static final FlyRingService instance = new FlyRingService ();
	}
	
	public static final FlyRingService getInstance ()
	{
		return SingletonHolder.instance;
	}
	
	private FlyRingService ()
	{
		for (FlyRingTemplate t : DataManager.FLY_RING_DATA.getFlyRingTemplates())
		{
			FlyRing f = new FlyRing(t);
			f.spawn();
			log.debug("Added "+f.getName()+" at m="+f.getWorldId()+",x="+f.getX()+",y="+f.getY()+",z="+f.getZ());
		}
	}
}
