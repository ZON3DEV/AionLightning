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

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

import javolution.util.FastMap;

@XmlRootElement(name = "strings")
@XmlType(name = "ClientStrings")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientStrings 
{
	@XmlElement(name = "string")
	List<ClientString> stringList;
	
	FastMap<Integer, ClientString> stringIndex;
	
	protected void afterUnmarshal(Unmarshaller u, Object parent)
	{
		stringIndex = new FastMap<Integer, ClientString>();
		for (ClientString s : stringList)
		{
			if (stringIndex.containsKey(s.getId()))
				continue;
			stringIndex.put(s.getId(), s);
		}
	}
	
	public String getStringById(int id)
	{
		if (stringIndex.containsKey(id))
			return stringIndex.get(id).body;
		
		return Integer.toString(id);
	}
}
