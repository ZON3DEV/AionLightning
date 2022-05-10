package quest.gelkmaros;

import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Kamui, Hellboy
 * 
 */
public class _21135VellunsRequest extends QuestHandler
{
	private final static int	questId	= 21135;

	public _21135VellunsRequest()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799239).addOnQuestStart(questId);	//Vellun
		qe.setNpcQuestData(799270).addOnTalkEvent(questId);		//Skira
		qe.setNpcQuestData(799271).addOnTalkEvent(questId);		//Gehlen
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{

		if(defaultQuestNoneDialog(env, 799239))
			return true;

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 799270)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1352);
					case 10000:
						return defaultCloseDialog(env, 0, 1, true, false);
				}
			}
		}
		return defaultQuestRewardDialog(env, 799271, 2375);
	}
}
