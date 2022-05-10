package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.quest.FinishedQuestCond;
import com.aionemu.gameserver.model.templates.quest.XMLStartCondition;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

import java.util.List;

/**
 * Created by Kill3r
 */
public class CmdAddQuest extends AbstractGMHandler {

    public CmdAddQuest(Player admin, String params){
        super(admin, params);
        run();
    }

    public void run(){
        try{
            int id = Integer.parseInt(params);
            QuestEnv env = new QuestEnv(admin, target, id, 0);

            if (QuestService.startQuest(env)) {
                PacketSendUtility.sendMessage(admin, "Quest started.");
            } else {
                QuestTemplate template = DataManager.QUEST_DATA.getQuestById(id);
                List<XMLStartCondition> preconditions = template.getXMLStartConditions();
                if (preconditions != null && preconditions.size() > 0) {
                    for (XMLStartCondition condition : preconditions) {
                        List<FinishedQuestCond> finisheds = condition.getFinishedPreconditions();
                        if (finisheds != null && finisheds.size() > 0) {
                            for (FinishedQuestCond fcondition : finisheds) {
                                QuestState qs1 = admin.getQuestStateList().getQuestState(fcondition.getQuestId());
                                if (qs1 == null || qs1.getStatus() != QuestStatus.COMPLETE) {
                                    PacketSendUtility.sendMessage(admin, "You have to finish " + fcondition.getQuestId() + " first!");
                                }
                            }
                        }
                    }
                }
                PacketSendUtility.sendMessage(admin, "Quest not started. Some preconditions failed");
            }
        }catch (NumberFormatException e){
            PacketSendUtility.sendMessage(admin, "Quest Id Not Found!");
        }

    }
}
