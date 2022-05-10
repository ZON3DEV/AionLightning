/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.model.gameobjects;

import org.openaion.gameserver.controllers.NpcController;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author kecimis
 *
 */
public class NpcWithCreator extends Npc
{
	/**
	 * this class is extended by
	 * GroupGate
	 * Servant
	 * Homing
	 * SkillAreaNpc
	 * Totem
	 * Trap
	 * 
	 * for easier handling
	 */
	
	protected Creature creator;
	
	protected int skillId;
	
	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public NpcWithCreator(int objId, NpcController controller, SpawnTemplate spawnTemplate,
		VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}
	
	public NpcWithCreator getOwner()
	{
		return this;
	}
	
	@Override
	public Creature getActingCreature()
	{
		return this.creator;
	}

	@Override
	public Creature getMaster()
	{
		return this.creator;
	}
	
	/**
	 * @return the creator
	 */
	public Creature getCreator()
	{
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(Creature creator)
	{
		this.creator = creator;
	}
	
	/**
	 * @return the skillId
	 */
	public int getSkillId()
	{
		return skillId;
	}

	/**
	 * @param skillId the skillId to set
	 */
	public void setSkillId(int skillId)
	{
		this.skillId = skillId;
	}
	
	@Override
	public boolean isEnemy(VisibleObject visibleObject)
	{
		if (creator == null)
		{
			getOwner().getLifeStats().reduceHp(10000, getOwner());
			return false;
		}
		return super.isEnemy(visibleObject);
	}
	@Override
	protected boolean isEnemyNpc(Npc visibleObject)
	{
		return this.creator.isEnemyNpc(visibleObject);
	}

	@Override
	protected boolean isEnemyPlayer(Player visibleObject)
	{
		return this.creator.isEnemyPlayer(visibleObject);
	}
	
	@Override
	protected boolean isEnemySummon(Summon summon)
	{
		return this.creator.isEnemySummon(summon);
	}


}
