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
 
public class _2841CleansingtheAsteriaChamber extends QuestHandler
{
	private final static int	questId	= 2841;
	public static WorldMapInstance newInstance;

	public _2841CleansingtheAsteriaChamber()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(271068).addOnQuestStart(questId);
		qe.setNpcQuestData(271068).addOnTalkEvent(questId);
		qe.setNpcQuestData(214762).addOnKillEvent(questId);
		qe.setNpcQuestData(214755).addOnKillEvent(questId);
		qe.setNpcQuestData(214752).addOnKillEvent(questId);
		qe.setNpcQuestData(214754).addOnKillEvent(questId);
		qe.setNpcQuestData(215441).addOnKillEvent(questId);
		qe.setNpcQuestData(214758).addOnKillEvent(questId);
		qe.setNpcQuestData(214766).addOnKillEvent(questId);
		qe.setNpcQuestData(214753).addOnKillEvent(questId);
		qe.setNpcQuestData(215444).addOnKillEvent(questId);
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
			if(targetId == 271068)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
				newInstance = InstanceService.getNextAvailableInstance(300050000);
				InstanceService.registerPlayerWithInstance(newInstance, player);
				TeleportService.teleportTo(player, 300050000, newInstance.getInstanceId(), 465, 566, 201, 0);
				return defaultQuestStartDialog(env);
				}
				else 
					 return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 271068)
			{
				TeleportService.teleportTo(player, 300050000, newInstance.getInstanceId(), 465, 566, 201, 0);
				return true;
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD && targetId == 271068)
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
			if(player.getCommonData().getPosition().getMapId() == 300050000)
			{
				if(qs.getQuestVarById(0) < 43)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return true;
				}
				else if(qs.getQuestVarById(0) == 43 || qs.getQuestVarById(0) > 43)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					TeleportService.teleportToNpc(player, 271068);
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
			if(player.getCommonData().getPosition().getMapId() != 300050000)
			{
				if(newInstance != null && InstanceService.isInstanceExist(300050000, newInstance.getInstanceId()))
				{
					TeleportService.teleportTo(player, 300050000, newInstance.getInstanceId(), 465, 566, 201, 0);
					return true;
				}
				else
				{
					newInstance = InstanceService.getNextAvailableInstance(300050000);
					InstanceService.registerPlayerWithInstance(newInstance, player);
					TeleportService.teleportTo(player, 300050000, newInstance.getInstanceId(), 465, 566, 201, 0);
					return true;
				}
			}
		}
		return false;
	}
}
