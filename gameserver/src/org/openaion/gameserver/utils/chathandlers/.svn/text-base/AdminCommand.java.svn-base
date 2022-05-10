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

import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.Util;
import org.openaion.gameserver.world.World;

/**
 * This is a base class representing admin command. Admin command is sent by player by typing a //command parameters in
 * chat
 * 
 * @author Luno
 * 
 */
public abstract class AdminCommand
{
	private final String	commandName;

	protected AdminCommand(String commandName)
	{
		this.commandName = commandName;
	}

	/**
	 * This method is responsible for number of arguments that comman will accept.<br>
	 * <br>
	 * Lets say user types command: <b>//doSomething arg1 arg2 arg3 arg4</b><br>
	 * If this method returns <b>-1</b>, then every arg that is separated by whitespace ( ) will be threatead as command
	 * parameter, example:
	 * <ul>
	 * <li>Command: doSomething</li>
	 * <li>Param: arg1</li>
	 * <li>Param: arg2</li>
	 * <li>Param: arg3</li>
	 * <li>Param: arg4</li>
	 * </ul>
	 * <br>
	 * Let's say this method returns <b>2</b>.<br>
	 * In such case it will be threated as:
	 * <ul>
	 * <li>Command: doSomething</li>
	 * <li>Param: arg1</li>
	 * <li>Param: arg2 arg3 arg4</li>
	 * </ul>
	 * so we will have only two params.<br>
	 *
	 * @return number of params in command
	 */
	public int getSplitSize()
	{
		return -1;
	}

	/**
	 * Returns the name of the command handled by this class.
	 * 
	 * @return command name
	 */
	public String getCommandName()
	{
		return commandName;
	}
	
	public Player parsePlayerParameter (String param, Player admin, String syntax)
	{
		Player player = null;
		World world = World.getInstance();
		int playerObjId = 0;
		boolean byName = false;
		
		if(param!=null)
		{
			try {
				playerObjId = Integer.parseInt(param);
				player = world.findPlayer(playerObjId);
			} catch (NumberFormatException e) {
				player = world.findPlayer(Util.convertName(param));
				byName = true;
			}
		}
		
		if (player == null)
		{
			if (admin.getTarget() == null)
			{
				if (byName)
				{
					PacketSendUtility.sendMessage(admin, "Player "+param+" is not online");
					return null;
				}
				PacketSendUtility.sendMessage(admin, "You must either select the player or indicate player parameter");
				PacketSendUtility.sendMessage(admin, syntax);
				return null;
			}
			else
			{
				VisibleObject adminTarget = admin.getTarget();
				if (adminTarget instanceof Player)
				{
					player = (Player)adminTarget;
				}
				else
				{
					PacketSendUtility.sendMessage(admin, "The selected target is not a player");
					PacketSendUtility.sendMessage(admin, syntax);
					return null;
				}
			}
		}
		return player;
	}

	/**
	 * Execute admin command represented by this class, with a given list of parametrs.
	 * 
	 * @param admin
	 * @param params
	 */
	public abstract void executeCommand(Player admin, String[] params);
}
