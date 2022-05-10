/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * @author blakawk
 * @author Arkshadow
 *
 */
public class SM_PLAYER_ID extends AionServerPacket
{
    private AionObject playerAionObject;
    private boolean init = false;
    private int instanceId = 0;
    private int remainingTime = 0;

    public SM_PLAYER_ID(AionObject playerAionObject)
    {
        this.playerAionObject = playerAionObject;
    }
    
    public SM_PLAYER_ID(AionObject playerAionObject, boolean init)
    {
    	this.playerAionObject = playerAionObject;
    	this.init = init;
    }
    
    public SM_PLAYER_ID(AionObject playerAionObject, int id, int time)
    {
    	this.playerAionObject = playerAionObject;
    	this.instanceId = id;
    	this.remainingTime = time;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(!init)
		{
			writeH(buf, 0x2);
	        writeD(buf, 0x0);
	        writeH(buf, 0x1);
	        writeD(buf, playerAionObject.getObjectId());
	        if(instanceId != 0 && remainingTime != 0)
	        {
	        	writeH(buf, 0x1); //instance info or not
	        	writeD(buf, instanceId); //instance ID
	        	writeD(buf, 0x0); //unk
	        	writeD(buf, remainingTime); //remaingTime in seconds
	        	writeH(buf, 0x0); //unk
	        	writeS(buf, playerAionObject.getName());
	        }
	        else
	        {
	        	writeH(buf, 0x0); //instance info or not
	        	writeS(buf, playerAionObject.getName());
	        }
		}
		else
		{
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
		}
	}
}
