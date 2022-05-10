/*
* This file is part of zetta-core <zetta-core.net>.
*
* credits: kecimis
*/
package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
* @author kecimis
*
*/
public class Movie extends AdminCommand
{
	public Movie()
	{
		super("movie");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVIE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		int movieId = 0;
		int type = 0;
		VisibleObject target = admin.getTarget();

		if (target == null || !(target instanceof Player))
		{
			target = admin;
		}
		if (params.length == 0)
		{
			PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			return;
		}
		if (params.length == 1)
		{
			try
			{
				movieId = Integer.valueOf(params[0]);
				PacketSendUtility.sendPacket((Player)target, new SM_PLAY_MOVIE(0, movieId));
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Use numbers only!");
			}
		}
		else if (params.length == 2)
		{
			try
			{
				type = Integer.valueOf(params[0]);
				movieId = Integer.valueOf(params[1]);
				PacketSendUtility.sendPacket((Player)target, new SM_PLAY_MOVIE(type, movieId));
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Use numbers only!");
			}
		}

	}
}