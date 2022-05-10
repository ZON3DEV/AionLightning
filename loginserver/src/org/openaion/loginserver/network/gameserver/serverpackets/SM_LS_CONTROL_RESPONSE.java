package org.openaion.loginserver.network.gameserver.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.network.gameserver.GsConnection;
import org.openaion.loginserver.network.gameserver.GsServerPacket;


/**
 * 
 * @author Aionchs-Wylovech
 * 
 */
public class SM_LS_CONTROL_RESPONSE extends GsServerPacket
{

	private int	type;

	private boolean	result;

	private String	playerName;

	private int	param;

	private String	adminName;

	private int	accountId;


	public SM_LS_CONTROL_RESPONSE(int type, boolean result, String playerName, int accountId, int param, String adminName)
	{
		super(0x04);

		this.type = type;
		this.result = result;
		this.playerName = playerName;
		this.param = param;
		this.adminName = adminName;
		this.accountId = accountId;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(GsConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());

		writeC(buf, type);
		writeC(buf, result ? 1 : 0);
		writeS(buf, adminName);
		writeS(buf, playerName);
		writeC(buf, param);
		writeD(buf, accountId);
	}
}        
