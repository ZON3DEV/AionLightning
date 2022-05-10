package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.model.Effect;


/**
 * @author kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DualMasteryEffect")
public class DualMasteryEffect extends BufEffect
{

	@XmlAttribute
	protected int value;
	
	@Override
	public void calculate(Effect effect)
	{
		
		//right now only players are affected
		Player player = (Player)  effect.getEffector();
		//check best mastery skill
		Integer skillId = player.getSkillList().getDualMasterySkill();
		if(skillId != null && skillId != effect.getSkillId())
			return;
		//check weather already skill applied and weapons isEquipeed
		boolean dualMasterySet = player.getEffectController().isDualMasterySet(skillId);
		boolean weaponsEquiped = player.getEquipment().isDualWieldEquipped();
		if(!dualMasterySet && weaponsEquiped)
			super.calculate(effect);
	}

	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		Player player = (Player)  effect.getEffector();
		player.getEffectController().unsetDualMastery();
		player.getEffectController().unsetDualEffect();
		
	}

	@Override
	public void startEffect(Effect effect)
	{
		Player player = (Player)  effect.getEffector();
		player.getEffectController().removePassiveEffect(player.getEffectController().getDualMastery());
		super.startEffect(effect);
		player.getEffectController().setDualMastery(effect.getSkillId());
		player.getEffectController().setDualEffect(value);
	}
	
	
}
