/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Equipment;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import org.openaion.gameserver.restrictions.RestrictionsManager;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * 
 * @author Avol modified by ATracer
 */
public class CM_EQUIP_ITEM extends AionClientPacket
{
	public int	slotRead;
	public int	itemUniqueId;
	public int	action;
	
	public CM_EQUIP_ITEM(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		action = readC(); // 0/1 = equip/unequip
		slotRead = readD();
		itemUniqueId = readD();
	}

	@Override
	protected void runImpl()
	{
		final Player activePlayer = getConnection().getActivePlayer();
		if(activePlayer == null)
			return;
		Equipment equipment = activePlayer.getEquipment();
		Item resultItem = null;

		if(!RestrictionsManager.canChangeEquip(activePlayer))
			return;
		
		switch(action)
		{
			case 0:
				resultItem = equipment.equipItem(itemUniqueId, slotRead);
				break;
			case 1:
				resultItem = equipment.unEquipItem(itemUniqueId, slotRead);
				break;
			case 2:
				if (activePlayer.getController().hasTask(TaskId.ITEM_USE) && !activePlayer.getController().getTask(TaskId.ITEM_USE).isDone())
				{
					PacketSendUtility.sendPacket(activePlayer, SM_SYSTEM_MESSAGE.CANT_EQUIP_ITEM_IN_ACTION());
					return;
				}
				equipment.switchHands();
				break;
		}

		if(resultItem != null || action == 2)
		{
			PacketSendUtility.broadcastPacket(activePlayer, new SM_UPDATE_PLAYER_APPEARANCE(activePlayer.getObjectId(),
				equipment.getEquippedItemsWithoutStigma()), true);		
		}
		
		if (!equipment.isShieldEquipped())
		{
			for (Effect effect : activePlayer.getEffectController().getNoShowEffects())
			{
			   if (effect.isStance())
				   activePlayer.getEffectController().removeNoshowEffect(effect.getSkillId());
			}
		}
		
	}
}
