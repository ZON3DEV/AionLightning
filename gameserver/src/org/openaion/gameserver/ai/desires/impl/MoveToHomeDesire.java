package org.openaion.gameserver.ai.desires.impl;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.desires.AbstractDesire;
import org.openaion.gameserver.ai.desires.MoveDesire;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.controllers.movement.MovementType;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.ShoutEventType;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.services.NpcShoutsService;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;

/**
 * @author ATracer
 *
 */
public class MoveToHomeDesire extends AbstractDesire implements MoveDesire
{
	private Npc owner;
	private float x;
	private float y;
	private float z;
	boolean started = false;
	
	public MoveToHomeDesire(Npc owner, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		SpawnTemplate template = owner.getSpawn();
		x = template.getX();
		y = template.getY();
		z = template.getZ();
	}

	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if (owner == null || owner.getLifeStats().isAlreadyDead())
			return false;
		
		if(!started)
		{
			if(MathUtil.getDistance(owner, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ()) > 50){
				// target too far, teleport to spawn point.
				World.getInstance().updatePosition(owner, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ(), owner.getSpawn().getHeading(), true);
				started = true;
			}
			else {
				owner.getMoveController().stopFollowing();
				owner.getMoveController().setNewDirection(x, y, z);
				owner.getGameStats().setStat(StatEnum.SPEED, (int) owner.getObjectTemplate().getStatsTemplate().getRunSpeed() * 1000);
				owner.getMoveController().setSpeed(owner.getObjectTemplate().getStatsTemplate().getRunSpeed());
				owner.getMoveController().setMovementType(MovementType.NPC_MOVEMENT_TYPE_III);
				PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2));
				started = true;
			}
			NpcShoutsService.getInstance().handleEvent(owner, owner, ShoutEventType.LEAVE);
		}

		double dist = MathUtil.getDistance(owner, x, y, z);
		if(dist < 1)
		{
			//make sure npc is at right position
			World.getInstance().updatePosition(owner, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ(), owner.getSpawn().getHeading(), true);
			ai.handleEvent(Event.BACK_HOME);
			return false;
		}
		else
		{
			if(!owner.getMoveController().isScheduled())
				owner.getMoveController().schedule();
		}
		return true;
	}

	@Override
	public int getExecutionInterval()
	{
		return 5;
	}

	@Override
	public void onClear()
	{
 		owner.getMoveController().stop();
 		owner.getController().stopMoving();
	}	
}
