package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.network.aion.serverpackets.SM_TARGET_IMMOBILIZE;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BuffStunEffect")
public class BuffStunEffect extends EffectTemplate
{

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(Effect effect)
	{
		final Creature effected = effect.getEffected();
   		effected.getController().cancelCurrentSkill(); 
		effect.setAbnormal(EffectId.STUN.getEffectId());
		effect.getEffected().getEffectController().setAbnormal(EffectId.STUN.getEffectId());
		PacketSendUtility.broadcastPacketAndReceive(effect.getEffected(), new SM_TARGET_IMMOBILIZE(effect.getEffected()));
	}

	@Override
	public void endEffect(Effect effect)
	{
		effect.getEffected().getEffectController().unsetAbnormal(EffectId.STUN.getEffectId());
	}
}
