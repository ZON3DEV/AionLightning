package org.openaion.loginserver.network.gameserver;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.openaion.commons.network.packet.BaseClientPacket;


/**
 * Base class for every GameServer -> LS Client Packet
 * 
 * @author -Nemesiss-
 */
public abstract class GsClientPacket extends BaseClientPacket<GsConnection>
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(GsClientPacket.class);

	/**
	 * Creates new packet instance. 
	 * 
	 * @param buf packet data
	 * @param client client
	 * @param opcode packet id
	 */
	protected GsClientPacket(ByteBuffer buf, GsConnection client, int opcode)
	{
		super(buf, opcode);
		setConnection(client);
	}

	/**
	 * run runImpl catching and logging Throwable.
	 */
	@Override
	public final void run()
	{
		try
		{
			runImpl();
		}
		catch (Throwable e)
		{
			log.warn("error handling gs (" + getConnection().getIP() + ") message " + this, e);
		}
	}

	/**
	 * Send new GsServerPacket to connection that is owner of this packet. This method is equivalent to:
	 * getConnection().sendPacket(msg);
	 * 
	 * @param msg
	 */
	protected void sendPacket(GsServerPacket msg)
	{
		getConnection().sendPacket(msg);
	}
}
