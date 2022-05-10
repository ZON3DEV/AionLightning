/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.ai.npcai;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.desires.AbstractDesire;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.ai.events.handler.EventHandler;
import org.openaion.gameserver.ai.state.AIState;
import org.openaion.gameserver.ai.state.handler.NoneNpcStateHandler;
import org.openaion.gameserver.ai.state.handler.StateHandler;
import org.openaion.gameserver.model.gameobjects.AionObject;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Trap;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.skill.SkillEngine;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author ATracer
 * 
 */
public class TrapAi extends NpcAi
{
	public TrapAi()
	{
		/**
		 * Event handlers
		 */
		this.addEventHandler(new SeeObjectEventHandler());

		/**
		 * State handlers
		 */
		this.addStateHandler(new ActiveTrapStateHandler());
		this.addStateHandler(new NoneNpcStateHandler());
	}

	public class SeeObjectEventHandler implements EventHandler
	{
		@Override
		public Event getEvent()
		{
			return Event.SEE_CREATURE;
		}

		@Override
		public void handleEvent(Event event, AI<?> ai)
		{
			ai.setAiState(AIState.ACTIVE);
			if(!ai.isScheduled())
				ai.analyzeState();
		}

	}

	class ActiveTrapStateHandler extends StateHandler
	{
		@Override
		public AIState getState()
		{
			return AIState.ACTIVE;
		}

		@Override
		public void handleState(AIState state, final AI<?> ai)
		{
			ai.clearDesires();
			final Trap owner = (Trap) ai.getOwner();
			final Creature trapCreator = owner.getCreator();			
			
			if (owner.getActingCreature() == null)
			{
				owner.getLifeStats().reduceHp(10000, owner);
				return;
			}
			
			owner.getKnownList().doOnAllObjects(new Executor<AionObject>(){
				@Override
				public boolean run (AionObject obj)
				{
					if (obj instanceof VisibleObject && trapCreator.isEnemy((VisibleObject)obj))
					{
						ai.addDesire(new TrapExplodeDesire(owner, trapCreator, AIState.ACTIVE.getPriority()));
						return false;
					}
					return true;
				}
			}, true);
			
			if(ai.desireQueueSize() == 0)
				ai.handleEvent(Event.NOTHING_TODO);
			else
				ai.schedule();
		}
	}

	class TrapExplodeDesire extends AbstractDesire
	{
		/**
		 * Trap object
		 */
		private Trap		owner;
		/**
		 * Owner of trap
		 */
		private Creature	creator;

		/**
		 * 
		 * @param desirePower
		 * @param owner
		 */
		private TrapExplodeDesire(Trap owner, Creature creator, int desirePower)
		{
			super(desirePower);
			this.owner = owner;
			this.creator = creator;
		}

		@Override
		public boolean handleDesire(AI<?> ai)
		{
			if (creator == null)
			{
				owner.getLifeStats().reduceHp(10000, owner);
				return false;
			}
			owner.getKnownList().doOnAllObjects(new Executor<AionObject>(){
				@Override
				public boolean run (AionObject visibleObject)
				{
					if(visibleObject == null)
						return true;

					if(visibleObject instanceof Creature)
					{
						Creature creature = (Creature) visibleObject;

						if(creature.getLifeStats() != null && !creature.getLifeStats().isAlreadyDead()
							&& MathUtil.isIn3dRange(owner, creature, owner.getAggroRange()))
						{
							if(!creator.isEnemy(creature))
								return true;

							owner.getAi().setAiState(AIState.NONE);

							int skillId = owner.getSkillId();
							Skill skill = SkillEngine.getInstance().getSkill(owner, skillId, 1, creature);
							skill.useSkill();
							ThreadPoolManager.getInstance().schedule(new Runnable() 
							{
								public void run() 
								{
									owner.getController().onDespawn(true);
								}
							}, skill.getSkillTemplate().getDuration() + 1000);
							return false;
						}
					}
					
					return true;
				}
			}, true);
			
			return true;
		}

		@Override
		public int getExecutionInterval()
		{
			return 2;
		}

		@Override
		public void onClear()
		{
			// TODO Auto-generated method stub
		}
	}
}
