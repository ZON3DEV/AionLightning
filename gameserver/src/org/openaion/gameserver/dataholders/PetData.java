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
package org.openaion.gameserver.dataholders;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openaion.gameserver.model.templates.pet.PetTemplate;


/**
 * @author Sylar
 *
 */
@XmlRootElement(name = "pets")
@XmlAccessorType(XmlAccessType.FIELD)
public class PetData
{
	@XmlElement(name="pet")
	private List<PetTemplate> pts;
	
	private TIntObjectHashMap<PetTemplate> templates;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		templates = new TIntObjectHashMap<PetTemplate>();
		for(PetTemplate pt: pts)
		{
			templates.put(pt.getPetId(), pt);
		}
		pts = null;
	}
	
	public PetTemplate getPetTemplate(int petId)
	{
		return templates.get(petId);
	}
	
	public PetTemplate getPetTemplateByEggId(int eggId)
	{
		for (TIntObjectIterator<PetTemplate> it = templates.iterator();it.hasNext();)
		{
			it.advance();
			if (it.value().getEggId()==eggId)
				return it.value();
		}
		return null;
	}

	/**
	 * @return titles.size()
	 */
	public int size()
	{
		return templates.size();
	}
}
