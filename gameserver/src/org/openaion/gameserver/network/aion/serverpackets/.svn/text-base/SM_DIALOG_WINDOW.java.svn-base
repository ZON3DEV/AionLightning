/*
 * This file is part of aion-unique <aionunique.smfnew.com>.
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

import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.world.World;


/**
 * 
 * @author alexa026
 * 
 */
public class SM_DIALOG_WINDOW extends AionServerPacket
{
	private int	targetObjectId;
	private int dialogID;
	private int	questId = 0;
	
	public SM_DIALOG_WINDOW(int targetObjectId, int dlgID)
	{
		this.targetObjectId = targetObjectId;
		this.dialogID = dlgID;
	}

	public SM_DIALOG_WINDOW(int targetObjectId , int dlgID , int questId)
	{
		this.targetObjectId = targetObjectId;
		this.dialogID = dlgID;
		this.questId = questId;
	}
	/**
	* {@inheritDoc}
	*/
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{		
		writeD(buf, targetObjectId);
		writeH(buf, dialogID);
		writeD(buf, questId);
		writeH(buf, 0);
		if(this.dialogID == 18)
		{
			AionObject object = World.getInstance().findAionObject(targetObjectId);
			if(object != null && object instanceof Npc)
			{
				Npc znpc = (Npc)object;
				if(znpc.getNpcId() == 798044 || znpc.getNpcId() == 798101)
					writeH(buf, 2);
			}
		}
	}
}
