/*
 * This file is part of Encom Evolved <Encom Evolved.com>
 *
 *  Encom Evolved is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom Evolved is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom Evolved.  If not, see <http://www.gnu.org/licenses/>.
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


public class _21138Odd_Strigik extends QuestHandler
{
	private final static int questId = 21138;
	
	public _21138Odd_Strigik()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 799415) //Ulakin
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
                  {
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182207921, 1)))) //Leaf Woven Pouch
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
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 799415) //Ulakin
			{
			if (env.getDialogId() == -1)
				return sendQuestDialog(env, 2375);
			else if (env.getDialogId() == 1009)
				return sendQuestDialog(env, 5);
			else return defaultQuestEndDialog(env);}
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
			if(targetId == 799274) //Bibi Lu
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1352);
					case 10000:
						if(var == 0)
						{
						    qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					return false;
				}
			}
            else if(targetId == 799273) //Bibiti
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 1)
							return sendQuestDialog(env, 1693);
					case 10001:
						if(var == 1)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					return false;
				}
			}
			else if(targetId == 799263) //Tamiki
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 2)
							return sendQuestDialog(env, 2034);
					case 10002:
						if(var == 2)
						{
                       				player.getInventory().removeFromBagByItemId(182207921, 1); //Leaf Woven Pouch
							if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182207922, 1)))) //Big Clothes Package
							qs.setQuestVarById(0, var + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					return false;
				}
			}
		return false;
	}
	
	@Override
    public void register()
	{
        qe.setNpcQuestData(799415).addOnQuestStart(questId); //Ulakin
		qe.setNpcQuestData(799415).addOnTalkEvent(questId); //Ulakin
        qe.setNpcQuestData(799274).addOnTalkEvent(questId); //Bibi Lu
        qe.setNpcQuestData(799273).addOnTalkEvent(questId); //Bibiti
        qe.setNpcQuestData(799263).addOnTalkEvent(questId); //Tamiki
    }
}