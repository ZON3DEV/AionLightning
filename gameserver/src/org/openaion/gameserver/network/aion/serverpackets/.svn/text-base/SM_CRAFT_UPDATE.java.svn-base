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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author Mr. Poke, HellBoy
 *
 */
public class SM_CRAFT_UPDATE extends AionServerPacket
{
	private int skillId;
	private int itemId;
	private int action;
	private int success;
	private int failure;
	private int nameId;
	private int delay;

	/**
	 * @param skillId
	 * @param item
	 * @param success
	 * @param failure
	 * @param action
	 */
	public SM_CRAFT_UPDATE(int skillId, ItemTemplate item, int success, int failure, int action)
	{
		this.action = action;
		this.skillId = skillId;
		this.itemId = item.getTemplateId();
		this.success = success;
		this.failure = failure;
		this.nameId = item.getNameId();
		if(skillId == 40009)
			this.delay = 1500;
		else
			this.delay = 700;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, skillId);
		writeC(buf, action);
		writeD(buf, itemId);

		switch(action)
		{
			case 0: //init
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 1200);	//delay after which bar will start moving (ms)
				writeD(buf, 1330048);	//start crafting system message
				writeH(buf, 0x24);
				writeD(buf, nameId);	//item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
			case 1: //regular update
			case 2: //speed up update
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, delay);	//time of moving execution (ms)
				writeD(buf, 1200);	//delay after which bar will start moving (ms)
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			case 3: //crit
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			case 4:	//cancel crafting
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);
				writeD(buf, 1330051);	//canceled crafting system message
				writeH(buf, 0);
				break;
			case 5: //success finish
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);	
				writeD(buf, 1300788);	//success crafting system message
				writeH(buf, 0x24);
				writeD(buf, nameId);	//item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
			case 6: //fail finish
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);	
				writeD(buf, 1330050);	//fail crafting system message
				writeH(buf, 0x24);
				writeD(buf, nameId);	//item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
		}
	}
}