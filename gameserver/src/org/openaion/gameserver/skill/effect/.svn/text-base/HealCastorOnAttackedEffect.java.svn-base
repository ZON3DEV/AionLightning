/*
 * This file is part of zetta-core <zetta-core.org>.
 *
 *  zetta-core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  zetta-core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with zetta-core.  If not, see <http://www.gnu.org/licenses/>.
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
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.utils.MathUtil;



/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealCastorOnAttackedEffect")
public class HealCastorOnAttackedEffect extends EffectTemplate
{	
	@XmlAttribute
	protected int value;
	@XmlAttribute
	protected int delta;

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect)
	{
		// The skill can be used only by a player
		if (effect.getEffector() instanceof Player)
			super.calculate(effect);
	}

	@Override
	public void startEffect(final Effect effect)
	{
		super.startEffect(effect);
		
		final Player player = (Player)effect.getEffector();
		final int valueWithDelta = value + delta * effect.getSkillLevel();
		
		//TODO find proper logId
		ActionObserver observer = new ActionObserver(ObserverType.ATTACKED){

			@Override
			public void attacked(Creature creature)
			{
				if (player.getPlayerGroup() != null)
				{
					for (Player p : player.getPlayerGroup().getMembers())
					{
						if (MathUtil.isIn3dRange(effect.getEffected(),p,5.0f))
							p.getLifeStats().increaseHp(TYPE.HP, valueWithDelta, effect.getSkillId(), 3);
					}
				}
				//TODO alliance
				else
				{
					if (MathUtil.isIn3dRange(effect.getEffected(),player,5.0f))
						player.getLifeStats().increaseHp(TYPE.HP, valueWithDelta, effect.getSkillId(), 3);
				}
			}
		};
		
		effect.getEffected().getObserveController().addObserver(observer);
		effect.setActionObserver(observer, position);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		ActionObserver observer = effect.getActionObserver(position);
		effect.getEffected().getObserveController().removeObserver(observer);
	}
}