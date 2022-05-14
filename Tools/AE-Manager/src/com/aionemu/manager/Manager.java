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

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.aionemu.manager.ui.ManagerMainView;





/**
 * @author vyaslav
 *
 */
public class Manager {
	public static ManagerMainView mainView;
	final public static int height=400;
	final public static int width=600;	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }


	    SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		    	mainView=new ManagerMainView();
		    	mainView.setSize(width, height);
		    	mainView.setVisible(true);
		      }
		    });
	}
}
