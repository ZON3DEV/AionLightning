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
package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.controllers.movement.ActionObserver;
import org.openaion.gameserver.controllers.movement.ActionObserver.ObserverType;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.ProvokeTarget;
import org.openaion.gameserver.skill.model.SkillTemplate;


/**
 * @author ATracer
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProvokerEffect")
public class ProvokerEffect extends ShieldEffect
{
	@XmlAttribute(name = "provoke_target")
	protected ProvokeTarget	provokeTarget;
	@XmlAttribute(name = "skill_id")
	protected int			skillId;

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(Effect effect)
	{
		ActionObserver observer = null;
		final Creature effector = effect.getEffector();
		
		if(attacktype == null)
			return;
		
		switch(attacktype)
		{
			case ATTACK://nmlattack
				observer = new ActionObserver(ObserverType.ATTACK){

					@Override
					public void attack(Creature creature)
					{
						if(Rnd.get(0, 100) <= probability)
						{
							Creature target = getProvokeTarget(provokeTarget, effector, creature);
							createProvokedEffect(effector, target);
						}
					}

				};
				break;
			case ATTACKED://everyhit
				observer = new ActionObserver(ObserverType.ATTACKED){

					@Override
					public void attacked(Creature creature)
					{
						if(Rnd.get(0, 100) <= probability)
						{
							Creature target = getProvokeTarget(provokeTarget, effector, creature);
							createProvokedEffect(effector, target);
						}
					}
				};
				break;
			case MAGICAL_SKILL://mahit
				observer = new ActionObserver(ObserverType.HITTED){

					@Override
					public void hitted(Creature creature, DamageType type)
					{
						if(type == DamageType.MAGICAL)
						{
							if(Rnd.get(0, 100) <= probability)
							{
								Creature target = getProvokeTarget(provokeTarget, effector, creature);
								createProvokedEffect(effector, target);
							}
						}
					}
				};
				break;
			case PHYSICAL_SKILL://phhit
				observer = new ActionObserver(ObserverType.HITTED){

					@Override
					public void hitted(Creature creature, DamageType type)
					{
						if(type == DamageType.PHYSICAL)
						{
							if(Rnd.get(0, 100) <= probability)
							{
								Creature target = getProvokeTarget(provokeTarget, effector, creature);
								createProvokedEffect(effector, target);
							}
						}
					}
				};
				break;
		}

		if(observer == null)
			return;

		effect.setActionObserver(observer, position);
		effect.getEffected().getObserveController().addObserver(observer);
	}

	/**
	 * 
	 * @param effector
	 * @param target
	 */
	private void createProvokedEffect(final Creature effector, Creature target)
	{
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		Effect e = new Effect(effector, target, template, template.getLvl(), template.getEffectsDuration());
		e.initialize();
		e.applyEffect();
	}

	/**
	 * 
	 * @param provokeTarget
	 * @param effector
	 * @param target
	 * @return
	 */
	private Creature getProvokeTarget(ProvokeTarget provokeTarget, Creature effector, Creature target)
	{
		switch(provokeTarget)
		{
			case ME:
				return effector;
			case OPPONENT:
				return target;
		}
		throw new IllegalArgumentException("Provoker target is invalid " + provokeTarget);
	}

	@Override
	public void endEffect(Effect effect)
	{
		ActionObserver observer = effect.getActionObserver(position);
		if(observer != null)
			effect.getEffected().getObserveController().removeObserver(observer);
	}
}
