package org.openaion.loginserver.network.aion;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import org.openaion.commons.network.AConnection;
import org.openaion.commons.network.ConnectionFactory;
import org.openaion.commons.network.Dispatcher;


/**
 * ConnectionFactory implementation that will be creating AionConnections
 * 
 * @author -Nemesiss-
 * 
 */
public class AionConnectionFactoryImpl implements ConnectionFactory
{
	/**
	 * Create a new {@link org.openaion.commons.network.AConnection AConnection} instance.<br>
	 * 
	 * @param socket
	 *            that new {@link org.openaion.commons.network.AConnection AConnection} instance will represent.<br>
	 * @param dispatcher
	 *            to witch new connection will be registered.<br>
	 * @return a new instance of {@link org.openaion.commons.network.AConnection AConnection}<br>
	 * @throws IOException
	 * @see org.openaion.commons.network.AConnection
	 * @see org.openaion.commons.network.Dispatcher
	 */
	@Override
	public AConnection create(SocketChannel socket, Dispatcher dispatcher) throws IOException
	{
		return new AionConnection(socket, dispatcher);
	}
}
