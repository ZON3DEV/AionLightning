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

import java.sql.Timestamp;

/**
 * @author HellBoy
 *
 */
public class InstanceCD
{
	/**
	 * time when cooldown ends
	 */
	private Timestamp CDEnd = null;
	/**
	 * instanceId - instance copy number
	 */
	private int instanceId = 0;
	/**
	 * instanceId - instance copy number
	 */
	private int groupId = 0;
	
	/**
	 * 
	 * @param CDEnd
	 * @param instanceId
	 */
	public InstanceCD(Timestamp CDEnd, int instanceId, int groupId)
	{
		this.CDEnd = CDEnd;
		this.instanceId = instanceId;
		this.groupId = groupId;
	}
	
	/**
	 * @return CDEnd
	 */
	public Timestamp getCDEndTime()
	{
		return CDEnd;
	}

	/**
	 * @return the instanceId
	 */
	public int getInstanceId()
	{
		return instanceId;
	}

	/**
	 * @return
	 */
	public int getGroupId()
	{
		return groupId;
	}
}
