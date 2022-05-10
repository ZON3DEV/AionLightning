/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.spawn;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.controllers.ActionitemController;
import org.openaion.gameserver.controllers.AethericFieldController;
import org.openaion.gameserver.controllers.ArtifactController;
import org.openaion.gameserver.controllers.ArtifactProtectorController;
import org.openaion.gameserver.controllers.BindpointController;
import org.openaion.gameserver.controllers.ChestController;
import org.openaion.gameserver.controllers.FortressGateArtifactController;
import org.openaion.gameserver.controllers.FortressGateController;
import org.openaion.gameserver.controllers.FortressGeneralController;
import org.openaion.gameserver.controllers.GatherableController;
import org.openaion.gameserver.controllers.GroupGateController;
import org.openaion.gameserver.controllers.HomingController;
import org.openaion.gameserver.controllers.KiskController;
import org.openaion.gameserver.controllers.MonsterController;
import org.openaion.gameserver.controllers.NpcController;
import org.openaion.gameserver.controllers.NpcWithCreatorController;
import org.openaion.gameserver.controllers.PortalController;
import org.openaion.gameserver.controllers.PostboxController;
import org.openaion.gameserver.controllers.RestrictedPortalController;
import org.openaion.gameserver.controllers.SummonController;
import org.openaion.gameserver.controllers.effect.EffectController;
import org.openaion.gameserver.controllers.instances.HaramelController;
import org.openaion.gameserver.controllers.instances.BeshmundirTempleController;
import org.openaion.gameserver.controllers.instances.KromedesTrialController;
import org.openaion.gameserver.controllers.instances.SteelRakeController;
import org.openaion.gameserver.dao.SpawnDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.dataholders.NpcData;
import org.openaion.gameserver.model.NpcType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Gatherable;
import org.openaion.gameserver.model.gameobjects.GroupGate;
import org.openaion.gameserver.model.gameobjects.Homing;
import org.openaion.gameserver.model.gameobjects.Kisk;
import org.openaion.gameserver.model.gameobjects.Monster;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.Servant;
import org.openaion.gameserver.model.gameobjects.SkillAreaNpc;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.Totem;
import org.openaion.gameserver.model.gameobjects.Trap;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.stats.NpcLifeStats;
import org.openaion.gameserver.model.gameobjects.state.CreatureVisualState;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.siege.AethericField;
import org.openaion.gameserver.model.siege.Artifact;
import org.openaion.gameserver.model.siege.ArtifactProtector;
import org.openaion.gameserver.model.siege.FortressGate;
import org.openaion.gameserver.model.siege.FortressGateArtifact;
import org.openaion.gameserver.model.siege.FortressGeneral;
import org.openaion.gameserver.model.siege.InstancePortal;
import org.openaion.gameserver.model.siege.SiegeRace;
import org.openaion.gameserver.model.templates.GatherableTemplate;
import org.openaion.gameserver.model.templates.NpcTemplate;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.model.templates.WorldMapTemplate;
import org.openaion.gameserver.model.templates.siege.AethericFieldTemplate;
import org.openaion.gameserver.model.templates.siege.ArtifactTemplate;
import org.openaion.gameserver.model.templates.siege.FortressGateArtifactTemplate;
import org.openaion.gameserver.model.templates.siege.FortressGateTemplate;
import org.openaion.gameserver.model.templates.siege.FortressGeneralTemplate;
import org.openaion.gameserver.model.templates.siege.InstancePortalTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnGroup;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.model.templates.stats.SummonStatsTemplate;
import org.openaion.gameserver.utils.idfactory.IDFactory;
import org.openaion.gameserver.world.NpcKnownList;
import org.openaion.gameserver.world.StaticObjectKnownList;
import org.openaion.gameserver.world.World;


/**
 * 
 * This class is responsible for NPCs spawn management. Current implementation is temporal and will be replaced in the
 * future.
 * 
 * @author Luno
 * 
 *         modified by ATracer
 * 
 */
public class SpawnEngine
{
	private static Logger				log					= Logger.getLogger(SpawnEngine.class);

	/** Counter counting number of npc spawns */
	private int							npcCounter			= 0;
	/** Counter counting number of gatherable spawns */
	private int							gatherableCounter	= 0;

	
	public static final SpawnEngine getInstance()
	{
		return SingletonHolder.instance;
	}

	private SpawnEngine()
	{
		this.spawnAll();
	}

	public VisibleObject spawnObject(SpawnTemplate spawn, int instanceIndex, boolean isCustom)
	{
		VisibleObject vObject = spawnObject(spawn, instanceIndex);
		if (vObject != null)
		{
			vObject.setCustom(isCustom);
		}
		return vObject;
	}
	/**
	 * Creates VisibleObject instance and spawns it using given {@link SpawnTemplate} instance.
	 * 
	 * @param spawn
	 * @return created and spawned VisibleObject
	 */
	public VisibleObject spawnObject(SpawnTemplate spawn, int instanceIndex)
	{
		
		VisibleObjectTemplate template = null;
		if (spawn == null)
		{
			return null;
		}
		
		int objectId = spawn.getSpawnGroup().getNpcid();
		
		NpcData	npcData = DataManager.NPC_DATA;
		if(objectId > 400000 && objectId < 499999)// gatherable
		{
			template = DataManager.GATHERABLE_DATA.getGatherableTemplate(objectId);
			if(template == null)
				return null;
			gatherableCounter++;
		}
		else
		// npc
		{
			template = npcData.getNpcTemplate(objectId);
			if(template == null)
			{
				log.error("No template for NPC " + String.valueOf(objectId));
				return null;
			}
			npcCounter++;
		}
		IDFactory iDFactory = IDFactory.getInstance();
		if(template instanceof NpcTemplate)
		{
			NpcType npcType = ((NpcTemplate) template).getNpcType();
			Npc npc = null;

			if(npcType != null)
			{
				switch(npcType)
				{
					case AGGRESSIVE:
					case ATTACKABLE:
						npc = new Monster(iDFactory.nextId(), new MonsterController(),
							spawn, template);
						npc.setKnownlist(new NpcKnownList(npc));
						break;
					case POSTBOX:
						npc = new Npc(iDFactory.nextId(), new PostboxController(), spawn,
							template);
						npc.setKnownlist(new StaticObjectKnownList(npc));
						break;
					case RESURRECT:
						BindpointController bindPointController = new BindpointController();
						bindPointController.setBindPointTemplate(DataManager.BIND_POINT_DATA.getBindPointTemplate(objectId));
						npc = new Npc(iDFactory.nextId(), bindPointController, spawn, template);
						npc.setKnownlist(new StaticObjectKnownList(npc));
						break;
					case USEITEM:
						npc = new Npc(iDFactory.nextId(), new ActionitemController(),
							spawn, template);
						npc.setKnownlist(new StaticObjectKnownList(npc));
						break;
					case PORTAL:
						npc = new Npc(iDFactory.nextId(), new PortalController(), spawn,
							template);
						npc.setKnownlist(new StaticObjectKnownList(npc));
						break;
					case CHEST:
						npc = new Npc(iDFactory.nextId(), new ChestController(), spawn,
							template);
						npc.setKnownlist(new NpcKnownList(npc));
						break;
					case NEUTRAL:
					default: // NON_ATTACKABLE
						npc = new Npc(iDFactory.nextId(), new NpcController(), spawn,
							template);
						npc.setKnownlist(new NpcKnownList(npc));
						break;
				}
			}
			else
			{
				npc = new Npc(iDFactory.nextId(), new NpcController(), spawn,
					template);
				npc.setKnownlist(new NpcKnownList(npc));
			}
			
			//steel rake exception
			if (objectId == 215402 || objectId == 215403 || objectId == 215404 || objectId == 215405 || objectId == 798378 || objectId == 798379)
			{
				npc = null;
				npc = new Npc(iDFactory.nextId(), new SteelRakeController(),
					spawn, template);
				npc.setKnownlist(new NpcKnownList(npc));
			}
			else if (objectId == 700548 || objectId == 730207)
			{
				npc = null;
				npc = new Npc(iDFactory.nextId(), new SteelRakeController(),
					spawn, template);
				npc.setKnownlist(new StaticObjectKnownList(npc));
			}
			//Haramel exception
            if (objectId == 730321) {
                npc = new Npc(iDFactory.nextId(), new HaramelController(),
                        spawn, template);
                npc.setKnownlist(new StaticObjectKnownList(npc));
            }
            if (objectId == 216922 || objectId == 700852) {
                npc = new Npc(iDFactory.nextId(), new HaramelController(),
                        spawn, template);
                npc.setKnownlist(new NpcKnownList(npc));
            }
			//Beshmundir Temple exception
			else if (objectId == 799517 || objectId == 799518 || objectId == 799519 || objectId == 799520 || objectId == 730275)
			{
				npc = null;
				npc = new Npc(iDFactory.nextId(), new BeshmundirTempleController(),
					spawn, template);
				npc.setKnownlist(new StaticObjectKnownList(npc));
			}
            if (objectId == 216583 || objectId == 799518) {
                npc = new Npc(iDFactory.nextId(), new BeshmundirTempleController(),
                        spawn, template);
                npc.setKnownlist(new NpcKnownList(npc));
            }
                        if (objectId == 216584 || objectId == 799519) {
                npc = new Npc(iDFactory.nextId(), new BeshmundirTempleController(),
                        spawn, template);
                npc.setKnownlist(new NpcKnownList(npc));
            }
                        if (objectId == 216585 || objectId == 799520) {
                npc = new Npc(iDFactory.nextId(), new BeshmundirTempleController(),
                        spawn, template);
                npc.setKnownlist(new NpcKnownList(npc));
            }
                        if (objectId == 216263 || objectId == 730286 || objectId == 730275) {
                npc = new Npc(iDFactory.nextId(), new BeshmundirTempleController(),
                        spawn, template);
                npc.setKnownlist(new NpcKnownList(npc));
            }			
            if (objectId == 216183 || objectId == 730287) {
                npc = new Npc(iDFactory.nextId(), new BeshmundirTempleController(),
                        spawn, template);
                npc.setKnownlist(new NpcKnownList(npc));
            }			
			//KromedesTrial exception
			else if (objectId == 700926 || objectId == 700924 || objectId == 700927 || objectId == 700922 || objectId == 730339 || objectId == 730336 || objectId == 730338 || objectId == 730337)
			{
				npc = null;
				npc = new Npc(iDFactory.nextId(), new KromedesTrialController(),
					spawn, template);
				npc.setKnownlist(new StaticObjectKnownList(npc));
			}
			//restricted teleporters exception
			if (objectId == 730218 || objectId == 730219)
			{
				npc = null;
				npc = new Npc(iDFactory.nextId(), new RestrictedPortalController(),
					spawn, template);
				npc.setKnownlist(new StaticObjectKnownList(npc));
			}
			
			//quest related exceptions
			if (objectId == 212649 || objectId == 204649 || objectId == 204020 || objectId == 278593 || objectId == 278599 || objectId == 278600 || objectId == 278601 || objectId == 278602 || objectId == 278603 || objectId == 278604 || objectId == 278605)
			{
				npc = null;
				npc = new Monster(iDFactory.nextId(), new MonsterController(),
					spawn, template);
				npc.setKnownlist(new NpcKnownList(npc));
			}
			//Veille && Mastarius exception
			if(objectId == 258221 || objectId == 258219)
			{
				npc = null;
				npc = new Monster(iDFactory.nextId(), new MonsterController(),
					spawn, template);
				npc.setKnownlist(new NpcKnownList(npc));
			}

			npc.setNpcSkillList(DataManager.NPC_SKILL_DATA.getNpcSkillList(template.getTemplateId()));
			npc.setEffectController(new EffectController(npc));
			npc.getController().onRespawn();
			bringIntoWorld(npc, spawn, instanceIndex);
			
			return npc;
		}
		else if(template instanceof GatherableTemplate)
		{
			Gatherable gatherable = new Gatherable(spawn, template, iDFactory.nextId(), new GatherableController());
			gatherable.setKnownlist(new StaticObjectKnownList(gatherable));
			bringIntoWorld(gatherable, spawn, instanceIndex);
			return gatherable;
		}
		return null;
	}

	/**
	 * 
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @return
	 */
	public Trap spawnTrap(SpawnTemplate spawn, int instanceIndex, Creature creator, int skillId)
	{
		int objectId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		Trap trap = new Trap(IDFactory.getInstance().nextId(), new NpcWithCreatorController(), spawn,
			npcTemplate);
		trap.setKnownlist(new NpcKnownList(trap));
		trap.setEffectController(new EffectController(trap));
		trap.setCreator(creator);
		trap.setSkillId(skillId);
		trap.setVisualState(CreatureVisualState.HIDE3);
		trap.getController().onRespawn();
		bringIntoWorld(trap, spawn, instanceIndex);
		return trap;
	}
	
	public FortressGeneral spawnFortressGeneral(int fortressId, SiegeRace race, FortressGeneralTemplate template)
	{
		int mapId = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations().get(fortressId).getLocationTemplate().getWorldId();
		int fgNpcId = template.getBaseInfo().getNpcId(race);
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(fgNpcId);
		SpawnTemplate sTemplate = addNewSpawn(mapId, 1, fgNpcId, template.getBaseInfo().getX(), template.getBaseInfo().getY(), template.getBaseInfo().getZ(), (byte)template.getBaseInfo().getH(), 0, 0, true, true);
		FortressGeneral gen = new FortressGeneral(IDFactory.getInstance().nextId(), new FortressGeneralController(), sTemplate, DataManager.NPC_DATA.getNpcTemplate(fgNpcId), fortressId);
	        gen.setNpcSkillList(DataManager.NPC_SKILL_DATA.getNpcSkillList(npcTemplate.getTemplateId()));
		gen.setKnownlist(new NpcKnownList(gen));
		gen.setEffectController(new EffectController(gen));
		gen.setLifeStats(new NpcLifeStats(gen));
		gen.getController().onRespawn();
		bringIntoWorld(gen, sTemplate, 1);
		return gen;
	}
	
	public InstancePortal spawnInstancePortal(int fortressId, SiegeRace race, InstancePortalTemplate template)
	{
		int mapId = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations().get(fortressId).getLocationTemplate().getWorldId();
		int ipNpcId = template.getBaseInfo().getNpcId(race);
		SpawnTemplate sTemplate = addNewSpawn(mapId, 1, ipNpcId, template.getBaseInfo().getX(), template.getBaseInfo().getY(), template.getBaseInfo().getZ(), (byte)template.getBaseInfo().getH(), 0, 0, true, true);
		InstancePortal portal = new InstancePortal(IDFactory.getInstance().nextId(), new PortalController(), sTemplate, DataManager.NPC_DATA.getNpcTemplate(ipNpcId), fortressId, template.getBaseInfo().getStaticId());
		portal.setKnownlist(new StaticObjectKnownList(portal));
		portal.setEffectController(new EffectController(portal));
		portal.setLifeStats(new NpcLifeStats(portal));
		portal.getController().onRespawn();
		bringIntoWorld(portal, sTemplate, 1);
		return portal;
	}
	
	public FortressGate spawnFortressGate(int fortressId, SiegeRace race, FortressGateTemplate template)
	{
		int mapId = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations().get(fortressId).getLocationTemplate().getWorldId();
		int fgNpcId = template.getBaseInfo().getNpcId(race);
		SpawnTemplate sTemplate = addNewSpawn(mapId, 1, fgNpcId, template.getBaseInfo().getX(), template.getBaseInfo().getY(), template.getBaseInfo().getZ(), (byte)template.getBaseInfo().getH(), 0, 0, true, true);
		FortressGate gate = new FortressGate(IDFactory.getInstance().nextId(), new FortressGateController(), sTemplate, DataManager.NPC_DATA.getNpcTemplate(fgNpcId), fortressId, template.getBaseInfo().getStaticId());
		gate.setKnownlist(new StaticObjectKnownList(gate));
		gate.setEffectController(new EffectController(gate));
		gate.setLifeStats(new NpcLifeStats(gate));
		gate.getController().onRespawn();
		bringIntoWorld(gate, sTemplate, 1);
		return gate;
	}
	
	public FortressGateArtifact spawnFortressGateArtifact(int fortressId, SiegeRace race, FortressGateArtifactTemplate template)
	{
		int mapId = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations().get(fortressId).getLocationTemplate().getWorldId();
		int fgaNpcId = template.getBaseInfo().getNpcId(race);
		SpawnTemplate sTemplate = addNewSpawn(mapId, 1, fgaNpcId, template.getBaseInfo().getX(), template.getBaseInfo().getY(), template.getBaseInfo().getZ(), (byte)template.getBaseInfo().getH(), 0, 0, true, true);
		FortressGateArtifact artifact = new FortressGateArtifact(IDFactory.getInstance().nextId(), new FortressGateArtifactController(), sTemplate, DataManager.NPC_DATA.getNpcTemplate(fgaNpcId), fortressId, template.getBaseInfo().getStaticId(), template.getHealGatePercent());
		artifact.setKnownlist(new StaticObjectKnownList(artifact));
		artifact.setEffectController(new EffectController(artifact));
		artifact.setLifeStats(new NpcLifeStats(artifact));
		artifact.getController().onRespawn();
		bringIntoWorld(artifact, sTemplate, 1);
		return artifact;
	}
	
	public Artifact spawnArtifact(int artifactId, SiegeRace race, ArtifactTemplate template)
	{
		int mapId = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations().get(artifactId).getLocationTemplate().getWorldId();
		// Spawn artifact itself
		int artifactNpcId = template.getBaseInfo().getNpcId(race);
		SpawnTemplate artifactSpawnTemplate = addNewSpawn(mapId, 1, artifactNpcId, template.getBaseInfo().getX(), template.getBaseInfo().getY(), template.getBaseInfo().getZ(), (byte)template.getBaseInfo().getH(), 0, 0, true, true);
		Artifact af = new Artifact(IDFactory.getInstance().nextId(), new ArtifactController(), artifactSpawnTemplate, DataManager.NPC_DATA.getNpcTemplate(artifactNpcId), artifactId, template.getBaseInfo().getStaticId());
		af.setKnownlist(new StaticObjectKnownList(af));
		af.setEffectController(new EffectController(af));
		af.getController().onRespawn();
		af.setTemplate(template);
		bringIntoWorld(af, artifactSpawnTemplate, 1);
				
		// Spawn and register protector
		if(template.getProtectorTemplate() != null)
		{
			int pNpcId = template.getProtectorTemplate().getBaseInfo().getNpcId(race);
			SpawnTemplate pSpawnTemplate = addNewSpawn(mapId, 1, pNpcId, template.getProtectorTemplate().getBaseInfo().getX(), template.getProtectorTemplate().getBaseInfo().getY(), template.getProtectorTemplate().getBaseInfo().getZ(), (byte)template.getProtectorTemplate().getBaseInfo().getH(), 0, 0, true, true);
			ArtifactProtector protector = new ArtifactProtector(IDFactory.getInstance().nextId(), new ArtifactProtectorController(), pSpawnTemplate, DataManager.NPC_DATA.getNpcTemplate(pNpcId));
			protector.setKnownlist(new NpcKnownList(protector));
			protector.setEffectController(new EffectController(protector));
			protector.setLifeStats(new NpcLifeStats(protector));
			protector.getController().onRespawn();
			bringIntoWorld(protector, pSpawnTemplate, 1);
			af.registerRelatedSpawn(protector.getObjectId());
			af.setProtector(protector);
		}		
		
		return af;
	}
	
	public AethericField spawnAethericGenerator(int fortressId, SiegeRace race, AethericFieldTemplate template)
	{
		int mapId = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations().get(fortressId).getLocationTemplate().getWorldId();
		int generatorNpcId = template.getBaseInfo().getNpcId(race);
		SpawnTemplate sTemplate = addNewSpawn(mapId, 1, generatorNpcId, template.getBaseInfo().getX(), template.getBaseInfo().getY(), template.getBaseInfo().getZ(), (byte)template.getBaseInfo().getH(), 0, 0, true, true);
		AethericField generator = new AethericField(IDFactory.getInstance().nextId(), new AethericFieldController(), sTemplate, DataManager.NPC_DATA.getNpcTemplate(generatorNpcId) ,fortressId);
		generator.setKnownlist(new NpcKnownList(generator));
		generator.setEffectController(new EffectController(generator));
		generator.setLifeStats(new NpcLifeStats(generator));
		generator.getController().onRespawn();
		bringIntoWorld(generator, sTemplate, 1);
		return generator;
	}

	/**
	 * 
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @return
	 */
	public GroupGate spawnGroupGate(SpawnTemplate spawn, int instanceIndex, Creature creator)
	{
		int objectId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		GroupGate groupgate = new GroupGate(IDFactory.getInstance().nextId(), new GroupGateController(), spawn,
			npcTemplate);
		groupgate.setKnownlist(new NpcKnownList(groupgate));
		groupgate.setEffectController(new EffectController(groupgate));
		groupgate.setCreator(creator);
		groupgate.getController().onRespawn();
		bringIntoWorld(groupgate, spawn, instanceIndex);
		return groupgate;
	}

	/**
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @return
	 */
	public Kisk spawnKisk(SpawnTemplate spawn, int instanceIndex, Player creator)
	{
		int npcId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(npcId);
		Kisk kisk = new Kisk(IDFactory.getInstance().nextId(), new KiskController(),
			spawn, template, creator);
		kisk.setKnownlist(new StaticObjectKnownList(kisk));
		kisk.setEffectController(new EffectController(kisk));
		kisk.getController().onRespawn();
		bringIntoWorld(kisk, spawn, instanceIndex);
		return kisk;
	}
	
	/**
	 * 
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @param skillId
	 * @return
	 */
	public Servant spawnServant(SpawnTemplate spawn, int instanceIndex, Creature creator, int skillId, float hpRatio)
	{
		int objectId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		Servant servant = new Servant(IDFactory.getInstance().nextId(), new NpcWithCreatorController(), spawn,
			npcTemplate);
		servant.setKnownlist(new NpcKnownList(servant));
		servant.setEffectController(new EffectController(servant));
		servant.setCreator(creator);
		servant.setSkillId(skillId);
		servant.setTarget(creator.getTarget());
		servant.getGameStats().setStat(StatEnum.MAXHP, Math.round((float)creator.getLifeStats().getMaxHp() * hpRatio / 100));
		//since servants are op and we dont have proper stats
		servant.getGameStats().setStat(StatEnum.BOOST_MAGICAL_SKILL, 300);
		servant.getObjectTemplate().setLevel(creator.getLevel());
		servant.getController().onRespawn();
		bringIntoWorld(servant, spawn, instanceIndex);
		return servant;
	}
	
	/**
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @param skillId
	 * @return
	*/
	public SkillAreaNpc spawnSkillAreaNpc(SpawnTemplate spawn, int instanceIndex, Creature creator, int skillId)
	{
		int objectId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		SkillAreaNpc saNpc = new SkillAreaNpc(IDFactory.getInstance().nextId(), new NpcWithCreatorController(), spawn,
			npcTemplate);
		saNpc.setNpcSkillList(DataManager.NPC_SKILL_DATA.getNpcSkillList(objectId));
		saNpc.setKnownlist(new NpcKnownList(saNpc));
		saNpc.setEffectController(new EffectController(saNpc));
		saNpc.setCreator(creator);
		saNpc.setSkillId(skillId);
		saNpc.getController().onRespawn();
		bringIntoWorld(saNpc, spawn, instanceIndex);
		return saNpc;
	}
	
	/**
	 * 
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @param skillId
	 * @return
	 */
	public Totem spawnTotem(SpawnTemplate spawn, int instanceIndex, Creature creator, int skillId)
	{
		int objectId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		Totem totem = new Totem(IDFactory.getInstance().nextId(), new NpcWithCreatorController(), spawn,
			npcTemplate);
		totem.setKnownlist(new NpcKnownList(totem));
		totem.setEffectController(new EffectController(totem));
		totem.setCreator(creator);
		totem.setSkillId(skillId);
		totem.getController().onRespawn();
		totem.getObjectTemplate().setLevel(creator.getLevel());
		bringIntoWorld(totem, spawn, instanceIndex);
		return totem;
	}
	/**
     * 
     * @param spawn
     * @param instanceIndex
     * @param creator
     * @param attackCount
     * @return
     */
    public Homing spawnHoming(SpawnTemplate spawn, int instanceIndex, Creature creator, int attackCount, int skillId, byte masterLevel)
    {
            int objectId = spawn.getSpawnGroup().getNpcid();
            NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
            //sets players lvl till we figure out retail like lvls
            npcTemplate.setLevel(masterLevel);
            Homing homing = new Homing(IDFactory.getInstance().nextId(), new HomingController(), spawn,
                    npcTemplate);
            homing.setState(CreatureState.WEAPON_EQUIPPED);
            homing.setKnownlist(new NpcKnownList(homing));
            homing.setEffectController(new EffectController(homing));
            homing.setCreator(creator);
            homing.setAttackCount(attackCount);
            homing.setSkillId(skillId);
            homing.setTarget(creator.getTarget());
            //set accurancy and magical accurancy, completly custom: lvl 55 has 2200 both
            homing.getGameStats().setStat(StatEnum.MAIN_HAND_ACCURACY, Math.round((float)homing.getLevel()*(2200f/55f)));
            homing.getGameStats().setStat(StatEnum.MAGICAL_ACCURACY, Math.round((float)homing.getLevel()*(2200f/55f)));
            //set custom power to prevent really low dmg of energies/servants
            //lvl 55 has 40 power(aprox. 200 per hit on a player)
            if (homing.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK) < (homing.getLevel()*40/55))
            	homing.getGameStats().setStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK, Math.round((float)homing.getLevel()*(40f/55f)));
            homing.getController().onRespawn();
            bringIntoWorld(homing, spawn, instanceIndex);
            return homing;
    }

	
	/**
	 * 
	 * @param creator
	 * @param npcId
	 * @return
	 */
	public Summon spawnSummon(Player creator, int npcId, int skillLvl)
	{	
		float x = creator.getX();
		float y = creator.getY();
		float z = creator.getZ();
		byte heading = creator.getHeading();
		int worldId = creator.getWorldId();
		int instanceId = creator.getInstanceId();
		
		SpawnTemplate spawn = createSpawnTemplate(worldId, npcId, x, y, z, heading, 0, 0);
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(npcId);
		
		byte level = (byte) (npcTemplate.getLevel() + skillLvl - 1);
		SummonStatsTemplate statsTemplate = DataManager.SUMMON_STATS_DATA.getSummonTemplate(npcId, level);
		Summon summon = new Summon(IDFactory.getInstance().nextId(), new SummonController(), spawn,
			npcTemplate, statsTemplate, level);
		summon.setKnownlist(new NpcKnownList(summon));
		summon.setEffectController(new EffectController(summon));
		summon.setMaster(creator);

		bringIntoWorld(summon, spawn, instanceId);
		return summon;
	}

	/**
	 * 
	 * @param worldId
	 * @param objectId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param walkerid
	 * @param randomwalk
	 * @return
	 */
	private SpawnTemplate createSpawnTemplate(int worldId, int objectId, float x, float y, float z, byte heading,
		int walkerid, int randomwalk)
	{
		SpawnTemplate spawnTemplate = new SpawnTemplate(x, y, z, heading, walkerid, randomwalk, 0);

		SpawnGroup spawnGroup = new SpawnGroup(worldId, objectId, 295, 1);
		spawnTemplate.setSpawnGroup(spawnGroup);
		spawnGroup.getObjects().add(spawnTemplate);

		return spawnTemplate;
	}

	/**
	 * Should be used when need to define whether spawn will be deleted after death Using this method spawns will not be
	 * saved with //save_spawn command
	 * 
	 * @param worldId
	 * @param objectId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param walkerid
	 * @param randomwalk
	 * @param noRespawn
	 * @return SpawnTemplate
	 */
	public SpawnTemplate addNewSpawn(int worldId, int instanceId, int objectId, float x, float y, float z,
		byte heading, int walkerid, int randomwalk, boolean noRespawn)
	{
		return this
			.addNewSpawn(worldId, instanceId, objectId, x, y, z, heading, walkerid, randomwalk, noRespawn, false);
	}

	/**
	 * 
	 * @param worldId
	 * @param instanceId
	 * @param objectId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param walkerid
	 * @param randomwalk
	 * @param noRespawn
	 * @param isNewSpawn
	 * @return SpawnTemplate
	 */
	public SpawnTemplate addNewSpawn(int worldId, int instanceId, int objectId, float x, float y, float z,
		byte heading, int walkerid, int randomwalk, boolean noRespawn, boolean isNewSpawn)
	{
		SpawnTemplate spawnTemplate = createSpawnTemplate(worldId, objectId, x, y, z, heading, walkerid, randomwalk);

		if(spawnTemplate == null)
		{
			log.warn("Object couldn't be spawned");
			return null;
		}

		if(!noRespawn)
		{
			DataManager.SPAWNS_DATA.addNewSpawnGroup(spawnTemplate.getSpawnGroup(), worldId, objectId, isNewSpawn);
		}

		spawnTemplate.setNoRespawn(noRespawn, instanceId);

		return spawnTemplate;
	}

	public void bringIntoWorld(VisibleObject visibleObject, SpawnTemplate spawn, int instanceIndex)
	{
		World world = World.getInstance();
		world.storeObject(visibleObject);
		world.setPosition(visibleObject, spawn.getWorldId(), instanceIndex, spawn.getX(), spawn.getY(), spawn.getZ(),
			spawn.getHeading());
		world.spawn(visibleObject);
	}

	/**
	 * Spawn all NPC's from templates
	 */
	public void spawnAll()
	{
		this.npcCounter = 0;
		this.gatherableCounter = 0;
		
		for(WorldMapTemplate worldMapTemplate : DataManager.WORLD_MAPS_DATA)
		{
			if(worldMapTemplate.isInstance())
				continue;
			int maxTwin = worldMapTemplate.getTwinCount();
			final int mapId = worldMapTemplate.getMapId();
			int numberToSpawn = maxTwin > 0 ? maxTwin : 1;

			for(int i = 1; i <= numberToSpawn; i++)
			{
				spawnInstance(mapId, i);
			}
		}

		Map<Integer, SpawnTemplate> spawns = DAOManager.getDAO(SpawnDAO.class).getAllSpawns();
		if (spawns != null)
		{
			int i = 0;
			for (Entry<Integer, SpawnTemplate> spawn : spawns.entrySet())
			{
				SpawnTemplate t = spawn.getValue();
				if (!t.isSpawned(1) && t.isNoRespawn(1))
				{
					continue;
				}
				VisibleObject o = spawnObject(spawn.getValue(), 1, true);
				if (o != null)
				{
					log.debug("Spawning "+o.getClass().getSimpleName()+" #"+t.getSpawnGroup().getNpcid()+" at map="+t.getWorldId()+", x="+t.getX()+", y="+t.getY()+", z="+t.getZ());
					DAOManager.getDAO(SpawnDAO.class).setSpawned(t.getSpawnId(), o.getObjectId(), true);
				}
				i++;
			}
			log.info("Loaded " + i + " spawns from database");
		}
		
		log.info("Loaded " + npcCounter + " npc spawns");
		log.info("Loaded " + gatherableCounter + " gatherable spawns");
		
		RiftSpawnManager.startRiftPool();
	}

	/**
	 * 
	 * @param worldId
	 * @param instanceIndex
	 */
	public void spawnInstance(int worldId, int instanceIndex)
	{

		List<SpawnGroup> worldSpawns = DataManager.SPAWNS_DATA.getSpawnsForWorld(worldId);

		if(worldSpawns == null || worldSpawns.size() == 0)
			return;

		int instanceSpawnCounter = 0;
		if (worldId == 300100000)
		{
			//special delivery spawn for Steel Rake
			int[] npcIds = { 215074, 215075, 215076, 215077, 215054, 215055 };
			Random rand = new Random();
			SpawnTemplate spawn = addNewSpawn(300100000, instanceIndex, npcIds[rand.nextInt(6)], 461.933f, 510.546f, 877.618f,
				(byte) 90, 0, 0, true, false);
			spawnObject(spawn,instanceIndex);
			instanceSpawnCounter++;
		}
		
		for(SpawnGroup spawnGroup : worldSpawns)
		{
			spawnGroup.resetLastSpawnCounter(instanceIndex);
			if(spawnGroup.getHandler() == null)
			{
				int pool = spawnGroup.getPool();
				for(int i = 0; i < pool; i++)
				{
					spawnObject(spawnGroup.getNextAvailableTemplate(instanceIndex), instanceIndex);

					instanceSpawnCounter++;
				}
				spawnGroup.clearLastSpawnedTemplate();
			}
			else
			{
				switch(spawnGroup.getHandler())
				{
					case RIFT:
						RiftSpawnManager.addRiftSpawnGroup(spawnGroup);
						break;
					case STATIC:
						StaticObjectSpawnManager.spawnGroup(spawnGroup, instanceIndex);
					default:
						break;
				}
			}
		}
		log.info("Spawned " + worldId + " [" + instanceIndex + "] : " + instanceSpawnCounter);
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final SpawnEngine instance = new SpawnEngine();
	}
}
