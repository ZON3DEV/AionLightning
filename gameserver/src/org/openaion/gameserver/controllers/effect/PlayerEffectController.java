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
package org.openaion.gameserver.controllers.effect;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.alliance.PlayerAllianceEvent;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.group.GroupEvent;
import org.openaion.gameserver.network.aion.serverpackets.SM_ABNORMAL_STATE;
import org.openaion.gameserver.services.AllianceService;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.SkillTemplate;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author ATracer
 *
 */
public class PlayerEffectController extends EffectController
{
	/**
	 * Logger for this class.
	 */
	private static final Logger log = Logger.getLogger(PlayerEffectController.class);

	/**
	 * weapon mastery
	 */
	private int weaponEffects;
	
	/**
	 * armor mastery
	 */
	private int armorEffects;
	
	/**
	 * dual weapon mastery
	 */
	private int dualEffects;
	/**
	 * holds the value of DualMasteryEffect
	 */
	private int dualEffect;
	
	/**
	 * shield mastery
	 */
	private int shieldEffects;
	
	/**
	 * current food effect
	 */
	private Effect foodEffect;

	public PlayerEffectController(Creature owner)
	{
		super(owner);
	}
	
	@Override
	public void addEffect(Effect effect)
	{
		if(effect.isFood())
			addFoodEffect(effect);
		
		super.addEffect(effect);
		updatePlayerIconsAndGroup(effect);
	}
	
	@Override
	public void clearEffect(Effect effect)
	{
		if(effect.isFood())
			foodEffect = null;
	
		super.clearEffect(effect);
		updatePlayerIconsAndGroup(effect);
	}

	@Override
	public Player getOwner()
	{
		return (Player) super.getOwner();
	}

	/**
	 * @param effect
	 */
	private void updatePlayerIconsAndGroup(Effect effect)
	{
		if(!effect.isPassive())
		{
			updatePlayerEffectIcons();		
			if(getOwner().isInGroup())
				getOwner().getPlayerGroup().updateGroupUIToEvent(getOwner(), GroupEvent.UPDATE);
			if(getOwner().isInAlliance())
				AllianceService.getInstance().updateAllianceUIToEvent(getOwner(), PlayerAllianceEvent.UPDATE);
		}
	}
	
	/**
	 * @param effect
	 */
	private void addFoodEffect(Effect effect)
	{
		if(foodEffect != null)
			foodEffect.endEffect();
		foodEffect = effect;
	}

	/**
	 * Weapon mastery
	 */
	public void setWeaponMastery(int skillId)
	{
		weaponEffects = skillId;
	}

	public void unsetWeaponMastery()
	{
		weaponEffects = 0;
	}

	public int getWeaponMastery()
	{
		return weaponEffects;
	}
	
	public boolean isWeaponMasterySet(int skillId)
	{
		return weaponEffects == skillId;
	}
	
	/**
	 * Armor mastery
	 */
	public void setArmorMastery(int skillId)
	{
		armorEffects = skillId;
	}

	public void unsetArmorMastery()
	{
		armorEffects = 0;
	}

	public int getArmorMastery()
	{
		return armorEffects;
	}
	
	public boolean isArmorMasterySet(int skillId)
	{
		return armorEffects == skillId;
	}
	
	/**
	 * Dual Weapon mastery
	 */
	public boolean isDualMasterySet(int skillId)
	{
		return dualEffects == skillId;
	}
	
	public void setDualMastery(int skillId)
	{
		dualEffects = skillId;
	}

	public void unsetDualMastery()
	{
		dualEffects = 0;
	}

	public int getDualMastery()
	{
		return dualEffects;
	}
	
	/**
	 * Set dualEffect, used in calculation of offhand damage
	 * @param dualEffect
	 */
	
	public void setDualEffect(int dualEffect)
	{
		this.dualEffect = dualEffect;
	}

	public void unsetDualEffect()
	{
		dualEffect = 0;
	}

	public int getDualEffect()
	{
		return dualEffect;
	}
	
	/**
	 * Shield mastery
	 */
	public void setShieldMastery(int skillId)
	{
		shieldEffects = skillId;
	}

	public void unsetShieldMastery()
	{
		shieldEffects = 0;
	}

	public int getShieldMastery()
	{
		return shieldEffects;
	}
	
	public boolean isShieldMasterySet(int skillId)
	{
		return shieldEffects == skillId;
	}

	/**
	 * @param skillId
	 * @param skillLvl
	 * @param currentTime
	 * @param reuseDelay
	 */
	public void addSavedEffect(int skillId, int skillLvl, int remainingTime)
	{
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		
		if(template == null)
		{
			log.warn("The skill template: " + skillId + " was not found.");
			return;
		}
		
		if(remainingTime <= 0)
			return;
		
		Effect effect = new Effect(getOwner(), getOwner(), template, skillLvl, remainingTime);
		if(effect.isFood())
			addFoodEffect(effect);
		abnormalEffectMap.put(effect.getStack(), effect);
		effect.addAllEffectToSucess();
		effect.startEffect(true);
		
		PacketSendUtility.sendPacket(getOwner(),
			new SM_ABNORMAL_STATE(Collections.singletonList(effect), abnormals));
		
	}
	
}
