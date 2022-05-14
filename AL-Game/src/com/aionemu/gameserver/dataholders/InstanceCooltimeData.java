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

package com.aionemu.gameserver.dataholders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javolution.util.FastMap;

import org.joda.time.DateTime;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.InstanceCooltime;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.utils.Util;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "instance_cooltimes")
public class InstanceCooltimeData {

	@XmlElement(name = "instance_cooltime", required = true)
	protected List<InstanceCooltime> instanceCooltime;
	private FastMap<Integer, InstanceCooltime> instanceCooltimes = new FastMap<Integer, InstanceCooltime>();
	private HashMap<Integer, Integer> syncIdToMapId = new HashMap<Integer, Integer>();
	private FastMap<Integer, List<InstanceCooltime>> syncIdToInstances = new FastMap<Integer, List<InstanceCooltime>>();
	private FastMap<Integer, InstanceCooltime> instanceIdToCooltime = new FastMap<Integer, InstanceCooltime>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (InstanceCooltime tmp : this.instanceCooltime) {
			Util.printRotatingBarHeader(size().intValue());
			this.instanceCooltimes.put(Integer.valueOf(tmp.getWorldId()), tmp);
			this.instanceIdToCooltime.put(Integer.valueOf(tmp.getId()), tmp);
			this.syncIdToMapId.put(Integer.valueOf(tmp.getId()), Integer.valueOf(tmp.getWorldId()));
			if ((tmp.getCount() > 0) && (tmp.getSyncId() > 0)) {
				if (!this.syncIdToInstances.containsKey(Integer.valueOf(tmp.getSyncId()))) {
					this.syncIdToInstances.put(Integer.valueOf(tmp.getSyncId()), new ArrayList<InstanceCooltime>());
				}
				this.syncIdToInstances.get(Integer.valueOf(tmp.getSyncId())).add(tmp);
			}
		}
		this.instanceCooltime.clear();
	}

	/**
	 * @param worldId
	 * @return
	 */
	public InstanceCooltime getInstanceCooltimeByWorldId(int worldId) {
		return instanceCooltimes.get(worldId);
	}

	public int getWorldId(int syncId) {
		if (!syncIdToMapId.containsKey(syncId))
			return 0;
		return syncIdToMapId.get(syncId);
	}

	public long getInstanceEntranceCooltimeById(Player player, int syncId) {
		if (!syncIdToMapId.containsKey(syncId))
			return 0;
		return getInstanceEntranceCooltime(player, syncIdToMapId.get(syncId));
	}

	public long getInstanceEntranceCooltime(Player player, int worldId) {
		int instanceCooldownRate = InstanceService.getInstanceRate(player, worldId);
		long instanceCooltime = 0;
		InstanceCooltime clt = getInstanceCooltimeByWorldId(worldId);
		if (clt != null) {
			instanceCooltime = clt.getEntCoolTime().intValue();
			if (clt.getCoolTimeType().isDaily()) {
				DateTime now = DateTime.now();
				DateTime repeatDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), (int) (instanceCooltime / 100), 0, 0);
				if (now.isAfter(repeatDate)) {
					repeatDate = repeatDate.plusHours(24);
					instanceCooltime = repeatDate.getMillis();
				} else {
					instanceCooltime = repeatDate.getMillis();
				}
			} else if (clt.getCoolTimeType().isWeekly()) {
				String[] days = clt.getTypeValue().split(",");
				instanceCooltime = getUpdateHours(days, (int) (instanceCooltime / 100));
			} else {
				instanceCooltime = System.currentTimeMillis() + instanceCooltime * 60 * 1000;
			}
		}
		if (instanceCooldownRate != 1) {
			instanceCooltime = System.currentTimeMillis() + (instanceCooltime - System.currentTimeMillis()) / instanceCooldownRate;
		}
		return instanceCooltime;
	}

	private long getUpdateHours(String[] days, int hour) {
		DateTime now = DateTime.now();
		DateTime repeatDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), hour, 0, 0);
		int curentDay = now.getDayOfWeek();
		for (String name : days) {
			int day = getDay(name);
			if (day >= curentDay) {
				if (day == curentDay) {
					if (now.isBefore(repeatDate)) {
						return repeatDate.getMillis();
					}
				} else {
					repeatDate = repeatDate.plusDays(day - curentDay);
					return repeatDate.getMillis();
				}
			}
		}
		return repeatDate.plusDays(7 - curentDay + getDay(days[0])).getMillis();
	}

	private int getDay(String day) {
		if (day.equals("Mon")) {
			return 1;
		}
		if (day.equals("Tue")) {
			return 2;
		}
		if (day.equals("Wed")) {
			return 3;
		}
		if (day.equals("Thu")) {
			return 4;
		}
		if (day.equals("Fri")) {
			return 5;
		}
		if (day.equals("Sat")) {
			return 6;
		}
		if (day.equals("Sun")) {
			return 7;
		}
		throw new IllegalArgumentException("Invalid Day: " + day);
	}

	public Integer size() {
		return Integer.valueOf(this.instanceCooltimes.size());
	}
}
