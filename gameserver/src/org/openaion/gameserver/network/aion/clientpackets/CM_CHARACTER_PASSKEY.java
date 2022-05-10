package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.dao.PlayerPasskeyDAO;
import org.openaion.gameserver.model.account.CharacterPasskey;
import org.openaion.gameserver.model.account.PlayerAccountData;
import org.openaion.gameserver.model.account.CharacterPasskey.ConnectType;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE_CHARACTER;
import org.openaion.gameserver.network.loginserver.LoginServer;
import org.openaion.gameserver.services.PlayerService;


/**
 * @author ginho1
 */
public class CM_CHARACTER_PASSKEY extends AionClientPacket
{
	private int	type;
	private String passkey;
	private String newPasskey;

	public CM_CHARACTER_PASSKEY(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		type = readH(); // 0:new, 2:update, 3:input
		passkey = readS();
		if (type == 2)
			newPasskey = readS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionConnection client = getConnection();
		CharacterPasskey chaPasskey = client.getAccount().getCharacterPasskey();

		switch (type)
		{
			case 0:
				chaPasskey.setIsPass(false);
				chaPasskey.setWrongCount(0);
				DAOManager.getDAO(PlayerPasskeyDAO.class).insertPlayerPasskey(client.getAccount().getId(), passkey);
				client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));
				break;
			case 2:
				boolean isSuccess = DAOManager.getDAO(PlayerPasskeyDAO.class).updatePlayerPasskey(
					client.getAccount().getId(),
					passkey,
					newPasskey);

				chaPasskey.setIsPass(false);
				if (isSuccess)
				{
					chaPasskey.setWrongCount(0);
					client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));
				}
				else
				{
					chaPasskey.setWrongCount(chaPasskey.getWrongCount() + 1);
					checkBlock(client.getAccount().getId(), chaPasskey.getWrongCount());
					client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));
				}
				break;
			case 3:
				boolean isPass = DAOManager.getDAO(PlayerPasskeyDAO.class).checkPlayerPasskey(
					client.getAccount().getId(),
					passkey);

				if (isPass)
				{
					chaPasskey.setIsPass(true);
					chaPasskey.setWrongCount(0);
					client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));

					if (chaPasskey.getConnectType() == ConnectType.ENTER)
						CM_ENTER_WORLD.enterWorld(client, chaPasskey.getObjectId());
					else if (chaPasskey.getConnectType() == ConnectType.DELETE)
					{
						PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(
							chaPasskey.getObjectId());

						PlayerService.deletePlayer(playerAccData);
						client.sendPacket(new SM_DELETE_CHARACTER(chaPasskey.getObjectId(), playerAccData
							.getDeletionTimeInSeconds()));
					}
				}
				else
				{
					chaPasskey.setIsPass(false);
					chaPasskey.setWrongCount(chaPasskey.getWrongCount() + 1);
					checkBlock(client.getAccount().getId(), chaPasskey.getWrongCount());
					client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));
				}
				break;
		}
	}

	/**
	 * @param accountId
	 * @param wrongCount
	 */
	private void checkBlock(int accountId, int wrongCount)
	{
		if (wrongCount >= GSConfig.PASSKEY_WRONG_MAXCOUNT)
		{
			// TODO : Change the account to be blocked
			LoginServer.getInstance().sendBanPacket((byte) 2, accountId, "", 60 * 8, 0);
		}
	}
}
