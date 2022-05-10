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
package quest.beluslan;

import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Degsx
 *
 */ 
public class _28600SuspiciousErrand extends QuestHandler
{
	private final static int	questId	= 28600;
		
	public _28600SuspiciousErrand()
	{
		super(questId);
	}
    
    @Override
	public void register()
	{
		int[] npcs = {204702, 205233, 204254};		
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
        qe.setNpcQuestData(204702).addOnQuestStart(questId);
	}
    
    @Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 204702, 182213004, 1))
			return true;
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 205233)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1352);
                        else if(var == 2)
    							return sendQuestDialog(env, 2034);
					case 10000:
						return defaultCloseDialog(env, 0, 1);
                    case 10002:
						return defaultCloseDialog(env, 2, 3, true, false, 182213005, 1, 182213004, 1);
                    
				}
            }
            else if(env.getTargetId() == 204254)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 1)
							return sendQuestDialog(env, 1693);
					case 10001:
						return defaultCloseDialog(env, 1, 2);
				}
            }
		}
        return defaultQuestRewardDialog(env, 204702, 2375);
    }
}
