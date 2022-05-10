package quest.heiron;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Balthazar
 */

public class _1648UndeadWarAlert extends QuestHandler
{
	private final static int	questId	= 1648;

	public _1648UndeadWarAlert()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204545).addOnQuestStart(questId);
		qe.setNpcQuestData(204545).addOnTalkEvent(questId);
		qe.setNpcQuestData(204612).addOnTalkEvent(questId);
		qe.setNpcQuestData(204500).addOnTalkEvent(questId);
		qe.setNpcQuestData(204590).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 204545)
			{
				if(env.getDialogId() == 26)
				{
					return sendQuestDialog(env, 1011);
				}
				else
					return defaultQuestStartDialog(env);
			}
		}

		if(qs == null)
			return false;

		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 204612:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							if(qs.getQuestVarById(0) == 0)
							{
								return sendQuestDialog(env, 1352);
							}
						}
						case 10000:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 0));
							return true;
						}
					}
				}
				case 204500:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							if(qs.getQuestVarById(0) == 1)
							{
								return sendQuestDialog(env, 1693);
							}
						}
						case 10001:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 0));
							return true;
						}
					}
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204590)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}