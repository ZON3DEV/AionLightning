package org.openaion.loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.aion.AionConnection;
import org.openaion.loginserver.network.aion.AionServerPacket;


/**
 * @author -Nemesiss-
 */
public class SM_AUTH_GG extends AionServerPacket
{
	/**
	 * Session Id of this connection
	 */
	private final int	sessionId;

	/**
	 * Constructs new instance of <tt>SM_AUTH_GG</tt> packet
	 * 
	 * @param sessionId
	 */
	public SM_AUTH_GG(int sessionId)
	{
		super(0x0b);

		this.sessionId = sessionId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, sessionId);
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeD(buf, 0x00);
	}
}
