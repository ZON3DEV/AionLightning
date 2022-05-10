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
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;


public class _30317SpiritsAndStigmaSlots extends QuestHandler
{
    private final static int questId = 30317;

    public _30317SpiritsAndStigmaSlots()
	{
        super(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        // Instanceof
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        // ------------------------------------------------------------
        // NPC Quest :
        // 0 - Start to //Garath
        if (qs == null || qs.getStatus() == QuestStatus.NONE)
		{
            if (targetId == 798208) //Garath
			{
                // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                if (env.getDialogId() == 26)
                    // Send HTML_PAGE_SELECT_NONE to eddit-HtmlPages.xml
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
                case 799322: //Herka
                    switch (env.getDialogId())
					{
                        // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                        case 26:
                        // Send select1 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 1011);
                        // Get HACTION_SETPRO1 in the eddit-HyperLinks.xml
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 799506: //Faithful Respondent Utra
                    if (var == 1)
					{
                        switch (env.getDialogId())
						{
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 26:
                            // Send select2 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 1352);
                            // Get HACTION_SETPRO2 in the eddit-HyperLinks.xml
                            case 10001:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    // Report The Result To Garath.
                case 798208: //Garath
                    switch (env.getDialogId())
					{
                        // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                        case 26:
                        // Send select1 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 2375);
                         case 2034:
                        // Send select2 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 2034);
                        // Get HACTION_CHECK_USER_HAS_QUEST_ITEM in the eddit-HyperLinks.xml
                        case 34:
                        // Collect Spirit's Incense Burner (1)
                        // Collect Scroll Of Repose (1)
                            if (QuestService.collectItemCheck(env, true))
	          {
                                player.getInventory().removeFromBagByItemId(182209718, 1);
                                player.getInventory().removeFromBagByItemId(182209719, 1);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else {
                                // Send check_user_item_fail to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                // No match
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 798208) //Garath
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
    @Override
    public void register()
	{
        qe.setNpcQuestData(798208).addOnQuestStart(questId); //Garath
        qe.setNpcQuestData(799322).addOnTalkEvent(questId); //Herka
		qe.setNpcQuestData(799506).addOnTalkEvent(questId); //Faithful Respondent Utra
		qe.setNpcQuestData(798208).addOnTalkEvent(questId); //Garath
    }
}