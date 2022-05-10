package quest.verteron;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import org.openaion.gameserver.quest.HandlerResult;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.services.ZoneService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.zone.ZoneName;


/**
 * @author Mr. Poke
 *
 */
public class _1019FlyingReconnaissance extends QuestHandler
{

	private final static int	questId	= 1019;
	private final static int[]	npc_ids	= {203146, 203098, 203147, 700037};

	public _1019FlyingReconnaissance()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setQuestMovieEndIds(22).add(questId);	
		qe.setQuestItemIds(182200023).add(questId);
		qe.setQuestItemIds(182200505).add(questId);
		qe.setNpcQuestData(210158).addOnAttackEvent(questId);
		qe.setNpcQuestData(210697).addOnKillEvent(questId);
		qe.setNpcQuestData(216891).addOnKillEvent(questId);
		for(int id: npc_ids)
			qe.setNpcQuestData(id).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch (env.getTargetId())
			{
				case 203146:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1, 182200505, 1, 0, 0);
					}
					break;
				case 203098:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1352);
						case 10001:
							return defaultCloseDialog(env, 2, 3);
					}
					break;
				case 203147:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 1438);
							else if (var == 5)
								return sendQuestDialog(env, 1693);
						case 10002:
							return defaultCloseDialog(env, 3, 4);
						case 10003:
							return defaultCloseDialog(env, 5, 6, 182200023, 1, 0, 0);
					}
					break;
				case 700037:
					if(env.getDialogId() == -1)
						return (defaultQuestUseNpc(env, 6, 9, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true));
					break;
			}
		}
		return defaultQuestRewardDialog(env, 203098, 2034);
	}

	@Override
	public boolean onAttackEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 4)
			return false;
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(targetId != 210158)
			return false;
		Creature creature = (Creature) env.getVisibleObject();
		if(creature.getLifeStats().getCurrentHp() < creature.getLifeStats().getMaxHp() / 3)
		{
			defaultQuestMovie(env, 22);
			creature.getLifeStats().reduceHp(creature.getLifeStats().getCurrentHp() + 1, creature);
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestCookie env, int movieId)
	{
		if(movieId != 22)
			return false;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		qs.setQuestVar(5);
		updateQuestStatus(env);
		TeleportService.teleportToNpc(player, 203147);
		return true;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if(qs == null)
			return HandlerResult.UNKNOWN;

		if(id == 182200023)
		{
			if(!ZoneService.getInstance().isInsideZone(player, ZoneName.TURSIN_TOTEM_POLE))
				return HandlerResult.FAILED;

			if (qs.getQuestVarById(0) != 9)
				return HandlerResult.FAILED;
			
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					player.getInventory().removeFromBagByObjectId(itemObjId, 1);
					qs.setQuestVarById(0, 10);
					updateQuestStatus(env);
				}
			}, 3000);
			return HandlerResult.SUCCESS;
		}
		else if(id == 182200505)
		{
			if(!ZoneService.getInstance().isInsideZone(player, ZoneName.TURSIN_OUTPOST_ENTRANCE))
				return HandlerResult.FAILED;

			if(qs.getQuestVarById(0) != 1)
				return HandlerResult.FAILED;
			
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					defaultQuestMovie(env, 18);
					qs.setQuestVar(2);
					updateQuestStatus(env);
					player.getInventory().removeFromBagByItemId(182200505, 1);
				}
			}, 3000);
			return HandlerResult.SUCCESS;
		}
		
		return HandlerResult.UNKNOWN;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 210697, 10, true) || defaultQuestOnKillEvent(env, 216891, 10, true))
			return true;
		else
			return false;
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}
	

	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		qs.setQuestVarById(0, var + 1);
		updateQuestStatus(env);
	}
}
