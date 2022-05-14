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

package quest.danaria;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author pralinka
 */
public class _10090IdgelIfYouAskMe extends QuestHandler {

	private final static int questId = 10090;

	public _10090IdgelIfYouAskMe() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcIds = { 800566, 800816, 730734, 800817, 730735, 800818, 800820 };
		qe.registerOnLevelUp(questId);
		for (int npcId : npcIds) {
			qe.registerQuestNpc(npcId).addOnTalkEvent(questId);
		}
		qe.registerOnLogOut(questId);
		qe.registerOnDie(questId);
		qe.registerOnQuestTimerEnd(questId);
		qe.registerQuestNpc(701545).addOnKillEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("QUEST_10090_301000000"), questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10085);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) { // rusted door
		return defaultOnKillEvent(env, 701545, 4, 5, 0);
	}

	@Override
	public boolean onQuestTimerEndEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 7) {
				QuestService.addNewSpawn(301000000, player.getInstanceId(), 800818, 564f, 426f, 95f, (byte) 119);
				changeQuestStep(env, 7, 8, false);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName != ZoneName.get("QUEST_10090_301000000")) {
			return false;
		}
		final Player player = env.getPlayer();
		if (player == null) {
			return false;
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		if (qs.getQuestVarById(0) == 5) {
			changeQuestStep(env, 5, 6, false);
			return true;
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 800566: { // gheanne
					switch (dialog) {
						case QUEST_SELECT: {
							if (qs.getQuestVarById(0) == 0) {
								return sendQuestDialog(env, 1011);
							}
						}
						case SETPRO1: {
							return defaultCloseDialog(env, 0, 1);
						}
						default:
							break;
					}
					break;
				}
				case 800816: { // platonos
					switch (dialog) {
						case QUEST_SELECT: {
							if (qs.getQuestVarById(0) == 1) {
								return sendQuestDialog(env, 1352);
							}
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 1, 2);
						}
						default:
							break;
					}
					break;
				}
				case 730734: { // silver cube
					switch (dialog) {
						case USE_OBJECT: {
							if (qs.getQuestVarById(0) == 2) {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO3: {
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(301000000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService2.teleportTo(player, 301000000, newInstance.getInstanceId(), 209, 506, 154);
							QuestService.addNewSpawn(301000000, player.getInstanceId(), 800817, 210f, 507f, 154f, (byte) 119);
							playQuestMovie(env, 851);
							changeQuestStep(env, 2, 3, false);
							return true;
						}
						default:
							break;
					}
					break;
				}
				case 800817: { // kaza1
					switch (dialog) {
						case QUEST_SELECT: {
							if (qs.getQuestVarById(0) == 3) {
								return sendQuestDialog(env, 2034);
							}
						}
						case SETPRO4: {
							return defaultCloseDialog(env, 3, 4);
						}
						default:
							break;
					}
					break;
				}
				case 730735: { // danuar shield
					switch (dialog) {
						case USE_OBJECT: {
							if (var == 6) {
								QuestService.questTimerStart(env, 120); // 2 min
								changeQuestStep(env, 6, 7, false);
								env.getVisibleObject();
								QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 231067, player.getX() + 7, player.getY() - 7,
										player.getZ(), (byte) 100);
								QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 231067, player.getX() - 7, player.getY() + 7,
										player.getZ(), (byte) 100);
								QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 231069, player.getX() + 9, player.getY() - 9,
										player.getZ(), (byte) 100);
								QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 231069, player.getX() - 9, player.getY() + 9,
										player.getZ(), (byte) 100);
								return true;
							}
						}
						default:
							break;
					}
					break;
				}
				case 800818: { // kaza2
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 8) {
								return sendQuestDialog(env, 3398);
							}
						}
						case SET_SUCCEED: {
							TeleportService2.teleportTo(player, 600060000, 1529f, 2934f, 223f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 8, 9, true, false);
						}
						default:
							break;
					}
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 800820) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onDieEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var > 2 && var < 8) {
				changeQuestStep(env, var, 2, false);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onLogOutEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var > 2 && var < 8) {
				changeQuestStep(env, var, 2, false);
				return true;
			}
		}
		return false;
	}
}
