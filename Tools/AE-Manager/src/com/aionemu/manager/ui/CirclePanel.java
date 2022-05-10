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
package com.aionemu.manager.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author vyaslav
 *
 */
@SuppressWarnings("serial")
public class CirclePanel extends JPanel {
	Color color;
	
	CirclePanel(Color color){
		this.color=color;
	}
	
	public void setColor(Color color){
		this.color=color;
		this.repaint();
	}

	public Color getColor(){
		return color;
	}
	
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	g.setColor(this.color);
    	g.fillOval(2, 2, this.getWidth()-2, this.getHeight()-2);

        //g.fillOval(arg0, arg1, arg2, arg3)
    }
    
    
}
