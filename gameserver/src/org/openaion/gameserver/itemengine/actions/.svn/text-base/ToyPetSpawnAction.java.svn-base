/*
 * This file is part of aion-unique <aion-unique.org>.
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
package org.openaion.gameserver.itemengine.actions;

import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Kisk;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author Sarynth
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ToyPetSpawnAction")
public class ToyPetSpawnAction extends AbstractItemAction
{
	
	@XmlAttribute
	protected int npcid;
	
	@XmlAttribute
	protected int time;	
	
	/**
	 * 
	 * @return the Npc Id
	 */
	public int getNpcId()
	{
		return npcid;
	}
	
	public int getTime()
	{
		return time;
	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (player.getFlyState() != 0)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_BINDSTONE_ITEM_WHILE_FLYING);
			return false;
		}
		if(player.isInInstance())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_FAR_FROM_NPC);
			return false;
		}
		if(player.getWorldId() == 110010000 
			|| player.getWorldId() == 120010000 
			|| player.getWorldId() == 110020000
			|| player.getWorldId() == 120020000
			|| player.getWorldId() == 210010000
			|| player.getWorldId() == 220010000)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_FAR_FROM_NPC);
			return false;
		}

		return true;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		SpawnEngine spawnEngine = SpawnEngine.getInstance();
		float x = player.getX();
		float y = player.getY();
		float z = player.getZ();
		byte heading = (byte) ((player.getHeading() + 60)%120);
		int worldId = player.getWorldId();
		int instanceId = player.getInstanceId();

		SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, 
			instanceId, npcid, x, y, z, heading, 0, 0, true, true);
		
		final Kisk kisk = spawnEngine.spawnKisk(spawn, instanceId, player);

		// Schedule Despawn Action
		Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
				kisk.getController().onDespawn(true);
			}
		}, 7200000);
		// Fixed 2 hours 2 * 60 * 60 * 1000
		
		kisk.getController().addTask(TaskId.DESPAWN, task);
		
		//ShowAction
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
			parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId()), true);
			
		//RemoveKisk
		player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1);
	}
}
