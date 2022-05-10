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
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
* @author Sylar
*/
public class Gps extends AdminCommand
{
	public Gps()
	{
		super("gps");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		PacketSendUtility.sendMessage(admin, "== GPS Coordinates ==");
		PacketSendUtility.sendMessage(admin, "X = " + admin.getX());
		PacketSendUtility.sendMessage(admin, "Y = " + admin.getY());
		PacketSendUtility.sendMessage(admin, "Z = " + admin.getZ());
		PacketSendUtility.sendMessage(admin, "H = " + admin.getHeading());
		PacketSendUtility.sendMessage(admin, "World = " + admin.getWorldId());
	}
}