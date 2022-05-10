package admincommands;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.dao.PlayerDAO;
import org.openaion.gameserver.dao.PlayerPasskeyDAO;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.loginserver.LoginServer;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.Util;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;


/**
 * @author ginho1
 */
public class Passkey extends AdminCommand
{
	public Passkey()
	{
		super("passkey");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_PASSKEY)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax: //passkey <player> <passkey>");
			return;
		}

		String name = Util.convertName(params[0]);
		int accountId = DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(name);
		if (accountId == 0)
		{
			PacketSendUtility.sendMessage(admin, "player " + name + " can't find!");
			PacketSendUtility.sendMessage(admin, "syntax: //passkey <player> <passkey>");
			return;
		}

		try
		{
			Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "parameters should be number!");
			return;
		}

		String newPasskey = params[1];
		if (!(newPasskey.length() > 5 && newPasskey.length() < 9))
		{
			PacketSendUtility.sendMessage(admin, "passkey is 6~8 digits!");
			return;
		}

		DAOManager.getDAO(PlayerPasskeyDAO.class).updateForcePlayerPasskey(accountId, newPasskey);
		LoginServer.getInstance().sendBanPacket((byte) 2, accountId, "", -1, admin.getObjectId());
	}
}