package org.openaion.loginserver.network.gameserver.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.gameserver.GsConnection;
import org.openaion.loginserver.network.gameserver.GsServerPacket;


/**
 * In this packet LoginServer is answering on GameServer ban request
 * 
 * @author Watson
 * 
 */
public class SM_BAN_RESPONSE extends GsServerPacket
{
	private final byte		type;
	private final int		accountId;
	private final String	ip;
	private final int		time;
	private final int		adminObjId;
	private final boolean	result;

	public SM_BAN_RESPONSE(byte type, int accountId, String ip, int time, int adminObjId, boolean result)
	{
		super(0x05);

		this.type = type;
		this.accountId = accountId;
		this.ip = ip;
		this.time = time;
		this.adminObjId = adminObjId;
		this.result = result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(GsConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());

		writeC(buf, type);
		writeD(buf, accountId);
		writeS(buf, ip);
		writeD(buf, time);
		writeD(buf, adminObjId);
		writeC(buf, result ? 1 : 0);
	}
}        
