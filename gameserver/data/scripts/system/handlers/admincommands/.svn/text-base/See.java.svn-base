package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureSeeState;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
* @author kecimis
*
*/

public class See extends AdminCommand
{
	public See()
	{
		super("see");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_SEE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}
		if (params.length == 1)
		{
			int see;
			try 
			{
				see = Integer.parseInt(params[0]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Syntax: //see <0|1|2|20>");
				return;
			}

			Player target = null;
		
			if (admin.getTarget() != null && admin.getTarget() instanceof Player)
				target = (Player)admin.getTarget();
			else
				target = admin;
		
			CreatureSeeState seeState;
				
			switch(see)
			{
				case 1:
					seeState = CreatureSeeState.SEARCH1;
					break;
				case 2:
					seeState = CreatureSeeState.SEARCH2;
					break;
				case 20:
					seeState = CreatureSeeState.SEARCH20;
					break;
				default:
					seeState = CreatureSeeState.NORMAL;
					break;
			}
				
			//reset see state
			for(CreatureSeeState saw : CreatureSeeState.values())
			{
				if (target.isInSeeState(saw))
					target.unsetSeeState(saw);
			}
				
			target.setSeeState(seeState);

			PacketSendUtility.broadcastPacket(target, new SM_PLAYER_STATE(target), true);
			PacketSendUtility.sendMessage(target, "Your SeeState level was changed to "+see);
			
		}
		else
			PacketSendUtility.sendMessage(admin, "Syntax: //see <0|1|2|20>");
		
	}
}