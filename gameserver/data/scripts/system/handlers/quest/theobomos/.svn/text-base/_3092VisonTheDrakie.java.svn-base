/*
 *  This file is part of Aion-Core Extreme <http://www.aion-core.net>.
 *
 *  Aion-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Aion-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Aion-Core Extreme.  If not, see <http://www.gnu.org/licenses/>.
 */

package quest.theobomos;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Orpheo
 */
 
public class _3092VisonTheDrakie extends QuestHandler
{
	private final static int	questId	= 3092;

	public _3092VisonTheDrakie()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798191).addOnQuestStart(questId);
		qe.setNpcQuestData(798191).addOnTalkEvent(questId);
		qe.setNpcQuestData(798214).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 798191)
			{
				switch(env.getDialogId())
				{
					case 26:
					{
						return sendQuestDialog(env, 1011);
					}
					default: return defaultQuestStartDialog(env);
				}
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 798214:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							if(qs.getQuestVarById(0) == 0)
							{
								long itemCount = player.getInventory().getItemCountByItemId(182208066);
								if(itemCount >= 1)
								{
									Npc npc = (Npc)env.getVisibleObject();
									if (MathUtil.getDistance(402, 1219, 134, npc.getX(), npc.getY(), npc.getZ()) > 10)
									{
										if(!npc.getMoveController().isScheduled())
											npc.getMoveController().schedule();
										npc.getMoveController().followTarget(4);
										return true;
									}
									else
									{
										qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
										qs.setStatus(QuestStatus.REWARD);
										updateQuestStatus(env);
										npc.getController().onDie(null);
										npc.getController().onDespawn(false);								
										return true;
									}
								}
							}							
						}
						case 26:
						{
							if(qs.getQuestVarById(0) == 0)
							{
								long itemCount = player.getInventory().getItemCountByItemId(182208066);
								if(itemCount >= 1)
								{
									return sendQuestDialog(env, 1352);
								}
							}
						}
						case 10000:
						{
							Npc npc = (Npc)env.getVisibleObject();
							npc.getMoveController().setDistance(4);
							npc.getMoveController().setSpeed(6);
							npc.getMoveController().schedule();
							npc.getMoveController().followTarget(4);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798191)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}