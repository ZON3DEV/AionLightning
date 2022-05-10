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

package com.aionemu.gameserver.utils.audit;

import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;

import javolution.util.FastMap;

/**
 * @author Waii
 *
 */
public class GMService {

	public static final GMService getInstance() {
		return SingletonHolder.instance;
	}

	private Map<Integer, Player> gms = new FastMap<Integer, Player>();
	private boolean announceAny = false;
	private List<Byte> announceList;

	private GMService() {

		announceList = new ArrayList<Byte>();
		announceAny = AdminConfig.ANNOUNCE_LEVEL_LIST.equals("*");
		if (!announceAny) {
			try {
				for (String level : AdminConfig.ANNOUNCE_LEVEL_LIST.split(",")) {
					announceList.add(Byte.parseByte(level));
				}
			} catch (Exception e) {
				announceAny = true;
			}
		}
	}

	public void onPlayerLogin(Player player) {
		if (player.isGM()) {
			gms.put(player.getObjectId(), player);
		}
	}

	public void onPlayerLogedOut(Player player) {
		if (player.isGM()) {
			gms.remove(player.getObjectId());
		}
	}

	public Collection<Player> getGMs() {
		return gms.values();
	}

	public void onPlayerAvailable(Player player) {
		if (player.isGM()) {
			gms.put(player.getObjectId(), player);
			String adminTag = "%s";
			StringBuilder sb = new StringBuilder(adminTag);

			if (AdminConfig.CUSTOMTAG_ENABLE) {
				if (player.getAccessLevel() == 1) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS1.substring(0, AdminConfig.CUSTOMTAG_ACCESS1.length() - 3)).toString();
				} else if (player.getAccessLevel() == 2) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS2.substring(0, AdminConfig.CUSTOMTAG_ACCESS2.length() - 3)).toString();
				} else if (player.getAccessLevel() == 3) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS3.substring(0, AdminConfig.CUSTOMTAG_ACCESS3.length() - 3)).toString();
				} else if (player.getAccessLevel() == 4) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS4.substring(0, AdminConfig.CUSTOMTAG_ACCESS4.length() - 3)).toString();
				} else if (player.getAccessLevel() == 5) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS5.substring(0, AdminConfig.CUSTOMTAG_ACCESS5.length() - 3)).toString();
				} else if (player.getAccessLevel() == 6) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS6.substring(0, AdminConfig.CUSTOMTAG_ACCESS6.length() - 3)).toString();
				} else if (player.getAccessLevel() == 7) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS7.substring(0, AdminConfig.CUSTOMTAG_ACCESS7.length() - 3)).toString();
				} else if (player.getAccessLevel() == 8) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS8.substring(0, AdminConfig.CUSTOMTAG_ACCESS8.length() - 3)).toString();
				} else if (player.getAccessLevel() == 9) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS9.substring(0, AdminConfig.CUSTOMTAG_ACCESS9.length() - 3)).toString();
				} else if (player.getAccessLevel() == 10) {
					adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS10.substring(0, AdminConfig.CUSTOMTAG_ACCESS10.length() - 3)).toString();
				}
			}

			Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(),
						"Information : " + String.format(adminTag, player.getName()) + LanguageHandler.translate(CustomMessageId.ANNOUNCE_GM_CONNECTION));
			}
		}
	}

	public void onPlayerUnavailable(Player player) {
		gms.remove(player.getObjectId());
		String adminTag = "%s";
		StringBuilder sb = new StringBuilder(adminTag);

		if (AdminConfig.CUSTOMTAG_ENABLE) {
			if (player.getAccessLevel() == 1) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS1.substring(0, AdminConfig.CUSTOMTAG_ACCESS1.length() - 3)).toString();
			} else if (player.getAccessLevel() == 2) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS2.substring(0, AdminConfig.CUSTOMTAG_ACCESS2.length() - 3)).toString();
			} else if (player.getAccessLevel() == 3) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS3.substring(0, AdminConfig.CUSTOMTAG_ACCESS3.length() - 3)).toString();
			} else if (player.getAccessLevel() == 4) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS4.substring(0, AdminConfig.CUSTOMTAG_ACCESS4.length() - 3)).toString();
			} else if (player.getAccessLevel() == 5) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS5.substring(0, AdminConfig.CUSTOMTAG_ACCESS5.length() - 3)).toString();
			} else if (player.getAccessLevel() == 6) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS6.substring(0, AdminConfig.CUSTOMTAG_ACCESS6.length() - 3)).toString();
			} else if (player.getAccessLevel() == 7) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS7.substring(0, AdminConfig.CUSTOMTAG_ACCESS7.length() - 3)).toString();
			} else if (player.getAccessLevel() == 8) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS8.substring(0, AdminConfig.CUSTOMTAG_ACCESS8.length() - 3)).toString();
			} else if (player.getAccessLevel() == 9) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS9.substring(0, AdminConfig.CUSTOMTAG_ACCESS9.length() - 3)).toString();
			} else if (player.getAccessLevel() == 10) {
				adminTag = sb.insert(0, AdminConfig.CUSTOMTAG_ACCESS10.substring(0, AdminConfig.CUSTOMTAG_ACCESS10.length() - 3)).toString();
			}
		}

		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(),
					"Information : " + String.format(adminTag, player.getName()) + LanguageHandler.translate(CustomMessageId.ANNOUNCE_GM_DECONNECTION));
		}
	}

	public void broadcastMesage(String message) {
		SM_MESSAGE packet = new SM_MESSAGE(0, null, message, ChatType.YELLOW);
		for (Player player : gms.values()) {
			PacketSendUtility.sendPacket(player, packet);
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final GMService instance = new GMService();
	}
}
