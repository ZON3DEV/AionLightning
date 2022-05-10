package org.openaion.gameserver.model.gameobjects.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.SkillElement;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.id.ItemStatEffectId;
import org.openaion.gameserver.model.gameobjects.stats.id.SkillEffectId;
import org.openaion.gameserver.model.gameobjects.stats.id.StatEffectId;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.StatModifier;
import org.openaion.gameserver.model.items.ItemSlot;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author blakawk
 * 
 */
public class CreatureGameStats<T extends Creature>
{
	protected static final Logger							log					= Logger
																					.getLogger(CreatureGameStats.class);

	private static final int								ATTACK_MAX_COUNTER	= Integer.MAX_VALUE;

	protected Map<StatEnum, Stat>							stats;
	protected Map<StatEffectId, TreeSet<StatModifier>>		statsModifiers;

	private int												attackCounter		= 0;
	protected T												owner				= null;
	
	protected Map<StatEnum, Integer> 						summonStats;
	
	protected AtomicBoolean recomputing = new AtomicBoolean();

	/**
	 * 
	 * @param owner
	 */
	protected CreatureGameStats(T owner)
	{
		this.owner = owner;
		this.stats = Collections.synchronizedMap(new HashMap<StatEnum,Stat>());
		this.summonStats = new HashMap<StatEnum,Integer>();
		this.statsModifiers = Collections.synchronizedMap(new HashMap<StatEffectId, TreeSet<StatModifier>>());
	}
	
	/**
	 * @return the atcount
	 */
	public int getAttackCounter()
	{
		return attackCounter;
	}

	/**
	 * @param atcount
	 *            the atcount to set
	 */
	protected void setAttackCounter(int attackCounter)
	{
		if(attackCounter <= 0)
		{
			this.attackCounter = 1;
		}
		else
		{
			this.attackCounter = attackCounter;
		}
	}
	
	public void increaseAttackCounter()
	{
		if(attackCounter == ATTACK_MAX_COUNTER)
		{
			this.attackCounter = 1;
		}
		else
		{
			this.attackCounter++;
		}
	}
	
	/**
	 * 
	 * @param stat
	 * @param value
	 */
	public void setStat(StatEnum stat, int value)
	{
		setStat(stat,value,false);		
	}

	/**
	 * @param stat
	 * @return
	 */
	public int getBaseStat(StatEnum stat)
	{
		if(stats.containsKey(stat))
			return stats.get(stat).getBase();
		else
			return 0;
	}
	
	/**
	 * 
	 * @param stat
	 * @return
	 */
	public int getStatBonus(StatEnum stat)
	{
		if (stats.containsKey(stat))
			return stats.get(stat).getBonus();
		else
			return 0;
	}
	
	/**
	 * 
	 * @param stat
	 * @return
	 */
	public int getCurrentStat(StatEnum stat)
	{
		if (stats.containsKey(stat))
			return stats.get(stat).getCurrent();
		else
			return 0;
	}

	/**
	 * 
	 * @param stat
	 * @return
	 */
	public int getOldStat(StatEnum stat)
	{
		if (stats.containsKey(stat))
			return stats.get(stat).getOld();
		else
			return 0;
	}

	/**
	 * 
	 * @param id
	 * @param modifiers
	 */
	public synchronized void addModifiers(StatEffectId id, TreeSet<StatModifier> modifiers)
	{
		if (modifiers==null || statsModifiers.containsKey(id))
			return;

		statsModifiers.put(id, modifiers);
		recomputeStats();
	}
	
	/**
	 * @return True if the StatEffectId is already added
	 */
	public synchronized boolean effectAlreadyAdded(StatEffectId id)
	{
		return statsModifiers.containsKey(id);
	}

	/**
	 * Recomputation of all stats
	 */
	public void recomputeStats()
	{
		try 
		{
			// make sure we are not doing it twice
			if (!recomputing.compareAndSet(false, true))
				return;
			
			resetStats();
			Map<StatEnum, StatModifiers> orderedModifiers = new HashMap<StatEnum, StatModifiers>();
				
			synchronized (statsModifiers)
			{
				//sort StatEffectIds according to StatEffectType order
				List<StatEffectId> statEffectIds = new ArrayList<StatEffectId>(statsModifiers.keySet());
		        Collections.sort(statEffectIds);

		        for (StatEffectId eid : statEffectIds)
		        {
					TreeSet<StatModifier> modifiers = statsModifiers.get(eid);
					int slots = 0;

					if(modifiers == null)
						continue;
									
					for(StatModifier modifier : modifiers)
					{
						if(eid instanceof ItemStatEffectId)
						{
							slots = ((ItemStatEffectId) eid).getSlot();
						}
						
						if (slots == 0)
							slots = ItemSlot.NONE.getSlotIdMask();
						
						List<ItemSlot> oSlots = ItemSlot.getSlotsFor(slots);
						for(ItemSlot slot : oSlots)
						{
							StatEnum statToModify = modifier.getStat().getMainOrSubHandStat(slot, false);
							
							if(!orderedModifiers.containsKey(statToModify))
							{
								orderedModifiers.put(statToModify, new StatModifiers());
							}
							orderedModifiers.get(statToModify).add(modifier);
						}
					}
				}
			}
			
			for(Entry<StatEnum, StatModifiers> entry : orderedModifiers.entrySet())
			{
				applyModifiers(entry.getKey(), entry.getValue());
			}

			orderedModifiers.clear();
			
			//apply limits
			applyLimits();
		}
		finally
		{
			recomputing.set(false);
		}
	}
	
	protected void applyLimits()
	{
		int MIN_SPEED = 600;
		int MIN_FLY_SPEED = 600;
		int MIN_ATTACK_SPEED = 600;
		int MIN_RESISTANCE = -90;
		int MIN_BOOST_MAGICAL_SKILL = 0;
		
		StatEnum[] resistances = {StatEnum.FIRE_RESISTANCE,StatEnum.EARTH_RESISTANCE,StatEnum.WIND_RESISTANCE,StatEnum.WATER_RESISTANCE};
		
		int bonus = 0;

		//speed limit
		if (getCurrentStat(StatEnum.SPEED) < MIN_SPEED)
		{
			bonus = this.getBaseStat(StatEnum.SPEED) - MIN_SPEED;
			this.setStat(StatEnum.SPEED, -bonus, true);
			bonus = 0;
		}
		
		
		//fly speed limit
		if (getCurrentStat(StatEnum.FLY_SPEED) < MIN_FLY_SPEED)
		{
			bonus = this.getBaseStat(StatEnum.FLY_SPEED) - MIN_FLY_SPEED;
			this.setStat(StatEnum.FLY_SPEED, -bonus, true);
			bonus = 0;
		}
		
		//attack speed min limit
		if (getCurrentStat(StatEnum.ATTACK_SPEED) < MIN_ATTACK_SPEED)
		{
			bonus = this.getBaseStat(StatEnum.ATTACK_SPEED) - MIN_ATTACK_SPEED;
			this.setStat(StatEnum.ATTACK_SPEED, -bonus, true);
			bonus = 0;
		}
		
		//magic boost limit
		if (getCurrentStat(StatEnum.BOOST_MAGICAL_SKILL) < MIN_BOOST_MAGICAL_SKILL)
		{
			bonus = this.getBaseStat(StatEnum.BOOST_MAGICAL_SKILL) - MIN_BOOST_MAGICAL_SKILL;
			this.setStat(StatEnum.BOOST_MAGICAL_SKILL, -bonus, true);
			bonus = 0;
		}
		
		//fire, earth, wind, water resistance limit
		for (StatEnum stat : resistances)
		{
			if (getCurrentStat(stat) < MIN_RESISTANCE)
			{
				bonus = this.getBaseStat(stat) - MIN_RESISTANCE;
				this.setStat(stat, -bonus, true);
				bonus = 0;
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 */
	public synchronized void endEffect(StatEffectId id)
	{
		statsModifiers.remove(id);
		recomputeStats();
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public int getMagicalDefenseFor(SkillElement element)
	{
		switch(element)
		{
			case EARTH:
				return getCurrentStat(StatEnum.EARTH_RESISTANCE);
			case FIRE:
				return getCurrentStat(StatEnum.FIRE_RESISTANCE);
			case WATER:
				return getCurrentStat(StatEnum.WATER_RESISTANCE);
			case WIND:
				return getCurrentStat(StatEnum.WIND_RESISTANCE);
			default:
				return 0;
		}
	}
	
	/**
	 *  Reset all stats
	 */
	protected void resetStats()
	{
		synchronized (stats)
		{
			for(Stat stat : stats.values())
			{
				stat.reset();
			}
		}
	}
	
	/**
	 * 
	 * @param stat
	 * @param modifiers
	 */
	protected void applyModifiers(final StatEnum stat, StatModifiers modifiers)
	{
		if(modifiers == null)
			return;

		if(!stats.containsKey(stat))
		{
			initStat(stat, 0);
		}

		Stat oStat = stats.get(stat);
		int newValue;
		
		for(StatModifierPriority priority : StatModifierPriority.values())
		{
			for (StatModifier modifier : modifiers.getModifiers(priority))
			{
				newValue = modifier.apply(oStat.getBase(), oStat.getCurrent());
				oStat.increase(newValue, modifier.isBonus());
			}
		}

		if(stat == StatEnum.MAXHP || stat == StatEnum.MAXMP)
		{
			final int oldValue = getOldStat(stat);
			final int newVal = getCurrentStat(stat);
			if(oldValue == newVal)
			{
				return;
			}
			final CreatureLifeStats<? extends Creature> lifeStats = owner.getLifeStats();
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					switch(stat)
					{
						case MAXHP:
							if(oldValue == 0 || newVal == 0)
							{
								lifeStats.setCurrentHp(newVal);
								break;
							}
							int hp = lifeStats.getCurrentHp();
							hp = (int) (hp * ((float) newVal / oldValue));
 							lifeStats.setCurrentHp(hp);
 							break;
						case MAXMP:
							if(oldValue == 0 || newVal == 0)
							{
								lifeStats.setCurrentMp(newVal);
								break;
							}
							int mp = lifeStats.getCurrentMp();
							mp = (int) (mp * ((float) newVal / oldValue));
							lifeStats.setCurrentMp(mp);
							break;
					}
				}
			}, 0);
		}
	}
	
	/**
	 * 
	 * @param stat
	 * @param modifiers
	 */
	//TODO move to playergamestats
	protected void computeSummonStats()
	{
		//reset summonStats
		summonStats.clear();
		
		Map<StatEnum, StatModifiers> orderedModifiers = new HashMap<StatEnum, StatModifiers>();
		synchronized (statsModifiers)
		{
			//sort StatEffectIds according to StatEffectType order
			List<StatEffectId> statEffectIds = new ArrayList<StatEffectId>(statsModifiers.keySet());
	        Collections.sort(statEffectIds);
	        
	        //order modifiers
	        for (StatEffectId eid : statEffectIds)
	        {
				TreeSet<StatModifier> modifiers = statsModifiers.get(eid);
				int slots = 0;

				if(modifiers == null)
					continue;
				if(eid instanceof SkillEffectId)
					continue;
				for(StatModifier modifier : modifiers)
				{
					if(eid instanceof ItemStatEffectId)
					{
						slots = ((ItemStatEffectId) eid).getSlot();
					}
					
					if (slots == 0)
						slots = ItemSlot.NONE.getSlotIdMask();
					if(modifier.getStat().isMainOrSubHandStat(false) && owner instanceof Player)
					{
						if(slots != ItemSlot.MAIN_HAND.getSlotIdMask() && slots != ItemSlot.SUB_HAND.getSlotIdMask())
						{
							if(((Player) owner).getEquipment().getOffHandWeaponType() != null)
								slots = ItemSlot.MAIN_OR_SUB.getSlotIdMask();
							else
							{
								slots = ItemSlot.MAIN_HAND.getSlotIdMask();
							}
						}
						
						if(slots == ItemSlot.MAIN_HAND.getSlotIdMask() || slots == ItemSlot.SUB_HAND.getSlotIdMask())
						{
							if (modifier.isBonus())
								slots = ItemSlot.MAIN_OR_SUB.getSlotIdMask();
						}
					}

					List<ItemSlot> oSlots = ItemSlot.getSlotsFor(slots);
					for(ItemSlot slot : oSlots)
					{
						StatEnum statToModify = modifier.getStat().getMainOrSubHandStat(slot, false);
						//StatEnum.PARRY for sub_hand is not applied 
						if (slot == ItemSlot.SUB_HAND && statToModify == StatEnum.PARRY)
						{
							if (!modifier.isBonus())
								continue;
						}
						if(!orderedModifiers.containsKey(statToModify))
						{
							orderedModifiers.put(statToModify, new StatModifiers());
						}
						orderedModifiers.get(statToModify).add(modifier);
					}
				}
	        }
	        
	        //apply modifiers to summonStats
	        for(Entry<StatEnum, StatModifiers> entry : orderedModifiers.entrySet())
			{
	        	StatEnum stat = entry.getKey();
	        	StatModifiers modifiers = entry.getValue();
	        	if(modifiers == null)
	    			return;

	        	int newValue;
	    		
	    		for(StatModifierPriority priority : StatModifierPriority.values())
	    		{
	    			for (StatModifier modifier : modifiers.getModifiers(priority))
	    			{
	    				if(modifier.isBonus())
	    				{
	    					if (summonStats.containsKey(stat))
	    					{
	    						newValue = modifier.apply(0, summonStats.get(stat));
	    						summonStats.remove(stat);
	    					}
	    					else
	    						newValue = modifier.apply(0, 0);
	    					
	    					summonStats.put(stat, newValue);
	    				}
	    			}
	    		}
			}
		}
		orderedModifiers.clear();
	}
	
	public int getSummonStat(StatEnum stat)
	{
		if (summonStats.containsKey(stat))
			return summonStats.get(stat);
		else
			return 0;
	}
	
	/**
	 * 
	 * @param stat
	 * @param value
	 */
	protected void initStat(StatEnum stat, int value)
	{
		if(!stats.containsKey(stat))
			stats.put(stat, new Stat(stat, value));
		else
		{
			stats.get(stat).reset();
			stats.get(stat).set(value, false);
		}
	}
	
	/**
	 * 
	 * @param stat
	 * @param value
	 * @param bonus
	 */
	protected void setStat(StatEnum stat, int value, boolean bonus)
	{
		if(!stats.containsKey(stat))
		{
			stats.put(stat, new Stat(stat, 0));
		}
		stats.get(stat).set(value, bonus);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("owner:" + owner.getObjectId());
		for(Stat stat : stats.values())
		{
			sb.append(stat);
		}
		sb.append('}');
		return sb.toString();
	}
}
