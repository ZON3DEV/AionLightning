package org.openaion.commons.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * This class is designed to handle situations when log4J was called like:<br>
 * 
 * <pre>
 * org.apache.log4j.Logger#log(Throwable)
 * </pre>
 * 
 * <p/>
 * In such cases this logger will take message from throwable and set it as message. Throwable will be threated as real
 * throwable, so no stacktraces would be lost.
 * 
 * @author SoulKeeper
 */
public class ThrowableAsMessageLogger extends Logger
{
	/**
	 * Creates new instance of this logger
	 * 
	 * @param name
	 *            logger's name
	 */
	protected ThrowableAsMessageLogger(String name)
	{
		super(name);
	}

	/**
	 * This method checks if message is instance of throwbale and throwable is null. If it is so it will move message to
	 * throwable and set localized message of throwable as message of the log record
	 * 
	 * @param fqcn
	 *            fully qualified class name, it would be used to get the line of call
	 * @param level
	 *            level of log record
	 * @param message
	 *            message of log record
	 * @param t
	 *            throwable, if any present
	 */
	@Override
	protected void forcedLog(String fqcn, Priority level, Object message, Throwable t)
	{

		if(message instanceof Throwable && t == null)
		{
			t = (Throwable) message;
			message = t.getLocalizedMessage();
		}

		super.forcedLog(fqcn, level, message, t);
	}
}
