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

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Rolandas
 *
 */
public class _2985AnExpertsReward extends QuestHandler
{

	private final static int	questId	= 2985;
	private final static int[]	skill_ids	= { 40001, 40002, 40003, 40004, 40007, 40008 };
	
	public _2985AnExpertsReward()
	{
		super(questId);
	}
	
    @Override
	public void register()
	{
		qe.setNpcQuestData(204052).addOnQuestStart(questId);
		qe.setNpcQuestData(204052).addOnTalkEvent(questId);
		qe.setNpcQuestData(204072).addOnTalkEvent(questId);
	}
    
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(env.getTargetId() == 204052)
			{
				if(env.getDialogId() == 26)
				{
					int skillLevel = 0;
					for (int skillId : skill_ids)
					{
						if (player.getSkillList().isSkillPresent(skillId))
							skillLevel = Math.max(skillLevel, player.getSkillList().getSkillLevel(skillId));
					}
					if (skillLevel <= 399)
						return sendQuestDialog(env, 4080);
					else
						return sendQuestDialog(env, 4762);
				}
				else
					return defaultQuestStartDialog(env);
			}
		}

		if(qs == null)
			return false;
		
		if (qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 204072)
			{
				if(env.getDialogId() == 26)
				{
					defaultCloseDialog(env, 0, 0, true, true);
					return sendQuestDialog(env, 2375);
				}
			}
		}
		
		return defaultQuestRewardDialog(env, 204072, 0);
	}

}
