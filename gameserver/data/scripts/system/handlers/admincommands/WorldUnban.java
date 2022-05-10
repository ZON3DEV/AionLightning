package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author blakawk
 * 
 */
public class WorldUnban extends AdminCommand
{
	public WorldUnban()
	{
		super("wunban");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		String syntax = "Syntax: //wunban <player>";

		if (!CustomConfig.CHANNEL_ALL_ENABLED)
		{
			PacketSendUtility.sendMessage(admin, "<There is no such admin command: " + getCommandName() + ">");
			return;
		}

		if (admin.getAccessLevel() < AdminConfig.COMMAND_WORLDBAN)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		String param = null;
		if (params.length>=1)
		{
			param = params[0];
		}
		
		Player player = parsePlayerParameter(param, admin, syntax);
		if (player==null)
		{
			return;
		}

		if (!player.isBannedFromWorld())
		{
			PacketSendUtility.sendMessage(admin, "Player "+player.getName()+" is not banned from the chat channels");
		}
		else
		{
			player.unbanFromWorld();
			PacketSendUtility.sendSysMessage(player, "You are no longer banned from the chat channels");
			PacketSendUtility.sendMessage(admin, "Player "+player.getName()+" is not banned from the chat channels");
		}
	}
}
