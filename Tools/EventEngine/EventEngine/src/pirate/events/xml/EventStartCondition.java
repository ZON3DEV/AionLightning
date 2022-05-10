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

package pirate.events.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author f14shm4n
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventStartCondition")
public class EventStartCondition {

    @XmlElement(name = "single_players_to_start")
    protected int singlePlayersToStart = 0;
    @XmlElement(name = "groups_to_start")
    protected int groupsToStart = 0;
    @XmlElement(name = "players_for_each_group_to_start")
    protected int playersForEachGroup = 0;

    public boolean isGroupCondition() {
        return this.groupsToStart > 0;
    }

    public int getSinglePlayersToStart() {
        return singlePlayersToStart;
    }

    public int getGroupsToStart() {
        return groupsToStart;
    }

    public int getPlayersForEachGroup() {
        return playersForEachGroup;
    }
}
