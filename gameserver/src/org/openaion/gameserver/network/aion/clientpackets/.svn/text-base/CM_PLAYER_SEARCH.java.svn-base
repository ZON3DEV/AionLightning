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
package org.openaion.gameserver.network.aion.clientpackets;

import java.util.ArrayList;
import java.util.List;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.FriendList.Status;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_SEARCH;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.utils.Util;
import org.openaion.gameserver.world.World;


/**
 * Received when a player searches using the social search panel
 * 
 * @author Ben
 * 
 */
public class CM_PLAYER_SEARCH extends AionClientPacket
{
	/**
	 * The max number of players to return as results
	 */
	public static final int	MAX_RESULTS	= 111;

	private String			name;
	private int				region;
	private int				classMask;
	private int				minLevel;
	private int				maxLevel;
	private int				lfgOnly;

	public CM_PLAYER_SEARCH(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		if(!(name = readS()).isEmpty())
		{
			name = Util.convertName(name);
			readB(52 - (name.length() * 2 + 2));
		}
		else
		{
			readB(50);
		}
		region = readD();
		classMask = readD();
		minLevel = readC();
		maxLevel = readC();
		lfgOnly = readC();
		readC(); // 0x00 in search pane 0x30 in /who?
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		final Player activePlayer = getConnection().getActivePlayer();

		final List<Player> matches = new ArrayList<Player>(MAX_RESULTS);

		if(activePlayer != null && activePlayer.getLevel() < CustomConfig.LEVEL_TO_SEARCH)
		{
			sendPacket(SM_SYSTEM_MESSAGE.LEVEL_NOT_ENOUGH_FOR_SEARCH(String.valueOf(CustomConfig.LEVEL_TO_SEARCH)));
			return;
		}

		World.getInstance().doOnAllPlayers(new Executor<Player>()
		{
			@Override
			public boolean run(Player player)
			{
				if (matches.size() >= MAX_RESULTS)
					return false;

				if(!player.isSpawned())
					return true;
				else if(CustomConfig.SEARCH_LIST_ALL && (activePlayer.getAccessLevel() >= AdminConfig.SEARCH_LIST_ALL))
				{
					if(player.getFriendList().getStatus() != Status.OFFLINE)
					{
						matches.add(player);
						return true;
					}
				}
				else if(player.getFriendList().getStatus() == Status.OFFLINE)
					return true;
				else if(lfgOnly == 1 && !player.isLookingForGroup())
					return true;
				else if(!name.isEmpty() && !player.getName().toLowerCase().contains(name.toLowerCase()))
					return true;
				else if(minLevel != 0xFF && player.getLevel() < minLevel)
					return true;
				else if(maxLevel != 0xFF && player.getLevel() > maxLevel)
					return true;
				else if(classMask > 0 && (player.getPlayerClass().getMask() & classMask) == 0)
					return true;
				else if(region > 0 && player.getActiveRegion().getMapId() != region)
					return true;
				else if((player.getCommonData().getRace() != activePlayer.getCommonData().getRace())&& (CustomConfig.FACTIONS_SEARCH_MODE == false))
					return true;
				else
				{
					matches.add(player);
					return true;
				}
				return true;
			}
		}, true);

		sendPacket(new SM_PLAYER_SEARCH(matches, region));
	}
}
