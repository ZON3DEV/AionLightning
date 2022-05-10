/**
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
package org.openaion.gameserver.model.siege;

import org.openaion.gameserver.ai.npcai.AggressiveAi;
import org.openaion.gameserver.ai.npcai.MonsterAi;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.controllers.FortressGeneralController;
import org.openaion.gameserver.model.alliance.PlayerAllianceGroup;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;

import javolution.util.FastList;


public class FortressGeneral extends Npc
{	
	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawn
	 * @param objectTemplate
	 */

	private int linkedFortressId;
	private FastList<PlayerGroup> rewardGroups;
	private FastList<PlayerAllianceGroup> rewardAlliances;

	public FortressGeneral(int objId, FortressGeneralController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int fortressId)
	{
		super(objId, controller, spawn, objectTemplate);
		this.linkedFortressId = fortressId;
		this.rewardGroups = new FastList<PlayerGroup>();
		this.rewardAlliances = new FastList<PlayerAllianceGroup>();
	}
	
	public void registerGroup(PlayerGroup group)
	{
		if(!rewardGroups.contains(group))
			rewardGroups.add(group);
	}
	
	public void registerAllianceGroup(PlayerAllianceGroup group)
	{
		if(!rewardAlliances.contains(group))
			rewardAlliances.add(group);
	}
	
	public FastList<PlayerGroup> getRewardGroups()
	{
		return rewardGroups;
	}
	
	public FastList<PlayerAllianceGroup> getRewardAlliances()
	{
		return rewardAlliances;
	}

	public int getFortressId()
	{
		return this.linkedFortressId;
	}

	@Override
	public FortressGeneralController getController()
	{
		return (FortressGeneralController) super.getController();
	}

	@Override
	public void initializeAi()
	{
		if(isAggressive() && !CustomConfig.DISABLE_MOB_AGGRO)
			this.ai = new AggressiveAi();
		else
			this.ai = new MonsterAi();

		ai.setOwner(this);	
	}
}
