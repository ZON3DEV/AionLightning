package org.openaion.gameserver.services;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.configs.main.GroupConfig;
import org.openaion.gameserver.controllers.PortalController;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.model.instances.Dredgion;
import org.openaion.gameserver.network.aion.serverpackets.SM_DREDGION_INSTANCE;
import org.openaion.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.world.WorldMap;
import org.openaion.gameserver.world.WorldMapInstance;


/**
 *
 * @author ginho1
*/
public class DredgionInstanceService extends InstanceService
{
	private List<PlayerGroup> asmosGroupsWaiting = new ArrayList<PlayerGroup>();
	private List<PlayerGroup> elyosGroupsWaiting = new ArrayList<PlayerGroup>();
	private List<PlayerGroup> asmosGroupsReady = new ArrayList<PlayerGroup>();
	private List<PlayerGroup> elyosGroupsReady = new ArrayList<PlayerGroup>();

	private static Logger	log	= Logger.getLogger(DredgionInstanceService.class);

	public void sendDredgionEntry(Player player)
	{
		if(player.getLevel() >= 46 && player.getLevel() <= 50 && player.getWorldId() != 300110000)
		{
			PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE((byte) 2, 6, 0, 0));
			PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE((byte) 1, 6, 1, 0));
		}else if(player.getLevel() >= 51 && player.getWorldId() != 300210000){
			PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE((byte) 1, 6, 0, 0));
			PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE((byte) 2, 6, 1, 0));
		}
	}

	public static boolean isDredgion(int worldId)
	{
		if(worldId == 300110000 || worldId == 300210000)
			return true;

		return false;
	}
	
	public boolean isChantraDredgion(PlayerGroup group)
	{
		int highestLevel = 0;
		for(Player member : group.getMembers())
		{
			if (!member.isOnline()) continue;

			if (member.getLevel() > highestLevel)
				highestLevel = member.getLevel();
		}

		if(highestLevel > 50)
			return true;

		return false;
	}
	
	public boolean isRegistredGroup(PlayerGroup group)
	{
		if(asmosGroupsReady.contains(group))
			return true;
		if(elyosGroupsReady.contains(group))
			return true;

		if(asmosGroupsWaiting.contains(group))
			return true;
		if(elyosGroupsWaiting.contains(group))
			return true;

		return false;
	}

	public boolean privateEntry(Player player)
	{
		if(player.isInGroup())
		{
			PacketSendUtility.sendMessage(player, "Leave group before select this option.");
			return false;
		}

		if(player.getInDredgion())
		{
		    PacketSendUtility.sendMessage(player, "You are already in dredgion.");
		    return false;
		}

		Race playerRace = player.getCommonData().getRace();
		boolean chantraDredgion = (player.getLevel() > 50);

		switch(playerRace)
		{
			case ASMODIANS:
				for(PlayerGroup group : asmosGroupsWaiting)
				{
					if(isChantraDredgion(group) == chantraDredgion)
					{
						if(!group.isFull())
						{
							group.addPlayerToGroup(player);
							PacketSendUtility.sendMessage(player, "Dreadgion registration successed, waiting for Elyos group.");
							startDredgion(player);
							return true;
						}
					}
				}
			break;
			case ELYOS:
				for(PlayerGroup group : elyosGroupsWaiting)
				{
					if(isChantraDredgion(group) == chantraDredgion)
					{
						if(!group.isFull())
						{
							group.addPlayerToGroup(player);
							PacketSendUtility.sendMessage(player, "Dreadgion registration successed, waiting for Asmosdians group.");
							startDredgion(player);
							return true;
						}
					}
				}
			break;
		}

		PacketSendUtility.sendMessage(player, "No groups to join, try again later or try other Entry option.");
		return false;
	}

	public boolean quickEntry(Player player)
	{
		if(player.isInGroup())
		{
			PacketSendUtility.sendMessage(player, "Leave group before select this option.");
			return false;
		}

		if(player.getInDredgion())
		{
		    PacketSendUtility.sendMessage(player, "You are already in dredgion.");
		    return false;
		}

		Race playerRace = player.getCommonData().getRace();
		boolean chantraDredgion = (player.getLevel() > 50);

		switch(playerRace)
		{
			case ASMODIANS:
				for(PlayerGroup group : asmosGroupsReady)
				{
					if(isChantraDredgion(group) == chantraDredgion)
					{
						if(!group.isFull())
						{
							if(joinPlayerToGroup(player, group, chantraDredgion));
								return true;
						}
					}
				}
			break;
			case ELYOS:
				for(PlayerGroup group : elyosGroupsReady)
				{
					if(isChantraDredgion(group) == chantraDredgion)
					{
						if(!group.isFull())
						{
							if(joinPlayerToGroup(player, group, chantraDredgion));
								return true;
						}
					}
				}
			break;
		}

		PacketSendUtility.sendMessage(player, "No groups to join, try again later or try other Entry option.");
		return false;
	}

	public boolean groupEntry(Player player)
	{
		if(!player.isInGroup())
		{
			PacketSendUtility.sendMessage(player, "Join in group before select this option.");
			return false;
		}

		if(player.getInDredgion())
		{
		    PacketSendUtility.sendMessage(player, "You are already in dredgion.");
		    return false;
		}

		if(player.getPlayerGroup().getGroupLeader().getObjectId() != player.getObjectId())
		{
			PacketSendUtility.sendMessage(player, "Only group leader can register group.");
			return false;
		}

		Race playerRace = player.getCommonData().getRace();
		PlayerGroup playerGroup = player.getPlayerGroup();

		switch(playerRace)
		{
			case ASMODIANS:
				if(!asmosGroupsWaiting.contains(playerGroup))
				{
					asmosGroupsWaiting.add(playerGroup);
					PacketSendUtility.sendMessage(player, "Dreadgion registration successed, waiting Elyos group.");
					startDredgion(player);
					return true;
				}
			break;
			case ELYOS:
				if(!elyosGroupsWaiting.contains(playerGroup))
				{
					elyosGroupsWaiting.add(playerGroup);
					PacketSendUtility.sendMessage(player, "Dreadgion registration successed, waiting for Asmosdian group.");
					startDredgion(player);
					return true;
				}
			break;
		}

		return false;
	}

	public void startDredgion(Player player)
	{
		PlayerGroup playerGroup = player.getPlayerGroup();

		if(!isRegistredGroup(playerGroup))
			return;

		Race playerRace = player.getCommonData().getRace();
		boolean chantraDredgion = isChantraDredgion(playerGroup);

		switch(playerRace)
		{
			case ASMODIANS:
				for(PlayerGroup enemyGroup : elyosGroupsWaiting)
				{
					if(isChantraDredgion(enemyGroup) == chantraDredgion)
					{
						teleportGroups(enemyGroup, playerGroup, chantraDredgion);
						elyosGroupsWaiting.remove(enemyGroup);
						elyosGroupsReady.add(enemyGroup);

						asmosGroupsWaiting.remove(playerGroup);
						asmosGroupsReady.add(playerGroup);
						return;
					}
				}
			break;
			case ELYOS:
				for(PlayerGroup enemyGroup : asmosGroupsWaiting)
				{
					if(isChantraDredgion(enemyGroup) == chantraDredgion)
					{
						teleportGroups(playerGroup, enemyGroup, chantraDredgion);
						asmosGroupsWaiting.remove(enemyGroup);
						asmosGroupsReady.add(enemyGroup);

						elyosGroupsWaiting.remove(playerGroup);
						elyosGroupsReady.add(playerGroup);
						return;
					}
				}
			break;
		}
	}

	private void teleportGroups(final PlayerGroup elyosGroup, final PlayerGroup asmosGroup, boolean chantraDredgion)
	{
		int worldId = 300110000;

		if(chantraDredgion)
			worldId = 300210000;

		final int instanceId = worldId;

		Dredgion dredgion = getNextDredgionInstance(worldId);

		registerGroupWithInstance(dredgion, elyosGroup);
		registerSecondDredgionGroup(dredgion, asmosGroup);

		for(final Player member : elyosGroup.getMembers())
		{
			if(chantraDredgion && member.getLevel() < 51)
			{
				PacketSendUtility.sendMessage(member, "Your level is too low to this dredgion");
				continue;
			}

			if(!chantraDredgion && member.getLevel() > 50)
			{
				PacketSendUtility.sendMessage(member, "Your level is too high to this dredgion");
				continue;
			}

			if(checkInstanceCooldown(member, worldId, 0))
				continue;

			PortalController.setInstanceCooldown(member, worldId, dredgion.getInstanceId());
			TeleportService.teleportTo(member, dredgion.getMapId(), dredgion.getInstanceId(), 558f, 190f, 432f, 0);
			PacketSendUtility.sendPacket(member, new SM_SYSTEM_MESSAGE(1400197));

			resetPoints(member);

			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					PacketSendUtility.sendPacket(member, new SM_DREDGION_INSTANCE(instanceId));
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(instanceId, getInstanceTime(member.getPlayerGroup().getInstanceStartTime()), elyosGroup, asmosGroup, false));
				}
			}, 8000);
		}

		for(final Player member : asmosGroup.getMembers())
		{
			if(chantraDredgion && member.getLevel() < 51)
			{
				PacketSendUtility.sendMessage(member, "Your level is too low to this dreadgion");
				continue;
			}

			if(!chantraDredgion && member.getLevel() > 50)
			{
				PacketSendUtility.sendMessage(member, "Your level is too high to this dreadgion");
				continue;
			}

			if(checkInstanceCooldown(member, worldId, 0))
				continue;

			member.setInDredgion(true);

			PortalController.setInstanceCooldown(member, worldId, dredgion.getInstanceId());
			TeleportService.teleportTo(member, dredgion.getMapId(), dredgion.getInstanceId(), 414f, 193f, 431f, 0);
			PacketSendUtility.sendPacket(member, new SM_SYSTEM_MESSAGE(1400197));

			resetPoints(member);

			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					PacketSendUtility.sendPacket(member, new SM_DREDGION_INSTANCE(instanceId));
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(instanceId, getInstanceTime(member.getPlayerGroup().getInstanceStartTime()), elyosGroup, asmosGroup, false));
				}
			}, 8000);
		}
	}

	private boolean joinPlayerToGroup(final Player player, final PlayerGroup group, boolean chantraDredgion)
	{
		if(group != null && group.isFull())
			return false;

		int worldId = 300110000;

		if(chantraDredgion)
			worldId = 300210000;

		final int instanceId = worldId;		

		if(checkInstanceCooldown(player, worldId, 0))
			return false;

		WorldMapInstance mapInstance = World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(group.getGroupLeader().getInstanceId());

		if(!(mapInstance instanceof Dredgion))
			return false;

		Dredgion dred = (Dredgion)mapInstance;

		group.addPlayerToGroup(player);

		final PlayerGroup elyosGroup = dred.getRegisteredGroup();
		final PlayerGroup asmosGroup = dred.getSecondGroup();

		if(dred.getRegisteredGroup().getObjectId() == group.getObjectId())
			TeleportService.teleportTo(player, dred.getMapId(), dred.getInstanceId(), 558f, 190f, 432f, 0);
		else
			TeleportService.teleportTo(player, dred.getMapId(), dred.getInstanceId(), 414f, 193f, 431f, 0);

		PortalController.setInstanceCooldown(player, worldId, dred.getInstanceId());
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400197));

		resetPoints(player);

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(instanceId));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(instanceId, getInstanceTime(player.getPlayerGroup().getInstanceStartTime()), elyosGroup, asmosGroup, false));
			}
		}, 8000);

		return true;
	}

	protected boolean checkInstanceCooldown(Player player, int worldId, int instanceId)
	{
		int instanceMapId = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getInstanceMapId();
		int mapname = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getMapNameId();

		if(!InstanceService.canEnterInstance(player, instanceMapId, instanceId) && CustomConfig.INSTANCE_COOLDOWN && player.getWorldId() != worldId)
		{
			int timeinMinutes = InstanceService.getTimeInfo(player).get(instanceMapId)/60;
			if (timeinMinutes >= 60 )
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_HOUR_CLIENT(mapname, timeinMinutes/60));
			else
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_MIN_CLIENT(mapname, timeinMinutes));

			return true;
		}
		return false;
	}

	private Dredgion getNextDredgionInstance(int worldId)
	{
		WorldMap map = World.getInstance().getWorldMap(worldId);

		if(!map.isInstanceType())
			throw new UnsupportedOperationException("Invalid call for next available instance  of " + worldId);

		int nextInstanceId = map.getNextInstanceId();

		log.info("Creating new Dredgion instance: " + nextInstanceId);

		Dredgion dredgionInstance = new Dredgion(map, nextInstanceId);
		startInstanceChecker(dredgionInstance);
		map.addInstance(nextInstanceId, dredgionInstance);
		SpawnEngine.getInstance().spawnInstance(worldId, dredgionInstance.getInstanceId());
		
		return dredgionInstance;
	}

	public void giveFinalReward(Creature captain, boolean chantraDredgion)
	{
		int worldId = 300110000;
		int winningReward = CustomConfig.DREDGION_AP_WIN;
		int defeatedReward = CustomConfig.DREDGION_AP_LOSE;

		if(chantraDredgion)
		{
			worldId = 300210000;
			winningReward = CustomConfig.CHANTRA_DREDGION_AP_WIN;
			defeatedReward = CustomConfig.CHANTRA_DREDGION_AP_LOSE;
		}

		Dredgion dred = (Dredgion)World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(captain.getInstanceId());

		final PlayerGroup elyosGroup = dred.getRegisteredGroup();
		final PlayerGroup asmosGroup = dred.getSecondGroup();

		if(elyosGroup != null && asmosGroup != null)
		{
			int elyosGroupPoints = getGroupScore(elyosGroup);
			int asmosGroupPoints = getGroupScore(asmosGroup);

			if(elyosGroupPoints > 0 || asmosGroupPoints > 0)
			{
				if(elyosGroupPoints > asmosGroupPoints)
				{
					if(elyosGroupPoints > 0)
					{
						for(Player member : elyosGroup.getMembers())
						{
							if(isDredgion(member.getWorldId()))
							{
								member.setInstancePlayerAP(winningReward);
								member.getCommonData().addAp(member.getInstancePlayerAP() + member.getInstancePlayerScore());
							}
							member.setInDredgion(false);
						}
					}

					if(asmosGroupPoints > 0)
					{
						for(Player member : asmosGroup.getMembers())
						{
							if(isDredgion(member.getWorldId()))
							{
								member.setInstancePlayerAP(defeatedReward);
								member.getCommonData().addAp(member.getInstancePlayerAP() + member.getInstancePlayerScore());
							}
	                        member.setInDredgion(false);
						}
					}
				}else{
					if(elyosGroupPoints > 0)
					{
						for(Player member : elyosGroup.getMembers())
						{
							if(isDredgion(member.getWorldId()))
							{
								member.setInstancePlayerAP(defeatedReward);
								member.getCommonData().addAp(member.getInstancePlayerAP() + member.getInstancePlayerScore());
							}
	                        member.setInDredgion(false);
						}
					}

					if(asmosGroupPoints > 0)
					{
						for(Player member : asmosGroup.getMembers())
						{
							if(isDredgion(member.getWorldId()))
							{
								member.setInstancePlayerAP(winningReward);
								member.getCommonData().addAp(member.getInstancePlayerAP() + member.getInstancePlayerScore());
							}
	                        member.setInDredgion(false);
						}
					}
				}
			}

			for(Player member : elyosGroup.getMembers())
			{
				if(isDredgion(member.getWorldId()))
				{
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(worldId, getInstanceTime(member.getPlayerGroup().getInstanceStartTime()), elyosGroup, asmosGroup, true));
				}
			}

			for(Player member : asmosGroup.getMembers())
			{
				if(isDredgion(member.getWorldId()))
				{
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(worldId, getInstanceTime(member.getPlayerGroup().getInstanceStartTime()), elyosGroup, asmosGroup, true));
				}
			}

			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					onGroupDisband(elyosGroup);
					onGroupDisband(asmosGroup);
					resetPoints(elyosGroup);
					resetPoints(asmosGroup);
				}
			}, 12000);
			
			dred.removeRegisteredGroup();
			dred.removeSecondGroup();
		}		
	}

	public void doPvpReward(Player winner, Player defeated)
	{
		if(!defeated.isInGroup())
			return;

		int defeatedPoints = defeated.getInstancePlayerScore();

		if(defeatedPoints < 60)
		{
			defeated.setInstancePlayerScore(0);
		}else{
			defeated.setInstancePlayerScore(defeatedPoints - 60);
			winner.setInstancePlayerScore(winner.getInstancePlayerScore() + 60);
		}

		winner.setInstancePVPKills(winner.getInstancePVPKills() + 1);

		int worldId = winner.getWorldId();

		Dredgion dred = (Dredgion)World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(winner.getInstanceId());

		PlayerGroup elyosGroup = dred.getRegisteredGroup();
		PlayerGroup asmosGroup = dred.getSecondGroup();
		
		//happens when instance finished but someone still inside
		if (elyosGroup == null || asmosGroup == null)
			return;

		for(Player member : elyosGroup.getMembers())
		{
			if(isDredgion(member.getWorldId()))
			{
				PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(member.getWorldId(), getInstanceTime(member.getPlayerGroup().getInstanceStartTime()), elyosGroup, asmosGroup, false));
			}
		}

		for(Player member : asmosGroup.getMembers())
		{
			if(isDredgion(member.getWorldId()))
			{
				PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(member.getWorldId(), getInstanceTime(member.getPlayerGroup().getInstanceStartTime()), elyosGroup, asmosGroup, false));
			}
		}
	}

	public void doReward(Player winner, Creature monster)
	{
		int rewardPoints = 0;
		boolean finalKill = false;
		boolean chantraDredgion = isChantraDredgion(winner.getPlayerGroup());

		int worldId = 300110000;

		if(chantraDredgion)
			worldId = 300210000;

		if(chantraDredgion)
		{
			switch(monster.getObjectTemplate().getTemplateId())
			{
				case 216890:
				case 216889:
					rewardPoints += 0;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 216859:
				case 216858:
				case 216857:
				case 216854:
					rewardPoints += 12;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 216869:
				case 216868:
				case 216867:
				case 216866:
				case 216865:
				case 216864:
				case 216863:
				case 216862:
				case 216861:
				case 216860:
					rewardPoints += 32;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 217037:
				case 216888:
				case 216887:
				case 216884:
				case 216883:
				case 216882:
				case 216880:
				case 216879:
				case 216878:
				case 216876:
				case 216874:
				case 216873:
				case 216872:
				case 216871:
				case 216870:
					rewardPoints += 42;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 700836:
					rewardPoints += 100;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 700838:
				case 700839:
					rewardPoints += 400;
					winner.setInstanceCaptured(winner.getInstanceCaptured() + 1);
				break;
				case 216885:
					rewardPoints += 500;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 700850:
				case 700840:
				case 700848:
				case 700849:
				case 700851:
					rewardPoints += 700;
					winner.setInstanceCaptured(winner.getInstanceCaptured() + 1);
				break;
				case 700845:
				case 700846:
					rewardPoints += 800;
					winner.setInstanceCaptured(winner.getInstanceCaptured() + 1);
				break;
				case 700847:
					rewardPoints += 900;
					winner.setInstanceCaptured(winner.getInstanceCaptured() + 1);
				break;
				case 700841:
				case 700842:
					rewardPoints += 1000;
					winner.setInstanceCaptured(winner.getInstanceCaptured() + 1);
				break;
				case 700843:
				case 700844:
					rewardPoints += 1100;
					winner.setInstanceCaptured(winner.getInstanceCaptured() + 1);
				break;
				case 216941:
					rewardPoints += 1000;
					winner.setInstanceCaptured(winner.getInstanceCaptured() + 1);
				break;
				case 216886:
					rewardPoints += 1000;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
					finalKill = true;
				break;
			}
		}else{
			switch(monster.getObjectTemplate().getTemplateId())
			{
				case 700485:
					rewardPoints += 500;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
					break;
				case 700487:
					rewardPoints += 900;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 700488:
					rewardPoints += 1080;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 700494:
					rewardPoints += 800;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 700490:
					rewardPoints += 600;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 700492:
				case 700498:
					rewardPoints += 700;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 700495:
				case 700507:
				case 700508:
					rewardPoints += 500;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 214822:
				case 214820:
				case 214806:
				case 214808:
				case 214811:
				case 214819:
				case 214821:
				case 216852:
				case 214812:
					rewardPoints += 40;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 214814:
				case 214813:
					rewardPoints += 18;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 215089:
				case 215092:
				case 215091:
				case 215090:
				case 215082:
				case 215084:
				case 215390:
				case 215391:
				case 215083:
				case 215093:
				case 215427:
				case 215085:
				case 215088:
					rewardPoints += 200;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 215087:
				case 215086:
					rewardPoints += 500;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
				break;
				case 214823:
					rewardPoints += 1000;
					winner.setInstanceBalaurKills(winner.getInstanceBalaurKills() + 1);
					finalKill = true;
				break;
			}
		}

		doReward(winner, rewardPoints);

		Dredgion dred = (Dredgion)World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(winner.getInstanceId());

		PlayerGroup elyosGroup = dred.getRegisteredGroup();
		PlayerGroup asmosGroup = dred.getSecondGroup();
		
		//happens when instance finished but someone still inside
		if (elyosGroup == null || asmosGroup == null)
			return;

		for(Player member : elyosGroup.getMembers())
		{
			if(isDredgion(member.getWorldId()))
				PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(member.getWorldId(), getInstanceTime(member.getPlayerGroup().getInstanceStartTime()), elyosGroup, asmosGroup, false));
		}

		for(Player member : asmosGroup.getMembers())
		{
			if(isDredgion(member.getWorldId()))
				PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(member.getWorldId(), getInstanceTime(member.getPlayerGroup().getInstanceStartTime()), elyosGroup, asmosGroup, false));
		}

		if(getInstanceTime(winner.getPlayerGroup().getInstanceStartTime()) < 4000)
			finalKill = true;

		if(finalKill)
			giveFinalReward(winner.getPlayerGroup().getGroupLeader(), chantraDredgion);
	}

	public void doReward(Player owner, int points)
	{
		PlayerGroup group = owner.getPlayerGroup();

		List<Player> players = new ArrayList<Player>();

		for(Player member : group.getMembers())
		{
			if (!member.isOnline()) continue;
			if(MathUtil.isIn3dRange(member, owner, GroupConfig.GROUP_MAX_DISTANCE))
			{
				if (member.getLifeStats().isAlreadyDead())
					continue;
				players.add(member);
			}
		}

		if (players.isEmpty())
			return;

		for(Player member : players)
		{
			member.setInstancePlayerScore(member.getInstancePlayerScore() + Math.round(points / players.size()));
		}
	}

	public void onGroupDisband(PlayerGroup group)
	{
		if(asmosGroupsWaiting.contains(group))
			asmosGroupsWaiting.remove(group);
		if(elyosGroupsWaiting.contains(group))
			elyosGroupsWaiting.remove(group);
		
		if(asmosGroupsReady.contains(group))
			asmosGroupsReady.remove(group);
		if(elyosGroupsReady.contains(group))
			elyosGroupsReady.remove(group);
	}

	public void cancelGroup(PlayerGroup group)
	{
		if(asmosGroupsWaiting.contains(group))
			asmosGroupsWaiting.remove(group);
		if(elyosGroupsWaiting.contains(group))
			elyosGroupsWaiting.remove(group);

		if(asmosGroupsReady.contains(group))
			asmosGroupsReady.remove(group);
		if(elyosGroupsReady.contains(group))
			elyosGroupsReady.remove(group);
	}

	public int getInstanceTime(long instanceStartTime)
	{
		int result = (int) (2361656 - (System.currentTimeMillis() - instanceStartTime));

		if(result < 0)
			result = 0;

		return result;
	}

	public void resetPoints(Player player)
	{
		player.setInstancePVPKills(0);
		player.setInstanceBalaurKills(0);
		player.setInstanceCaptured(0);
		player.setInstancePlayerScore(0);
		player.setInstancePlayerAP(0);
		player.setInDredgion(true);
	}

	public void resetPoints(PlayerGroup playerGroup)
	{
		for(Player member : playerGroup.getMembers())
		{
			resetPoints(member);
		}
	}

	public int getGroupScore(PlayerGroup group)
	{
		int points = 0;

		for(Player member : group.getMembers())
		{
			points += member.getInstancePlayerScore();
		}

		return points;
	}

	public static void registerSecondDredgionGroup(Dredgion dredgion, PlayerGroup group)
	{
		group.setInstanceStartTimeNow();
		group.setGroupInstancePoints(0);
		dredgion.registerSecondGroup(group);
	}

	public static DredgionInstanceService getInstance()
	{
		return SingletonHolder.dr;
	}

	private static class SingletonHolder
	{
		public static DredgionInstanceService dr = new DredgionInstanceService();
	}
}
