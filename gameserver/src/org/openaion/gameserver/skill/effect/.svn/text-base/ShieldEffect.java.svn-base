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

import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.controllers.movement.AttackCalcObserver;
import org.openaion.gameserver.controllers.movement.AttackShieldObserver;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.id.SkillEffectId;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.StatModifier;
import org.openaion.gameserver.skill.model.AttackType;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.SkillTargetRace;


/**
 * @author ATracer
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShieldEffect")
public class ShieldEffect extends EffectTemplate
{

	@XmlAttribute
	protected int	delta;
	@XmlAttribute
	protected int	value;
	@XmlAttribute
	protected boolean	percent;
	@XmlAttribute
	protected int	hitdelta;
	@XmlAttribute
	protected int	hitvalue;
	@XmlAttribute
	protected AttackType	attacktype;
	@XmlAttribute
	protected int	probability;
	

	@XmlAttribute(name = "cond_race")
	protected SkillTargetRace	cond_race;

	@Override
	public void applyEffect(Effect effect)
	{
		if (cond_race == null || effect.getEffected() instanceof Npc && ((Npc)effect.getEffected()).getObjectTemplate().getRace().toString().equals(cond_race.toString()))
		{
			boolean apply = true;
			//player can have only one shield at a time, applies for reflector too
			if (effect.getEffected() instanceof Player)
			{
				boolean abort = false;
				for(Effect ef : effect.getEffected().getEffectController().getAbnormalEffects())
				{
					if (effect.getSkillId() == ef.getSkillId())
						continue;
					for(EffectTemplate et : ef.getEffectTemplates())
					{
						if (et.getClass() == this.getClass())
						{
							if (et.getBasicLvl() <= this.basicLvl)
								abort = true;
							else
								apply = false;
						}
					}
					if (abort)
					{
						ef.endEffect();
						break;
					}
				}
			}
			
			if (apply)
			{
				effect.addToEffectedController();
			}
			else
			{
				effect.setForbidAdding(true);
				effect.getEffected().getEffectController().clearEffect(effect);
				effect.setAddedToController(false);
			}
		}
	}

	@Override
	public void startEffect(Effect effect)
	{
		int skillLvl = effect.getSkillLevel();
		int valueWithDelta = value + delta * skillLvl;
		int hitValueWithDelta = hitvalue + hitdelta * skillLvl;

		AttackShieldObserver asObserver = new AttackShieldObserver(hitValueWithDelta,
			valueWithDelta, percent, effect, attacktype, probability, 2);
		
		effect.getEffected().getObserveController().addAttackCalcObserver(asObserver);
		effect.setAttackShieldObserver(asObserver, position);
		
		//UD effect for Stone Skin
		if (effect.getStack().contains("MA_STONESKIN"))
		{
			TreeSet<StatModifier> modifiers = getUDModifiers();
			SkillEffectId skillEffectId = SkillEffectId.getInstance(effect.getSkillId(), effectid, position);
			effect.getEffected().getGameStats().addModifiers(skillEffectId, modifiers);
		}
	}

	@Override
	public void endEffect(Effect effect)
	{
		if (effect.getStack().contains("MA_STONESKIN"))
			effect.getEffected().getGameStats().endEffect(SkillEffectId.getInstance(effect.getSkillId(), effectid, position));
		
		AttackCalcObserver acObserver = effect.getAttackShieldObserver(position);
		if (acObserver != null)
			effect.getEffected().getObserveController().removeAttackCalcObserver(acObserver);
	}
	
	/**
	 * adding ud resistances for Stone Skin
	 * need more info
	 * http://www.youtube.com/watch?v=yJzpK4Xoozo
	 * so far it looks like that Stone Skin should resist only Open Aerial
	 * @param effect
	 * @return
	 */
	private TreeSet<StatModifier> getUDModifiers()
	{
		TreeSet<StatModifier> modifiers = new TreeSet<StatModifier> ();
		
		//modifiers.add(AddModifier.newInstance(StatEnum.OPENAREIAL_RESISTANCE,1000,true));

		return modifiers;
	}
}
