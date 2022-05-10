package org.openaion.gameserver.skill.effect;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.skill.model.DispelType;
import org.openaion.gameserver.skill.model.Effect;


/**
 * @author kecimis
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EvadeEffect")
public class EvadeEffect extends EffectTemplate
{
	//TODO effectids?
	@XmlElement
	protected List<String> effecttype;
	@XmlAttribute
	protected DispelType	dispeltype;

	@Override
	public void applyEffect(Effect effect)
	{
		if(effect.getEffected() == null || effect.getEffected().getEffectController() == null)
			return;
		
		if(dispeltype == null)
			return;
		
		if(dispeltype == DispelType.EFFECTTYPE && effecttype == null)
			return;

		switch(dispeltype)
		{
			case EFFECTTYPE:
				if (effecttype == null)
					return;
				
				for(String type : effecttype)
				{
					EffectId abnormalType = EffectId.getEffectIdByName(type);
					if(abnormalType != null && effect.getEffected().getEffectController().isAbnormalSet(abnormalType))
					{
						for (Effect ef : effect.getEffected().getEffectController().getAbnormalEffects())
						{
							if ((ef.getAbnormals() & abnormalType.getEffectId()) == abnormalType.getEffectId())
								ef.endEffect();
						}
					}
				}
				break;
		}
	}
}
