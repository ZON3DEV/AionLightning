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

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_STANCE_STATE;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 *
 */
public class CM_SKILL_DEACTIVATE extends AionClientPacket
{
	private int skillId;
	
	public CM_SKILL_DEACTIVATE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		skillId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if (player != null && player.getEffectController() != null)
			player.getEffectController().removeNoshowEffect(skillId);
		
		if (DataManager.SKILL_DATA.getSkillTemplate(skillId).isStance())
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_STANCE_STATE(((AionObject)player).getObjectId(), 0));
	}
}
