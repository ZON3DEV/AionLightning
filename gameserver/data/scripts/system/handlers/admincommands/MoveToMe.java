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

package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.alliance.PlayerAllianceMember;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.legion.Legion;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.Util;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;
import org.openaion.gameserver.world.World;

/**
 * Admin movetome command.
 *
 * @author Cyrakuse
 */

public class MoveToMe extends AdminCommand
{

	/**
	 * Constructor.
	 */

	public MoveToMe()
	{
		super("movetome");
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVETOME)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //movetome <player name> [group|alliance|legion]");
			return;
		}

		Player playerToMove = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (playerToMove == null)
		{
			PacketSendUtility.sendMessage(admin, "The specified player is not online.");
			return;
		}

		if (playerToMove == admin)
		{
			PacketSendUtility.sendMessage(admin, "Cannot use this command on yourself.");
			return;
		}
		
		if(params.length == 2)
		{
			if(params[1].equals("alliance"))
			{
				if(playerToMove.getPlayerAlliance() != null)
				{
					for(PlayerAllianceMember m : playerToMove.getPlayerAlliance().getMembers())
					{
						if(m != null && m.getPlayer() != null)
						{
							port(admin, m.getPlayer());
						}
					}
				}
				else
				{
					PacketSendUtility.sendMessage(admin, "This player is not in alliance.");
					port(admin, playerToMove);
				}
			}
			else if(params[1].equals("group"))
			{
				if(playerToMove.isInGroup() && playerToMove.getPlayerGroup() != null)
				{
					for(Player p : playerToMove.getPlayerGroup().getMembers())
					{
						if(p != null)
							port(admin, p);
					}
				}
				else
				{
					PacketSendUtility.sendMessage(admin, "This player is not in group.");
					port(admin, playerToMove);
				}
			}
			else if(params[1].equals("legion"))
			{
				if(playerToMove.getLegion() != null)
				{
					Legion legion = playerToMove.getLegion();
					for(Integer pid : legion.getLegionMembers())
					{
						Player target = World.getInstance().findPlayer(pid);
						if(target != null)
							port(admin, target);
					}
				}
				else
				{
					PacketSendUtility.sendMessage(admin, "This player is not in a legion.");
					port(admin, playerToMove);
				}
			}
			else
			{
				PacketSendUtility.sendMessage(admin, "syntax //movetome <player name> [group|alliance|legion]");
				return;
			}
		}
		else
			port(admin, playerToMove);

		
	}
	
	private void port(Player admin, Player playerToMove)
	{
		TeleportService.teleportTo(playerToMove, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(), 0);
		PacketSendUtility.sendMessage(admin, "Teleported player " + playerToMove.getName() + " to your location.");
		PacketSendUtility.sendMessage(playerToMove, "You have been teleported by " + admin.getName() + ".");
	}
	
}
