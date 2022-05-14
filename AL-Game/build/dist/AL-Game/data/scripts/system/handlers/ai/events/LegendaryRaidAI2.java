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

package ai.events;

import org.joda.time.DateTime;

import ai.AggressiveNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Ever'
 */
@AIName("LegendaryRaid")
public class LegendaryRaidAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}

	@Override
	protected void handleSpawned() {
		DateTime now = DateTime.now();
		int currentDay = now.getDayOfWeek();
		int currentHour = now.getHourOfDay();
		switch (getNpcId()) {
			case 281810: { // Omega
				if (currentDay == 1 && currentHour == 21) {
					super.handleSpawned();
				} else if (!isAlreadyDead()) {
					getOwner().getController().onDelete();
				}
				break;
			}
			case 281811: { // Ragnarok
				if (currentDay == 1 && currentHour == 19) {
					super.handleSpawned();
				} else if (!isAlreadyDead()) {
					getOwner().getController().onDelete();
				}
				break;
			}
		}
	}

	@Override
	protected void handleDespawned() {
		DateTime now = DateTime.now();
		int currentDay = now.getDayOfWeek();
		int currentHour = now.getHourOfDay();
		switch (getNpcId()) {
			case 281810: { // Omega
				if (currentDay == 1 && currentHour >= 22) {
					super.handleDespawned();
				} else if (!isAlreadyDead()) {
					getOwner().getController().onDelete();
				}
				break;
			}
			case 281811: { // Ragnarok
				if (currentDay == 1 && currentHour == 20) {
					super.handleDespawned();
				} else if (!isAlreadyDead()) {
					getOwner().getController().onDelete();
				}
				break;
			}
		}
	}
}
