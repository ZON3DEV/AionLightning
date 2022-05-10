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
package org.openaion.gameserver.controllers.attack;

import java.util.Collection;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 * @author ATracer, KKnD
 *
 */
public class AggroList
{	
	@SuppressWarnings("unused")
	private static final Logger	log	= Logger.getLogger(AggroList.class);
	
	private Creature owner;
	
	private FastMap<Creature, AggroInfo> aggroList = new FastMap<Creature, AggroInfo>().shared();
	
	public AggroList(Creature owner)
	{
		this.owner = owner;
	}

	/**
	 * Only add damage from enemies. (Verify this includes
	 * summons, traps, pets, and excludes fall damage.)
	 * @param creature
	 * @param damage
	 */
	public void addDamage(Creature creature, int damage)
	{
		if (creature == null ||
			!owner.isEnemy(creature))
			return;
			
		AggroInfo ai = getAggroInfo(creature);
		ai.addDamage(damage);
		/**
		 * For now we add hate equal to each damage received
		 * Additionally there will be broadcast of extra hate
		 */
		ai.addHate(damage);
		
		owner.getAi().handleEvent(Event.ATTACKED);
	}

	/**
	 * Extra hate that is received from using non-damange skill effects
	 * 
	 * @param creature
	 * @param hate
	 */
	public void addHate(Creature creature, int hate)
	{
		if (creature == null ||
			creature == owner ||
			!owner.isEnemy(creature))
			return;
	
		AggroInfo ai = getAggroInfo(creature);
		ai.addHate(hate);
		
		if(hate > 0)		
			owner.getAi().handleEvent(Event.ATTACKED);
	}

	/**
	 * @return player/group/alliance with most damage.
	 */
	public AionObject getMostDamage()
	{
		AionObject mostDamage = null;
		int maxDamage = 0;
		
		for (AggroInfo ai : getFinalDamageList(true))
		{
			if (ai.getAttacker() == null)
				continue;
			
			if (ai.getDamage() > maxDamage)
			{
				mostDamage = ai.getAttacker();
				maxDamage = ai.getDamage();
			}
		}
		
		return mostDamage;
	}
	
	/**
	 * @return player with most damage
	 */
	public Player getMostPlayerDamage()
	{
		if (aggroList.isEmpty())
			return null;
		
		Player mostDamage = null;
		int maxDamage = 0;
		
		// Use final damage list to get pet damage as well.
		for (AggroInfo ai : this.getFinalDamageList(false))
		{
			if (ai.getDamage() > maxDamage)
			{
				mostDamage = (Player)ai.getAttacker();
				maxDamage = ai.getDamage();
			}
		}
		
		return mostDamage;
	}

	/**
	 * 
	 * @return most hated creature
	 */
	public Creature getMostHated()
	{
		if (aggroList.isEmpty())
			return null;

		Creature mostHated = null;
		int maxHate = 0;

		for (AggroInfo ai : aggroList.values())
		{
			if (ai == null)
				continue;
			
			// aggroList will never contain anything but creatures
			Creature attacker = (Creature)ai.getAttacker();
			
			if(attacker.getLifeStats().isAlreadyDead()
				|| !owner.getKnownList().knowns(ai.getAttacker()))
				ai.setHate(0);

			if (ai.getHate() > maxHate)
			{
				mostHated = attacker;
				maxHate = ai.getHate();
			}
		}

		return mostHated;
	}	
	
	/**
	 * 
	 * @param creature
	 * @return
	 */
	public boolean isMostHated(Creature creature)
	{
		if(creature == null || creature.getLifeStats().isAlreadyDead())
			return false;
		
		Creature mostHated = getMostHated();
		if(mostHated == null)
			return false;
		
		return mostHated.equals(creature);
	}

	/**
	 * @param creature
	 * @param value
	 */
	public void notifyHate(Creature creature, int value)
	{
		//transfer hate from SkillAreaNpc,Homing,Trap,Totem,Servant to master
		if (creature instanceof Npc && creature.getActingCreature() != null && creature.getActingCreature() != creature)
			creature = creature.getActingCreature();
		
		if(isHating(creature))
			addHate(creature, value);
	}
	
	/**
	 * 
	 * @param creature
	 */
	public void stopHating(Creature creature)
	{
		AggroInfo aggroInfo = aggroList.get(creature);
		if(aggroInfo != null)
			aggroInfo.setHate(0);
	}
	
	/**
	 * Remove completely creature from aggro list
	 * 
	 * @param creature
	 */
	public void remove(Creature creature)
	{
		aggroList.remove(creature);
	}
	
	/**
	 * Clear aggroList
	 */
	public void clear()
	{
		aggroList.clear();
	}
	
	/**
	 * 
	 * @param creature
	 * @return aggroInfo
	 */
	public AggroInfo getAggroInfo(Creature creature)
	{
		AggroInfo ai = aggroList.get(creature);
		if (ai == null)
		{
			ai = new AggroInfo(creature);
			aggroList.put(creature, ai);
		}
		return ai;
	}
	
	/**
	 * 
	 * @param creature
	 * @return boolean
	 */
	private boolean isHating(Creature creature)
	{
		return aggroList.containsKey(creature);
	}

	/**
	 * @return aggro list
	 */
	public Collection<AggroInfo> getList()
	{
		return aggroList.values();
	}

	/**
	 * @return total damage
	 */
	public int getTotalDamage()
	{
		int totalDamage = 0;
		for(AggroInfo ai : this.aggroList.values())
		{
			totalDamage += ai.getDamage();
		}
		return totalDamage;
	}

	/**
	 * Used to get a list of AggroInfo with player/group/alliance damages combined.
	 *  - Includes only AggroInfo with PlayerAlliance, PlayerGroup, and Player objects.
	 * @return finalDamageList including players/groups/alliances
	 */
	public Collection<AggroInfo> getFinalDamageList(boolean mergeGroupDamage)
	{
		final FastMap<AionObject, AggroInfo> list = new FastMap<AionObject, AggroInfo>().shared();
		
		for(AggroInfo ai : this.aggroList.values())
		{
			if (!(ai.getAttacker() instanceof Creature))
				continue;
			
			// Check to see if this is a summon, if so add the damage to the group. 
			
			Creature master = ((Creature)ai.getAttacker()).getMaster();
			
			if (!(master instanceof Player))
				continue;
			
			Player player = (Player)master;
			
			
			// Don't include damage from players outside the known list.
			if (!owner.getKnownList().knowns(player))
				continue;
			
			if (mergeGroupDamage)
			{
				AionObject source;
				
				if (player.isInAlliance())
				{
					source = player.getPlayerAlliance();
				}
				else if (player.isInGroup())
				{
					source = player.getPlayerGroup();
				}
				else
				{
					source = player;
				}
				
				if (list.containsKey(source))
				{
					(list.get(source)).addDamage(ai.getDamage());
				}
				else
				{
					AggroInfo aggro = new AggroInfo(source);
					aggro.setDamage(ai.getDamage());
					list.put(source, aggro);
				}
			}
			else if (list.containsKey(player))
			{
				// Summon or other assistance
				list.get(player).addDamage(ai.getDamage());
			}
			else
			{
				// Create a separate object so we don't taint current list.
				AggroInfo aggro = new AggroInfo(player);
				aggro.addDamage(ai.getDamage());
				list.put(player, aggro);
			}
		}
		
		return list.values();
	}

	
	
}
