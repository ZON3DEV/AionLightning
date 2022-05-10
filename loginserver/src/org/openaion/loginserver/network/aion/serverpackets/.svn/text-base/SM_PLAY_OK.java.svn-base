package org.openaion.loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.aion.AionConnection;
import org.openaion.loginserver.network.aion.AionServerPacket;
import org.openaion.loginserver.network.aion.SessionKey;


/**
 * @author -Nemesiss-
 */
public class SM_PLAY_OK extends AionServerPacket
{
	/**
	 * playOk1 is part of session key - its used for security purposes [checked at game server side]
	 */
	private final int	playOk1;
	/**
	 * playOk2 is part of session key - its used for security purposes [checked at game server side]
	 */
	private final int	playOk2;

	private final int serverId;
	/**
	 * Constructs new instance of <tt>SM_PLAY_OK </tt> packet.
	 * 
	 * @param key session key
	 */
	public SM_PLAY_OK(SessionKey key, byte serverId)
	{
		super(0x07);

		this.playOk1 = key.playOk1;
		this.playOk2 = key.playOk2;
		this.serverId = serverId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, playOk1);
		writeD(buf, playOk2);
		writeC(buf, serverId);
	}
}
