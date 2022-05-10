/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.eltnen;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Rolandas
 * 
 */
public class _1463MessageToASpy extends QuestHandler
{
	private final static int	questId	= 1463;

	public _1463MessageToASpy()
	{
		super(questId);
	}
	
    @Override
	public void register()
	{
		qe.setNpcQuestData(203940).addOnQuestStart(questId);
		qe.setNpcQuestData(203940).addOnTalkEvent(questId);
		qe.setNpcQuestData(203903).addOnTalkEvent(questId);
		qe.setNpcQuestData(204424).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if (defaultQuestNoneDialog(env, 203940))
			return true;
		
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		int tempVar = qs.getQuestVarById(1);
		
		Player player = env.getPlayer();
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203903:
					switch(env.getDialogId())
					{
						case -1:
							if (var == 2)
								return sendQuestDialog(env, 2375);
							else
								return false;
						case 26:
							if (var == 0)
							{
								// reset temp var
								qs.setQuestVarById(1, 0);
								return sendQuestDialog(env, 1352);
							}
						case 1438:
							if (var == 0)
							{
								// accepted
								qs.setQuestVarById(1, 1);
								return sendQuestDialog(env, 1438);
							}
						case 10000:
							if (tempVar == 1)
								return defaultCloseDialog(env, 0, 1, false, false, 182201382, 1, 0, 0);
							else
							{
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
								return true;
							}
						case 1009:
							if (player.getInventory().getItemCountByItemId(182201383) > 0)
							{
								defaultQuestRemoveItem(env, 182201383, 1);
								return defaultCloseDialog(env, 2, 0, true, true);
							}
							return sendQuestDialog(env, 2375);
					}
				break;
				case 204424:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1693);
						case 10001:
							return defaultCloseDialog(env, 1, 2, false, false, 182201383, 1, 182201382, 1);
					}
				break;
			}
		}

		return defaultQuestRewardDialog(env, 203903, 0);
	}
}
