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
package quest.inggison;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.services.TeleportService;


/**
 * @author Dns
 * 
 */
public class _10001BoundforInggison extends QuestHandler
{
	private final static int	questId	= 10001;
	private final static int[]	npc_ids	= {798600, 798513, 203760, 203782, 798408, 203709, 798926};

	public _10001BoundforInggison()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 798600:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
				case 798513:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
						/**case 10255:
							if(defaultCloseDialog(env, 5, 0, true, false))
							{
								TeleportService.teleportTo(player, 210050000, 1, 1867.56f, 2748.76f, 531.971f, (byte) 77, 0);
								defaultQuestMovie(env, 501);
								return true;
							}*/
					}
				case 203760:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1694);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
					}
				case 203782:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10003:
							return defaultCloseDialog(env, 3, 4);
					}
				case 798408:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 2375);
							else if(var == 6)
								return sendQuestDialog(env, 3057);
							else if(var == 7)
								return sendQuestDialog(env, 3398);
						case 10004:
							return defaultCloseDialog(env, 4, 5);
						case 10006:
							return defaultCloseDialog(env, 6, 7);
						case 10255:
							if(defaultCloseDialog(env, 7, 0, true, false))
							{
								TeleportService.teleportTo(player, 210050000, 1, 1440.11f, 407.94f, 552.266f, (byte) 77, 0);
								defaultQuestMovie(env, 501);
								return true;
							}
					}
				case 203709:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 5)
								return sendQuestDialog(env, 2716);
						case 10005:
							return defaultCloseDialog(env, 5, 6);
					}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(env.getTargetId() == 798926)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 18)
				{
					int[] ids = {10020, 10021, 10022, 10023, 10024, 10025, 10026};
					for(int id: ids)
						QuestService.startQuest(new QuestCookie(env.getVisibleObject(), env.getPlayer(), id, env.getDialogId()), QuestStatus.LOCKED);
				}
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}	
}
