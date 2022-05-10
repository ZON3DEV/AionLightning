/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.model.gameobjects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.ai.npcai.AggressiveAi;
import org.openaion.gameserver.ai.npcai.NpcAi;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.configs.main.DropConfig;
import org.openaion.gameserver.configs.main.NpcMovementConfig;
import org.openaion.gameserver.controllers.NpcController;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.NpcType;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.ShoutEventType;
import org.openaion.gameserver.model.drop.DropTemplate;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.stats.NpcGameStats;
import org.openaion.gameserver.model.gameobjects.stats.NpcLifeStats;
import org.openaion.gameserver.model.templates.NpcTemplate;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.model.templates.bonus.InventoryBonusType;
import org.openaion.gameserver.model.templates.item.ItemRace;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.npcskill.NpcSkillList;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.model.templates.stats.NpcRank;
import org.openaion.gameserver.services.NpcShoutsService;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.KnownList;
import org.openaion.gameserver.world.NpcKnownList;
import org.openaion.gameserver.world.WorldPosition;
import org.openaion.gameserver.world.WorldType;


/**
 * This class is a base class for all in-game NPCs, what includes: monsters and npcs that player can talk to (aka
 * Citizens)
 * 
 * @author Luno
 * 
 */
public class Npc extends Creature
{
	
	private NpcSkillList npcSkillList;
	public double lastShoutedSeconds;
	
	private ScheduledFuture<?> shoutThread;
	
	/**
	 * Constructor creating instance of Npc.
	 * 
	 * @param spawn
	 *            SpawnTemplate which is used to spawn this npc
	 * @param objId
	 *            unique objId
	 */
	public Npc(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate, new WorldPosition());
		controller.setOwner(this);
		
		super.setGameStats(new NpcGameStats(this));
		super.setLifeStats(new NpcLifeStats(this));
		lastShoutedSeconds = System.currentTimeMillis() / 1000;
		
		final Npc npc = this;
		
		if(NpcShoutsService.getInstance().hasShouts(npc.getNpcId(), ShoutEventType.IDLE))
		{
			shoutThread = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){
				
				@Override
				public void run()
				{
					NpcShoutsService.getInstance().handleEvent(npc, npc, ShoutEventType.IDLE);
				}
			}, Rnd.get(0, 180000), Rnd.get(175000, 185000));
		}
		
	}

	@Override
	public NpcTemplate getObjectTemplate()
	{
		return (NpcTemplate) objectTemplate;
	}
	@Override
	public String getName()
	{
		return getObjectTemplate().getName();
	}

	public int getNpcId()
	{
		return getObjectTemplate().getTemplateId();
	}

	@Override
	public byte getLevel()
	{
		return getObjectTemplate().getLevel();
	}

	/**
	 * @return the lifeStats
	 */
	@Override
	public NpcLifeStats getLifeStats()
	{
		return (NpcLifeStats) super.getLifeStats();
	}

	/**
	 * @return the gameStats
	 */
	@Override
	public NpcGameStats getGameStats()
	{
		return (NpcGameStats) super.getGameStats();
	}
		
	@Override
	public NpcController getController()
	{
		return (NpcController) super.getController();
	}

	public boolean hasWalkRoutes()
	{
		return getSpawn().getWalkerId() > 0 || (getSpawn().hasRandomWalk() && NpcMovementConfig.ACTIVE_NPC_MOVEMENT);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAggressive()
	{
		String currentTribe = getObjectTemplate().getTribe();
		return DataManager.TRIBE_RELATIONS_DATA.hasAggressiveRelations(currentTribe) || isGuard() || isHostile();
	}
	
	public boolean isHostile()
	{
		String currentTribe = getObjectTemplate().getTribe();
		return DataManager.TRIBE_RELATIONS_DATA.hasHostileRelations(currentTribe);
	}
	
	@Override
	public boolean isAggressiveTo(Creature creature)
	{
		if(creature instanceof Player || creature instanceof Summon)
		{
			if(this.getWorldId() != 300080000 && this.getWorldId() != 300090000 && this.getWorldId() != 300060000
				&& !this.isGuard() && this.getLevel() + 10 <= creature.getLevel())
				return false;
			
			Player player = (Player) (creature instanceof Player ? creature : creature.getMaster());
			if(player.getAdminNeutral())
				return false;
		}

		if(DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(getTribe(), creature.getTribe())
			|| DataManager.TRIBE_RELATIONS_DATA.isHostileRelation(getTribe(), creature.getTribe()))
			return true;
		
		if(creature instanceof Npc && guardAgainst((Npc)creature))
			return true;

		return false;
	}
	
	@Override
	public boolean isAggroFrom(Npc npc)
	{
		return DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getTribe(), getTribe());
	}
	
	@Override
	public boolean isHostileFrom(Npc npc)
	{
		return DataManager.TRIBE_RELATIONS_DATA.isHostileRelation(npc.getTribe(), getTribe());
	}

	@Override
	public boolean isSupportFrom(Npc npc)
	{
		return DataManager.TRIBE_RELATIONS_DATA.isSupportRelation(npc.getTribe(), getTribe());
	}

	/** 
	 * 
	 * @return
	 */
	public boolean isGuard()
	{
		String currentTribe = getTribe();
		return DataManager.TRIBE_RELATIONS_DATA.isGuardDark(currentTribe)
			|| DataManager.TRIBE_RELATIONS_DATA.isGuardLight(currentTribe)
			|| DataManager.TRIBE_RELATIONS_DATA.isGuardDrakan(currentTribe);
	}
	
	@Override
	public String getTribe()
	{
		return this.getObjectTemplate().getTribe();
	}
	
	public int getAggroRange()
	{
		return getObjectTemplate().getAggroRange();
	}
	
	@Override
	public void initializeAi()
	{
		if(isAggressive() && !CustomConfig.DISABLE_MOB_AGGRO)
			this.ai = new AggressiveAi();
		else
			this.ai = new NpcAi();
		ai.setOwner(this);
	}

	/**
	 *  Check whether npc located at initial spawn location
	 *  
	 * @return true or false
	 */
	public boolean isAtSpawnLocation()
	{
		return MathUtil.getDistance(getSpawn().getX(), getSpawn().getY(), getSpawn().getZ(),
			getX(), getY(), getZ()) < 3 ;
	}

	/**
	 * @return the npcSkillList
	 */
	public NpcSkillList getNpcSkillList()
	{
		return npcSkillList;
	}

	/**
	 * @param npcSkillList the npcSkillList to set
	 */
	public void setNpcSkillList(NpcSkillList npcSkillList)
	{
		this.npcSkillList = npcSkillList;
	}
	
	@Override
	protected boolean isEnemyNpc(Npc visibleObject)
	{
		if(this.getObjectTemplate().getNpcType() == NpcType.NEUTRAL || this.getObjectTemplate().getNpcType() == NpcType.ARTIFACT)
			return false;
		
		String ownerTribe = getTribe();
		
		if(ownerTribe.equals(visibleObject.getTribe()))
			return false;

		if((DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(ownerTribe, visibleObject.getTribe())
		|| !DataManager.TRIBE_RELATIONS_DATA.isFriendlyRelation(ownerTribe, visibleObject.getTribe())))
			return true;

		guardAgainst(visibleObject);

		return false;
	}
	
	/**
	 * Represents the action of a guard defending its position
	 * @param npc
	 * @return true if this npc is a guard and the given npc is aggro to their PC race
	 */
	protected boolean guardAgainst(Npc npc)
	{
		if(DataManager.TRIBE_RELATIONS_DATA.isGuardLight(getTribe())
				&& DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getTribe(), "PC"))
			return true;
		else if(DataManager.TRIBE_RELATIONS_DATA.isGuardDark(getTribe())
				&& DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getTribe(), "PC_DARK"))
			return true;

		return false;
	}
	@Override
	protected boolean isEnemyPlayer(Player visibleObject)
	{
		Player player = (Player)visibleObject;
		if (getObjectTemplate().getRace() == player.getCommonData().getRace())
			return false;
		
		return true;//TODO
	}
	
	@Override
	protected boolean isEnemySummon(Summon visibleObject)
	{
		return true;//TODO
	}
	
	@Override
	protected boolean canSeeNpc(Npc npc)
	{
		return true; //TODO
	}

	@Override
	protected boolean canSeePlayer(Player player)
	{
		if(!player.isInState(CreatureState.ACTIVE))
			return false;
		
		if (player.getVisualState() == 1 && getObjectTemplate().getRank() == NpcRank.NORMAL)
		   return false;
		
		if (player.getVisualState() == 2 && (getObjectTemplate().getRank() == NpcRank.ELITE || getObjectTemplate().getRank() == NpcRank.NORMAL))
		   return false;
		
		if (player.getVisualState() >= 3)
		   return false;
		
		return true;
	}
	
	@Override
	public void setKnownlist(KnownList knownList)
	{
		if(knownList != null && !(knownList instanceof NpcKnownList))
		{
			throw new RuntimeException("Invalid knownlist "+knownList.getClass().getSimpleName()+" for "+getClass().getSimpleName());
		}
		super.setKnownlist(knownList);
	}
	
	@Override
	public NpcKnownList getKnownList()
	{
		return (NpcKnownList)super.getKnownList();
	}
	
	public Set<DropTemplate> getWorldDrops(Player player)
	{
		NpcTemplate template = this.getObjectTemplate();
		InventoryBonusType dropType = InventoryBonusType.NONE;
		
		// Just simulating item drops by their race
		if (template.getRace() == Race.ASMODIANS)
			dropType = InventoryBonusType.WORLD_DROP_A;
		else if (template.getRace() == Race.ELYOS)
			dropType = InventoryBonusType.WORLD_DROP_E;
		else if (template.getRace() == Race.BEAST ||
				 template.getRace() == Race.DEMIHUMANOID ||
				 template.getRace() == Race.DRAKAN ||
				 template.getRace() == Race.BROWNIE ||
				 template.getRace() == Race.LIZARDMAN ||
				 template.getRace() == Race.MAGICALMONSTER ||
				 template.getRace() == Race.NAGA ||
				 template.getRace() == Race.UNDEAD ||
				 template.getRace() == Race.LYCAN)
		{
			if (this.getWorldType() == WorldType.BALAUREA || this.getWorldType() == WorldType.ABYSS)
				dropType = InventoryBonusType.WORLD_DROP_B;
			else if (this.getWorldType() == WorldType.ASMODAE)
				dropType = InventoryBonusType.WORLD_DROP_A;
			else if (this.getWorldType() == WorldType.ELYSEA)
				dropType = InventoryBonusType.WORLD_DROP_E;
			else if (template.getLevel() >= 50)
				dropType = InventoryBonusType.WORLD_DROP_B;
			else
				return null; // nothing to drop
		}
		else
			return null; // nothing to drop
		
		int startLevel = template.getLevel() / 10 * 10;
		List<Integer> itemIds = DataManager.ITEM_DATA.getBonusItems(dropType, startLevel, startLevel + 5);

		if (itemIds.size() == 0)
			return null;
		
		Set<DropTemplate> dropTemplates = new HashSet<DropTemplate>();
		int itemId = itemIds.get(Rnd.get(itemIds.size()));
		
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		
		// check just in case the item race (world drops are for both races)
		if (itemTemplate.getRace() != ItemRace.ALL)
		{
			if (!player.getCommonData().getRace().toString().equals(itemTemplate.getRace().toString()))
				return null;
		}

		float chance = 0;
		switch (itemTemplate.getItemQuality())
		{
			case COMMON:
				chance = DropConfig.WORLD_DROP_CHANCE_COMMON;
				break;
			case RARE:
				chance = DropConfig.WORLD_DROP_CHANCE_RARE;
				break;
			case LEGEND:
				chance = DropConfig.WORLD_DROP_CHANCE_LEGENDARY;
				break;
			case UNIQUE:
				chance = DropConfig.WORLD_DROP_CHANCE_UNIQUE;
		}
		
		dropTemplates.add(new DropTemplate(this.getNpcId(), itemId, 1, 1, chance));
		
		return dropTemplates;
	}
	
	public boolean mayShout()
	{
		return ((System.currentTimeMillis() / 1000) - lastShoutedSeconds) > 16;
	}
	
	public void shout()
	{
		lastShoutedSeconds = System.currentTimeMillis() / 1000;
	}
	
	public void stopShoutThread()
	{
		if(shoutThread != null)
			shoutThread.cancel(false);
	}
	
}
