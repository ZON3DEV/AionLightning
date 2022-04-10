package quest.tutorial;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Falke_34
 */
public class _90001Tutorial_Title extends QuestHandler {

	private final static int questId = 90001;

	public _90001Tutorial_Title() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		DialogAction action = env.getDialog();

		if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
			switch (action) {
			case QUEST_AUTO_REWARD:
				qs.setQuestVar(0);
				qs.setStatus(QuestStatus.COMPLETE);
				updateQuestStatus(env);
				player.getTitleList().addTitle(379, true, 0);
				return closeDialogWindow(env);
			default:
				break;
			}
		}
		return false;
	}

}