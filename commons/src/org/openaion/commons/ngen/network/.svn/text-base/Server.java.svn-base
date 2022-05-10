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
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import org.apache.log4j.Logger;

/**
 * @author blakawk
 *
 */
public class Server extends Thread
{
	private ServerConfig        config;
	private Acceptor			acceptor;
	private static final Logger log = Logger.getLogger(Server.class);
	private boolean             running = false;
	private boolean             debugEnabled = false;
	
	public Server (ServerConfig config)
	{
		super("server-"+config.name.replace(" ", "-").toLowerCase());
		
		this.config = config;
		this.debugEnabled = config.debugEnabled;
		
		if (config.readThreads <= 0 || config.writeThreads <= 0 || (config.enableWorkers && config.workerThreads <= 0))
		{
			if (config.enableWorkers)
			{
				throw new RuntimeException ("You should at least use one reader thread, one writer thread and one worker thread");
			}
			else
			{
				throw new RuntimeException ("You should at least use one write thread and one read thread");
			}
		}
	}
	
	public boolean isDebugEnabled ()
	{
		return debugEnabled;
	}
	
	@Override
	public void run ()
	{
		try
		{
			ServerSocketChannel ssc = ServerSocketChannel.open();

			ssc.configureBlocking(false);

			InetSocketAddress isa;
			if ("*".equals(config.hostname))
			{
				isa = new InetSocketAddress(config.port);
			}
			else
			{
				isa = new InetSocketAddress(config.hostname, config.port);
			}

			ssc.socket().bind(isa);
			acceptor = new Acceptor ("server-"+config.name.replace(" ", "-").toLowerCase()+"-acceptor", ssc, config.factory, config.readThreads, config.writeThreads, config.enableWorkers, config.workerThreads, config.bufferCount, config.readTries, config.writeTries, config.debugEnabled);
			acceptor.setDaemon(false);
			acceptor.start();
			
			synchronized (acceptor)
			{
				try { acceptor.wait(); } catch (InterruptedException e) { }
			}
			
			synchronized (this)
			{
				running = true;
				notifyAll();
			}
			
			log.info("Started "+getClass().getSimpleName()+" "+config.name+" listening on "+config.hostname+":"+config.port+" with "+config.readThreads+" readers (maximum retries: "+config.readTries+") and "+config.writeThreads+" writers (maximum retries: "+config.writeTries+")"+(config.enableWorkers?", with "+config.workerThreads+" worker threads using "+config.bufferCount+" buffers":"")+(config.debugEnabled?" (debug enabled)":""));
		}
		catch (IOException e)
		{
			log.error("Error starting server !", e);
			System.exit(1);
		}
		
		try { acceptor.join(); } catch (InterruptedException e) { }
		log.debug(getName()+" stopped");
	}
	
	public void manage (Connection c) throws IOException
	{
		synchronized (this)
		{
			while (!running)
			{
				try { wait(); } catch (InterruptedException e) { }
			}
		}
		
		acceptor.manage(c);
	}

	public void close()
	{
		if (acceptor!=null)
		{
			acceptor.close();
		}
	}
}
