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

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.world.World;


/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BackDashEffect")
public class BackDashEffect extends DamageEffect
{
	
	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect, DamageType.PHYSICAL, true);
	}
	
	@Override
	public void applyEffect(Effect effect)
	{
		super.applyEffect(effect);
		if (CustomConfig.GEODATA_EFFECTS_ENABLED)
		{
			Player player = (Player)effect.getEffector();
			double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
			float x2 = (float)(player.getX() + (25 * Math.cos(Math.PI+radian)));
			float y2 = (float)(player.getY() + (25 * Math.sin(Math.PI+radian)));
			float z2 = player.getZ() + 0.5f;
			World.getInstance().updatePosition(player, x2, y2, z2,player.getHeading(), false);
			PacketSendUtility.broadcastPacket(player, new SM_FORCED_MOVE(player, x2, y2, z2), true);
		}
	}
}
