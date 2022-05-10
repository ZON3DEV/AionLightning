package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Sylar
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XPBoostEffect")
public class XPBoostEffect extends EffectTemplate
{
	@XmlAttribute
	protected int percent;
	
	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}
	@Override
	public void calculate(Effect effect)
	{
		effect.addSucessEffect(this);
	}
	@Override
	public void startEffect(Effect effect)
	{
		Logger.getLogger(XPBoostEffect.class).info("Starting " + percent + "% XP Boost");
		if(!(effect.getEffected() instanceof Player))
		{
			Logger.getLogger(XPBoostEffect.class).error("Effected creature of XPBoostEffect is not a player ! Aborting.");
			return;
		}
		Player player = (Player)effect.getEffected();
		player.setXpBoost(percent);
	}

	@Override
	public void endEffect(Effect effect)
	{
		Logger.getLogger(XPBoostEffect.class).info("Ending XP Boost");
		if(!(effect.getEffected() instanceof Player))
		{
			Logger.getLogger(XPBoostEffect.class).error("Effected creature of XPBoostEffect is not a player ! Aborting.");
			return;
		}
		Player player = (Player)effect.getEffected();
		player.setXpBoost(0);
		// STR_MSG_DELETE_CASH_XPBOOST_BY_TIMEOUT
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1390246));
	}
}
