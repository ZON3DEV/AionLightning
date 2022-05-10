package org.openaion.loginserver.utils;

import org.apache.log4j.Logger;

/**
 * @author -Nemesiss-
 */
public class ThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(ThreadUncaughtExceptionHandler.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		log.error("Critical Error - Thread: " + t.getName() + " terminated abnormaly: " + e, e);
		if (e instanceof OutOfMemoryError)
		{
			log.error("Server went out of memory, trying running GC to free some bytes ...", e);
			// TODO maybe restart thread
			System.gc();
			System.runFinalization();
			log.info("GC pass completed successfully.");
		}
		// TODO! some threads should be "restarted" on error
	}
}
