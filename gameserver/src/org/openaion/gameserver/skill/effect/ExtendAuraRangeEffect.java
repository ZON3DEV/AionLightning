package org.openaion.gameserver.skill.effect;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.controllers.movement.ActionObserver;
import org.openaion.gameserver.controllers.movement.ActionObserver.ObserverType;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.skill.model.Effect;


/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtendAuraRangeEffect")
public class ExtendAuraRangeEffect extends EffectTemplate
{
	@XmlAttribute
	protected int value;

	@Override
	public void applyEffect(final Effect effect)
	{
		//TODO implement basiclvl
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
		} else {
			effect.addToEffectedController();
		}
	}

	@Override
	public void endEffect(Effect effect)
	{
		Creature effected = effect.getEffected();
		effected.getController().setAuraRangeRate(effected.getController().getAuraRangeRate() - (float)value/100f);
		
	}

	@Override
	public void startEffect(Effect effect)
	{
		Creature effected = effect.getEffected();
		effected.getController().setAuraRangeRate(effected.getController().getAuraRangeRate() + (float)value/100f);
	}

}