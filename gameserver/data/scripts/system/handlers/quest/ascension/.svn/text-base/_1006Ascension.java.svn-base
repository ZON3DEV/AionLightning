/**
 * This file is part of Aion-Core Extreme <www.aion-core.net>
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

package quest.ascension;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.SystemMessageId;
import org.openaion.gameserver.network.aion.serverpackets.*;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.*;
import org.openaion.gameserver.skill.SkillEngine;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.WorldMapInstance;
import org.openaion.gameserver.world.zone.ZoneName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MrPoke / Orpheo
 *
 */
public class _1006Ascension extends QuestHandler {
    private final static int questId = 1006;

    public _1006Ascension() {
        super(questId);
    }

    @Override
    public void register() {
        if (CustomConfig.ENABLE_SIMPLE_2NDCLASS)
            return;
        qe.addQuestLvlUp(questId);
        qe.setNpcQuestData(790001).addOnTalkEvent(questId);
        qe.setQuestItemIds(182200007).add(questId);
        qe.setNpcQuestData(730008).addOnTalkEvent(questId);
        qe.setNpcQuestData(205000).addOnTalkEvent(questId);
        qe.setNpcQuestData(211042).addOnKillEvent(questId);
        qe.setNpcQuestData(211043).addOnAttackEvent(questId);
        qe.setQuestMovieEndIds(151).add(questId);
        qe.addOnEnterWorld(questId);
        qe.addOnDie(questId);
        qe.addOnQuestFinish(questId);
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        if (defaultQuestOnKillEvent(env, 211042, 51, 54))
        {
            return true;
        }
        if (defaultQuestOnKillEvent(env, 211042, 54, 55)) {
            Player player = env.getPlayer();
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            qs.setQuestVar(4);
            updateQuestStatus(env);
            Npc mob = (Npc) QuestService.addNewSpawn(310010000, player.getInstanceId(), 211043, (float) 223.35f, (float) 252.35f, (float) 205.75f, (byte) 0, true);
            // TODO: Tempt decrease P attack.
            mob.getGameStats().setStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK, mob.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK) / 3);
            mob.getAggroList().addDamage(player, 1000);
            return true;
        } else
            return false;
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVars().getQuestVars();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 790001) {
                switch (env.getDialogId()) {
                    case 26:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                        else if (var == 3)
                            return sendQuestDialog(env, 1693);
                        else if (var == 5)
                            return sendQuestDialog(env, 2034);
                    case 10000:
                        if (var == 0) {
                            if (player.getInventory().getItemCountByItemId(182200007) == 0)
                                if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182200007, 1))))
                                    return true;
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                    case 10002:
                        if (var == 3) {
                            player.getInventory().removeFromBagByItemId(182200009, 1);
                            qs.setQuestVar(99);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                            WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(310010000);
                            InstanceService.registerPlayerWithInstance(newInstance, player);
                            TeleportService.teleportTo(player, 310010000, newInstance.getInstanceId(), 52, 174, 229, 0);
                            return true;
                        }
                    case 10003:
                        if (var == 5) {
                            PlayerClass playerClass = player.getCommonData().getPlayerClass();
                            if (playerClass == PlayerClass.WARRIOR)
                                return sendQuestDialog(env, 2375);
                            else if (playerClass == PlayerClass.SCOUT)
                                return sendQuestDialog(env, 2716);
                            else if (playerClass == PlayerClass.MAGE)
                                return sendQuestDialog(env, 3057);
                            else if (playerClass == PlayerClass.PRIEST)
                                return sendQuestDialog(env, 3398);
                        }
                    case 10004:
                        if (var == 5)
                            return setPlayerClass(env, qs, PlayerClass.GLADIATOR);
                    case 10005:
                        if (var == 5)
                            return setPlayerClass(env, qs, PlayerClass.TEMPLAR);
                    case 10006:
                        if (var == 5)
                            return setPlayerClass(env, qs, PlayerClass.ASSASSIN);
                    case 10007:
                        if (var == 5)
                            return setPlayerClass(env, qs, PlayerClass.RANGER);
                    case 10008:
                        if (var == 5)
                            return setPlayerClass(env, qs, PlayerClass.SORCERER);
                    case 10009:
                        if (var == 5)
                            return setPlayerClass(env, qs, PlayerClass.SPIRIT_MASTER);
                    case 10010:
                        if (var == 5)
                            return setPlayerClass(env, qs, PlayerClass.CLERIC);
                    case 10011:
                        if (var == 5)
                            return setPlayerClass(env, qs, PlayerClass.CHANTER);
                }
            } else if (targetId == 730008) {
                switch (env.getDialogId()) {
                    case 26:
                        if (var == 2) {
                            if (player.getInventory().getItemCountByItemId(182200008) != 0)
                                return sendQuestDialog(env, 1352);
                            else
                                return sendQuestDialog(env, 1354);
                        }
                    case 1353:
                        if (var == 2) {
                            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 14));
                            player.getInventory().removeFromBagByItemId(182200008, 1);
                            ItemService.addItems(player, Collections.singletonList(new QuestItems(182200009, 1)));
                        }
                        return false;
                    case 10001:
                        if (var == 2) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
            } else if (targetId == 205000) {
                switch (env.getDialogId()) {
                    case 26:
                        if (var == 99) {
                            PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
                            Skill skill = SkillEngine.getInstance().getSkill(player,1910,1,player);
                            skill.useSkill();
                            qs.setQuestVar(50);
                            updateQuestStatus(env);
                            ThreadPoolManager.getInstance().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    qs.setQuestVar(51);
                                    updateQuestStatus(env);
                                    List<Npc> mobs = new ArrayList<Npc>();
                                    mobs.add((Npc) QuestService.addNewSpawn(310010000, player.getInstanceId(), 211042, (float) 224.073, (float) 239.1, (float) 206.7, (byte) 0, true));
                                    mobs.add((Npc) QuestService.addNewSpawn(310010000, player.getInstanceId(), 211042, (float) 233.5, (float) 241.04, (float) 206.365, (byte) 0, true));
                                    mobs.add((Npc) QuestService.addNewSpawn(310010000, player.getInstanceId(), 211042, (float) 229.6, (float) 265.7, (float) 205.7, (byte) 0, true));
                                    mobs.add((Npc) QuestService.addNewSpawn(310010000, player.getInstanceId(), 211042, (float) 222.8, (float) 262.5, (float) 205.7, (byte) 0, true));
                                    for (Npc mob : mobs) {
                                        // TODO: Tempt decrease P attack.
                                        mob.getGameStats().setStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK, mob.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK) / 3);
                                        mob.getGameStats().setStat(StatEnum.PHYSICAL_DEFENSE, 0);
                                        mob.getAggroList().addDamage(player, 1000);
                                    }
                                }
                            }, 43000);
                            return true;
                        }
                        return false;
                    default:
                        return false;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 790001) {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null)
            return false;
        boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
        if (!lvlCheck)
            return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
    }

    @Override
    public boolean onAttackEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 4)
            return false;
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (targetId != 211043)
            return false;
        Npc npc = (Npc) env.getVisibleObject();
        if (npc.getLifeStats().getCurrentHp() < npc.getLifeStats().getMaxHp() / 3) {
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 151));
            npc.getController().onDelete();
        }
        return false;
    }

    @Override
    public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if(id != 182200007 || qs == null)
			return HandlerResult.UNKNOWN;
		
		if(!ZoneService.getInstance().isInsideZone(player, ZoneName.ITEMUSE_Q1006))
			return HandlerResult.FAILED;
			
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                player.getInventory().removeFromBagByObjectId(itemObjId, 1);
                ItemService.addItems(player, Collections.singletonList(new QuestItems(182200008, 1)));
                qs.setQuestVarById(0, 2);
                updateQuestStatus(env);
            }
        }, 3000);
        return HandlerResult.SUCCESS;
    }

    @Override
    public boolean onMovieEndEvent(final QuestCookie env, int movieId) {
        if (movieId != 151)
            return false;
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 4)
            return false;
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                QuestService.addNewSpawn(310010000, player.getInstanceId(), 790001, (float) 220.6, (float) 247.8, (float) 206.0, (byte) 0, true);
                qs.setQuestVar(5);
                updateQuestStatus(env);
            }
        }, 3000);
        return true;
    }

    private boolean setPlayerClass(QuestCookie env, QuestState qs, PlayerClass playerClass) {
        Player player = env.getPlayer();
        player.getCommonData().setPlayerClass(playerClass);
        player.getCommonData().upgradePlayer();
        qs.setStatus(QuestStatus.REWARD);
        updateQuestStatus(env);
        SkillLearnService.addMissingSkills(player);
        sendQuestDialog(env, 5);
        return true;
    }

    @Override
    public boolean onDieEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;
        int var = qs.getQuestVars().getQuestVars();
        if (var == 5 || (var >= 51 && var <= 54)) {
            qs.setQuestVar(3);
            updateQuestStatus(env);
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
        }

        return false;
    }

    @Override
    public boolean onEnterWorldEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVars().getQuestVars();
            if (var == 5 || (var >= 51 && var <= 56) || var == 99) {
                if (player.getWorldId() != 310010000) {
                    qs.setQuestVar(3);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
                } else {
                    PacketSendUtility.sendPacket(player, new SM_ASCENSION_MORPH(1));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onQuestFinishEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            TeleportService.teleportTo(player, 210010000, 1, 242, 1638, 100, (byte) 20, 0);
            return true;
        }
        return false;
    }
}
