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

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.loginserver.LoginServer;
import org.openaion.gameserver.network.loginserver.LsClientPacket;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.Util;
import org.openaion.gameserver.utils.rates.Rates;
import org.openaion.gameserver.world.World;

/**
 * 
 * @author Aionchs-Wylovech
 * 
 */
public class CM_LS_CONTROL_RESPONSE extends LsClientPacket
{
	private int		type;
	private boolean		result;
	private String		playerName;
	private byte		param;
	private String		adminName;
	private int		accountId;

	public CM_LS_CONTROL_RESPONSE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		type = readC();
		result = readC() == 1;
		adminName = readS();
		playerName = readS();
		param = (byte) readC();
		accountId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		World world = World.getInstance();
		Player admin = world.findPlayer(Util.convertName(adminName));
		Player player = world.findPlayer(Util.convertName(playerName));
		LoginServer.getInstance().accountUpdate(accountId, param, type);
		switch (type)
		{
			case 1:
				if(!result)
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, playerName + " access level has been updated to " + param );
				   if(player != null)
				   {
				      PacketSendUtility.sendMessage(player, "Your access level have been updated to " + param + " by " + adminName);
				   }
				}
				else
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, " Abnormal, the operation failed! ");
				}
				break;
			case 2:
				if(!result)
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, playerName + " membership has been updated to " + param );
				   if(player != null)
				   {
				      player.setRates(Rates.getRatesFor(param));
				      PacketSendUtility.sendMessage(player, "Your membership have been updated to " + param + " by " + adminName);
				   }
				}
				else
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, " Abnormal, the operation failed! ");
				}
				break;
		}
	}
}
