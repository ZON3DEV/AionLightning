package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.Map.Entry;

import org.openaion.gameserver.model.NpcType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.GroupGate;
import org.openaion.gameserver.model.gameobjects.Homing;
import org.openaion.gameserver.model.gameobjects.Kisk;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.Servant;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.Trap;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.items.ItemSlot;
import org.openaion.gameserver.model.items.NpcEquippedGear;
import org.openaion.gameserver.model.siege.Artifact;
import org.openaion.gameserver.model.siege.FortressGate;
import org.openaion.gameserver.model.siege.FortressGateArtifact;
import org.openaion.gameserver.model.siege.InstancePortal;
import org.openaion.gameserver.model.templates.NpcTemplate;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.SiegeService;
import org.openaion.gameserver.world.WorldType;


/**
 * This packet is displaying visible npc/monsters.
 * 
 * @author -Nemesiss-
 * 
 */
public class SM_NPC_INFO extends AionServerPacket
{
	/**
	 * Visible npc
	 */
	private Creature npc;
	private NpcTemplate npcTemplate;
	private int npcId;
	private int masterObjId;
	private String masterName = "";
	@SuppressWarnings("unused")
	private float speed = 0.3f;
	private final int npcTypeId;
	

	/**
	 * Constructs new <tt>SM_NPC_INFO </tt> packet
	 * 
	 * @param npc
	 *            visible npc.
	 * @param player 
	 */
	public SM_NPC_INFO(Npc npc, Player player)
	{
		this.npc = npc;
		npcTemplate = npc.getObjectTemplate();
		if(npcTemplate.getNpcType() == NpcType.NEUTRAL || npcTemplate.getNpcType() == NpcType.ARTIFACT){
			if(player.isAggroIconTo(npc.getTribe()))
				npcTypeId = NpcType.NEUTRAL.getId();
			else
				npcTypeId = NpcType.NON_ATTACKABLE.getId();
		}
		else{
		npcTypeId = (player.isAggroIconTo(npc.getTribe()) ?
			NpcType.AGGRESSIVE.getId() : npcTemplate.getNpcType().getId());
		}
		npcId = npc.getNpcId();
		
	}
	
	/**
	 * Constructs new <tt>SM_NPC_INFO </tt> packet
	 * 
	 * @param player 
	 * @param kisk - the visible npc.
	 */
	public SM_NPC_INFO(Player player, Kisk kisk)
	{
		this.npc = kisk;
		npcTypeId = (kisk.isAggroFrom(player) ?
			NpcType.ATTACKABLE.getId() : NpcType.NON_ATTACKABLE.getId());
		npcTemplate = kisk.getObjectTemplate();
		npcId = kisk.getNpcId();
		
		masterObjId = kisk.getOwnerObjectId();
		masterName = kisk.getOwnerName();
	}
	
	/**
	 * 
	 * @param player
	 * @param groupgate - the visible npc.
	 */
	public SM_NPC_INFO(Player player, GroupGate groupgate)
	{
		this.npc = groupgate;
		npcTypeId = (groupgate.isAggroFrom(player) ?
			NpcType.ATTACKABLE.getId() : NpcType.NON_ATTACKABLE.getId());
		npcTemplate = groupgate.getObjectTemplate();
		npcId = groupgate.getNpcId();
		
		Player owner = (Player)groupgate.getCreator();
		if(owner != null)
		{
			masterObjId = owner.getObjectId();
			masterName = owner.getName();
		}
	}
	
	/**
	 * 
	 * @param summon
	 */
	public SM_NPC_INFO(Summon summon)
	{
		this.npc = summon;
		npcTemplate = summon.getObjectTemplate();
		npcTypeId = npcTemplate.getNpcType().getId();
		npcId = summon.getNpcId();
		Player owner = summon.getMaster();
		if(owner != null)
		{
			masterObjId = owner.getObjectId();
			masterName = owner.getName();
			speed = owner.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000f;
		}
		else
		{
			masterName = "LOST";
		}
	}
	
	    public SM_NPC_INFO(Trap trap) {
        this.npc = trap;
        npcTemplate = trap.getObjectTemplate();
        npcTypeId = npcTemplate.getNpcType().getId();
        npcId = trap.getNpcId();
        Player owner = (Player)trap.getMaster();
        if (owner != null) {
            masterObjId = owner.getObjectId();
            masterName = owner.getName();
            speed = 0;
        } else {
            masterName = "LOST";
        }
    }
	
	/**
	 *
	 * * @param homing
	 */
	public SM_NPC_INFO(Homing homing)
    {
		this.npc = homing;
		npcTemplate = homing.getObjectTemplate();
		npcTypeId = npcTemplate.getNpcType().getId();
		npcId = homing.getNpcId();
		Creature owner = homing.getCreator();
		if(owner != null)
		{
			masterObjId = owner.getObjectId();
		}
    }

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeF(buf, npc.getX());// x
		writeF(buf, npc.getY());// y
		writeF(buf, npc.getZ());// z
		writeD(buf, npc.getObjectId());
		writeD(buf, npcId);
		writeD(buf, npcId);

		writeC(buf, npcTypeId);

		writeH(buf, npc.getState());// unk 65=normal,0x47 (71)= [dead npc ?]no drop,0x21(33)=fight state,0x07=[dead monster?]
								// no drop
								// 3,19 - wings spread (NPCs)
								// 5,6,11,21 - sitting (NPC)
								// 7,23 - dead (no drop)
								// 8,24 - [dead][NPC only] - looks like some orb of light (no normal mesh)
								// 32,33 - fight mode

		writeC(buf, npc.getHeading());
		writeD(buf, npcTemplate.getNameId());
		
		if(npcTemplate.getTitleId() > 0)
			writeD(buf, npcTemplate.getTitleId());// titleID
		else if(npc.getWorldType() == WorldType.ABYSS || npc.getWorldType() == WorldType.BALAUREA)
		{
			int slocid = SiegeService.getInstance().getSiegeNpcLocation(npc.getObjectId());
			if(slocid > 0)
			{
				int fortressTitleId = SiegeService.getInstance().getFortressTitleId(slocid);
				writeD(buf, fortressTitleId);
			}
			else
			{
				writeD(buf, 0);
			}
		}
		else
			writeD(buf, 0);

		writeH(buf, 0x00);// unk
		writeC(buf, 0x00);// unk
		writeD(buf, 0x00);// unk

		/*
		 * Master Info (Summon, Kisk, Etc)
		 */
		writeD(buf, masterObjId);// masterObjectId
		writeS(buf, masterName);// masterName

		int maxHp = npc.getLifeStats().getMaxHp();
		int currHp = npc.getLifeStats().getCurrentHp();
		writeC(buf, 100 * currHp / maxHp);// %hp
		writeD(buf, npc.getGameStats().getCurrentStat(StatEnum.MAXHP));
		writeC(buf, npc.getLevel());// lvl

		NpcEquippedGear gear = npcTemplate.getEquipment();
		if(gear == null)
			writeH(buf, 0x00);
		else
		{
			writeH(buf, gear.getItemsMask());
			for(Entry<ItemSlot,ItemTemplate> item: gear) // getting it from template ( later if we make sure that npcs actually use items, we'll make Item from it )
			{
				writeD(buf, item.getValue().getTemplateId());
				writeD(buf, 0x00);
				writeD(buf, 0x00);
				writeH(buf, 0x00);
			}
		}

		writeF(buf, 1.5f);// unk
		writeF(buf, npcTemplate.getHeight());
		writeF(buf, npc.getGameStats().getCurrentStat(StatEnum.SPEED)/1000);// speed

		writeH(buf, 2000);// 0x834 (depends on speed ? )
		writeH(buf, 2000);// 0x834
		
		if(npc instanceof Servant)
			writeC(buf, 0x01);// unk
		else
			writeC(buf, 0x00);

		/**
		 * Movement
		 */
		writeF(buf, npc.getX());// x
		writeF(buf, npc.getY());// y
		writeF(buf, npc.getZ());// z
		writeC(buf, 0x00); // move type
		if(npc instanceof FortressGate)
		{
			writeH(buf, ((FortressGate)npc).getStaticId());
		}
		else if(npc instanceof FortressGateArtifact)
		{
			writeH(buf, ((FortressGateArtifact)npc).getStaticId());
		}
		else if(npc instanceof InstancePortal)
		{
			writeH(buf, ((InstancePortal)npc).getStaticId());
		}
		else if(npc instanceof Artifact)
		{
			writeH(buf, ((Artifact)npc).getStaticId());
		}
		else
		{
			SpawnTemplate spawn = npc.getSpawn();
			if (spawn == null)
				writeH(buf, 0);
			else
				writeH(buf, spawn.getStaticid());
		}
		writeC(buf, 0);
		writeC(buf, 0); // all unknown
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, npc.getVisualState()); // visualState

		/**
		 * 1 : normal (kisk too)
		 * 2 : summon
		 * 32 : trap
		 * 1024 : holy servant, noble energy
		 */
		writeH(buf, npc.getNpcObjectType().getId());
		writeC(buf, 0x00);// unk
		if(npc instanceof Artifact)
		{
			writeD(buf, 0);
		}
		else
		{
			writeD(buf, npc.getTarget() == null ? 0 : npc.getTarget().getObjectId());
		}
	}
}
