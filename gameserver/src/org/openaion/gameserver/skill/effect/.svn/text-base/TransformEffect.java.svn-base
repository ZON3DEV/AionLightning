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

import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.TransformType;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Sweetkr
 * @reworked kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformEffect")
public abstract class TransformEffect extends EffectTemplate
{
	@XmlAttribute(required = true)
	protected int model;
	
	@XmlAttribute
	protected TransformType type = TransformType.NONE;
	
	
	@Override
	public void applyEffect(Effect effect)
	{
		final Creature effected = effect.getEffected();
		boolean transformed = false;

		if(effected instanceof Player)
		{
			transformed = effected.getTransformedModelId() != 0;
		}
		else if(effected instanceof Creature)
		{
			transformed = effected.getTransformedModelId() != effected.getObjectTemplate().getTemplateId();
		}
		boolean apply = true;
		if (transformed)
		{
			for ( Effect tmp : effected.getEffectController().getAbnormalEffects())
			{
				if (effect.getSkillId() == tmp.getSkillId())
					continue;
				boolean abort = false;
				for (EffectTemplate template : tmp.getEffectTemplates())
				{
					if (template instanceof DeformEffect) 
					{
						if (this instanceof DeformEffect)
							abort = true;
						else
							apply = false;
					}
				}
				if (abort)
					tmp.endEffect();
			}
			
			for ( Effect tmp : effected.getEffectController().getAbnormalEffects())
			{
				if (effect.getSkillId() == tmp.getSkillId())
					continue;
				boolean abort = false;
				for (EffectTemplate template : tmp.getEffectTemplates())
				{
					if ((template instanceof PolymorphEffect || template instanceof ShapeChangeEffect)
						&& (this instanceof PolymorphEffect || this instanceof ShapeChangeEffect))
					{
						if (template.getBasicLvl() <= this.basicLvl)
							abort = true;

						else
							apply = false;

					}
				}
				if (abort)
				{
					tmp.endEffect();
					if (tmp.isAvatar())
						effected.getEffectController().removeEffect(tmp.getLaunchSkillId());
				}
			}
		}
		if (apply)
			effect.addToEffectedController();
		else
		{
			effect.setForbidAdding(true);
			effected.getEffectController().clearEffect(effect);
			effect.setAddedToController(false);
		}
	}

	public void endEffect(Effect effect, EffectId effectId)
	{
		final Creature effected = effect.getEffected();
		
		if (effectId != null)
		{
			effected.getEffectController().unsetAbnormal(effectId.getEffectId());
		}

		if(effected instanceof Player)
		{
			int newModel = 0;
			for ( Effect tmp : effected.getEffectController().getAbnormalEffects())
			{
				for (EffectTemplate template : tmp.getEffectTemplates())
				{
					if (template instanceof TransformEffect)
					{
						if (((TransformEffect)template).getTransformId() == model)
							continue;
						newModel = ((TransformEffect)template).getTransformId();
						break;
					}
				}
			}
			effected.setTransformedModelId(newModel);
		}
		else if(effected instanceof Creature)
		{
			effected.setTransformedModelId(effected.getObjectTemplate().getTemplateId());
		}
		PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TRANSFORM(effected));
	}
	
	public void startEffect(final Effect effect, EffectId effectId)
	{
		final Creature effected = effect.getEffected();

		if (effectId != null)
		{	
			effect.setAbnormal(effectId.getEffectId());
			effected.getEffectController().setAbnormal(effectId.getEffectId());
		}
		effected.setTransformedModelId(model);
		PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TRANSFORM(effected));
	}
	
	public TransformType getTransformType()
	{
		return type;
	}
	public int getTransformId()
	{
		return model;
	}

}
