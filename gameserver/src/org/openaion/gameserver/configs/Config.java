/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.configs;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.openaion.commons.configuration.ConfigurableProcessor;
import org.openaion.commons.database.DatabaseConfig;
import org.openaion.commons.utils.PropertiesUtils;
import org.openaion.gameserver.GameServer;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.configs.main.CacheConfig;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.configs.main.DropConfig;
import org.openaion.gameserver.configs.main.EnchantsConfig;
import org.openaion.gameserver.configs.main.EventConfig;
import org.openaion.gameserver.configs.main.FallDamageConfig;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.configs.main.GroupConfig;
import org.openaion.gameserver.configs.main.HTMLConfig;
import org.openaion.gameserver.configs.main.LegionConfig;
import org.openaion.gameserver.configs.main.NpcMovementConfig;
import org.openaion.gameserver.configs.main.PeriodicSaveConfig;
import org.openaion.gameserver.configs.main.PricesConfig;
import org.openaion.gameserver.configs.main.RateConfig;
import org.openaion.gameserver.configs.main.ShutdownConfig;
import org.openaion.gameserver.configs.main.SiegeConfig;
import org.openaion.gameserver.configs.main.TaskManagerConfig;
import org.openaion.gameserver.configs.main.ThreadConfig;
import org.openaion.gameserver.configs.network.FloodConfig;
import org.openaion.gameserver.configs.network.IPConfig;
import org.openaion.gameserver.configs.network.NetworkConfig;


/**
 * @author -Nemesiss-
 * @author SoulKeeper
 */
public class Config
{
	/**
	 * Logger for this class.
	 */
	protected static final Logger	log	= Logger.getLogger(Config.class);

	/**
	 * Initialize all configs in org.openaion.gameserver.configs package
	 */
	public static void load()
	{
		try
		{
			Properties props = PropertiesUtils.load(GameServer.CONFIGURATION_FILE); 
			ConfigurableProcessor.process(Config.class, props);			
			ConfigurableProcessor.process(AdminConfig.class, props);
			ConfigurableProcessor.process(LegionConfig.class, props);
			ConfigurableProcessor.process(DropConfig.class, props);
			ConfigurableProcessor.process(RateConfig.class, props);
			ConfigurableProcessor.process(CacheConfig.class, props);
			ConfigurableProcessor.process(ShutdownConfig.class, props);
			ConfigurableProcessor.process(TaskManagerConfig.class, props);
			ConfigurableProcessor.process(GroupConfig.class, props);
			ConfigurableProcessor.process(CustomConfig.class, props);
			ConfigurableProcessor.process(EnchantsConfig.class, props);
			ConfigurableProcessor.process(FallDamageConfig.class, props);
			ConfigurableProcessor.process(GSConfig.class, props);
			ConfigurableProcessor.process(NpcMovementConfig.class, props);
			ConfigurableProcessor.process(PeriodicSaveConfig.class, props);
			ConfigurableProcessor.process(PricesConfig.class, props);
			ConfigurableProcessor.process(SiegeConfig.class, props);
			ConfigurableProcessor.process(ThreadConfig.class, props);
			ConfigurableProcessor.process(NetworkConfig.class, props);
			ConfigurableProcessor.process(DatabaseConfig.class, props);
			ConfigurableProcessor.process(HTMLConfig.class, props);
			ConfigurableProcessor.process(FloodConfig.class, props);
			ConfigurableProcessor.process(EventConfig.class, props);
		}
		catch(Exception e)
		{
			log.fatal("Can't load gameserver configuration: ", e);
			throw new Error("Can't load gameserver configuration: ", e);
		}

		IPConfig.load();
	}
}