/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */


package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.ItemUseObserver;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.actions.AuthorizeAction;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="AuthorizeAction")
public class AuthorizeAction extends AbstractItemAction {

	@Override
  public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (!targetItem.getItemTemplate().isAccessory() && !targetItem.getItemTemplate().isFeather()) {
            return false;
        }
        if (targetItem.getItemTemplate().getMaxAuthorize() == 0) {
            return false;
        }
        if (targetItem.getAuthorize() >= targetItem.getItemTemplate().getMaxAuthorize()) {
            return false;
        }
        return true;
    }

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));

		final ItemUseObserver observer = new ItemUseObserver() {
			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 3, 0));
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_CANCEL(targetItem.getNameId()));
			}
		};
        player.getObserveController().attach(observer);
        player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (player.getInventory().decreaseByItemId(parentItem.getItemId(), 1L)) {
                    if (!AuthorizeAction.this.isSuccess(targetItem.getAuthorize())) {
                        PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 2, 0));
                        targetItem.setAuthorize(0);
                    if (targetItem.getItemTemplate().isFeather()) {
					if (targetItem.isEquipped())
						player.getEquipment().getEquippedItemByObjId(targetItem.getObjectId()).decreaseItemCount(targetItem.getItemCount());
					  else
						player.getInventory().decreaseByObjectId(targetItem.getObjectId(), targetItem.getItemCount());
					}
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_FAILED(targetItem.getNameId()));
                    } else {
                        PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 1, 0));
                        targetItem.setAuthorize(targetItem.getAuthorize() + 1);
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_SUCCEEDED(targetItem.getNameId(), targetItem.getAuthorize()));
                    }
                    PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
                    player.getObserveController().removeObserver(observer);
                    if (targetItem.isEquipped()) {
                        player.getGameStats().updateStatsVisually();
                    }
                    ItemPacketService.updateItemAfterInfoChange(player, targetItem);
                    if (targetItem.isEquipped()) {
                        player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
                    } else {
                        player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
                    }
                }
            }
        }, 5000L));
    }

	public boolean isSuccess(int authorize) {
		if (authorize <= 0)
			return true;

		double rnd;
		try {
			rnd = 1000 *  Math.exp(-0.150 * authorize);
		}
		catch (Exception e) {
			rnd = 0;
		}

		if (rnd < 200)
			rnd = 200;

		return Rnd.get(0, 1000) < rnd;
	}
}