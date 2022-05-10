/**
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

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.templates.NpcTemplate;


/**
 * This is a container holding and serving all {@link NpcTemplate} instances.<br>
 * Briefly: Every {@link Npc} instance represents some class of NPCs among which each have the same id, name, items,
 * statistics. Data for such NPC class is defined in {@link NpcTemplate} and is uniquely identified by npc id.
 * 
 * @author Luno
 * 
 */
@XmlRootElement(name = "npc_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class NpcData
{
	@XmlElement(name = "npc_template")
	private List<NpcTemplate> npcs;
	
	/** A map containing all npc templates */
	private TIntObjectHashMap<NpcTemplate>	npcData	= new TIntObjectHashMap<NpcTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		npcData.clear();
		for(NpcTemplate npc: npcs)
		{
			if(CustomConfig.NPC_DYNAMIC_STAT && !npc.getName().equals("Fire Spirit")
				&& !npc.getName().equals("Earth Spirit")
				&& !npc.getName().equals("Water Spirit")
				&& !npc.getName().equals("Wind Spirit")
				&& !npc.getName().equals("Magma Spirit")
				&& !npc.getName().equals("Tempest Spirit"))
			{
				// Apply dynamic stats
				int npcLevel = npc.getLevel();
				float rankModifier;
				float powerModifier;
				switch(npc.getRank())
				{
					case NORMAL: rankModifier = 2.0f; powerModifier = 1.0f; break;
					case ELITE: rankModifier = 3.0f; powerModifier = 1.3f; break;
					case HERO: rankModifier = 3.5f; powerModifier = 1.5f; break;
					case LEGENDARY: rankModifier = 4.0f; powerModifier = 1.7f; break;
					default: rankModifier = 1.0f; powerModifier = 1.0f; break;
				}
				
				int baseStat = Math.round((npcLevel * rankModifier));
				
				npc.getStatsTemplate().setAccuracy(baseStat);
				npc.getStatsTemplate().setBlock(baseStat);
				npc.getStatsTemplate().setCrit(baseStat);
				npc.getStatsTemplate().setEvasion(baseStat);
				npc.getStatsTemplate().setMagicAccuracy(baseStat);
				npc.getStatsTemplate().setMdef(baseStat);
				npc.getStatsTemplate().setParry(baseStat);
				npc.getStatsTemplate().setPdef(baseStat);
				npc.getStatsTemplate().setPower(Math.round(baseStat / powerModifier));
			}
			
			npcData.put(npc.getTemplateId(), npc);
		}
	}
	
	public int size()
	{
		return npcData.size();
	}
	
	public List<NpcTemplate> getTemplates()
	{
		return npcs;
	}
	
	/**

	/**
	 * Returns an {@link NpcTemplate} object with given id.
	 * 
	 * @param id
	 *            id of NPC
	 * @return NpcTemplate object containing data about NPC with that id.
	 */
	public NpcTemplate getNpcTemplate(int id)
	{
		return npcData.get(id);
	}
	
	public void setTemplates(List<NpcTemplate> templates)
	{
		npcs.clear();
		npcs = null;
		npcs = templates;
		afterUnmarshal(null, null);
	}

}
