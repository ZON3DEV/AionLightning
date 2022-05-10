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

import org.openaion.gameserver.controllers.attack.AttackUtil;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Effect;


/**
 * @author ATracer
 * @edit kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignetBurstEffect")
public class SignetBurstEffect extends DamageEffect
{
	@XmlAttribute
    protected int signetlvl;
	@XmlAttribute
	protected String signet;

	@Override
	public void calculate(Effect effect)
	{
		Creature effected = effect.getEffected();
		Effect signetEffect = null;
		for (int i=1;i <= 5;i++)
		{
			if (effected.getEffectController().getAbnormalEffect(signet+"_"+i) != null)
			{
				signetEffect = effected.getEffectController().getAbnormalEffect(signet+"_"+i);
				break;
			}
		}
		
		if(signetEffect == null)
			return;
		
		int level = signetEffect.getSkillLevel();
		int valueWithDelta = value + delta * effect.getSkillLevel();
		
		//custom bonuses for magical accurancy according to rune level and effector level
		int accmod = 0;
		switch (level)
		{
			case 1: 
				accmod = 18 * effect.getEffector().getLevel();//-990 on 55lvl
				break;
			case 2:
				accmod = 4 * effect.getEffector().getLevel();//-220 on 55 lvl
				break;
			case 3:
				accmod = 4 * effect.getEffector().getLevel();//+220 on 55 lvl
				break;
			case 4:
				accmod = 9 * effect.getEffector().getLevel();//+495 on 55lvl
				break;
			case 5:
				accmod = 18 * effect.getEffector().getLevel();//+990 on 55lvl
				break;
		}
		effect.setAccModBoost(accmod);
		
		//calculate damage
		AttackUtil.calculateMagicalSkillAttackResult(effect, (valueWithDelta * level/5), getElement(), applyActionModifiers(effect), false);

		effect.setSignetBursted(level);
		signetEffect.endEffect();

		super.calculate(effect, DamageType.MAGICAL, false);

	}
}
