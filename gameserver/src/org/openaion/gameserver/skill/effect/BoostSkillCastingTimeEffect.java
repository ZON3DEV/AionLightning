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

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.SkillSubType;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoostSkillCastingTimeEffect")
public class BoostSkillCastingTimeEffect extends BufEffect
{
	@XmlAttribute(required = true)
	protected int percent;
	@XmlAttribute(required = true)
	protected SkillSubType type = SkillSubType.NONE;;
	
	/*
	 * SUMMONTRAP,
	 * SUMMON,
	 * SUMMONHOMING,
	 * HEAL,
	 * ATTACK,
	 * NONE //general, for all skills, example: Boon of Quickness
	 * (non-Javadoc)
	 * @see org.openaion.gameserver.skill.effect.BufEffect#startEffect(org.openaion.gameserver.skill.model.Effect)
	 */
	@Override
	public void startEffect(Effect effect)
	{
		effect.getEffected().getController().addBoostCastingRate(type, percent);

		if (type == SkillSubType.NONE && effect.getEffected() instanceof Player)
			PacketSendUtility.sendPacket((Player)effect.getEffected(), new SM_STATS_INFO((Player)effect.getEffected()));
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		effect.getEffected().getController().removeBoostCastingRate(type, percent);
		
		if (type == SkillSubType.NONE && effect.getEffected() instanceof Player)
			PacketSendUtility.sendPacket((Player)effect.getEffected(), new SM_STATS_INFO((Player)effect.getEffected()));
	}

}
