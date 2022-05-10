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
package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.HTMLService;

/**
 * @author lhw, Kaipo and ginho1
 */
public class CM_QUESTIONNAIRE extends AionClientPacket
{
	private int objectId;

	public CM_QUESTIONNAIRE(int opcode)
	{
		super(opcode);
	}

	/* (non-Javadoc)
	 * @see org.openaion.commons.network.packet.BaseClientPacket#readImpl()
	 */
	@Override
	protected void readImpl()
	{
		objectId = readD();
		readH();
		readH();
		readH();
		readH();
	}

	/* (non-Javadoc)
	 * @see org.openaion.commons.network.packet.BaseClientPacket#runImpl()
	 */
	@Override
	protected void runImpl()
	{
		if(objectId > 0)
		{
			Player player = getConnection().getActivePlayer();
			HTMLService.getMessage(player, objectId);
		}
	}
}
