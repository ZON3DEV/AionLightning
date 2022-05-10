package org.openaion.loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.aion.AionAuthResponse;
import org.openaion.loginserver.network.aion.AionConnection;
import org.openaion.loginserver.network.aion.AionServerPacket;


/**
 * @author KID
 */
public class SM_LOGIN_FAIL extends AionServerPacket
{
	/**
	 * response - why login fail
	 */
	private AionAuthResponse	response;

	/**
	 * Constructs new instance of <tt>SM_LOGIN_FAIL</tt> packet.
	 * 
	 * @param response auth responce
	 */
	public SM_LOGIN_FAIL(AionAuthResponse response)
	{
		super(0x01);

		this.response = response;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, response.getMessageId());
	}
}