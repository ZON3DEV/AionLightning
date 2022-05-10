/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openaion.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.model.ChatType;
import org.openaion.gameserver.model.gameobjects.player.FriendList;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.restrictions.RestrictionsManager;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.Util;
import org.openaion.gameserver.world.World;


/**
 * Packet that reads Whisper chat messages.<br>
 * 
 * @author SoulKeeper
 */
public class CM_CHAT_MESSAGE_WHISPER extends AionClientPacket
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(CM_CHAT_MESSAGE_WHISPER.class);

	/**
	 * To whom this message is sent
	 */
	private String				name;

	/**
	 * Message text
	 */
	private String				message;

	/**
	 * Constructs new client packet instance.
	 * @param opcode
	 */
	public CM_CHAT_MESSAGE_WHISPER(int opcode)
	{
		super(opcode);

	}

	/**
	 * Read message
	 */
	@Override
	protected void readImpl()
	{
		name = readS();
		message = readS();
	}

	/**
	 * Print debug info
	 */
	@Override
	protected void runImpl()
	{
		String formatname = Util.convertName(name);

		Player sender = getConnection().getActivePlayer();
		Player receiver = World.getInstance().findPlayer(formatname);

		if(GSConfig.LOG_CHAT)
			log.info(String.format("[MESSAGE] [%s] Whisper To: %s, Message: %s", sender.getName(), formatname, message));

		if(receiver == null || receiver.getFriendList().getStatus()== FriendList.Status.OFFLINE || CustomConfig.FACTIONS_WHISPER_MODE == 0 && (sender.getCommonData().getRace() != receiver.getCommonData().getRace() && (!sender.isGM() && !receiver.isGM())))
		{
			sendPacket(SM_SYSTEM_MESSAGE.PLAYER_IS_OFFLINE(formatname));
		}
		else if(sender.getLevel() < CustomConfig.LEVEL_TO_WHISPER)
		{
			sendPacket(SM_SYSTEM_MESSAGE.LEVEL_NOT_ENOUGH_FOR_WHISPER(String.valueOf(CustomConfig.LEVEL_TO_WHISPER)));
		}
		else if (receiver.getBlockList().contains(sender.getObjectId()))
		{
			sendPacket(SM_SYSTEM_MESSAGE.YOU_ARE_BLOCKED_BY(receiver.getName()));
		}
		else if (!sender.isGM() && !receiver.isWhisperable())
		{
			PacketSendUtility.sendMessage(sender, receiver.getName() + " is on whisper refusal mode now, sorry.");
		}
		else
		{
			if(RestrictionsManager.canChat(sender))
				PacketSendUtility.sendPacket(receiver, new SM_MESSAGE(sender, message, ChatType.WHISPER));
		}
	}
}
