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

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.legion.Legion;
import org.openaion.gameserver.services.LegionService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;
import org.openaion.gameserver.world.World;

/**
 * @author Sylar
 */
public class Spy extends AdminCommand
{
	public Spy()
	{
		super("spy");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < 2)
		{
			PacketSendUtility.sendMessage(admin, "Not authorized");
			return;
		}

		if(params.length != 2)
		{
			syntax(admin);
			return;
		}

		if(params[1].startsWith("L"))
		{
			try
			{
				int legionId = Integer.parseInt(params[1].replace("L", ""));
				Legion legion = LegionService.getInstance().getLegion(legionId);
				if(legion == null)
					throw new Exception("no such legion.");
				
				if(params[0].equals("start"))
				{
					if(!admin.spyedLegions.contains(legionId))
						admin.spyedLegions.add(legionId);
				}
				else if(params[0].equals("stop"))
				{
					if(admin.spyedLegions.contains(legionId))
						admin.spyedLegions.remove(new Integer(legionId));
				}
				else
					syntax(admin);
			}
			catch(Exception e)
			{
				PacketSendUtility.sendMessage(admin, "no such legion.");
				return;
			}
		}
		else if(params[1].startsWith("G"))
		{
			Player target = World.getInstance().findPlayer(params[1].substring(1));
			if(target == null)
			{
				PacketSendUtility.sendMessage(admin, "target player not found.");
				return;
			}
			if(target.getPlayerGroup() == null)
			{
				PacketSendUtility.sendMessage(admin, "target player not in group.");
				return;
			}
			
			if(params[0].equals("start"))
			{
				if(!admin.spyedGroups.contains(target.getPlayerGroup().getGroupId()))
					admin.spyedGroups.add(target.getPlayerGroup().getGroupId());
			}
			else if(params[0].equals("stop"))
			{
				if(admin.spyedGroups.contains(target.getPlayerGroup().getGroupId()))
					admin.spyedGroups.remove(new Integer(target.getPlayerGroup().getGroupId()));
			}
			else
				syntax(admin);
			
		}

	}

	private void syntax(Player admin)
	{
		PacketSendUtility.sendMessage(admin, "Syntax: //spy <start|stop> <legionName>");
	}
}