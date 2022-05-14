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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

import com.aionemu.gameserver.model.templates.world.WeatherTable;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "weatherData" })
@XmlRootElement(name = "weather")
public class MapWeatherData {

	@XmlElement(name = "map", required = true)
	private List<WeatherTable> weatherData;
	@XmlTransient
	private TIntObjectHashMap<WeatherTable> mapWeather;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		mapWeather = new TIntObjectHashMap<WeatherTable>();

		for (WeatherTable table : weatherData) {
			mapWeather.put(table.getMapId(), table);
		}

		weatherData.clear();
		weatherData = null;
	}

	public WeatherTable getWeather(int mapId) {
		return mapWeather.get(mapId);
	}

	public int size() {
		return mapWeather.size();
	}
}
