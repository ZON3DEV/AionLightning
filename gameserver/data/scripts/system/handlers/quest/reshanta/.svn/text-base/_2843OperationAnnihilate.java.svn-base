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
 
public class _2843OperationAnnihilate extends QuestHandler
{
	private final static int	questId	= 2843;
	public static WorldMapInstance newInstance;

	public _2843OperationAnnihilate()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(268081).addOnQuestStart(questId);
		qe.setNpcQuestData(268081).addOnTalkEvent(questId);
		qe.setNpcQuestData(215134).addOnKillEvent(questId);
		qe.setNpcQuestData(215136).addOnKillEvent(questId);
		qe.setNpcQuestData(215122).addOnKillEvent(questId);
		qe.setNpcQuestData(215124).addOnKillEvent(questId);
		qe.setNpcQuestData(215116).addOnKillEvent(questId);
		qe.setNpcQuestData(215315).addOnKillEvent(questId);
		qe.setNpcQuestData(215299).addOnKillEvent(questId);
		qe.setNpcQuestData(215112).addOnKillEvent(questId);
		qe.setNpcQuestData(215120).addOnKillEvent(questId);
		qe.setNpcQuestData(215125).addOnKillEvent(questId);
		qe.setNpcQuestData(215118).addOnKillEvent(questId);
		qe.setNpcQuestData(215119).addOnKillEvent(questId);
		qe.setNpcQuestData(215312).addOnKillEvent(questId);
		qe.setNpcQuestData(215109).addOnKillEvent(questId);
		qe.setNpcQuestData(215097).addOnKillEvent(questId);
		qe.setNpcQuestData(215304).addOnKillEvent(questId);
		qe.setNpcQuestData(215308).addOnKillEvent(questId);
		qe.setNpcQuestData(215292).addOnKillEvent(questId);
		qe.setNpcQuestData(215101).addOnKillEvent(questId);
		qe.setNpcQuestData(215126).addOnKillEvent(questId);
		qe.setNpcQuestData(215098).addOnKillEvent(questId);
		qe.setNpcQuestData(215305).addOnKillEvent(questId);
		qe.setNpcQuestData(215289).addOnKillEvent(questId);
		qe.setNpcQuestData(215300).addOnKillEvent(questId);
		qe.setNpcQuestData(215113).addOnKillEvent(questId);
		qe.setNpcQuestData(215316).addOnKillEvent(questId);
		qe.setNpcQuestData(215094).addOnKillEvent(questId);
		qe.setNpcQuestData(215301).addOnKillEvent(questId);
		qe.setNpcQuestData(215313).addOnKillEvent(questId);
		qe.setNpcQuestData(215110).addOnKillEvent(questId);
		qe.setNpcQuestData(215297).addOnKillEvent(questId);
		qe.setNpcQuestData(215314).addOnKillEvent(questId);
		qe.setNpcQuestData(215111).addOnKillEvent(questId);
		qe.setNpcQuestData(215298).addOnKillEvent(questId);
		qe.setNpcQuestData(215104).addOnKillEvent(questId);
		qe.setNpcQuestData(215096).addOnKillEvent(questId);
		qe.setNpcQuestData(215303).addOnKillEvent(questId);
		qe.setNpcQuestData(215287).addOnKillEvent(questId);
		qe.setNpcQuestData(215128).addOnKillEvent(questId);
		qe.setNpcQuestData(215121).addOnKillEvent(questId);
		qe.setNpcQuestData(215106).addOnKillEvent(questId);
		qe.setNpcQuestData(215293).addOnKillEvent(questId);
		qe.setNpcQuestData(215107).addOnKillEvent(questId);
		qe.setNpcQuestData(215100).addOnKillEvent(questId);
		qe.setNpcQuestData(215291).addOnKillEvent(questId);
		qe.setNpcQuestData(215307).addOnKillEvent(questId);
		qe.setNpcQuestData(215290).addOnKillEvent(questId);
		qe.setNpcQuestData(215099).addOnKillEvent(questId);
		qe.setNpcQuestData(215306).addOnKillEvent(questId);
		qe.setNpcQuestData(215123).addOnKillEvent(questId);
		qe.setNpcQuestData(215108).addOnKillEvent(questId);
		qe.setNpcQuestData(215295).addOnKillEvent(questId);
		qe.setNpcQuestData(215311).addOnKillEvent(questId);
		qe.setNpcQuestData(215095).addOnKillEvent(questId);
		qe.setNpcQuestData(215302).addOnKillEvent(questId);
		qe.setNpcQuestData(215133).addOnKillEvent(questId);
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
			if(targetId == 268081)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
				newInstance = InstanceService.getNextAvailableInstance(300140000);
				InstanceService.registerPlayerWithInstance(newInstance, player);
				TeleportService.teleportTo(player, 300140000, newInstance.getInstanceId(), 527, 137, 179, 30);
				return defaultQuestStartDialog(env);
				}
				else 
					 return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 268081)
			{
				TeleportService.teleportTo(player, 300140000, newInstance.getInstanceId(), 527, 137, 179, 30);
				return true;
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD && targetId == 268081)
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
			if(player.getCommonData().getPosition().getMapId() == 300140000)
			{
				if(qs.getQuestVarById(0) < 79)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return true;
				}
				else if(qs.getQuestVarById(0) == 79 || qs.getQuestVarById(0) > 79)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					TeleportService.teleportToNpc(player, 268081);
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
			if(player.getCommonData().getPosition().getMapId() != 300140000)
			{
				if(newInstance != null && InstanceService.isInstanceExist(300140000, newInstance.getInstanceId()))
				{
					TeleportService.teleportTo(player, 300140000, newInstance.getInstanceId(), 527, 137, 179, 30);
					return true;
				}
				else
				{
					newInstance = InstanceService.getNextAvailableInstance(300140000);
					InstanceService.registerPlayerWithInstance(newInstance, player);
					TeleportService.teleportTo(player, 300140000, newInstance.getInstanceId(), 527, 137, 179, 30);
					return true;
				}
			}
		}
		return false;
	}
}
