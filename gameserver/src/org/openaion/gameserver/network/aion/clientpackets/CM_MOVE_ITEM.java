/**
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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

import java.util.Collections;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.StorageType;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import org.openaion.gameserver.network.aion.serverpackets.SM_WAREHOUSE_UPDATE;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author alexa026, kosyachok
 * 
 */
public class CM_MOVE_ITEM extends AionClientPacket
{

	/**
	 * Target object id that client wants to TALK WITH or 0 if wants to unselect
	 */
	private int					targetObjectId;
	private int					source;
	private int					destination;
	private int					slot;
	private static final Logger	log	= Logger.getLogger(CM_MOVE_ITEM.class);
	/**
	 * Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
	 * @param opcode
	 */
	public CM_MOVE_ITEM(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		targetObjectId = readD();// empty
		source = readC();        //FROM (0 - player inventory, 1 - regular warehouse, 2 - account warehouse, 3 - legion warehouse)
		destination = readC();   //TO
		slot = readH();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		
		if (player != null)
		{
			if(player.isTrading() && source != destination)
			{
				log.warn("[AUDIT] Trying to use trade exploit: " + player.getName());

				Item item = player.getStorage(source).getItemByObjId(targetObjectId);

				if(source == StorageType.CUBE.getId())
					PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(Collections.singletonList(item)));
				else
					PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_UPDATE(item, source));

				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player,EmotionType.END_LOOT,0,0));

				return;
			}
			
			//prevent from using items endless  amount of times with packets.
			if (player.getController().hasTask(TaskId.ITEM_USE))
			{
				log.info("[AUDIT] "+player.getName()+" sending fake CM_MOVE_ITEM packet. Trying to dupe item.");
				return;
			}

			ItemService.moveItem(player, targetObjectId, source, destination, slot);
			
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player,EmotionType.END_LOOT,0,0));
		}
	}
}
