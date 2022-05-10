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
package org.openaion.gameserver.utils.chathandlers;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.i18n.CustomMessageId;
import org.openaion.gameserver.utils.i18n.LanguageHandler;
import org.openaion.gameserver.world.World;


/**
 * @author blakawk
 *
 */
public abstract class CustomChannel extends UserCommand
{
	
	private int channel;
	
	public CustomChannel (String name, int channel)
	{
		super(name);
		this.channel = channel;
	}
	
	public static void sendWorldBanMessage (Player player)
	{
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_BANNED, player.getBannedFromWorldBy(), player.getBannedFromWorldReason(), player.getBannedFromWorldRemainingTime()));
	}

	public static void sendMessageOnWorld (final Player sender, final String worldMessage, final int channel)
	{
		if(sender.isBannedFromWorld())
		{
			sendWorldBanMessage(sender);
			return;
		}

		if (sender.getAccessLevel() == 0)
		{
			if ((sender.getCommonData().getRace() == Race.ASMODIANS && channel == Player.CHAT_FIXED_ON_ELYOS) || (sender.getCommonData().getRace() == Race.ELYOS && channel == Player.CHAT_FIXED_ON_ASMOS))
			{
				PacketSendUtility.sendMessage(sender, LanguageHandler.translate(CustomMessageId.CHANNEL_NOT_ALLOWED));
				return;
			}
		}
		
		if (!sender.canSayInChannel())
			return;
		
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player p)
			{
				if(!p.isSpawned()||!p.isOnline())
				{
					return true;
				}

				String toSend = "";
				if(sender.getAccessLevel()>0 && CustomConfig.GMTAG_DISPLAY)
				{
					switch (sender.getAccessLevel())
					{
						case 1:
							toSend += " "+CustomConfig.GM_LEVEL1.trim();
							break;
						case 2:
							toSend += " "+CustomConfig.GM_LEVEL2.trim();
							break;
						case 3:
							toSend += " "+CustomConfig.GM_LEVEL3.trim();
							break;
					}
				}
				toSend += " " + sender.getName();
				toSend += " : " + worldMessage;

				if(sender.getAccessLevel() > 0)
				{
					if (channel == Player.CHAT_FIXED_ON_ELYOS && (p.getCommonData().getRace() == Race.ELYOS || p.getAccessLevel() > 0))
					{
						PacketSendUtility.sendMessage(p, ("["+Player.getChanName(channel)+"]").concat(toSend));
					}
					if (channel == Player.CHAT_FIXED_ON_ASMOS && (p.getCommonData().getRace() == Race.ASMODIANS || p.getAccessLevel() > 0))
					{
						PacketSendUtility.sendMessage(p, ("["+Player.getChanName(channel)+"]").concat(toSend));
					}
					if (channel == Player.CHAT_FIXED_ON_WORLD)
					{
						PacketSendUtility.sendMessage(p, ("["+Player.getChanName(channel)+"]").concat(toSend));
					}
					if (channel == Player.CHAT_FIXED_ON_BOTH)
					{
						if (p.equals(sender) || p.getAccessLevel() > 0)
						{
							toSend = "["+Player.getChanName(channel)+"]"+toSend;
						}
						else
						{
							if (p.getCommonData().getRace() == Race.ASMODIANS)
							{
								toSend = "["+Player.getChanName(Player.CHAT_FIXED_ON_ASMOS)+"]"+toSend;
							}
							if (p.getCommonData().getRace() == Race.ELYOS)
							{
								toSend = "["+Player.getChanName(Player.CHAT_FIXED_ON_ELYOS)+"]"+toSend;
							}
						}
						PacketSendUtility.sendMessage(p, toSend);
					}
				}
				else
				{
					toSend = "["+Player.getChanName(channel)+"]"+toSend;
					if (sender.getCommonData().getRace() == Race.ASMODIANS)
					{
						if (channel == Player.CHAT_FIXED_ON_ASMOS || channel == Player.CHAT_FIXED_ON_WORLD)
						{
							if ( p.getCommonData().getRace() == Race.ASMODIANS || channel == Player.CHAT_FIXED_ON_WORLD || p.getAccessLevel() > 0)
							{
								PacketSendUtility.sendMessage(p, toSend);
							}
						}
					}
					if (sender.getCommonData().getRace() == Race.ELYOS)
					{
						if (channel == Player.CHAT_FIXED_ON_ELYOS || channel == Player.CHAT_FIXED_ON_WORLD)
						{
							if ( p.getCommonData().getRace() == Race.ELYOS || channel == Player.CHAT_FIXED_ON_WORLD || p.getAccessLevel() > 0)
							{
								PacketSendUtility.sendMessage(p, toSend);
							}
						}
					}
				}
				sender.setLastMessageTime();
				return true;
			}
		});
		return;
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
		if (!CustomConfig.CHANNEL_ALL_ENABLED)
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_ALL_DISABLED));
			return;
		}
		
		String[] cmdAndParams = params.split(" ", 2);
		
		if (cmdAndParams.length > 1)
		{
			if (cmdAndParams[0].equalsIgnoreCase("fix"))
			{
				if(player.isBannedFromWorld())
				{
					sendWorldBanMessage(player);
					return;
				}

				if(player.CHAT_FIX_WORLD_CHANNEL == channel)
				{
					PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_ALREADY_FIXED, Player.getChanCommand(channel)));
					return;
				}
				
				player.CHAT_FIX_WORLD_CHANNEL = channel;
				
				switch(channel)
				{
					case Player.CHAT_FIXED_ON_ASMOS:
						if (player.getCommonData().getRace()==Race.ASMODIANS || player.getAccessLevel() > 0)
						{
							PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_FIXED, Player.getChanName(channel)));
						}
						else
						{
							PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_NOT_ALLOWED));
							return;
						}
						break;
					case Player.CHAT_FIXED_ON_ELYOS:
						if (player.getCommonData().getRace()==Race.ELYOS || player.getAccessLevel() > 0)
						{
							PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_FIXED, Player.getChanName(channel)));
						}
						else
						{
							PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_NOT_ALLOWED));
							return;
						}
						break;
					case Player.CHAT_FIXED_ON_WORLD:
						PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_FIXED, Player.getChanName(channel)));
						break;
					case Player.CHAT_FIXED_ON_BOTH:
						PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_FIXED_BOTH, Player.getChanName(Player.CHAT_FIXED_ON_ASMOS), Player.getChanName(Player.CHAT_FIXED_ON_ELYOS)));
						break;
					default:
						return;
				}
				PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_UNFIX_HELP, Player.getChanCommand(channel)));
				return;
			}
			
			if (cmdAndParams[0].equalsIgnoreCase("unfix"))
			{
				if (player.CHAT_FIX_WORLD_CHANNEL == Player.CHAT_NOT_FIXED)
				{
					PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_NOT_FIXED));
					return;
				}

				if (player.CHAT_FIX_WORLD_CHANNEL != channel)
				{
					PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_FIXED_OTHER, Player.getChanName(channel)));
					return;
				}

				player.CHAT_FIX_WORLD_CHANNEL = Player.CHAT_NOT_FIXED;
				
				switch(channel)
				{
					case Player.CHAT_FIXED_ON_ASMOS:
					case Player.CHAT_FIXED_ON_ELYOS:
					case Player.CHAT_FIXED_ON_WORLD:
						PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_RELEASED, Player.getChanName(channel)));
						break;
					case Player.CHAT_FIXED_ON_BOTH:
						PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_RELEASED_BOTH, Player.getChanName(Player.CHAT_FIXED_ON_ASMOS), Player.getChanName(Player.CHAT_FIXED_ON_ELYOS)));
						break;
				}
				
				return;
			}
		}
		
		sendMessageOnWorld(player, params, channel);
	}

}
