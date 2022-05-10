package org.openaion.commons.log4j.filters;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Log4J filter that looks if there is chat log present in the logging event and accepts event if present. Otherwise it
 * blocks filtring.
 * 
 * @author Divinity
 */
public class ChatLogFilter extends Filter
{
	/**
	 * Decides what to do with logging event.<br>
	 * This method accepts only log events that contain exceptions.
	 * 
	 * @param loggingEvent
	 *            log event that is going to be filtred.
	 * @return {@link org.apache.log4j.spi.Filter#ACCEPT} if chatlog, {@link org.apache.log4j.spi.Filter#DENY}
	 *         otherwise
	 */
	@Override
	public int decide(LoggingEvent loggingEvent)
	{
		Object message = loggingEvent.getMessage();

		if (((String) message).startsWith("[MESSAGE]"))
		{
			return ACCEPT;
		}

		return DENY;
	}
}
