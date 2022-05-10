package org.openaion.loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.controller.AccountTimeController;
import org.openaion.loginserver.model.Account;
import org.openaion.loginserver.network.gameserver.GsClientPacket;
import org.openaion.loginserver.network.gameserver.GsConnection;


/**
 * In this packet GameServer is informing LoginServer that some account is no longer on GameServer [ie was disconencted]
 *
 * @author -Nemesiss-
 *
 */
public class CM_ACCOUNT_DISCONNECTED extends GsClientPacket
{
	/**
	 * AccountId of account that was disconnected form GameServer.
	 */
	private int	accountId;

	/**
	 * Constructor.
	 *
	 * @param buf
	 * @param client
	 */
	public CM_ACCOUNT_DISCONNECTED(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x03);
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		accountId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Account	account = getConnection().getGameServerInfo().removeAccountFromGameServer(accountId);

        /**
         * account can be null if a player logged out from gs
         * {@link CM_ACCOUNT_RECONNECT_KEY 
         */
		if(account != null)
		{
			AccountTimeController.updateOnLogout(account);
		}
	}
}
 