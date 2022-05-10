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
package quest.eltnen;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Rhys2002
 * 
 */
public class _1040ScoutingtheScouts extends QuestHandler
{
	private final static int	questId	= 1040;
	
	public _1040ScoutingtheScouts()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(212010).addOnKillEvent(questId);
		qe.setNpcQuestData(204046).addOnKillEvent(questId);
		qe.setNpcQuestData(203989).addOnTalkEvent(questId);
		qe.setNpcQuestData(203901).addOnTalkEvent(questId);
		qe.setNpcQuestData(204020).addOnTalkEvent(questId);
		qe.setNpcQuestData(204024).addOnTalkEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 212010, 1, 4))
			return true;
		if(defaultQuestOnKillEvent(env, 204046, 8, 9))
		{
			defaultQuestMovie(env, 36);
			return true;
		}
		else
			return false;
	}	
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203989)
					return defaultQuestEndDialog(env);
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 203989)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
					else if(var == 4)
						return sendQuestDialog(env, 1352);					
					return false;
					
				case 1013:
					if(var == 0)
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 183));
					return false;	
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10001:
					if(var == 4)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;					
			}
		}
		else if(targetId == 203901)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 5)
						return sendQuestDialog(env, 1693);
					return false;

				case 10002:
					if(var == 5)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
			}
		}
		else if(targetId == 204020)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 6)
						return sendQuestDialog(env, 2034);
					else if(var == 10)
						return sendQuestDialog(env, 3057);						
					return false;

				case 10003:
					if(var == 6)
					{
						TeleportService.teleportTo(player, 210020000, 2211, 811, 513, 0);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return true;

					}
				case 10006:
					if(var == 10)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}					
					return false;
			}
		}
		else if(targetId == 204024)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 7)
						return sendQuestDialog(env, 2375);
					else if(var == 9)
						return sendQuestDialog(env, 2716);						
					return false;

				case 10004:
					if(var == 7)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10005:
					if(var == 9)
					{
						TeleportService.teleportTo(player, 210020000, 1606, 1529, 318, 0);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return true;
					}					
					return false;
			}
		}

		return false;
	}
}
