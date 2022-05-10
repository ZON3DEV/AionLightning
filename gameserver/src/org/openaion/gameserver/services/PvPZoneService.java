/**
 * This file is part of aion-engine <aion-engine.com>
 *
 *  aion-engine is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-engine is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.services;

import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.spawn.SpawnEngine;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.world.World;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.Race;


/**
 * @author Luno
 * @co-author Aion Germany, Dallas, Iven, Dex
 */

public class PvPZoneService
{
	private static VisibleObject ELYGATE;
	private static VisibleObject ASMOGATE;
	private static VisibleObject CANNON;
	private static VisibleObject eventnpc[];	
	private static boolean opened = false;
                
	
	public static boolean Spawn(int ELYGATEnpcID, int ASMOGATEnpcID, int CANNONnpcID)
	{
		if (!opened)
		{
			//Gelkmaros Gate
			float x = 1812.5325f;
			float y = 2929.5986f;
			float z = 554.7982f;
			byte heading = 0;
			int worldId = 220070000;
			SpawnTemplate spawn = SpawnEngine.getInstance().addNewSpawn(worldId, 1, ASMOGATEnpcID, x, y, z, heading, 0, 0, false, true);
			VisibleObject visibleObject = SpawnEngine.getInstance().spawnObject(spawn, 1);			
			
			//Inggison Gate
			float x2 = 1272.4163f;
			float y2 = 330.46143f;	
			float z2 = 597.85114f;
			byte heading2 = 0;
			int worldId2 = 210050000;
			SpawnTemplate spawn2 = SpawnEngine.getInstance().addNewSpawn(worldId2, 1, ELYGATEnpcID, x2, y2, z2, heading2, 0, 0, false, true);
			VisibleObject visibleObject2 = SpawnEngine.getInstance().spawnObject(spawn2, 1);			
			
			//Cannon
			float x3 = 681.724f;
			float y3 = 550.67f;
			float z3 = 1023.79f;
			byte heading3 = 2;
			int worldId3 = 300100000;
			SpawnTemplate spawn3 = SpawnEngine.getInstance().addNewSpawn(worldId3, 1, CANNONnpcID, x3, y3, z3, heading3, 0, 0, false, true);
			VisibleObject visibleObject3 = SpawnEngine.getInstance().spawnObject(spawn3, 1);			
			
			World.getInstance().doOnAllPlayers(new Executor<Player> () {
			@Override
				public boolean run(Player player)
				{
					PacketSendUtility.sendSysMessage(player, "PvP Event, Steel Rake Zone, is ready for fighting!");
					return true;
				}
			});
			
			ELYGATE = visibleObject;
			ASMOGATE = visibleObject2;
			CANNON = visibleObject3;
			opened = true;
			
			int mapId = 300100000;			
			eventnpc = new VisibleObject[33];
	        
			eventnpc[0] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204510, (float) 505.083, (float) 522.940, (float) 1033.27, (byte) 103, true);
			eventnpc[1] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204510, (float) 493.386, (float) 523.142, (float) 1033.27, (byte) 95, true);
			eventnpc[2] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204510, (float) 478.550, (float) 522.938, (float) 1033.27, (byte) 119, true);
			eventnpc[3] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204510, (float) 494.086, (float) 543.000, (float) 1034.76, (byte) 29, true);
			eventnpc[4] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204510, (float) 493.904, (float) 535.081, (float) 1034.75, (byte) 90, true);
			eventnpc[5] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204510, (float) 486.043, (float) 522.754, (float) 1033.27, (byte) 89, true);
			eventnpc[6] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204510, (float) 515.335, (float) 523.375, (float) 1033.27, (byte) 91, true);
			eventnpc[7] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204711, (float) 618.918, (float) 541.987, (float) 1031.07, (byte) 57, true);
			eventnpc[8] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204711, (float) 618.587, (float) 550.355, (float) 1031.06, (byte) 62, true);
			eventnpc[9] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204711, (float) 615.788, (float) 545.923, (float) 1031.05, (byte) 61, true);
			eventnpc[10] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204711, (float) 638.013, (float) 469.111, (float) 1031.04, (byte) 61, true);
			eventnpc[11] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204711, (float) 637.519, (float) 475.227, (float) 1031.05, (byte) 63, true);
			eventnpc[12] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204711, (float) 626.466, (float) 469.339, (float) 1031.04, (byte) 66, true);
			eventnpc[13] = (Npc) InstanceService.addNewSpawn(mapId, 1, 204711, (float) 626.797, (float) 476.795, (float) 1031.05, (byte) 73, true);
			eventnpc[14] = (Npc) InstanceService.addNewSpawn(mapId, 1, 798920, (float) 463.071, (float) 559.572, (float) 1032.98, (byte) 105, true);
			eventnpc[15] = (Npc) InstanceService.addNewSpawn(mapId, 1, 799219, (float) 686.955, (float) 465.914, (float) 1022.67, (byte) 57, true);
			eventnpc[16] = (Npc) InstanceService.addNewSpawn(mapId, 1, 730207, (float) 482.904, (float) 540.082, (float) 1034.74, (byte) 53, true);
			eventnpc[17] = (Npc) InstanceService.addNewSpawn(mapId, 1, 250146, (float) 411.274, (float) 544.287, (float) 1072.08, (byte) 91, true);
			eventnpc[18] = (Npc) InstanceService.addNewSpawn(mapId, 1, 250146, (float) 416.153, (float) 473.500, (float) 1072.08, (byte) 31, true);
			eventnpc[19] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700554, (float) 624.570, (float) 541.108, (float) 936.094, (byte) 60, true);
			eventnpc[20] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700473, (float) 609.516, (float) 481.285, (float) 936.027, (byte) 28, true);
			eventnpc[21] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700554, (float) 578.419, (float) 514.348, (float) 944.670, (byte) 88, true);
			eventnpc[22] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700554, (float) 351.892, (float) 587.142, (float) 948.015, (byte) 77, true);
			eventnpc[23] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700554, (float) 283.347, (float) 452.374, (float) 952.558, (byte) 90, true);
			eventnpc[24] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700554, (float) 239.519, (float) 523.133, (float) 948.674, (byte) 80, true);
			eventnpc[25] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700554, (float) 428.203, (float) 536.419, (float) 946.658, (byte) 39, true);
			eventnpc[26] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700554, (float) 474.668, (float) 573.676, (float) 958.078, (byte) 114, true);
			eventnpc[27] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700554, (float) 516.533, (float) 533.960, (float) 958.744, (byte) 6, true);
			eventnpc[28] = (Npc) InstanceService.addNewSpawn(mapId, 1, 281443, (float) 242.355, (float) 506.127, (float) 948.674, (byte) 119, true);
			eventnpc[29] = (Npc) InstanceService.addNewSpawn(mapId, 1, 700473, (float) 237.316, (float) 506.174, (float) 948.674, (byte) 62, true);
			eventnpc[30] = (Npc) InstanceService.addNewSpawn(mapId, 1, 250147, (float) 233.975, (float) 489.493, (float) 948.674, (byte) 89, true);
			eventnpc[31] = (Npc) InstanceService.addNewSpawn(mapId, 1, 281938, (float) 413.900, (float) 510.320, (float) 1071.85, (byte) 62, true);
			eventnpc[32] = (Npc) InstanceService.addNewSpawn(mapId, 1, 217778, (float) 264.693, (float) 527.494, (float) 947.018, (byte) 26, true);
			
			return true;
		}
		else
			return false;
	}
	
	public static void AdminReset()
	{
		try { Delete(); } catch (Exception ex) { }
		ELYGATE = null;
		ASMOGATE = null;
		CANNON = null;
	}
	
	public static boolean Delete()
	{
		if (opened)
		{
			Npc npc = (Npc) ELYGATE;
			DataManager.SPAWNS_DATA.removeSpawn(npc.getSpawn());
			npc.getController().delete();
			
			npc = (Npc) ASMOGATE;
			DataManager.SPAWNS_DATA.removeSpawn(npc.getSpawn());
			npc.getController().delete();
			
			npc = (Npc) CANNON;
			DataManager.SPAWNS_DATA.removeSpawn(npc.getSpawn());
			npc.getController().delete();
			
			for(VisibleObject npcs : eventnpc){
			    Npc spawn = (Npc) npcs;
			    spawn.getController().onDelete();
			}
            
			World.getInstance().doOnAllPlayers(new Executor<Player> () {
			@Override
				public boolean run(Player player)
				{						
					if (player.getWorldId() == 300100000)
					{
					
						if (player.getX() <= 1000f && player.getX() >= 100f && player.getY() <= 1000f && player.getY() >= 100f)
						if (player.getCommonData().getRace().equals(Race.ELYOS))
						{
							TeleportService.freeTeleport(player, 210050000, 1275.1191f, 328.14645f, 597.85114f); //Inggison
							PacketSendUtility.sendMessage(player, "PvP Event, Steel Rake Zone, was closed and you will be ported to Inggison.");
						
						}
						else if (player.getCommonData().getRace().equals(Race.ASMODIANS))
						{
							TeleportService.freeTeleport(player, 220070000, 1808.944f, 2931.2979f, 554.8001f); //Gelkmaros
							PacketSendUtility.sendMessage(player, "PvP Event, Steel Rake Zone, was closed and you will be ported to Gelkmaros.");
							
						
						}
					}
					PacketSendUtility.sendSysMessage(player, "PvP Event,Steel Rake Zone,is not accesible anymore!");
					return true;
				}
			});
			
			opened = false;
			return true;
		}
		else
			return false;
    }
}
