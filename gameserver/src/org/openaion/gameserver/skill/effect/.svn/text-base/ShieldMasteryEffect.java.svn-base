package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.model.Effect;


/**
 * @author kecimis
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShieldMasteryEffect")
public class ShieldMasteryEffect extends BufEffect
{
	@Override
	public void calculate(Effect effect)
	{
		// right now only players are affected
		Player player = (Player) effect.getEffector();
		// check best mastery skill
		Integer skillId = player.getSkillList().getShieldMasterySkill();
		if(skillId != null && skillId != effect.getSkillId())
			return;
		// check weather already skill applied and weapon isEquipeed
		boolean shieldMasterySet = player.getEffectController().isShieldMasterySet(skillId);
		boolean isShieldEquiped = player.getEquipment().isShieldEquipped();
		if(!shieldMasterySet && isShieldEquiped)
			super.calculate(effect);
	}

	@Override
	public void startEffect(Effect effect)
	{
		super.startEffect(effect);
		Player player = (Player) effect.getEffector();
		player.getEffectController().removePassiveEffect(player.getEffectController().getShieldMastery());
		player.getEffectController().setShieldMastery(effect.getSkillId());
	}

	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		Player player = (Player) effect.getEffector();
		player.getEffectController().unsetShieldMastery();
	}
}