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

package org.openaion.gameserver.network.loginserver.clientpackets;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.loginserver.LsClientPacket;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;

/**
 * 
 * @author Watson
 * 
 */
public class CM_BAN_RESPONSE extends LsClientPacket
{
	private byte		type;
	private int			accountId;
	private String		ip;
	private int			time;
	private int			adminObjId;
	private boolean		result;

	public CM_BAN_RESPONSE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		this.type = (byte) readC();
		this.accountId = readD();
		this.ip = readS();
		this.time = readD();
		this.adminObjId = readD();
		this.result = readC() == 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player admin = World.getInstance().findPlayer(adminObjId);
		
		if (admin == null)
			return;

		if (admin.getAccessLevel() < AdminConfig.COMMAND_BAN)
			return;
		
		// Some messages stuff
		String message;
		if (type == 1 || type == 3)
		{
			if (result)
			{
				if (time < 0)
					message = "Account ID " + accountId + " was successfully unbanned";
				else if (time == 0)
					message = "Account ID " + accountId + " was successfully banned";
				else
					message = "Account ID " + accountId + " was successfully banned for " + time + " minutes";
			}
			else
				message = "Error occurred while banning player's account";
			PacketSendUtility.sendMessage(admin, message);
		}
		if (type == 2 || type == 3)
		{
			if (result)
			{
				if (time < 0)
					message = "IP mask " + ip + " was successfully removed from block list";
				else if (time == 0)
					message = "IP mask " + ip + " was successfully added to block list";
				else
					message = "IP mask " + ip + " was successfully added to block list for " + time + " minutes";
			}
			else
				message = "Error occurred while adding IP mask " + ip;
			PacketSendUtility.sendMessage(admin, message);
		}
	}
}
