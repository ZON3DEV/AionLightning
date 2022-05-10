package org.openaion.loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.aion.AionAuthResponse;
import org.openaion.loginserver.network.aion.AionConnection;
import org.openaion.loginserver.network.aion.AionServerPacket;


/**
 * @author -Nemesiss-
 * 
 */
public class SM_PLAY_FAIL extends AionServerPacket
{
	/**
	 * response - why play fail
	 */
	private AionAuthResponse	response;

	/**
	 * Constructs new instance of <tt>SM_PLAY_FAIL</tt> packet.
	 * 
	 * @param response auth response
	 */
	public SM_PLAY_FAIL(AionAuthResponse response)
	{
		super(0x06);

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
