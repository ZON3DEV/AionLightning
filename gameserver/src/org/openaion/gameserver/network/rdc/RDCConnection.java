/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.rdc;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.openaion.commons.ngen.network.Connection;
import org.openaion.gameserver.network.rdc.commands.RDCACommandTable;
import org.openaion.gameserver.network.rdc.commands.RDCCommand;


/**
 * @author blakawk
 *
 */
public class RDCConnection extends Connection
{
	private String result;
	
	public RDCConnection(SocketChannel sc, boolean debugEnabled)
	{
		super(sc, debugEnabled, Mode.TEXT);
	}
	
	@Override
	protected void onDisconnect()
	{
		// nothing to do
	}

	@Override
	protected void onInit()
	{
		// nothing to do, maybe send a welcome packet ?
	}

	@Override
	protected void onServerClose()
	{
		// nothing to do
	}

	@Override
	protected boolean processData(ByteBuffer data)
	{
		String query = new String ();
		String[] queryData;
		RDCCommand command;
		
		while (data.hasRemaining())
		{
			query += (char)data.get();
		}
		
		queryData = query.trim().split(" ");
		
		command = RDCACommandTable.getCommandByName(queryData[0]);
		
		if(command != null)
		{
			if(queryData.length > 1)
			{
				String[] commandArgs = new String[queryData.length - 1];
				for(int i=1; i < queryData.length; i++)
				{
					commandArgs[i-1] = queryData[i];
				}
				result = command.handleRequest(commandArgs);
			}
			else
			{
				result = command.handleRequest(new String[0]);
			}
		}
		
		if (result != null && result.length() > 0)
			enableWriteInterest();
		else
			return false;
		
		return true;
	}

	@Override
	protected boolean writeData(ByteBuffer data)
	{
		data.asCharBuffer().put(result);
		data.limit(result.length()*2);
		
		pendingClose = true;
		
		return true;
	}

}
