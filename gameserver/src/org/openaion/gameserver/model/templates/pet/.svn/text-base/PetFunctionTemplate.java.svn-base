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
package org.openaion.gameserver.model.templates.pet;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Sylar
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "petfunction")
public class PetFunctionTemplate
{
	@XmlAttribute(name = "type", required = true)
	private String			type;
	
	@XmlAttribute(name = "id", required = true)
	private int				id;
	
	@XmlAttribute(name = "slots", required = true)
	private int		 		slots;
	
	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the slots
	 */
	public int getSlots()
	{
		return slots;
	}

	void afterUnmarshal (Unmarshaller u, Object parent)
	{
		
	}
}
