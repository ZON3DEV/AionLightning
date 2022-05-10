/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.MailService;

/**
 * @author kosyachok
 *
 */
public class CM_SEND_MAIL extends AionClientPacket
{	
	private static Logger	log	= Logger.getLogger(CM_SEND_MAIL.class);

	private String recipientName;
	private String title;
	private String message;
	private int itemObjId;
	private long itemCount;
	private long kinahCount;
	private int express;
	
	public CM_SEND_MAIL(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{
		recipientName = readS();
		title = readS();
		message = readS();
		itemObjId = readD();
		itemCount = readQ();
		kinahCount = readQ();
		express = readC();
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		
		if(itemCount < -1 || kinahCount < 0) 
		{
			log.info("[ANTICHEAT] Player trying to abuse CM_MAIL packet: " + player.getName() + " IP: " + getConnection().getIP());
			return;
	    }
		 
		if (player.isTrading() || kinahCount > 999999999)
			return;
		
		MailService.getInstance().sendMail(player, recipientName, title, message, itemObjId, itemCount, kinahCount, express == 1, false);
	}
}
