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
package quest.ascension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.SystemMessageId;
import org.openaion.gameserver.network.aion.serverpackets.SM_ASCENSION_MORPH;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.SkillLearnService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.WorldMapInstance;


/**
 * @author MrPoke
 * 
 */
public class _2008Ascension extends QuestHandler
{
	private final static int	questId	= 2008;

	public _2008Ascension()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		if(CustomConfig.ENABLE_SIMPLE_2NDCLASS)
			return;
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(203550).addOnTalkEvent(questId);
		qe.setNpcQuestData(790003).addOnTalkEvent(questId);
		qe.setNpcQuestData(790002).addOnTalkEvent(questId);
		qe.setNpcQuestData(203546).addOnTalkEvent(questId);
		qe.setNpcQuestData(205020).addOnTalkEvent(questId);
		qe.setNpcQuestData(205040).addOnKillEvent(questId);
		qe.setNpcQuestData(205041).addOnAttackEvent(questId);
		qe.setQuestMovieEndIds(152).add(questId);
		qe.addOnEnterWorld(questId);
		qe.addOnDie(questId);
		qe.addOnQuestFinish(questId);
	}	

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 205040, 51, 54))
			return true;
		if(defaultQuestOnKillEvent(env, 205040, 54, 55))
		{
			Player player = env.getPlayer();
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			qs.setQuestVar(5);
			updateQuestStatus(env);
			Npc mob = (Npc) QuestService.addNewSpawn(320010000, player.getInstanceId(), 205041, 301f, 259f, 205.5f, (byte) 0, true);
			// TODO: Tempt decrease P attack.
			mob.getGameStats().setStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK, mob.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK) / 3);
			mob.getAggroList().addDamage(player, 1000);
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean onAttackEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 5)
			return false;
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(targetId != 205041)
			return false;
		Npc npc = (Npc) env.getVisibleObject();
		if(npc.getLifeStats().getCurrentHp() < npc.getLifeStats().getMaxHp() / 3)
		{
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 152));
			npc.getController().onDelete();
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final int instanceId = player.getInstanceId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVars().getQuestVars();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 203550)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
						else if(var == 4)
							return sendQuestDialog(env, 2375);
						else if(var == 6)
							return sendQuestDialog(env, 2716);
					case 2376:
						if(var == 4)
						{
							PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 57));
							player.getInventory().removeFromBagByItemId(182203009, 1);
							player.getInventory().removeFromBagByItemId(182203010, 1);
							player.getInventory().removeFromBagByItemId(182203011, 1);
							return false;
						}
					case 10000:
						if(var == 0)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					case 10004:
						if(var == 4)
						{
							qs.setQuestVar(99);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							// Create instance
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(320010000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService.teleportTo(player, 320010000, newInstance.getInstanceId(), 457.65f, 426.8f, 230.4f, 0);
							return true;
						}
					case 10005:
						if(var == 6)
						{
							PlayerClass playerClass = player.getCommonData().getPlayerClass();
							if(playerClass == PlayerClass.WARRIOR)
								return sendQuestDialog(env, 3057);
							else if(playerClass == PlayerClass.SCOUT)
								return sendQuestDialog(env, 3398);
							else if(playerClass == PlayerClass.MAGE)
								return sendQuestDialog(env, 3739);
							else if(playerClass == PlayerClass.PRIEST)
								return sendQuestDialog(env, 4080);
						}
					case 10006:
						if(var == 6)
							return setPlayerClass(env, qs, PlayerClass.GLADIATOR);
					case 10007:
						if(var == 6)
							return setPlayerClass(env, qs, PlayerClass.TEMPLAR);
					case 10008:
						if(var == 6)
							return setPlayerClass(env, qs, PlayerClass.ASSASSIN);
					case 10009:
						if(var == 6)
							return setPlayerClass(env, qs, PlayerClass.RANGER);
					case 10010:
						if(var == 6)
							return setPlayerClass(env, qs, PlayerClass.SORCERER);
					case 10011:
						if(var == 6)
							return setPlayerClass(env, qs, PlayerClass.SPIRIT_MASTER);
					case 10012:
						if(var == 6)
							return setPlayerClass(env, qs, PlayerClass.CHANTER);
					case 10013:
						if(var == 6)
							return setPlayerClass(env, qs, PlayerClass.CLERIC);
				}
			}
			else if(targetId == 790003)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 1)
							return sendQuestDialog(env, 1352);
					case 10001:
						if(var == 1)
						{
							if(player.getInventory().getItemCountByItemId(182203009) == 0)
								if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182203009, 1))))
									return true;
								
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
				}
			}
			else if(targetId == 790002)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 2)
							return sendQuestDialog(env, 1693);
					case 10002:
						if(var == 2)
						{
							if(player.getInventory().getItemCountByItemId(182203010) == 0)
								if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182203010, 1))))
									return true;
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
				}
			}
			else if(targetId == 203546)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 3)
							return sendQuestDialog(env, 2034);
					case 10003:
						if(var == 3)
						{
							if(player.getInventory().getItemCountByItemId(182203011) == 0)
								if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182203011, 1))))
									return true;
								
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
				}
			}
			else if(targetId == 205020)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 99)
						{
							PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 3001, 0));
							qs.setQuestVar(50);
							updateQuestStatus(env);
							ThreadPoolManager.getInstance().schedule(new Runnable(){
								@Override
								public void run()
								{
									qs.setQuestVar(51);
									updateQuestStatus(env);
									List<Npc> mobs = new ArrayList<Npc>();
									mobs.add((Npc) QuestService.addNewSpawn(320010000, instanceId, 205040, 294f, 277f, 207f, (byte) 0, true));
									mobs.add((Npc) QuestService.addNewSpawn(320010000, instanceId, 205040, 305f, 279f, 206.5f, (byte) 0, true));
									mobs.add((Npc) QuestService.addNewSpawn(320010000, instanceId, 205040, 298f, 253f, 205.7f, (byte) 0, true));
									mobs.add((Npc) QuestService.addNewSpawn(320010000, instanceId, 205040, 306f, 251f, 206f, (byte) 0, true));
									for(Npc mob : mobs)
									{
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
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203550)
			{
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, false);
	}

	@Override
	public boolean onEnterWorldEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null && qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVars().getQuestVars();
			if(var == 5 || (var >= 51 && var <= 56) || var == 99)
			{
				if(player.getWorldId() != 320010000)
				{
					qs.setQuestVar(4);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
				}
				else
				{
					PacketSendUtility.sendPacket(player, new SM_ASCENSION_MORPH(1));
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestCookie env, int movieId)
	{
		if(movieId != 152)
			return false;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 5)
			return false;
		int instanceId = player.getInstanceId();
		QuestService.addNewSpawn(320010000, instanceId, 203550, 301.92999f, 274.26001f, 205.7f, (byte) 0, true);
		qs.setQuestVar(6);
		updateQuestStatus(env);
		return true;
	}

	private boolean setPlayerClass(QuestCookie env, QuestState qs, PlayerClass playerClass)
	{
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
	public boolean onDieEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		if(qs.getStatus() != QuestStatus.START)
			return false;
		int var = qs.getQuestVars().getQuestVars();
		if(var == 5 || (var >= 51 && var <= 54))
		{
			qs.setQuestVar(4);
			updateQuestStatus(env);
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
		}
		return false;
	}
	
	@Override
	public boolean onQuestFinishEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null && qs.getStatus() == QuestStatus.REWARD)
		{
			TeleportService.teleportTo(player, 220010000, 1, 385, 1895, 327, (byte) 20, 0);
			return true;
		}
		return false;
	}
}
