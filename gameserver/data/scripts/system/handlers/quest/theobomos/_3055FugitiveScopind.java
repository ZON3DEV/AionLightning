package quest.theobomos;

import java.util.Collections;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Leunam
 *
 */
public class _3055FugitiveScopind extends QuestHandler {
	private final static int questId = 3055;
	
	public _3055FugitiveScopind() {
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(730146).addOnQuestStart(questId);
		qe.setNpcQuestData(730146).addOnTalkEvent(questId);
		qe.setNpcQuestData(798195).addOnTalkEvent(questId);	
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 730146)
		{
            	if (env.getDialogId() == -1)
                		return sendQuestDialog(env, 4762);
            	else if (env.getDialogId() == 10000) {
                		QuestService.startQuest(env, QuestStatus.START);
                		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                		return true;
            	}
		}
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798195)
			{
				return defaultQuestEndDialog(env);
			}
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 798195)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
				case 34:
					if(var == 0)
					{
						if(player.getInventory().getItemCountByItemId(182208040) >= 1 )
						{
							player.getInventory().removeFromBagByItemId(182208040, 1);
							qs.setQuestVarById(0, var + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);								
							return sendQuestDialog(env, 5);
						}
						else
							return sendQuestDialog(env, 10001);
					}
					return false;
			}
		}			
		return false;
	}
}
