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
package org.openaion.gameserver.dataholders;

import gnu.trove.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openaion.gameserver.model.templates.npcshouts.Shout;
import org.openaion.gameserver.model.templates.npcshouts.ShoutNpc;


/**
 * @author Sylar
 */
@XmlRootElement(name = "npc_shouts")
@XmlAccessorType(XmlAccessType.FIELD)
public class NpcShoutsData
{
	@XmlElement(name = "shout_npc")
	protected List<ShoutNpc> shouts;
	
	private TIntObjectHashMap<List<Shout>> npcShouts = new TIntObjectHashMap<List<Shout>>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		npcShouts.clear();
		for(ShoutNpc shout : shouts)
		{
			if(!npcShouts.containsKey(shout.getNpcId()))
				npcShouts.put(shout.getNpcId(), new ArrayList<Shout>());
			
			npcShouts.get(shout.getNpcId()).addAll(shout.getShouts());
		}
		shouts.clear();
	}
	
	public List<Shout> getShoutsForNpc(int npcId)
	{
		return npcShouts.get(npcId);
	}
	
	public int size()
	{
		return npcShouts.size();
	}
	
}
