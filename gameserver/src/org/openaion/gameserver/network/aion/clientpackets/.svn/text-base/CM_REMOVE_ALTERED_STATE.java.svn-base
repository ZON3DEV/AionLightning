package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;

/**
 * @author dragoon112
 *
 */
public class CM_REMOVE_ALTERED_STATE extends AionClientPacket
{

	private int	skillid;

	/**
	 * @param opcode
	 */
	public CM_REMOVE_ALTERED_STATE(int opcode)
	{
		super(opcode);
	}

	/* (non-Javadoc)
	 * @see org.openaion.commons.network.packet.BaseClientPacket#readImpl()
	 */
	@Override
	protected void readImpl()
	{
		skillid = readH();
		
	}

	/* (non-Javadoc)
	 * @see org.openaion.commons.network.packet.BaseClientPacket#runImpl()
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		player.getEffectController().removeEffect(skillid);
	}

}
