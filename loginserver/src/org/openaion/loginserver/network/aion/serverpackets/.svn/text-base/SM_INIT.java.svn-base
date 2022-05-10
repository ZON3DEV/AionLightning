package org.openaion.loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import javax.crypto.SecretKey;

import org.openaion.loginserver.network.aion.AionConnection;
import org.openaion.loginserver.network.aion.AionServerPacket;


/**
 * Format: dd b dddd s d: session id d: protocol revision b: 0x90 bytes : 0x80 bytes for the scrambled RSA public key
 * 0x10 bytes at 0x00 d: unknow d: unknow d: unknow d: unknow s: blowfish key
 */
public final class SM_INIT extends AionServerPacket
{
	/**
	 * Session Id of this connection
	 */
	private final int		sessionId;

	/**
	 * public Rsa key that client will use to encrypt login and password that will be send in RequestAuthLogin client
	 * packet.
	 */
	private final byte[]	publicRsaKey;
	/**
	 * blowfish key for packet encryption/decryption.
	 */
	private final byte[]	blowfishKey;

	/**
	 * Constructor
	 * 
	 * @param client
	 * @param blowfishKey 
	 */
	public SM_INIT(AionConnection client, SecretKey blowfishKey)
	{
		this(client.getEncryptedModulus(), blowfishKey.getEncoded(), client.getSessionId());
	}

	/**
	 * Creates new instance of <tt>SM_INIT</tt> packet.
	 * 
	 * @param publicRsaKey      Public RSA key
	 * @param blowfishKey       Blowfish key
	 * @param sessionId         Session identifier
	 */
	private SM_INIT(byte[] publicRsaKey, byte[] blowfishKey, int sessionId)
	{
		super(0x00);

		this.sessionId = sessionId;
		this.publicRsaKey = publicRsaKey;
		this.blowfishKey = blowfishKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode()); // init packet id

		writeD(buf, sessionId); // session id
		writeD(buf, 0x0000c621); // protocol revision
		writeB(buf, publicRsaKey); // RSA Public Key
		// unk
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeD(buf, 0x00);

		writeB(buf, blowfishKey); // BlowFish key
		writeD(buf, 197635); // unk
		writeD(buf, 2097152); // unk
		
	}
}
