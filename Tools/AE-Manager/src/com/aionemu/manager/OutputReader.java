/**
 * This file is part of Aion Extreme  Emulator <aion-core.net>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.manager;
/*
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.aionemu.manager.ui.ManagerMainView;

/**
 *
 * @author vyaslav
 */

public class OutputReader extends Thread
{
    InputStream is;
    ManagerMainView parent;
    int serverId;

    public OutputReader(InputStream is, ManagerMainView parent, int serverId)
    {
        this.is = is;
        this.serverId = serverId;
        this.parent = parent;
    }

    public synchronized void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
            	parent.updateOutput(line,serverId);
            } catch (IOException ioe)
              {
                ioe.printStackTrace();
              }
    }
}
