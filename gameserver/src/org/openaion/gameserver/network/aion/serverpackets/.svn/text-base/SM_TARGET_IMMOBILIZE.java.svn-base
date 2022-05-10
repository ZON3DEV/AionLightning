package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author Sweetkr
 */
public class SM_TARGET_IMMOBILIZE extends AionServerPacket
{
	private Creature creature;

	public SM_TARGET_IMMOBILIZE(Creature creature)
	{
		this.creature = creature;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, creature.getObjectId());
		writeF(buf, creature.getX());
		writeF(buf, creature.getY());
		writeF(buf, creature.getZ());
		writeC(buf, creature.getHeading());
	}
}
