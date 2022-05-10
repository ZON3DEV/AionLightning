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

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.PlayerAppearanceDAO;
import org.openaion.gameserver.dao.PlayerDAO;
import org.openaion.gameserver.model.Gender;
import org.openaion.gameserver.model.account.Account;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerAppearance;
import org.openaion.gameserver.model.gameobjects.player.PlayerCommonData;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.services.PlayerService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * In this packets aion client is requesting edit of character.
 *
 * @author IlBuono
 *
 */
public class CM_CHARACTER_EDIT extends AionClientPacket
{

    private int objectId;

    private boolean gender_change;

    private boolean check_ticket = true;

	/**
	 * Constructs new instance of <tt>CM_CREATE_CHARACTER </tt> packet
	 *
	 * @param opcode
	 */
	public CM_CHARACTER_EDIT(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
        AionConnection client = getConnection();
		Account account = client.getAccount();
        objectId = readD();
        Player player = PlayerService.getPlayer(objectId, account);
        if(player.getPlayerAccount().getId() != account.getId())
        {
        	Logger.getLogger(CM_CHARACTER_EDIT.class).warn("HACK ATTEMPT: " + client.getIP() + " trying to hack chaOid " + objectId + " not on his account!");
        	return;
        }
        readB(52);
        PlayerCommonData playerCommonData = player.getCommonData();
        PlayerAppearance playerAppearance = player.getPlayerAppearance();
        //Before modify appearance, we do a check of ticket
        int gender = readD();
        gender_change = playerCommonData.getGender().getGenderId() == gender ? false : true;
        if(!gender_change)
        {
            if (player.getInventory().getItemCountByItemId(169650000) == 0 && player.getInventory().getItemCountByItemId(169650001) == 0)
            {
                check_ticket = false;
                return;
            }
        }
        else
        {
            if (player.getInventory().getItemCountByItemId(169660000) == 0 && player.getInventory().getItemCountByItemId(169660001) == 0)
            {
                check_ticket = false;
                return;
            }
        }
        playerCommonData.setGender(gender == 0 ? Gender.MALE : Gender.FEMALE);
		readD(); //race
		readD(); //player class

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

		playerAppearance.setFacialRatio(readC());
		
		readC(); // always 0

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
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionConnection client = getConnection();
		CM_ENTER_WORLD.enterWorld(client, objectId);
		Player player = client.getActivePlayer();
        if (!check_ticket)
        {
            if (!gender_change)
                PacketSendUtility.sendSysMessage(player, "You must have a Plastic Surgery Ticket!");
            else
                PacketSendUtility.sendSysMessage(player, "You must have a Gender Switch Ticket!");
        }
        else
        {
        	//Remove ticket and save appearance
        	if(!gender_change)
            {
                if (player.getInventory().getItemCountByItemId(169650000) > 0) //plastic surgery ticket normal
					player.getInventory().removeFromBagByItemId(169650000, 1);
                else if (player.getInventory().getItemCountByItemId(169650001) > 0) //plastic surgery ticket event
                	player.getInventory().removeFromBagByItemId(169650001, 1);
            }
            else
            {
                if (player.getInventory().getItemCountByItemId(169660000) > 0) //gender switch ticket normal
					player.getInventory().removeFromBagByItemId(169660000, 1);
                else if (player.getInventory().getItemCountByItemId(169660001) > 0) //gender switch ticket event
                	player.getInventory().removeFromBagByItemId(169660001, 1);
                DAOManager.getDAO(PlayerDAO.class).storePlayer(player); //save new gender
            }
        	DAOManager.getDAO(PlayerAppearanceDAO.class).store(player);	//save new appearance
        }
	}
}