package org.openaion.loginserver.network.gameserver.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.gameserver.GsConnection;
import org.openaion.loginserver.network.gameserver.GsServerPacket;


/**
 * In this packet LoginServer is sending response for CM_ACCOUNT_RECONNECT_KEY with account name and reconnectionKey.
 * 
 * @author -Nemesiss-
 * 
 */
public class SM_ACCOUNT_RECONNECT_KEY extends GsServerPacket
{
	/**
	 * accountId of account that will be reconnecting.
	 */
	private final int	accountId;
	/**
	 * ReconnectKey that will be used for authentication.
	 */
	private final int	reconnectKey;

	/**
	 * Constructor.
	 * 
	 * @param accountId
	 * @param reconnectKey
	 */
	public SM_ACCOUNT_RECONNECT_KEY(int accountId, int reconnectKey)
	{
		super(0x03);

		this.accountId = accountId;
		this.reconnectKey = reconnectKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(GsConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
		writeD(buf, reconnectKey);
	}
}
