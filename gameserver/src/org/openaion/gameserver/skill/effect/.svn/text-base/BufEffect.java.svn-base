/*
 * This file is part of aion-unique <aion-unique.com>.
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
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;
import org.openaion.gameserver.controllers.movement.ActionObserver;
import org.openaion.gameserver.controllers.movement.ActionObserver.ObserverType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.stats.CreatureGameStats;
import org.openaion.gameserver.model.gameobjects.stats.id.SkillEffectId;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.AddModifier;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.RateModifier;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.SetModifier;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.StatModifier;
import org.openaion.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import org.openaion.gameserver.skill.change.Change;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.utils.PacketSendUtility;



/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BufEffect")
public abstract class BufEffect extends EffectTemplate
{
	private static final Logger log = Logger.getLogger(BufEffect.class);
	
	@Override
	public void applyEffect(final Effect effect)
	{
		if (isOnFly())
		{
			ActionObserver observer = new ActionObserver(ObserverType.STATECHANGE) {
				@Override
				public void stateChanged (CreatureState state, boolean isSet)
				{
					if (state == CreatureState.FLYING)
					{
						if (isSet)
						{
							if (!effect.getEffected().getEffectController().hasAbnormalEffect(effect.getSkillId()))
								effect.addToEffectedController();
						}
						else if (!effect.getEffected().isInState(CreatureState.FLYING))
						{
							effect.endEffect();
						}
					}
				}
			};
			
			effect.getEffected().getObserveController().addObserver(observer);
			
			//add observer only for non-passives
			if (!effect.getSkillTemplate().isPassive())
				effect.setActionObserver(observer, position);
		} else {
			effect.addToEffectedController();
		}
	}

	/**
	 * Will be called from effect controller when effect ends
	 */
	@Override
	public void endEffect(Effect effect)
	{
		Creature effected = effect.getEffected();
		int skillId = effect.getSkillId();
		effected.getGameStats().endEffect(SkillEffectId.getInstance(skillId, effectid, position));
		
		//remove observer for non passive skill
		if (!effect.getSkillTemplate().isPassive())
		{
			ActionObserver observer = effect.getActionObserver(position);
			if (observer != null)
				effect.getEffected().getObserveController().removeObserver(observer);
		}
	}
	/**
	 * Will be called from effect controller when effect starts
	 */
	@Override
	public void startEffect(Effect effect)
	{
		if(change == null)
			return;
	
		Creature effected = effect.getEffected();		
		CreatureGameStats<? extends Creature> cgs = effected.getGameStats();

		TreeSet<StatModifier> modifiers = getModifiers(effect);
		SkillEffectId skillEffectId = getSkillEffectId(effect);
		
		if (modifiers.size()>0)
		{
			cgs.addModifiers(skillEffectId, modifiers);
			
			if(effect.getEffected() instanceof Player)
			{
				Player player = (Player)effect.getEffected();
				PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			}
			else if(effect.getEffected() instanceof Summon)
			{
				Summon s = (Summon)effect.getEffected();
				PacketSendUtility.sendPacket(s.getMaster(), new SM_SUMMON_UPDATE(s));
			}
		}
	}

	/**
	 * 
	 * @param effect
	 * @return
	 */
	protected SkillEffectId getSkillEffectId(Effect effect)
	{
		int skillId = effect.getSkillId();
		return SkillEffectId.getInstance(skillId, effectid, position);
	}
	
	/**
	 * 
	 * @param effect
	 * @return
	 */
	protected TreeSet<StatModifier> getModifiers(Effect effect)
	{
		int skillId = effect.getSkillId();
		int skillLvl = effect.getSkillLevel();
		
		TreeSet<StatModifier> modifiers = new TreeSet<StatModifier> ();
		
		for(Change changeItem : change)
		{
			if(changeItem.getStat() == null)
			{
				log.warn("Skill stat has wrong name for skillid: " + skillId);
				continue;
			}

			int valueWithDelta = changeItem.getValue() + changeItem.getDelta() * skillLvl;

			switch(changeItem.getFunc())
			{
				case ADD:
					modifiers.add(AddModifier.newInstance(changeItem.getStat(),valueWithDelta,true));
					break;
				case PERCENT:
					modifiers.add(RateModifier.newInstance(changeItem.getStat(),valueWithDelta,true));
					break;
				case REPLACE:
					modifiers.add(SetModifier.newInstance(changeItem.getStat(),valueWithDelta, true));
					break;
			}
		}
		return modifiers;
	}

	@Override
	public void onPeriodicAction(Effect effect)
	{
		// TODO Auto-generated method stub
		
	}
}
