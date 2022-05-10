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
import org.openaion.gameserver.utils.ThreadPoolManager;


public class _4934TheShulacksStigma extends QuestHandler
{
	private final static int questId = 4934;
	
	public _4934TheShulacksStigma()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		// Instanceof
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		// ------------------------------------------------------------
		// NPC Quest :
		// 0 - Vergelmir start
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 204051)
			{
				// Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
				if(env.getDialogId() == 26)
					// Send HTML_PAGE_SELECT_NONE to eddit-HtmlPages.xml
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);

			}
		}
		
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);			

		if(qs.getStatus() == QuestStatus.START)
		{
			
			switch(targetId)
			{
				  
        //Moreinen
        case 204211:
					if(var == 0)
					{
						switch(env.getDialogId())
						{
							// Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
							case 26:
								// Send select1 to eddit-HtmlPages.xml
								return sendQuestDialog(env, 1011);
							// Get HACTION_SETPRO1 in the eddit-HyperLinks.xml
							case 10000:
								qs.setQuestVar(1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				// 2 / 4- Talk with Teirunerk
				case 204285:
					if(var == 1)
					{
						switch(env.getDialogId())
						{
							// Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
							case 26:
							// Send select1 to eddit-HtmlPages.xml
							return sendQuestDialog(env, 1352);
							// Get HACTION_SETPRO1 in the eddit-HyperLinks.xml
							case 10001:
								qs.setQuestVar(2);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
					else if(var == 2)
					{
						switch(env.getDialogId())
						{
							// Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
							case 26:
							// Send select1 to eddit-HtmlPages.xml
							return sendQuestDialog(env, 1693);
							// Get HACTION_SETPRO1 in the eddit-HyperLinks.xml
							case 34:
								if(player.getInventory().getItemCountByItemId(182207102) < 1)
								{
									// player doesn't own required item
									return sendQuestDialog(env, 10001);
								}
								player.getInventory().removeFromBagByItemId(182207102, 1);	
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);	
								return sendQuestDialog(env, 10000);
						}
					
          }
          return false;
					case 700562:
			    if (var == 2) 
           {
           ThreadPoolManager.getInstance().schedule(new Runnable(){
					  @Override
						 public void run()
									{
									 updateQuestStatus(env);
                  }
								}, 3000);
           return true;
           }
          break;
			     
          
      }
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204051)
				{
        if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
				}
		}
	return false;
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204051).addOnQuestStart(questId); //Vergelmir start
		qe.setNpcQuestData(204211).addOnTalkEvent(questId);	//Moreinen
		qe.setNpcQuestData(204285).addOnTalkEvent(questId);	//Teirunerk
		qe.setNpcQuestData(700562).addOnTalkEvent(questId); //
    qe.setNpcQuestData(204051).addOnTalkEvent(questId);		// Vergelmir
	}
}
