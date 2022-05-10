package org.openaion.commons.log4j.exceptions;

/**
 * This class is thrown when logging system cant be initialized
 * 
 * @author SoulKeeper
 */
public class Log4jInitializationError extends Error
{
	/**
	 * SerialID
	 */
	private static final long	serialVersionUID	= -628697707807736993L;

	/**
	 * Creates new Error
	 */
	public Log4jInitializationError()
	{
	}

	/**
	 * Creates new error
	 * 
	 * @param message
	 *            error description
	 */
	public Log4jInitializationError(String message)
	{
		super(message);
	}

	/**
	 * Creates new error
	 * 
	 * @param message
	 *            error description
	 * @param cause
	 *            reason of this error
	 */
	public Log4jInitializationError(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Creates new error
	 * 
	 * @param cause
	 *            reason of this error
	 */
	public Log4jInitializationError(Throwable cause)
	{
		super(cause);
	}
}
