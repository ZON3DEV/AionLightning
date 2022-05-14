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

package instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Eloann
 */
@InstanceID(301140000)
public class DanuarSanctuaryInstance extends GeneralInstanceHandler {

	private int danuarSanctuaryBossEasy;
	private int danuarSanctuaryBossMiddle;
	private int danuarSanctuaryBossStrong;
	private boolean isStartTimer1 = false;
	private boolean isStartTimer2 = false;
	private boolean isStartTimer3 = false;
	private Map<Integer, StaticDoor> doors;
	private List<Integer> movies = new ArrayList<Integer>();

	@Override
	public void onEnterInstance(Player player) {
		if (movies.contains(500)) {
			return;
		}
		doors = instance.getDoors();
		sendMovie(player, 500);
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);

		switch (Rnd.get(1, 3)) {
			case 1: // Danuar Sanctuary Boss Easy
				spawn(233086, 1056.5698f, 693.86584f, 282.0391f, (byte) 30);
				spawn(233089, 1045.4534f, 682.2679f, 282.0391f, (byte) 60);
				spawn(233090, 1056.4889f, 670.9826f, 282.0391f, (byte) 91);
				break;
			case 2: // Danuar Sanctuary Boss Middle
				spawn(233380, 1045.4534f, 682.2679f, 282.0391f, (byte) 60);
				spawn(233381, 1056.4889f, 670.9826f, 282.0391f, (byte) 91);
				spawn(233382, 1056.5698f, 693.86584f, 282.0391f, (byte) 30);
				break;
			case 3: // Danuar Sanctuary Boss Strong
				spawn(233383, 1045.4534f, 682.2679f, 282.0391f, (byte) 60);
				spawn(233384, 1056.4889f, 670.9826f, 282.0391f, (byte) 91);
				spawn(233385, 1056.5698f, 693.86584f, 282.0391f, (byte) 30);
				break;
		}
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 701859: // Danuar Sanctuary Switch 1
				ItemService.addItem(player, 182400001, 1);
				break;
			case 701860: // Danuar Sanctuary Switch 2
				ItemService.addItem(player, 182400001, 10);
				break;
			case 701861: // Danuar Sanctuary Switch 3
				ItemService.addItem(player, 182400001, 100);
				break;
			case 701862: // Danuar Sanctuary Switch 4 (Activate the device in
							// room 4)
				openDoor(0);
				break;
		}
	}

	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 233187: // Attack the rocks to activate the updraft
				TeleportService2.teleportTo(player, 301140000, 887.1296f, 845.2534f, 292.86874f, (byte) 112, TeleportAnimation.BEAM_ANIMATION);
				despawnNpc(npc);
				break;
			case 233086:
			case 233089:
			case 233090:
				danuarSanctuaryBossEasy++;
				if (danuarSanctuaryBossEasy == 3) {
					spawn(701876, 1075.5334f, 682.4891f, 282.0391f, (byte) 60); // Exit
				}
				break;
			case 233380:
			case 233381:
			case 233382:
				danuarSanctuaryBossMiddle++;
				if (danuarSanctuaryBossMiddle == 3) {
					spawn(701876, 1075.5334f, 682.4891f, 282.0391f, (byte) 60); // Exit
				}
				break;
			case 233383:
			case 233384:
			case 233385:
				danuarSanctuaryBossStrong++;
				if (danuarSanctuaryBossStrong == 3) {
					spawn(701876, 1075.5334f, 682.4891f, 282.0391f, (byte) 60); // Exit
				}
				break;
		}
	}

	// The players have 15 minutes to clear the way
	// After 15 min boss "ELITE" will be removed
	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("STR_ZONE_NAME_IDLDF5_UNDER_02_A_301140000")) {
			if (!isStartTimer1) {
				isStartTimer1 = true;
				System.currentTimeMillis();
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900));
				final Npc mobwaypath1A = (Npc) spawn(233415, 1050.2266f, 276.24228f, 309.3889f, (byte) 64);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						mobwaypath1A.getController().delete();
					}
				}, 900000);
			}
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("STR_ZONE_NAME_IDLDF5_UNDER_02_B_301140000")) {
			if (!isStartTimer2) {
				isStartTimer2 = true;
				System.currentTimeMillis();
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900));
				final Npc mobwaypath1B = (Npc) spawn(701871, 717.85614f, 980.1326f, 318.71698f, (byte) 110);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						mobwaypath1B.getController().delete();
					}
				}, 900000);
			}
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("STR_ZONE_NAME_IDLDF5_UNDER_02_C_301140000")) {
			if (!isStartTimer3) {
				isStartTimer3 = true;
				System.currentTimeMillis();
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900));
				final Npc mobwaypath1C = (Npc) spawn(701871, 1005.93787f, 1366.8308f, 337.25f, (byte) 13);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						mobwaypath1C.getController().delete();
					}
				}, 900000);
			}
		}
	}

	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	private void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}
}
