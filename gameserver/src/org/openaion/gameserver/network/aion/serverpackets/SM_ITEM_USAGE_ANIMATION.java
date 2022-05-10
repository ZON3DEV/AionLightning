/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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

import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.siege.Artifact;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author ATracer
 */
public class SM_ITEM_USAGE_ANIMATION extends AionServerPacket
{
	private int playerObjId;
	private int targetObjId;
	private int itemObjId;
	private int itemId;
    private int time;
    private int end;
    private int unk;

	public SM_ITEM_USAGE_ANIMATION(int playerObjId, int itemObjId, int itemId)
	{
		this.playerObjId = playerObjId;
		this.targetObjId = playerObjId;
		this.itemObjId = itemObjId;
		this.itemId = itemId;
		this.time = 0;
		this.end = 1;
		this.unk = 1;
	}
	
	public SM_ITEM_USAGE_ANIMATION(int playerObjId, int itemObjId, int itemId, int time, int end, int unk)
	{
		this(playerObjId, playerObjId, itemObjId, itemId, time, end, unk);
	}
	public SM_ITEM_USAGE_ANIMATION(int playerObjId, int targetObjId, int itemObjId, int itemId, int time, int end, int unk)
	{
		this.playerObjId = playerObjId;
		this.targetObjId = targetObjId;
		this.itemObjId = itemObjId;
		this.itemId = itemId;
		this.time = time;
		this.end = end;
		this.unk = unk;
	}

	public SM_ITEM_USAGE_ANIMATION(int playerObjId, int itemObjId, int itemId, int time, int end)
	{
		this.playerObjId = playerObjId;
		this.targetObjId = playerObjId;
		this.itemObjId = itemObjId;
		this.itemId = itemId;
		this.time = time;
		this.end = end;
	}
	
	public SM_ITEM_USAGE_ANIMATION(Artifact artifact, Player player, Item stone, int end)
	{
		this.playerObjId = player.getObjectId();
		this.targetObjId = artifact.getObjectId();
		this.itemObjId = stone.getObjectId();
		this.itemId = stone.getItemId();
		this.time = (end == 0) ? 5000 : 0;
		this.end = end;
		this.unk = 1;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playerObjId); // player obj id
		writeD(buf, targetObjId); // target obj id 
		
		writeD(buf, itemObjId); // itemObjId
		writeD(buf, itemId); // item id
		
		writeD(buf, time); // time of casting bar of an item
		writeC(buf, end); // 1-casting bar hitted end, 3- interrupted by moving
		writeC(buf, 0); // always 0
		writeC(buf, 1); //always 1
		writeD(buf, unk); //some weird numbers
	}
}