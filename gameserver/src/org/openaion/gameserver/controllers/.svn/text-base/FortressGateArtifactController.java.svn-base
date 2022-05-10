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

import org.openaion.gameserver.model.DescriptionId;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.RequestResponseHandler;
import org.openaion.gameserver.model.siege.FortressGateArtifact;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import org.openaion.gameserver.utils.PacketSendUtility;

/**
 * @author Sylar
 *
 */
public class FortressGateArtifactController extends NpcController
{	

	@Override
	public void onDialogRequest(final Player player)
	{
		RequestResponseHandler artifactHandler = new RequestResponseHandler(player){
			
			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				// Close window
			}
			
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				RequestResponseHandler acceptItem = new RequestResponseHandler(player){
					
					@Override
					public void denyRequest(Creature requester, Player responder)
					{
						// Refuse item do nothing
					}
					
					@Override
					public void acceptRequest(Creature requester, Player responder)
					{
						onActivate(player);
					}
				};
				if(player.getResponseRequester().putRequest(160016, acceptItem))
				{
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160016, player.getObjectId(), new DescriptionId(2*716568+1)));
				}
			}
		};
		if(player.getResponseRequester().putRequest(160027, artifactHandler))
		{
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160027, player.getObjectId()));
		}
	}
	
	public void onActivate(Player player)
	{
		
	}
	
	@Override
	public void onRespawn()
	{
		super.onRespawn();
	}

	@Override
	public FortressGateArtifact getOwner()
	{
		return (FortressGateArtifact) super.getOwner();
	}
}
