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
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.DashParam;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.DashParam.DashType;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.world.World;


/**
 * @author Sarynth
 * @edit kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoveBehindEffect")
public class MoveBehindEffect extends DamageEffect
{
	@Override
	public void applyEffect(Effect effect)
	{
		Player effector = (Player)effect.getEffector();
		
		DashParam dash = effect.getDashParam();
		World.getInstance().updatePosition(
			effector,
			dash.getX(),
			dash.getY(),
			dash.getZ(),
			(byte)dash.getHeading());
		
		super.applyEffect(effect);
	}
	
	@Override
	public void calculate(Effect effect)
	{
		Creature effected = effect.getEffected();
		
		// Move Effector to Effected
		double radian = Math.toRadians(MathUtil.convertHeadingToDegree(effected.getHeading()));
		float x1 = (float)(Math.cos(Math.PI + radian) * 1.3F);
		float y1 = (float)(Math.sin(Math.PI + radian) * 1.3F);

		effect.setDashParam(new DashParam(DashType.MOVEBEHIND, effected.getX() + x1, effected.getY() + y1, effected.getZ() + 0.25f, effected.getHeading()));
		
		this.calculate(effect, DamageType.PHYSICAL, true);
	}
	
	
}
