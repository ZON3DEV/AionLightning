/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.dao.PlayerPasskeyDAO;
import org.openaion.gameserver.model.account.PlayerAccountData;
import org.openaion.gameserver.model.account.CharacterPasskey.ConnectType;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE_CHARACTER;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.PlayerService;


/**
 * In this packets aion client is requesting deletion of character.
 * 
 * @author -Nemesiss-, ginho1
 * 
 */
public class CM_DELETE_CHARACTER extends AionClientPacket
{
	/**
	 * PlayOk2 - we dont care...
	 */
	@SuppressWarnings("unused")
	private int	playOk2;
	/**
	 * ObjectId of character that should be deleted.
	 */
	private int	chaOid;
	
	/**
	 * Constructs new instance of <tt>CM_DELETE_CHARACTER </tt> packet
	 * @param opcode
	 */
	public CM_DELETE_CHARACTER(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		playOk2 = readD();
		chaOid = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionConnection client = getConnection();
		PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(chaOid);
		if(playerAccData != null && !playerAccData.isLegionMember())
		{
			// passkey check
			if(GSConfig.PASSKEY_ENABLE && !client.getAccount().getCharacterPasskey().isPass())
			{
				client.getAccount().getCharacterPasskey().setConnectType(ConnectType.DELETE);
				client.getAccount().getCharacterPasskey().setObjectId(chaOid);
				boolean isExistPasskey = DAOManager.getDAO(PlayerPasskeyDAO.class).existCheckPlayerPasskey(client.getAccount().getId());

				if (!isExistPasskey)
					client.sendPacket(new SM_CHARACTER_SELECT(0));
				else
					client.sendPacket(new SM_CHARACTER_SELECT(1));
			}
			else
			{
				PlayerService.deletePlayer(playerAccData);
				client.sendPacket(new SM_DELETE_CHARACTER(chaOid, playerAccData.getDeletionTimeInSeconds()));
			}
		}
		else
		{
			client.sendPacket(SM_SYSTEM_MESSAGE.STR_DELETE_CHARACTER_IN_LEGION());
		}		
	}
}
