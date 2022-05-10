/* 
* This file is part of aion unique <aion-unique.org>.
*
*  aion unique is free software: you can redistribute it and/or modify
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
*  along with aion unique.  If not, see <http://www.gnu.org/licenses/>.
*/
package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureVisualState;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import org.openaion.gameserver.skill.effect.EffectId;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
* @author Divinity
*
*/

public class Invis extends AdminCommand
{
	public Invis()
	{
		super("invis");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_INVIS)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}
		if (admin.getVisualState() < 20)
		{
			admin.getEffectController().setAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
			admin.setVisualState(CreatureVisualState.HIDE20);
			PacketSendUtility.broadcastPacket(admin, new SM_PLAYER_STATE(admin), true);
			PacketSendUtility.sendMessage(admin, "You are now invisible.");
		}
		else
		{
			admin.getEffectController().unsetAbnormal(EffectId.INVISIBLE_RELATED.getEffectId());
			admin.unsetVisualState(CreatureVisualState.HIDE20);
			PacketSendUtility.broadcastPacket(admin, new SM_PLAYER_STATE(admin), true);
			PacketSendUtility.sendMessage(admin, "You are now visible.");
		}
	}
}
