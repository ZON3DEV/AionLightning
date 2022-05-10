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

package org.openaion.gameserver.utils.chathandlers;

import java.io.File;

import org.openaion.commons.scripting.scriptmanager.ScriptManager;
import org.openaion.gameserver.GameServerError;


import javolution.util.FastList;

/**
 * This class is managing a list of all chat handlers.
 * 
 * @see ChatHandler
 * @author Luno
 * 
 */
public class ChatHandlers
{
	private FastList<ChatHandler>	handlers;

	public static final File CHAT_DESCRIPTOR_FILE = new File("./data/scripts/system/handlers.xml");
	
	private ScriptManager sm;

	public static final ChatHandlers getInstance()
	{
		return SingletonHolder.instance;
	}

	private ChatHandlers()
	{
		handlers	= new FastList<ChatHandler>();
		sm = new ScriptManager();
		createChatHandlers();
	}

	void addChatHandler(ChatHandler ch)
	{
		handlers.add(ch);
	}
	/**
	 * @return the handlers
	 */
	public FastList<ChatHandler> getHandlers()
	{
		return handlers;
	}

	/**
	 * Creates and return object of {@link ChatHandlers} class
	 * 
	 * @return ChatHandlers
	 */
	private void createChatHandlers()
	{
		final CommandChatHandler adminCCH = new CommandChatHandler();
		addChatHandler(adminCCH);

		// set global loader
		sm.setGlobalClassListener(new ChatHandlersLoader(adminCCH));

		try
		{
			sm.load(CHAT_DESCRIPTOR_FILE);
		}
		catch (Exception e)
		{
			throw new GameServerError("Can't initialize chat handlers.", e);
		}
	}

	public void reloadChatHandlers()
	{
		ScriptManager tmpSM;
		final CommandChatHandler adminCCH;
		try
		{
			tmpSM = new ScriptManager();
			adminCCH = new CommandChatHandler();
			tmpSM.setGlobalClassListener(new ChatHandlersLoader(adminCCH));
			
			tmpSM.load(CHAT_DESCRIPTOR_FILE);
		}
		catch(Exception e)
		{
			throw new GameServerError("Can't reload chat handlers.", e);
		}
		
		if(tmpSM != null && adminCCH != null)
		{
			sm.shutdown();
			sm = null;
			handlers.clear();
			sm = tmpSM;
			addChatHandler(adminCCH);
		}
		
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ChatHandlers instance = new ChatHandlers();
	}
}
