package org.openaion.loginserver.controller;

import java.util.HashMap;
import java.util.Map;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.commons.utils.NetworkUtils;
import org.openaion.loginserver.GameServerInfo;
import org.openaion.loginserver.GameServerTable;
import org.openaion.loginserver.configs.Config;
import org.openaion.loginserver.dao.AccountDAO;
import org.openaion.loginserver.dao.AccountTimeDAO;
import org.openaion.loginserver.model.Account;
import org.openaion.loginserver.model.ReconnectingAccount;
import org.openaion.loginserver.network.aion.AionAuthResponse;
import org.openaion.loginserver.network.aion.AionConnection;
import org.openaion.loginserver.network.aion.SessionKey;
import org.openaion.loginserver.network.aion.AionConnection.State;
import org.openaion.loginserver.network.aion.serverpackets.SM_SERVER_LIST;
import org.openaion.loginserver.network.aion.serverpackets.SM_UPDATE_SESSION;
import org.openaion.loginserver.network.gameserver.GsConnection;
import org.openaion.loginserver.network.gameserver.serverpackets.SM_ACCOUNT_AUTH_RESPONSE;
import org.openaion.loginserver.network.gameserver.serverpackets.SM_GS_REQUEST_CHARACTER_COUNT;
import org.openaion.loginserver.network.gameserver.serverpackets.SM_REQUEST_KICK_ACCOUNT;
import org.openaion.loginserver.utils.AccountUtils;


/**
 * This class is resposible for controlling all account actions
 *
 * @author KID
 * @author SoulKeeper
 */
public class AccountController
{
	/**
	 * Map with accounts that are active on LoginServer or joined GameServer and are not authenticated yet.
	 */
	private static final Map<Integer, AionConnection>		accountsOnLS         = new HashMap<Integer,
																					   AionConnection>();

	/**
	 * Map with accounts that are reconnecting to LoginServer ie was joined GameServer.
	 */
	private static final Map<Integer, ReconnectingAccount>	reconnectingAccounts = new HashMap<Integer,
																					   ReconnectingAccount>();

	/**
	 * Map with characters count on each gameserver and accounts
	 */
	private static final Map<Integer, Map<Integer, Integer>> accountsCharacterCounts = new HashMap<Integer, Map<Integer, Integer>> ();
	
	/**
	 * Removes account from list of connections
	 *
	 * @param account
	 *            account
	 */
	public static synchronized void removeAccountOnLS(Account account)
	{
		accountsOnLS.remove(account.getId());
	}

	/**
	 * This method is for answering GameServer question about account authentication on GameServer side.
	 *
	 * @param key
	 * @param gsConnection
	 */
	public static synchronized void checkAuth(SessionKey key, GsConnection gsConnection)
	{
		AionConnection	con = accountsOnLS.get(key.accountId);

		if(con != null && con.getSessionKey().checkSessionKey(key))
		{
			/**
			 * account is successful logged in on gs remove it from here
			 */
			accountsOnLS.remove(key.accountId);

			GameServerInfo gsi = gsConnection.getGameServerInfo();
			Account acc = con.getAccount();

			/**
			 * Add account to accounts on GameServer list and update accounts last server
			 */
			gsi.addAccountToGameServer(acc);

			acc.setLastServer(gsi.getId());
			getAccountDAO().updateLastServer(acc.getId(), acc.getLastServer());

			/**
			 * Send response to GameServer
			 */
			gsConnection.sendPacket(new SM_ACCOUNT_AUTH_RESPONSE(key.accountId, true, acc.getName(), acc
				.getAccessLevel(), acc.getMembership()));
		}
		else
		{
			gsConnection.sendPacket(new SM_ACCOUNT_AUTH_RESPONSE(key.accountId, false, null, (byte)0, (byte) 0));
		}
	}

	/**
	 * Add account to reconnectionAccount list
	 *
	 * @param acc
	 */
	public static synchronized void addReconnectingAccount(ReconnectingAccount acc)
	{
		reconnectingAccounts.put(acc.getAccount().getId(), acc);
	}

	/**
	 * Check if reconnecting account may auth.
	 *
	 * @param accountId
	 *            id of account
	 * @param loginOk
	 *            loginOk
	 * @param reconnectKey
	 *            reconnect key
	 * @param client
	 *            aion client
	 */
	public static synchronized void authReconnectingAccount(int accountId, int loginOk, int reconnectKey,
			AionConnection client)
	{
		ReconnectingAccount	reconnectingAccount = reconnectingAccounts.remove(accountId);

		if(reconnectingAccount != null && reconnectingAccount.getReconnectionKey() == reconnectKey)
		{
			Account	acc = reconnectingAccount.getAccount();

			client.setAccount(acc);
			accountsOnLS.put(acc.getId(), client);
			client.setState(State.AUTHED_LOGIN);
			client.setSessionKey(new SessionKey(client.getAccount()));
			client.sendPacket(new SM_UPDATE_SESSION(client.getSessionKey()));
		}
		else
		{
			client.close( /* new SM_UPDATE_SESSION, */true);
		}
	}

	/**
	 * Tries to authentificate account.<br>
	 * If success returns {@link AionAuthResponse#AUTHED} and sets account object to connection.<br>
	 *
	 * If {@link org.openaion.loginserver.configs.Config#ACCOUNT_AUTO_CREATION} is enabled - creates new account.<br>
	 *
	 * @param name
	 *            name of account
	 * @param password
	 *            password of account
	 * @param connection
	 *            connection for account
	 * @return Response with error code
	 */
	public static AionAuthResponse login(String name, String password, AionConnection connection)
	{
		Account account = loadAccount(name);

		// Try to create new account
		if(account == null && Config.ACCOUNT_AUTO_CREATION)
		{
			account = createAccount(name, password);
		}

		// If account not found and not created
		if(account == null)
		{
			return AionAuthResponse.INVALID_PASSWORD;
		}

		// check for paswords beeing equals
		if(!account.getPasswordHash().equals(AccountUtils.encodePassword(password)))
		{
			return AionAuthResponse.INVALID_PASSWORD;
		}
		
		// check for paswords beeing equals
		if(account.getActivated() != 1)
		{
			return AionAuthResponse.INVALID_PASSWORD;
		}

		// If account expired
		if(AccountTimeController.isAccountExpired(account))
		{
			return AionAuthResponse.TIME_EXPIRED;
		}

		// if account is banned
		if(AccountTimeController.isAccountPenaltyActive(account))
		{
			return AionAuthResponse.BAN_IP;
		}

		// if account is restricted to some ip or mask
		if(account.getIpForce() != null)
		{
			if(!NetworkUtils.checkIPMatching(account.getIpForce(), connection.getIP()))
			{
				return AionAuthResponse.BAN_IP;
			}
		}

		// if ip is banned
		if(BannedIpController.isBanned(connection.getIP()))
		{
			return AionAuthResponse.BAN_IP;
		}

		// Do not allow to login two times with same account
		synchronized (AccountController.class)
		{
			if(GameServerTable.isAccountOnAnyGameServer(account))
			{
				GameServerTable.kickAccountFromGameServer(account);
				return AionAuthResponse.ALREADY_LOGGED_IN;
			}

			// If someone is at loginserver, he should be disconnected
			if(accountsOnLS.containsKey(account.getId()))
			{
				AionConnection	aionConnection = accountsOnLS.remove(account.getId());

				aionConnection.close(true);

				return AionAuthResponse.ALREADY_LOGGED_IN;
			}
			else
			{
				connection.setAccount(account);
				accountsOnLS.put(account.getId(), connection);
			}
		}

		AccountTimeController.updateOnLogin(account);

		// if everything was OK
		getAccountDAO().updateLastIp(account.getId(), connection.getIP());

		return AionAuthResponse.AUTHED;
	}

	/**
	 * Kicks account from LoginServer and GameServers
	 * 
	 * @param accountId
	 *            account ID to kick
	 */
	public static void kickAccount(int accountId) 
	{
		synchronized (AccountController.class)
		{
			for (GameServerInfo gsi : GameServerTable.getGameServers())
			{
				if (gsi.isAccountOnGameServer(accountId))
				{
					gsi.getGsConnection().sendPacket(new SM_REQUEST_KICK_ACCOUNT(accountId));
					break;
				}
			}
			if (accountsOnLS.containsKey(accountId))
			{
				AionConnection aionConnection = accountsOnLS.remove(accountId);
				aionConnection.close(true);
			}
		}
	}

	/**
	 * Loads account from DB and returns it, or returns null if account was not loaded
	 * @param name acccount name
	 * @return loaded account or null
	 */
	public static Account loadAccount(String name) 
	{
		Account	account = getAccountDAO().getAccount(name);
		if (account != null)
		{
			account.setAccountTime(getAccountTimeDAO().getAccountTime(account.getId()));
		}
		return account;
	}

	/**
	 * Creates new account and stores it in DB. Returns account object in case of success or null if failed
	 *
	 * @param name
	 *            account name
	 * @param password
	 *            account password
	 * @return account object or null
	 */
	public static Account createAccount(String name, String password)
	{
		String	passwordHash = AccountUtils.encodePassword(password);
		Account	account      = new Account();

		account.setName(name);
		account.setPasswordHash(passwordHash);
		account.setAccessLevel((byte) 0);
		account.setMembership((byte) 0);
		account.setActivated((byte) 1);

		if(getAccountDAO().insertAccount(account))
		{
			return account;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns {@link org.openaion.loginserver.dao.AccountDAO}, just a shortcut
	 *
	 * @return {@link org.openaion.loginserver.dao.AccountDAO}
	 */
	private static AccountDAO getAccountDAO()
	{
		return DAOManager.getDAO(AccountDAO.class);
	}

	/**
	 * Returns {@link org.openaion.loginserver.dao.AccountTimeDAO}, just a shortcut
	 *
	 * @return {@link org.openaion.loginserver.dao.AccountTimeDAO}
	 */
	private static AccountTimeDAO getAccountTimeDAO()
	{
		return DAOManager.getDAO(AccountTimeDAO.class);
	}
	
	/**
	 * Request character count on each gameserver for account
	 * 
	 * @param accountId
	 */
	public static synchronized void loadCharactersCount (int accountId)
	{
		GsConnection gsc = null;
		Map<Integer, Integer> accountCharacterCount = null;
		
		if (accountsCharacterCounts.containsKey(accountId))
		{
			accountsCharacterCounts.remove(accountId);
		}
		accountsCharacterCounts.put(accountId, new HashMap<Integer, Integer> ());
		
		accountCharacterCount = accountsCharacterCounts.get(accountId);
		
		for (GameServerInfo gsi : GameServerTable.getGameServers()) {
			gsc = gsi.getGsConnection();
			if (gsc != null) {
				gsc.sendPacket(new SM_GS_REQUEST_CHARACTER_COUNT(accountId));
			} else {
				accountCharacterCount.put((int)gsi.getId(), 0);
			}
		}
		if (hasAllCharacterCounts(accountId))
		{
			sendServerListFor(accountId);
		}
	}
	
	/**
	 * Check if all SM_GS_CHARACTER_COUNT have been received from all game servers
	 * 
	 * @param accountId
	 * @return
	 */
	public static synchronized boolean hasAllCharacterCounts (int accountId)
	{
		Map<Integer, Integer> characterCount = accountsCharacterCounts.get(accountId);
		
		if (characterCount != null)
		{
			if(characterCount.size() == GameServerTable.getGameServers().size())
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Send SM_SERVER_LIST to client for account
	 * 
	 * @param accountId
	 */
	public static synchronized void sendServerListFor (int accountId)
	{
		if(accountsOnLS.containsKey(accountId))
		{
			accountsOnLS.get(accountId).sendPacket(new SM_SERVER_LIST());
		}
	}
	
	/**
	 * Return map containing characters count on each server for account
	 * 
	 * @param accountId
	 * @return
	 */
	public static synchronized Map<Integer, Integer> getCharacterCountsFor (int accountId)
	{
		return accountsCharacterCounts.get(accountId);
	}
	
	/**
	 * Add characters count for account on game server
	 * 
	 * @param accountId
	 * @param gsid
	 * @param characterCount
	 */
	public static synchronized void addCharacterCountFor (int accountId, int gsid, int characterCount)
	{
		if(!accountsCharacterCounts.containsKey(accountId)) {
			accountsCharacterCounts.put(accountId, new HashMap<Integer, Integer> ());
		}
		accountsCharacterCounts.get(accountId).put(gsid, characterCount);
	}
}
