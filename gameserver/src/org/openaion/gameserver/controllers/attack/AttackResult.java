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
package org.openaion.gameserver.controllers.attack;

import org.openaion.gameserver.skill.action.DamageType;

/**
 * @author ATracer
 *
 */
public class AttackResult
{
	private int damage;
	
	private AttackStatus attackStatus;
	
	private int shieldType;
	
	private int reflectedDamage;
	
	private int skillId;
	
	private DamageType damageType;
	
	public AttackResult(int damage, AttackStatus attackStatus)
	{
		this(damage, attackStatus, 0, 0, DamageType.PHYSICAL);
	}
	public AttackResult(int damage, AttackStatus attackStatus, int reflectedDamage, int skillId)
	{
		this(damage, attackStatus, reflectedDamage, skillId, DamageType.PHYSICAL);
	}
	public AttackResult(int damage, AttackStatus attackStatus, int reflectedDamage, int skillId, DamageType damageType)
	{
		this.damage = damage;
		this.attackStatus = attackStatus;
		this.reflectedDamage = reflectedDamage;
		this.skillId = skillId;
		this.damageType = damageType;
	}

	/**
	 * @return the damage
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	/**
	 * @return the attackStatus
	 */
	public AttackStatus getAttackStatus()
	{
		return attackStatus;
	}

	/**
	 * @return the shieldType
	 */
	public int getShieldType()
	{
		return shieldType;
	}

	/**
	 * @param shieldType the shieldType to set
	 */
	public void setShieldType(int shieldType)
	{
		this.shieldType = shieldType;
	}
	
	public int getReflectedDamage()
	{
		return this.reflectedDamage;
	}
	public void setReflectedDamage(int reflectedDamage)
	{
		this.reflectedDamage = reflectedDamage;
	}
	public int getSkillId()
	{
		return this.skillId;
	}
	public void setSkillId(int skillId)
	{
		this.skillId = skillId;
	}
	public DamageType getDamageType()
	{
		return this.damageType;
	}
	
}
