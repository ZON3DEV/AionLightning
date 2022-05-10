/**
 * This file is part of Aion Mythology <www.aionmythology.com>
 */

package quest.gelkmaros;

import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.ItemService;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Orpheo
 */

public class _30317SpiritsAndStigmaSlots	extends QuestHandler
{
	private final static int questId = 30317;
	
	public _30317SpiritsAndStigmaSlots()
	{
		super (questId);
	}
	
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(799208).addOnQuestStart(questId);
		qe.setNpcQuestData(799322).addOnTalkEvent(questId);
		qe.setNpcQuestData(799506).addOnTalkEvent(questId);
		qe.setNpcQuestData(799208).addOnTalkEvent(questId); 
	}
	
    @Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, 30316);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if (targetId == 799208)
			{
				if (env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if (qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 799208)
            {
                return defaultQuestEndDialog(env);
            }
            return false;
		} else if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 799322:
					if (var == 0)
					{
						switch (env.getDialogId())
						{
							case 26:
								return sendQuestDialog(env, 1011);
							case 10000:
								qs.setQuestVar(1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
					}
				case 799506:
					if (var == 1)
					{
						switch (env.getDialogId())
						{
							case 26:
								return sendQuestDialog(env, 1352);
							case 10001:
								qs.setQuestVar(2);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
					}
				case 799208:
					if (var == 2)
					{
						switch (env.getDialogId())
						{
							case 26:
								return sendQuestDialog(env, 1693);
							case 34:
								if (player.getInventory().getItemCountByItemId(182209718) < 1 || (player.getInventory().getItemCountByItemId(182209719) < 1))
								{
									return sendQuestDialog(env, 10001);
								}
								else if (player.getInventory().getItemCountByItemId(182209718) == 1 || (player.getInventory().getItemCountByItemId(182209719) == 1))
								{
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);									
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
									player.getInventory().removeFromBagByItemId(182209718, 1);
									player.getInventory().removeFromBagByItemId(182209719, 1);
									return sendQuestDialog(env, 5);
								} else
									return sendQuestDialog(env, 2716);
							}
						}
				return false;
			}
		}
		return false;
	}
}