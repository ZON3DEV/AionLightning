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

package org.openaion.commons.ngen.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

/**
 * @author blakawk
 *
 */
public abstract class Connection
{
	private static final Logger _log = Logger.getLogger(Connection.class);
	protected Logger log;
	private SocketChannel sc;
	private Writer writer;
	public boolean initialized = false;
	private boolean alreadyClosed = false;
	private boolean closeInProgress = false;
	protected boolean pendingClose = false;
	protected boolean isForcedClosing = false;
	private String src;
	private Mode mode = Mode.BINARY;
	protected boolean debugEnabled = false;
	
	public enum Mode {
		TEXT,
		BINARY
	}
	
	public Connection (SocketChannel sc, boolean debugEnabled)
	{
		this.sc     = sc;
		this.writer = null;
		this.src = sc.socket().getInetAddress().getHostAddress()+":"+sc.socket().getPort();
		this.log = Logger.getLogger(getClass());
		this.debugEnabled = debugEnabled;
	}
	
	public Connection (SocketChannel sc, boolean debugEnabled, Mode mode)
	{
		this(sc, debugEnabled);
		this.mode = mode;
	}
	
	public void setWriter (Writer writer)
	{
		this.writer = writer;
	}
	
	public SocketChannel channel ()
	{
		return sc;
	}
	
	protected void enableWriteInterest ()
	{
		if (alreadyClosed || closeInProgress)
			return;
		
		if (!initialized)
		{
			synchronized (this)
			{
				try { wait(); } catch (Exception e) { }
			}
		}
		
		writer.wakeup(this);
	}
	
	public Mode getMode ()
	{
		return mode;
	}
	
	protected boolean isWriteDisabled ()
	{
		return alreadyClosed || closeInProgress;
	}
	
	public void close (boolean serverClose)
	{
		if (alreadyClosed || closeInProgress)
			return;
		
		closeInProgress = true;
		
		writer.remove();
		
		if (sc.isOpen())
		{
			try {
				sc.socket().shutdownOutput();
				sc.close();
			}
			catch (IOException e)
			{
				if (debugEnabled)
					_log.error("IOException on closing socket for connection "+this, e);
			}
		}

		try {
			if (serverClose)
			{
				onServerClose();
			}
			else
			{
				onDisconnect();
			}
		} catch (Exception e) {
			if (debugEnabled)
				_log.error(e.getClass().getSimpleName()+" while closing connection "+this, e);
		}
		
		alreadyClosed = true;
	}
	
	public String getSource ()
	{
		return src;
	}
	
	public String toString ()
	{
		return src;
	}
	
	public boolean isPendingClose ()
	{
		return pendingClose;
	}
	
	abstract protected void onInit ();
	
	abstract protected void onDisconnect ();
	
	abstract protected void onServerClose ();
	
	abstract protected boolean processData(ByteBuffer data);
	
	abstract protected boolean writeData(ByteBuffer data);
}
