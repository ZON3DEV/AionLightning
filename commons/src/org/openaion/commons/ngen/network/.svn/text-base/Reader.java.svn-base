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
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openaion.commons.utils.Rnd;


/**
 * @author blakawk
 *
 */
public class Reader extends Processor
{
	private Selector           selector, retrySelector;
	private List<Worker>       workers;
	private List<SelectionKey> keys;
	private boolean            workersEnabled;
	private int                workerThreads;
	private int                bufferCount;
	private boolean            waitingFreeWorker = false;
	private Worker elected = null;
	private int minQueueSize = Integer.MAX_VALUE;
	private boolean identics = true;
	private int last = 0;
	private int queueSize = 0;
	private int nbFull = 0;
	private List<Worker> idles = new ArrayList<Worker> ();
	private int i = 0;
	private Worker w;
	private SocketChannel sc = null;
	private int read = 0, bufferSize = 0, oldLimit = 0, totalRead = 0, maxTries = 3, tries = 0, retrySelection;
	private long timeLostInRetries = 0, before = 0;
	private Object gate;
	private boolean debugEnabled = false;

	public Reader(String name, boolean workersEnabled, int workerThreads, int bufferCount, int maxTries, boolean debugEnabled) throws IOException
	{
		super(name, workersEnabled);

		this.keys     = new ArrayList<SelectionKey> ();
		this.selector = Selector.open();
		this.retrySelector = Selector.open();
		this.workersEnabled = workersEnabled;
		this.workerThreads = workerThreads;
		this.bufferCount = bufferCount;
		this.gate = new Object ();
		this.maxTries = maxTries;
		this.debugEnabled = debugEnabled;

		if (workersEnabled)
			initWorkers();
	}

	private void initWorkers ()
	{
		workers = new ArrayList<Worker> ();

		for (int i = 0; i < workerThreads; i ++)
		{
			Worker worker = new Worker(getName()+"-worker-"+(i+1), this, bufferCount);
			workers.add(worker);
			worker.start();
		}
	}

	public boolean isWaitingFreeWorker ()
	{
		return waitingFreeWorker;
	}

	private Worker choose ()
	{
		idles.clear();
		nbFull = 0;
		last = 0;
		identics = true;
		elected = null;
		minQueueSize = Integer.MAX_VALUE;

		for (i = 0; i < workers.size(); i ++)
		{
			w = workers.get(i);

			if (!w.isIdle())
			{
				if (w.isFull())
				{
					nbFull++;
					continue;
				}

				queueSize = w.getQueueSize();

				if (last == 0)
				{
					last = queueSize;
				}
				else
				{
					if (last != queueSize)
					{
						identics = false;
					}
				}

				if (queueSize < minQueueSize)
				{
					minQueueSize = queueSize;
					elected = w;
				}
			}
			else
			{
				idles.add(w);
			}
		}

		if (idles.size() > 0)
		{
			elected = idles.get(Rnd.get(idles.size()));
		}
		else
		{
			if (nbFull == workers.size() || identics)
			{
				elected = workers.get(Rnd.get(workers.size()));
			}
		}

		return elected;
	}

	public void wakeup ()
	{
		synchronized (gate)
		{
			gate.notify();
		}
	}

	@Override
	public void manage (Connection conn) throws RuntimeException
	{
		if (!conn.channel().isOpen())
		{
			throw new RuntimeException ("Trying to manage a connection while channel is already closed");
		}

		synchronized (gate)
		{
			selector.wakeup();

			if (conn.channel().isOpen())
			{
				SelectionKey key;
				try
				{
					key = conn.channel().register(selector, SelectionKey.OP_READ, conn);
				}
				catch(ClosedChannelException e)
				{
					throw new RuntimeException("Trying to manage a connection while channel is already closed", e);
				}

				keys.add(key);
			}
		}
	}

	@Override
	public void close ()
	{
		super.close();

		synchronized (gate)
		{
			selector.wakeup();

			if (workersEnabled)
			{
				for (Worker w : workers)
				{
					w.end();
					try { w.join(); } catch (InterruptedException e) { }
				}
			}

			try {
				for (SelectionKey key : keys)
				{	
					if (key.isValid())
					{
						Connection c = (Connection)key.attachment();

						key.attach(null);
						key.cancel();

						if (c.channel().keyFor(retrySelector) != null)
						{
							c.channel().keyFor(retrySelector).cancel();
						}

						c.close(true);
					}
				}
			} catch (CancelledKeyException e) { }

			keys.clear();
		}
	}

	@Override
	public int getNumberOfConnections ()
	{
		return keys.size();
	}

	@Override
	public void run ()
	{
		int selection = 0;
		Iterator<SelectionKey> ski;
		SelectionKey sk;
		Set<SelectionKey> sks;
		Connection c;

		imRunning();

		while (running())
		{
			try
			{
				imIdle();
				selection = selector.select();
				imBusy();

				if (selection > 0)
				{
					sks = selector.selectedKeys();

					for (ski = sks.iterator(); ski.hasNext(); )
					{
						sk = ski.next();
						ski.remove();

						c = (Connection) sk.attachment();

						if (c == null)
							continue;

						if (!sk.isValid())
						{
							if (keys.contains(sk))
							{
								keys.remove(sk);
							}

							c.close(false);

							continue;
						}

						if (sk.isValid() && sk.isReadable())
						{
							read(c);
						}
						else
						{
							c.close(false);
						}
					}
				}

				synchronized (gate) { }
			}
			catch(Exception e)
			{
				if (debugEnabled)
					log.error(e.getClass().getSimpleName()+" while processing connection", e);
			}
		}

		try {
			selector.close();
			retrySelector.close();
		} catch (IOException e) {
			log.error("Exception while closing selector", e);
		}
		if (debugEnabled)
			log.debug(getName()+" stopped");
	}

	private void read (Connection c) throws IOException
	{
		switch (c.getMode())
		{
			case BINARY:
				readBinary(c);
				break;
			case TEXT:
				readText(c);
				break;
		}
	}
	
	private void readText (Connection c) throws IOException
	{
		sc = c.channel();
		buffer.clear();
		
		if (sc == null || c.isWriteDisabled() || !sc.isOpen())
		{
			c.close (false);
		}
		
		try {
			synchronized (sc)
			{
				read = sc.read(buffer);
			}
		}
		catch (IOException e)
		{
			if (debugEnabled)
				log.debug("IOException reading from connection "+c, e);
			c.close (false);
		}
		
		if (read > 0)
		{
			buffer.flip();

			if (debugEnabled)
				log.debug("Connection "+c+" about to process data from buffer "+buffer);
			
			if (workersEnabled)
			{
				try {
					choose().add(c, buffer);
				} catch (RuntimeException e) {
					if (debugEnabled)
						log.debug("Cannot queue packet "+buffer+" for connection "+c, e);
					c.close(false);
				}
			}
			else
			{
				try {
					if (!c.processData(buffer)) {
						c.close(false);
						return;
					}
				} catch (Exception e) {
					log.error(e.getClass().getSimpleName()+" while processing buffer "+buffer+" for connection "+c, e);
				}
			}
		} else if (read < 0) {
			c.close(false);
		}
	}
	
	private void readBinary (Connection c) throws IOException
	{
		sc = c.channel();
		buffer.clear();
		oldLimit = 0;
		bufferSize = 0;
		read = 0;
		totalRead = 0;

		if (sc == null || c.isWriteDisabled() || !sc.isOpen())
		{
			c.close (false);
		}

		try {
			synchronized (sc)
			{
				totalRead = read = sc.read(buffer);
			}
		}
		catch (IOException e)
		{
			if (debugEnabled)
				log.debug("IOException reading from connection "+c, e);
			c.close (false);
		}

		if (read > 0)
		{
			bufferSize = buffer.getShort(0);
			tries = 0;
			
			if (debugEnabled)
				log.debug ("Connection "+c+" about to process "+buffer+", packet size: "+bufferSize);

			while (buffer.position() < bufferSize && tries < maxTries)
			{
				tries ++;
				
				before = System.currentTimeMillis();
				
				synchronized (sc)
				{
					totalRead += read = sc.read(buffer);
				}

				if (read == 0)
				{
					if (sc.keyFor(retrySelector) == null) {
						sc.register(retrySelector, SelectionKey.OP_READ);
					} else {
						sc.keyFor(retrySelector).interestOps(SelectionKey.OP_READ);
					}
					retrySelection = retrySelector.select();
					if (retrySelection > 0)
					{
						retrySelector.selectedKeys().clear();
					}
				}
			}
			
			if (sc.keyFor(retrySelector) != null)
			{
				sc.keyFor(retrySelector).interestOps(0);
			}

			if (tries == maxTries)
			{
				if (debugEnabled)
					log.error("Too much read tries ("+maxTries+") without any bytes read (read: "+totalRead+", remaining: "+buffer.remaining()+") for connection "+c+", kicking client");
				c.close(false);
				return;
			} else if (tries > 0 && debugEnabled) {
				timeLostInRetries += System.currentTimeMillis() - before;
				log.debug("Read successfully "+totalRead+" bytes after "+tries+" tries (total time lost: "+timeLostInRetries+" ms)");
			}

			buffer.flip();

			while(buffer.remaining() > 2 && buffer.remaining() >= buffer.getShort(buffer.position()))
			{
				try {
					bufferSize = buffer.getShort();

					if(bufferSize > 1) {
						bufferSize -= 2;
					}

					oldLimit = buffer.limit() - 2;
					buffer.compact();
					buffer.limit(bufferSize);
					buffer.position(0);
				}  catch (IllegalArgumentException e) {
					if (debugEnabled)
						log.debug("Illegal argument while parsing buffer "+buffer+" (read: "+totalRead+", awaited: "+bufferSize+") for connection "+c, e);
					c.close(false);
					return;
				}

				if (debugEnabled)
					log.debug("Connection "+c+" about to process data from buffer "+buffer);
				
				if (workersEnabled)
				{
					try {
						choose().add(c, buffer);
					} catch (RuntimeException e) {
						if (debugEnabled)
							log.debug("Cannot queue packet "+buffer+" for connection "+c, e);
						c.close(false);
					}
				}
				else
				{
					try {
						if (c.processData(buffer)) {
							if (buffer.position() != bufferSize)
							{
								if (debugEnabled)
									log.debug("After processing, buffer position is not as expected: expected "+(bufferSize)+", buffer: "+buffer+", fixing...");
								buffer.position(bufferSize);
							}
						} else {
							c.close(false);
							return;
						}
					} catch (Exception e) {
						if (debugEnabled)
							log.error(e.getClass().getSimpleName()+" while processing buffer "+buffer+" for connection "+c, e);
						c.close(false);
					}
				}

				if (oldLimit > buffer.position()) {
					buffer.limit(oldLimit);
					if (debugEnabled)
						log.debug("Connection "+c+": buffer "+buffer+" has more packets (old limit: "+oldLimit+", last packet size: "+bufferSize+", next packet size: "+buffer.getShort(buffer.position())+") ...");
					buffer.compact();
					buffer.position(0);
					buffer.limit(oldLimit - bufferSize);
					if (debugEnabled)
						log.debug("Connection "+c+" about to process next packet from buffer "+buffer+", next packet size: "+buffer.getShort(0));
				} else {
					if (debugEnabled)
						log.debug("Connection "+c+" buffer "+buffer+" seems entirely read, old limit: "+oldLimit+", read: "+totalRead);
					break;
				}
			}

			if (buffer.hasRemaining())
			{
				if (debugEnabled)
					log.error("Buffer "+buffer+" still has data (awaited: "+bufferSize+", read: "+totalRead+"), discarding and closing connection "+c+"...");
				c.close(false);
				return;
			}
		}
		else if (read < 0)
		{
			c.close (false);
		}
	}
}
