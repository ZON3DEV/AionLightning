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
package quest.daevanion_quest;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;


/**
 * @author HellBoy, modified Rolandas
 */
public class _1990ASagesGift extends QuestHandler
{
	private final static int	questId	= 1990;
	private final static int[]	mobs	= {256617, 253720, 254513};
	
	public _1990ASagesGift()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203771).addOnQuestStart(questId);
		qe.setNpcQuestData(203771).addOnTalkEvent(questId);
		for(int mob: mobs)
			qe.setNpcQuestData(mob).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		int plate = player.getEquipment().itemSetPartsEquipped(9);
		int chain = player.getEquipment().itemSetPartsEquipped(8);
		int leather = player.getEquipment().itemSetPartsEquipped(7);
		int cloth = player.getEquipment().itemSetPartsEquipped(6);
		int startDialog = 4762;

		if(plate != 5 && chain != 5 && leather != 5 && cloth != 5)
			startDialog = 4848;
		
		if(defaultQuestNoneDialog(env, 203771, startDialog))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0); // ignore 7 bits
		int var1 = (qs.getQuestVars().getQuestVars() >> 7) & 0x7F;

		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203771)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
						if(var == 1)
							return sendQuestDialog(env, 1352);
						if(var == 2 && var1 == 30)
							return sendQuestDialog(env, 1693);
						if(var == 3)
							return sendQuestDialog(env, 2034);
					case 34:
						return defaultQuestItemCheck(env, 0, 1, false, 10000, 10001);
					case 2035:
						if(var == 3)
						{
							if(player.getCommonData().getDp() < 4000 || player.getInventory().getItemCountByItemId(186000040) == 0)
								return sendQuestDialog(env, 2120);
							else if(defaultCloseDialog(env, 3, 0, true, false, 0, 0, 186000040, 1))
							{
								player.getCommonData().setDp(0);
								return sendQuestDialog(env, 5);
							}
						}
						break;
					case 10001:
						return defaultCloseDialog(env, 1, 2);
					case 10002:
						return defaultCloseDialog(env, 2, 3);
				}
			}
			return false;
		}
		return defaultQuestRewardDialog(env, 203771, 0);
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int var0 = qs.getQuestVarById(0);
		if (var0 != 2)
			return false;
		
		int var = qs.getQuestVars().getQuestVars();
		int var1 = (var >> 7) & 0x7F;
		int var2 = (var >> 14) & 0x7F;
		int var3 = (var >> 21) & 0x7F;
		boolean killed = false;
		
		int targetId = env.getTargetId();

		if((targetId == 256617 || targetId == 253720 || targetId == 254513) && qs.getQuestVarById(0) == 2)
		{
			switch(targetId)
			{
				case 256617:
					if(var1 >= 0 && var1 < 30)
					{
						++var1;
						killed = true;
					}
					break;
				case 253720:
					if(var2 >= 0 && var2 < 30)
					{
						++var2;
						killed = true;
					}
					break;
				case 254513:
					if(var3 >= 0 && var3 < 30)
					{
						++var3;
						killed = true;
					}
					break;
			}
		}
		if(killed)
		{
			var = var3;
			var = ((var << 7) | var2);
			var = ((var << 7) | var1);
			var = ((var << 7) | var0);
			qs.setQuestVar(var);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}
