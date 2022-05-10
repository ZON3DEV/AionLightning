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

import java.util.Map;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.ChatType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.i18n.CustomMessageId;
import org.openaion.gameserver.utils.i18n.LanguageHandler;


/**
 * This chat handler is responsible for handling admin commands, starting with //, and user commands starting with .
 * 
 * @author Luno
 * @author Divinity - updated for GM Audit
 * @author blakawk - updated for user commands
 * 
 */
public class CommandChatHandler implements ChatHandler
{
	private static final Logger			log			= Logger.getLogger(CommandChatHandler.class);

	private Map<String, AdminCommand>	adminCommands = new FastMap<String, AdminCommand>();
	private Map<String, UserCommand>    userCommands  = new FastMap<String, UserCommand>();

	public CommandChatHandler () { }

	void registerAdminCommand(AdminCommand command)
	{
		if(command == null)
			throw new NullPointerException("Command instance cannot be null");

		String commandName = command.getCommandName();

		AdminCommand old = adminCommands.put(commandName, command);
		
		if(old != null)
		{
			log.warn("Overriding handler for command " + commandName + " from " + old.getClass().getName() + " to "
				+ command.getClass().getName());
		}
	}

	void registerUserCommand(UserCommand command)
	{
		if(command == null)
			throw new NullPointerException("Command instance cannot be null");

		String commandName = command.getCommandName();

		UserCommand old = userCommands.put(commandName, command);
		
		if(old != null)
		{
			log.warn("Overriding handler for command " + commandName + " from " + old.getClass().getName() + " to "
				+ command.getClass().getName());
		}
	}

	@Override
	public ChatHandlerResponse handleChatMessage(ChatType chatType, String message, Player sender)
	{
		if(!message.startsWith("//") && !message.startsWith("."))
		{
			if(CustomConfig.CHANNEL_ALL_ENABLED && sender.CHAT_FIX_WORLD_CHANNEL != Player.CHAT_NOT_FIXED)
			{
				CustomChannel.sendMessageOnWorld(sender, message, sender.CHAT_FIX_WORLD_CHANNEL);
				return ChatHandlerResponse.BLOCKED_MESSAGE;
			}
			else
			{
				return new ChatHandlerResponse(false, message);
			}
		}
		else if(message.startsWith("."))
		{
			String[] commandAndParams = message.split(" ", 2);
			String command = commandAndParams[0].substring(1);
			
			UserCommand usrc = userCommands.get(command);
			if (usrc == null)
			{
				PacketSendUtility.sendMessage(sender, LanguageHandler.translate(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST));
				return ChatHandlerResponse.BLOCKED_MESSAGE;
			}
			
			String params = "";
			if (commandAndParams.length > 1)
			{
				params = commandAndParams[1];
			}
			
			usrc.executeCommand(sender, params);
			return ChatHandlerResponse.BLOCKED_MESSAGE;
		}
		else
		{
			String[] commandAndParams = message.split(" ", 2);

			String command = commandAndParams[0].substring(2);
			AdminCommand admc = adminCommands.get(command);

			if (sender.getAccessLevel() == 0)
				log.info("[ADMIN COMMAND] > [Name: " + sender.getName() + "]: The player has tried to use the command without have the rights :");

			if (sender.getTarget() != null && sender.getTarget() instanceof Creature)
			{
				Creature target = (Creature) sender.getTarget();

				log.info("[ADMIN COMMAND] > [Name: " + sender.getName() + "][Target : " + target.getName() + "]: " + message);
			}
			else
				log.info("[ADMIN COMMAND] > [Name: " + sender.getName() + "]: " + message);

			if(admc == null)
			{
				PacketSendUtility.sendMessage(sender, "<There is no such admin command: " + command + ">");
				return ChatHandlerResponse.BLOCKED_MESSAGE;
			}

			String[] params = new String[] {};
			if(commandAndParams.length > 1)
				params = commandAndParams[1].split(" ", admc.getSplitSize());

			admc.executeCommand(sender, params);
			return ChatHandlerResponse.BLOCKED_MESSAGE;
		}
	}

	void clearHandlers()
	{
		this.adminCommands.clear();
		this.userCommands.clear();
	}

	public int getAdminCommandsCount()
	{
		return this.adminCommands.size();
	}
	
	public int getUserCommandsCount()
	{
		return this.userCommands.size();
	}
	
}
