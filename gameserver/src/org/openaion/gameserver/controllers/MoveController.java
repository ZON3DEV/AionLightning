package org.openaion.gameserver.controllers;

import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.openaion.gameserver.ai.desires.impl.MoveToHomeDesire;
import org.openaion.gameserver.controllers.movement.MovementType;
import org.openaion.gameserver.geo.GeoEngine;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.siege.FortressGeneral;
import org.openaion.gameserver.network.aion.serverpackets.SM_MOVE;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;


/**
 * @author ATracer
 * 
 */
public class MoveController
{

	@SuppressWarnings("unused")
	private static final Logger	log				= Logger.getLogger(MoveController.class);

	private Future<?>			moveTask;
	private Creature			owner;

	private VisibleObject		target;

	private float				targetX;
	private float				targetY;
	private float				targetZ;

	private float				ownerX;
	private float				ownerY;
	private float				ownerZ;

	private float				x2;
	private float				y2;
	private float				z2;
	private byte				h2;

	private boolean				isFollowTarget;
	private float 				followOffset 	= 0;

	private float				speed			= 0;
	private float				distance		= 2;

	private boolean				walking;
	private boolean				canWalk			= true;
	private boolean				isStopped		= false;
	private boolean				directionChanged= true;

	private MovementType		movementType	= owner instanceof Player ? MovementType.MOVEMENT_START_KEYBOARD : MovementType.NPC_MOVEMENT_TYPE_III;

	/**
	 * @param owner
	 */
	public MoveController(Creature owner)
	{
		this.owner = owner;
	}

	public void stopFollowing()
	{
		this.isFollowTarget = false;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	/**
	 * @return The speed.
	 */
	public float getSpeed()
	{
		return speed;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(float distance)
	{
		this.distance = distance;
	}

	/**
	 * @return the walking
	 */
	public boolean isWalking()
	{
		return walking;
	}

	/**
	 * @param walking
	 *            the walking state to set
	 */
	public void setWalking(boolean walking)
	{
		this.walking = walking;
	}

	/**
	 * @return creature is able to walk
	 */
	public boolean canWalk()
	{
		return canWalk;
	}

	/**
	 * @param canWalk
	 *            if creature is able to walk
	 */
	public void setCanWalk(boolean canWalk)
	{
		this.canWalk = canWalk;
	}

	public void setNewDirection(float x, float y, float z)
	{
		if(this.targetX != x || this.targetY != y || this.targetZ != z)
			directionChanged = true;
		this.targetX = x;
		this.targetY = y;
		this.targetZ = z;
	}

	public float getTargetX()
	{
		return targetX;
	}

	public float getTargetY()
	{
		return targetY;
	}

	public float getTargetZ()
	{
		return targetZ;
	}

	public boolean isScheduled()
	{
		return moveTask != null && !moveTask.isCancelled();
	}

	public void schedule()
	{
		if(isScheduled())
			return;

		if(speed == 0)
			speed = owner.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000;

		moveTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				move();
			}
		}, 0, 1000);
	}

	private void move()
	{		
		/**
		 * Demo npc skills - prevent movement while casting
		 */
		if(!owner.canPerformMove() || owner.isCasting() || !canWalk)
		{
			if(!isStopped)
			{
				isStopped = true;
				owner.getController().stopMoving();
			}
			return;
		}

		target = owner.getTarget();
		ownerX = owner.getX();
		ownerY = owner.getY();
		ownerZ = owner.getZ();
		
		if(isFollowTarget && target != null)
		{
			distance = (float) MathUtil.getDistance(owner, target);

			// We are in the allowed range so no need to move!
			if(this.distance <= followOffset)
				return;

			// Check if a movement offset is defined
			if (followOffset > 0)
			{
				// Calculate movement angles needed
				float sin = (target.getY() - ownerY)/distance;
				float cos = (target.getX() - ownerX)/distance;
				distance -= followOffset;

				// Set new direction with offset included
				setNewDirection(ownerX + (float)(distance * cos), ownerY + (float)(distance * sin), target.getZ());
			}
			else
				setNewDirection(target.getX(), target.getY(), target.getZ());
		}
		else
			distance = (float) MathUtil.getDistance(ownerX, ownerY, ownerZ, targetX, targetY, targetZ);

		// 2 hacks for FortressGenerals
		if(owner instanceof FortressGeneral)
		{
			this.distance = 3.5f;

			if(MathUtil.getDistance(owner, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ()) > 150)
			{
				owner.getAi().clearDesires();
				owner.getAi().addDesire(new MoveToHomeDesire((Npc) owner, 100));
				owner.getAi().schedule();
				return;
			}
		}
		
		// Prevent from entering a loop around desired position
		if(this.distance < speed)
		{
			if(target != null && target instanceof Player)
			{
				ownerZ = GeoEngine.getInstance().getZ(owner.getWorldId(), ownerX, ownerY, ownerZ);
				
				Player player = (Player)target;
				
				if(player.isInState(CreatureState.FLYING) || player.isInState(CreatureState.GLIDING))
					targetZ = GeoEngine.getInstance().getZ(owner.getWorldId(), targetX, targetY, targetZ);
				PacketSendUtility.broadcastPacket(owner, new SM_MOVE(owner.getObjectId(), ownerX, ownerY, ownerZ, targetX, targetY, targetZ, (byte) 0, movementType));
			}
			
			World.getInstance().updatePosition(owner, targetX, targetY, targetZ, (byte) 0, true);
			directionChanged = false;
		}
		
		else if(distance > 0.5)// 0.5 as tolerance though 0 worked fine on most cases
		{
			if(target != null && target instanceof Player)
			{
				Player player = (Player)target;
				
				if(player.isInState(CreatureState.FLYING) || player.isInState(CreatureState.GLIDING))
					targetZ = GeoEngine.getInstance().getZ(owner.getWorldId(), targetX, targetY, targetZ);
			}
			
			x2 = (float) (((targetX - ownerX) / distance) * speed);
			y2 = (float) (((targetY - ownerY) / distance) * speed);
			isStopped = false;

			h2 = (byte) (Math.toDegrees(Math.atan2(y2, x2)) / 3);

			if(directionChanged)
			{
				ownerZ = GeoEngine.getInstance().getZ(owner.getWorldId(), ownerX, ownerY, ownerZ);
				z2 = (float) (((targetZ - ownerZ) / distance) * speed);
				PacketSendUtility.broadcastPacket(owner, new SM_MOVE(owner.getObjectId(), ownerX, ownerY, ownerZ,
					targetX, targetY, targetZ, h2, movementType));
				World.getInstance().updatePosition(owner, ownerX + x2, ownerY + y2, ownerZ + z2, h2, true);
				directionChanged = false;
			}
			else
			{
				z2 = (float) (((targetZ - ownerZ) / distance) * speed);
				World.getInstance().updatePosition(owner, ownerX + x2, ownerY + y2, ownerZ + z2, h2, false);
			}
		}
		else
		{
			if(!isStopped)
			{
				isStopped = true;
				owner.getController().stopMoving();
				if(owner instanceof FortressGeneral && owner.getTarget() instanceof Creature)
				{
					owner.getController().attackTarget((Creature) owner.getTarget());
				}
			}
		}
	}

	public double getDistanceToTarget()
	{
		if(isFollowTarget)
		{
			VisibleObject target = owner.getTarget();
			if(target != null)
				return MathUtil.getDistance(owner.getX(), owner.getY(), owner.getZ(), target.getX(), target.getY(),
					target.getZ());

		}
		return MathUtil.getDistance(owner.getX(), owner.getY(), owner.getZ(), targetX, targetY, targetZ);
	}

	public void setMovementType(MovementType movementType)
	{
		this.movementType = movementType;
	}

	public void stop()
	{
		this.walking = false;

		if(moveTask != null)
		{
			if(!moveTask.isCancelled())
				moveTask.cancel(true);
			moveTask = null;
		}
	}
	
	public void followTarget(float offset)
	{
		this.isFollowTarget = true;
		if(this.followOffset != offset)
			this.followOffset = offset;
	}
}
