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
package quest.heiron;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Degsx
 *
 */ 
public class _18600ScoringSomeBadStigma extends QuestHandler
{
	private final static int	questId	= 18600;
		
	public _18600ScoringSomeBadStigma()
	{
		super(questId);
	}
    
    @Override
	public void register()
	{
		int[] npcs = {204500, 798321, 205228};		
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
        qe.setNpcQuestData(204500).addOnQuestStart(questId);
		qe.addQuestLvlUp(questId);
        qe.addOnQuestFinish(questId);
	}
    
    @Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 204500, 182213000, 1))
			return true;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 798321)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1352);
					case 10000:
						return defaultCloseDialog(env, 0, 1);
				}
            }
            else if(env.getTargetId() == 205228)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 1)
							return sendQuestDialog(env, 1693);
					case 10001:
						return defaultCloseDialog(env, 1, 2, true, false, 182213001, 1, 182213000, 1);
				}
            }
		}
        return defaultQuestRewardDialog(env, 204500, 2375);
    }
}
