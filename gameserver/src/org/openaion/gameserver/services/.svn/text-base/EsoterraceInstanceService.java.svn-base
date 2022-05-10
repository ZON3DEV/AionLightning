package org.openaion.gameserver.services;

import org.openaion.gameserver.model.gameobjects.Monster;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.spawn.SpawnEngine;

//@author Degsx

public class EsoterraceInstanceService
{
    protected VisibleObject                 daliachest[];
    protected VisibleObject                 muruganchest[];
    protected VisibleObject                 kexkrachest[];
    int mapId = 300250000;
    
    public void onGroupReward(Monster monster, PlayerGroup group)
    {
        // When kills Dalia Charlands spawn 6 boxes arounde him and Lab Gatekeeper boss
        if(monster.getObjectTemplate().getTemplateId() == 217185) {
            SpawnDaliaChest(group);
            
            SpawnTemplate LabGatekeeper;            
            LabGatekeeper = SpawnEngine.getInstance().addNewSpawn(300250000, group.getGroupLeader().getInstanceId(), 217281, 789.73425f, 939.05743f, 352.94437f, (byte)89, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(LabGatekeeper, group.getGroupLeader().getInstanceId());
        }
        
        // When you kill Lab Gatekeeper spawn Captain Murugan
        if(monster.getObjectTemplate().getTemplateId() == 217281) {
            SpawnMuruganChest(group);
            
            SpawnTemplate Murugan;            
            Murugan = SpawnEngine.getInstance().addNewSpawn(300250000, group.getGroupLeader().getInstanceId(), 217195, 789.48f, 1115.29f, 363.131f, (byte)89, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Murugan, group.getGroupLeader().getInstanceId());
        }
        
        // When you kill Biolab Watchman spawn Kexkra Prototype
        if(monster.getObjectTemplate().getTemplateId() == 217289) {
            SpawnTemplate KexkraPrototype;
            KexkraPrototype = SpawnEngine.getInstance().addNewSpawn(300250000, group.getGroupLeader().getInstanceId(), 217205, 1315.99f, 1170.77f, 51.8004f, (byte)87, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(KexkraPrototype, group.getGroupLeader().getInstanceId());
        }
        
        // If you kill Kexkra Prototype spawn Warden Surama
        if(monster.getObjectTemplate().getTemplateId() == 217205) {
            SpawnTemplate WardenSurama;
            WardenSurama = SpawnEngine.getInstance().addNewSpawn(300250000, group.getGroupLeader().getInstanceId(), 217206, 1315.99f, 1170.77f, 51.8004f, (byte)89, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(WardenSurama, group.getGroupLeader().getInstanceId());
        }
                
        // When kill Warden Surama
        if(monster.getObjectTemplate().getTemplateId() == 217206) {
            SpawnKexkraChest(group);
        }
    }
    
    public void SpawnDaliaChest(PlayerGroup group)
    {   
        daliachest = new VisibleObject[7];        
        daliachest[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701022, (float) 1294.6598, (float) 634.97595, (float) 296.26846, (byte) 0, true);
        daliachest[1] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701022, (float) 1278.0482, (float) 613.33636, (float) 296.1695, (byte) 0, true);
        daliachest[2] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701022, (float) 1253.5836, (float) 623.45404, (float) 296.75, (byte) 0, true);
        daliachest[3] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701022, (float) 1227.3922, (float) 637.30554, (float) 296.45117, (byte) 0, true);
        daliachest[4] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701022, (float) 1238.9745, (float) 665.42346, (float) 296.50638, (byte) 0, true);
        daliachest[5] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701022, (float) 1265.1886, (float) 679.7532, (float) 296.71472, (byte) 0, true);
        daliachest[6] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701023, (float) 1290.3278, (float) 668.816, (float) 296.44647, (byte) 0, true);
    }
    
    public void SpawnMuruganChest(PlayerGroup group)
    {   
        muruganchest = new VisibleObject[2];
        muruganchest[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701024, (float) 798.7157, (float) 1119.9598, (float) 363.13092, (byte) 1, true);
        muruganchest[1] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701024, (float) 781.8872, (float) 1120.1724, (float) 363.1309, (byte) 1, true);
    }
    
    public void SpawnKexkraChest(PlayerGroup group)
    {   
        kexkrachest = new VisibleObject[6];
        kexkrachest[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701027, (float) 1316.7238, (float) 1183.9585, (float) 51.493996, (byte) 29, true);
        kexkrachest[1] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701027, (float) 1327.7646, (float) 1178.0612, (float) 51.493996, (byte) 29, true);
        kexkrachest[2] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701027, (float) 1328.8212, (float) 1167.138, (float) 51.493996, (byte) 29, true);
        kexkrachest[3] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701027, (float) 1321.0255, (float) 1158.1405, (float) 51.493996, (byte) 29, true);
        kexkrachest[4] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701027, (float) 1309.7302, (float) 1160.1057, (float) 51.493996, (byte) 29, true);
        kexkrachest[5] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 701027, (float) 1302.0417, (float) 1172.4471, (float) 51.493996, (byte) 29, true);
    }
    
    public static EsoterraceInstanceService getInstance()
    {
        return SingletonHolder.instance;
    }
    
    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder
    {
        protected static final EsoterraceInstanceService instance = new EsoterraceInstanceService();
    }
    
}