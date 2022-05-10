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
package org.openaion.gameserver.model.templates.guild;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author HellBoy
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "guildQuests", propOrder = {"guildQuest"})
public class GuildQuests
{
	@XmlElement(name = "guild_quest")
	protected List<GuildQuest> guildQuest;

    /**
     * Gets the value of the guildQuest property.
     */
    public List<GuildQuest> getGuildQuest()
    {
        if(guildQuest == null)
        	guildQuest = new ArrayList<GuildQuest>();
        return this.guildQuest;
    }
}
