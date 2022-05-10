package org.openaion.loginserver.network.gameserver.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.gameserver.GsConnection;
import org.openaion.loginserver.network.gameserver.GsServerPacket;


/**
 * In this packet LoginSerer is requesting kicking account from GameServer.
 * 
 * @author -Nemesiss-
 * 
 */
public class SM_REQUEST_KICK_ACCOUNT extends GsServerPacket
{
	/**
	 * Account that must be kicked at GameServer side.
	 */
	private final int	accountId;

	/**
	 * Constructor.
	 * 
	 * @param accountId
	 */
	public SM_REQUEST_KICK_ACCOUNT(int accountId)
	{
		super(0x02);

		this.accountId = accountId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(GsConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
	}
}
