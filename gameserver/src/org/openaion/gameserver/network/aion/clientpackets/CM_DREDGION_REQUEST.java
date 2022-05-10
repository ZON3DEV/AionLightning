
package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_DREDGION_INSTANCE;
import org.openaion.gameserver.services.DredgionInstanceService;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author ginho1
 *
 */
public class CM_DREDGION_REQUEST extends AionClientPacket
{

	private byte type;
	private int state;
	
	public CM_DREDGION_REQUEST(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{
		type = (byte) readD(); // 1 for dredgion regular - 2 for chantra
		state = readH(); // 68 00 when clicking on little icon, 64 00 when registrating as individual(private) 64 01 (quick) 64 02 (group) and opened wait window, 65 when canceling registration.
		
	}

	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();

		if(player != null)
		{
			switch(state)
			{
				//private entry
				case 100:
					if(DredgionInstanceService.getInstance().privateEntry(player))
					{
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 1, 1, 21248));
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 6, 0, 0));
					}
				break;
				//cancel
				case 101:
					DredgionInstanceService.getInstance().cancelGroup(player.getPlayerGroup());
					PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 6, 0, 0));
				break;
				//open request entry window
				case 104:
					PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 0, 0, 0));
				break;
				//quick entry
				case 356:
					if(DredgionInstanceService.getInstance().quickEntry(player))
					{
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 1, 1, 21248));
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 6, 0, 0));
					}
				break;
				//group entry
				case 612:
					if(DredgionInstanceService.getInstance().groupEntry(player))
					{

						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 1, 1, 21248));
						PacketSendUtility.sendPacket(player, new SM_DREDGION_INSTANCE(type, 6, 0, 0));
					}
				break;

			}
		}
	}	
}