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

import org.openaion.gameserver.model.legion.Legion;
import org.openaion.gameserver.model.legion.LegionEmblem;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_LEGION_EMBLEM;
import org.openaion.gameserver.services.LegionService;

/**
 * @author Simple
 */
public class CM_LEGION_EMBLEM extends AionClientPacket
{
	
	private int	legionId;

	public CM_LEGION_EMBLEM(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		legionId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Legion legion = LegionService.getInstance().getLegion(legionId);
		if (legion != null)
		{
			LegionEmblem legionEmblem = legion.getLegionEmblem();
			sendPacket(new SM_LEGION_EMBLEM(legionId, legionEmblem.getEmblemVer(), legionEmblem.getColor_r(), legionEmblem.getColor_g(), legionEmblem.getColor_b(), legion.getLegionName(), legionEmblem.getIsCustom()));
		}
	}
}
