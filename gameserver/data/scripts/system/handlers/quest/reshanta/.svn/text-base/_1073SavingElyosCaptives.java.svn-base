/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.reshanta;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.zone.ZoneName;


/**
 * @author Degsx
 *
 */
 
 public class _1073SavingElyosCaptives extends QuestHandler
{
	private final static int	questId	= 1073;
	
	public _1073SavingElyosCaptives()
	{
		super(questId);
	}
	
    @Override
	public void register()
	{
		qe.setNpcQuestData(278502).addOnTalkEvent(questId); //sakmis
		qe.setNpcQuestData(278517).addOnTalkEvent(questId); //Nereus
		qe.setNpcQuestData(278590).addOnTalkEvent(questId); //Dactyl
		qe.setNpcQuestData(253623).addOnTalkEvent(questId); //Elyos prisioner
		qe.setQuestEnterZone(ZoneName.Q1073).add(questId);
		qe.addQuestLvlUp(questId);
	}   
    
    @Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}
    
    @Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);

		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 278502)//sakmis
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
					case 10000:
						if(var == 0)
							return defaultCloseDialog(env, 0, 1);

				}
			}
            
            else if(env.getTargetId() == 278517)//Nereus
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 1)
							return sendQuestDialog(env, 1352);
					case 10001:
						if(var == 1)
							return defaultCloseDialog(env, 1, 2);

				}
			}
            
            else if(env.getTargetId() == 278590)//Dactyl
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 2)
							return sendQuestDialog(env, 1693);
					case 10002:
						if(var == 2)
							return defaultCloseDialog(env, 2, 3);

				}
			}
            
            else if(env.getTargetId() == 253623)//Elyos prisioner
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 3)
                            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 269));
							return sendQuestDialog(env, 2034);
					case 10003:
						if(var == 3)
							return defaultCloseDialog(env, 3, 4);

				}
			}
                        
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
			return defaultQuestRewardDialog(env, 278517, 10002);

		return false;
	}
        
    @Override
	public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName)
	{
    	Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(zoneName != ZoneName.Q1073)
			return false;
		if(qs == null || qs.getQuestVarById(0) != 4)
			return false;
        PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 270));
		qs.setStatus(QuestStatus.REWARD);
		updateQuestStatus(env);
		return true;
	}
	
}
