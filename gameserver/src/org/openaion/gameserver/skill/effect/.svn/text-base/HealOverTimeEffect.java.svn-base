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
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.HealType;


/**
 * @author ATracer,
 * @rework kecimis 
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealOverTimeEffect")
public abstract class HealOverTimeEffect extends AbstractOverTimeEffect
{
	public void calculate(Effect effect, HealType healtype)
	{
		int valueWithDelta = value + delta * effect.getSkillLevel();

		int maxCurValue = getMaxCurStatValue(effect);
		int possibleHealValue = 0;
		if(percent)
		{
			possibleHealValue = maxCurValue * valueWithDelta / 100;
		}
		else // non percent heal
		{
			// +100 = 100% heal min value for all class.
			// Boost heal formula = boost heal value / 10 = additional % heal value.
			//each player start with boost_heal 100, therefore -100
			float boostHeal = 0;
			float healRate = 1.0f;
			if (healtype == HealType.HP && effect.getEffector() instanceof Player)
			{
				boostHeal = ((float)(effect.getEffector().getGameStats().getCurrentStat(StatEnum.BOOST_HEAL)-100) / 1000f);
				healRate = effect.getEffector().getController().getHealRate();
			}
			
			possibleHealValue = (int)(valueWithDelta * (float)(healRate + boostHeal));
		}
		
		switch (healtype)
		{
			case MP:
				effect.setmotValue(possibleHealValue);
				break;
			default:
				effect.sethotValue(possibleHealValue);
				break;
		}
		
		super.calculate(effect);
	}

	public void onPeriodicAction(Effect effect, HealType healtype)
	{
		Creature effected = effect.getEffected();
		
		int currentValue = getCurrentStatValue(effect);
		int maxCurValue = getMaxCurStatValue(effect);
		int possibleHealValue = 0;
		switch (healtype)
		{
			case MP:
				possibleHealValue = effect.getmotValue();
				break;
			default:
				possibleHealValue = effect.gethotValue();
				break;
		}
		
		int healValue = maxCurValue - currentValue < possibleHealValue ? (maxCurValue - currentValue) : possibleHealValue;
		
		if (healtype == HealType.HP && effect.getEffected().getEffectController().isAbnormalSet(EffectId.DISEASE))
			return;
		
		if (healValue == 0)
			return;
		
		switch (healtype)
		{
			case FP:
				effected.getLifeStats().increaseFp(TYPE.FP, healValue);
				break;
			case HP:
				effected.getLifeStats().increaseHp(TYPE.HP, healValue, effect.getSkillId(), 3);
				break;
			case MP:
				effected.getLifeStats().increaseMp(TYPE.MP, healValue, effect.getSkillId(), 4);
				break;
			case DP:
				((Player) effect.getEffected()).getCommonData().addDp(healValue);
				break;
		}
	}

	protected abstract int getCurrentStatValue(Effect effect);
	protected abstract int getMaxCurStatValue(Effect effect);

}
