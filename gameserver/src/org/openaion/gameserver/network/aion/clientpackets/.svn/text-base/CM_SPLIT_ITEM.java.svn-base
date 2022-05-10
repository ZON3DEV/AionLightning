/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 *
 * @author kosyak
 */
public class CM_SPLIT_ITEM extends AionClientPacket
{

	int sourceItemObjId;
	int sourceStorageType;
	long itemAmount;
	int destinationItemObjId;
	int destinationStorageType;
	int slotNum; // destination slot.

	public CM_SPLIT_ITEM(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		sourceItemObjId = readD();       // drag item unique ID. If merging and itemCount becoming null, this item must be deleted.
		itemAmount = readD();            // Items count to be moved.
		@SuppressWarnings("unused")
		byte[] zeros = readB(4);         // Nothing
		sourceStorageType = readC();     // Source storage
		destinationItemObjId = readD();  // Destination item unique ID if merging. Null if spliting.
		destinationStorageType = readC();// Destination storage
		slotNum = readH();               // Destination slot. Not needed right now, Items adding only to next available slot. Not needed at all when merge.
	}


	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();

		// Temp Fix To Avoid Legion Warehouse Dupe Exploit
		/*if(sourceStorageType == 3)
			return;*/
		
		if (player.isTrading())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INVENTORY_SPLIT_DURING_TRADE);
			return;
		}

		if(destinationItemObjId == 0)
			ItemService.splitItem(player, sourceItemObjId, itemAmount, slotNum, sourceStorageType, destinationStorageType);
		else
			ItemService.mergeItems(player, sourceItemObjId, itemAmount, destinationItemObjId, sourceStorageType, destinationStorageType);
	}
}
