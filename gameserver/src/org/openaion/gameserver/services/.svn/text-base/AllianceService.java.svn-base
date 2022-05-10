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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.openaion.gameserver.configs.main.GroupConfig;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.alliance.PlayerAlliance;
import org.openaion.gameserver.model.alliance.PlayerAllianceEvent;
import org.openaion.gameserver.model.alliance.PlayerAllianceMember;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Monster;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.RequestResponseHandler;
import org.openaion.gameserver.model.group.LootGroupRules;
import org.openaion.gameserver.model.group.LootRuleType;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.network.aion.serverpackets.SM_ALLIANCE_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_ALLIANCE_MEMBER_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_INSTANCE_COOLDOWN;
import org.openaion.gameserver.network.aion.serverpackets.SM_LEAVE_GROUP_MEMBER;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_SHOW_BRAND;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.quest.QuestEngine;
import org.openaion.gameserver.quest.model.QuestCookie;
import org.openaion.gameserver.restrictions.RestrictionsManager;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.utils.idfactory.IDFactory;
import org.openaion.gameserver.utils.stats.StatFunctions;
import org.openaion.gameserver.world.WorldType;

import javolution.util.FastMap;


/**
 * @author Sarynth
 */
public class AllianceService
{
	/**
	 * 
	 * @return alliance service
	 */
	public static final AllianceService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	/**
	 * Caching remove group member schedule
	 */
	private FastMap<Integer, ScheduledFuture<?>>	playerAllianceRemovalTasks;

	/**
	 * Caching alliance members
	 */
	private final FastMap<Integer, PlayerAlliance> allianceMembers;
	
	public AllianceService()
	{
		allianceMembers = new FastMap<Integer, PlayerAlliance>();
		playerAllianceRemovalTasks = new FastMap<Integer, ScheduledFuture<?>>();
	}

	/**
	 * This method will add a member to the group member cache
	 * 
	 * @param player
	 */
	private void addAllianceMemberToCache(Player player)
	{
		if(!allianceMembers.containsKey(player.getObjectId()))
			allianceMembers.put(player.getObjectId(), player.getPlayerAlliance());
	}
	
	/**
	 * 
	 * @param playerObjId
	 */
	private void removeAllianceMemberFromCache(int playerObjId)
	{
		if(allianceMembers.containsKey(playerObjId))
			allianceMembers.remove(playerObjId);
	}

	
	/**
	 * @param playerObjId
	 * @return returns true if player is in the cache
	 */
	public boolean isAllianceMember(int playerObjId)
	{
		return allianceMembers.containsKey(playerObjId);
	}

	/**
	 * Returns the player's alliance -- Required when Relogging
	 * 
	 * @param playerObjId
	 * @return PlayerAlliance
	 */
	public PlayerAlliance getPlayerAlliance(int playerObjId)
	{
		return allianceMembers.get(playerObjId);
	}
	
	/**
	 * 
	 * @param playerObjectId
	 * @param task
	 */
	private void addAllianceRemovalTask(int playerObjectId, ScheduledFuture<?> task)
	{
		if (!playerAllianceRemovalTasks.containsKey(playerObjectId))
			playerAllianceRemovalTasks.put(playerObjectId, task);
	}
	
	/**
	 * 
	 * @param playerObjectId
	 */
	private void cancelRemovalTask(int playerObjectId)
	{
		if(playerAllianceRemovalTasks.containsKey(playerObjectId))
		{
			playerAllianceRemovalTasks.get(playerObjectId).cancel(true);
			playerAllianceRemovalTasks.remove(playerObjectId);
		}
	}
	
	/**
	 * 
	 * @param player
	 */
	public void scheduleRemove(final Player player)
	{
		ScheduledFuture<?> future = ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				removeMemberFromAlliance(player.getPlayerAlliance(), player.getObjectId(), PlayerAllianceEvent.LEAVE_TIMEOUT);
			}
		}, GroupConfig.ALLIANCE_REMOVE_TIME * 1000);
		
		addAllianceRemovalTask(player.getObjectId(), future);
		player.getPlayerAlliance().onPlayerDisconnect(player);
	}
	
	/**
	 * 
	 * @param inviter
	 * @param invited
	 */
	public void invitePlayerToAlliance(final Player inviter, final Player invited)
	{
		if(RestrictionsManager.canInviteToAlliance(inviter, invited))
		{
			RequestResponseHandler responseHandler = getResponseHandler(inviter, invited);

			boolean result = invited.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_REQUEST_ALLIANCE_INVITE, responseHandler);
			
			if(result)
			{
				if (invited.isInGroup())
				{
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_INVITED_HIS_PARTY(invited.getName()));
				}
				else
				{
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_FORCE_INVITED_HIM(invited.getName()));
				}
				
				PacketSendUtility.sendPacket(invited, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_REQUEST_ALLIANCE_INVITE, 0, inviter.getName()));
			}
		}
	}

	/**
	 * @param inviter
	 * @param invited
	 * @return requestResponseHandler
	 */
	private RequestResponseHandler getResponseHandler(final Player inviter, final Player invited)
	{
		RequestResponseHandler responseHandler = new RequestResponseHandler(inviter){
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				List<Player> playersToAdd = new ArrayList<Player>();
				PlayerAlliance alliance = inviter.getPlayerAlliance();
				
				if (alliance == null)
				{
					alliance = new PlayerAlliance(IDFactory.getInstance().nextId(), inviter.getObjectId());
					
					// Collect Inviter Group
					if (inviter.isInGroup())
					{
						PlayerGroup group = inviter.getPlayerGroup();

						for(Player pl : group.getMembers())
						{
							if(!pl.isInAlliance() && pl.isOnline())
							{
								GroupService.getInstance().removePlayerFromGroup(pl);
								playersToAdd.add(pl);
							}
						}
					}
					else
					{
						if(!inviter.isInAlliance() && inviter.isOnline())
							playersToAdd.add(inviter);
					}
				}
				else if (invited.isInGroup() && (invited.getPlayerGroup().size() + alliance.size()) > 24)
				{
					PacketSendUtility.sendPacket(invited, SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_FAILED_NOT_ENOUGH_SLOT());
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_FAILED_NOT_ENOUGH_SLOT());
					return;
				}
				else if (alliance.size() == 24)
				{
					PacketSendUtility.sendPacket(invited, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_CANT_ADD_NEW_MEMBER());
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_CANT_ADD_NEW_MEMBER());
					return;
				}

				// Collect Invited Group
				if (invited.isInGroup())
				{
					PlayerGroup group = invited.getPlayerGroup();

					for(Player pl : group.getMembers())
					{
						if(!pl.isInAlliance() && pl.isOnline())
						{
							playersToAdd.add(pl);
						}
					}
				}
				else
				{
					if(!invited.isInAlliance() && invited.isOnline())
						playersToAdd.add(invited);
				}
				
				// Finally, send packets and add players.
				for (Player member : playersToAdd)
				{
					addMemberToAlliance(alliance, member);
				}
			}

			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_REJECT_INVITATION(responder.getName()));
			}
		};
		return responseHandler;
	}
	
	/**
	 * 
	 * @param alliance
	 * @param newMember
	 */
	protected void addMemberToAlliance(PlayerAlliance alliance, Player newMember)
	{
		alliance.addMember(newMember);
		addAllianceMemberToCache(newMember);
		
		PacketSendUtility.sendPacket(newMember, new SM_ALLIANCE_INFO(alliance));
		PacketSendUtility.sendPacket(newMember, new SM_SHOW_BRAND(0, 0, 0));
		PacketSendUtility.sendPacket(newMember, SM_SYSTEM_MESSAGE.STR_FORCE_ENTERED_FORCE());
		
		broadcastAllianceMemberInfo(alliance, newMember.getObjectId(), PlayerAllianceEvent.ENTER);
		sendOtherMemberInfo(alliance, newMember);
	}


	/**
	 * 
	 * @param alliance
	 * @param playerObjectId
	 * @param allianceGroupId
	 * @param secondObjectId
	 */
	public void handleGroupChange(PlayerAlliance alliance, int playerObjectId, int allianceGroupId, int secondObjectId)
	{
		if (allianceGroupId == 0)
		{
			alliance.swapPlayers(playerObjectId, secondObjectId);
			
			broadcastAllianceMemberInfo(alliance, playerObjectId, PlayerAllianceEvent.MEMBER_GROUP_CHANGE);
			broadcastAllianceMemberInfo(alliance, secondObjectId, PlayerAllianceEvent.MEMBER_GROUP_CHANGE);
		}
		else
		{
			alliance.setAllianceGroupFor(playerObjectId, allianceGroupId);
			broadcastAllianceMemberInfo(alliance, playerObjectId, PlayerAllianceEvent.MEMBER_GROUP_CHANGE);
		}
	}

	/**
	 * 
	 * @param memberToUpdate
	 * @param event
	 * @param params
	 */
	private void broadcastAllianceMemberInfo(PlayerAlliance alliance, int playerObjectId, PlayerAllianceEvent event, String ... params)
	{
		PlayerAllianceMember memberToUpdate = alliance.getPlayer(playerObjectId);
		if (memberToUpdate != null)
			broadcastAllianceMemberInfo(alliance, memberToUpdate, event, params);
	}

	private void broadcastAllianceMemberInfo(PlayerAlliance alliance, PlayerAllianceMember memberToUpdate, PlayerAllianceEvent event, String ... params)
	{
		if(memberToUpdate.getPlayer() == null)
			return;

		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline() || allianceMember.getPlayer() == null)
				continue;

			Player member = allianceMember.getPlayer();

			PacketSendUtility.sendPacket(member, new SM_ALLIANCE_MEMBER_INFO(memberToUpdate, event));
			PacketSendUtility.sendPacket(member, new SM_INSTANCE_COOLDOWN(memberToUpdate.getPlayer()));

			switch(event)
			{
				case ENTER:
					if (!member.equals(memberToUpdate.getPlayer()))
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_HE_ENTERED_FORCE(memberToUpdate.getName()));
					break;
				case LEAVE:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_LEAVED_PARTY(memberToUpdate.getName()));
					break;
				case LEAVE_TIMEOUT:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_HE_BECOME_OFFLINE_TIMEOUT(memberToUpdate.getName()));
					break;
				case BANNED:
					if (member.equals(memberToUpdate.getPlayer()))
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_BAN_ME(params[0]));
					else
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_BAN_HIM(params[0], memberToUpdate.getName()));
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * 
	 * @param alliance
	 * @param event
	 * @param params
	 */
	public void broadcastAllianceInfo(PlayerAlliance alliance, PlayerAllianceEvent event, String ... params)
	{
		SM_ALLIANCE_INFO packet = new SM_ALLIANCE_INFO(alliance);
		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline()) continue;
			Player member = allianceMember.getPlayer();
			PacketSendUtility.sendPacket(member, packet);
			switch(event)
			{
				case APPOINT_CAPTAIN:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_CHANGE_LEADER(params[0], alliance.getCaptain().getName()));
					break;
				case APPOINT_VICE_CAPTAIN:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_PROMOTE_MANAGER(params[0]));
					break;
				case DEMOTE_VICE_CAPTAIN:
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_DEMOTE_MANAGER(params[0]));
					break;
			}
		}
	}
	
	/**
	 * @param alliance
	 * @param member
	 */
	private void sendOtherMemberInfo(PlayerAlliance alliance, Player memberToSend)
	{
		
		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline())
				continue;
			
			PacketSendUtility.sendPacket(memberToSend, new SM_ALLIANCE_MEMBER_INFO(allianceMember, PlayerAllianceEvent.UPDATE));
			PacketSendUtility.sendPacket(memberToSend, new SM_INSTANCE_COOLDOWN(allianceMember.getPlayer()));
		}
	}

	/**
	 * @param actingMember
	 * @param status
	 * @param playerObjId
	 */
	public void playerStatusInfo(Player actingMember, int status, int playerObjId)
	{
		PlayerAlliance alliance = actingMember.getPlayerAlliance();
		
		if (alliance == null)
		{
			PacketSendUtility.sendMessage(actingMember, "Your alliance does not exist or can not be found");
			PacketSendUtility.sendPacket(actingMember, new SM_LEAVE_GROUP_MEMBER());
			return;
		}
		
		switch(status)
		{
			case 14: // Leave Alliance
				removeMemberFromAlliance(alliance, actingMember.getObjectId(), PlayerAllianceEvent.LEAVE);
				break;
			case 15: // Ban from Alliance
				removeMemberFromAlliance(alliance, playerObjId, PlayerAllianceEvent.BANNED, actingMember.getName());
				break;
			case 16: // Make Alliance Captain
				String oldLeader = alliance.getCaptain().getName();
				alliance.setLeader(playerObjId);
				broadcastAllianceInfo(alliance, PlayerAllianceEvent.APPOINT_CAPTAIN, oldLeader, alliance.getCaptain().getName());
				break;
			case 19: // Check Readiness State
				PacketSendUtility.sendMessage(actingMember, "Readiness check is not implmeneted yet. (ID: " + playerObjId + ")");
				//PacketSendUtility.sendPacket(actingMember, new SM_ALLIANCE_READY_CHECK(playerObjId, status));
				break;
			case 23: // Appoint Alliance ViceCaptain
				alliance.promoteViceLeader(playerObjId);
				broadcastAllianceInfo(alliance, PlayerAllianceEvent.APPOINT_VICE_CAPTAIN, alliance.getPlayer(playerObjId).getName());
				break;
			case 24: // Demote Alliance ViceCaptain
				alliance.demoteViceLeader(playerObjId);
				broadcastAllianceInfo(alliance, PlayerAllianceEvent.DEMOTE_VICE_CAPTAIN, alliance.getPlayer(playerObjId).getName());
				break;
		}
	}

	/**
	 * 
	 * @param member
	 * @param event
	 * @param params
	 */
	public void removeMemberFromAlliance(PlayerAlliance alliance, int memberObjectId, PlayerAllianceEvent event, String ... params)
	{
		// Player
		PlayerAllianceMember allianceMember = alliance.getPlayer(memberObjectId);

		// TODO: Why is this null sometimes (found when banning from alliance)
		if (allianceMember == null)
			return;

		if (allianceMember.isOnline())
		{
			allianceMember.getPlayer().setPlayerAlliance(null);
			PacketSendUtility.sendPacket(allianceMember.getPlayer(), new SM_LEAVE_GROUP_MEMBER());
		}

		// Alliance
		broadcastAllianceMemberInfo(alliance, allianceMember, event, params);
		alliance.removeMember(memberObjectId);
		removeAllianceMemberFromCache(memberObjectId);

		broadcastAllianceMemberInfo(alliance, memberObjectId, PlayerAllianceEvent.BANNED);

		// Check Disband
		if (alliance.size() == 1)
		{
			// IDFactory.getInstance().releaseId(alliance.getObjectId());
			Player player = alliance.getCaptain().getPlayer();
			removeMemberFromAlliance(alliance, alliance.getCaptainObjectId(), event);
			if (player != null)
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_DISPERSED());
		}
	}

	/**
	 * Unused until login packet sequence can be determined
	 * 
	 * @param player
	 */
	public void setAlliance(Player player)
	{
		if(!isAllianceMember(player.getObjectId()))
			return;

		final PlayerAlliance alliance = getPlayerAlliance(player.getObjectId());
		
		// Alliance is empty.
		if(alliance.size() == 0)
		{
			removeAllianceMemberFromCache(player.getObjectId());
			return;
		}
		
		player.setPlayerAlliance(alliance);
		cancelRemovalTask(player.getObjectId());
	}

	/**
	 * Unused until login packet sequence can be determined
	 * 
	 * @param player
	 */
	public void onLogin(Player player)
	{
		final PlayerAlliance alliance = player.getPlayerAlliance();

		alliance.onPlayerLogin(player);
		
		// Required for relogging to work. (?)
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		
		PacketSendUtility.sendPacket(player, new SM_ALLIANCE_INFO(alliance));
		PacketSendUtility.sendPacket(player, new SM_SHOW_BRAND(0, 0, 0));
		
		broadcastAllianceMemberInfo(alliance, player.getObjectId(), PlayerAllianceEvent.RECONNECT);
		sendOtherMemberInfo(alliance, player);
	}
	
	/**
	 * @param player
	 */
	public void onLogout(Player player)
	{
		scheduleRemove(player);
	}
	
	/**
	 * Currently sends movement packet to all alliance members.
	 * 
	 * @param player
	 * @param event
	 */
	public void updateAllianceUIToEvent(Player player, PlayerAllianceEvent event)
	{
		PlayerAlliance alliance = player.getPlayerAlliance();
		
		switch(event)
		{
			case MOVEMENT:
			case UPDATE:
				PlayerAllianceMember member = alliance.getPlayer(player.getObjectId());
				if (member != null)
				{
					SM_ALLIANCE_MEMBER_INFO packet = new SM_ALLIANCE_MEMBER_INFO(member, event);
					for(PlayerAllianceMember allianceMember : alliance.getMembers())
					{
						if (allianceMember.isOnline() && !player.equals(allianceMember.getPlayer()))
							PacketSendUtility.sendPacket(allianceMember.getPlayer(), packet);
					}
				}
				break;
				
			default:
				// Unsupported
				break;
		}
	}
	
	/**
	 * Sends brand information to each member.
	 * 
	 * @param alliance
	 * @param modeId
	 * @param brandId
	 * @param targetObjectId
	 */
	public void showBrand(PlayerAlliance alliance, int modeId, int brandId, int targetObjectId)
	{
		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline()) continue;
			PacketSendUtility.sendPacket(allianceMember.getPlayer(), new SM_SHOW_BRAND(modeId, brandId, targetObjectId));
		}
	}

	/**
	 * @param winner
	 * @param owner
	 */
	public void doReward(PlayerAlliance alliance, Monster owner)
	{
		// TODO: Merge with group type do-reward. (Near identical to GroupService doReward code.)
		// Plus complete rewrite of drop system and exp system.
		// http://www.aionsource.com/topic/40542-character-stats-xp-dp-origin-gerbatorteam-july-2009/
		
		// Find Group Members and Determine Highest Level
		List<Player> players = new ArrayList<Player>();
		int partyLvlSum = 0;
		int highestLevel = 0;
		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			if (!allianceMember.isOnline()) continue;
			Player member = allianceMember.getPlayer();  
			if(MathUtil.isIn3dRange(member, owner, GroupConfig.GROUP_MAX_DISTANCE))
			{
				if (member.getLifeStats().isAlreadyDead())
					continue;
				players.add(member);
				partyLvlSum += member.getLevel();
				if (member.getLevel() > highestLevel)
					highestLevel = member.getLevel();
			}
		}
		
		// All are dead or not nearby.
		if (players.size() == 0)
			return;
		
		//AP reward
		int apRewardPerMember = 0;
		WorldType worldType = owner.getWorldType();
		
		//WorldType worldType = sp.getWorld().getWorldMap(player.getWorldId()).getWorldType();
		if(worldType == WorldType.ABYSS || 
		(worldType == WorldType.BALAUREA && (owner.getObjectTemplate().getRace() == Race.DRAKAN || owner.getObjectTemplate().getRace() == Race.LIZARDMAN)))
		{
			// Split Evenly
			apRewardPerMember = Math.round(StatFunctions.calculateGroupAPReward(highestLevel, owner) / players.size());
		}
		
		// Exp reward
		long expReward = StatFunctions.calculateGroupExperienceReward(highestLevel, owner);
		
		// Exp Mod
		// TODO: Add logic to prevent power leveling. Players 10 levels below highest member should get 0 exp.
		double mod = 1;
		if (players.size() == 0)
			return;
		else if (players.size() > 1)
			mod = 1+(((players.size()-1)*10)/100);
		
		expReward *= mod; 

		for(Player member : players)
		{
			if((highestLevel - member.getLevel()) < 10)
			{
				// Exp reward
				long currentExp = member.getCommonData().getExp();
				long reward = (expReward * member.getLevel())/partyLvlSum;
				reward *= member.getRates().getGroupXpRate();
				member.getCommonData().setExp(currentExp + reward);

				if(owner == null || owner.getObjectTemplate() == null)
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.EXP(Long.toString(reward)));
				else
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.EXP(reward, owner.getObjectTemplate().getNameId()));

				// DP reward
				int currentDp = member.getCommonData().getDp();
				int dpReward = StatFunctions.calculateGroupDPReward(member, owner);
				member.getCommonData().setDp(dpReward + currentDp);

				// AP reward
				if (apRewardPerMember > 0)
					member.getCommonData().addAp(Math.round(apRewardPerMember * member.getRates().getApNpcRate()));
			}
			QuestEngine.getInstance().onKill(new QuestCookie(owner, member, 0 , 0));
		}
		
		// Give Drop
		Player leader = alliance.getCaptain().getPlayer();
		
		// TODO: Better Group/Alliance Drop methods.
		if (leader == null) return;
		
		DropService.getInstance().registerDrop(owner, leader, highestLevel, players);
	}

	/**
	 * This method will get all group members
	 *
	 * @param group
	 * @param except
	 * @return list of group members
	 */
	public List<Integer> getAllianceMembers(final PlayerAlliance alliance, boolean except)
	{
		List<Integer> luckyMembers = new ArrayList<Integer>();

		for(PlayerAllianceMember allianceMember : alliance.getMembers())
		{
			int memberObjId = allianceMember.getObjectId();
			if(except)
			{
				if(alliance.getCaptain().getObjectId() != memberObjId)
					luckyMembers.add(memberObjId);
			}
			else
				luckyMembers.add(memberObjId);
		}
		return luckyMembers;
	}
	/**
	 * @return FastMap<Integer, Boolean>
	 */
	public List<Integer> getMembersToRegistrateByRules(Player player, PlayerAlliance alliance, Npc npc)
	{
		LootGroupRules lootRules = alliance.getLootAllianceRules();
		LootRuleType lootRule = lootRules.getLootRule();
		List<Integer> luckyMembers = new ArrayList<Integer>();

		switch(lootRule)
		{
			case ROUNDROBIN:
				int roundRobinMember = alliance.getRoundRobinMember(npc);
				if(roundRobinMember != 0)
				{
					luckyMembers.add(roundRobinMember);
					break;
				} // if no member is found then the loot is FREEFORALL.
			case FREEFORALL:
				luckyMembers = getAllianceMembers(alliance, false);
				break;
			case LEADER:
				luckyMembers.add(alliance.getCaptain().getObjectId());
				break;
		}
		return luckyMembers;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AllianceService instance = new AllianceService();
	}
}
