package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * 
 * @author kamui
 * 
 */
public class SM_STANCE_STATE extends AionServerPacket
{
	private int		playerObjectId;
	private int		stateId;

	public SM_STANCE_STATE(int playerObjectId, int stateId)
	{
		this.playerObjectId = playerObjectId;
		this.stateId = stateId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playerObjectId);
		writeC(buf, stateId);
	}
}
