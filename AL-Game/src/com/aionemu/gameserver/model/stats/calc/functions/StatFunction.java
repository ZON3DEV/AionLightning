/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.stats.calc.functions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.skillengine.condition.Conditions;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleModifier")
public class StatFunction implements IStatFunction {

	@XmlAttribute(name = "name")
	protected StatEnum stat;
	@XmlAttribute
	private boolean bonus;
	@XmlAttribute
	protected int value;
	@XmlElement(name = "conditions")
	private Conditions conditions;
	private int rndNumber;

	public StatFunction(StatEnum stat, int value, boolean bonus) {
		this.stat = stat;
		this.value = value;
		this.bonus = bonus;
	}

	@Override
	public int compareTo(IStatFunction o) {
		int result = getPriority() - o.getPriority();
		if (result == 0) {
			return hashCode() - o.hashCode();
		}
		return result;
	}

	@Override
	public StatOwner getOwner() {
		return null;
	}

	@Override
	public final StatEnum getName() {
		return this.stat;
	}

	@Override
	public final boolean isBonus() {
		return this.bonus;
	}

	@Override
	public int getPriority() {
		return 16;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public boolean validate(Stat2 stat, IStatFunction statFunction) {
		return this.conditions != null ? this.conditions.validate(stat, statFunction) : true;
	}

	@Override
	public String toString() {
		return "stat=" + stat + ", bonus=" + bonus + ", value=" + value + ", priority=" + getPriority();
	}

	public StatFunction withConditions(Conditions conditions) {
		this.conditions = conditions;
		return this;
	}

	@Override
	public boolean hasConditions() {
		return this.conditions != null;
	}

	public static List<StatFunction> mergeRandomBonuses(List<StatFunction> modifiers, List<StatFunction> rndBonuses) {
		if (modifiers == null) {
			modifiers = new ArrayList<StatFunction>();
		}
		if (rndBonuses == null) {
			return modifiers;
		}
		List<StatFunction> allModifiers = new ArrayList<StatFunction>();
		EnumSet<StatEnum> rndNames = EnumSet.noneOf(StatEnum.class);
		for (IStatFunction func : rndBonuses) {
			rndNames.add(func.getName());
		}
		for (StatFunction modifier : modifiers) {
			if ((!rndNames.contains(modifier.getName())) || (!modifier.isBonus()) || (modifier.hasConditions())) {
				allModifiers.add(modifier);
			} else {
				IStatFunction rndBonus = null;
				for (IStatFunction func : rndBonuses) {
					if (func.getName() == modifier.getName()) {
						rndBonus = func;
						rndNames.remove(func.getName());
						break;
					}
				}
				int finalValue = modifier.getValue() + (rndBonus == null ? 0 : rndBonus.getValue());
				if ((modifier instanceof StatAddFunction)) {
					if (finalValue != 0) {
						allModifiers.add(new StatAddFunction(modifier.getName(), finalValue, true));
					}
				} else if ((modifier instanceof StatRateFunction)) {
					if (finalValue != 0) {
						allModifiers.add(new StatRateFunction(modifier.getName(), finalValue, true));
					}
				} else {
					allModifiers.add(modifier);
				}
			}
		}
		for (StatFunction modifier : rndBonuses) {
			if (rndNames.contains(modifier.getName())) {
				allModifiers.add(modifier);
			}
		}
		return allModifiers;
	}

	public int getRandomNumber() {
		return rndNumber;
	}

	public void setRandomNumber(int rndNumber) {
		this.rndNumber = rndNumber;
	}

	public StatFunction() {
	}

	@Override
	public void apply(Stat2 stat) {
	}

}
