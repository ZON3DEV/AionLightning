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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author DeathMagnestic
 * @author Cx3
 *
 */
@InstanceID(300800000)
public class InfinityShardInstance extends GeneralInstanceHandler {

	private int protection;
	private boolean isInstanceDestroyed;
	private Future<?> resonator;
	private FastMap<Integer, Future<?>> skillTask = new FastMap<Integer, Future<?>>().shared();
	private FastList<Integer> skillCount = FastList.newInstance();

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		Npc hyperion = instance.getNpc(231073);
		SkillEngine.getInstance().getSkill(hyperion, 21254, 60, hyperion).useNoAnimationSkill();
		Npc idegenerator1 = instance.getNpc(231074);
		SkillEngine.getInstance().getSkill(idegenerator1, 21371, 60, idegenerator1).useNoAnimationSkill();
		Npc idegenerator2 = instance.getNpc(231078);
		SkillEngine.getInstance().getSkill(idegenerator2, 21371, 60, idegenerator2).useNoAnimationSkill();
		Npc idegenerator3 = instance.getNpc(231082);
		SkillEngine.getInstance().getSkill(idegenerator3, 21371, 60, idegenerator3).useNoAnimationSkill();
		Npc idegenerator4 = instance.getNpc(231086);
		SkillEngine.getInstance().getSkill(idegenerator4, 21371, 60, idegenerator4).useNoAnimationSkill();
	}

	@Override
	public void onDie(Npc npc) {
		Npc hyperion = getNpc(231073);
		if (isInstanceDestroyed) {
			return;
		}
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 231074:
				sendMsg(1401795);
				protection++;
				removeProtection();
				npc.getController().onDelete();
				break;
			case 231078:
				sendMsg(1401795);
				protection++;
				removeProtection();
				npc.getController().onDelete();
				break;
			case 231082:
				sendMsg(1401795);
				protection++;
				removeProtection();
				npc.getController().onDelete();
				break;
			case 231086:
				sendMsg(1401795);
				protection++;
				removeProtection();
				npc.getController().onDelete();
				break;
			case 231073:
				spawn(730842, 124.669853f, 137.840668f, 113.942917f, (byte) 0);
				cancelResonatorTask();
				despawnNpc(231092);
				despawnNpc(231093);
				despawnNpc(231094);
				despawnNpc(231095);
				break;
			case 231087:
			case 231088:
			case 231089:
				isDeadGenerator1();
				break;
			case 231075:
			case 231076:
			case 231077:
				isDeadGenerator2();
				break;
			case 231083:
			case 231084:
			case 231085:
				isDeadGenerator3();
				break;
			case 231079:
			case 231080:
			case 231081:
				isDeadGenerator4();
				break;
			case 231092:
				if (!(hyperion.getAi2().getState() == (AIState.IDLE)) || !(hyperion.getAi2().getState() == (AIState.DIED))
						|| !(hyperion.getAi2().getState() == (AIState.DESPAWNED))) {
					resonatorSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
				break;
			case 231093:
				if (!(hyperion.getAi2().getState() == (AIState.IDLE)) || !(hyperion.getAi2().getState() == (AIState.DIED))
						|| !(hyperion.getAi2().getState() == (AIState.DESPAWNED))) {
					resonatorSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
				break;
			case 231094:
				if (!(hyperion.getAi2().getState() == (AIState.IDLE)) || !(hyperion.getAi2().getState() == (AIState.DIED))
						|| !(hyperion.getAi2().getState() == (AIState.DESPAWNED))) {
					resonatorSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
				break;
			case 231095:
				if (!(hyperion.getAi2().getState() == (AIState.IDLE)) || !(hyperion.getAi2().getState() == (AIState.DIED))
						|| !(hyperion.getAi2().getState() == (AIState.DESPAWNED))) {
					resonatorSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
				break;
		}
	}

	private void cancelResonatorTask() {
		if (resonator != null && !resonator.isCancelled()) {
			resonator.cancel(true);
		}
	}

	private void cancelskillTask(int npcId) {
		Future<?> task = skillTask.get(npcId);
		if (task != null && !task.isCancelled()) {
			task.cancel(true);
		}
		skillTask.remove(npcId);
	}

	private void startSkillTask(final Npc npc, final int skillId, final int messageId) {
		Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				Npc hyperion = getNpc(231073);
				if (hyperion == null) {
					return;
				}
				if (hyperion.getLifeStats().isAlreadyDead()) {
					return;
				}
				NpcShoutsService.getInstance().sendMsg(npc, messageId);
				SkillEngine.getInstance().getSkill(npc, skillId, 1, hyperion).useNoAnimationSkill();
				if (!skillCount.contains(npc.getNpcId())) {
					skillCount.add(npc.getNpcId());
				}
				if (skillCount.size() == 4) {
					stopInstance();
				}
			}
		}, 32000);
		skillTask.put(npc.getNpcId(), task);
	}

	private void stopInstance() {
		NpcShoutsService.getInstance().sendMsg(instance, 1401794, 0, false, 25, 0);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				NpcShoutsService.getInstance().sendMsg(instance, 1401909, 0, false, 25, 0);
				Npc hyperion = getNpc(231073);
				for (Player p : instance.getPlayersInside()) {
					p.getController().onAttack(hyperion, p.getLifeStats().getMaxHp() + 1, true);
				}
				cancelResonatorTask();
				for (FastMap.Entry<Integer, Future<?>> e = skillTask.head(), end = skillTask.tail(); (e = e.getNext()) != end;) {
					if (e.getValue() != null && !e.getValue().isCancelled()) {
						e.getValue().cancel(true);
					}
					despawnNpc(e.getKey());

				}
				skillTask.clear();
				spawn(730842, 129.46301f, 137.99736f, 112.17429f, (byte) 54);
				despawnNpc(231073);
			}
		}, 5000);
	}

	private void spawnResonators() {
		resonator = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				startSkillTask((Npc) spawn(231092, 108.55013f, 138.96948f, 132.60164f, (byte) 0), 21258, 1401791);
				resonator = ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						startSkillTask((Npc) spawn(231093, 126.5471f, 154.47961f, 131.47116f, (byte) 90), 21382, 1401792);
						resonator = ThreadPoolManager.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								startSkillTask((Npc) spawn(231094, 146.72455f, 139.12267f, 132.68515f, (byte) 60), 21384, 1401793);
								resonator = ThreadPoolManager.getInstance().schedule(new Runnable() {
									@Override
									public void run() {
										startSkillTask((Npc) spawn(231095, 129.41306f, 121.34766f, 131.47116f, (byte) 30), 21416, 1401794);
									}
								}, 40 * 1000);
							}
						}, 40 * 1000);
					}
				}, 40 * 1000);
			}
		}, 40 * 1000);
	}

	private void resonatorSpawn(final int npcId, final float x, final float y, final float z, final float h) {
		cancelskillTask(npcId);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				spawn(npcId, x, y, z, (byte) h);
			}
		}, 40 * 1000);
	}

	private boolean isDeadGenerator1() {
		Npc Generator1 = getNpc(231087);
		Npc Generator2 = getNpc(231088);
		Npc Generator3 = getNpc(231089);
		if (isDead(Generator1) && isDead(Generator2) && isDead(Generator3)) {
			Npc idegenerator1 = getNpc(231074);
			if (idegenerator1 != null) {
				idegenerator1.getEffectController().removeEffect(21371);
			}
			return true;
		}
		return false;
	}

	private boolean isDeadGenerator2() {
		Npc Generator4 = getNpc(231075);
		Npc Generator5 = getNpc(231076);
		Npc Generator6 = getNpc(231077);
		if (isDead(Generator4) && isDead(Generator5) && isDead(Generator6)) {
			Npc idegenerator2 = getNpc(231078);
			if (idegenerator2 != null) {
				idegenerator2.getEffectController().removeEffect(21371);
			}
			return true;
		}
		return false;
	}

	private boolean isDeadGenerator3() {
		Npc Generator7 = getNpc(231083);
		Npc Generator8 = getNpc(231084);
		Npc Generator9 = getNpc(231085);
		if (isDead(Generator7) && isDead(Generator8) && isDead(Generator9)) {
			Npc idegenerator3 = getNpc(231082);
			if (idegenerator3 != null) {
				idegenerator3.getEffectController().removeEffect(21371);
			}
			return true;
		}
		return false;
	}

	private boolean isDeadGenerator4() {
		Npc Generator10 = getNpc(231079);
		Npc Generator11 = getNpc(231080);
		Npc Generator12 = getNpc(231081);
		if (isDead(Generator10) && isDead(Generator11) && isDead(Generator12)) {
			Npc idegenerator4 = getNpc(231086);
			if (idegenerator4 != null) {
				idegenerator4.getEffectController().removeEffect(21371);
			}
			return true;
		}
		return false;
	}

	private void removeProtection() {
		if (protection != 4) {
			return;
		}
		Npc hyperion = instance.getNpc(231073);
		if (hyperion != null) {
			sendMsg(1401796);
			despawnNpc(284437);
			hyperion.getEffectController().removeEffect(21254);
			getRandomTarget(hyperion);
			spawnResonators();
			NpcShoutsService.getInstance().sendMsg(instance, 1401790, 0, false, 25, 0);
		}
	}

	private void getRandomTarget(Npc hyperion) {
		List<Player> players = new ArrayList<Player>();
		for (Player player : instance.getPlayersInside()) {
			if (!PlayerActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, hyperion, 16)) {
				players.add(player);
			}
		}
		if (players.isEmpty()) {
			return;
		}
		SkillEngine.getInstance().getSkill(hyperion, 21241, 1, players.get(Rnd.get(0, players.size() - 1))).useNoAnimationSkill();
	}

	private void despawnNpc(int npcId) {
		Npc npc = getNpc(npcId);
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}

    @Override
    public boolean onDie(final Player player, Creature lastAttacker) {
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
