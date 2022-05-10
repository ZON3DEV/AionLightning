package org.openaion.loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.loginserver.dao.AccountDAO;
import org.openaion.loginserver.model.Account;
import org.openaion.loginserver.network.gameserver.GsClientPacket;
import org.openaion.loginserver.network.gameserver.GsConnection;
import org.openaion.loginserver.network.gameserver.serverpackets.SM_LS_CONTROL_RESPONSE;


/**
 * 
 * @author Aionchs-Wylovech
 * 
 */
public class CM_LS_CONTROL extends GsClientPacket
{
	private String		accountName;
	
	private int		param;

	private int		type;

	private String		playerName;

	private String		adminName;

	private boolean		result;

	public CM_LS_CONTROL(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x05);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{

		type = readC();
		adminName = readS();
		accountName = readS();
		playerName = readS();
		param = readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{

		Account	account = DAOManager.getDAO(AccountDAO.class).getAccount(accountName);
		switch (type)
		{
			case 1:
				account.setAccessLevel((byte)param);
				break;
			case 2:
				account.setMembership((byte)param);
				break;
		}
		result = DAOManager.getDAO(AccountDAO.class).updateAccount(account);
		sendPacket(new SM_LS_CONTROL_RESPONSE(type, result, playerName, account.getId(), param, adminName));	
	}
}        
