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
package org.openaion.gameserver.controllers;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.templates.zone.ZoneTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import org.openaion.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.ZoneService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.world.zone.FlightZoneInstance;
import org.openaion.gameserver.world.zone.ZoneInstance;


/**
 * @author ATracer
 *
 */
public class FlyController
{
	@SuppressWarnings("unused")
	private static final Logger	log	= Logger.getLogger(FlyController.class);

	private Player player;

	public FlyController(Player player)
	{
		this.player = player;
	}

	/**
	 * 
	 */
	public void onStopGliding()
	{
		if(player.isInState(CreatureState.GLIDING))
		{
			player.unsetState(CreatureState.GLIDING);

			if(player.isInState(CreatureState.FLYING))
			{
				// Check Flight
				if (!checkFlightZone())
					return;
				
				//flight allowed
				player.setFlyState(1);
			}
			else
			{
				player.setFlyState(0);
				player.getLifeStats().triggerFpRestore();
			}

			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
		}
	}

	/**
	 *  Ends flying
	 *  1) by CM_EMOTION (pageDown or fly button press)
	 *  2) from server side during teleportation (abyss gates should not break flying)
	 *  3) when FP is decreased to 0
	 *  4) while gliding if speed drops under 6000, or player is in cannot move state
	 */
	public void endFly()
	{
		// unset flying and gliding
		if(player.isInState(CreatureState.FLYING) || player.isInState(CreatureState.GLIDING))
		{
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.LAND, 0, 0), true);
			int state = player.getState();
			//unset like this to recalculate stats only once
			state &= ~CreatureState.GLIDING.getId();
			player.setState(state);
			player.unsetState(CreatureState.FLYING);
			player.setFlyState(0);

			// this is probably needed to change back fly speed into speed.
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_EMOTE2, 0, 0), true);
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));

			player.getLifeStats().triggerFpRestore();
		}
	}

	/**
     * This method is called to check if you should be able to fly
     * Call with silentCheck == true to avoid client messages
	 */
    public boolean canFly(boolean silentCheck) {
        // Bypass Checks for GM Access Levels
    	if (player.getAccessLevel() < AdminConfig.GM_FLIGHT_FREE) {
	        // Check Zone, Always Disallow Flight in Unknown Zones
	        ZoneInstance currentZone = player.getZoneInstance();
	        if (currentZone == null) {
	        	if (!silentCheck) PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLYING_FORBIDDEN_HERE);
	        	return false;
	        }
	        // Check for allowed flight zones in no fly maps, will always be null if no flight zone and/or not in flight zone
            FlightZoneInstance currentFlightZoneName = null;
	        if (ZoneService.getInstance().mapHasFightZones(player.getWorldId())) {
		        currentFlightZoneName = ZoneService.getInstance().findFlightZoneInCurrentMap(player.getPosition());;
	        }
	        // Check Zone Template, Always override and forbid flying in flyBan = true zones
	        // Overrides freefly for non gm's so certain zones can me made nofly on freefly servers
	        // Does not Override defined fly zones (aether gathering areas etc on no fly maps)
            ZoneTemplate currentZoneTemplate = currentZone.getTemplate();
            if (currentZoneTemplate.isFlightBanned()== true && currentFlightZoneName == null){
	        	if (!silentCheck) PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLYING_FORBIDDEN_HERE);
                return false;
            }
	        // Check Zone Template, Is Flight Allowed?: Default is (0), False if not defined.
            // Check if freefly is enabled, freefly Overrides fly="false" (isFlightAllowed)in template.
            // Use flyBan="true" in Zone Template to override free fly for non GM.
            if (currentZoneTemplate.isFlightAllowed()== false && GSConfig.FREEFLY == false) {
	        	if (!silentCheck) PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLYING_FORBIDDEN_HERE);
			return false;
            }
	    }
		
		// Flight Allowed
        return true;
    }
    
    /**
     * This method is called to start flying
     * (called by CM_EMOTION when pageUp or pressed fly button)
     */
    public boolean startFly() {
    	if (player.getFlyController().canFly(false) == true) {
            //zer0patches TODO: check and optimize
		player.setState(CreatureState.FLYING);
		player.setFlyState(1);
		player.getLifeStats().triggerFpReduce(null);
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_EMOTE2, 0, 0), true);
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
		return true;
    	} else {
        	return false;
    	}
	}

	/**
	 *  Switching to glide mode
	 *  (called by CM_MOVE with VALIDATE_GLIDE movement type)
	 *  
	 *  1) from standing state
	 *  2) from flying state
	 *  
	 *  If from stand to glide - start fp reduce + emotions/stats
	 *  if from fly to glide - only emotions/stats
	 */
	public void switchToGliding()
	{
		if(!player.isInState(CreatureState.GLIDING))
		{
			player.setState(CreatureState.GLIDING);
			player.getLifeStats().triggerFpReduce(null);
			player.setFlyState(2);

			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
		}
	}
	
	public boolean checkFlightZone()
	{
		// Check Flight
		FlightZoneInstance currentFlightZone = null;
		if (ZoneService.getInstance().mapHasFightZones(player.getWorldId()))
		{
			currentFlightZone = ZoneService.getInstance().findFlightZoneInCurrentMap(player.getPosition());
			if (currentFlightZone == null && !player.isGM() && !canFly(true))
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLYING_FORBIDDEN_HERE);
				if(player.isInState(CreatureState.FLYING))
					player.getFlyController().endFly();
				return false;
			}
			else if (currentFlightZone != null && !player.isGM() && !canFly(true))
			{
				if (currentFlightZone.getTop() != 0.0f && currentFlightZone.getTop() < player.getZ())
				{
					PacketSendUtility.broadcastPacket(player, new SM_FORCED_MOVE(player, player.getX(),player.getY(),(currentFlightZone.getTop() - 5f)),true);
					World.getInstance().updatePosition(player, player.getX(), player.getY(), (currentFlightZone.getTop() - 5f),player.getHeading(), false);
					return false;
				}
			}
		}
		else
		{
			ZoneInstance currentZone = player.getZoneInstance();
			if(currentZone != null)
			{
				boolean flightAllowed = currentZone.getTemplate().isFlightAllowed();
				if(!flightAllowed && !player.isGM()  && !canFly(true))
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLYING_FORBIDDEN_HERE);
					if(player.isInState(CreatureState.FLYING))
						player.getFlyController().endFly();
					return false;
				}
			}
		}
		
		return true;
	}
}
