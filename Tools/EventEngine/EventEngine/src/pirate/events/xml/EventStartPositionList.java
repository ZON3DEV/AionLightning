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

import com.aionemu.commons.utils.Rnd;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author f14shm4n
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventStartPositions")
public class EventStartPositionList {

    @XmlAttribute(name = "use_group")
    protected boolean useGroup;
    @XmlElement(name = "position")
    protected List<EventStartPosition> positions;

    public List<EventStartPosition> getPositions() {
        return positions;
    }

    public List<EventStartPosition> getPositionForGroup(int id) {
        if (this.isUseGroup()) {
            List<EventStartPosition> rList = new ArrayList<EventStartPosition>();
            for (EventStartPosition pos : this.positions) {
                if (pos.group == id) {
                    rList.add(pos);
                }
            }
            return rList;
        } else {
            return this.getPositions();
        }
    }

    public EventStartPosition getRandomPosition() {
        return this.positions.get(Rnd.get(positions.size() - 1));
    }

    public boolean isUseGroup() {
        return useGroup;
    }
}
