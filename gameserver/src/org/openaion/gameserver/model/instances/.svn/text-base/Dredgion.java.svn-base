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
package org.openaion.gameserver.model.instances;

import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.world.WorldMap;
import org.openaion.gameserver.world.WorldMapInstance;

/**
 *
 * @author ArkShadow
 * @redone kosyachok
 */

public class Dredgion extends WorldMapInstance
{
	private PlayerGroup secondGroup = null;	
	
	/**
	 * @param parent
	 * @param instanceId
	 */
	public Dredgion(WorldMap parent, int instanceId)
	{
		super(parent, instanceId);
	}
	
	public void registerSecondGroup(PlayerGroup group)
	{
		group.setGroupInstancePoints(0);
		this.secondGroup = group;
	}
	
	public void removeSecondGroup()
	{
		this.secondGroup = null;
	}
	
	public PlayerGroup getSecondGroup()
	{
		return secondGroup;
	}
}
