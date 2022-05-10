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
package org.openaion.gameserver.quest;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectHashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.openaion.commons.scripting.scriptmanager.ScriptManager;
import org.openaion.gameserver.GameServerError;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.QuestScriptsData;
import org.openaion.gameserver.dataholders.QuestsData;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.flyring.FlyRing;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.QuestTemplate;
import org.openaion.gameserver.model.templates.bonus.AbstractInventoryBonus;
import org.openaion.gameserver.model.templates.bonus.InventoryBonusType;
import org.openaion.gameserver.model.templates.quest.CollectItem;
import org.openaion.gameserver.model.templates.quest.CollectItems;
import org.openaion.gameserver.model.templates.quest.NpcQuestData;
import org.openaion.gameserver.model.templates.quest.QuestDrop;
import org.openaion.gameserver.model.templates.quest.QuestItems;
import org.openaion.gameserver.model.templates.quest.QuestWorkItems;
import org.openaion.gameserver.quest.handlers.QuestHandler;
import org.openaion.gameserver.quest.handlers.QuestHandlerLoader;
import org.openaion.gameserver.quest.handlers.models.QuestScriptData;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.quest.model.QuestState;
import org.openaion.gameserver.quest.model.QuestStatus;
import org.openaion.gameserver.services.QuestService;
import org.openaion.gameserver.world.zone.ZoneName;


/**
 * @author MrPoke
 * 
 */
public class QuestEngine
{
	private static final Logger							log						= Logger.getLogger(QuestEngine.class);

	private static final FastMap<Integer, QuestHandler>	questHandlers			= new FastMap<Integer, QuestHandler>();

	private static ScriptManager						scriptManager			= new ScriptManager();

	public static final File							QUEST_DESCRIPTOR_FILE	= new File(
	"./data/scripts/system/quest_handlers.xml");

	private QuestsData						questData = DataManager.QUEST_DATA;

	private QuestScriptsData					questScriptsData = DataManager.QUEST_SCRIPTS_DATA;

	private TIntObjectHashMap<NpcQuestData>				npcQuestData = new TIntObjectHashMap<NpcQuestData>();
	private TIntObjectHashMap<TIntArrayList>			questItemIds= new TIntObjectHashMap<TIntArrayList>();
	private TIntObjectHashMap<TIntArrayList>			questSellBuyItemIds= new TIntObjectHashMap<TIntArrayList>();
	private TIntArrayList						questLvlUp = new TIntArrayList();
	private FastMap<ZoneName, TIntArrayList>			questEnterZone= new FastMap<ZoneName, TIntArrayList>();
	private FastMap<Integer, TIntArrayList>				questOnSkillUse= new FastMap<Integer, TIntArrayList>();
	private TIntObjectHashMap<TIntArrayList>			questMovieEndIds= new TIntObjectHashMap<TIntArrayList>();
	private TIntArrayList						questOnDie= new TIntArrayList();
	private TIntArrayList						questOnKillPlayer= new TIntArrayList();
	private TIntArrayList						questOnEnterWorld= new TIntArrayList();
	private TIntObjectHashMap<List<QuestDrop>>			questDrop= new TIntObjectHashMap<List<QuestDrop>>();
	private TIntArrayList							questOnQuestFinish= new TIntArrayList();
	private List<Integer>							questOnQuestTimerEnd= new ArrayList<Integer>();
	private TIntArrayList							questOnQuestAbort= new TIntArrayList();
	private FastMap<InventoryBonusType, TIntArrayList>              questBonusTypes= new FastMap<InventoryBonusType, TIntArrayList>();
	private TIntObjectHashMap<TIntArrayList>			collectItems = new TIntObjectHashMap<TIntArrayList>();
	private TIntArrayList                                           questOnFlyThroughRing= new TIntArrayList();

	private final NpcQuestData 							emptyNpcQuestData 	= new NpcQuestData();

	public static final QuestEngine getInstance()
	{
		return SingletonHolder.instance;
	}

	// Constructor
	private QuestEngine()
	{
		log.info("Initializing QuestEngine");
	}

	public FastMap<Integer, QuestHandler> TEMP_HANDLERS = new FastMap<Integer, QuestHandler>();

	public void load(boolean reload)
	{
		ScriptManager tempScriptManager = new ScriptManager();
		tempScriptManager.setGlobalClassListener(new QuestHandlerLoader());

		try
		{
			tempScriptManager.load(QUEST_DESCRIPTOR_FILE);
		}
		catch (Exception e)
		{
			throw new GameServerError("Can't initialize quest handlers.", e);
		}

		if(reload)
		{
			scriptManager.shutdown();
			clear();
			scriptManager = null;
			questHandlers.clear();
		}

		scriptManager = tempScriptManager;

		for (QuestTemplate data : questData.getQuestsData())
		{
			for (QuestDrop drop : data.getQuestDrop())
			{
				drop.setQuestId(data.getId());
				setQuestDrop(drop.getNpcId()).add(drop);
			}
			if (data.getCollectItems() != null)
			{
				for (CollectItem ci : data.getCollectItems().getCollectItem())
				{
					setQuestsForCollectItem(ci.getItemId()).add(data.getId());
				}
			}
		}

		for (QuestScriptData data : questScriptsData.getData())
		{
			data.register(this);
		}

		for(Entry<Integer, QuestHandler> questHEntry : TEMP_HANDLERS.entrySet())
		{
			questHEntry.getValue().register();
			if (questHandlers.containsKey(questHEntry.getValue().getQuestId()))
				log.warn("Duplicate quest: "+questHEntry.getValue().getQuestId());
			questHandlers.put(questHEntry.getValue().getQuestId(), questHEntry.getValue());
		}

		TEMP_HANDLERS.clear();

		log.info((reload ? "Reloaded " : "Loaded ") + questHandlers.size() + " quest handlers.");
	}

	public void shutdown()
	{
		scriptManager.shutdown();
		clear();
		scriptManager = null;
		log.info("Quests are shutdown...");
	}
	
	public boolean onDialog(QuestCookie env)
	{
		QuestHandler questHandler = null;
		if(env.getQuestId() != 0)
		{
			questHandler = getQuestHandlerByQuestId(env.getQuestId());
			if(questHandler != null)
			{
				if(questHandler.onDialogEvent(env))
					return true;
			}
		}
		else
		{
			Npc npc = (Npc) env.getVisibleObject();
			for(int questId : getNpcQuestData(npc == null ? 0 : npc.getNpcId()).getOnTalkEvent())
			{
				questHandler = getQuestHandlerByQuestId(questId);
				if(questHandler != null)
				{
					env.setQuestId(questId);
					if(questHandler.onDialogEvent(env))
						return true;
				}
			}
		}
		return false;
	}

	public boolean onKill(QuestCookie env)
	{
		if(env.getVisibleObject() instanceof Player)
		{
			for (int index=0 ; index<questOnKillPlayer.size(); index++)
			{
				QuestHandler questHandler = getQuestHandlerByQuestId(questOnKillPlayer.get(index));
				if(questHandler != null)
				{
					if(questHandler.onKillEvent(env))
						return true;
				}
			}
		}
		if(env.getVisibleObject() instanceof Npc)
		{
			Npc npc = (Npc) env.getVisibleObject();
			for(int questId : getNpcQuestData(npc.getNpcId()).getOnKillEvent())
			{
				QuestHandler questHandler = getQuestHandlerByQuestId(questId);
				if(questHandler != null)
				{
					if(questHandler.onKillEvent(env))
						return true;
				}
			}
		}
		return false;
	}

	public boolean onAttack(QuestCookie env)
	{
		Npc npc = (Npc) env.getVisibleObject();

		for(int questId : getNpcQuestData(npc.getNpcId()).getOnAttackEvent())
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(questId);
			if(questHandler != null)
			{
				if(questHandler.onAttackEvent(env))
					return true;
			}
		}
		return false;
	}

	public HandlerResult onFlyThroughRing(QuestCookie env, FlyRing ring)
	{
		for (int index=0 ; index<questOnFlyThroughRing.size(); index++)
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(questOnFlyThroughRing.get(index));
			if(questHandler != null)
			{
				HandlerResult result = questHandler.onFlyThroughRingEvent(env, ring);
				if (result != HandlerResult.UNKNOWN)
					return result;
			}
		}
		return HandlerResult.UNKNOWN;
	}

	public boolean onActionItem(QuestCookie env)
	{
		Npc npc = (Npc) env.getVisibleObject();

		for(int questId : getNpcQuestData(npc.getNpcId()).getOnActionItemEvent())
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(questId);
			if(questHandler != null)
				if(questHandler.onActionItemEvent(env))
					return true;
		}
		return false;
	}

	public void onLvlUp(QuestCookie env)
	{
		for (int index=0 ; index<questLvlUp.size(); index++)
		{
			int questId = questLvlUp.get(index);
			QuestHandler questHandler = getQuestHandlerByQuestId(questId);
			if(questHandler != null)
			{				
				env.setQuestId(questId);
				questHandler.onLvlUpEvent(env);
			}
		}
	}

	public void onDie(QuestCookie env)
	{
		for (int index=0 ; index<questOnDie.size(); index++)
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(questOnDie.get(index));
			if(questHandler != null)
			{
				questHandler.onDieEvent(env);
			}
		}
	}

	public void onEnterWorld(QuestCookie env)
	{
		for (int index=0 ; index<questOnEnterWorld.size(); index++)
		{
			int questId = questOnEnterWorld.get(index);
			QuestHandler questHandler = getQuestHandlerByQuestId(questId);
			if(questHandler != null)
			{
				env.setQuestId(questId);
				questHandler.onEnterWorldEvent(env);
			}			
		}
	}

	public HandlerResult onItemUseEvent(QuestCookie env, Item item)
	{
		TIntArrayList lists = getQuestItemIds(item.getItemTemplate().getTemplateId());
		for (int index=0; index<lists.size(); index++)
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(lists.get(index));
			if (questHandler != null)
			{
				HandlerResult result = questHandler.onItemUseEvent(env, item);
				if (result != HandlerResult.UNKNOWN)
					return result;
			}
		}
		return HandlerResult.UNKNOWN;
	}

	public boolean onItemSellBuyEvent(QuestCookie env, int itemId)
	{
		TIntArrayList lists = getQuestSellBuyItemIds(itemId);
		for (int index=0 ; index<lists.size(); index++)
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(lists.get(index));
			if(questHandler != null)
			{
				if(questHandler.onItemSellBuyEvent(env, itemId))
					return true;
			}
		}
		return false;
	}

	public boolean onEnterZone(QuestCookie env, ZoneName zoneName)
	{
		TIntArrayList lists = getQuestEnterZone(zoneName);
		for (int index=0 ; index<lists.size(); index++)
		{
			int questId = lists.get(index);
			QuestHandler questHandler = getQuestHandlerByQuestId(questId);
			if(questHandler != null)
			{
				env.setQuestId(questId);
				if(questHandler.onEnterZoneEvent(env, zoneName))
					return true;
			}
		}
		return false;
	}

	public boolean onMovieEnd(QuestCookie env, int movieId)
	{
		TIntArrayList lists = getQuestMovieEndIds(movieId);
		for (int index=0 ; index<lists.size(); index++)
		{
			int questId = lists.get(index);
			QuestHandler questHandler = getQuestHandlerByQuestId(questId);
			if(questHandler != null)
			{
				env.setQuestId(questId);
				if(questHandler.onMovieEndEvent(env, movieId))
					return true;
			}
		}
		return false;
	}

	public void onQuestFinish(QuestCookie env)
	{
		for (int index=0 ; index<questOnQuestFinish.size(); index++)
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(questOnQuestFinish.get(index));
			if(questHandler != null)
			{
				questHandler.onQuestFinishEvent(env);
			}
		}
	}

	public void onQuestAbort(QuestCookie env)
	{
		for (int index=0 ; index<questOnQuestAbort.size(); index++)
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(questOnQuestFinish.get(index));
			if(questHandler != null)
			{
				questHandler.onQuestAbortEvent(env);
			}
		}
	}

	public void onQuestTimerEnd(QuestCookie env)
	{
		for(int questId : questOnQuestTimerEnd)
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(questId);
			if(questHandler != null)
			{
				questHandler.onQuestTimerEndEvent(env);
			}
		}
	}

	public HandlerResult onBonusApply(QuestCookie env, int index, AbstractInventoryBonus bonus)
	{
		TIntArrayList lists = getQuestBonusType(bonus.getType());

		for (int i = 0; i < lists.size(); i++)
		{
			int questId = lists.get(i);
			if(questId != env.getQuestId())
				continue;
			QuestHandler questHandler = getQuestHandlerByQuestId(lists.get(i));
			if(questHandler != null)
				return questHandler.onBonusApplyEvent(env, index, bonus);
		}
		return HandlerResult.UNKNOWN;
	}

	public boolean deleteQuest(Player player, int questId)
	{
		QuestTemplate template = questData.getQuestById(questId);

		if(template.isCannotGiveup())
			return false;

		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(qs == null)
			return false;

		qs.setStatus(QuestStatus.NONE);

		this.onQuestAbort(new QuestCookie(null, player, questId, 0));
		//remove all worker list item if abandoned
		QuestWorkItems qwi = template.getQuestWorkItems();

		if(qwi != null)
		{
			long count = 0;
			for(QuestItems qi : qwi.getQuestWorkItem())
			{
				if(qi != null)
				{	
					count = player.getInventory().getItemCountByItemId(qi.getItemId());
					if(count > 0)
					{
						if(!player.getInventory().removeFromBagByItemId(qi.getItemId(), count))
							return false;
					}
				}
			}
		}

		//remove all collect list item if abandoned the work order
		if(template.getCombineSkill() != null)
		{
			CollectItems collectItems = template.getCollectItems();
			for (CollectItem collectItem : collectItems.getCollectItem())
				player.getInventory().removeFromBagByItemId(collectItem.getItemId(), player.getInventory().getItemCountByItemId(collectItem.getItemId()));
		}
		if(player.getController().getTask(TaskId.QUEST_TIMER) != null)
			QuestService.questTimerEnd(new QuestCookie(null, player, questId, 0));
		return true;
	}

	public NpcQuestData getNpcQuestData(int npcTemplateId)
	{
		if(npcQuestData.containsKey(npcTemplateId))
		{
			return npcQuestData.get(npcTemplateId);
		}
		return emptyNpcQuestData;

	}
	public NpcQuestData setNpcQuestData(int npcTemplateId)
	{
		if(!npcQuestData.containsKey(npcTemplateId))
		{
			npcQuestData.put(npcTemplateId, new NpcQuestData());
		}
		return npcQuestData.get(npcTemplateId);
	}

	public TIntArrayList getQuestItemIds(int itemId)
	{
		if(questItemIds.containsKey(itemId))
		{
			return questItemIds.get(itemId);
		}
		return new TIntArrayList();
	}

	public TIntArrayList setQuestItemIds(int itemId)
	{
		if(!questItemIds.containsKey(itemId))
		{
			questItemIds.put(itemId, new TIntArrayList());
		}
		return questItemIds.get(itemId);
	}

	public TIntArrayList getQuestsForCollectItem(int itemId)
	{
		if(collectItems.containsKey(itemId))
		{
			return collectItems.get(itemId);
		}
		return new TIntArrayList();
	}

	public TIntArrayList setQuestsForCollectItem(int itemId)
	{
		if(!collectItems.containsKey(itemId))
		{
			collectItems.put(itemId, new TIntArrayList());
		}
		return collectItems.get(itemId);
	}

	public TIntArrayList getQuestSellBuyItemIds(int itemId)
	{
		if(questSellBuyItemIds.containsKey(itemId))
		{
			return questSellBuyItemIds.get(itemId);
		}
		return new TIntArrayList();
	}

	public TIntArrayList setQuestSellBuyItemIds(int itemId)
	{
		if(!questSellBuyItemIds.containsKey(itemId))
		{
			questSellBuyItemIds.put(itemId, new TIntArrayList());
		}
		return questSellBuyItemIds.get(itemId);
	}

	public List<QuestDrop> setQuestDrop(int npcId)
	{
		if(!questDrop.containsKey(npcId))
		{
			questDrop.put(npcId, new ArrayList<QuestDrop>());
		}
		return questDrop.get(npcId);
	}

	public List<QuestDrop> getQuestDrop(int npcId)
	{
		if(questDrop.containsKey(npcId))
		{
			return questDrop.get(npcId);
		}
		return new ArrayList<QuestDrop>();
	}

	public void addQuestLvlUp(int questId)
	{
		if(!questLvlUp.contains(questId))
			questLvlUp.add(questId);
	}

	public void addOnEnterWorld(int questId)
	{
		if(!questOnEnterWorld.contains(questId))
			questOnEnterWorld.add(questId);
	}

	public void addOnDie(int questId)
	{
		if(!questOnDie.contains(questId))
			questOnDie.add(questId);
	}

	public void addOnFlyThroughRing(int questId)
	{
		if(!questOnFlyThroughRing.contains(questId))
			questOnFlyThroughRing.add(questId);
	}

	public void addOnKillPlayer(int questId)
	{
		if(!questOnKillPlayer.contains(questId))
			questOnKillPlayer.add(questId);
	}
	
	public TIntArrayList getQuestEnterZone(ZoneName zoneName)
	{
		if(questEnterZone.containsKey(zoneName))
		{
			return questEnterZone.get(zoneName);
		}
		return new TIntArrayList();
	}

	public TIntArrayList getQuestSkillIds(int skillId)
	{
		if(questOnSkillUse.containsKey(skillId))
		{
			return questOnSkillUse.get(skillId);
		}
		return new TIntArrayList();
	}

	public TIntArrayList setQuestSkillIds(int skillId)
	{
		if(!questOnSkillUse.containsKey(skillId))
		{
			questOnSkillUse.put(skillId, new TIntArrayList());
		}
		return questOnSkillUse.get(skillId);
	}

	public TIntArrayList setQuestEnterZone(ZoneName zoneName)
	{
		if(!questEnterZone.containsKey(zoneName))
		{
			questEnterZone.put(zoneName, new TIntArrayList());
		}
		return questEnterZone.get(zoneName);
	}

	public TIntArrayList getQuestMovieEndIds(int moveId)
	{
		if(questMovieEndIds.containsKey(moveId))
		{
			return questMovieEndIds.get(moveId);
		}
		return new TIntArrayList();
	}

	public TIntArrayList setQuestMovieEndIds(int moveId)
	{
		if(!questMovieEndIds.containsKey(moveId))
		{
			questMovieEndIds.put(moveId, new TIntArrayList());
		}
		return questMovieEndIds.get(moveId);
	}

	public TIntArrayList getQuestBonusType(InventoryBonusType bonusType)
	{
		if(questBonusTypes.containsKey(bonusType))
		{
			return questBonusTypes.get(bonusType);
		}
		return new TIntArrayList();
	}

	public TIntArrayList setQuestBonusType(InventoryBonusType bonusType)
	{
		if(!questBonusTypes.containsKey(bonusType))
		{
			questBonusTypes.put(bonusType, new TIntArrayList());
		}
		return questBonusTypes.get(bonusType);
	}

	public void addOnQuestFinish(int questId)
	{
		if(!questOnQuestFinish.contains(questId))
			questOnQuestFinish.add(questId);
	}

	public void addOnQuestAbort(int questId)
	{
		if(!questOnQuestAbort.contains(questId))
			questOnQuestAbort.add(questId);
	}

	public void addOnQuestTimerEnd(int questId)
	{
		if(!questOnQuestTimerEnd.contains(questId))
			questOnQuestTimerEnd.add(questId);
	}

	public boolean onSkillUse(QuestCookie env, int skillId)
	{
		TIntArrayList lists = getQuestSkillIds(skillId);
		for (int index=0 ; index<lists.size(); index++)
		{
			QuestHandler questHandler = getQuestHandlerByQuestId(lists.get(index));
			if(questHandler != null)
				if(questHandler.onSkillUseEvent(env, skillId))
					return true;
		}
		return false;
	}

	public void clear()
	{
		npcQuestData.clear();
		questItemIds.clear();
		questSellBuyItemIds.clear();
		questLvlUp.clear();
		questOnEnterWorld.clear();
		questOnDie.clear();
		questOnKillPlayer.clear();
		questEnterZone.clear();
		questMovieEndIds.clear();
		questDrop.clear();
		questOnQuestFinish.clear();
		questOnQuestAbort.clear();
		questOnQuestTimerEnd.clear();
		questBonusTypes.clear();
		questHandlers.clear();
		questOnSkillUse.clear();
		questOnFlyThroughRing.clear();
		collectItems.clear();
	}

	public void addQuestHandler (QuestHandler questHandler)
	{

	}

	private QuestHandler getQuestHandlerByQuestId(int questId)
	{
		return questHandlers.get(questId);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final QuestEngine instance = new QuestEngine();
	}
}