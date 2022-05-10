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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.model.templates.GatherableTemplate;
import org.openaion.gameserver.model.templates.gather.Material;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author ATracer, orz
 *
 */
public class SM_GATHER_UPDATE extends AionServerPacket
{
	private GatherableTemplate template;
	private int action;
	private int itemId;
	private int success;
	private int failure;
	private int nameId;

	public SM_GATHER_UPDATE(GatherableTemplate template, Material material, int success, int failure, int action)
	{
		this.action = action;
		this.template = template;
		this.itemId = material.getItemid();
		this.success = success;
		this.failure = failure;
		this.nameId = material.getNameid();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, template.getSkillLevel());
		writeC(buf, action);
		writeD(buf, itemId);

		switch(action)
		{
			case 0:
			{
				writeD(buf, 100); //success value
				writeD(buf, 100); //failure value
				writeD(buf, 0);
				writeD(buf, 1200);	//delay after which bar will start moving (ms)
				writeD(buf, 1330011); //start gathering system message
				writeH(buf, 0x24);
				writeD(buf, nameId); //item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
			}
			case  1:
			{
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 700);	//time of moving execution (ms)
				writeD(buf, 1200);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			}
			case 2:
			{
				writeD(buf, 100);
				writeD(buf, failure);
				writeD(buf, 700);
				writeD(buf, 1200);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			}
			case 5: // you have stopped gathering
			{
				writeD(buf, 0);
				writeD(buf, 0);
				writeD(buf, 700);
				writeD(buf, 1200);
				writeD(buf, 1330080);
				writeH(buf, 0);
				break;
			}
			case 6:
			{
				writeD(buf, 100);
				writeD(buf, failure);
				writeD(buf, 700);
				writeD(buf, 1200);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			}
			case 7:
			{
				writeD(buf, success);
				writeD(buf, 100);
				writeD(buf, 0);
				writeD(buf, 1200);
				writeD(buf, 1330079);
				writeH(buf, 0x24);
				writeD(buf, nameId);
				writeH(buf, 0);
				break;
			}
		}
	}

}
