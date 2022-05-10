package org.openaion.gameserver.ai.npcai;


import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.desires.AbstractDesire;
import org.openaion.gameserver.ai.events.Event;
import org.openaion.gameserver.ai.events.handler.EventHandler;
import org.openaion.gameserver.ai.state.AIState;
import org.openaion.gameserver.ai.state.handler.NoneNpcStateHandler;
import org.openaion.gameserver.ai.state.handler.StateHandler;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Totem;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.SkillEngine;
import org.openaion.gameserver.skill.model.Skill;


/**
 * @author kecimis
 *
 */
public class TotemAi extends NpcAi
{
	public TotemAi()
	{
		/**
		 * Event handlers
		 */
		this.addEventHandler(new RespawnEventHandler());
		this.addEventHandler(new AttackedEventHandler());
			
		/**
		 * State handlers
		 */
		this.addStateHandler(new ActiveTotemStateHandler());
		this.addStateHandler(new NoneNpcStateHandler());
	}
	
	public class RespawnEventHandler implements EventHandler
	{
		@Override
		public Event getEvent()
		{
			return Event.RESPAWNED;
		}

		@Override
		public void handleEvent(Event event, AI<?> ai)
		{
			ai.setAiState(AIState.ACTIVE);
			if(!ai.isScheduled())
				ai.analyzeState();
		}

	}
	class AttackedEventHandler implements EventHandler
	{
		@Override
		public Event getEvent()
		{
			return Event.ATTACKED;
		}

		@Override
		public void handleEvent(Event event, AI<?> ai)
		{
			ai.setAiState(AIState.ACTIVE);
			if(!ai.isScheduled())
				ai.analyzeState();
		}
	}
	
	class ActiveTotemStateHandler extends StateHandler
	{
		@Override
		public AIState getState()
		{
			return AIState.ACTIVE;
		}

		@Override
		public void handleState(AIState state, AI<?> ai)
		{
			ai.clearDesires();
			Totem owner = (Totem) ai.getOwner();
			Creature totemOwner = owner.getCreator();
			
			Creature target = null;
			
			if (totemOwner instanceof Player)
			{
				target = totemOwner;
			}	
			
						

			if (target != null)
			{
				ai.addDesire(new TotemSkillUseDesire(owner, (Creature) target, AIState.ACTIVE
					.getPriority()));
			}
			
			
			if(ai.desireQueueSize() == 0)
			{
				ai.handleEvent(Event.NOTHING_TODO);
			}
			else
			{
				ai.schedule();
			}
		}
	}
	
	class TotemSkillUseDesire extends AbstractDesire
	{
		/**
		 * Totem object
		 */
		private Totem		owner;
		/**
		 * Owner of totem
		 */
		private Creature	target;

		/**
		 * 
		 * @param desirePower
		 * @param owner
		 */
		private TotemSkillUseDesire(Totem owner, Creature target, int desirePower)
		{
			super(desirePower);
			this.owner = owner;
			this.target = target;
		}

		@Override
		public boolean handleDesire(AI<?> ai)
		{		
			if(target == null || target.getLifeStats().isAlreadyDead())
				return true;
			
			if (owner.getActingCreature() == null)
			{
				owner.getLifeStats().reduceHp(10000, owner);
				return false;
			}
			
			if(owner.getActingCreature().isEnemy(target))
				return false;
			
			if(owner.getSkillId() == 0)
				return false;
			else
			{
				Skill skill = SkillEngine.getInstance().getSkill(owner, owner.getSkillId(), 1, target);
				if(skill != null)
				{
					skill.useSkill();
				}
			}
			return true;
		}

		@Override
		public int getExecutionInterval()
		{
			//interval 3 sec
			return 3;
		}

		@Override
		public void onClear()
		{
			// TODO Auto-generated method stub
		}
	}
}