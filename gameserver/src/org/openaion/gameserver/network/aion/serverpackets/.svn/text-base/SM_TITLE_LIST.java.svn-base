/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 *     Aion-unique is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Aion-unique is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.configs.main.GSConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.Title;
import org.openaion.gameserver.model.gameobjects.player.TitleList;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.TitleService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * 
 * @author Nemiroff
 * @author blakawk, ginho1
 * 
 */
public class SM_TITLE_LIST extends AionServerPacket
{
	private TitleList	titleList;
	private int objectId;
	private int titleId;

	// TODO Make List from DataBase
	public SM_TITLE_LIST(Player player)
	{
		this.titleList = player.getTitleList();
	}

	public SM_TITLE_LIST(int objectId, int titleId)
	{
		this.objectId = objectId;
		this.titleId = titleId;
	}

	public SM_TITLE_LIST(int titleId)
	{
		this.titleId = titleId;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(titleList != null)
		{
			writeImplTitleList(buf);
			return;
		}

		if(objectId > 0 && titleId > 0)
		{
			writeImplTitleUpdate(buf);
			return;
		}

		writeImplTitleSet(buf);
		return;
	}

	private void writeImplTitleList(ByteBuffer buf)
	{
		Player player = titleList.getOwner();
		TitleService.removeExpiredTitles(player);

		if(GSConfig.SERVER_VERSION.startsWith("2."))
			writeH(buf, 0); // unk
		else
			writeC(buf, 0); // unk

		writeH(buf, titleList.size());

		for(final Title title : titleList.getTitles())
		{
			writeD(buf, title.getTitleId());
			writeD(buf, (int)title.getTitleTimeLeft());
		}

		if(player.getCommonData().getTitleId() > 0)
		{
			if(titleList.canAddTitle(player.getCommonData().getTitleId()))
			{
				player.getCommonData().setTitleId(0);
				PacketSendUtility.sendMessage(player, "The usage time of title has expired.");
			}
		}

	}

	private void writeImplTitleUpdate(ByteBuffer buf)
	{
		writeD(buf, objectId);
		writeD(buf, titleId);
	}

	protected void writeImplTitleSet(ByteBuffer buf)
	{
		writeC(buf, 1);
		writeD(buf, titleId);
	}
}
