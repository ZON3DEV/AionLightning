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
package quest.altgard;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author Mr. Poke
 *
 */
public class _2290GrokensEscape extends QuestHandler
{

	private final static int	questId	= 2290;
	
	private boolean executorReturn = true;

	public _2290GrokensEscape()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203608).addOnQuestStart(questId);
		qe.setNpcQuestData(203608).addOnTalkEvent(questId);
		qe.setNpcQuestData(700178).addOnTalkEvent(questId);
		qe.setNpcQuestData(203607).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 203608)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if (env.getDialogId() == 1002)
				{
					if (QuestService.startQuest(env, QuestStatus.START))
					{
						Npc npc = (Npc)env.getVisibleObject();
						npc.getMoveController().setNewDirection(1219.15f, 1212f, 247.37f);
						npc.getMoveController().schedule();
						return sendQuestDialog(env, 1003);
					}
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 700178:
				{
					if (qs.getQuestVarById(0) == 0 && env.getDialogId() == -1)
					{
						executorReturn = true;
						
						player.getKnownList().doOnAllNpcs(new Executor<Npc>(){
							@Override
							public boolean run(Npc obj)
							{
								if (obj.getNpcId() != 203608)
									return true;
								if (MathUtil.getDistance(player.getX(), player.getY(), player.getZ(),obj.getX(), obj.getY(), obj.getZ()) > 4)
								{
									executorReturn = false;
									return false;
								}
								obj.getController().onDie(null);
								obj.getController().onDespawn(false);
								return true;
							}
						}, true);
						
						if (!executorReturn)
						{
							return false;
						}
						
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
							1));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
							targetObjectId), true);
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								if(!player.isTargeting(targetObjectId))
									return;
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
									targetObjectId, 3000, 0));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
									targetObjectId), true);
								qs.setQuestVarById(0, 3);
								updateQuestStatus(env);
							}
						}, 3000);
					}
				}
				break;
				case 203607:
				{
					if (qs.getQuestVarById(0) == 3)
					{
						if(env.getDialogId() == 26)
							return sendQuestDialog(env, 1693);
						else if(env.getDialogId() == 1009)
							{
								player.getInventory().removeFromBagByItemId(182203208, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return defaultQuestEndDialog(env);
							}
							else
								return defaultQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203607)
				return defaultQuestEndDialog(env);
		}
		return false;
	}
}
