/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package usercommands;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.CustomChannel;
import org.openaion.gameserver.utils.i18n.CustomMessageId;
import org.openaion.gameserver.utils.i18n.LanguageHandler;


/**
 * @author blakawk
 *
 */
public class WorldChannelMessage extends CustomChannel
{
	public WorldChannelMessage ()
	{
		super(LanguageHandler.translate(CustomMessageId.CHANNEL_COMMAND_WORLD), Player.CHAT_FIXED_ON_WORLD);
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
		if (!CustomConfig.CHANNEL_WORLD_ENABLED)
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_WORLD_DISABLED, Player.getChanCommand(Player.CHAT_FIXED_ON_WORLD), Player.getChanCommand(Player.CHAT_FIXED_ON_ELYOS), Player.getChanCommand(Player.CHAT_FIXED_ON_ASMOS)));
			return;
		}
		
		super.executeCommand(player, params);
	}

}
