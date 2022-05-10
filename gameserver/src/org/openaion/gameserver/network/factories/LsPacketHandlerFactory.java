/*
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
package org.openaion.gameserver.network.factories;


import org.openaion.gameserver.network.loginserver.LsClientPacket;
import org.openaion.gameserver.network.loginserver.LsPacketHandler;
import org.openaion.gameserver.network.loginserver.LoginServerConnection.State;
import org.openaion.gameserver.network.loginserver.clientpackets.CM_ACCOUNT_RECONNECT_KEY;
import org.openaion.gameserver.network.loginserver.clientpackets.CM_ACOUNT_AUTH_RESPONSE;
import org.openaion.gameserver.network.loginserver.clientpackets.CM_BAN_RESPONSE;
import org.openaion.gameserver.network.loginserver.clientpackets.CM_GS_AUTH_RESPONSE;
import org.openaion.gameserver.network.loginserver.clientpackets.CM_LS_CONTROL_RESPONSE;
import org.openaion.gameserver.network.loginserver.clientpackets.CM_LS_REQUEST_CHARACTER_COUNT;
import org.openaion.gameserver.network.loginserver.clientpackets.CM_REQUEST_KICK_ACCOUNT;

/**
 * @author Luno
 * 
 */
public class LsPacketHandlerFactory
{
	private LsPacketHandler	handler	= new LsPacketHandler();

	public static final LsPacketHandlerFactory getInstance()
	{
		return SingletonHolder.instance;
	}

	/**
	 * @param loginServer
	 */
	private LsPacketHandlerFactory()
	{

		addPacket(new CM_ACCOUNT_RECONNECT_KEY(0x03), State.AUTHED);
		addPacket(new CM_ACOUNT_AUTH_RESPONSE(0x01), State.AUTHED);
		addPacket(new CM_GS_AUTH_RESPONSE(0x00), State.CONNECTED);
		addPacket(new CM_REQUEST_KICK_ACCOUNT(0x02), State.AUTHED);
		addPacket(new CM_LS_CONTROL_RESPONSE(0x04), State.AUTHED);
		addPacket(new CM_BAN_RESPONSE(0x05), State.AUTHED);
		addPacket(new CM_LS_REQUEST_CHARACTER_COUNT(0x06), State.AUTHED);

	}

	private void addPacket(LsClientPacket prototype, State... states)
	{
		handler.addPacketPrototype(prototype, states);
	}

	public LsPacketHandler getPacketHandler()
	{
		return handler;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final LsPacketHandlerFactory instance = new LsPacketHandlerFactory();
	}
}
