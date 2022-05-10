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
package quest.pandaemonium;

import java.util.Collections;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.WorldMapType;


/**
 * @author DNS
 *
 */
public class _2938SecretLibraryAccess extends QuestHandler {
	private final static int questId	= 2938;
	private final static int[] npc_ids	= { 204267, 203557 }; // 204267 - Oublette(start and finish), 203557 - Suthran(for recomendation)

	public _2938SecretLibraryAccess()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204267).addOnQuestStart(questId);
		for(int npc_id : npc_ids) {
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
		}
	}

	//// self explanatory ////////////////////////////////////////////////////////////////////
	private boolean AreVerteronQuestsFinished(Player player) {
		QuestState qs = player.getQuestStateList().getQuestState(2022); // last quest in Altgard state
		return ((qs == null) || (qs.getStatus() != QuestStatus.COMPLETE)) ? false : true;
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(targetId == 204267)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE) {
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else return defaultQuestStartDialog(env);
			} else if (qs.getStatus() == QuestStatus.REWARD && qs.getQuestVarById(0) == 0 ||
				       qs.getStatus() == QuestStatus.COMPLETE) {
				if(env.getDialogId() == -1 && qs.getStatus() == QuestStatus.REWARD)
					return sendQuestDialog(env, 10002);
				else if (env.getDialogId() == 18) {
					player.getInventory().removeFromBagByItemId(182207026, 1);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return defaultQuestEndDialog(env);
				} else if (env.getDialogId() == 1009) {
					return defaultQuestEndDialog(env);
				}
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						TeleportService.teleportTo(player, WorldMapType.PANDAEMONIUM.getId(), 1400.1f, 1060.9f, 206.0f, 119);
					}
				}, 3000);
			}
		} else if(targetId == 203557)
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 26) {
					if(AreVerteronQuestsFinished(player)) {
						return sendQuestDialog(env, 1011);
					} else return sendQuestDialog(env, 1097);
				}
				else if(env.getDialogId() == 10255) {
					if(ItemService.addItems(player, Collections.singletonList(new QuestItems(182207026, 1)))) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
					}
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
				else return defaultQuestStartDialog(env);
			}
		}
		return false;
	}
}
