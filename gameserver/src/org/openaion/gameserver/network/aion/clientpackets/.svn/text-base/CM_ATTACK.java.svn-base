/**
 * This file is part of aion-unique <aion-unique.com>.
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

import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.world.World;
/**
 * 
 * @author alexa026, Avol, ATracer
 * 
 */
public class CM_ATTACK extends AionClientPacket
{
	/**
	 * Packet send when player is auto attacking
	 */
	private int					targetObjectId;
	private int					attackno;
	private int					time;
	private int					type;
	
	// TODO: Question, are they really needed?
	@SuppressWarnings("unused")
	private long                exp;
	@SuppressWarnings("unused")
	private long                maxexp;
	@SuppressWarnings("unused")
	private int					at;

	public CM_ATTACK(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		targetObjectId = readD();
		attackno = readC();
		time = readH();
		type = readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if(player != null && !player.getLifeStats().isAlreadyDead())
		{
			if(player.isProtectionActive())
				player.getController().stopProtectionActiveTask();

			if(!player.getController().checkAttackPacketSpeed())
				return;
			
			AionObject object = World.getInstance().findAionObject(targetObjectId);
			if (object instanceof Creature)
				player.getController().attackTarget((Creature)object, attackno, time, type);
		}
	}
}
