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
package org.openaion.gameserver.dataholders;

import gnu.trove.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openaion.gameserver.model.templates.GuildTemplate;


/**
 * @author HellBoy
 *
 */
@XmlRootElement(name = "guild_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class GuildsData
{
	@XmlElement(name = "guild_template")
	private List<GuildTemplate> guildTemplates;
	/**
	 *  Map that contains npcId - GuildTemplate key-value pair
	 */
	private TIntObjectHashMap<GuildTemplate>	guildDataNpcId		= new TIntObjectHashMap<GuildTemplate>();
	private TIntObjectHashMap<GuildTemplate>	guildDataGuildId	= new TIntObjectHashMap<GuildTemplate>();
	private TIntObjectHashMap<GuildTemplate>	guildDataQuestId	= new TIntObjectHashMap<GuildTemplate>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		guildDataNpcId.clear();
		guildDataGuildId.clear();
		guildDataQuestId.clear();
		for(GuildTemplate guildTemplate: guildTemplates)
		{
			guildDataNpcId.put(guildTemplate.getNpcId(), guildTemplate);
			guildDataGuildId.put(guildTemplate.getGuildId(), guildTemplate);
			for(int i=0; i<guildTemplate.getGuildQuests().getGuildQuest().size(); i++)
				guildDataQuestId.put(guildTemplate.getGuildQuests().getGuildQuest().get(i).getGuildQuestId(), guildTemplate);
		}
	}
	
	/** 
	 * @param npcId
	 * @return GuildTemplate
	 */
	public GuildTemplate getGuildTemplateByNpcId(int npcId)
	{
		return guildDataNpcId.get(npcId);
	}
	
	/** 
	 * @param guildId
	 * @return GuildTemplate
	 */
	public GuildTemplate getGuildTemplateByGuildId(int guildId)
	{
		return guildDataGuildId.get(guildId);
	}
	
	/** 
	 * @param guildId
	 * @return GuildTemplate
	 */
	public GuildTemplate getGuildTemplateByQuestId(int questId)
	{
		return guildDataQuestId.get(questId);
	}
	
	/**
	 * @return guildData()
	 */
	public int size()
	{
		return guildDataNpcId.size();
	}

	/**
	 * @return the skillTemplates
	 */
	public List<GuildTemplate> getGuildTemplates()
	{
		return guildTemplates;
	}

	/**
	 * @param guildTemplates the guildTemplates to set
	 */
	public void setGuildTemplates(List<GuildTemplate> guildTemplates)
	{
		this.guildTemplates = guildTemplates;
		afterUnmarshal(null, null);
	}
}
