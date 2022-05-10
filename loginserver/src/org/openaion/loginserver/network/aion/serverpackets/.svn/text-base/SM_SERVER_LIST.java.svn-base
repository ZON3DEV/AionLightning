package org.openaion.loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openaion.loginserver.GameServerInfo;
import org.openaion.loginserver.GameServerTable;
import org.openaion.loginserver.controller.AccountController;
import org.openaion.loginserver.network.aion.AionConnection;
import org.openaion.loginserver.network.aion.AionServerPacket;


/**
 * @author -Nemesiss-
 */
public class SM_SERVER_LIST extends AionServerPacket
{
	/**
	 * Logger for this class.
	 */
	protected static Logger	log	= Logger.getLogger(SM_SERVER_LIST.class);

	/**
	 * Constructs new instance of <tt>SM_SERVER_LIST</tt> packet.
	 */
	public SM_SERVER_LIST()
	{
		super(0x04);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		Collection<GameServerInfo> servers = GameServerTable.getGameServers();
		Map<Integer, Integer> charactersCountOnServer = null;
		
		int accountId = con.getAccount().getId();
		int maxId = 0;
		int accessLvl;
		
		charactersCountOnServer = AccountController.getCharacterCountsFor(accountId);
		
		writeC(buf, getOpcode());
		writeC(buf, servers.size());// servers
		writeC(buf, con.getAccount().getLastServer());// last server
		for (GameServerInfo gsi : servers)
		{
			accessLvl = (int)(con.getAccount().getAccessLevel());
			if (gsi.getId()>maxId)
			{
				maxId = gsi.getId();
			}
			writeC(buf, gsi.getId());// server id
			writeB(buf, gsi.getIPAddressForPlayer(con.getIP())); // server IP
			writeD(buf, gsi.getPort());// port
			writeC(buf, 0x00); // age limit
			writeC(buf, 0x01);// pvp=1
			writeH(buf, gsi.getCurrentPlayers());// currentPlayers
			writeH(buf, gsi.canAccess(accessLvl) ? gsi.getMaxPlayers() : 0);// maxPlayers
			writeC(buf, gsi.canAccess(accessLvl) ? (gsi.isOnline() ? 1 : 0) : 0);// ServerStatus, up=1
			writeD(buf, gsi.canAccess(accessLvl) ? 1 : 0);// bits);
			writeC(buf, 1);// server.brackets ? 0x01 : 0x00);
		}
		
		writeH(buf, maxId+1);
		writeC(buf, 0x01); // 0x01 for autoconnect
		
		for (int i = 1; i <= maxId; i++)
		{
			if (charactersCountOnServer.containsKey(i))
			{
				writeC(buf, charactersCountOnServer.get(i));
			}
			else
			{
				writeC(buf, 0);
			}
		}
	}
}
