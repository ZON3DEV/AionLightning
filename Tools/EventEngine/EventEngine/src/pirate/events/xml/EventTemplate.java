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
import pirate.events.enums.EventType;

/**
 *
 * @author flashman
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Event")
public class EventTemplate {

    @XmlElement(name = "type")
    protected EventType etype;
    @XmlElement(name = "event_id")
    protected int eventId;
    @XmlElement(name = "map")
    protected int map;
    @XmlElement(name = "schedule")
    protected String schedule;
    @XmlElement(name = "registration_time")
    protected int registration_time;
    @XmlElement(name = "reentry_cooldown")
    protected int reentry_cooldown;
    @XmlElement(name = "name")
    protected String eventName = "";
    @XmlElement(name = "cmd_name")
    protected String cmdName = "";
    @XmlElement(name = "start_condition")
    protected EventStartCondition start_condition;
    @XmlElement(name = "start_position_info")
    protected EventStartPositionList start_position_info;
    @XmlElement(name = "reward_info")
    protected EventRewardTemplate reward_info;

    /**
     * ID Events dlya InstanceHandler
     *
     * @return
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Schedule to run the event period.
     *
     * @return rule cron
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * type Events
     *
     * @return
     */
    public EventType getEventType() {
        return etype;
    }

    /**
     * identifier locations
     *
     * @return
     */
    public int getMapId() {
        return map;
    }

    /**
     * name of the event period
     *
     * @return
     */
    public String getEventName() {
        return this.eventName;
    }

    public String getCmdName() {
        return this.cmdName;
    }

    /**
     * The open registration
     *
     * @return
     */
    public int getRegistrationTime() {
        return registration_time;
    }

    /**
     * Time through with which you can re-entry.
     *
     * @return seconds
     */
    public int getReentryCooldown() {
        return reentry_cooldown;
    }

    public EventStartCondition getStartCondition() {
        return this.start_condition;
    }

    public EventRewardTemplate getRewardInfo() {
        return this.reward_info;
    }

    public EventStartPositionList getStartPositionInfo() {
        return this.start_position_info;
    }
}
