package org.openaion.loginserver.network.gameserver.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.gameserver.GsConnection;
import org.openaion.loginserver.network.gameserver.GsServerPacket;


/**
 * Packet sent to gameserver to request characters count for account
 * @author blakawk
 */
public class SM_GS_REQUEST_CHARACTER_COUNT extends GsServerPacket
{
	private int	accountId;

	public SM_GS_REQUEST_CHARACTER_COUNT(int accountId)
	{
		super(0x06);
		
		this.accountId = accountId;
	}

	@Override
	protected void writeImpl(GsConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
	}

}
