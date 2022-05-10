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
package org.openaion.gameserver.model.gameobjects;

import org.openaion.gameserver.controllers.CreatureController;
import org.openaion.gameserver.controllers.SummonController;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.SummonGameStats;
import org.openaion.gameserver.model.gameobjects.stats.SummonLifeStats;
import org.openaion.gameserver.model.templates.NpcTemplate;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.model.templates.stats.SummonStatsTemplate;
import org.openaion.gameserver.world.WorldPosition;

/**
 * @author ATracer
 * 
 */
public class Summon extends Creature
{

	private Player	master;
	private SummonMode mode;
	private byte level;

	public static enum SummonMode
	{
		ATTACK(0),
		GUARD(1),
		REST(2),
		RELEASE(3);
		
		private int id;
		
		private SummonMode(int id)
		{
			this.id = id;
		}

		/**
		 * @return the id
		 */
		public int getId()
		{
			return id;
		}	
	}
	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param statsTemplate 
	 * @param position
	 */
	public Summon(int objId, CreatureController<? extends Creature> controller, SpawnTemplate spawnTemplate,
		VisibleObjectTemplate objectTemplate, SummonStatsTemplate statsTemplate, byte level)
	{
		super(objId, controller, spawnTemplate, objectTemplate, new WorldPosition());

		controller.setOwner(this);
		this.level = level;
		super.setGameStats(new SummonGameStats(this, statsTemplate));
		super.setLifeStats(new SummonLifeStats(this));
		
		this.mode = SummonMode.GUARD;
	}

	/**
	 * @return the owner
	 */
	@Override
	public Player getMaster()
	{
		return master;
	}

	/**
	 * @param master
	 *            the master to set
	 */
	public void setMaster(Player master)
	{
		this.master = master;
	}
	
	@Override
	public Creature getActingCreature()
	{
		return this.master;
	}
	
	@Override
	public String getName()
	{
		return objectTemplate.getName();
	}

	/**
	 * @return the level
	 */
	@Override
	public byte getLevel()
	{
		return level;
	}

	@Override
	public void initializeAi()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public NpcTemplate getObjectTemplate()
	{
		return (NpcTemplate) super.getObjectTemplate();
	}

	public int getNpcId()
	{
		return getObjectTemplate().getTemplateId();
	}
	
	public int getNameId()
	{
		return getObjectTemplate().getNameId();
	}

	/**
	 * @return NpcObjectType.SUMMON
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.SUMMON;
	}

	@Override
	public SummonController getController()
	{
		return (SummonController) super.getController();
	}

	/**
	 * @return the mode
	 */
	public SummonMode getMode()
	{
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(SummonMode mode)
	{
		this.mode = mode;
	}

	@Override
	protected boolean isEnemyNpc(Npc visibleObject)
	{
		if(master == null)
			return false;
		return master.isEnemyNpc(visibleObject);
	}

	@Override
	protected boolean isEnemyPlayer(Player visibleObject)
	{
		if(master == null)
			return false;
		return master.isEnemyPlayer(visibleObject);
	}

	@Override
	protected boolean isEnemySummon(Summon summon)
	{
		if(master == null)
			return false;
		return master.isEnemySummon(summon);
	}

	@Override
	public String getTribe()
	{
		if(master == null)
			return "";
		return master.getTribe();
	}

	@Override
	public boolean isAggressiveTo(Creature creature)
	{
		return creature.isAggroFrom(this);
	}
	
	@Override
	public boolean isAggroFrom(Npc npc)
	{
		if(getMaster() == null)
			return false;
		return getMaster().isAggroFrom(npc);
	}
}
