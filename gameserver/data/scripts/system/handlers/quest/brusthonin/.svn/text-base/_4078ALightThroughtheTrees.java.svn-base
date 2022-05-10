package quest.brusthonin;

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


/**
 * @author Assholes
 *
 */
public class _4078ALightThroughtheTrees extends QuestHandler {

	private final static int questId = 4078;
	private final static int[] npc_ids = { 205157, 700427, 700428, 700429 };
	
	public _4078ALightThroughtheTrees() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.setNpcQuestData(205157).addOnQuestStart(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env) {
	final Player player = env.getPlayer();
	final QuestState qs = player.getQuestStateList().getQuestState(questId);

	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	if(targetId == 205157)
	{
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
                		if (env.getDialogId() == 26)
                    			return sendQuestDialog(env, 4762);
                		else
                    			return defaultQuestStartDialog(env);
            	}
	}
	if(qs == null)
	return false;
		
	int var = qs.getQuestVarById(0);
	if(qs.getStatus() == QuestStatus.REWARD)
	{
		if(targetId == 205157)
		{
			if (env.getDialogId() == -1)
				return sendQuestDialog(env, 10002);
			else if (env.getDialogId() == 1009)
				return sendQuestDialog(env, 5);
			else return defaultQuestEndDialog(env);
		}
	}
	else if(qs.getStatus() != QuestStatus.START)
	{
		return false;
	}
	if(targetId == 205157)
	{
		switch(env.getDialogId())
		{
			case 26:
				if(var == 0)
					return sendQuestDialog(env, 1011);
			case 34:
				if(player.getInventory().getItemCountByItemId(182209049) >= 9 )
				{
					player.getInventory().removeFromBagByItemId(182209049, 9);
					ItemService.addItems(player, Collections.singletonList(new QuestItems(182209050, 1)));
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					return sendQuestDialog(env, 10000);
				}
				else
					return sendQuestDialog(env, 10001);
		}
	}
	else if(targetId == 700428)
	{
			switch(env.getDialogId())
			{
				case -1:
					if(var == 1)
					{
						if (player.getInventory().getItemCountByItemId(182209050) == 1)
 						{
                					final int targetObjectId = env.getVisibleObject().getObjectId();
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);	

                					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                        			targetObjectId), true);
                					ThreadPoolManager.getInstance().schedule(new Runnable() {
                    					@Override
                    					public void run() {
                        				if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
                            				return;
                        				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
                                			3000, 0));
                        				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                			targetObjectId), true);
                    					}
                					}, 3000);
            				}
        				}
				return false;
			}
		}
		else if(targetId == 700427)
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var == 2)
					{
						if (player.getInventory().getItemCountByItemId(182209050) == 1)
 						{
                					final int targetObjectId = env.getVisibleObject().getObjectId();
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);	

                					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                        			targetObjectId), true);
                					ThreadPoolManager.getInstance().schedule(new Runnable() {
                    					@Override
                    					public void run() {
                        				if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
                            				return;
                        				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
                                			3000, 0));
                        				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                			targetObjectId), true);
                    					}
                					}, 3000);
            				}
        				}
				return false;
			}
		}
		else if(targetId == 700429)
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var == 3)
					{
						if (player.getInventory().getItemCountByItemId(182209050) == 1)
 						{
                					final int targetObjectId = env.getVisibleObject().getObjectId();
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);	

                					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                        			targetObjectId), true);
                					ThreadPoolManager.getInstance().schedule(new Runnable() {
                    					@Override
                    					public void run() {
                        				if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
                            				return;
                        				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
                                			3000, 0));
                        				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                			targetObjectId), true);
                    					}
                					}, 3000);
            				}
        				}
				return false;
			}
		}
		return false;
	}
}
