package org.openaion.loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.controller.AccountController;
import org.openaion.loginserver.network.aion.SessionKey;
import org.openaion.loginserver.network.gameserver.GsClientPacket;
import org.openaion.loginserver.network.gameserver.GsConnection;


/**
 * In this packet Gameserver is asking if given account sessionKey is valid at Loginserver side. [if user that is
 * authenticating on Gameserver is already authenticated on Loginserver]
 * 
 * @author -Nemesiss-
 * 
 */
public class CM_ACCOUNT_AUTH extends GsClientPacket
{
	/**
	 * SessionKey that GameServer needs to check if is valid at Loginserver side.
	 */
	private SessionKey	sessionKey;

	/**
	 * Constructor.
	 * 
	 * @param buf
	 * @param client
	 */
	public CM_ACCOUNT_AUTH(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x01);

	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		int accountId = readD();
		int loginOk = readD();
		int playOk1 = readD();
		int playOk2 = readD();

		sessionKey = new SessionKey(accountId, loginOk, playOk1, playOk2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AccountController.checkAuth(sessionKey, getConnection());
	}
}
