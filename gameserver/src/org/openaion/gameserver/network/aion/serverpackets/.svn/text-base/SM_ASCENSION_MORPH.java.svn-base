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

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * ascension quest's morph
 * @author wylovech
 *
 */
public class SM_ASCENSION_MORPH extends AionServerPacket
{
   private int inascension;   
   public SM_ASCENSION_MORPH(int inascension)
   {
      this.inascension = inascension;
   }
   
   @Override
   protected void writeImpl(AionConnection con, ByteBuffer buf)
   {
      writeC(buf,inascension);//if inascension =0x01 morph.   
   }
}
