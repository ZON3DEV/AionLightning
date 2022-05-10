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

package pirate.events;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.utils.PacketSendUtility;
import pirate.debug.DebugInfo;
import pirate.events.enums.EventRergisterState;

/**
 *
 * @author flashman
 */
public class EventRegisterInfo extends DebugInfo<EventRergisterState> {

    public EventRegisterInfo(EventRergisterState state, String message) {
        super(state, message);
    }

    public EventRegisterInfo(EventRergisterState state) {
        super(state);
    }

    public void sendMessageToGroupMebmers(PlayerGroup pg) {
        switch (this.state) {
            case GROUP_NOT_REGISTRED:
                break;
            default:
                for (Player p : pg.getMembers()) {
                    if (p != null && p.isOnline()) {
                        PacketSendUtility.sendMessage(p, message);
                    }
                }
                break;
        }
    }
}
