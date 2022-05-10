/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */


package com.aionemu.packetsamurai.dataholders;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "string", propOrder = {"id", "name", "body", "display_type", "message_type", "ment" })
public class ClientString 
{
	private int id;
	String name;
	String body;
	
	int display_type;
	String message_type;
	String ment;
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}	
	
	@XmlTransient
	int _realMessageType = (int)-1;
	
	public int getMessageType()
	{
		if (_realMessageType != -1)
			return _realMessageType;
		
		try
		{
			_realMessageType = Byte.parseByte(message_type);
		}
		catch (NumberFormatException e)
		{
			_realMessageType = 0;
		}
		return _realMessageType;
	}
	
	public int getDisplayType()
	{
		return display_type;
	}
	
	public String getText()
	{
		if (body == null)
			return "";
		
		return body;
	}
	
	public String getMentality()
	{
		if (ment == null)
			return "";
		
		return ment;
	}
}
