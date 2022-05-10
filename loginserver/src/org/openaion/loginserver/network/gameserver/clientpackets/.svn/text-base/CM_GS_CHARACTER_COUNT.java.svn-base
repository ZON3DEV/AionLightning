package org.openaion.loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.GameServerInfo;
import org.openaion.loginserver.controller.AccountController;
import org.openaion.loginserver.network.gameserver.GsClientPacket;
import org.openaion.loginserver.network.gameserver.GsConnection;


/**
 * @author blakawk
 *
 * Packet sent to game server to request account characters count
 * When all characters count have been received, send server list to client 
 * 
 */
public class CM_GS_CHARACTER_COUNT extends GsClientPacket
{
	private int accountId;
	private int characterCount;
	
	/**
	 * @param buf
	 * @param client
	 */
	public CM_GS_CHARACTER_COUNT(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x07);
	}

	@Override
	protected void readImpl()
	{
		accountId = readD();
		characterCount = readC();
	}

	@Override
	protected void runImpl()
	{
		GameServerInfo gsi = getConnection().getGameServerInfo();
		
		AccountController.addCharacterCountFor(accountId, gsi.getId(), characterCount);
		
		if(AccountController.hasAllCharacterCounts(accountId))
		{
			AccountController.sendServerListFor(accountId);
		}
	}
}
