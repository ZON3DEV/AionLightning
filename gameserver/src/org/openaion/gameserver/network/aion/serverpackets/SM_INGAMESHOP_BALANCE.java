package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


public class SM_INGAMESHOP_BALANCE extends AionServerPacket
{	
	public SM_INGAMESHOP_BALANCE()
	{
		
	}
	/**
	 * Update the player current currency
	 */
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeQ(buf, con.getActivePlayer().shopMoney);
	}
}
