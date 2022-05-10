package org.openaion.loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;

import org.openaion.loginserver.GameServerTable;
import org.openaion.loginserver.controller.AccountController;
import org.openaion.loginserver.model.Account;
import org.openaion.loginserver.network.gameserver.GsClientPacket;
import org.openaion.loginserver.network.gameserver.GsConnection;
import org.openaion.loginserver.network.gameserver.serverpackets.SM_REQUEST_KICK_ACCOUNT;


/**
 * Reads the list of accoutn id's that are logged to game server
 *
 * @author SoulKeeper
 */
public class CM_ACCOUNT_LIST extends GsClientPacket
{
	/**
	 * Array with accounts that are logged in
	 */
	private String[] accountNames;

	/**
	 * Creates new packet instance.
	 *
	 * @param buf	packet data
	 * @param client client
	 */
	public CM_ACCOUNT_LIST(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x04);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		accountNames = new String[readD()];
		for(int i = 0; i < accountNames.length; i++)
		{
			accountNames[i] = readS();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		for(String s : accountNames)
		{
			Account a = AccountController.loadAccount(s);
			if(GameServerTable.isAccountOnAnyGameServer(a))
			{
				getConnection().sendPacket(new SM_REQUEST_KICK_ACCOUNT(a.getId()));
				continue;
			}
			getConnection().getGameServerInfo().addAccountToGameServer(a);
		}
	}
}
