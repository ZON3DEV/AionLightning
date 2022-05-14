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

package ai.instance.infinityShard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Eloann
 * @rework Swig
 */
@AIName("hyperion")
public class HyperionAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	private Future<?> skillTask;
	private Future<?> BlasterTask;
	private Future<?> EnergyTask;
	protected List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			startSkillTask();
			startBlasterTask();
			startEnergyTask();
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 75:
					case 60:
					case 55:
						spawnHyperionNormal1();
						break;
					case 80:
					case 47:
						AI2Actions.useSkill(this, 21245);
						spawnHyperionEasy();
						break;
					case 52:
					case 35:
					case 20:
						AI2Actions.useSkill(this, 21253);
						spawnHyperionNormal();
						break;
					case 50:
					case 25:
						AI2Actions.useSkill(this, 21244);
						spawnHyperionHard();
						break;
					case 40:
						AI2Actions.useSkill(this, 21244);
						break;
					case 30:
						cancelEnergyTask();
						AI2Actions.useSkill(this, 21248);
						spawnHyperionHard();
						break;
					case 10:
						AI2Actions.useSkill(this, 21246);
						spawnHyperionNormal();
						break;
					case 5:
						AI2Actions.useSkill(this, 21249);
						break;
					case 2:
						AI2Actions.useSkill(this, 21249);
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void spawnHyperionEasy() {
		spawn(231096, 148.12894f, 148.34091f, 124.03375f, (byte) 105);
		spawn(233292, 108.5921f, 145.41702f, 114.03043f, (byte) 20);
		spawn(231103, 132.1073f, 127.7515f, 112.1236f, (byte) 35);
		spawn(231103, 129.2615f, 137.8656f, 110.5048f, (byte) 82);
		spawn(231103, 125.4013f, 129.6880f, 112.1227f, (byte) 22);
		spawn(233289, 110.090965f, 128.28905f, 124.15179f, (byte) 43);
	}

	private void spawnHyperionNormal() {
		spawn(233288, 148.12894f, 148.34091f, 124.03375f, (byte) 105);
		spawn(233294, 108.5921f, 145.41702f, 114.03043f, (byte) 20);
		spawn(231103, 150.05635f, 128.56758f, 114.49583f, (byte) 16);
		spawn(231103, 136.9867f, 133.6464f, 112.1236f, (byte) 52);
		spawn(231103, 124.3499f, 145.8533f, 112.1236f, (byte) 101);
		spawn(233296, 110.090965f, 128.28905f, 124.15179f, (byte) 43);
	}

	private void spawnHyperionNormal1() {
		spawn(233292, 148.12894f, 148.34091f, 124.03375f, (byte) 105);
		spawn(233294, 108.5921f, 145.41702f, 114.03043f, (byte) 20);
		spawn(233295, 150.05635f, 128.56758f, 114.49583f, (byte) 16);
		spawn(231103, 140.8518f, 139.3699f, 112.1228f, (byte) 63);
		spawn(231103, 119.3952f, 140.4778f, 112.1228f, (byte) 114);
		spawn(233295, 110.090965f, 128.28905f, 124.15179f, (byte) 43);
	}

	private void spawnHyperionHard() {
		spawn(233288, 148.12894f, 148.34091f, 124.03375f, (byte) 105);
		spawn(233299, 148.12894f, 148.34091f, 124.03375f, (byte) 105);
		spawn(233294, 108.5921f, 145.41702f, 114.03043f, (byte) 20);
		spawn(233298, 150.05635f, 128.56758f, 114.49583f, (byte) 16);
		spawn(231103, 134.50612f, 141.6616f, 112.1228f, (byte) 72);
		spawn(231103, 121.2149f, 133.1198f, 112.1220f, (byte) 10);
		spawn(231103, 125.4013f, 129.68802f, 112.1227f, (byte) 22);
		spawn(233298, 110.090965f, 128.28905f, 124.15179f, (byte) 43);
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 80, 75, 60, 55, 52, 50, 40, 35, 30, 25, 20, 10, 5, 2 });
	}

	private void startSkillTask() {
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelskillTask();
				} else {
					Throw();
				}
			}
		}, 30000, 120000);
	}

	private void startBlasterTask() {
		BlasterTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelBlasterTask();
				} else {
					Blaster();
				}
			}
		}, 2000, 90000);
	}

	private void startEnergyTask() {
		EnergyTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelEnergyTask();
				} else {
					Energy();
				}
			}
		}, 10000, 160000);
	}

	private void cancelskillTask() {
		if (skillTask != null && !skillTask.isCancelled()) {
			skillTask.cancel(true);
		}
	}

	private void cancelBlasterTask() {
		if (BlasterTask != null && !BlasterTask.isCancelled()) {
			BlasterTask.cancel(true);
		}
	}

	private void cancelEnergyTask() {
		if (EnergyTask != null && !EnergyTask.isCancelled()) {
			EnergyTask.cancel(true);
		}
	}

	private void Throw() {
		SkillEngine.getInstance().getSkill(getOwner(), 21250, 55, getOwner()).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(getOwner(), 21251, 55, getOwner()).useNoAnimationSkill();
			}
		}, 5000);
	}

	private void Blaster() {
		SkillEngine.getInstance().getSkill(getOwner(), 21241, 60, getOwner().getTarget()).useNoAnimationSkill();
		Player target = getRandomTarget();
		if (target == null) {
		}
	}

	private void Energy() {
		SkillEngine.getInstance().getSkill(getOwner(), 21247, 60, getOwner().getTarget()).useNoAnimationSkill();
		Player target = getRandomTarget();
		if (target == null) {
		}
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private void despawnAdds() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		deleteNpcs(instance.getNpcs(231096));
		deleteNpcs(instance.getNpcs(233292));
		deleteNpcs(instance.getNpcs(231103));
		deleteNpcs(instance.getNpcs(233289));
		deleteNpcs(instance.getNpcs(233288));
		deleteNpcs(instance.getNpcs(233294));
		deleteNpcs(instance.getNpcs(233296));
		deleteNpcs(instance.getNpcs(233295));
		deleteNpcs(instance.getNpcs(233299));
		deleteNpcs(instance.getNpcs(233298));
		deleteNpcs(instance.getNpcs(231104));
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		addPercent();
		cancelskillTask();
		cancelBlasterTask();
		cancelEnergyTask();
		isHome.set(true);
		despawnAdds();
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		percents.clear();
		despawnAdds();
		cancelskillTask();
		cancelBlasterTask();
		cancelEnergyTask();
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		percents.clear();
		cancelskillTask();
		cancelBlasterTask();
		cancelEnergyTask();
		despawnAdds();
	}
}
