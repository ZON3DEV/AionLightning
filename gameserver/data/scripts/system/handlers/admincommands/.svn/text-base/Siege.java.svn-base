/**
 * This file is part of Aion-Extreme <aion-core.net>.
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

package admincommands;

import java.util.Map;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.configs.main.SiegeConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.siege.Artifact;
import org.openaion.gameserver.model.siege.Commander;
import org.openaion.gameserver.model.siege.SiegeLocation;
import org.openaion.gameserver.model.siege.SiegeRace;
import org.openaion.gameserver.model.siege.SiegeType;
import org.openaion.gameserver.services.SiegeService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Sarynth, Dallas 03/11
 * 
 * Command Syntax
 * //siege capture <location id> <race id> [legion id]
 * //siege set <location id> <current state> [next state] 
 * [not implemented] //siege timer <reset|set [time]>
 * //siege help
 * 
 * TODO: use StringBuilder
 */


public class Siege extends AdminCommand
{
    static final String command = "siege";
    static final String commandHelp = "siege help";
    static final String commandHelpSet = commandHelp + " set";
    static final String commandHelpCapture = commandHelp + " capture";
        /**
         * Constructor
         */
        public Siege()
        {
                super("siege");
        }


        /**
         *  {@inheritDoc}
         */
        @Override
        public void executeCommand(Player admin, String[] params)
        {
                if (SiegeConfig.SIEGE_ENABLED == false)
                {
                        PacketSendUtility.sendMessage(admin, "Siege system is currently disabled.");
                        return;
                }
                
                if (admin.getAccessLevel() < AdminConfig.COMMAND_SIEGE)
                {
                        PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
                        return;
                }
                
                if (params == null || params.length == 0)
                {
                        PacketSendUtility.sendMessage(admin, "No parameters detected.\nPlease use //siege help");
                        return;
                }
                
                // Determine Command
                try
                {
                        String cmd = params[0].toLowerCase();
                        if (("help").startsWith(cmd))
                        {
                                sendHelp(admin, params);
                        }
                        else if (("capture").startsWith(cmd))
                        {
                                processCapture(admin, params);
                        }
                        else if (("set").startsWith(cmd))
                        {
                                processSet(admin, params);
                        }
                        else if (("timer").startsWith(cmd))
                        {
                                processTimer(admin, params);
                        }
                        
                        else
                        {
                                PacketSendUtility.sendMessage(admin, "Sub Command does not exist.\nPlease use //siege help");
                        }
                }
                catch (Exception e)
                {
                        PacketSendUtility.sendMessage(admin, "Error with your request.\nPlease use //siege help");
                }
        }
        
      
        /**
         * @param admin
         * @param params
         */
        private void processTimer(Player admin, String[] params)
        {
                // TODO Auto-generated method stub
                
        }


        /**
         * @param admin
         * @param params
         */
        private void processSet(Player admin, String[] params)
        {
                if (params.length < 3 || params.length > 4)
                {
                        PacketSendUtility.sendMessage(admin, "Incorrect parameter count.\n" +
                                "Please use //siege help set");
                        return;
                }
                
                // Try to get Location Id
                int locationId;
                try
                {
                        locationId = Integer.parseInt(params[1]);
                }
                catch (NumberFormatException e)
                {
                        // TODO: Enable set state by partial fortress location names. 
                        PacketSendUtility.sendMessage(admin, "Location ID must be an integer.");
                        return;
                }
                
                // Try to get Current State
                int currentState;
                try
                {
                        currentState = Integer.parseInt(params[2]);
                }
                catch (NumberFormatException e)
                {
                        String cmd = params[2].toLowerCase();
                        if (("invulnerable").startsWith(cmd))
                        {
                                currentState = 0;
                        }
                        else if (("vulnerable").startsWith(cmd))
                        {
                                currentState = 2;
                        }
                        else
                        {
                                PacketSendUtility.sendMessage(admin, "Current State must be an integer.");
                                return;
                        }
                }
                
                
                // Try to get next state
                int nextState = -1;
                if (params.length == 4)
                {
                        try
                        {
                                nextState = Integer.parseInt(params[3]);
                        }
                        catch (NumberFormatException e)
                        {
                                String cmd = params[3].toLowerCase();
                                if (("invulnerable").startsWith(cmd))
                                {
                                        nextState = 0;
                                }
                                else if (("vulnerable").startsWith(cmd))
                                {
                                        nextState = 1;
                                }
                                else
                                {
                                        PacketSendUtility.sendMessage(admin, "Next State must be an integer.");
                                        return;
                                }
                        }
                }
                
                if (currentState != 0 && currentState != 2)
                {
                        PacketSendUtility.sendMessage(admin, "Incorrect current state value.\n" +
                                "Please use //siege help set");
                        return;
                }
                
                if (params.length == 4 && nextState != 0 && nextState != 1)
                {
                        PacketSendUtility.sendMessage(admin, "Incorrect next state value.\n" +
                                "Please use //siege help set");
                        return;
                }
                
                
                SiegeLocation sLoc = SiegeService.getInstance().getSiegeLocation(locationId);
                
                if (sLoc == null)
                {
                        PacketSendUtility.sendMessage(admin, "Location does not exist: " + locationId);
                        return;         
                }
                
                PacketSendUtility.sendMessage(admin, "[Admin Set State]\n - Location ID: " + locationId +
                        "\n - New Current State: " + (currentState == 2 ? "Vulnerable" : "Invulnerable") +
                        (params.length == 4 ? "\n - New Next State: " + (nextState == 1 ? "Vulnerable" : "Invulnerable") : "") +
                        "\n");
                
                if (sLoc.isVulnerable() != (currentState == 2))
                {
                        sLoc.setVulnerable(currentState == 2);
                }
                
                if (params.length == 4 && sLoc.getNextState() != nextState)
                {
                        sLoc.setNextState(nextState);
                }
                
                SiegeService.getInstance().broadcastUpdate(sLoc);
        }


        /**
         * //siege capture <location id> <race id> [legion id]
         * @param admin
         * @param params
         */
        private void processCapture(Player admin, String[] params)
        {
                if (params.length < 3 || params.length > 4)
                {
                        PacketSendUtility.sendMessage(admin, "Incorrect parameter count.\n" +
                                "Please use //siege help capture");
                        return;
                }
                
                // Try to get Location Id
                int locationId;
                try
                {
                        locationId = Integer.parseInt(params[1]);
                }
                catch (NumberFormatException e)
                {
                        // TODO: Enable capture by partial fortress location names. 
                        PacketSendUtility.sendMessage(admin, "Location ID must be an integer.");
                        return;
                }
                
                // Try to get capturing race
                SiegeRace race;
                String raceName = params[2].toLowerCase();
                if (("elyos").startsWith(raceName))
                {
                        race = SiegeRace.ELYOS;
                }
                else if (("asmos").startsWith(raceName))
                {
                        race = SiegeRace.ASMODIANS;
                }
                else if (("balaur").startsWith(raceName))
                {
                        race = SiegeRace.BALAUR;
                }
                else
                {
                        PacketSendUtility.sendMessage(admin, "Race must be: Elyos, Asmos, or Balaur.\n" +
                        "Please use //siege help capture");
                        return;
                }
                
                // Try to get legion id
                int legionId = 0;
                if (params.length == 4)
                {
                        try
                        {
                                legionId = Integer.parseInt(params[3]);
                        }
                        catch (NumberFormatException e)
                        {
                                // TODO: Enable capture by legion name as a string. 
                                PacketSendUtility.sendMessage(admin, "Legion ID must be an integer.");
                                return;
                        }
                }
                
                SiegeLocation sLoc = SiegeService.getInstance().getSiegeLocation(locationId);
                
                if (sLoc == null)
                {
                        PacketSendUtility.sendMessage(admin, "Location does not exist: " + locationId);
                        return;         
                }
                
                PacketSendUtility.sendMessage(admin, "[Admin Capture]\n - Location ID: " + locationId +
                        "\n - Race: " + race.toString() + "\n - Legion ID: " + legionId + "\n");
               

		SiegeService.getInstance().capture(locationId, race, legionId);
		SiegeService.getInstance().clearFortress(locationId);

		final SiegeRace siegeRace = race;
		final int fortressId = locationId;

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				SiegeService.getInstance().spawnLocation(fortressId, siegeRace, "SIEGE");
			}
		}, 5000);
	}

        /**
         * @param admin
         * @param params
         */
        private void sendHelp(Player admin, String[] params)
        {
                if (params.length == 2)
                {
                        String cmd = params[1].toLowerCase();
                        if (("capture").startsWith(cmd))
                        {
                                sendHelpCapture(admin);
                                return;
                        }
                        else if (("set").startsWith(cmd))
                        {
                                sendHelpSet(admin);
                                return;
                        }
                        else if (("timer").startsWith(cmd))
                        {
                                sendHelpTimer(admin, params);
                                return;
                       
                        }
                }
                sendHelpGeneral(admin);
        }


        /**
         * @param admin
         * @param params
         */
        private void sendHelpList(Player admin, String[] params)
        {
                PacketSendUtility.sendMessage(admin,
                        "[Help: Siege List Command]\n" +
                        "  The siege list command outputs each siege location." +
                        "  Format is: - (fortress|artifact) <location id> (faction owner) [legion id]\n");
        }


        /**
         * @param admin
         */
        private void sendHelpGeneral(Player admin)
        {
                PacketSendUtility.sendMessage(admin,
                        "[Help: Siege Command]\n" +
                        "  Use //siege help <capture|set> for more details on the command.\n" +
                        "  Notice: This command uses smart matching. You may abbreviate most commands.\n" +
                        "  For example: (//siege cap 1011 ely) will match to (//siege capture 1011 elyos)\n");
        }


        /**
         * @param admin
         * @param params
         */
        private void sendHelpTimer(Player admin, String[] params)
        {
                PacketSendUtility.sendMessage(admin,
                        "Timer not implemented.");
        }


        /**
         * @param admin
         */
        private void sendHelpSet(Player admin)
        {
                PacketSendUtility.sendMessage(admin,
                        "Syntax: //siege set <location id> <current state> [next state]\n" +
                        "Current State Values: 0 - Invulnerable, 2 - Vulnerable\n" +
                        "Next State Values: 0 - Invulnerable, 1 - Vulnerable");
        }


        /**
         * @param admin
         */
        private void sendHelpCapture(Player admin)
        {
                PacketSendUtility.sendMessage(admin,
                        "Syntax: //siege capture <location id> <race> [legion id]\n" +
                        "Race may be: Elyos, Asmos, Balaur. (Not case sensitive.)");
        }


}
