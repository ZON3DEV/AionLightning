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
package quest.greater_stigma_quest;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;


public class _30217StigmaAndScars	extends QuestHandler
{
	private final static int questId = 30217;
	
	public _30217StigmaAndScars()
	{
		super (questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		//Instanceof
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		// ------------------------------------------------------------
        // NPC Quest :
		// Reemul start
		if (qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 798909) // Reemul Start
			{
				if (env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if (qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				// Pilomenes
				case 798941:
					if (var == 0)
					{
						switch (env.getDialogId())
						{
							case 26:
								return sendQuestDialog(env, 1011);
							case 10000:
								qs.setQuestVar(1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
					}
					// Faithful Utra summoned
				case 799506:
					if (var == 1)
					{
						switch (env.getDialogId())
						{
							case 26:
								return sendQuestDialog(env, 1352);
							case 10001:
								qs.setQuestVar(2);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
					}
				case 798909:
					if (var == 2)
					{
						switch (env.getDialogId())
						{
							case 26:
								return sendQuestDialog(env, 1693);
							case 34:
								if (player.getInventory().getItemCountByItemId(182209618) < 1)
								{
									return sendQuestDialog(env, 10001);
								}
								else if (player.getInventory().getItemCountByItemId(182209619) < 1)
								{
									return sendQuestDialog(env, 10001);
								}
								player.getInventory().removeFromBagByItemId(182209618, 1);
								player.getInventory().removeFromBagByItemId(182209619, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
						}
					}
				return false;
			}
		}
		return false;
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(798909).addOnQuestStart(questId); //Reemul start
		qe.setNpcQuestData(798941).addOnTalkEvent(questId); //Pilomenes 
		qe.setNpcQuestData(799506).addOnTalkEvent(questId); //Faithful Responded Ultra summoned
		qe.setNpcQuestData(798909).addOnTalkEvent(questId); //Reemul finish
	}
}