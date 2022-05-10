/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.reshanta;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.world.WorldMapInstance;


/**
 * @author Hilgert
 * 
 */
 
public class _2842BalaurintheUndergroundFortress extends QuestHandler
{
	private final static int	questId	= 2842;
	public static WorldMapInstance newInstance;

	public _2842BalaurintheUndergroundFortress()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(266568).addOnQuestStart(questId);
		qe.setNpcQuestData(266568).addOnTalkEvent(questId);
		qe.setNpcQuestData(215447).addOnKillEvent(questId);
		qe.setNpcQuestData(214777).addOnKillEvent(questId);
		qe.setNpcQuestData(214773).addOnKillEvent(questId);
		qe.setNpcQuestData(214781).addOnKillEvent(questId);
		qe.setNpcQuestData(214784).addOnKillEvent(questId);
		qe.setNpcQuestData(700472).addOnKillEvent(questId);
		qe.setNpcQuestData(700474).addOnKillEvent(questId);
		qe.setNpcQuestData(700473).addOnKillEvent(questId);
		qe.addOnEnterWorld(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
		int targetId = 0;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE)
		{
			if(targetId == 266568)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
				newInstance = InstanceService.getNextAvailableInstance(300070000);
				InstanceService.registerPlayerWithInstance(newInstance, player);
				TeleportService.teleportTo(player, 300070000, newInstance.getInstanceId(), 504, 395, 95, 0);
				return defaultQuestStartDialog(env);
				}
				else 
					 return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 266568)
			{
				TeleportService.teleportTo(player, 300070000, newInstance.getInstanceId(), 504, 395, 95, 0);
				return true;
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD && targetId == 266568)
		{
			qs.setQuestVarById(0, 0);
			updateQuestStatus(env);
			return defaultQuestEndDialog(env);
		}
		return false;
    }	

	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		if(qs.getStatus() == QuestStatus.START)
		{	
			if(player.getCommonData().getPosition().getMapId() == 300070000)
			{
				if(qs.getQuestVarById(0) < 38)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return true;
				}
				else if(qs.getQuestVarById(0) == 38 || qs.getQuestVarById(0) > 38)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					TeleportService.teleportToNpc(player, 266568);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null && qs.getStatus() == QuestStatus.START)
		{
			if(player.getCommonData().getPosition().getMapId() != 300070000)
			{
				if(newInstance != null && InstanceService.isInstanceExist(300070000, newInstance.getInstanceId()))
				{
					TeleportService.teleportTo(player, 300070000, newInstance.getInstanceId(), 504, 395, 95, 0);
					return true;
				}
				else
				{
					newInstance = InstanceService.getNextAvailableInstance(300070000);
					InstanceService.registerPlayerWithInstance(newInstance, player);
					TeleportService.teleportTo(player, 300070000, newInstance.getInstanceId(), 504, 395, 95, 0);
					return true;
				}
			}
		}
		return false;
	}
}
