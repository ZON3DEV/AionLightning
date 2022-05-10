package org.openaion.gameserver.skill.effect;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.skill.model.Effect;


/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvulnerableWingEffect")
public class InvulnerableWingEffect extends EffectTemplate
{
	@Override
	public void applyEffect(final Effect effect)
	{
		//NOTE look at LifeStatsRestoreService.java line 169
		effect.addToEffectedController();
	}
}

