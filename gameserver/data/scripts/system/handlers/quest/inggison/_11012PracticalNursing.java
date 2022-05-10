package quest.inggison;

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
import org.openaion.gameserver.utils.PacketSendUtility;



/**
 * @author Assholes
 * 
 */
public class _11012PracticalNursing extends QuestHandler
{
	private final static int	questId	= 11012;
	
	public _11012PracticalNursing()
	{
		super(questId);
	}

	@Override
	public void register()
	{
	qe.setNpcQuestData(799071).addOnQuestStart(questId);
	qe.setNpcQuestData(799071).addOnTalkEvent(questId);
	qe.setNpcQuestData(799072).addOnTalkEvent(questId);
	qe.setNpcQuestData(799073).addOnTalkEvent(questId);
	qe.setNpcQuestData(799074).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 799071)
		{
			if(qs == null)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else if(env.getDialogId() == 1002)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206715, 3))))
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

		if (qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 799072 && var == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
				{
					qs.setQuestVar(++var);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
			else if(targetId == 799073 && var == 1)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10001)
				{
					qs.setQuestVar(++var);
					 updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
			else if(targetId == 799074 && var == 2)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1693);
				else if(env.getDialogId() == 10002)
				{
					qs.setStatus(QuestStatus.REWARD);
					 updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
                }
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799071)
			{	
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}
