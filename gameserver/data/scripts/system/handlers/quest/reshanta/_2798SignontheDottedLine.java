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

import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author Degsx
 *
 */ 
public class _2798SignontheDottedLine extends QuestHandler
{
	private final static int	questId	= 2798;
		
	public _2798SignontheDottedLine()
	{
		super(questId);
	}
    
    @Override
	public void register()
	{
		int[] npcs = {279007, 263569, 263267, 264769, 271054, 266554, 270152, 269252, 268052, 260236};		
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
        qe.setNpcQuestData(279007).addOnQuestStart(questId);
	}
    
    @Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 279007, 4762, 182205646, 1))
			return true;
		
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 263569)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
					case 10000:
						return defaultCloseDialog(env, 0, 1);
				}
            }
            else if(env.getTargetId() == 263267)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 1)
							return sendQuestDialog(env, 1352);
					case 10001:
						return defaultCloseDialog(env, 1, 2);
				}
            }            
            else if(env.getTargetId() == 264769)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 2)
							return sendQuestDialog(env, 1693);
					case 10002:
						return defaultCloseDialog(env, 2, 3);
				} 
            }            
            else if(env.getTargetId() == 271054)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 3)
							return sendQuestDialog(env, 2034);
					case 10003:
						return defaultCloseDialog(env, 3, 4);
				}
            }            
            else if(env.getTargetId() == 266554)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 4)
							return sendQuestDialog(env, 2375);
					case 10004:
						return defaultCloseDialog(env, 4, 5);
				}
            }
            else if(env.getTargetId() == 270152)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 5)
							return sendQuestDialog(env, 2716);
					case 10005:
						return defaultCloseDialog(env, 5, 6);
				}
            }
            else if(env.getTargetId() == 269252)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 6)
							return sendQuestDialog(env, 3057);
					case 10006:
						return defaultCloseDialog(env, 6, 7);
				}
            }
            else if(env.getTargetId() == 268052)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 7)
							return sendQuestDialog(env, 3398);
					case 10007:
						return defaultCloseDialog(env, 7, 8);
				}
            }
            else if(env.getTargetId() == 260236)
            {
                switch(env.getDialogId())
				{
					case 26:
						if(var == 8)
							return sendQuestDialog(env, 3739);
					case 10255:
						return defaultCloseDialog(env, 8, 0, true, false);
				}
            }
		}
        return defaultQuestRewardDialog(env, 279007, 10002);
    }
}