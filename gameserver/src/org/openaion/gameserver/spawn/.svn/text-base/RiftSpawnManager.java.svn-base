/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.spawn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.controllers.RiftController;
import org.openaion.gameserver.controllers.effect.EffectController;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.NpcTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnGroup;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_DELETE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.utils.Util;
import org.openaion.gameserver.utils.idfactory.IDFactory;
import org.openaion.gameserver.world.NpcKnownList;
import org.openaion.gameserver.world.World;


/**
 * @author ATracer
 * @author Divinity - Offlike Rift System
 *
 */
public class RiftSpawnManager
{
	
	private static final Logger log = Logger.getLogger(RiftSpawnManager.class);
	
	private static final ConcurrentLinkedQueue<Npc> rifts = new ConcurrentLinkedQueue<Npc>();
	
	
	private static final int RIFT_RESPAWN_DELAY	= 120 * 60 * 1000;	// 2 hours
	private static final int RIFT_LIFETIME		= 120 * 60 * 1000;	// 2 hours
	
	private static final Map<String, SpawnGroup> spawnGroups = new HashMap<String, SpawnGroup>();
	
	public static void addRiftSpawnGroup(SpawnGroup spawnGroup)
	{
		spawnGroups.put(spawnGroup.getAnchor(), spawnGroup);
	}

	/**
	 * 
	 */
	public static void startRiftPool()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		
		long TIME_BEFORE_SPAWN_RIFT = 0;
		
		if (calendar.get(Calendar.HOUR_OF_DAY) % 2 == 0)
			TIME_BEFORE_SPAWN_RIFT += 1 * 60 * 60 * 1000; // 1 heure
		
		TIME_BEFORE_SPAWN_RIFT += ((60 - calendar.get(Calendar.MINUTE)) * 60 - calendar.get(Calendar.SECOND)) * 1000;
		
		Timestamp newTime = new Timestamp(System.currentTimeMillis() + TIME_BEFORE_SPAWN_RIFT);
		log.info("Next rifts will spawn in " + TIME_BEFORE_SPAWN_RIFT / 1000 + " seconds, at " + newTime.toString());
		
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				Util.printSection("Rift Manager");
				ArrayList<Integer> rifts = new ArrayList<Integer>();
				int nbRift, rndRift;
				
				for (int i=0; i<4; i++)
				{
					// Generate number of rift for each town
					nbRift = getNbRift();
					
					log.info("Spawning " + nbRift + " rifts for the map : " + getMapName(i));
					
					for (int j=0; j<nbRift; j++)
					{
						rndRift = Rnd.get(i*7, (i+1)*7-1);
						
						// try to avoid duplicate
						while (rifts.contains(rndRift))
							rndRift = Rnd.get(i*7, (i+1)*7-1);
						
						// Save rift spawned
						rifts.add(rndRift);
						
						// Spawnrift
						spawnRift(RiftEnum.values()[rndRift]);
					}
					
					rifts.clear();
				}
			}
		}, TIME_BEFORE_SPAWN_RIFT, RIFT_RESPAWN_DELAY);
	}
	
	/**
	 * 
	 * @return
	 */
	private static int getNbRift()
	{
		double rnd = Rnd.get(0, 99);
		
		/*
		 * 0 : 29%
		 * 1 : 45%
		 * 2 : 15%
		 * 3 : 5%
		 * 4 : 3%
		 * 5 : 2%
		 * 6 : 1%
		 */
		
		if (rnd == 0)
			return 6;
		else if (rnd <= 2)
			return 5;
		else if (rnd <= 5)
			return 4;
		else if (rnd <= 10)
			return 3;
		else if (rnd <= 25)
			return 2;
		else if (rnd <= 70)
			return 1;
		else
			return 0;
		
	}
	
	/**
	 * 
	 * @param mapId
	 * @return
	 */
	private static String getMapName(int mapId)
	{
		switch (mapId)
		{
			case 0:
				return "ELTNEN";
			case 1:
				return "HEIRON";
			case 2:
				return "MORHEIM";
			case 3:
				return "BELUSLAN";
			default:
				return "UNKNOWN";
		}
	}
	
	/**
	 * @param rift
	 */
	private static void spawnRift(RiftEnum rift)
	{
		log.info("Spawning rift : " + rift.name());
		SpawnGroup masterGroup = spawnGroups.get(rift.getMaster());
		SpawnGroup slaveGroup = spawnGroups.get(rift.getSlave());
		
		if(masterGroup == null || slaveGroup == null)
			return;
		
		int instanceCount = World.getInstance().getWorldMap(masterGroup.getMapid()).getInstanceCount();
		
		SpawnTemplate masterTemplate = masterGroup.getNextRandomTemplate();
		SpawnTemplate slaveTemplate = slaveGroup.getNextRandomTemplate();
		
		for(int i = 1; i <= instanceCount; i++)
		{
			Npc slave = spawnInstance(i, masterGroup, slaveTemplate, new RiftController(null, rift));
			spawnInstance(i, masterGroup, masterTemplate, new RiftController(slave, rift));
		}		
	}

	private static Npc spawnInstance(int instanceIndex, SpawnGroup spawnGroup, SpawnTemplate spawnTemplate, RiftController riftController)
	{
		NpcTemplate masterObjectTemplate = DataManager.NPC_DATA.getNpcTemplate(spawnGroup.getNpcid());
		Npc npc = new Npc(IDFactory.getInstance().nextId(),riftController,
			spawnTemplate, masterObjectTemplate);
		
		npc.setKnownlist(new NpcKnownList(npc));
		npc.setEffectController(new EffectController(npc));
		npc.getController().onRespawn();

		World world = World.getInstance();
		world.storeObject(npc);
		world.setPosition(npc, spawnTemplate.getWorldId(), instanceIndex, 
			spawnTemplate.getX(), spawnTemplate.getY(), spawnTemplate.getZ(), spawnTemplate.getHeading());
		world.spawn(npc);
		rifts.add(npc);

		scheduleDespawn(npc);		
		riftController.sendAnnounce();
		
		return npc;
	}

	/**
	 * @param npc
	 */
	private static void scheduleDespawn(final Npc npc)
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if(npc != null && !npc.isSpawned())
				{
					PacketSendUtility.broadcastPacket(npc, new SM_DELETE(npc, 15));
					npc.getController().onDespawn(true);
					World.getInstance().despawn(npc);						
				}	
				rifts.remove(npc);
			}
		}, RIFT_LIFETIME);
	}

	public enum RiftEnum
	{
		ELTNEN_AM("ELTNEN_AM", "MORHEIM_AS", 12, 28, Race.ASMODIANS),
		ELTNEN_BM("ELTNEN_BM", "MORHEIM_BS", 20, 32, Race.ASMODIANS),
		ELTNEN_CM("ELTNEN_CM", "MORHEIM_CS", 35, 36, Race.ASMODIANS),
		ELTNEN_DM("ELTNEN_DM", "MORHEIM_DS", 35, 37, Race.ASMODIANS),
		ELTNEN_EM("ELTNEN_EM", "MORHEIM_ES", 45, 40, Race.ASMODIANS),
		ELTNEN_FM("ELTNEN_FM", "MORHEIM_FS", 50, 40, Race.ASMODIANS),
		ELTNEN_GM("ELTNEN_GM", "MORHEIM_GS", 50, 45, Race.ASMODIANS),
		
		HEIRON_AM("HEIRON_AM", "BELUSLAN_AS", 24, 35, Race.ASMODIANS),
		HEIRON_BM("HEIRON_BM", "BELUSLAN_BS", 36, 35, Race.ASMODIANS),
		HEIRON_CM("HEIRON_CM", "BELUSLAN_CS", 48, 46, Race.ASMODIANS),
		HEIRON_DM("HEIRON_DM", "BELUSLAN_DS", 48, 40, Race.ASMODIANS),
		HEIRON_EM("HEIRON_EM", "BELUSLAN_ES", 60, 60, Race.ASMODIANS),
		HEIRON_FM("HEIRON_FM", "BELUSLAN_FS", 60, 60, Race.ASMODIANS),
		HEIRON_GM("HEIRON_GM", "BELUSLAN_GS", 72, 60, Race.ASMODIANS),
		
		MORHEIM_AM("MORHEIM_AM", "ELTNEN_AS", 12, 28, Race.ELYOS),
		MORHEIM_BM("MORHEIM_BM", "ELTNEN_BS", 20, 32, Race.ELYOS),
		MORHEIM_CM("MORHEIM_CM", "ELTNEN_CS", 35, 36, Race.ELYOS),
		MORHEIM_DM("MORHEIM_DM", "ELTNEN_DS", 35, 37, Race.ELYOS),
		MORHEIM_EM("MORHEIM_EM", "ELTNEN_ES", 45, 40, Race.ELYOS),
		MORHEIM_FM("MORHEIM_FM", "ELTNEN_FS", 50, 40, Race.ELYOS),
		MORHEIM_GM("MORHEIM_GM", "ELTNEN_GS", 50, 45, Race.ELYOS),
		
		BELUSLAN_AM("BELUSLAN_AM", "HEIRON_AS", 24, 35, Race.ELYOS),
		BELUSLAN_BM("BELUSLAN_BM", "HEIRON_BS", 36, 35, Race.ELYOS),
		BELUSLAN_CM("BELUSLAN_CM", "HEIRON_CS", 48, 46, Race.ELYOS),
		BELUSLAN_DM("BELUSLAN_DM", "HEIRON_DS", 48, 40, Race.ELYOS),
		BELUSLAN_EM("BELUSLAN_EM", "HEIRON_ES", 60, 60, Race.ELYOS),
		BELUSLAN_FM("BELUSLAN_FM", "HEIRON_FS", 60, 60, Race.ELYOS),
		BELUSLAN_GM("BELUSLAN_GM", "HEIRON_GS", 72, 60, Race.ELYOS);
		
		private String master;
		private String slave;
		private int entries;
		private int maxLevel;
		private Race destination;
		
		private RiftEnum(String master, String slave, int entries, int maxLevel, Race destination)
		{
			this.master = master;
			this.slave = slave;
			this.entries = entries;
			this.maxLevel = maxLevel;
			this.destination = destination;
		}

		/**
		 * @return the master
		 */
		public String getMaster()
		{
			return master;
		}

		/**
		 * @return the slave
		 */
		public String getSlave()
		{
			return slave;
		}

		/**
		 * @return the entries
		 */
		public int getEntries()
		{
			return entries;
		}

		/**
		 * @return the maxLevel
		 */
		public int getMaxLevel()
		{
			return maxLevel;
		}

		/**
		 * @return the destination
		 */
		public Race getDestination()
		{
			return destination;
		}
	}

	/**
	 * @param activePlayer
	 */
	public static void sendRiftStatus(Player activePlayer)
	{
		for(Npc rift : rifts)
		{
			if(rift.getWorldId() == activePlayer.getWorldId())
			{
				((RiftController) rift.getController()).sendMessage(activePlayer);
			}
		}
	}
}
