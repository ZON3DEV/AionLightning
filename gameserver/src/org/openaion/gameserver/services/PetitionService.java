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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.PetitionDAO;
import org.openaion.gameserver.model.Petition;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.network.aion.serverpackets.SM_PETITION;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;


/**
 * @author Sylar
 */
public class PetitionService
{
	private static Logger	log	= Logger.getLogger(PetitionService.class);
	
	private static SortedMap<Integer, Petition> registeredPetitions = Collections.synchronizedSortedMap(new TreeMap<Integer, Petition>());
	
	public static final PetitionService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public PetitionService()
	{
		log.info("Loading PetitionService ...");
		Set<Petition> petitions = DAOManager.getDAO(PetitionDAO.class).getPetitions();
		for(Petition p : petitions)
		{
			registeredPetitions.put(p.getPetitionId(), p);
		}
		log.info("Successfully loaded " + registeredPetitions.size() + " database petitions");
	}
	
	public Collection<Petition> getRegisteredPetitions()
	{
		return registeredPetitions.values();
	}
	
	public void deletePetition(int playerObjId)
	{
		synchronized (registeredPetitions)
		{
			for (Iterator<Petition> it = registeredPetitions.values().iterator(); it.hasNext(); )
			{
				Petition p = it.next();

				if(p.getPlayerObjId() == playerObjId)
					it.remove();
			}
		}
		
		DAOManager.getDAO(PetitionDAO.class).deletePetition(playerObjId);
		if(playerObjId > 0 && World.getInstance().findPlayer(playerObjId) != null)
		{
			Player p = World.getInstance().findPlayer(playerObjId);
			PacketSendUtility.sendPacket(p, new SM_PETITION());
		}
		rebroadcastPlayerData();
	}
	
	public void setPetitionReplied(int petitionId)
	{
		int playerObjId = registeredPetitions.get(petitionId).getPlayerObjId();
		DAOManager.getDAO(PetitionDAO.class).setReplied(petitionId);
		registeredPetitions.remove(petitionId);
		rebroadcastPlayerData();
		if(playerObjId > 0 && World.getInstance().findPlayer(playerObjId) != null)
		{
			Player p = World.getInstance().findPlayer(playerObjId);
			PacketSendUtility.sendPacket(p, new SM_PETITION());
		}
	}
	
	public synchronized Petition registerPetition(Player sender, int typeId, String title, String contentText, String additionalData)
	{
		int id = DAOManager.getDAO(PetitionDAO.class).getNextAvailableId();
		Petition ptt = new Petition(id, sender.getObjectId(), typeId, title, contentText, additionalData, 0);
		DAOManager.getDAO(PetitionDAO.class).insertPetition(ptt);
		registeredPetitions.put(ptt.getPetitionId(), ptt);
		broadcastMessageToGM(sender, ptt.getPetitionId());
		return ptt;
	}
	
	private void rebroadcastPlayerData()
	{
		synchronized (registeredPetitions)
		{
			for (Petition p : registeredPetitions.values())
			{
				Player player = World.getInstance().findPlayer(p.getPlayerObjId());
				if(player != null)
					PacketSendUtility.sendPacket(player, new SM_PETITION(p));
			}
		}
	}
	
	private void broadcastMessageToGM(final Player sender, final int petitionId)
	{
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player p)
			{
				if(p.getAccessLevel() > 0)
				{
					PacketSendUtility.sendSysMessage(p, "New Support Petition from: " + sender.getName() + " (#" + petitionId + ")");
				}
				return true;
			}
		});
	}
	
	public boolean hasRegisteredPetition(Player player)
	{
		return hasRegisteredPetition(player.getObjectId());
	}
	
	public boolean hasRegisteredPetition(int playerObjId)
	{
		synchronized (registeredPetitions)
		{
			for (Petition p : registeredPetitions.values())
			{
				if (p.getPlayerObjId() == playerObjId)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Petition getPetition(int playerObjId)
	{
		synchronized (registeredPetitions)
		{
			for(Petition p : registeredPetitions.values())
			{
				if(p.getPlayerObjId() == playerObjId)
					return p;
			}
		}
		return null;
	}
	
	public synchronized int getNextAvailablePetitionId()
	{
		return 0;
	}
	
	public int getWaitingPlayers(int playerObjId)
	{
		int counter = 0;
		
		synchronized (registeredPetitions)
		{
			for(Petition p : registeredPetitions.values())
			{
				if(p.getPlayerObjId() == playerObjId)
					break;
				counter++;
			}
		}
		return counter;
	}
	
	public int calculateWaitTime(int playerObjId)
	{
		int timePerPetition = 15;
		int timeBetweenPetition = 30;
		int result = timeBetweenPetition;
		synchronized (registeredPetitions)
		{
			for(Petition p : registeredPetitions.values())
			{
				if(p.getPlayerObjId() == playerObjId)
					break;
				result += timePerPetition;
				result += timeBetweenPetition;
			}
		}
		return result;
	}
	
	public void onPlayerLogin(Player player)
	{
		if(hasRegisteredPetition(player))
			PacketSendUtility.sendPacket(player, new SM_PETITION(getPetition(player.getObjectId())));
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final PetitionService instance = new PetitionService();
	}

}
