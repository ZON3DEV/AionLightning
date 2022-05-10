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
package org.openaion.gameserver.task;

import org.apache.log4j.Logger;
import org.openaion.commons.taskmanager.AbstractLockManager;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.GameServer;
import org.openaion.gameserver.GameServer.StartupHook;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author lord_rex and MrPoke 
 * 	based on l2j-free engines.
 * 
 * This can be used for periodic calls.
 */
public abstract class AbstractPeriodicTaskManager extends AbstractLockManager implements Runnable, StartupHook
{
	protected static final Logger	log	= Logger.getLogger(AbstractPeriodicTaskManager.class);

	private final int				period;

	public AbstractPeriodicTaskManager(int period)
	{
		this.period = period;

		GameServer.addStartupHook(this);

		log.info(getClass().getSimpleName() + ": Initialized.");
	}

	@Override
	public final void onStartup()
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 1000 + Rnd.get(period),
			Rnd.get(period - 5, period + 5));
	}

	@Override
	public abstract void run();
}
