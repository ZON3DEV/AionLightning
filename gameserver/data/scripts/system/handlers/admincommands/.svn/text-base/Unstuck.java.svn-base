/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Nemiroff
 *         Date: 11.01.2010
 */
public class Unstuck extends AdminCommand
{

	public Unstuck()
	{
		super("unstuck");
	}

	/**
	* Execute admin command represented by this class, with a given list of parametrs.
	*
	* @param admin the player of the admin that requests the command
	* @param params the parameters of the command
	*/

	@Override
	public void executeCommand(Player admin, String[] params) 
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_UNSTUCK)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		if (admin.getLifeStats().isAlreadyDead())
		{
			PacketSendUtility.sendMessage(admin, "You cant execute this command while you are dead");
			return;
		}
		TeleportService.moveToBindLocation(admin, true, CustomConfig.UNSTUCK_DELAY);
	}
}
