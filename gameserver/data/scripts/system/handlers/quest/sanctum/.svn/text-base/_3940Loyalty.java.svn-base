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
package quest.sanctum;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;

 
/**
 * @author Hellboy
 *
 */
public class _3940Loyalty extends QuestHandler
{
	private final static int	questId	= 3940;
	private final static int[]	npcs = {203701, 203752};
	private final static int[]	mobs = {251002, 251021, 251018, 251039, 251033, 251036};

	public _3940Loyalty()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(203701).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
		for(int mob: mobs)
			qe.setNpcQuestData(mob).addOnKillEvent(questId);
		qe.setNpcQuestData(214823).addOnKillEvent(questId);
		qe.setNpcQuestData(216850).addOnKillEvent(questId);
		qe.setNpcQuestData(216886).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 203701, 4762))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVars().getQuestVars();
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203701:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 306)
								return sendQuestDialog(env, 1693);
							else if(var == 4)
								return sendQuestDialog(env, 2375);
						case 10002:
							if(var ==  306)
							{
								qs.setQuestVar(3);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
									return true;
							}
							else
								return false;
						case 10004:
							return defaultCloseDialog(env, 4, 5);
						case 34:
							return defaultQuestItemCheck(env, 0, 6, false, 10000, 10001);
					}
				}	break;
				case 203752:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 5)
								return sendQuestDialog(env, 2716);
						case 2717:
							if(player.getCommonData().getDp() != 4000)
								return sendQuestDialog(env, 2802);
						case 2718:
							if(player.getInventory().getItemCountByItemId(186000083) == 0)
								return sendQuestDialog(env, 2887);
						case 10255:
							if(player.getInventory().getItemCountByItemId(186000083) >= 1)
								return defaultCloseDialog(env, 5, 0, true, false, 0, 0, 186000083, 1);
							else
								return sendQuestDialog(env, 2887);
					}
				} break;
			}
		}
		return defaultQuestRewardDialog(env, 203701, 10002);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		int var = qs.getQuestVars().getQuestVars();

		if(qs.getStatus() != QuestStatus.START)
			return false;
		for(int id : mobs)
		{
			if(env.getTargetId() == id)
			{
				if(var >= 6 && var < 306)
				{
					qs.setQuestVar(var + 1);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		switch(env.getTargetId())
		{
			case 214823:
				return defaultQuestOnKillEvent(env, 214823, 3, 4);
			case 216850:
				return defaultQuestOnKillEvent(env, 216850, 3, 4);
			case 216886:
				return defaultQuestOnKillEvent(env, 216886, 3, 4);
		}
		return false;
	}
}
