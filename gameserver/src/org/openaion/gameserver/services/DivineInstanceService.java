package org.openaion.gameserver.services;

import org.openaion.gameserver.model.gameobjects.Monster;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.group.PlayerGroup;

/**
 * @author Ritsu
 * 
 */

public class DivineInstanceService
{
	//see : http://gameguide.na.aiononline.com/aion/Abyssal+Splinter+Walkthrough
	protected VisibleObject					chests[];
	protected VisibleObject					YamennesPainflare[];
	protected VisibleObject					YamennesBlindsight[];
	int mapId = 300220000;
	
	public void onGroupReward(Monster monster, PlayerGroup group)
	{
		if(monster.getObjectTemplate().getTemplateId() == 700955) // Huge Aether Fragment
			group.setInstanceKills(group.getInstanceKills() + 1);
		
		if(monster.getObjectTemplate().getTemplateId() == 216951) // Pazuzu
			PazuzuChests(group);
		
		if(monster.getObjectTemplate().getTemplateId() == 216950) // Kaluva
			KaluvaChests(group);
		
		if(monster.getObjectTemplate().getTemplateId() == 282010) // Dayshade
			DayshadeChests(group);
		
		if(monster.getObjectTemplate().getTemplateId() == 216952 || monster.getObjectTemplate().getTemplateId() == 216960) // YamennesBlindsight & Painflare
			YamennesChests(group);
		
		if(monster.getObjectTemplate().getTemplateId() == 700856) // Yamennes artifact
		{
			if (group.getInstanceKills() == 3)
				YamennesPainflare(group);
			else
				YamennesBlindsight(group);
		}
	}
	
	public void PazuzuChests(PlayerGroup group)
	{
		chests = new VisibleObject[5];

		chests[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 651.532, (float) 357.085, (float) 467.417, (byte) 0, true);
		chests[1] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 653.838, (float) 360.395, (float) 467.514, (byte) 0, true);
		chests[2] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 647.004, (float) 357.248, (float) 469.606, (byte) 0, true);
		chests[3] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700860, (float) 649.243, (float) 361.338, (float) 467.392, (byte) 0, true);
		//Huge Aether Fragment
		chests[4] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700955, (float) 669.576, (float) 335.135, (float) 465.895, (byte) 0, true);
	}

	public void KaluvaChests(PlayerGroup group)
	{	
		chests = new VisibleObject[5];

		chests[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 597.216, (float) 583.954, (float) 424.283, (byte) 0, true);
		chests[1] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 601.293, (float) 584.667, (float) 424.227, (byte) 0, true);
		chests[2] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 602.959, (float) 589.268, (float) 424.283, (byte) 0, true);
		chests[3] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700935, (float) 598.828, (float) 588.259, (float) 422.79437, (byte) 0, true);
		//Huge Aether Fragment
		chests[4] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700955, (float) 633.75, (float) 557.882, (float) 423.124, (byte) 0, true);
	}

	public void DayshadeChests(PlayerGroup group)
	{	
		chests = new VisibleObject[5];

		chests[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 406.744, (float) 655.591, (float) 440.413, (byte) 0, true);
		chests[1] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 408.109, (float) 650.901, (float) 440.441, (byte) 0, true);
		chests[2] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 402.404, (float) 655.552, (float) 440.421, (byte) 0, true);
		chests[3] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700936, (float) 403.983, (float) 651.584, (float) 440.284, (byte) 0, true);
		//Huge Aether Fragment
		chests[4] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700955, (float) 452.897, (float) 692.361, (float) 432.692, (byte) 0, true);
	}
	
	public void YamennesChests(PlayerGroup group)
	{	
		chests = new VisibleObject[4];

		chests[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 326.53, (float) 735.133, (float) 199.267, (byte) 0, true);
		chests[1] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 326.978, (float) 729.841, (float) 198.468, (byte) 0, true);
		chests[2] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700934, (float) 329.846, (float) 738.411, (float) 198.468, (byte) 0, true);
		chests[3] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700938, (float) 330.891, (float) 733.294, (float) 197.841, (byte) 0, true);
	}
	
	public void YamennesPainflare(PlayerGroup group)
	{	
		YamennesPainflare = new VisibleObject[1];

		YamennesPainflare[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 216960, (float) 329.70886, (float) 733.8744, (float) 197.60938, (byte) 0, true);
	}
	
	public void YamennesBlindsight(PlayerGroup group)
	{	
		YamennesBlindsight = new VisibleObject[1];

		YamennesBlindsight[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 216952, (float) 329.70886, (float) 733.8744, (float) 197.60938, (byte) 0, true);
	}

	public static DivineInstanceService getInstance()
	{
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DivineInstanceService	instance	= new DivineInstanceService();
	}
}