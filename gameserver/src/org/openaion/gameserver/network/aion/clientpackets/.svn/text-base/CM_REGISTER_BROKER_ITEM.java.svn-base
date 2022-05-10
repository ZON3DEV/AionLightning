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
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.BrokerService;


/**
 * @author kosyak
 *
 */
public class CM_REGISTER_BROKER_ITEM extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_REGISTER_BROKER_ITEM.class);
	@SuppressWarnings("unused")
	private int brokerId;
	private int itemUniqueId;
	private long price;
	private int itemCount;
	
	public CM_REGISTER_BROKER_ITEM(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{
		this.brokerId = readD();
		this.itemUniqueId = readD();
		this.price = readQ();
		this.itemCount = readH();
	}

	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if (price < 1 || itemCount < 1)
		{
			log.warn("[AUDIT] Possible client hack Player: "+player.getName()+" Account name: "+player.getAcountName()+toString());
			return;
		}
		BrokerService.getInstance().registerItem(player, itemUniqueId, price, itemCount);
	}
}
