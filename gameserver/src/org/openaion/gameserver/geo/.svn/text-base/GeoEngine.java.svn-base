/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package org.openaion.gameserver.geo;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.templates.WorldMapTemplate;


import aionjHungary.geoEngine.GeoWorldLoader;
import aionjHungary.geoEngine.models.GeoMap;
import aionjHungary.geoEngine.scene.Spatial;


/**
 * @author Mr. Poke
 *
 */
public class GeoEngine
{
	private static Logger	log	= Logger.getLogger(GeoEngine.class);

	private Map<Integer, GeoMap> geoMaps = new HashMap<Integer, GeoMap>();
	
	public static final GeoEngine getInstance()
	{
		return SingletonHolder.instance;
	}

	GeoEngine()
	{
		if(GSConfig.GEODATA_ENABLED)
		{
			Map<String, Spatial> models = GeoWorldLoader.loadMeshs();
			for (WorldMapTemplate map : DataManager.WORLD_MAPS_DATA)
			{
				GeoMap geoMap = new GeoMap(Integer.toString(map.getMapId()), map.getWorldSize());
				if (GeoWorldLoader.loadWorld(map.getMapId(), models, geoMap))
				{
					geoMaps.put(map.getMapId(), geoMap);
				}
			}
			models.clear();
			models = null;
			log.info("Geodata engine: "+geoMaps.size()+" geoMaps loaded!");
		}
		else
		{
			log.info("Geodata engine disabled.");
		}
	}

	public float getZ(int worldId, float x, float y, float z)
	{
		if(GSConfig.GEODATA_ENABLED && geoMaps.containsKey(worldId))
			return geoMaps.get(worldId).getZ(x, y, z);
		else
			return z;
	}

	public boolean canSee(VisibleObject object, VisibleObject target)
	{
		return this.canSee(object.getWorldId(), object.getX(), object.getY(), object.getZ(), target.getX(), target.getY(), target.getZ());
	}

	public boolean canSee(int worldId, float x, float y, float z, float targetX, float targetY, float targetZ)
	{
		if(GSConfig.GEODATA_ENABLED && geoMaps.containsKey(worldId))
			return geoMaps.get(worldId).canSee(x, y, z, targetX, targetY, targetZ);
		else
			return true;
	}

	public GeoMap getGeoMapByWorldId(int worldId)
	{
		return geoMaps.get(worldId);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final GeoEngine	instance	= new GeoEngine();
	}
}
