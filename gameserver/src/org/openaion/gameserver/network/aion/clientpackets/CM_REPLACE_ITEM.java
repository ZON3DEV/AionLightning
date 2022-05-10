/*
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

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.ItemService;
/**
 *
 * @author kosyachok
 */
public class CM_REPLACE_ITEM extends AionClientPacket
{

	private int sourceStorageType;
	private int sourceItemObjId;
	private int replaceStorageType;
	private int replaceItemObjId;

	public CM_REPLACE_ITEM(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		sourceStorageType = readC();
		sourceItemObjId = readD();
		replaceStorageType = readC();
		replaceItemObjId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		ItemService.switchStoragesItems(player, sourceStorageType, sourceItemObjId, replaceStorageType, replaceItemObjId);
	}

}
