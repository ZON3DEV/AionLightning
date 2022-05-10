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
package org.openaion.gameserver.skill.properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;
import org.openaion.gameserver.geo.GeoEngine;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirstTargetRangeProperty")
public class FirstTargetRangeProperty extends Property
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(FirstTargetRangeProperty.class);

	@XmlAttribute(required = true)
	protected int value;

	@Override
	public boolean set(Skill skill)
	{
		if(!skill.isFirstTargetRangeCheck())
			return true;
		
		Creature effector = skill.getEffector();
		Creature firstTarget = skill.getFirstTarget();
		if(firstTarget == null && skill.getTargetType() == 1)//point skill
		{
			if (MathUtil.getDistance(skill.getEffector(), skill.getX(), skill.getY(), skill.getZ()) <= value)
				return true;
			else
				return false;
		}
		else if (firstTarget == null)
			return false;

		
		if (firstTarget.getPosition().getMapId() == 0)
			log.warn("FirstTarget has mapId of 0. (" + firstTarget.getName() + ")");
		
		float distance = (float)value;
		
		//addweaponrange
		if (skill.getAddWeaponRangeProperty())
			distance += (float)skill.getEffector().getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f;

		//tolerance
		distance += 3.0;

		//testing new firsttargetrangeproperty
		if (!MathUtil.isIn3dRange(effector, firstTarget, distance))
		{
			if (effector instanceof Player)
				PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.STR_ATTACK_TOO_FAR_FROM_TARGET());
			return false;
		}

		if (!GeoEngine.getInstance().canSee(effector, firstTarget))
		{
			if (effector instanceof Player)
				PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.STR_SKILL_OBSTACLE);
			return false;
		}
		return true;
	}
	
	public int getValue()
	{
		return value;
	}

}
