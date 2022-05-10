package org.openaion.commons.log4j.exceptions;

/**
 * This error will be thrown if any of custom appenders won't be able to initialize. We have some logs there, so we
 * shouldn't do any actions with them in case of errors.
 * 
 * @author SoulKeeper
 */
public class AppenderInitializationError extends Error
{
	/**
	 * SerialID
	 */
	private static final long	serialVersionUID	= -6090251689433934051L;

	/**
	 * Creates new Error
	 */
	public AppenderInitializationError()
	{
	}

	/**
	 * Creates new error
	 * 
	 * @param message
	 *            error description
	 */
	public AppenderInitializationError(String message)
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
	public AppenderInitializationError(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Creates new error
	 * 
	 * @param cause
	 *            reason of this error
	 */
	public AppenderInitializationError(Throwable cause)
	{
		super(cause);
	}
}
