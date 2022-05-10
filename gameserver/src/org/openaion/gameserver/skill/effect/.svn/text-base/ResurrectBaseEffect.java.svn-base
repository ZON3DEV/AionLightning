/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.controllers.movement.ActionObserver;
import org.openaion.gameserver.controllers.movement.ActionObserver.ObserverType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.model.Effect;


/**
 * @author blakawk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResurrectBaseEffect")
public class ResurrectBaseEffect extends BufEffect
{
	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect);
	}

	@Override
	public void applyEffect(final Effect effect)
	{
		super.applyEffect(effect);
		
		Creature effected = effect.getEffected();
		
		if (effected instanceof Player)
		{
			ActionObserver observer = new ActionObserver (ObserverType.DEATH) {
				@Override
				public void died(Creature creature)
				{
					if(creature instanceof Player)
					{
						((Player) creature).getReviveController().kiskRevive();
					}
				}
			};
			effect.getEffected().getObserveController().attach(observer);
			effect.setActionObserver(observer, position);
		}
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		
		if (!effect.getEffected().getLifeStats().isAlreadyDead() && effect.getActionObserver(position) != null)
		{
			effect.getEffected().getObserveController().removeDeathObserver(effect.getActionObserver(position));
		}
	}
}
