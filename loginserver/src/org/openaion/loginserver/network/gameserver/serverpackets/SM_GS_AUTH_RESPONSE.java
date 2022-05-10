package org.openaion.loginserver.network.gameserver.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.gameserver.GsAuthResponse;
import org.openaion.loginserver.network.gameserver.GsConnection;
import org.openaion.loginserver.network.gameserver.GsServerPacket;


/**
 * This packet is response for CM_GS_AUTH its notify Gameserver if registration was ok or what was wrong.
 * 
 * @author -Nemesiss-
 */
public class SM_GS_AUTH_RESPONSE extends GsServerPacket
{
	/**
	 * Response for Gameserver authentication
	 */
	private final GsAuthResponse	response;

	/**
	 * Constructor.
	 * 
	 * @param response
	 */
	public SM_GS_AUTH_RESPONSE(GsAuthResponse response)
	{
		super(0x00);

		this.response = response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(GsConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeC(buf, response.getResponseId());
	}
}
