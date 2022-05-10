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
package org.openaion.gameserver.model.siege;

import org.openaion.gameserver.controllers.FortressGateArtifactController;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;

/**
 * @author Sarynth
 *
 */
public class FortressGateArtifact extends Npc
{
	private int fortressId;
	private FortressGate relatedGate;
	private int healRatio;
	
	private int spawnStaticId;
	
	public FortressGateArtifact(int objId, FortressGateArtifactController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int fortressId, int staticId,int healRatio)
	{
		super(objId, controller, spawn, objectTemplate);
		this.fortressId = fortressId;
		this.spawnStaticId = staticId;
		this.healRatio = healRatio;
	}
	
	public FortressGate getRelatedGate()
	{
		return relatedGate;
	}
	
	public void setRelatedGate(FortressGate gate)
	{
		relatedGate = gate;
	}
	
	public void healGate()
	{
		int hpToAdd = relatedGate.getLifeStats().getMaxHp() * (healRatio / 100);
		relatedGate.getLifeStats().increaseHp(TYPE.NATURAL_HP, hpToAdd);
	}
	
	public int getFortressId()
	{
		return fortressId;
	}
	
	public int getStaticId()
	{
		return spawnStaticId;
	}
	
}
