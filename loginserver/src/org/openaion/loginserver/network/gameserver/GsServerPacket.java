package org.openaion.loginserver.network.gameserver;

import java.nio.ByteBuffer;

import org.openaion.commons.network.packet.BaseServerPacket;


/**
 * Base class for every LS -> GameServer Server Packet.
 * 
 * @author -Nemesiss-
 */
public abstract class GsServerPacket extends BaseServerPacket
{
	/**
	 * Constructs a new server packet with specified id.
	 *
	 * @param opcode packet opcode.
	 */
	protected GsServerPacket(int opcode)
	{
		super(opcode);
	}

	/**
	 * Write this packet data for given connection, to given buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	public final void write(GsConnection con, ByteBuffer buf)
	{
		buf.putShort((short) 0);
		writeImpl(con, buf);
		buf.flip();
		buf.putShort((short) buf.limit());
		buf.position(0);
	}

	/**
	 * Write data that this packet represents to given byte buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	protected abstract void writeImpl(GsConnection con, ByteBuffer buf);
}
