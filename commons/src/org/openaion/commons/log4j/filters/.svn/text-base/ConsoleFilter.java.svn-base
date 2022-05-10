package org.openaion.commons.log4j.filters;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Log4J filter that looks if there is console present in the logging event and accepts event if present. Otherwise it
 * blocks filtring.
 * 
 * @author Divinity
 */
public class ConsoleFilter extends Filter
{
	/**
	 * Decides what to do with logging event.<br>
	 * This method accepts only log events that contain exceptions.
	 * 
	 * @param loggingEvent
	 *            log event that is going to be filtred.
	 * @return {@link org.apache.log4j.spi.Filter#DENY} if chat, {@link org.apache.log4j.spi.Filter#ACCEPT}
	 *         otherwise
	 */
	@Override
	public int decide(LoggingEvent loggingEvent)
	{
		Object message = loggingEvent.getMessage();

		if (message == null)
			return DENY;
		
		if (((String) message).startsWith("[MESSAGE]") 
			|| ((String) message).startsWith("[ADMIN COMMAND]")
			|| ((String) message).startsWith("[AUDIT]")
			|| ((String) message).startsWith("[ITEM]"))
		{
			return DENY;
		}

		return ACCEPT;
	}
}
