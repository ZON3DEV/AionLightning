package admincommands;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.dao.PlayerDAO;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;


/**
* Admin online command.
*
* @author Kamui & blakawk
*/

public class Online extends AdminCommand
{
	public Online()
	{
		super("online");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ONLINE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		int pCount = DAOManager.getDAO(PlayerDAO.class).getOnlinePlayerCount();

		if (pCount == 1)
		{
			PacketSendUtility.sendMessage(admin, "There is only " + (pCount) + " player online now !");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "There are " + (pCount) + " players online now !");
		}
	}
}