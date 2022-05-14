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
package com.aionemu.gameserver.model.stats.calc;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.stats.container.StatEnum;

public abstract class Stat2 {

	float bonusRate = 1.0F;
	int base;
	int bonus;
	private final Creature owner;
	protected final StatEnum stat;

	public Stat2(StatEnum stat, int base, Creature owner) {
		this(stat, base, owner, 1.0F);
	}

	public Stat2(StatEnum stat, int base, Creature owner, float bonusRate) {
		this.stat = stat;
		this.base = base;
		this.owner = owner;
		this.bonusRate = bonusRate;
	}

	public final StatEnum getStat() {
		return this.stat;
	}

	public final int getBase() {
		return this.base;
	}

	public final void setBase(int base) {
		this.base = base;
	}

	public final int getBonus() {
		return this.bonus;
	}

	public final int getCurrent() {
		return this.base + this.bonus;
	}

	public final void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public final float getBonusRate() {
		return this.bonusRate;
	}

	public final void setBonusRate(float bonusRate) {
		this.bonusRate = bonusRate;
	}

	public final Creature getOwner() {
		return this.owner;
	}

	@Override
	public String toString() {
		return "[" + stat.name() + " base=" + base + ", bonus=" + bonus + "]";
	}

	public abstract void addToBase(int paramInt);

	public abstract void addToBonus(int paramInt);

	public abstract float calculatePercent(int paramInt);
}
