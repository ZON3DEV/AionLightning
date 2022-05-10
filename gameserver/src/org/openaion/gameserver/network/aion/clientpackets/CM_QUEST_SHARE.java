package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.QuestsData;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Kamui - Bio
 * 
 */
public class CM_QUEST_SHARE extends AionClientPacket
{
	static QuestsData		questsData = DataManager.QUEST_DATA;
	public int questId;

	public CM_QUEST_SHARE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		questId = readD();
	}

	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();

		//NPE Check - Exploit Check
		if (player == null || questsData.getQuestById(questId).isCannotShare())
			return;

		//Player can only share quests within a group
		if (!player.isInGroup())
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100000));//[there are no group members to share that quest with]
			return;
		}

		//Player cannot share quests he dont have or its already completed
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.COMPLETE)
			return;

		//Player must try to share quests with all his group members
		PlayerGroup playerGroup = player.getPlayerGroup();

		for(Player target : playerGroup.getMembers())
		{
			if( target == player || !MathUtil.isIn3dRange(target, player, 95))
				continue;

			//Cannot share quests if the target player dont meet the level requirements
			if (!QuestService.checkLevelRequirement(questId, target.getCommonData().getLevel()))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100003, (target.getName())));//[you failed to share the quest with %playername]
				PacketSendUtility.sendPacket(target, new SM_SYSTEM_MESSAGE(1100003, (player.getName())));//[you failed to share the quest with %playername]
				return;
			}

			//Send share quest dialog question to target players and wait for the answer
			PacketSendUtility.sendPacket(target, new SM_QUEST_ACCEPTED(questId, ((VisibleObject) player).getObjectId(), true));
		}
	}
}
