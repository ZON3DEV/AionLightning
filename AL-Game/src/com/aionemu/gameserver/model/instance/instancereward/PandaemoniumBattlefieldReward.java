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
package com.aionemu.gameserver.model.instance.instancereward;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.instance.playerreward.PandaemoniumBattlefieldPlayerReward;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Falke_34
 */
public class PandaemoniumBattlefieldReward extends InstanceReward<PandaemoniumBattlefieldPlayerReward> {

	private int points;
	private int npcKills;
	private int rank = 7;
	protected WorldMapInstance instance;

	public PandaemoniumBattlefieldReward(Integer mapId, int instanceId, WorldMapInstance instance) {
		super(mapId, instanceId);
	}

	public void portToPosition(Player player) {
		float Rx = Rnd.get(-5, 5);
		float Ry = Rnd.get(-5, 5);
        Point3D AsmoStartPoint = new Point3D(1240.918f + Rx, 1177.3456f + Ry, 214.66058f);

		if (player.getRace() == Race.ASMODIANS) {
			TeleportService2.teleportTo(player, mapId, instanceId, AsmoStartPoint.getX(), AsmoStartPoint.getY(), AsmoStartPoint.getZ(), (byte) 15);
		}
	}

	public void addPoints(int points) {
		this.points += points;
	}

	public int getPoints() {
		return points;
	}

	public void addNpcKill() {
		npcKills++;
	}

	public int getNpcKills() {
		return npcKills;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}
}
