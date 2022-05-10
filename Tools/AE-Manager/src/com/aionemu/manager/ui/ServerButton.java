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

import javax.swing.JToggleButton;

import com.aionemu.manager.ui.CirclePanel;

/**
 * @author vyaslav
 *
 */
@SuppressWarnings("serial")
public class ServerButton extends JToggleButton {
	CirclePanel serverCircle;
	public ServerButton(String arg0, CirclePanel serverCircle) {
		super(arg0);
		this.serverCircle=serverCircle;
	}
	
	public CirclePanel getServerCircle(){
		return serverCircle;
	}
}
