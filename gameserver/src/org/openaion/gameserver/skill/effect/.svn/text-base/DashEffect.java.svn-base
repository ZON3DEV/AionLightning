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
import org.openaion.gameserver.world.World;


/**
 * @author ATracer
 * @edit kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DashEffect")
public class DashEffect extends DamageEffect
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
		Creature effector = effect.getEffector();
		Creature effected = effect.getEffected();
		double radian = (double) Math.atan2(effected.getY() - effector.getY(), effected.getX() - effector.getX());
		
		if (radian < 0)
			radian += (2*Math.PI);
		
		float x2 = (float)(effected.getX() + (1 * Math.cos(radian)));
		float y2 = (float)(effected.getY() + (1 * Math.sin(radian)));
		
		effect.setDashParam(new DashParam(DashType.DASH, x2, y2, effected.getZ()+0.25f, (int)effector.getHeading()));
		
		super.calculate(effect, DamageType.PHYSICAL, true);
	}
}
