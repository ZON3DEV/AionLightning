package org.openaion.loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.aion.AionConnection;
import org.openaion.loginserver.network.aion.AionServerPacket;
import org.openaion.loginserver.network.aion.SessionKey;


/**
 * @author -Nemesiss-
 */
public class SM_LOGIN_OK extends AionServerPacket
{
	/**
	 * accountId is part of session key - its used for security purposes
	 */
	private final int	accountId;
	/**
	 * loginOk is part of session key - its used for security purposes
	 */
	private final int	loginOk;

	/**
	 * Constructs new instance of <tt>SM_LOGIN_OK</tt> packet.
	 * 
	 * @param key session key
	 */
	public SM_LOGIN_OK(SessionKey key)
	{
		super(0x03);
		
		this.accountId = key.accountId;
		this.loginOk = key.loginOk;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
		writeD(buf, loginOk);
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeD(buf, 0x000003ea);
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeB(buf, new byte[16]);
	}
}
