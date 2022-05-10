package quest.brusthonin;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.bonus.AbstractInventoryBonus;
import org.openaion.gameserver.model.templates.bonus.InventoryBonusType;
import org.openaion.gameserver.model.templates.bonus.RedeemBonus;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;


/**
 *
 * @author Rolandas, HellBoy
 */
public class _4074GainorLoose extends QuestHandler
{
	private final static int questId = 4074;
	private final static int	Items[][] =
	{{1011, 1352, 1693},
	{1000, 5000, 25000}};

	public _4074GainorLoose()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(205181).addOnTalkEvent(questId);
		qe.setQuestBonusType(InventoryBonusType.REDEEM).add(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		int	removeKinahCount = 0;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = 0;
		
		if(qs != null)
			var = qs.getQuestVarById(1);
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE)
		{
			if(env.getTargetId() == 205181)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel()))
							return sendQuestDialog(env, 1011);
						else
							return sendQuestDialog(env, 3398);
					case 1011:
					case 1352:
					case 1693:
						int i = 0;
						for(int id: Items[0])
						{
							if(id == env.getDialogId())
								break;
							i++;
						}
						removeKinahCount = Items[1][i];
						var = i;
						
						if(player.getInventory().getKinahCount() >= removeKinahCount && player.getInventory().getItemCountByItemId(186000038) > 0)
						{
							if(qs == null)
							{
								qs = new QuestState(questId, QuestStatus.REWARD, 0, 0);
								player.getQuestStateList().addQuest(questId, qs);
							}
							else
								qs.setStatus(QuestStatus.REWARD);
							qs.setQuestVarById(1, var);
							
							return sendQuestDialog(env, var + 5);
						}
						else
							return sendQuestDialog(env, 1009);
				}
			}
		}
		
		if(qs == null)
			return false;
		
		return defaultQuestRewardDialog(env, 205181, 0, var);
	}

	@Override
	public HandlerResult onBonusApplyEvent(QuestCookie env, int index, AbstractInventoryBonus bonus)
	{
		if(!(bonus instanceof RedeemBonus))
			return HandlerResult.UNKNOWN;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null && qs.getStatus() == QuestStatus.REWARD)
		{
			if(index == 0 && qs.getQuestVarById(1) == 0 ||
				index == 1 && qs.getQuestVarById(1) == 1 ||
				index == 2 && qs.getQuestVarById(1) == 2)
				return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
	}
}
