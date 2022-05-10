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
package quest.ishalgen;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE; 
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.skill.SkillEngine;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.WorldMapInstance;


/**
 * @author Mr. Poke, modified by Hellboy
 *
 */
public class _2002WheresRae extends QuestHandler
{
	private final static int	questId	= 2002;
	private final static int[]	npc_ids	= {203519, 203534, 203553, 700045, 203516, 205020, 203538};
	private final static int[]	mobs = {210377, 210378};

	public _2002WheresRae()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for(int mob : mobs)
			qe.setNpcQuestData(mob).addOnKillEvent(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203519:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 203534:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 1353:
							return defaultQuestMovie(env, 52);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
					}
					break;
				case 790002:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1693);
							else if(var == 10)
								return sendQuestDialog(env, 2034);
							else if(var == 11)
								return sendQuestDialog(env, 2375);
							else if(var == 12)
								return sendQuestDialog(env, 2462);
							else if(var == 13)
								return sendQuestDialog(env, 2716);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
						case 10003:
							return defaultCloseDialog(env, 10, 11);
						case 10005:
							return defaultCloseDialog(env, 13, 14);
						case 10004:
							if(defaultCloseDialog(env, 12, 99))
							{
								WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(320010000);
								InstanceService.registerPlayerWithInstance(newInstance, player);
								TeleportService.teleportTo(player, 320010000, newInstance.getInstanceId(), 457.65f, 426.8f, 230.4f, (byte) 75);
								return true;
							}
						case 34:
							return defaultQuestItemCheck(env, 11, 12, false, 2461, 2376);
					}
					break;
				case 205020:
					switch(env.getDialogId())
					{
						case 26:
							PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 3001, 0));
							ThreadPoolManager.getInstance().schedule(new Runnable(){
								@Override
								public void run()
								{
									qs.setQuestVar(13);
									updateQuestStatus(env);
									TeleportService.teleportTo(player, 220010000, 1, 940.15f, 2295.64f, 265.7f, (byte) 43);
								}
							}, 38000);
							return true;
						default:
							return false;
					}
				case 700045:
					if(env.getDialogId() == -1)
						return defaultQuestUseNpc(env, 11, 12, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
					break;
				case 203538:
					if(var == 14 && env.getDialogId() == -1)
					{
						qs.setQuestVar(15);
						updateQuestStatus(env);
						Npc npc = (Npc)env.getVisibleObject();
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 203553, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), true);
						npc.getController().onDie(null);
						npc.getController().onDespawn(true);
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 256));
						return true;
					}
					break;
				case 203553:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 15)
								return sendQuestDialog(env, 3057);
						case 10006:
							if(defaultCloseDialog(env, 15, 0, true, false))
							{
								env.getVisibleObject().getController().delete();
								return true;
							}
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 203516, 0);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, mobs, 3, 10))
			return true;
		else
			return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		SkillEngine.getInstance().getSkill(player, 8343, 1, player).useSkill();
	}
}
