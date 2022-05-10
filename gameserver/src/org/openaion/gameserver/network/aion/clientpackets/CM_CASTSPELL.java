package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.siege.FortressGeneral;
import org.openaion.gameserver.network.aion.AionClientPacket;

/**
 * @author alexa026
 * @author rhys2002
 */
public class CM_CASTSPELL extends AionClientPacket
{
	private int					spellid;
	private int					targetType; // 0 - obj id, 1 - point location
	private float					x, y, z;
	
	private int					targetObjectId;

	private int					time;
	private int					level;
	
	
	/**
	 * Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
	 * @param opcode
	 */
	public CM_CASTSPELL(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		spellid = readH();
		level = readC();
		
		targetType = readC();
		
		switch(targetType)
		{
			case 0:
				targetObjectId = readD();
				break;
			case 1:
				x = readF();
				y = readF();
				z = readF();
				break;
			default:
				break;
		}
		
		time = readH();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		
		if (player == null)
			return;
		
		if(player.isProtectionActive())
		{
			player.getController().stopProtectionActiveTask();
		}
		
		if(player.getTarget() instanceof FortressGeneral)
		{
			if(player.getCommonData().getRace() == ((FortressGeneral)player.getTarget()).getObjectTemplate().getRace())
				return;
		}
		
		if(!player.getLifeStats().isAlreadyDead())
		{
			if(player.isCasting() && spellid == 0 && level == 0)
				player.getController().cancelCurrentSkill();
			
			if(!player.getSkillList().isSkillPresent(spellid))
				return;
			
			if(player.getController().checkSkillPacket(spellid, time, targetObjectId))
				player.getController().useSkill(spellid, targetType, x, y, z, time);
		}
		
	}
}
