package quest.beluslan;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Assholes
 *
 */
public class _2664AnAntidotetotheLepharists extends QuestHandler {
	private final static int questId = 2664;
	private final static int[] npc_ids = { 204777, 700324 };
	
	public _2664AnAntidotetotheLepharists() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.setNpcQuestData(204777).addOnQuestStart(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
	if(targetId == 204777)
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
		if(targetId == 204777)
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
	if(targetId == 700324)
	{
		switch(env.getDialogId())
		{
			case -1:
				if(var >= 0 && var < 4)
				{
					if(player.getInventory().getItemCountByItemId(182204489) >= 1 )
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return true;
					}
				}
				else if(var == 4)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			return false;
		}
	}
	return false;
     }
}
