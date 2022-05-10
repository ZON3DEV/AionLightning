package org.openaion.loginserver.network.gameserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.commons.network.AConnection;
import org.openaion.commons.network.Dispatcher;
import org.openaion.loginserver.GameServerInfo;
import org.openaion.loginserver.dao.GameServersDAO;
import org.openaion.loginserver.utils.ThreadPoolManager;


/**
 * Object representing connection between LoginServer and GameServer.
 * 
 * @author -Nemesiss-
 */
public class GsConnection extends AConnection
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(GsConnection.class);

	/**
	 * Possible states of GsConnection
	 */
	public static enum State
	{
		/**
		 * Means that GameServer just connect, but is not authenticated yet
		 */
		CONNECTED,
		/**
		 * GameServer is authenticated
		 */
		AUTHED
	}

	/**
	 * Server Packet "to send" Queue
	 */
	private final Deque<GsServerPacket>	sendMsgQueue	= new ArrayDeque<GsServerPacket>();

	/**
	 * Current state of this connection
	 */
	private State						state;

	/**
	 * GameServerInfo for this GsConnection.
	 */
	private GameServerInfo				gameServerInfo	= null;

	/**
	 * Constructor.
	 * 
	 * @param sc
	 * @param d
	 * @throws IOException
	 */
	public GsConnection(SocketChannel sc, Dispatcher d) throws IOException
	{
		super(sc, d);
		state = State.CONNECTED;

		String ip = getIP();
		log.debug("GS connection from: " + ip);
	}

	/**
	 * Called by Dispatcher. ByteBuffer data contains one packet that should be processed.
	 * 
	 * @param data
	 * @return True if data was processed correctly, False if some error occurred and connection should be closed NOW.
	 */
	@Override
	public boolean processData(ByteBuffer data)
	{
		GsClientPacket pck = GsPacketHandler.handle(data, this);
		log.info("recived packet: " + pck);

		if (pck != null && pck.read())
			ThreadPoolManager.getInstance().executeGsPacket(pck);

		return true;
	}

	/**
	 * This method will be called by Dispatcher, and will be repeated till return false.
	 * 
	 * @param data
	 * @return True if data was written to buffer, False indicating that there are not any more data to write.
	 */
	@Override
	protected boolean writeData(ByteBuffer data)
	{
		GsServerPacket packet = sendMsgQueue.pollFirst();
		if (packet == null)
			return false;

		packet.write(this, data);
		return true;
	}

	/**
	 * This method is called by Dispatcher when connection is ready to be closed.
	 * 
	 * @return time in ms after witch onDisconnect() method will be called. Always return 0.
	 */
	@Override
	protected final long getDisconnectionDelay()
	{
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onDisconnect()
	{
		log.info(this + " disconnected");
		if (gameServerInfo != null)
		{
			gameServerInfo.setGsConnection(null);
			gameServerInfo.clearAccountsOnGameServer();
			DAOManager.getDAO(GameServersDAO.class).writeGameServerStatus(gameServerInfo);
			gameServerInfo = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onServerClose()
	{
		// TODO mb some packet should be send to gameserver before closing?
		close(/* packet, */true);
	}

	/**
	 * Sends GsServerPacket to this client.
	 * 
	 * @param bp
	 *            GsServerPacket to be sent.
	 */
	public final void sendPacket(GsServerPacket bp)
	{
		/**
		 * Connection is already closed or waiting for last (close packet) to be sent
		 */
		if (isWriteDisabled())
			return;

		log.info("sending packet: " + bp);

		sendMsgQueue.addLast(bp);
		enableWriteInterest();
	}

	/**
	 * Its guaranted that closePacket will be sent before closing connection, but all past and future packets wont.
	 * Connection will be closed [by Dispatcher Thread], and onDisconnect() method will be called to clear all other
	 * things. forced means that server shouldn't wait with removing this connection.
	 * 
	 * @param closePacket
	 *            Packet that will be send before closing.
	 * @param forced
	 *            have no effect in this implementation.
	 */
	public final void close(GsServerPacket closePacket, boolean forced)
	{
		if (isWriteDisabled())
			return;

		log.info("sending packet: " + closePacket + " and closing connection after that.");

		pendingClose = true;
		isForcedClosing = forced;
		sendMsgQueue.clear();
		sendMsgQueue.addLast(closePacket);
		enableWriteInterest();
	}

	/**
	 * @return Current state of this connection.
	 */
	public State getState()
	{
		return state;
	}

	/**
	 * @param state
	 *            Set current state of this connection.
	 */
	public void setState(State state)
	{
		this.state = state;
	}

	/**
	 * @return GameServerInfo for this GsConnection or null if this GsConnection is not authenticated yet.
	 */
	public GameServerInfo getGameServerInfo()
	{
		return gameServerInfo;
	}

	/**
	 * @param gameServerInfo
	 *            Set GameServerInfo for this GsConnection.
	 */
	public void setGameServerInfo(GameServerInfo gameServerInfo)
	{
		this.gameServerInfo = gameServerInfo;
	}

	/**
	 * @return String info about this connection
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("GameServer [ID:");
		if (gameServerInfo != null)
		{
			sb.append(gameServerInfo.getId());
		}
		else
		{
			sb.append("null");
		}
		sb.append("] ").append(getIP());
		return sb.toString();
	}
}
