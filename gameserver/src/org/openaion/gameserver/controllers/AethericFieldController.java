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
package org.openaion.gameserver.controllers;

import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.siege.AethericField;
import org.openaion.gameserver.model.siege.SiegeLocation;
import org.openaion.gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import org.openaion.gameserver.services.SiegeService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Sylar, Vial, Ritsu
 *
 */
public class AethericFieldController extends NpcController
{	

	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);
		int id = getOwner().getFortressId();
		SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(id);
		//disable field
		loc.setShieldActive(false);
		//TODO : Find sys message sended on generator death
		getOwner().getKnownList().doOnAllPlayers(new Executor<Player>(){		
			@Override
			public boolean run(Player object)
			{
				//Needed to update the display of shield effect
				PacketSendUtility.sendPacket(object, new SM_SIEGE_LOCATION_INFO());
				return true;
			}
		}, true);
	}

	@Override
	public AethericField getOwner()
	{
		return (AethericField) super.getOwner();
	}	
}
