/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.pandaemonium;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Rolandas
 *
 */
public class _80021EventFestiveUs extends QuestHandler
{
	private final static int	questId	= 80021;
	private final static int[]	npcs = {799784, 799783, 203618, 203650};
	
	public _80021EventFestiveUs()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(799784).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if ((qs == null || qs.getStatus() == QuestStatus.NONE) && !onLvlUpEvent(env))
			return false;
		
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(env.getQuestId());

		if(qs == null || qs.getStatus() == QuestStatus.NONE || 
		   qs.getStatus() == QuestStatus.COMPLETE && qs.getCompleteCount() < template.getMaxRepeatCount())
		{
			if(env.getTargetId() == 799784)
			{
				if(env.getDialogId() == -1 || env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestNoneDialog(env, 799784, 182214014, 1);
			}
			return false;
		}
		
		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 799783)
			{
				if(env.getDialogId() == -1 || env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10000)
				{
					defaultCloseDialog(env, 0, 1, 182214015, 2, 182214014, 1);
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
			else if(env.getTargetId() == 203618 && var == 1)
			{
				if(env.getDialogId() == -1 || env.getDialogId() == 26)
					return sendQuestDialog(env, 1693);
				else if(env.getDialogId() == 1694)
				{
					int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.EMOTE, 7,
						targetObjectId), true);
					return sendQuestDialog(env, 1694);
				}
				else if(env.getDialogId() == 10001)
				{
					defaultCloseDialog(env, 1, 2, 0, 0, 182214015, 1);
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
			else if(env.getTargetId() == 203650 && var == 2)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2034);
				else if(env.getDialogId() == 2035)
				{
					int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.EMOTE, 30,
						targetObjectId), true);
					return sendQuestDialog(env, 2035);
				}
				else if(env.getDialogId() == 10002)
					return defaultCloseDialog(env, 2, 3, true, false, 0, 0, 0, 182214015, 1);
				else
					return defaultQuestStartDialog(env);
			}
		}

		return defaultQuestRewardDialog(env, 799784, 2375);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Calendar now = Calendar.getInstance();
		Calendar cal1 = new GregorianCalendar(now.get(Calendar.YEAR), 11, 15);
		Calendar cal2 = new GregorianCalendar(now.get(Calendar.YEAR) + 1, 0, 5);
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (cal1.before(now) && cal2.after(now))
		{
			if(!QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel()))
				return false;

			return true;
		} 
		else if (qs != null)
		{
			// Set as expired
			QuestEngine.getInstance().deleteQuest(player, questId);
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId));
			player.getController().updateNearbyQuests();
		}
		return false;
	}	
	
}
