/*
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

import java.sql.Timestamp;
import java.util.List;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.InventoryDAO;
import org.openaion.gameserver.model.Gender;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.account.Account;
import org.openaion.gameserver.model.account.PlayerAccountData;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerAppearance;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.serverpackets.SM_CREATE_CHARACTER;
import org.openaion.gameserver.services.PlayerService;
import org.openaion.gameserver.utils.Util;
import org.openaion.gameserver.utils.idfactory.IDFactory;


/**
 * In this packets aion client is requesting creation of character.
 * 
 * @author -Nemesiss-
 * 
 */
public class CM_CREATE_CHARACTER extends AionClientPacket
{
	/** Character appearance */
	private PlayerAppearance	playerAppearance;
	/** Player base data */
	private PlayerCommonData	playerCommonData;

	/**
	 * Constructs new instance of <tt>CM_CREATE_CHARACTER </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_CREATE_CHARACTER(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		@SuppressWarnings("unused")
		int playOk2 = readD(); // ignored for now
		@SuppressWarnings("unused")
		String someShit = readS(); // something + accointId

		playerCommonData = new PlayerCommonData(IDFactory.getInstance().nextId());
		String name = Util.convertName(readS());

		playerCommonData.setName(name);
		readB(50 - (name.length() * 2)); // some shit 2.5.x
		
		playerCommonData.setLevel(1);
		playerCommonData.setGender(readD() == 0 ? Gender.MALE : Gender.FEMALE);
		playerCommonData.setRace(readD() == 0 ? Race.ELYOS : Race.ASMODIANS);
		playerCommonData.setPlayerClass(PlayerClass.getPlayerClassById((byte) readD()));

		playerAppearance = new PlayerAppearance();

		playerAppearance.setVoice(readD());

		playerAppearance.setSkinRGB(readD());
		playerAppearance.setHairRGB(readD());
		playerAppearance.setEyeRGB(readD());
		playerAppearance.setLipRGB(readD());

		playerAppearance.setFace(readC());
		playerAppearance.setHair(readC());
		playerAppearance.setDecoration(readC());
		playerAppearance.setTattoo(readC());
		playerAppearance.setFaceContour(readC());
		playerAppearance.setExpression(readC());
		      
		readC(); // Always 6 - 2.5.x
		      
		playerAppearance.setJawLine(readC());
		playerAppearance.setForehead(readC());
		playerAppearance.setEyeHeight(readC());
		playerAppearance.setEyeSpace(readC());
		playerAppearance.setEyeWidth(readC());
		playerAppearance.setEyeSize(readC());
		playerAppearance.setEyeShape(readC());
		playerAppearance.setEyeAngle(readC());

		playerAppearance.setBrowHeight(readC());
		playerAppearance.setBrowAngle(readC());
		playerAppearance.setBrowShape(readC());

		playerAppearance.setNose(readC());
		playerAppearance.setNoseBridge(readC());
		playerAppearance.setNoseWidth(readC());
		playerAppearance.setNoseTip(readC());

		playerAppearance.setCheeks(readC());
		playerAppearance.setLipHeight(readC());
		playerAppearance.setMouthSize(readC());
		playerAppearance.setLipSize(readC());
		playerAppearance.setSmile(readC());
		playerAppearance.setLipShape(readC());

		playerAppearance.setChinHeight(readC());
		playerAppearance.setCheekBones(readC());

		playerAppearance.setEarShape(readC());
		playerAppearance.setHeadSize(readC());

		playerAppearance.setNeck(readC());
		playerAppearance.setNeckLength(readC());

		playerAppearance.setShoulderSize(readC());

		playerAppearance.setTorso(readC());
		playerAppearance.setChest(readC()); // only woman
		playerAppearance.setWaist(readC());
		playerAppearance.setHips(readC());

		playerAppearance.setArmThickness(readC());
		playerAppearance.setHandSize(readC());

		playerAppearance.setLegThickness(readC());
		playerAppearance.setFootSize(readC());
		
		readC(); // always 0

		playerAppearance.setFacialRatio(readC());

		playerAppearance.setArmLength(readC());
		playerAppearance.setLegLength(readC());
		
		playerAppearance.setShoulders(readC());
		playerAppearance.setFaceShape(readC());
		    
		readC(); // always 0
		readC(); // always 0
		readC(); // always 0
		playerAppearance.setHeight(readF());
	}

	/**
	 * Actually does the dirty job
	 */
	@Override
	protected void runImpl()
	{
		AionConnection client = getConnection();

		/* Some reasons why player can' be created */
		if(!PlayerService.isValidName(playerCommonData.getName()))
		{
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_INVALID_NAME));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		if(!PlayerService.isFreeName(playerCommonData.getName()))
		{
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_NAME_ALREADY_USED));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		if(!playerCommonData.getPlayerClass().isStartingClass())
		{
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.FAILED_TO_CREATE_THE_CHARACTER));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}

		Account account = client.getAccount();

		Player player = PlayerService.newPlayer(playerCommonData, playerAppearance, account);

		if(!PlayerService.storeNewPlayer(player, account.getName(), account.getId()))
		{
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_DB_ERROR));
		}
		else
		{
			List<Item> equipment = DAOManager.getDAO(InventoryDAO.class).loadEquipment(player.getObjectId());
			PlayerAccountData accPlData = new PlayerAccountData(playerCommonData, playerAppearance, equipment, null);

			accPlData.setCreationDate(new Timestamp(System.currentTimeMillis()));
			PlayerService.storeCreationTime(player.getObjectId(), accPlData.getCreationDate());

			account.addPlayerAccountData(accPlData);
			client.sendPacket(new SM_CREATE_CHARACTER(accPlData, SM_CREATE_CHARACTER.RESPONSE_OK));
		}
	}
}
