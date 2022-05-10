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
package quest.reshanta;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.model.templates.bonus.AbstractInventoryBonus;
import org.openaion.gameserver.model.templates.bonus.InventoryBonusType;
import org.openaion.gameserver.model.templates.bonus.MedalBonus;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;


/**
 * @author Rolandas
 *
 */
public class _2717SilverForTheFountain extends QuestHandler
{

	private final static int	questId	= 2717;
	
	public _2717SilverForTheFountain()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(730143).addOnQuestStart(questId);
		qe.setNpcQuestData(730143).addOnTalkEvent(questId);
		qe.setNpcQuestData(730143).addOnActionItemEvent(questId);
		qe.setQuestBonusType(InventoryBonusType.MEDAL).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(env.getQuestId());
		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat(template.getMaxRepeatCount()))
		{
			if(env.getTargetId() == 730143)
			{
				switch(env.getDialogId())
				{
					case -1:
						if(QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel()))
							return sendQuestDialog(env, 1011);
						else
							return true;
					case 10000:
						if(player.getInventory().getItemCountByItemId(186000031) > 0)
						{
							if (qs == null)
							{
								qs = new QuestState(questId, QuestStatus.REWARD, 0, 0);
								player.getQuestStateList().addQuest(questId, qs);
							}
							else
								qs.setStatus(QuestStatus.REWARD);
							return sendQuestDialog(env, 5);
						}
						else
							return true;
				}
			}
		}
		
		if(qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.REWARD && env.getTargetId() == 730143)
		{
			if (env.getDialogId() == 17)
			{
				if (player.getInventory().getItemCountByItemId(186000031) > 0 &&
					QuestService.questFinish(env, 0))
				{
					sendQuestDialog(env, 1008);
					return true;
				}
			}
			return sendQuestDialog(env, 5);
		}
		
		return false;
	}
	
	@Override
	public boolean onActionItemEvent(QuestCookie env)
	{
		return (env.getTargetId() == 730143);
	}
	
    @Override
    public HandlerResult onBonusApplyEvent(QuestCookie env, int index, AbstractInventoryBonus bonus)
    {
		if(!(bonus instanceof MedalBonus))
		    return HandlerResult.UNKNOWN;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null && qs.getStatus() == QuestStatus.REWARD)
		{
		    if(index == 0)
		       return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
    }
}
