package org.openaion.gameserver.services;

import org.openaion.gameserver.model.gameobjects.Monster;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;

/**
 * @author kosyachok, dns
 * 
 */
public class DarkPoetaInstanceService
{
	public void onGroupReward(Monster monster, final PlayerGroup group)
	{
		group.setInstanceKills(group.getInstanceKills() + 1);

		int mapId = 300040000;
		int instanceTime = (int) ((group.getInstanceStartTime() + 14400000) - System.currentTimeMillis());
		int pointsReward = calculatePointsReward(monster, group.getGroupLeader());
		int grandTotal = group.getGroupInstancePoints() + pointsReward;

		group.setGroupInstancePoints(group.getGroupInstancePoints() + pointsReward);

		if(group.getInstanceDisplaycounter())
		{
			for(Player member : group.getMembers())
			{
				if(member.getWorldId() == 300040000)
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(mapId, instanceTime, 2097152, grandTotal,
						grandTotal, group.getInstanceKills(), 7));
			}
		}else{
			return;
		}

		// extra boss spawn after killing final boss
		if(monster.getObjectTemplate().getTemplateId() == 214904)
		{
			group.setInstanceDisplaycounter(false);
			int totalPoints = group.getGroupInstancePoints();
			long timeRemain = (group.getInstanceStartTime() + 14400000) - System.currentTimeMillis();

			SpawnTemplate spawn;

			if(timeRemain > 7200000 && totalPoints >= 19643)// S Grade
			{
				for(Player member : group.getMembers())
				{
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(mapId, instanceTime, 3145728, grandTotal,
						grandTotal, group.getInstanceKills(), 1));
				}
				spawn = SpawnEngine.getInstance().addNewSpawn(300040000, group.getGroupLeader().getInstanceId(),
					215280, 1176f, 1227f, 145f, (byte) 14, 0, 0, true);
				SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
			}
			else if(timeRemain > 5400000 && totalPoints >= 17046)// A Grade
			{
				for(Player member : group.getMembers())
				{
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(mapId, instanceTime, 3145728, grandTotal,
						grandTotal, group.getInstanceKills(), 2));
				}
				spawn = SpawnEngine.getInstance().addNewSpawn(300040000, group.getGroupLeader().getInstanceId(),
					215281, 1176f, 1227f, 145f, (byte) 14, 0, 0, true);
				SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
			}
			else if(timeRemain > 3600000 && totalPoints > 13055)// B Grade
			{
				for(Player member : group.getMembers())
				{
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(mapId, instanceTime, 3145728, grandTotal,
						grandTotal, group.getInstanceKills(), 3));
				}
				spawn = SpawnEngine.getInstance().addNewSpawn(300040000, group.getGroupLeader().getInstanceId(),
					215282, 1176f, 1227f, 145f, (byte) 14, 0, 0, true);
				SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
			}
			else if(timeRemain > 1800000 && totalPoints > 9334)// C Grade
			{
				for(Player member : group.getMembers())
				{
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(mapId, instanceTime, 3145728, grandTotal,
						grandTotal, group.getInstanceKills(), 4));
				}
				spawn = SpawnEngine.getInstance().addNewSpawn(300040000, group.getGroupLeader().getInstanceId(),
					215283, 1176f, 1227f, 145f, (byte) 14, 0, 0, true);
				SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
			}
			else if(timeRemain > 1)// grade D
			{
				for(Player member : group.getMembers())
				{
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(mapId, instanceTime, 3145728, grandTotal,
						grandTotal, group.getInstanceKills(), 5));
				}
				spawn = SpawnEngine.getInstance().addNewSpawn(300040000, group.getGroupLeader().getInstanceId(),
					215284, 1176f, 1227f, 145f, (byte) 14, 0, 0, true);
				SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
			}
			else
			// grade F -- failed
			{
				for(Player member : group.getMembers())
				{
					PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(mapId, instanceTime, 3145728, grandTotal,
						grandTotal, group.getInstanceKills(), 8));
				}
			}
			group.setGroupInstancePoints(0);
			group.setInstanceKills(0);
			group.setInstanceDisplaycounter(true);

			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					for(Player member : group.getMembers())
					{
						PacketSendUtility.sendPacket(member, new SM_INSTANCE_SCORE(0, 0, 0, 0, 0, 0, 0));
					}
				}
			}, 20000);
		}
	}

	private int calculatePointsReward(Monster monster, Player leader)
	{
		int pointsReward = 0;

		switch(monster.getObjectTemplate().getRank())
		{
			case HERO:
				switch(monster.getObjectTemplate().getHpGauge())
				{
					case 21:
						pointsReward = 786;
						break;

					default:
						pointsReward = 300;
				}
				break;

			default:
				if(monster.getObjectTemplate().getRace() != null)
				{
					switch(monster.getObjectTemplate().getRace().getRaceId())
					{
						case 22: // UNDEAD
							pointsReward = 12;
							break;
						case 9: // BROWNIE
							pointsReward = 18;
							break;
						case 6: // LIZARDMAN
							pointsReward = 24;
							break;
						case 8: // NAGA
						case 18: // DRAGON
						case 24: // MAGICALMONSTER
							pointsReward = 30;
							break;
						default:
							pointsReward = 11;
							break;
					}
				}
				else
					pointsReward = 11;
		}

		// Drana
		if(monster.getObjectTemplate().getTemplateId() == 700520)
			pointsReward = 48;
		// Walls
		else if(monster.getObjectTemplate().getTemplateId() == 700518
			|| monster.getObjectTemplate().getTemplateId() == 700558)
			pointsReward = 156;
		// Named1
		else if(monster.getObjectTemplate().getTemplateId() == 214841
			|| monster.getObjectTemplate().getTemplateId() == 215431)
			pointsReward = 162;
		// Named2
		else if(monster.getObjectTemplate().getTemplateId() == 214842
			|| monster.getObjectTemplate().getTemplateId() == 215429
			|| monster.getObjectTemplate().getTemplateId() == 215430
			|| monster.getObjectTemplate().getTemplateId() == 215432)
			pointsReward = 186;
		// Named3
		else if(monster.getObjectTemplate().getTemplateId() == 214871
			|| monster.getObjectTemplate().getTemplateId() == 215386
			|| monster.getObjectTemplate().getTemplateId() == 215428)
			pointsReward = 204;
		// Marabata
		else if(monster.getObjectTemplate().getTemplateId() == 214849
			|| monster.getObjectTemplate().getTemplateId() == 214850
			|| monster.getObjectTemplate().getTemplateId() == 214851)
			pointsReward = 318;
		// Generators
		else if(monster.getObjectTemplate().getTemplateId() == 214895
			|| monster.getObjectTemplate().getTemplateId() == 214896
			|| monster.getObjectTemplate().getTemplateId() == 214897)
			pointsReward = 372;
		// Atmach
		else if(monster.getObjectTemplate().getTemplateId() == 214843)
			pointsReward = 456;
		// Boss
		else if(monster.getObjectTemplate().getTemplateId() == 214864
			|| monster.getObjectTemplate().getTemplateId() == 214880
			|| monster.getObjectTemplate().getTemplateId() == 214894
			|| monster.getObjectTemplate().getTemplateId() == 215387
			|| monster.getObjectTemplate().getTemplateId() == 215388
			|| monster.getObjectTemplate().getTemplateId() == 215389)
			pointsReward = 786;
		else if(monster.getObjectTemplate().getTemplateId() == 214904)
			pointsReward = 954;

		if(leader.getAbyssRank().getRank().getId() >= 10)
			pointsReward = Math.round(pointsReward * 1.1f);

		return pointsReward;

	}

	public static DarkPoetaInstanceService getInstance()
	{
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DarkPoetaInstanceService	instance	= new DarkPoetaInstanceService();
	}
}
