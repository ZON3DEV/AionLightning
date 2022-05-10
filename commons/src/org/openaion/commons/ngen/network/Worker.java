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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author blakawk
 *
 */
public class Worker extends Thread
{
	private static final Logger log = Logger.getLogger(Worker.class);
	private boolean running = false;
	private List<Task> tasks;
	private List<Task> busyTasks;
	private List<Task> freeTasks;
	private Task freeTask;
	private Task newTask;
	
	private int bufferCount;
	
	private class Task
	{
		private ByteBuffer bb;
		private Connection c;
		
		public Task ()
		{
			this.c = null;
			this.bb = ByteBuffer.allocate(8192 * 2).order(ByteOrder.LITTLE_ENDIAN);
		}
		
		public void queue (Connection c, ByteBuffer bb) throws RuntimeException
		{
			if (bb.remaining() > this.bb.capacity())
				throw new RuntimeException ("Trying to queue a too large buffer");
			this.c = c;
			this.bb.clear();
			this.bb.put(bb);
			if (bb.hasRemaining())
			{
				log.debug("Buffer "+bb+" still has data after queueing...");
			}
			
			this.bb.flip();
		}
		
		public void run ()
		{
			try {
				if (!c.processData(bb))
				{
					c.close(false);
				}
				
				bb.clear();
			} catch (Exception e) {
				log.error(e.getClass().getSimpleName()+" while processing buffer "+bb+" for "+c, e);
			}
			
			c = null;
		}
	}
	
	public Worker (String name, Reader p, int bufferCount)
	{
		super(name);
		this.tasks = new ArrayList<Task>(bufferCount);
		this.busyTasks = new ArrayList<Task>(bufferCount);
		this.freeTasks = new ArrayList<Task>(bufferCount);
		
		for (int i = 0; i < bufferCount; i ++)
		{
			newTask = new Task ();
			this.tasks.add(newTask);
			this.freeTasks.add(newTask);
		}
		
		this.bufferCount = bufferCount;
	}
	
	public void add (Connection c, ByteBuffer bb)
	{
		synchronized (busyTasks)
		{
			do
			{
				if (busyTasks.size() == bufferCount)
				{
					try { busyTasks.wait (); } catch (InterruptedException e) { }
				}
		
				if (freeTasks.size() > 0) {
					freeTask = freeTasks.remove(0);
					freeTask.queue(c, bb);
					busyTasks.add(freeTask);
					break;
				}
			} while ((busyTasks.size() == bufferCount) && running);
			
			if (busyTasks.size() == 1)
				busyTasks.notify();
		}
	}
	
	public boolean isFull ()
	{
		return busyTasks.size() == bufferCount;
	}
	
	public int getQueueSize ()
	{
		return busyTasks.size();
	}
	
	public boolean isIdle ()
	{
		return busyTasks.size() == 0;
	}
	
	public void end ()
	{
		synchronized (busyTasks)
		{
			running = false;
			if (busyTasks.size() == 0)
				busyTasks.notify();
		}
	}
	
	@Override
	public void run ()
	{
		Task task = null;
		
		running = true;
		
		while (running)
		{
			synchronized (busyTasks)
			{
				while (busyTasks.size() == 0 && running)
				{
					try { busyTasks.wait(); } catch (InterruptedException e) { }
				}

				if (!running)
					break;
				
				task = busyTasks.get(0);
			}

			task.run();
			
			synchronized (busyTasks)
			{
				freeTasks.add(busyTasks.remove(0));
				if (busyTasks.size() == bufferCount - 1)
					busyTasks.notify();
			}
		}
		
		synchronized (busyTasks)
		{
			busyTasks.notify();
		}
		
		log.debug(getName()+" stopped");
	}
}
