/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.controllers.instances;


import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.controllers.NpcController;
import org.openaion.gameserver.controllers.movement.StartMovingListener;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;

/**
 * @author PZIKO333
 * @Finalized GosthMan
 */

public class HaramelController extends NpcController {

	@Override
	public void onDialogRequest(final Player player) {
		getOwner().getAi().handleEvent(Event.TALK);

		switch (getOwner().getNpcId()) {
			//Lift
			case 730321:
				TeleportService.teleportTo(player, 300200000, player.getInstanceId(), 220, 213, 126, 0);
				break;
			//Dimensional Gate
			case 700852:
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(), 3000, 1));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner().getObjectId()), true);

				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().attach(new StartMovingListener() {

					@Override
					public void moved() {
						player.getController().cancelTask(TaskId.ITEM_USE);
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(), 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getOwner().getObjectId()), true);
					}
				});
				player.getController().addNewTask(TaskId.ITEM_USE,
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(), 3000, 0));
							PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getOwner().getObjectId()), true);
							analyzePortation(player);
						}

						private void analyzePortation(final Player player) {
							if(player.getTribe().equals("PC")) {
								TeleportService.teleportTo(player, 210030000, 1, 2539, 834, 104, 0);
							} else {
								TeleportService.teleportTo(player, 220030000, 1, 2909, 1456, 252, 0);
							}
						}
					}, 3000));
				break;
		}
	}

	@Override
	public void onDie(Creature lastAttacker) {
		super.onDie(lastAttacker);
		Npc owner = getOwner();
		Player player;
		player = (Player) lastAttacker;

		if(owner.getNpcId() == 216922)
		{
			owner.getController().onDelete();
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 457));
			InstanceService.addNewSpawn(300200000, player.getInstanceId(), 700852, (float) 224.59f, (float) 331.14f, (float) 142.89f, (byte) 90, true);
			PlayerClass playerClass = player.getCommonData().getPlayerClass();
			if (playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR)
			InstanceService.addNewSpawn(300200000, player.getInstanceId(), 700829, (float) 224.13f, (float) 268.60f, (float) 144.89f, (byte) 90, true);
			else if (playerClass ==PlayerClass.ASSASSIN || playerClass == PlayerClass.RANGER)
			InstanceService.addNewSpawn(300200000, player.getInstanceId(), 700830, (float) 224.13f, (float) 268.60f, (float) 144.89f, (byte) 90, true);
			else if (playerClass ==PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER)
			InstanceService.addNewSpawn(300200000, player.getInstanceId(), 700831, (float) 224.13f, (float) 268.60f, (float) 144.89f, (byte) 90, true);
			else if (playerClass ==PlayerClass.CLERIC || playerClass == PlayerClass.CHANTER)
			InstanceService.addNewSpawn(300200000, player.getInstanceId(), 700832, (float) 224.13f, (float) 268.60f, (float) 144.89f, (byte) 90, true);      	
		}
	}
}
