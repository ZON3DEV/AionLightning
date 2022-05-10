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
package org.openaion.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.world.World;


/**
 * @author ATracer
 *
 */
public class CM_SUMMON_ATTACK extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_SUMMON_ATTACK.class);

	@SuppressWarnings("unused")
	private int summonObjId;
	private int targetObjId;
	@SuppressWarnings("unused")
	private int unk1;
	@SuppressWarnings("unused")
	private int unk2;
	@SuppressWarnings("unused")
	private int unk3;
	
	public CM_SUMMON_ATTACK(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		summonObjId = readD();
		targetObjId = readD();
		unk1 = readC();
		unk2 = readH();
		unk3 = readC();
	}

	@Override
	protected void runImpl()
	{
		// TODO: Use summonObjId to get summon, instead of activePlayer?
		Player activePlayer = getConnection().getActivePlayer();
		if (activePlayer == null)
		{
			log.error("CM_SUMMON_ATTACK packet received but cannot get master player.");
			return;
		}
		
		Summon summon = activePlayer.getSummon();
		
		if(summon == null)
		{
			log.error("CM_SUMMON_ATTACK packet received but cannot get summon.");
			return;
		}
		
		Creature creature = (Creature) World.getInstance().findAionObject(targetObjId);
		if (creature == null)
			return;
		summon.getController().attackTarget(creature);
	}
}
