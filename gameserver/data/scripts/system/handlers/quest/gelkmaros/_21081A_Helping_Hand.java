/*
 * This file is part of aion-unique <aion-unique.org>
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.gelkmaros;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;

import java.util.Collections;


public class _21081A_Helping_Hand extends QuestHandler
{
    private final static int questId = 21081;

    public _21081A_Helping_Hand()
	{
        super(questId);
    }
	
    @Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
		targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(targetId == 799225) //Richelle
		{
			if(qs == null)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182214017, 1)))) //Bordereau de caisse d'armes
						return defaultQuestStartDialog(env);
					else
						return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if(qs == null)
			return false;

		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{ 
                case 799332: //Agovard
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							return sendQuestDialog(env, 1352);
						}
						case 10000:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env); 
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
                case 799217: //Renato
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							return sendQuestDialog(env, 1693);
						}
						case 10001:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env); 
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
				case 799202: //Ipses
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							return sendQuestDialog(env, 2375);
						}
						case 1009:
						{
							player.getInventory().removeFromBagByItemId(182214017, 1);
							qs.setQuestVar(4);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return defaultQuestEndDialog(env);
						}
						default: return defaultQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799202) //Ipses
			{
				switch(env.getDialogId())
				{
					case 1009:
					{
						return sendQuestDialog(env, 5);
					}
					default : return defaultQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
    @Override
    public void register()
	{
        qe.setNpcQuestData(799225).addOnQuestStart(questId); //Richelle
		qe.setNpcQuestData(799225).addOnTalkEvent(questId); //Richelle
        qe.setNpcQuestData(799332).addOnTalkEvent(questId); //Agovard
        qe.setNpcQuestData(799217).addOnTalkEvent(questId); //Renato
        qe.setNpcQuestData(799202).addOnTalkEvent(questId); //Ipses
    }
}