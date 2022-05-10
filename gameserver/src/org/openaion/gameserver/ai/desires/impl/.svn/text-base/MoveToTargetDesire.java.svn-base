package org.openaion.gameserver.ai.desires.impl;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.desires.AbstractDesire;
import org.openaion.gameserver.ai.desires.MoveDesire;
import org.openaion.gameserver.ai.state.AIState;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.utils.MathUtil;
/**
 * @author Pinguin, ATracer
 *
 */
public class MoveToTargetDesire extends AbstractDesire implements MoveDesire
{
	private Npc owner;
	private Creature target;
	private float targetOffset;
	
	/**
	 * @param crt 
	 * @param desirePower
	 */
	public MoveToTargetDesire(Npc owner, Creature target, float targetOffset, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		this.target = target;
		this.targetOffset = targetOffset;
	}

	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if (owner == null || owner.getLifeStats().isAlreadyDead())
			return false;
		if(target == null || target.getLifeStats().isAlreadyDead())
			return false;

		if(MathUtil.isInRange(owner, target, targetOffset))
		{
			owner.getMoveController().stop();
			return true;
		}

		owner.getMoveController().followTarget(targetOffset);
		if(!owner.getMoveController().isScheduled())
			owner.getMoveController().schedule();
		
		/** MoveToHome if *target* is trying to lure it away from home 
		 * (this will prevent Npcs from going home and heal inevitably) 
			and not only if target is too far away **/
		if (owner.getSpawn() != null &&
			MathUtil.getDistance(target, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ()) > 125)
		{
			owner.getLifeStats().increaseHp(TYPE.NATURAL_HP, owner.getLifeStats().getMaxHp());
			ai.setAiState(AIState.MOVINGTOHOME);
			return false;
		}

		if(owner.getMoveController().getDistanceToTarget() > 150)
			return false;
		
		return true;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof MoveToTargetDesire))
			return false;

		MoveToTargetDesire that = (MoveToTargetDesire) o;
		return target.equals(that.target);
	}

	/**
	 * @return the target
	 */
	public Creature getTarget()
	{	
		return target;
	}

	@Override
	public int getExecutionInterval()
	{
		return 1;
	}

	@Override
	public void onClear()
	{
		owner.getMoveController().stop();
	}
}