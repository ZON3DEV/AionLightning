/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;

import java.util.Collections;

/**
 * @author Orpheo
 *
 */

public class _1562CrossedDestiny extends QuestHandler
{
	private final static int	questId	= 1562;
	
	public _1562CrossedDestiny()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setQuestItemIds(182201780).add(questId);
		qe.setNpcQuestData(204589).addOnQuestStart(questId);
		qe.setNpcQuestData(204589).addOnTalkEvent(questId);
		qe.setNpcQuestData(204616).addOnTalkEvent(questId);		
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(env.getTargetId() == 204589)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else if (env.getDialogId() == 1002) {
                    if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182201780, 1))))
                        return defaultQuestStartDialog(env);
                    else
                        return true;
                } else
                    return defaultQuestStartDialog(env);
            }
		}
		
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(env.getTargetId() == 204589)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 10002);
				else
					return defaultQuestEndDialog(env);
			}
			return false;
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204616:
					switch(env.getDialogId())
					{
						case -1:
							if(var == 2)
							{
								Npc npc = (Npc) env.getVisibleObject();
								if(MathUtil.getDistance(1127, 1704, 109, npc.getX(), npc.getY(), npc.getZ()) > 5)
								{
									if(!npc.getMoveController().isScheduled())
										npc.getMoveController().schedule();
									npc.getMoveController().followTarget(4);
									return true;
								}
								else
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
									.getObjectId(), 0));
								npc.getController().onDespawn(true);
								return true;
							}
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10001:
							player.getInventory().removeFromBagByItemId(182201780, 1);
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							Npc npc = (Npc) env.getVisibleObject();
							npc.getMoveController().followTarget(4);
							npc.getMoveController().schedule();
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 0));
							return true;
					}
			}
		}
		return false;
	}
	
}