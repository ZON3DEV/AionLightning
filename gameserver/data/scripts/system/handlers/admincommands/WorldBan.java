package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.utils.HumanTime;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author blakawk
 * 
 */
public class WorldBan extends AdminCommand
{
	public WorldBan()
	{
		super("wban");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		String syntax = "Syntax: //wban <player name> <time_in_minutes> <reason>";
		Player player = null;
		int duration = 0;
		int durationIndex = 0;

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

		if (params == null || params.length == 0)
		{
			PacketSendUtility.sendMessage(admin, syntax);
			return;
		}

		player = parsePlayerParameter(params[0], admin, syntax);
		if (player == null)
		{
			return;
		}

		try 
		{
			if (player.equals(admin.getTarget()))
			{
				if (params.length < 1)
				{
					PacketSendUtility.sendMessage(admin, syntax);
					return;
				}
				duration = Integer.parseInt(params[0]);
			}
			else
			{
				if (params.length<2)
				{
					PacketSendUtility.sendMessage(admin, syntax);
					return;
				}
				duration = Integer.parseInt(params[1]);
				durationIndex = 1;
			}
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "Duration invalid " + params[durationIndex]);
			PacketSendUtility.sendMessage(admin, syntax);
			return;
		}

		String reason = "";
		for (int i = durationIndex + 1; i<params.length; i++)
		{
			reason += params[i]+" ";
		}

		if (reason.trim().isEmpty())
		{
			reason = "no reason specified";
		}

		if (!player.banFromWorld(admin.getName(), reason, duration * 60 * 1000))
		{
			PacketSendUtility.sendMessage(admin, "Unable to ban " + player.getName() + " from the chat channels, please try again later");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "Player " + player.getName() + " has been banned from the chat channels for " +((duration==0)?"indefinitely":(HumanTime.approximately(duration*60*1000)))+" for the following reason : "+reason);
			PacketSendUtility.sendSysMessage(player, "You have been banned from chat channels by " + admin.getName() + " for "+((duration==0)?"indefinitely":HumanTime.approximately(duration*60*1000))+ " for the following reason : "+reason);
		}
	}
}
