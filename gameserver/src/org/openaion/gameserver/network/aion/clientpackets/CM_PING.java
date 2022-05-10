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
package org.openaion.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.serverpackets.SM_PONG;


/**
 * I have no idea wtf is this
 * 
 * @author -Nemsiss-
 * 
 */
public class CM_PING extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_PING.class);
	private static boolean firstPing = true;

	/**
	 * Constructs new instance of <tt>CM_PING </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_PING(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		// empty
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		long lastMS = getConnection().getLastPingTimeMS();
		
		if(lastMS > 0)
		{
			long pingInterval = System.currentTimeMillis() - lastMS;
			// PingInterval should be 3min (180000ms)
			if(pingInterval < CustomConfig.KICK_PINGINTERVAL)// client timer cheat
			{
				String ip = getConnection().getIP();
				//String ip = getConnection().getSource();
				String name = "[unknown]";
				if(getConnection().getActivePlayer() != null)
					name = getConnection().getActivePlayer().getName();

				if(CustomConfig.KICK_SPEEDHACK)
				{
					if(!firstPing)
					{
					    log.info("[AUDIT] possible client timer cheat kicking player: " + pingInterval + " by " + name + ", ip=" + ip);
					    AionConnection connection = getConnection().getActivePlayer().getClientConnection();
					    if (connection != null)
					    	connection.close(true);
					    return;
					}
					firstPing = false;
				}else{				
					log.info("[AUDIT] possible client timer cheat: " + pingInterval + " by " + name + ", ip=" + ip);
				}
				
			}

		}
		getConnection().setLastPingTimeMS(System.currentTimeMillis());
		sendPacket(new SM_PONG());
	}
}