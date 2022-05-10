/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import org.openaion.gameserver.cache.HTMLCache;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.services.HTMLService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author lord_rex
 * 
 */
public class HTML extends AdminCommand
{
	public HTML()
	{
		super("html");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_HTML)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //html reload || //html test <filename.xhtml>");
			return;
		}

		if (params[0].equals("reload"))
		{
			HTMLCache.getInstance().reload(true);
			PacketSendUtility.sendMessage(admin, HTMLCache.getInstance().toString());
		}
		else if (params[0].equals("test"))
		{
			HTMLService.showHTML(admin, HTMLCache.getInstance().getHTML(params[1]));
		}
	}
}
