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

import org.openaion.gameserver.controllers.movement.ActionObserver;
import org.openaion.gameserver.controllers.movement.ActionObserver.ObserverType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureVisualState;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.skill.model.SkillTargetSlot;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Sweetkr
 * @author Cura
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HideEffect")
public class HideEffect extends BufEffect
{
	@XmlAttribute
	protected int value;

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		
		Creature effected = effect.getEffected();
		effected.getEffectController().unsetAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());

		CreatureVisualState visualState;

		switch(value)
		{
			case 1:
				visualState = CreatureVisualState.HIDE1;
				break;
			case 2:
				visualState = CreatureVisualState.HIDE2;
				break;
			case 3:
				visualState = CreatureVisualState.HIDE3;
				break;
			case 10:
				visualState = CreatureVisualState.HIDE10;
				break;
			case 13:
				visualState = CreatureVisualState.HIDE13;
				break;
			case 20:
				visualState = CreatureVisualState.HIDE20;
				break;
			default:
				visualState = CreatureVisualState.VISIBLE;
				break;
		}
		effected.unsetVisualState(visualState);

		if(effected instanceof Player)
		{
			PacketSendUtility.broadcastPacket((Player)effected, new SM_PLAYER_STATE((Player)effected), true);
		}
	}

	@Override
	public void startEffect(final Effect effect)
	{
		super.startEffect(effect);
		
		final Creature effected = effect.getEffected();
		effect.setAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
		effected.getEffectController().setAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());

		CreatureVisualState visualState;

		switch(value)
		{
			case 1:
				visualState = CreatureVisualState.HIDE1;
				break;
			case 2:
				visualState = CreatureVisualState.HIDE2;
				break;
			case 3:
				visualState = CreatureVisualState.HIDE3;
				break;
			case 10:
				visualState = CreatureVisualState.HIDE10;
				break;
			case 13:
				visualState = CreatureVisualState.HIDE13;
				break;
			case 20:
				visualState = CreatureVisualState.HIDE20;
				break;
			default:
				visualState = CreatureVisualState.VISIBLE;
				break;
		}
		effected.setVisualState(visualState);

		if(effected instanceof Player)
		{
			PacketSendUtility.broadcastPacket((Player)effected, new SM_PLAYER_STATE((Player)effected), true);
		}
		
		//Remove Hide when use skill
		effected.getObserveController().addObserver(
			new ActionObserver(ObserverType.SKILLUSE)
			{
				int counter = 0;
				@Override
				public void skilluse(Skill skill)
				{
				    boolean interrupt = true;
				    if(skill.getSkillTemplate().getTargetSlot() == SkillTargetSlot.BUFF)
				    {
    				    int limit = 0;
    				    switch(effect.getSkillId())
    				    {
    				        case 948:
    				        case 951:
    				            limit = 3;
    				            break;
    				        case 582:
    				            limit = 2;
    				            break;
    				    }
    				    counter++;
    				    if(counter <= limit)
    				        interrupt = false;
				    }
				    if(interrupt)
				    {
				        effected.getEffectController().removeEffect(effect.getSkillId());
				        effected.getObserveController().removeObserver(this);
				    }					
				}
			}
		);
		
		// Remove Hide when attacked
		effected.getObserveController().attach(
			new ActionObserver(ObserverType.ATTACKED)
			{
				@Override
				public void attacked(Creature creature)
				{
					effected.getEffectController().removeEffect(effect.getSkillId());
				}
			}
		);
		
		// Remove Hide when attacking
		effected.getObserveController().attach(
			new ActionObserver(ObserverType.ATTACK)
			{
				@Override
				public void attack(Creature creature)
				{
					effected.getEffectController().removeEffect(effect.getSkillId());
				}			
			}
		);
	}
}
