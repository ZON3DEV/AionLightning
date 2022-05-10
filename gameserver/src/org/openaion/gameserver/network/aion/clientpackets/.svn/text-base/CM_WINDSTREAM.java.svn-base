package org.openaion.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_WINDSTREAM;
import org.openaion.gameserver.utils.PacketSendUtility;

//import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;

/**
 * Packet concerning windstreams.
 * 
 * @author Dns, LokiReborn
 * 
 */
public class CM_WINDSTREAM extends AionClientPacket
{
	int teleportId;
	int distance;
	int state;

	private static final Logger	log	= Logger.getLogger(CM_WINDSTREAM.class);

	/**
	 * @param opcode
	 */
	public CM_WINDSTREAM(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		teleportId = readD(); //typical teleport id (ex : 94001 for talloc hallow in inggison)
		distance = readD();	 // 600 for talloc.
		state = readD(); // 0 or 1.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if(player == null)
			return;
		
		switch(state)
		{
		case 0:
		case 4:
		case 8:
			//TODO:	Find in which cases second variable is 0 & 1
			//		Jego's example packets had server refuse with 0 and client kept retrying.
			PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(state,1));
			break;
		case 1:
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM, teleportId, distance), true);
			player.setEnterWindstream(1);
			break;
		case 2:
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM_END, 0, 0), true);			
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(state,1));
			log.info("Player entered Windstream with telID: " + teleportId + ", distance: " + distance + " and state: " + state);
			break;
		case 7:
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM_BOOST, 0, 0), true);
			player.setEnterWindstream(7);			
			break;
		default:
			log.error("Unknown Windstream state #" + state + " was found!" );
		}
	}
}
