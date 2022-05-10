/**
 * This file is part of aion-unique <www.aion-unique.com>.
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

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.itemengine.actions.AbstractItemAction;
import org.openaion.gameserver.itemengine.actions.ItemActions;
import org.openaion.gameserver.itemengine.actions.UnwrapAction;
import org.openaion.gameserver.model.DescriptionId;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.items.WrapperItem;
import org.openaion.gameserver.model.templates.item.ItemRace;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.item.LevelRestrict;
import org.openaion.gameserver.model.templates.item.LevelRestrictType;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.restrictions.RestrictionsManager;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Avol
 */
public class CM_USE_ITEM extends AionClientPacket {

	public int uniqueItemId;
	public int type, targetItemId;

	private static final Logger log = Logger.getLogger(CM_USE_ITEM.class);

	public CM_USE_ITEM(int opcode) {
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		uniqueItemId = readD();
		type = readC();
		if (type == 2)
		{
			targetItemId = readD();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() 
	{
		Player player = getConnection().getActivePlayer();
		Item item = player.getInventory().getItemByObjId(uniqueItemId);
		
		if(item == null)
		{
			log.warn(String.format("CHECKPOINT: null item use action: %d %d", player.getObjectId(), uniqueItemId));
			return;
		}
		
		if(!RestrictionsManager.canUseItem(player))
			return;
		
		//check item race
		ItemTemplate template = item.getItemTemplate();
		switch(template.getRace())
		{
			case ASMODIANS:
				if(player.getCommonData().getRace() != Race.ASMODIANS)
					return;
				break;
			case ELYOS:
				if(player.getCommonData().getRace() != Race.ELYOS)
					return;
				break;
		}	
		
		//check class restrict
		if (!template.checkClassRestrict(player.getCommonData().getPlayerClass()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_INVALID_CLASS());
			return;
		}
			
		//check level restrict
		LevelRestrict restrict = template.getRectrict(player.getCommonData().getPlayerClass(), player.getLevel());
		LevelRestrictType restrictType = restrict.getType();
		if (restrictType == LevelRestrictType.LOW)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_LOW_LEVEL_MUST_BE_THIS_LEVEL(
				restrict.getLevel(), new DescriptionId(Integer.parseInt(item.getName()))));
			return;
		}
		else if (restrictType == LevelRestrictType.HIGH)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_HIGH_LEVEL(
				restrict.getLevel(), new DescriptionId(Integer.parseInt(item.getName()))));
			return;
		}
			
		HandlerResult hResult = QuestEngine.getInstance().onItemUseEvent(new QuestCookie(null, player, template.getItemQuestId(), 0), item);
		if (hResult == HandlerResult.FAILED)
			return;
		
		//check use item multicast delay exploit cast (spam)
		if(player.isCasting())
		{
			//PacketSendUtility.sendMessage(this.getOwner(), "You must wait until cast time finished to use skill again.");
			return;
		}
		
		Item targetItem = player.getInventory().getItemByObjId(targetItemId);
		if(targetItem == null)
			targetItem = player.getEquipment().getEquippedItemByObjId(targetItemId);

		ItemActions itemActions = item.getItemTemplate().getActions();
		ArrayList<AbstractItemAction> actions = new ArrayList<AbstractItemAction>();
		
		ItemRace playerRace = player.getCommonData().getRace() == Race.ASMODIANS ?
			ItemRace.ASMODIANS : ItemRace.ELYOS;
		WrapperItem wrapper = DataManager.WRAPPED_ITEM_DATA.getItemWrapper(item.getItemId());
		if (wrapper != null && wrapper.hasAnyItems(playerRace))
		{
			if (itemActions == null)
				itemActions = new ItemActions();
			itemActions.getItemActions().add(new UnwrapAction());
		}
		
		if (itemActions == null)
			return;
		
		for (AbstractItemAction itemAction : itemActions.getItemActions())
		{
			// check if the item can be used before placing it on the cooldown list.
			if (itemAction.canAct(player, item, targetItem))
				actions.add(itemAction);
		}
		
		
		if(actions.size() == 0)
			return;
		
		// Store Item CD in server Player variable.
		// Prevents potion spamming, and relogging to use kisks/aether jelly/long CD items.
		if (player.isItemUseDisabled(item.getItemTemplate().getDelayId()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANT_USE_UNTIL_DELAY_TIME);
			return;
		}
		
		int useDelay = item.getItemTemplate().getDelayTime();
		player.addItemCoolDown(item.getItemTemplate().getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);

		for (AbstractItemAction itemAction : actions)
		{
			itemAction.act(player, item, targetItem);
		}
	}
}
