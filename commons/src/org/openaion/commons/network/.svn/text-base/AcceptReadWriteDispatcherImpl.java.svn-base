package org.openaion.commons.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is implementation of <code>Dispatcher</code> that may accept connections, read and write data.
 * 
 * @author -Nemesiss-
 * @see org.openaion.commons.network.Dispatcher
 * @see java.nio.channels.Selector
 */
public class AcceptReadWriteDispatcherImpl extends Dispatcher
{
	/**
	 * List of connections that should be closed by this <code>Dispatcher</code> as soon as possible.
	 */
	private final List<AConnection>	pendingClose	= new ArrayList<AConnection>();

	/**
	 * Constructor that accept <code>String</code> name and <code>DisconnectionThreadPool</code> dcPool as parameter.
	 * 
	 * @param name
	 * @param dcPool
	 * @throws IOException
	 * @see org.openaion.commons.network.DisconnectionThreadPool
	 */
	public AcceptReadWriteDispatcherImpl(String name, DisconnectionThreadPool dcPool) throws IOException
	{
		super(name, dcPool);
	}

	/**
	 * Process Pending Close connections and then dispatch <code>Selector</code> selected-key set.
	 * 
	 * @see org.openaion.commons.network.Dispatcher#dispatch()
	 */
	@Override
	void dispatch() throws IOException
	{
		int selected = selector.select();

		processPendingClose();

		if(selected != 0)
		{
			Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
			while(selectedKeys.hasNext())
			{
				SelectionKey key = selectedKeys.next();
				selectedKeys.remove();

				if(!key.isValid())
					continue;

				/** Check what event is available and deal with it */
				switch(key.readyOps())
				{
					case SelectionKey.OP_ACCEPT:
						this.accept(key);
						break;
					case SelectionKey.OP_READ:
						this.read(key);
						break;
					case SelectionKey.OP_WRITE:
						this.write(key);
						break;
					case SelectionKey.OP_READ | SelectionKey.OP_WRITE:
						this.read(key);
						if(key.isValid())
							this.write(key);
						break;
				}
			}
		}
	}

	/**
	 * Add connection to pendingClose list, so this connection will be closed by this <code>Dispatcher</code> as soon as
	 * possible.
	 * 
	 * @see org.openaion.commons.network.Dispatcher#closeConnection(org.openaion.commons.network.AConnection)
	 */
	@Override
	void closeConnection(AConnection con)
	{
		synchronized(pendingClose)
		{
			pendingClose.add(con);
		}
	}

	/**
	 * Process Pending Close connections.
	 */
	private void processPendingClose()
	{
		synchronized(pendingClose)
		{
			for(AConnection connection : pendingClose)
				closeConnectionImpl(connection);
			pendingClose.clear();
		}
	}
}
