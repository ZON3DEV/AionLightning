/*
 * This file is part of aion-unique.
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
package quest.silentera_canyon;

import java.util.Collections;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


public class _30056Dirvisias_Sorrow extends QuestHandler
{
	private final static int questId = 30056;
	
	public _30056Dirvisias_Sorrow()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 798929) //Gellius
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else if(env.getDialogId() == 1011)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182209223, 1)))) //Stone of Restoration
						return sendQuestDialog(env, 4);
					else
						return true;
					}
					else
						return defaultQuestStartDialog(env);
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203901) //Telemachus
			    player.getInventory().removeFromBagByItemId(182209223, 1); //Stone Of Restoration
				player.getInventory().removeFromBagByItemId(182209224, 1); //Dirvisia's Necklace
				return defaultQuestEndDialog(env);
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 700569: //Statue Dirvisia
	            {
                    if (qs.getQuestVarById(0) == 1 && player.getInventory().getItemCountByItemId(182209223) > 0) //Stone Of Restoration
		            {
                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                        ThreadPoolManager.getInstance().schedule(new Runnable()
					    {
                            @Override
                            public void run()
				            {
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182209224, 1))); //Dirvisia's Necklace
                                qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                                updateQuestStatus(env);
                            }
                        }, 3000);
					}
				}
			}
		}
			return false;
	}
	
	@Override
	public void register()
	{
	    qe.setNpcQuestData(798929).addOnQuestStart(questId); //Gellius
		qe.setNpcQuestData(798929).addOnTalkEvent(questId); //Gellius
		qe.setNpcQuestData(799234).addOnTalkEvent(questId); //Nep
		qe.setNpcQuestData(700569).addOnActionItemEvent(questId); //Statue Dirvisia
		qe.setQuestItemIds(182209223).add(questId); //Stone Of Restoration
		qe.setQuestItemIds(182209224).add(questId); //Dirvisia's Necklace
	}
}