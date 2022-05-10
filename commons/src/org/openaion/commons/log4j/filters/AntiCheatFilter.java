package org.openaion.commons.log4j.filters;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author kosyachok
 *
 */
public class AntiCheatFilter extends Filter
{
	@Override
	public int decide(LoggingEvent loggingEvent)
	{
		Object message = loggingEvent.getMessage();

		if (((String) message).startsWith("[CHEAT]"))
		{
			return ACCEPT;
		}

		return DENY;
	}
}
