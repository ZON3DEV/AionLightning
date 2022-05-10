package quest.reshanta;

import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.zone.ZoneName;


public class _2076TheShadowSummons extends QuestHandler
{

	private final static int	questId	= 2076;
	private final static int[]	npc_ids	= { 798300, 204253, 204089, 700368, 700369, 203550 };
	
	public _2076TheShadowSummons()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.addOnEnterWorld(questId);
		qe.setNpcQuestData(700368).addOnActionItemEvent(questId);
		qe.setNpcQuestData(700369).addOnActionItemEvent(questId);
		qe.setQuestEnterZone(ZoneName.SHADOW_COURT_DUNGEON_320120000).add(questId);
		qe.setQuestMovieEndIds(133).add(questId);
		qe.setQuestMovieEndIds(423).add(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		int[] quests = {2071, 2072, 2073, 2074, 2075};
		return defaultQuestOnLvlUpEvent(env, quests, true);
	}
	
	@Override
	public boolean onActionItemEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		return (env.getTargetId() == 700368 && var == 4 || env.getTargetId() == 700369 && var == 5);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		switch (env.getTargetId())
		{
			case 798300:
				switch (env.getDialogId())
				{
					case 26:
						if (var == 0)
							return sendQuestDialog(env, 1011);
						break;
					case 10000:
						return defaultCloseDialog(env, 0, 1);
				}
			case 204253:
				switch (env.getDialogId())
				{
					case 26:
						if (var == 2)
							return sendQuestDialog(env, 1693);
						break;
					case 10002:
						return defaultCloseDialog(env, 2, 3);
				}
			case 204089:
				switch (env.getDialogId())
				{
					case 26:
						if (var == 3)
							return sendQuestDialog(env, 2034);
						else if (var == 6)
							return sendQuestDialog(env, 3057);
						break;
					case 10003:
						return defaultCloseDialog(env, 3, 4);
					case 10255:
						return defaultCloseDialog(env, 6, 6, true, false);
				}
			case 700368:
				if (env.getDialogId() == -1 && var == 4)
				{
					return defaultQuestUseNpc(env, 4, 5, EmotionType.NEUTRALMODE2, 
						EmotionType.START_LOOT, false);
				}
			case 700369:
				if (env.getDialogId() == -1 && var == 5)
				{
					return defaultQuestUseNpc(env, 5, 6, EmotionType.NEUTRALMODE2, 
						EmotionType.START_LOOT, false);
				}
		}
		
		return defaultQuestRewardDialog(env, 203550, 10002);
	}
	
	@Override
	public void QuestUseNpcInsideFunction(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return;
		
		int var = qs.getQuestVarById(0);
		if (var == 4)
		{
			defaultQuestMovie(env, 133);
		}
		else if (var == 5)
		{
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					TeleportService.teleportTo(player, 120010000, 1, 982, 1556, 210, (byte)90, 500);
					qs.setQuestVar(6);
					updateQuestStatus(env);
				}
			}, 2000);
		}
	}
	
	@Override
	public boolean onMovieEndEvent(final QuestCookie env, final int movieId)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || movieId != 133 && movieId != 423)
			return false;
		
		int var = qs.getQuestVarById(0);
		if (var == 4)
		{
			if (movieId == 423)
			{
				qs.setQuestVar(5);
				updateQuestStatus(env);
				return defaultQuestRemoveItem(env, 182205502, 1);
			}
			
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					TeleportService.teleportTo(player, 320120000, 1, 591.6565f, 399.28558f, 206.62482f, (byte)90, 500);
				}
			}, 2000);
		}
		return true;
	}
	
	@Override
    public boolean onEnterZoneEvent(final QuestCookie env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (zoneName != ZoneName.SHADOW_COURT_DUNGEON_320120000)
            return false;
        if (qs == null)
            return false;
        
        int var = qs.getQuestVarById(0);
		if (var == 4)
		{
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					defaultQuestMovie(env, 423);
				}
			}, 2000);
		}
        return true;
    }
	
	@Override
	public boolean onEnterWorldEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		
		if (var == 1 && defaultQuestGiveItem(env, 182205502, 1))
		{
			qs.setQuestVar(2);
			updateQuestStatus(env);
			return true;
		}
		
		return false;
	}

}
