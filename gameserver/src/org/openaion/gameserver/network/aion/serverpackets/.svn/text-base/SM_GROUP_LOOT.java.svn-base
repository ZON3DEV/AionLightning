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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author Rhys2002
 */
public class SM_GROUP_LOOT extends AionServerPacket
{	
	private int groupId;
	private int unk1;
	private int unk2;
	private int itemId;
	private int itemIndex;
	private int lootCorpseId;
	private int distributionId;
	private int playerId;
	private int luck;

	/**
	 * Start the roll options.
	 * 
	 * @param Player
	 *            Id must be 0 to start the Roll Options
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.itemIndex = itemIndex;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = 0;
		this.luck = 1;
	}

	/**
	 * Update the roll when someone rolls or passes.
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId, int playerId, int luck)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.itemIndex = itemIndex;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = playerId;
		this.luck = luck;
	}

	/**
	 * Send a packet with the winner.
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId, int playerId)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.itemIndex = itemIndex;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = playerId;
		this.luck = -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, groupId);
		writeD(buf, unk1);
		writeD(buf, unk2);
		writeD(buf, itemId);
		writeC(buf, itemIndex);
		writeD(buf, lootCorpseId);
		writeC(buf, distributionId);
		writeD(buf, playerId);
		writeD(buf, luck);
	}
}
