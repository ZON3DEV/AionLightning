/*
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
package org.openaion.gameserver.model.gameobjects.player;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * Manages the asking of and responding to <tt>SM_QUESTION_WINDOW</tt>
 * 
 * @author Ben
 * @author Jego
 */
public class ResponseRequester
{
	private Player										player;
	private HashMap<Integer, RequestResponseHandler>	map			= new HashMap<Integer, RequestResponseHandler>();
	private ArrayList<ResponseRequesterEntry>			waitingList	= new ArrayList<ResponseRequesterEntry>();
	private static Logger								log			= Logger.getLogger(ResponseRequester.class);

	public ResponseRequester(Player player)
	{
		this.player = player;
	}

	/**
	 * Adds this handler to this messageID, returns false if there already exists one
	 * @param messageId ID of the request message
	 * @return true or false
	 */
	public synchronized boolean putRequest(int messageId, RequestResponseHandler handler)
	{
		if (map.containsKey(messageId))
			return false;

		map.put(messageId, handler);
		return true;
	}

	/**
	 * Adds this handler to the messageId, puts the messageID on a waitinglist if there is already a message of this
	 * type send to the player.
	 * 
	 * @param messageId
	 *            The Id of the message.
	 * @param handler
	 *            The handler for the response.
	 * @param packet
	 *            The message to send to the player.
	 * @return
	 */
	public synchronized boolean sendRequest(int messageId, RequestResponseHandler handler, AionServerPacket packet)
	{
		if(map.containsKey(messageId))
		{
			waitingList.add(new ResponseRequesterEntry(messageId, handler, packet));
			return true;
		}

		map.put(messageId, handler);
		PacketSendUtility.sendPacket(player, packet);
		return true;
	}

	/**
	 * Send the next request of the same type.
	 * 
	 * @return True if a new request is send, else false.
	 */
	private boolean sendWaitingRequest(int messageId)
	{
		for(ResponseRequesterEntry rre : waitingList)
		{
			if(rre.getMessageId() == messageId)
			{
				map.put(messageId, rre.getHandler());
				PacketSendUtility.sendPacket(player, rre.getPacket());
				waitingList.remove(rre);
				return true;
			}
		}
		map.remove(messageId);
		return false;
	}

	/**
	 * Responds to the given message ID with the given response
	 * Returns success
	 * @param messageId
	 * @param response
	 * @return Success
	 */
	public synchronized boolean respond(int messageId, int response)
	{
		RequestResponseHandler handler = map.get(messageId);
		if (handler != null)
		{
			log.debug("RequestResponseHandler triggered for response code " + messageId + " from " + player.getName());
			handler.handle(player, response);
			sendWaitingRequest(messageId);
			return true;
		}
		return false;
	}

	/**
	 * Automatically responds 0 to all requests, passing the given player as the responder
	 */
	public synchronized void denyAll()
	{
		for (RequestResponseHandler handler : map.values())
		{
			handler.handle(player, 0);
		}
		for(ResponseRequesterEntry rre : waitingList)
		{
			rre.getHandler().handle(player, 0);
		}

		map.clear();
		waitingList.clear();
	}
}
