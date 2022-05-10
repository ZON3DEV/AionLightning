/**
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package org.openaion.gameserver.network.aion.clientpackets;

import java.util.List;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.DescriptionId;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.RequestResponseHandler;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.model.templates.quest.CollectItem;
import org.openaion.gameserver.model.templates.quest.CollectItems;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;



/**
 * 
 * @author Avol
 * 
 */
public class CM_DELETE_ITEM extends AionClientPacket
{
	public int objId;

	public CM_DELETE_ITEM(int opcode)
	{
		super(opcode);
	}


	@Override
	protected void readImpl()
	{
		objId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		Item resultItem = player.getInventory().getItemByObjId(objId);
		if(resultItem == null)
			return;
		boolean campaignItem = false;
		boolean collectable = false;
		boolean activeQuest = false;
		final int itemQuestId = resultItem.getItemTemplate().getItemQuestId();
		QuestTemplate questTemplate = DataManager.QUEST_DATA.getQuestById(itemQuestId);
		
		if(questTemplate != null)
		{
			final QuestState qs = player.getQuestStateList().getQuestState(itemQuestId);
			if(qs != null)
			{
				if (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD)
				{
					activeQuest = true;				
					CollectItems collectitems = questTemplate.getCollectItems(); //check if it's collectable item which can be deleted
					if (collectitems != null)
					{
						List<CollectItem> collectitem = collectitems.getCollectItem();
						if (collectitem != null)
						{
							for (CollectItem ci : collectitem)
							{
								if (ci.getItemId() == resultItem.getItemId())
								{
									collectable = true;
									break;
								}
							}
						}
					}
					
					if(questTemplate.isCannotGiveup()) //check if it's a campaign quest or regular
						campaignItem = true;
				}
			}
		}
		
		if(activeQuest)
		{
			if(!collectable)
			{
				if(campaignItem)
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_QUEST_GIVEUP_WHEN_DELETE_QUEST_ITEM_IMPOSSIBLE(new DescriptionId(resultItem.getNameID()), new DescriptionId(questTemplate.getNameId())));
					  return;
				}
				else
				{
					RequestResponseHandler responseHandler = new RequestResponseHandler(player){

						public void acceptRequest(Creature requester, Player responder)
						{
							QuestEngine.getInstance().deleteQuest(player, itemQuestId);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(itemQuestId));
							player.getController().updateNearbyQuests();
							return;
						}

						public void denyRequest(Creature requester, Player responder)
						{
							return;
						}
					};

					boolean requested = player.getResponseRequester().putRequest(
						SM_QUESTION_WINDOW.STR_QUEST_GIVEUP_WHEN_DELETE_QUEST_ITEM, responseHandler);
					if(requested)
					{
						PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(
							SM_QUESTION_WINDOW.STR_QUEST_GIVEUP_WHEN_DELETE_QUEST_ITEM, 0, new DescriptionId(resultItem.getNameID()), new DescriptionId(questTemplate.getNameId())));
							return;
					}
				}
			}
		}
		if(player.getInventory().removeFromBag(resultItem, true))
			sendPacket(new SM_DELETE_ITEM(objId));
	}
}
