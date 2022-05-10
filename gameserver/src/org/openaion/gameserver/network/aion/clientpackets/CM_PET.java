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

import java.util.List;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.PlayerPetsDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.ToyPet;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.templates.item.ItemCategory;
import org.openaion.gameserver.model.templates.pet.PetTemplate;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.ToyPetService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * 
 * @author Sylar - Kamui
 * 
 */
public class CM_PET extends AionClientPacket
{
	
	private static Logger	log	= Logger.getLogger(CM_PET.class);
	
	private int actionId;
	private int petId;
	private String petName;
	private int decorationId;
	private int eggObjId;
	private int foodObjId;
	private int foodAmount;
	
	@SuppressWarnings("unused")
	private int unk1;
	@SuppressWarnings("unused")
	private int unk2;
	@SuppressWarnings("unused")
	private int unk3;
	@SuppressWarnings("unused")
	private int unk5;
	@SuppressWarnings("unused")
	private int unk6;
	
	public CM_PET(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		actionId = readH();
		switch(actionId)
		{
			case 1:
				//adopt
				eggObjId = readD();
				petId = readD();
				unk2 = readC();
				unk3 = readD();
				decorationId = readD();
				unk5 = readD();
				unk6 = readD();
				petName = readS();
				break;
			case 2:
			case 3:
			case 4:
				petId = readD();
				break;
			case 9:
				//feed
				unk1 = readD();
				foodObjId = readD();
				foodAmount = readD();
				break;
			case 10:
				petId = readD();
				petName = readS();
				break;
			default:
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();

		switch(actionId)
		{
			case 0:
				break;
			case 1:
				// adopt
				if(!ToyPetService.isValidName(petName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400643));//[That name is invalid. Please try another..]
					return;
				}
				Item petEgg = player.getInventory().getItemByObjId(eggObjId);
				if (petEgg != null)
				{
					ItemCategory eggCategory = petEgg.getItemTemplate().getItemCategory();
					
					if ( eggCategory != ItemCategory.CASH_PETADOPTION 
						&& eggCategory != ItemCategory.PETADOPTION
						&& eggCategory != ItemCategory.CHANGE_CHARACTER_NAME )
					{	
						log.info("[AUDIT] Player" + player.getName() + "trying to create pet: " + petId + " from item: "+petEgg.getItemId());
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400642));
						return;
					}
							
					PetTemplate pt = DataManager.PET_DATA.getPetTemplateByEggId(petEgg.getItemId());
					if ( pt == null || pt.getPetId() != petId )
					{
						log.info("[AUDIT] Player" + player.getName() + "trying to create pet: " + petId + " from item: "+petEgg.getItemId());
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400642));
						return;
					}
					
					List<ToyPet> list = DAOManager.getDAO(PlayerPetsDAO.class).getPlayerPets(player.getObjectId());
					ToyPet pet = null;
					for(ToyPet p : list)
					{
						if(p.getPetId() == petId)
						{
							pet = p;
							break;
						}
					}
					
					if (pet != null)
					{
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400651));
						return;
					}
					
					if (!player.getInventory().removeFromBagByObjectId(eggObjId, 1))
						return;
					
					ToyPetService.getInstance().createPetForPlayer(player, petId, decorationId, petName);
				}
				break;
			case 2:
				// surrender
				ToyPetService.getInstance().surrenderPet(player, petId);
				break;
			case 3:
				// spawn
				ToyPetService.getInstance().summonPet(player, petId);
				break;
			case 4:
				// dismiss
				ToyPetService.getInstance().dismissPet(player, petId);
				break;
			case 9:
				// feed
				// TODO: you get this message when try to move after state change
				// thus, it appears after the rest as well
				if (player.getState() != CreatureState.ACTIVE.getId() &&
					player.getState() != (CreatureState.ACTIVE.getId() | CreatureState.POWERSHARD.getId()))
				{
					PacketSendUtility.sendPacket(player, 
						SM_SYSTEM_MESSAGE.STR_MSGBOX_TOYPET_FEED_CANT_FEED(SM_SYSTEM_MESSAGE.MSG_ASF_COMBAT));
					return;
				}
				ToyPetService.getInstance().feedPet(player, foodObjId, foodAmount);
				break;
			case 10:
				// rename
				if(!ToyPetService.isValidName(petName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400643));//[That name is invalid. Please try another..]
					return;
				}
				ToyPetService.getInstance().renamePet(player, petId, petName);
				break;
			default:
				break;
		}
	}
}
