/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.model.drop;

import gnu.trove.TIntObjectHashMap;

import java.util.HashSet;
import java.util.Set;

/**
 *  @author ATracer
 */
public class DropList 
{	
	private TIntObjectHashMap<Set<DropTemplate>> templatesMap = new TIntObjectHashMap<Set<DropTemplate>>();
	
	public void addDropTemplate(int mobId, DropTemplate dropTemplate)
	{
		Set<DropTemplate> dropTemplates = templatesMap.get(mobId);
		if(dropTemplates == null)
		{
			dropTemplates = new HashSet<DropTemplate>();
			templatesMap.put(mobId, dropTemplates);
		}
		dropTemplates.add(dropTemplate);
	}
	
	public void removeDrop(final int mobId, final int itemId)
	{
		DropTemplate select = null;
		if(templatesMap.get(mobId)!=null)
		{
			Set<DropTemplate> npcDrops = templatesMap.get(mobId);
			for(DropTemplate drop : npcDrops)
			{
				if(drop.getItemId() == itemId)
				{
					select = drop;
					break;
				}
			}
		}
		if(select != null)
			templatesMap.get(mobId).remove(select);
	}
	
	public TIntObjectHashMap<Set<DropTemplate>> getAll()
	{
		return templatesMap;
	}
	
	public Set<DropTemplate> getDropsFor(int mobId)
	{
		Set<DropTemplate> drops = templatesMap.get(mobId);
		if (drops == null)
			return drops;
		
		Set<DropTemplate> copy = new HashSet<DropTemplate>();
		copy.addAll(drops);
		return copy;
	}
	
	public int getSize()
	{
		return templatesMap.size();
	}
}