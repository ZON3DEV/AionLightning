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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;


/**
 * @author blakawk
 *
 */
public class Acceptor extends Thread
{
	private ServerSocketChannel ssc;
	private Selector			selector;
	private SelectionKey        key;
	private ConnectionFactory   factory;
	private List<Reader>        readers;
	private List<Writer>        writers;
	private boolean             running = false;
	private Object              gate;
	private boolean             workersEnabled = false;
	private int                 workerThreads = 0;
	private int                 bufferCount = 0;
	private boolean				debugEnabled = false;
	private static final Logger log = Logger.getLogger(Acceptor.class);

	public Acceptor (String name, ServerSocketChannel ssc, ConnectionFactory factory, int readThreads, int writeThreads, boolean enableWorkers, int workerThreads, int bufferCount, int readTries, int writeTries, boolean debugEnabled) throws IOException
	{
		super(name);

		this.ssc = ssc;
		this.factory = factory;
		this.gate = new Object ();
		this.workersEnabled = enableWorkers;
		this.workerThreads = workerThreads;
		this.bufferCount = bufferCount;
		this.debugEnabled = debugEnabled;
		
		init(readThreads, writeThreads, readTries, writeTries);
	}

	private void init (int readThreadsCount, int writeThreadsCount, int readTries, int writeTries) throws IOException
	{
		initReaders(readThreadsCount, readTries);
		initWriters(writeThreadsCount, writeTries);
		
		if (readers.size() == 0 || writers.size() == 0)
		{
			log.error("Error in threads initialization !");
			System.exit(1);
		}

		selector = SelectorProvider.provider().openSelector();
		key = ssc.register(selector, SelectionKey.OP_ACCEPT, this);
	}

	private void initReaders (int readThreadsCount, int readTries) throws IOException
	{
		String name = getName()+"-reader-";
		Reader r;
		
		readers = new ArrayList<Reader> ();
		
		for (int i = 0; i < readThreadsCount; i ++)
		{
			r = new Reader(name+(i+1), workersEnabled, workerThreads, bufferCount, readTries, debugEnabled);
			readers.add(r);
			r.start();
		}
	}
	
	private void initWriters (int writeThreadsCount, int writeTries) throws IOException
	{
		String name = getName()+"-writer-";
		Writer w;
		
		writers = new ArrayList<Writer> ();
		
		for (int i = 0; i < writeThreadsCount; i ++)
		{
			w = new Writer(name+(i+1), writeTries, debugEnabled);
			writers.add(w);
			w.start();
		}
	}

	private Writer chooseWriter ()
	{
		return (Writer)choose(writers);
	}
	
	private Reader chooseReader ()
	{
		return (Reader)choose(readers);
	}
	
	private Processor choose (List<? extends Processor> threads)
	{
		int min = Integer.MAX_VALUE;
		int last = 0;
		boolean identics = true;
		int numberOfConnections = 0;

		Processor elected = null;

		for (Processor thread : threads)
		{
			numberOfConnections = thread.getNumberOfConnections();
			
			if (last == 0)
			{
				last = numberOfConnections;
			}
			else
			{
				if (last != numberOfConnections)
				{
					identics = false;
				}
			}

			if (numberOfConnections < min)
			{
				elected = thread;
				min = numberOfConnections;
			}
		}

		if (identics)
		{
			List<Processor> idles = new ArrayList<Processor> ();

			for (Processor thread : threads)
			{
				if (thread.isIdle())
				{
					idles.add(thread);
				}
			}

			if (idles.size() > 0)
				elected = idles.get(Rnd.get(idles.size()));
			else
				elected = threads.get(Rnd.get(threads.size()));
		}

		return elected;
	}
	
	public void manage (Connection c) throws IOException
	{
		Reader r = chooseReader();
		Writer w = chooseWriter();
		
		if (debugEnabled)
		{
			log.debug("Elected "+r.getName()+" and "+w.getName()+" to manage connection "+c);
		}

		synchronized (c)
		{
			r.manage(c);
			w.manage(c);
			
			c.initialized = true;

			c.notifyAll();
		}
		
		c.onInit();
	}
	
	public void close ()
	{
		synchronized (gate)
		{
			selector.wakeup();
			
			running = false;
			
			if (key.isValid())
			{
				key.attach(null);
				key.cancel();
			}
		}
		
		try {
			ssc.close();
			selector.close();
		} catch (IOException e) {
			log.error("Exception while closing server socket", e);
		}
		
		stopThreads(readers);
		stopThreads(writers);
	}

	/**
	 * 
	 */
	private void stopThreads(List<? extends Processor> processors)
	{
		for (Processor p : processors)
		{
			p.close();
		}
	}

	@Override
	public void run ()
	{
		SocketChannel sc;
		int selection = 0;
		Connection c = null;
		Iterator<SelectionKey> ski;
		SelectionKey sk;
		int errorsCount = 0;
		int errorThreshold = 1;

		running = true;
		
		synchronized (this)
		{
			notifyAll();
		}
		
		while (running)
		{
			try {
				selection = selector.select();

				if (selection > 0)
				{
					for (ski = selector.selectedKeys().iterator(); ski.hasNext(); )
					{
						sk = ski.next();
						ski.remove();

						if (!sk.isValid())
							continue;

						if (sk.readyOps() == SelectionKey.OP_ACCEPT)
						{
							sc = ssc.accept();
							sc.configureBlocking(false);

							c = factory.create(sc, debugEnabled);
							manage(c);
						}
					}
				}
				
				synchronized (gate) { }
				
				if (errorsCount > 0)
					errorsCount--;
			}
			catch (Exception e)
			{
				log.error(e.getClass().getSimpleName()+" while accepting connection", e);
				if (c != null)
				{
					c.close(false);
				}
				
				errorsCount ++;
				
				if (errorsCount >= errorThreshold)
				{
					log.fatal("Too much errors detected ("+errorsCount+"), terminating server...");
					close();
				}
			}
		}
		
		if (debugEnabled)
			log.debug(getName()+" stopped");
	}
}
