/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.commons.ngen.utils.ssl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author blakawk
 *
 */
public class ResourceLoader
{
	public static InputStream loadResource (String name)
	{
		InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(name);
		DataInputStream dis = null;
		if (is==null)
		{
			FileInputStream fis;
			try {
				fis = new FileInputStream(name);
				dis = new DataInputStream(fis);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			try {
				dis = new DataInputStream(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
		    byte[] bytes = new byte[dis.available()];
	        dis.readFully(bytes);
	        is = new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
}
