/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.main.SiegeConfig;
import org.openaion.gameserver.controllers.movement.ActionObserver;
import org.openaion.gameserver.controllers.movement.ActionObserver.ObserverType;
import org.openaion.gameserver.dao.SiegeDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.DescriptionId;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.alliance.PlayerAllianceGroup;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.model.legion.Legion;
import org.openaion.gameserver.model.legion.LegionMember;
import org.openaion.gameserver.model.siege.AethericField;
import org.openaion.gameserver.model.siege.Artifact;
import org.openaion.gameserver.model.siege.FortressGate;
import org.openaion.gameserver.model.siege.FortressGateArtifact;
import org.openaion.gameserver.model.siege.FortressGeneral;
import org.openaion.gameserver.model.siege.Influence;
import org.openaion.gameserver.model.siege.InstancePortal;
import org.openaion.gameserver.model.siege.SiegeLocation;
import org.openaion.gameserver.model.siege.SiegeRace;
import org.openaion.gameserver.model.siege.SiegeType;
import org.openaion.gameserver.model.templates.siege.AethericFieldTemplate;
import org.openaion.gameserver.model.templates.siege.ArtifactTemplate;
import org.openaion.gameserver.model.templates.siege.DefenseReward;
import org.openaion.gameserver.model.templates.siege.FortressGateArtifactTemplate;
import org.openaion.gameserver.model.templates.siege.FortressGateTemplate;
import org.openaion.gameserver.model.templates.siege.SiegeGuardTemplate;
import org.openaion.gameserver.model.templates.siege.SiegeRewardTemplate;
import org.openaion.gameserver.model.templates.siege.SiegeSpawnList;
import org.openaion.gameserver.model.templates.siege.SiegeSpawnLocationTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_ABYSS_ARTIFACT_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_ABYSS_ARTIFACT_INFO3;
import org.openaion.gameserver.network.aion.serverpackets.SM_FORTRESS_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_FORTRESS_STATUS;
import org.openaion.gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import org.openaion.gameserver.network.aion.serverpackets.SM_SIEGE_AETHERIC_FIELDS;
import org.openaion.gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;


/**
 * @author Sarynth, Sylar
 */
public class SiegeService
{
	private static Logger	log	= Logger.getLogger(SiegeService.class);	
	
	private Map<Integer, SiegeLocation> locations;
	private Map<Integer, SiegeSpawnList> spawnLists = new HashMap<Integer, SiegeSpawnList>();
	
	private List<String> errorReportItems = new ArrayList<String>();
	
	private Map<Integer, List<Integer>> fortressRelatedObjectIds = new HashMap<Integer, List<Integer>>();

	private Map<Integer, HashMap<Integer, Integer>> apRecords = new HashMap<Integer, HashMap<Integer,Integer>>();
	
	private long lastCalculationMillis;
	
	@SuppressWarnings("deprecation")
	private SiegeService()
	{
		if (SiegeConfig.SIEGE_ENABLED)
		{
			log.info("Loading Siege Location Data...");
			// Load Siege Status from Database
			locations = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations();
			DAOManager.getDAO(SiegeDAO.class).loadSiegeLocations(locations);
			log.info("Successfully loaded " + locations.size() + " siege locations");
			
			// Load Siege Spawns
			log.info("Loading siege spawn data ...");
			for(SiegeLocation loc : locations.values())
			{
				// Initialize Fortress Spawn Cache
				if(fortressRelatedObjectIds.containsKey(loc.getLocationId()))
					fortressRelatedObjectIds.remove(loc.getLocationId());
				fortressRelatedObjectIds.put(loc.getLocationId(), new ArrayList<Integer>());
				
				// Initialize Fortress AP records cache
				if(apRecords.containsKey(loc.getLocationId()))
					apRecords.remove(loc.getLocationId());
				apRecords.put(loc.getLocationId(), new HashMap<Integer, Integer>());
				
				// Load Spawnlist
				SiegeSpawnList list = DataManager.SIEGE_SPAWN_DATA.getSpawnsForLocation(loc.getLocationId());
				if(list != null)
					spawnLists.put(loc.getLocationId(), list);
			}
			
			log.info("Successfully loaded " + spawnLists.size() + " siege spawnlists");
			
			// Start spawn
			log.info("Starting spawn of siege locations ....");
			
			// 1. Spawn Fortresses
			for(SiegeSpawnList sl : spawnLists.values())
			{
				if(locations.get(sl.getLocationId()).getSiegeType() == SiegeType.FORTRESS)
				{
					Date now = new Date();
					
					boolean isVulNow = goesVulnerable(sl.getLocationId(), now.getDay(), now.getHours());
					if(!isVulNow)
						isVulNow = goesVulnerable(sl.getLocationId(), now.getDay(), now.getHours() - 1);
					
					locations.get(sl.getLocationId()).setVulnerable(isVulNow);
					
					boolean isVulNext = goesVulnerable(sl.getLocationId(), now.getDay(), now.getHours() + 2);
					if(!isVulNext)
						isVulNext = goesVulnerable(sl.getLocationId(), now.getDay(), now.getHours() + 1);
						
					locations.get(sl.getLocationId()).setNextState(isVulNext ? 1 : 0);
					
					if(locations.get(sl.getLocationId()).isVulnerable())
						spawnLocation(sl.getLocationId(), locations.get(sl.getLocationId()).getRace(), "SIEGE");
					else
						spawnLocation(sl.getLocationId(), locations.get(sl.getLocationId()).getRace(), "PEACE");
				}
			}
			
			// Start Timers
			lastCalculationMillis = System.currentTimeMillis();
			ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){
				@Override
				public void run()
				{
					processVulnerabilityCalculation();
				}
			}, getNextRoundHour() * 1000, 7200000);
			log.info("Setting next vulnerability calculation in " + (getNextRoundHour() * 1000) + " ms");
			
			// 2. Spawn Artifacts
			for(final SiegeSpawnList sl : spawnLists.values())
			{
				if(locations.get(sl.getLocationId()).getSiegeType() == SiegeType.ARTIFACT)
					spawnLocation(sl.getLocationId(), locations.get(sl.getLocationId()).getRace(), "SIEGE");
			}
			
			log.info("Successfully completed siege spawning");
			writeLogAbyssStatus();
		}
		else
		{
			log.info("Siege Disabled by Config.");
			locations = new FastMap<Integer, SiegeLocation>();
		}
	}
	
	@SuppressWarnings("deprecation")
	private int getNextRoundHour()
	{
		Date now = new Date();
		int minutes = now.getMinutes();
		int seconds = now.getSeconds();
		int totalElapsedSecs = (minutes * 60) + seconds;
		switch(now.getHours())
		{
			case 0:
			case 2:
			case 4:
			case 6:
			case 8:
			case 10:
			case 12:
			case 14:
			case 16:
			case 18:
			case 20:
			case 22:
				return (3600 - totalElapsedSecs) + 3600;
		}
		now = null;
		return (3600 - totalElapsedSecs);
	}
	
	private boolean goesVulnerable(int locationId, int day, int hour)
	{
		if(hour > 23)
		{
			hour = hour - 24;
			day++;
		}
		if(SiegeConfig.SIEGE_SCHEDULE_TYPE == 1)
			return goesVulnerablePvE(locationId, day, hour);
		else if(SiegeConfig.SIEGE_SCHEDULE_TYPE == 2)
			return goesVulnerablePvP(locationId, day, hour);
		else
			return true;
		
	}
	
	private boolean goesVulnerablePvE(int locationId, int day, int hour)
	{
		switch(locationId)
		{
			case 1011:
				if((day == 3 || day == 6) && hour == 20)
					return true;
				break;
			case 1131:
				if((day == 1 && hour == 22) || (day == 4 && hour == 18) || (day == 0 && hour == 18))
					return true;
				break;
			case 1132:
				if((day == 1 && hour == 18) || (day == 4 && hour == 22) || (day == 0 && hour == 18))
					return true;
				break;
			case 1141:
				if((day == 2 && hour == 18) || (day == 3 && hour == 14) || (day == 5 && hour == 18) || (day == 0 && hour == 22))
					return true;
				break;
			case 1211:
				if((day == 3 && hour == 14) || (day == 5 && hour == 18))
					return true;
				break;
			case 1221:
				if((day == 2 && hour == 22) || (day == 0 && hour == 18))
					return true;
				break;
			case 1231:
				if((day == 1 && hour == 18) || (day == 5 && hour == 22))
					return true;
				break;
			case 1241:
				if((day == 2 && hour == 18) || (day == 4 && hour == 22) || (day == 0 && hour == 18))
					return true;
				break;
			case 1251:
				if((day == 3 && hour == 14) || (day == 6 && hour == 20))
					return true;
				break;
			case 2011:
				if((day == 2 && hour == 22) || (day == 5 && hour == 22))
					return true;
				break;
			case 2021:
				if((day == 1 && hour == 18) || (day == 4 && hour == 22))
					return true;
				break;
			case 3011:
				if((day == 4 && hour == 18) || (day == 6 && hour == 20))
					return true;
				break;
			case 3021:
				if((day == 1 && hour == 22) || (day == 5 && hour == 18))
					return true;
				break;
		}
		return false;
	}
	
	private boolean goesVulnerablePvP(int locationId, int day, int hour)
	{
		switch(locationId)
		{
			 case 1011:
    if((day == 0 && hour == 0) || (day == 0 && hour == 8) || (day == 0 && hour == 12) || (day == 0 && hour == 20) || (day == 1 && hour == 2) || (day == 1 && hour == 6) || (day == 1 && hour == 14) || (day == 1 && hour == 22) || (day == 2 && hour == 4) || (day == 2 && hour == 8) || (day == 2 && hour == 16) || (day == 2 && hour == 20) || (day == 3 && hour == 2) || (day == 3 && hour == 10) || (day == 3 && hour == 14) || (day == 3 && hour == 22) || (day == 4 && hour == 0) || (day == 4 && hour == 8) || (day == 4 && hour == 16) || (day == 4 && hour == 20) || (day == 5 && hour == 2) || (day == 5 && hour == 6) || (day == 5 && hour == 14) || (day == 5 && hour == 18) || (day == 6 && hour == 4) || (day == 6 && hour == 8) || (day == 6 && hour == 16) || (day == 6 && hour == 20))
     return true;
    break;
   case 1131:
    if((day == 1 && hour == 0) || (day == 1 && hour == 8) || (day == 1 && hour == 12) || (day == 1 && hour == 20) || (day == 0 && hour == 2) || (day == 0 && hour == 6) || (day == 0 && hour == 14) || (day == 0 && hour == 22) || (day == 3 && hour == 4) || (day == 3 && hour == 8) || (day == 3 && hour == 16) || (day == 3 && hour == 20) || (day == 4 && hour == 2) || (day == 4 && hour == 10) || (day == 4 && hour == 14) || (day == 4 && hour == 22) || (day == 5 && hour == 0) || (day == 5 && hour == 8) || (day == 5 && hour == 16) || (day == 5 && hour == 20) || (day == 6 && hour == 2) || (day == 6 && hour == 6) || (day == 6 && hour == 14) || (day == 6 && hour == 18) || (day == 2 && hour == 4) || (day == 2 && hour == 8) || (day == 2 && hour == 16) || (day == 2 && hour == 20))
     return true;
    break;
   case 1132:
    if((day == 2 && hour == 0) || (day == 2 && hour == 8) || (day == 2 && hour == 12) || (day == 2 && hour == 20) || (day == 3 && hour == 2) || (day == 3 && hour == 6) || (day == 3 && hour == 14) || (day == 3 && hour == 22) || (day == 0 && hour == 4) || (day == 0 && hour == 8) || (day == 0 && hour == 16) || (day == 0 && hour == 20) || (day == 1 && hour == 2) || (day == 1 && hour == 10) || (day == 1 && hour == 14) || (day == 1 && hour == 22) || (day == 6 && hour == 0) || (day == 6 && hour == 8) || (day == 6 && hour == 16) || (day == 6 && hour == 20) || (day == 4 && hour == 2) || (day == 4 && hour == 6) || (day == 4 && hour == 14) || (day == 4 && hour == 18) || (day == 5 && hour == 4) || (day == 5 && hour == 8) || (day == 5 && hour == 16) || (day == 5 && hour == 20))
     return true;
    break;
   case 1141:
    if((day == 3 && hour == 0) || (day == 3 && hour == 8) || (day == 3 && hour == 12) || (day == 3 && hour == 20) || (day == 2 && hour == 2) || (day == 2 && hour == 6) || (day == 2 && hour == 14) || (day == 2 && hour == 22) || (day == 6 && hour == 4) || (day == 6 && hour == 8) || (day == 6 && hour == 16) || (day == 6 && hour == 20) || (day == 5 && hour == 2) || (day == 5 && hour == 10) || (day == 5 && hour == 14) || (day == 5 && hour == 22) || (day == 0 && hour == 0) || (day == 0 && hour == 8) || (day == 0 && hour == 16) || (day == 0 && hour == 20) || (day == 4 && hour == 2) || (day == 4 && hour == 6) || (day == 4 && hour == 14) || (day == 4 && hour == 18) || (day == 1 && hour == 4) || (day == 1 && hour == 8) || (day == 1 && hour == 16) || (day == 1 && hour == 20))
     return true;
    break;
   case 1211:
    if((day == 4 && hour == 0) || (day == 4 && hour == 8) || (day == 4 && hour == 12) || (day == 4 && hour == 20) || (day == 5 && hour == 2) || (day == 5 && hour == 6) || (day == 5 && hour == 14) || (day == 5 && hour == 22) || (day == 1 && hour == 4) || (day == 1 && hour == 8) || (day == 1 && hour == 16) || (day == 1 && hour == 20) || (day == 2 && hour == 2) || (day == 2 && hour == 10) || (day == 2 && hour == 14) || (day == 2 && hour == 22) || (day == 3 && hour == 0) || (day == 3 && hour == 8) || (day == 3 && hour == 16) || (day == 3 && hour == 20) || (day == 0 && hour == 2) || (day == 0 && hour == 6) || (day == 0 && hour == 14) || (day == 0 && hour == 18) || (day == 6 && hour == 4) || (day == 6 && hour == 8) || (day == 6 && hour == 16) || (day == 6 && hour == 20))
     return true;
    break;
   case 1221:
    if((day == 5 && hour == 0) || (day == 5 && hour == 8) || (day == 5 && hour == 12) || (day == 5 && hour == 20) || (day == 6 && hour == 2) || (day == 6 && hour == 6) || (day == 6 && hour == 14) || (day == 6 && hour == 22) || (day == 4 && hour == 4) || (day == 4 && hour == 8) || (day == 4 && hour == 16) || (day == 4 && hour == 20) || (day == 0 && hour == 2) || (day == 0 && hour == 10) || (day == 0 && hour == 14) || (day == 0 && hour == 22) || (day == 2 && hour == 0) || (day == 2 && hour == 8) || (day == 2 && hour == 16) || (day == 2 && hour == 20) || (day == 1 && hour == 2) || (day == 1 && hour == 6) || (day == 1 && hour == 14) || (day == 1 && hour == 18) || (day == 3 && hour == 4) || (day == 3 && hour == 8) || (day == 3 && hour == 16) || (day == 3 && hour == 20))
     return true;
    break;
   case 1231:
    if((day == 6 && hour == 0) || (day == 6 && hour == 8) || (day == 6 && hour == 12) || (day == 6 && hour == 20) || (day == 4 && hour == 2) || (day == 4 && hour == 6) || (day == 4 && hour == 14) || (day == 4 && hour == 22) || (day == 5 && hour == 4) || (day == 5 && hour == 8) || (day == 5 && hour == 16) || (day == 5 && hour == 20) || (day == 3 && hour == 2) || (day == 3 && hour == 10) || (day == 3 && hour == 14) || (day == 3 && hour == 22) || (day == 1 && hour == 0) || (day == 1 && hour == 8) || (day == 1 && hour == 16) || (day == 1 && hour == 20) || (day == 2 && hour == 2) || (day == 2 && hour == 6) || (day == 2 && hour == 14) || (day == 2 && hour == 18) || (day == 0 && hour == 4) || (day == 0 && hour == 8) || (day == 0 && hour == 16) || (day == 0 && hour == 20))
     return true;
    break;
   case 1241:
    if((day == 3 && hour == 0) || (day == 3 && hour == 8) || (day == 3 && hour == 12) || (day == 3 && hour == 20) || (day == 2 && hour == 2) || (day == 2 && hour == 6) || (day == 2 && hour == 14) || (day == 2 && hour == 22) || (day == 6 && hour == 4) || (day == 6 && hour == 8) || (day == 6 && hour == 16) || (day == 6 && hour == 20) || (day == 5 && hour == 2) || (day == 5 && hour == 10) || (day == 5 && hour == 14) || (day == 5 && hour == 22) || (day == 0 && hour == 0) || (day == 0 && hour == 8) || (day == 0 && hour == 16) || (day == 0 && hour == 20) || (day == 4 && hour == 2) || (day == 4 && hour == 6) || (day == 4 && hour == 14) || (day == 4 && hour == 18) || (day == 1 && hour == 4) || (day == 1 && hour == 8) || (day == 1 && hour == 16) || (day == 1 && hour == 20))
     return true;
    break;
   case 1251:
    if((day == 0 && hour == 0) || (day == 0 && hour == 8) || (day == 0 && hour == 12) || (day == 0 && hour == 20) || (day == 1 && hour == 2) || (day == 1 && hour == 6) || (day == 1 && hour == 14) || (day == 1 && hour == 22) || (day == 2 && hour == 4) || (day == 2 && hour == 8) || (day == 2 && hour == 16) || (day == 2 && hour == 20) || (day == 3 && hour == 2) || (day == 3 && hour == 10) || (day == 3 && hour == 14) || (day == 3 && hour == 22) || (day == 4 && hour == 0) || (day == 4 && hour == 8) || (day == 4 && hour == 16) || (day == 4 && hour == 20) || (day == 5 && hour == 2) || (day == 5 && hour == 6) || (day == 5 && hour == 14) || (day == 5 && hour == 18) || (day == 6 && hour == 4) || (day == 6 && hour == 8) || (day == 6 && hour == 16) || (day == 6 && hour == 20))
     return true;
    break;
   case 2011:
    if((day == 0 && hour == 10) || (day == 0 && hour == 10) || (day == 0 && hour == 16) || (day == 0 && hour == 20) || (day == 1 && hour == 0) || (day == 1 && hour == 10) || (day == 1 && hour == 14) || (day == 1 && hour == 18) || (day == 2 && hour == 12) || (day == 2 && hour == 16) || (day == 2 && hour == 20) || (day == 2 && hour == 22) || (day == 3 && hour == 0) || (day == 3 && hour == 12) || (day == 3 && hour == 16) || (day == 3 && hour == 20) || (day == 4 && hour == 10) || (day == 4 && hour == 16) || (day == 4 && hour == 20) || (day == 4 && hour == 22) || (day == 5 && hour == 0) || (day == 5 && hour == 10) || (day == 5 && hour == 14) || (day == 5 && hour == 18) || (day == 6 && hour == 12) || (day == 6 && hour == 16) || (day == 6 && hour == 20) || (day == 6 && hour == 22))
     return true;
    break;
   case 2021:
    if((day == 0 && hour == 0) || (day == 0 && hour == 10) || (day == 0 && hour == 14) || (day == 0 && hour == 18) || (day == 1 && hour == 10) || (day == 1 && hour == 16) || (day == 1 && hour == 20) || (day == 1 && hour == 22) || (day == 2 && hour == 0) || (day == 2 && hour == 12) || (day == 2 && hour == 16) || (day == 2 && hour == 20) || (day == 3 && hour == 12) || (day == 3 && hour == 16) || (day == 3 && hour == 20) || (day == 3 && hour == 22) || (day == 4 && hour == 0) || (day == 4 && hour == 10) || (day == 4 && hour == 14) || (day == 4 && hour == 18) || (day == 5 && hour == 10) || (day == 5 && hour == 16) || (day == 5 && hour == 20) || (day == 5 && hour == 22) || (day == 6 && hour == 0) || (day == 6 && hour == 12) || (day == 6 && hour == 16) || (day == 6 && hour == 20))
     return true;
    break;
   case 3011:
    if((day == 0 && hour == 12) || (day == 0 && hour == 16) || (day == 0 && hour == 20) || (day == 0 && hour == 22) || (day == 1 && hour == 0) || (day == 1 && hour == 12) || (day == 1 && hour == 16) || (day == 1 && hour == 20) || (day == 2 && hour == 0) || (day == 2 && hour == 10) || (day == 2 && hour == 14) || (day == 2 && hour == 18) || (day == 3 && hour == 10) || (day == 3 && hour == 16) || (day == 3 && hour == 20) || (day == 3 && hour == 22) || (day == 4 && hour == 12) || (day == 4 && hour == 16) || (day == 4 && hour == 20) || (day == 4 && hour == 22) || (day == 5 && hour == 0) || (day == 5 && hour == 12) || (day == 5 && hour == 16) || (day == 5 && hour == 20) || (day == 6 && hour == 0) || (day == 6 && hour == 10) || (day == 6 && hour == 14) || (day == 6 && hour == 18))
     return true;
    break;
   case 3021:
    if((day == 0 && hour == 0) || (day == 0 && hour == 12) || (day == 0 && hour == 16) || (day == 0 && hour == 20) || (day == 1 && hour == 12) || (day == 1 && hour == 16) || (day == 1 && hour == 20) || (day == 1 && hour == 22) || (day == 2 && hour == 10) || (day == 2 && hour == 16) || (day == 2 && hour == 20) || (day == 2 && hour == 22) || (day == 3 && hour == 0) || (day == 3 && hour == 10) || (day == 3 && hour == 14) || (day == 3 && hour == 18) || (day == 4 && hour == 0) || (day == 4 && hour == 12) || (day == 4 && hour == 16) || (day == 4 && hour == 20) || (day == 5 && hour == 12) || (day == 5 && hour == 16) || (day == 5 && hour == 20) || (day == 5 && hour == 22) || (day == 6 && hour == 10) || (day == 6 && hour == 16) || (day == 6 && hour == 20) || (day == 6 && hour == 22))
					return true;
				break;
		}
		return false;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final SiegeService instance = new SiegeService();
	}
	
	public static final SiegeService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public Map<Integer, SiegeLocation> getSiegeLocations()
	{
		return locations;
	}
	
	/**
	 * @return siege time
	 */
	public int getSiegeTime()
	{
		return getNextRoundHour();
	}
	
	public boolean isSiegeNpc(int objectId)
	{
		for(Entry<Integer, List<Integer>> entry : fortressRelatedObjectIds.entrySet())
		{
			if(entry.getValue().contains(objectId))
				return true;
		}
		return false;
	}
	
	public int getSiegeNpcLocation(int objectId)
	{
		for(Entry<Integer, List<Integer>> entry : fortressRelatedObjectIds.entrySet())
		{
			if(entry.getValue().contains(objectId))
				return entry.getKey();
		}
		return -1;
	}
	
	public long getLastCalculationMillis()
	{
		return lastCalculationMillis;
	}
	
	public void processVulnerabilityCalculation()
	{
		log.info("Processing vulnerability calculation");
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				for(final SiegeLocation loc : locations.values())
				{
					Date now = new Date();
					boolean vul = goesVulnerable(loc.getLocationId(), now.getDay(), now.getHours());
					if(vul)
					{
						clearFortress(loc.getLocationId());
						// spawn siege
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							
							@Override
							public void run()
							{
								spawnLocation(loc.getLocationId(), loc.getRace(), "SIEGE");
							}
						}, 5000);
						loc.setVulnerable(true);
					}
					else if(!vul && loc.isVulnerable())
					{
						if(loc.getLegionId() != 0)
						{
							Legion legion = LegionService.getInstance().getLegion(loc.getLegionId());
							DefenseReward dr = loc.getLocationTemplate().getDefenseReward();
							if(legion != null)
							{
								LegionMember member = null;
								for(Integer pid : legion.getLegionMembers())
								{
									LegionMember m = LegionService.getInstance().getLegionMember(pid);
									if(m.isBrigadeGeneral())
										member = m;
								}
								if(member != null && dr != null)
								{
									String title = loc.getLocationId() + ",2,";
									switch(loc.getRace())
									{
										case ASMODIANS: title += "1"; break;
										case ELYOS: title += "0"; break;
										default: break;
									}
									String message = (System.currentTimeMillis() / 1000) + ",2";
									MailService.getInstance().sendSystemMail("ABYSS_REWARD_MAIL", title, message, member.getObjectId(), ItemService.newItem(186000030, dr.getGold(), "", member.getObjectId(), 0, 0), 0);
									MailService.getInstance().sendSystemMail("ABYSS_REWARD_MAIL", title, message, member.getObjectId(), ItemService.newItem(186000031, dr.getSilver(), "", member.getObjectId(), 0, 0), 0);
								}
							}
						}
						loc.setVulnerable(false);
						clearFortress(loc.getLocationId());
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							
							@Override
							public void run()
							{
								spawnLocation(loc.getLocationId(), loc.getRace(), "PEACE");
							}
						}, 5000);
					}
					boolean vulnext = goesVulnerable(loc.getLocationId(), now.getDay(), now.getHours() + 2);
					loc.setNextState(vulnext ? 1 : 0);
					broadcastUpdate(loc);
				}
			}
		}, 3000);
	}
	
	public void clearArtifact(Artifact artifact)
	{
		int artifactId = artifact.getLocationId();
		for(Integer creature : artifact.getRelatedSpawnIds())
		{
			AionObject obj = World.getInstance().findAionObject(creature);
			if(obj instanceof Creature)
			{
				Creature c = (Creature)obj;
				if(c.isSpawned())
					World.getInstance().despawn(c);
				DataManager.SPAWNS_DATA.removeSpawn(c.getSpawn());
				c.setAi(null);
				c.getController().delete();
			}
		}
		DataManager.SPAWNS_DATA.removeSpawn(artifact.getSpawn());
		artifact.getController().delete();
		log.info("Cleaned ARTIFACT " + artifactId);
	}
	
	public void clearFortress(int fortressId)
	{
		List<Integer> spawnedCreatures = fortressRelatedObjectIds.get(fortressId);
		if(spawnedCreatures != null)
		{
			for(Integer creature : spawnedCreatures)
			{
				AionObject obj = World.getInstance().findAionObject(creature);
				if(obj != null && obj instanceof Creature)
				{
					Creature c = (Creature)obj;
					if(c.isSpawned())
						World.getInstance().despawn(c);
					DataManager.SPAWNS_DATA.removeSpawn(c.getSpawn());
					c.getAi().clearDesires();
					c.setAi(null);
					c.getController().delete();
				}
			}
		}
		fortressRelatedObjectIds.get(fortressId).clear();
		log.info("Cleaned FORTRESS " + fortressId);
	}
	
	public SiegeLocation getSiegeLocation(int locationId)
	{
		return locations.get(locationId);
	}
	
	public void capture(int locationId, SiegeRace race)
	{
		this.capture(locationId, race, 0);
	}
	
	public void capture(int locationId, SiegeRace race, int legionId)
	{
		SiegeLocation sLoc = locations.get(locationId);
		sLoc.setRace(race);
		String legionName = "";
		if(legionId > 0)
		{
			Legion legion = LegionService.getInstance().getLegion(legionId);
			
			if(legion != null)
			{
				sLoc.setLegionId(legion.getLegionId());
				legionName = legion.getLegionName();
			}
			else
			{
				sLoc.setLegionId(0);
				if(race == SiegeRace.ASMODIANS)
					legionName = "Asmodians";
				else if(race == SiegeRace.BALAUR)
					legionName = "Balaurs";
				else if(race == SiegeRace.ELYOS)
					legionName = "Elyos";
			}
		}
		else
		{
			sLoc.setLegionId(0);
			if(race == SiegeRace.ASMODIANS)
				legionName = "Asmodians";
			else if(race == SiegeRace.BALAUR)
				legionName = "Balaurs";
			else if(race == SiegeRace.ELYOS)
				legionName = "Elyos";
		}
		
		if (sLoc.getSiegeType() == SiegeType.FORTRESS)
			sLoc.setVulnerable(false);
		
		DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(sLoc);
		
		DAOManager.getDAO(SiegeDAO.class).insertSiegeLogEntry(legionName, "CAPTURE", System.currentTimeMillis() / 1000, locationId);
		
		broadcastUpdate(sLoc);
		Influence.getInstance().recalculateInfluence();
		
		checkBalaureaBosses(sLoc);
	}
	
	public void broadcastUpdate(SiegeLocation loc)
	{
		SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO(loc);
		broadcast(pkt);
	}
	
	public void broadcastUpdate()
	{
		SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO();
		broadcast(pkt);
	}
	
	private void broadcast(final SM_SIEGE_LOCATION_INFO pkt)
	{
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run (Player player)
			{
				PacketSendUtility.sendPacket(player, pkt);
				PacketSendUtility.sendPacket(player, new SM_INFLUENCE_RATIO());
				return true;
			}
		});
	}
	
	public synchronized void recordAPGain(int locationId, int playerObjId, int apCount)
	{
		int newVal = 0;
		if(apRecords.get(locationId).containsKey(playerObjId))
		{
			newVal = apRecords.get(locationId).get(playerObjId);
			apRecords.get(locationId).remove(playerObjId);
		}
		newVal += apCount;
		apRecords.get(locationId).put(playerObjId, newVal);
	}
	
	public boolean spawnLocation(int locationId, SiegeRace spawnRace, String spawnType)
	{
		/*
		 * String spawnType: SIEGE|PEACE|REINFORCEMENT
		 */
		
		SiegeSpawnList list = spawnLists.get(locationId);
		SiegeLocation location = locations.get(locationId);
		
		if(list == null || location == null)
			return false;
		
		List<SiegeGuardTemplate> guards;
		if(spawnType.equals("SIEGE"))
			guards = list.getGuards().getSiegeGuards();
		else if(spawnType.equals("PEACE"))
			guards = list.getGuards().getPeaceGuards();
		else if(spawnType.equals("REINFORCEMENT"))
			guards = list.getGuards().getReinforcementsGuards();
		else
			return false;
		
		if(guards == null)
		{
			log.error("Cannot spawn a siege location without any guard. Aborting location " + locationId);
			return false;
		}
		
		if(location.getSiegeType() == SiegeType.ARTIFACT)
		{
			ArtifactTemplate artifactTemplate = list.getArtifactTemplate();
			
			if(artifactTemplate == null)
			{
				log.error("Cannot spawn an ARTIFACT location without an artifact XML definition.");
				return false;
			}
			
			if(artifactTemplate.getBaseInfo() == null)
			{
				log.error("Cannot spawn an ARTIFACT with no baseInfo definition !");
				return false;
			}
			
			if(artifactTemplate.getEffectTemplate() == null)
			{
				log.error("Cannot spawn an ARTIFACT with no related effect !");
				return false;
			}
			
			if(artifactTemplate.getProtectorTemplate() == null)
			{
				log.error("Cannot spawn an ARTIFACT with no protector !");
				return false;
			}
			
			int spawnedCounter = 0;
			
			// Everything is fine, let's go ! :)
			// 1. Spawn Artifact + Protector
			Artifact artifact = SpawnEngine.getInstance().spawnArtifact(locationId, spawnRace, artifactTemplate);
			spawnedCounter += 2;
			
			// 2. Spawn Guards
			for(SiegeGuardTemplate guardTemplate : guards)
			{
				int npcId = guardTemplate.getNpcId(spawnRace);
				if(npcId == 0)
					continue;
				for(SiegeSpawnLocationTemplate spawnLoc : guardTemplate.getSpawnLocations())
				{
					SpawnTemplate gST = SpawnEngine.getInstance().addNewSpawn(location.getLocationTemplate().getWorldId(), 1, npcId, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), (byte)spawnLoc.getH(), 0, 0, false, true);
					VisibleObject result = SpawnEngine.getInstance().spawnObject(gST, 1);
					if(result != null)
					{
						artifact.registerRelatedSpawn(result.getObjectId());
						spawnedCounter++;
					}
				}
			}
			
			log.info("Spawned ARTIFACT " + locationId + ": " + spawnedCounter);
			return true;
			
		}
		else if(location.getSiegeType() == SiegeType.FORTRESS)
		{
			int spawnedCounter = 0;
			apRecords.get(location.getLocationId()).clear();
			if(spawnType.equals("PEACE"))
			{
				// 1. Guards
				for(SiegeGuardTemplate guardTemplate : guards)
				{
					int npcId = guardTemplate.getNpcId(spawnRace);
					if(npcId == 0)
						continue;
					for(SiegeSpawnLocationTemplate spawnLoc : guardTemplate.getSpawnLocations())
					{
						SpawnTemplate gST = SpawnEngine.getInstance().addNewSpawn(location.getLocationTemplate().getWorldId(), 1, npcId, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), (byte)spawnLoc.getH(), 0, 0, false);
						VisibleObject result = SpawnEngine.getInstance().spawnObject(gST, 1);
						if(result != null)
						{
							if(!fortressRelatedObjectIds.get(locationId).contains(result.getObjectId()))
								fortressRelatedObjectIds.get(locationId).add(result.getObjectId());
							spawnedCounter++;
						}
					}
					
					if(list.getArtifactTemplate() != null)
					{
						ArtifactTemplate artifactTemplate = list.getArtifactTemplate();
						if(artifactTemplate.getBaseInfo() == null)
						{
							log.error("Cannot spawn an ARTIFACT with no baseInfo definition !");
							return false;
						}
						
						if(artifactTemplate.getEffectTemplate() == null)
						{
							log.error("Cannot spawn an ARTIFACT with no related effect !");
							return false;
						}
						
						Artifact fArtifact = SpawnEngine.getInstance().spawnArtifact(locationId, spawnRace, artifactTemplate);
						
						if(fArtifact != null)
						{
							fortressRelatedObjectIds.get(locationId).add(fArtifact.getObjectId());
							spawnedCounter++;
						}
						
					}
					
				}
				
				// 2. Instance Portal
				// Spawned only if fortress is controlled by asmo or elyos, on invulnerable state.
				if(spawnRace.getRaceId() != 2)
				{
					if(list.getInstancePortalTemplate() != null && list.getInstancePortalTemplate().getBaseInfo().getNpcId(spawnRace) != 0)
				{
					InstancePortal portal = SpawnEngine.getInstance().spawnInstancePortal(locationId, spawnRace, list.getInstancePortalTemplate());
					if(portal != null)
					{
						if(!fortressRelatedObjectIds.get(locationId).contains(portal.getObjectId()))
						fortressRelatedObjectIds.get(locationId).add(portal.getObjectId());
						spawnedCounter++;
					}
				}
				else
				log.error("No Instance portal defined for FORTRESS " + locationId);
				}
			}
			else if(spawnType.equals("REINFORCEMENT"))
			{
				// Not implemented yet
			}
			else if(spawnType.equals("SIEGE"))
			{
				// SIEGE !!
				// 1. Guards
				for(SiegeGuardTemplate guardTemplate : guards)
				{
					int npcId = guardTemplate.getNpcId(spawnRace);
					if(npcId == 0)
						continue;
					for(SiegeSpawnLocationTemplate spawnLoc : guardTemplate.getSpawnLocations())
					{
						SpawnTemplate gST = SpawnEngine.getInstance().addNewSpawn(location.getLocationTemplate().getWorldId(), 1, npcId, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), (byte)spawnLoc.getH(), 0, 0, false);
						VisibleObject result = SpawnEngine.getInstance().spawnObject(gST, 1);
						if(result != null)
						{
							if(!fortressRelatedObjectIds.get(locationId).contains(result.getObjectId()))
								fortressRelatedObjectIds.get(locationId).add(result.getObjectId());
							spawnedCounter++;
						}
					}
				}
				
				// 2. Fortress Guardian Deity
				if(list.getFortressGeneralTemplate() == null)
				{
					log.error("No Fortress General defined for FORTRESS " + locationId);
					return false;
				}
				FortressGeneral general = SpawnEngine.getInstance().spawnFortressGeneral(locationId, spawnRace, list.getFortressGeneralTemplate());
				if(general != null)
				{
					if(!fortressRelatedObjectIds.get(locationId).contains(general.getObjectId()))
						fortressRelatedObjectIds.get(locationId).add(general.getObjectId());
					spawnedCounter++;
				}
				
				// 3. Fortress Gates
				List<FortressGateTemplate> fortressGatesTemplates = list.getFortressGatesTemplates();
				if(fortressGatesTemplates != null && fortressGatesTemplates.size() > 0)
				{
					for(FortressGateTemplate fgTemplate : fortressGatesTemplates)
					{
						FortressGate gate = SpawnEngine.getInstance().spawnFortressGate(locationId, spawnRace, fgTemplate);
						if(gate != null && fgTemplate.getArtifact() != null)
						{
							spawnedCounter++;
							fortressRelatedObjectIds.get(locationId).add(gate.getObjectId());
							FortressGateArtifactTemplate aTemplate = fgTemplate.getArtifact();
							FortressGateArtifact fga = SpawnEngine.getInstance().spawnFortressGateArtifact(locationId, spawnRace, aTemplate);
							if(fga != null)
							{
								fga.setRelatedGate(gate);
								spawnedCounter++;
								fortressRelatedObjectIds.get(locationId).add(fga.getObjectId());
							}
						}
					}
				}
				
				// 4. Fortress Artifact (if present)
				if(list.getArtifactTemplate() != null)
				{
					ArtifactTemplate artifactTemplate = list.getArtifactTemplate();
					if(artifactTemplate.getBaseInfo() == null)
					{
						log.error("Cannot spawn an ARTIFACT with no baseInfo definition !");
						return false;
					}
					
					if(artifactTemplate.getEffectTemplate() == null)
					{
						log.error("Cannot spawn an ARTIFACT with no related effect !");
						return false;
					}
					
					Artifact fArtifact = SpawnEngine.getInstance().spawnArtifact(locationId, spawnRace, artifactTemplate);
					
					if(fArtifact != null)
					{
						fortressRelatedObjectIds.get(locationId).add(fArtifact.getObjectId());
						spawnedCounter++;
					}
					
				}
				
				// 5. Aetheric Field
				if(list.getAethericFieldTemplate() != null)
				{
					AethericFieldTemplate afTemplate = list.getAethericFieldTemplate();
					if(afTemplate.getBaseInfo() == null)
					{
						log.error("Missing definition informations for Aetheric Field");
					}
					else
					{
						AethericField generator = SpawnEngine.getInstance().spawnAethericGenerator(locationId, spawnRace, afTemplate);
						spawnedCounter++;
						fortressRelatedObjectIds.get(locationId).add(generator.getObjectId());
					}
				}

				log.info("Spawned FORTRESS/SIEGE " + locationId + ": " + spawnedCounter);
				return true;
				
			}
			else
			{
				log.error("Unknown spawnType: " + spawnType);
				return false;
			}
		}
		else
		{
			log.error("Cannot spawn siege type " + location.getSiegeType().name() + ": no such handler");
			return false;
		}
		
		return true;
		
	}
	
	public void onArtifactCaptured(Artifact artifact)
	{
		final int artifactId = artifact.getLocationId();
		SiegeService.getInstance().capture(artifact.getLocationId(), SiegeRace.BALAUR);
		clearArtifact(artifact);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				spawnLocation(artifactId, SiegeRace.BALAUR, "SIEGE");
			}
		}, 5000);
	}
	
	public void onArtifactCaptured(Artifact artifact, Player taker)
	{
		final int artifactId = artifact.getLocationId();
		SiegeRace newRace = null;
		if(taker.getCommonData().getRace() == Race.ASMODIANS)
			newRace = SiegeRace.ASMODIANS;
		else if(taker.getCommonData().getRace() == Race.ELYOS)
			newRace = SiegeRace.ELYOS;
		
		if(newRace == null)
			return;
		
		final SiegeRace nr = newRace;
		
		int legionId = 0;
		if(taker.getLegion() != null)
			legionId = taker.getLegion().getLegionId();
		
		SiegeService.getInstance().capture(artifact.getLocationId(), newRace, legionId);
		clearArtifact(artifact);
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				spawnLocation(artifactId, nr, "SIEGE");
			}
		}, 5000);
	}
	
	public void onFortressCaptured(final FortressGeneral general, Creature lastAttacker)
	{
		final int fortressId = general.getFortressId();
		
		SiegeService.getInstance().capture(fortressId, SiegeRace.BALAUR, 0);
		SiegeService.getInstance().clearFortress(fortressId);

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				SiegeService.getInstance().spawnLocation(fortressId, SiegeRace.BALAUR, "PEACE");
			}
		}, 5000);
	}
	
	public void onFortressCaptured(final FortressGeneral general, Creature lastAttacker, FastList<PlayerGroup> rewardGroups, FastList<PlayerAllianceGroup> rewardAlliances)
	{
		long captureTime = System.currentTimeMillis() / 1000;
		
		SortedMap<Integer, Integer> apCache = new TreeMap<Integer, Integer>();
		for(Entry<Integer, Integer> apResult : apRecords.get(general.getFortressId()).entrySet())
		{
			Player player = World.getInstance().findPlayer(apResult.getKey());
			if(player == null)
				continue;
			if(((general.getObjectTemplate().getRace() == Race.GCHIEF_LIGHT)&&(player.getCommonData().getRace()==Race.ELYOS))||((general.getObjectTemplate().getRace() == Race.GCHIEF_DARK)&&(player.getCommonData().getRace()==Race.ASMODIANS)))
				continue;
			apCache.put(apResult.getValue(), apResult.getKey());
		}
		
		ArrayList<Integer> finalPlayerOids = new ArrayList<Integer>();
		for(int i=0; i < apCache.size(); i++)
		{
			int lastKey = apCache.lastKey();
			finalPlayerOids.add(apCache.get(lastKey));
			apCache.remove(lastKey);
		}
		
		final Race newRace;
		
		if(general.getObjectTemplate().getRace() == Race.DRAKAN)
		{
			newRace = World.getInstance().findPlayer(finalPlayerOids.get(0)).getCommonData().getRace();
		}
		else
		{
			if(general.getObjectTemplate().getRace() == Race.GCHIEF_LIGHT)
				newRace = Race.ASMODIANS;
			else 
				newRace = Race.ELYOS;
		}
		
		for(int i = finalPlayerOids.size() - 1; i >= 0; i--)
		{
			Integer pid = finalPlayerOids.get(i);
			Player p = World.getInstance().findPlayer(pid);
			if(p.getCommonData().getRace() != newRace)
				finalPlayerOids.remove(pid);
		}
		
		int legionId = 0;
		
		for(Integer pid : finalPlayerOids)
		{
			if(legionId == 0)
			{
				Player pl = World.getInstance().findPlayer(pid);
				if(pl != null && pl.getLegion() != null)
					legionId = pl.getLegion().getLegionId();
			}
			else
				break;
		}
		
		// Alert other faction that fort is lost
		if(locations.get(general.getFortressId()).getLegionId() != 0)
		{
			final Legion lostLegion = LegionService.getInstance().getLegion(locations.get(general.getFortressId()).getLegionId());
			if(lostLegion != null)
			{
				World.getInstance().doOnAllPlayers(new Executor<Player>(){

					@Override
					public boolean run(Player object)
					{
						if(object.getCommonData().getRace() != newRace)
						{
							PacketSendUtility.sendPacket(object, new SM_SYSTEM_MESSAGE(1320005, lostLegion.getLegionName(), getLocationName(general.getFortressId())));
						}
						return true;
					}
				}, true);
			}
		}
		
		if(legionId != 0)
		{
			final Legion gainLegion = LegionService.getInstance().getLegion(legionId);
			if(gainLegion != null)
			{
				World.getInstance().doOnAllPlayers(new Executor<Player>(){

					@Override
					public boolean run(Player object)
					{
						if(object.getCommonData().getRace() == newRace)
						{
							PacketSendUtility.sendPacket(object, new SM_SYSTEM_MESSAGE(1320003, gainLegion.getLegionName(), getLocationName(general.getFortressId())));
						}
						return true;
					}
				}, true);
			}
		}
		
		int rewardedCounter = 0;
		for(SiegeRewardTemplate rewardTemplate : locations.get(general.getFortressId()).getLocationTemplate().getSiegeRewards())
		{
			if(finalPlayerOids.size() == 0)
				break;
			
			int gradeRewardCount = rewardTemplate.getTop() - rewardedCounter;

			for(int j=0; j < gradeRewardCount; j++)
			{
				if(finalPlayerOids.size() == 0)
					break;				
				int playerId = finalPlayerOids.get(0);
				String title = general.getFortressId() + ",1,";
				switch(newRace)
				{
					case ASMODIANS: title += "1"; break;
					case ELYOS: title += "0"; break;
					default: break;
				}
				String message = captureTime + "," + rewardTemplate.getGrade();
				MailService.getInstance().sendSystemMail("ABYSS_REWARD_MAIL", title, message, playerId, ItemService.newItem(rewardTemplate.getItemId(), rewardTemplate.getItemCount(), "", playerId, 0, 0), 0);
				rewardedCounter++;
				finalPlayerOids.remove(0);
			}
			
		}
		
		final Race nr = newRace;
		// Send enemies back to landing
		general.getKnownList().doOnAllPlayers(new Executor<Player>(){

			@Override
			public boolean run(Player object)
			{
				if(general.getObjectTemplate().getRace() == Race.DRAKAN)
				{
					if(object.getCommonData().getRace() != nr)
					{
						if(object.getCommonData().getRace() == Race.ELYOS)
							TeleportService.teleportTo(object, 400010000, 2867, 1034, 1528, 0);
						else
							TeleportService.teleportTo(object, 400010000, 1078, 2839, 1636, 0);
					}
				}
				else
				{
					if(object.getCommonData().getRace() == general.getObjectTemplate().getRace())
					{
						if(object.getCommonData().getRace() == Race.ELYOS)
							TeleportService.teleportTo(object, 400010000, 2867, 1034, 1528, 0);
						else
							TeleportService.teleportTo(object, 400010000, 1078, 2839, 1636, 0);
					}						
				}
				return false;
			}
		}, true);
		
		final SiegeRace sr;
		if(newRace == Race.ELYOS)
			sr = SiegeRace.ELYOS;
		else 
			sr = SiegeRace.ASMODIANS;
		
		capture(general.getFortressId(), sr, legionId);
			
		clearFortress(general.getFortressId());
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			
			@Override
			public void run()
			{
				spawnLocation(general.getFortressId(), sr, "PEACE");
			}
		}, 5000);
	}
	
	/**
	 * Alerts a player about currently vulnerable fortresses
	 * @param p
	 */
	public void alertVulnerable(Player p)
	{
		for(SiegeLocation loc : locations.values())
		{
			if(loc.getSiegeType() == SiegeType.FORTRESS && loc.isVulnerable())
			{
				PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1301040, getLocationName(loc.getLocationId())));
			}
		}
	}
	
	public DescriptionId getLocationName(int fortressId)
	{
		switch(fortressId)
		{
			case 1011: return new DescriptionId(400910*2+1);
			case 1012: return new DescriptionId(301566*2+1);
			case 1013: return new DescriptionId(301567*2+1);
			case 1014: return new DescriptionId(301568*2+1);
			case 1015: return new DescriptionId(301569*2+1);
			case 1016: return new DescriptionId(301570*2+1);
			case 1017: return new DescriptionId(301571*2+1);
			case 1018: return new DescriptionId(301572*2+1);
			case 1019: return new DescriptionId(301573*2+1);
			case 1020: return new DescriptionId(308331*2+1);
			case 1131: return new DescriptionId(400911*2+1);
			case 1132: return new DescriptionId(400912*2+1);
			case 1133: return new DescriptionId(301653*2+1);
			case 1134: return new DescriptionId(301764*2+1);
			case 1135: return new DescriptionId(301875*2+1);
			case 1141: return new DescriptionId(400913*2+1);
			case 1142: return new DescriptionId(301929*2+1);
			case 1143: return new DescriptionId(302057*2+1);
			case 1144: return new DescriptionId(302075*2+1);
			case 1145: return new DescriptionId(302093*2+1);
			case 1146: return new DescriptionId(302111*2+1);
			case 1211: return new DescriptionId(400914*2+1);
			case 1212: return new DescriptionId(302129*2+1);
			case 1213: return new DescriptionId(302132*2+1);
			case 1214: return new DescriptionId(306054*2+1);
			case 1215: return new DescriptionId(305141*2+1);
			case 1221: return new DescriptionId(400915*2+1);
			case 1222: return new DescriptionId(302231*2+1);
			case 1223: return new DescriptionId(305144*2+1);
			case 1231: return new DescriptionId(400916*2+1);
			case 1232: return new DescriptionId(302356*2+1);
			case 1233: return new DescriptionId(305147*2+1);
			case 1241: return new DescriptionId(400917*2+1);
			case 1242: return new DescriptionId(302484*2+1);
			case 1243: return new DescriptionId(305150*2+1);
			case 1251: return new DescriptionId(400918*2+1);
			case 1252: return new DescriptionId(304626*2+1);
			case 1253: return new DescriptionId(304755*2+1);
			case 1254: return new DescriptionId(304798*2+1);
			case 2011: return new DescriptionId(401288*2+1);
			case 2012: return new DescriptionId(319968*2+1);
			case 2013: return new DescriptionId(319971*2+1);
			case 2021: return new DescriptionId(401290*2+1); 
			case 2022: return new DescriptionId(320772*2+1);
			case 2023: return new DescriptionId(320775*2+1);
			case 3011: return new DescriptionId(401292*2+1); 
			case 3012: return new DescriptionId(320994*2+1);
			case 3013: return new DescriptionId(320997*2+1);
			case 3021: return new DescriptionId(401294*2+1);
			case 3022: return new DescriptionId(321216*2+1);
			case 3023: return new DescriptionId(321219*2+1);
		}
		return new DescriptionId(0);
	}
	
	public int getFortressTitleId(int fortressId)
	{
		switch(fortressId)
		{
			case 1011: return 314331;
			case 1131: return 314323;
			case 1132: return 314324;
			case 1141: return 314325;
			case 1211: return 314326;
			case 1221: return 314328;
			case 1231: return 314329;
			case 1241: return 314330;
			case 1251: return 314327;
			case 2011: return 314389;
			case 2021: return 314390;
			case 3011: return 314391;
			case 3021: return 314392;
		}
		return 0;
	}
	
	public void onPlayerLogin(final Player player)
	{
		//alertVulnerable(player);
		PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO(locations.values()));
		PacketSendUtility.sendPacket(player, new SM_SIEGE_AETHERIC_FIELDS(locations.values()));
		PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO3(locations.values()));
		PacketSendUtility.sendPacket(player, new SM_FORTRESS_STATUS());
		
		for(final SiegeLocation loc : locations.values())
		{
			if(loc.getSiegeType() == SiegeType.FORTRESS)
			{
				PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(loc.getLocationId(), 0));
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					
					@Override
					public void run()
					{
						PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(loc.getLocationId(), 1));
					}
				}, 1500);				
			}
		}
		
	}
	
	private void writeLogAbyssStatus()
	{
		log.info("===== ABYSS STATUS ======");
		int counterVul = 0;
		int counterInvul = 0;
		for(SiegeLocation loc : locations.values())
		{
			if(loc.isVulnerable())
				counterVul++;
			else
				counterInvul++;
		}
		log.info("Vulnerable Fortresses: " + counterVul);
		log.info("Invulnerable Fortresses: " + counterInvul);
	}
	
	public void registerError(String errorMessage)
	{
		errorReportItems.add(errorMessage);
	}
	
	private void flushErrorReport()
	{
		errorReportItems.clear();
	}
	
	public boolean writeReport()
	{
		try
		{
			FileWriter writer = new FileWriter("fortress-report.txt");
			BufferedWriter out = new BufferedWriter(writer);
			out.write("=FortressErrorReport[Items:" + errorReportItems.size() + "]=\n");
			for(String m : errorReportItems)
			{
				out.write(m + "\n");
			}
			out.close();
			flushErrorReport();
			return true;
		}
		catch(Exception e)
		{
			log.error("Cannot write fortress error report", e);
			return false;
		}
	}
	
	private void checkBalaureaBosses(SiegeLocation sLoc)
	{
		//Enraged Mastarius spawn
		if (sLoc.getLocationTemplate().getId() == 3011 || sLoc.getLocationTemplate().getId() == 3021)
		{
			SiegeLocation sLoc1 = locations.get(3011);
			SiegeLocation sLoc2 = locations.get(3021);
			if (sLoc1.getRace() == SiegeRace.ELYOS && sLoc2.getRace() == SiegeRace.ELYOS)
			{
				final SiegeLocation locBoss = locations.get(3111);
				locBoss.setVulnerable(true);
				broadcastUpdate(locBoss);
				
				SpawnEngine spawnEngine = SpawnEngine.getInstance();
				SpawnTemplate spawn = spawnEngine.addNewSpawn(220070000, 1, 258205, 1821.76f, 1976.65f, 391.25f, (byte)0, 0, 0, true, true);
				final Npc boss = (Npc)spawnEngine.spawnObject(spawn, 1);
				//notification on boss appear
				World.getInstance().doOnAllPlayers(new Executor<Player> () {
					@Override
					public boolean run(Player player)
					{
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400318));
						return true;
					}
				});
				//notification on boss die
				boss.getObserveController().attach(new ActionObserver(ObserverType.DEATH){
					@Override
					public void died(Creature creature)
					{		
						final Player killer = creature.getAggroList().getMostPlayerDamage();
						locBoss.setVulnerable(false);
						broadcastUpdate(locBoss);
						
						World.getInstance().doOnAllPlayers(new Executor<Player> () {
							@Override
							public boolean run(Player player)
							{
								PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400323, killer.getCommonData().getRace(),killer.getName()));
								return true;
							}
						});
					}
				});
				//despawn after 2 hours
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						if (boss != null)
						{
							boss.getController().delete();
							locBoss.setVulnerable(false);
							broadcastUpdate(locBoss);
							World.getInstance().doOnAllPlayers(new Executor<Player> () {
								@Override
								public boolean run(Player player)
								{
									PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400320));
									return true;
								}
							});
						}
					}
				}, 2* 60 * 60 * 1000);
			}
		}
		
		//Enraged Veille spawn
		if (sLoc.getLocationTemplate().getId() == 2011 || sLoc.getLocationTemplate().getId() == 2021)
		{
			SiegeLocation sLoc1 = locations.get(2011);
			SiegeLocation sLoc2 = locations.get(2021);
			if (sLoc1.getRace() == SiegeRace.ASMODIANS && sLoc2.getRace() == SiegeRace.ASMODIANS)
			{
				final SiegeLocation locBoss = locations.get(2111);
				locBoss.setVulnerable(true);
				broadcastUpdate(locBoss);
				
				SpawnEngine spawnEngine = SpawnEngine.getInstance();
				SpawnTemplate spawn = spawnEngine.addNewSpawn(210050000, 1, 258200, 1079.61f, 1492.91f, 404.125f, (byte)0, 0, 0, true, true);
				final Npc boss = (Npc)spawnEngine.spawnObject(spawn, 1);
				//world notification
				World.getInstance().doOnAllPlayers(new Executor<Player> () {
					@Override
					public boolean run(Player player)
					{
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400317));
						return true;
					}
				});
				//notification on boss die
				boss.getObserveController().attach(new ActionObserver(ObserverType.DEATH){
					@Override
					public void died(Creature creature)
					{	
						final Player killer = creature.getAggroList().getMostPlayerDamage();
						locBoss.setVulnerable(false);
						broadcastUpdate(locBoss);

						World.getInstance().doOnAllPlayers(new Executor<Player> () {
							@Override
							public boolean run(Player player)
							{
								PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400324, killer.getCommonData().getRace(),killer.getName()));
								return true;
							}
						});
					}
				});
				//despawn after 2 hours
				ThreadPoolManager.getInstance().schedule(new Runnable(){

					@Override
					public void run()
					{
						if (boss != null)
						{
							boss.getController().delete();
							locBoss.setVulnerable(false);
							broadcastUpdate(locBoss);
							World.getInstance().doOnAllPlayers(new Executor<Player> () {
								@Override
								public boolean run(Player player)
								{
									PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400319));
									return true;
								}
							});
						}
					}
				}, 2* 60 * 60 * 1000);
			}
		}
	}
}
