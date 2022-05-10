package org.openaion.gameserver.services;

import org.openaion.gameserver.model.gameobjects.Monster;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.group.PlayerGroup;

/**
 * @author Ritsu
 * 
 */

public class MirenInstanceService
{
	protected VisibleObject					chests[];
	int mapId = 300130000;
	
	public void onGroupReward(Monster monster, PlayerGroup group)
	{
		if(monster.getObjectTemplate().getTemplateId() == 215222)
		{
			SpawnChest(group);
		}
	}
	
	public void SpawnChest(PlayerGroup group)
	{	
		chests = new VisibleObject[12];
		
		chests[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700544, (float) 576.8508, (float) 836.40424, (float) 200, (byte) 44, true);
		chests[1] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 477.4225, (float) 814.993, (float) 199.70894, (byte) 11, true);
		chests[2] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 470.9954, (float) 834.474, (float) 199.70894, (byte) 118, true);
		chests[3] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 470.5955, (float) 854.473, (float) 199.70894, (byte) 117, true);
		chests[4] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 476.9531, (float) 874.273, (float) 199.70882, (byte) 108, true);
		chests[5] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 489.9968, (float) 889.522, (float) 199.70882, (byte) 103, true);
		chests[6] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 507.7754, (float) 900.224, (float) 199.70882, (byte) 98, true);
		chests[7] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 528.1088, (float) 903.631, (float) 199.70882, (byte) 90, true);
		chests[8] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 547.4296, (float) 900.064, (float) 199.70882, (byte) 83, true);
		chests[9] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 565.3545, (float) 890.399, (float) 199.70882, (byte) 78, true);
		chests[10] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700543, (float) 578.5674, (float) 875.200, (float) 199.70882, (byte) 70, true);
		chests[11] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700561, (float) 586.5694, (float) 856.1183, (float) 199.70882, (byte) 64, true);
	}
	
	public static MirenInstanceService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final MirenInstanceService	instance	= new MirenInstanceService();
	}
}