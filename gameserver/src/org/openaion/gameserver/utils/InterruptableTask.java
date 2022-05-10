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
package org.openaion.gameserver.utils;

import java.util.concurrent.FutureTask;

import org.openaion.commons.utils.concurrent.ExecuteWrapper;

/**
 * @author Rolandas
 * Implementation of Runnable which could receive interrupt exceptions
 * without long task warnings when scheduled in the ThreadPoolManager
 */
public class InterruptableTask extends ExecuteWrapper
{

	FutureTask<?> task;
	
	public InterruptableTask(FutureTask<?> task, long noWarnMilliseconds)
	{
		super(task);
		this.task = task;
		this.noWarnMilliseconds = noWarnMilliseconds;
	}
	
	protected long noWarnMilliseconds = 0;
	
	@Override
	protected long getMaximumRuntimeInMillisecWithoutWarning()
	{
		return noWarnMilliseconds;
	}
	
	public boolean cancel()
	{
		return task.cancel(true);
	}
	
}
