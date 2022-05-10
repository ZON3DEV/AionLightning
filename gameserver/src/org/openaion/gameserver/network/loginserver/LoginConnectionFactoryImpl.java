/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.loginserver;

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
public class LoginConnectionFactoryImpl implements ConnectionFactory
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

	/* (non-Javadoc)
	 * @see org.openaion.commons.network.ConnectionFactory#create(java.nio.channels.SocketChannel, org.openaion.commons.network.Dispatcher)
	 */
	@Override
	public AConnection create(SocketChannel socket, Dispatcher dispatcher) throws IOException
	{
		return new LoginServerConnection(socket, dispatcher);
	}
}
