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
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Orpheo
 */
 
public class _3099CheorunerksCareOfHisWife extends QuestHandler
{
	private final static int	questId	= 3099;
	
	public _3099CheorunerksCareOfHisWife()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798169).addOnQuestStart(questId);
		qe.setNpcQuestData(798169).addOnTalkEvent(questId);
		qe.setNpcQuestData(203792).addOnTalkEvent(questId);
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
			if(targetId == 798169)
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
				case 203792:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182208071);
							long itemCount2 = player.getInventory().getItemCountByItemId(152020086);
							long itemCount3 = player.getInventory().getItemCountByItemId(152000908);
							long itemCount4 = player.getInventory().getItemCountByItemId(152011053);
							if(itemCount1 >= 12 && itemCount2 >= 1 && itemCount3 >= 3 && itemCount4 >= 1)
							{
								return sendQuestDialog(env, 5);
							}
						}
						case 17:
						{
							qs.setStatus(QuestStatus.COMPLETE);
							qs.setCompliteCount(1);
							player.getInventory().removeFromBagByItemId(182208071, 1);
							player.getInventory().removeFromBagByItemId(152020086, 1);
							player.getInventory().removeFromBagByItemId(152000908, 1);
							player.getInventory().removeFromBagByItemId(152011053, 1);
							int rewardExp = player.getRates().getQuestXpRate() * 889900;
							player.getCommonData().addExp(rewardExp);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(5, questId, QuestStatus.COMPLETE, 2));
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							updateQuestStatus(env);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}