package org.openaion.loginserver.network.aion;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.openaion.commons.network.packet.BaseClientPacket;
import org.openaion.loginserver.model.Account;


/**
 * Base class for every Aion -> LS Client Packet
 * 
 * @author -Nemesiss-
 */
public abstract class AionClientPacket extends BaseClientPacket<AionConnection>
{
	/**
	 * Logger for this class.
	 */
	private static final Logger		log	= Logger.getLogger(AionClientPacket.class);

	/**
	 * Constructs new client packet.
	 * 
	 * @param buf       packet data
	 * @param client    client
	 * @param opcode    packet id
	 */
	protected AionClientPacket(ByteBuffer buf, AionConnection client, int opcode)
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
			String name;
			Account account = getConnection().getAccount();
			if (account != null)
			{
				name = account.getName();
			}
			else
			{
				name = getConnection().getIP();
			}

			log.error("error handling client (" + name + ") message " + this, e);
		}
	}

	/**
	 * Send new AionServerPacket to connection that is owner of this packet. This method is equvalent to:
	 * getConnection().sendPacket(msg);
	 * 
	 * @param msg
	 */
	protected void sendPacket(AionServerPacket msg)
	{
		getConnection().sendPacket(msg);
	}
}
