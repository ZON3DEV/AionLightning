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

import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author kecimis
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HpUseOverTimeEffect")
public class HpUseOverTimeEffect extends AbstractOverTimeEffect
{
	@XmlAttribute
	private int cost_start;
	@XmlAttribute
	private int cost_end;
	
	@Override
	public void startEffect(final Effect effect)
	{
		Creature effected = effect.getEffected();
		int hpUsed = 0;
		if (percent)
		{
			int maxHp = effected.getGameStats().getCurrentStat(StatEnum.MAXHP);
			hpUsed = maxHp * value/100;
		}
		else
			hpUsed = value;
		
		final int requiredHp = hpUsed;
		int newtime = 0;
		if (checktime == 0)
			newtime = 3000;
		else
			newtime = checktime;
		
		if (cost_start != 0)
		{
			effected.getLifeStats().reduceHp(cost_start,effected);
		}
		
		Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				onPeriodicAction(effect, requiredHp);
			}
		}, 0, newtime);
		effect.setMpUseTask(task);
	}

	public void onPeriodicAction(Effect effect, int value)
	{
		Creature effected = effect.getEffected();
		if(effected.getLifeStats().getCurrentHp() < value)
			effect.endEffect();

		effected.getLifeStats().reduceHp(value, effect.getEffected());
	}

	@Override
	public void calculate(Effect effect)
	{
		Creature effected = effect.getEffected();
		int hpUsed = 0;
		if (percent)
		{
			int maxHp = effected.getGameStats().getCurrentStat(StatEnum.MAXHP);
			hpUsed = maxHp * value/100;
		}
		else
			hpUsed = value;
		
		if(effected.getLifeStats().getCurrentHp() < hpUsed)
			return;

		super.calculate(effect);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		if (cost_end != 0)
		{
			effect.getEffected().getLifeStats().reduceHp(cost_end,effect.getEffected());
		}
		super.endEffect(effect);
	}

}
