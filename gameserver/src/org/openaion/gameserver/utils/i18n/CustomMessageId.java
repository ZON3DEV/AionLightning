/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.utils.i18n;

/**
 * @author blakawk
 *
 */
public enum CustomMessageId
{
    WELCOME_PREMIUM("Welcome Premium Member to %s server.\nPowered by Aion-Extreme&OA 2010 www.aion-core.net.\nSERVER RATES:\nExp Rate: %d\nQuest Rate: %d\nDrop Rate: %d\n"),
    WELCOME_REGULAR("Welcome to %s server.\nPowered by Aion-Extreme&OA 2010 www.aion-core.net.\nSERVER RATES:\nExp Rate: %d\nQuest Rate: %d\nDrop Rate: %d\n"),
    SERVER_REVISION("Server Revision: %-6s"),
    WELCOME_BASIC("Welcome to %s server.\nPowered by Aion-Extreme&OA 2011 www.aion-core.net."),
    ANNOUNCE_GM_CONNECTION("%s just entered into Atreia."),
        COMMAND_NOT_ENOUGH_RIGHTS("You dont have enough rights to execute this command"),
        PLAYER_NOT_ONLINE("The player %s is not online"),
        INTEGER_PARAMETER_REQUIRED("Parameter needs to be an integer"),
        INTEGER_PARAMETERS_ONLY("Parameters need to be only integers"),
        SOMETHING_WRONG_HAPPENED("Something wrong happened"),
        COMMAND_DISABLED("This command is disabled"),
        COMMAND_ADD_SYNTAX("Syntax: //add <player name> <item id> <quantity>"),
        COMMAND_ADD_ADMIN_SUCCESS("Item(s) successfully added to player %s"),
        COMMAND_ADD_PLAYER_SUCCESS("Admin %s gave you %d item(s)"),
        COMMAND_ADD_FAILURE("Item %d does not exists and/or cannot be added to %s"),
        COMMAND_ADDDROP_SYNTAX("Syntax: //adddrop <mob id> <item id> <min> <max> <chance>"),
        COMMAND_ADDSET_SYNTAX("Syntax: //addset <player name> <itemset id>"),
        COMMAND_ADDSET_SET_DOES_NOT_EXISTS("Item set with id %d does not exists"),
        COMMAND_ADDSET_NOT_ENOUGH_SLOTS("Inventory needs at least %d free slots"),
        COMMAND_ADDSET_CANNOT_ADD_ITEM("Item %d cannot be added to %s"),
        COMMAND_ADDSET_ADMIN_SUCCESS("Item set %d successfully added to %s"),
        COMMAND_ADDSET_PLAYER_SUCCESS("Admin %s gave you an item set"),
        COMMAND_ADDSKILL_SYNTAX("Syntax: //addskill <skill id> <skill level>"),
        COMMAND_ADDSKILL_ADMIN_SUCCESS("Skill %d was added to player %s with success"),
        COMMAND_ADDSKILL_PLAYER_SUCCESS("%s gave you a new skill"),
        COMMAND_ADDTITLE_SYNTAX("Syntax: //addtitle <title id> <player name> [special]"),
        COMMAND_ADDTITLE_TITLE_INVALID("Title %d is invalid, it must be between 1 and 50"),
        COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME("You can't add title %d to yourself"),
        COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER("You can't add title %d to %s"),
        COMMAND_ADDTITLE_ADMIN_SUCCESS_ME("You added title %d to yourself with success"),
        COMMAND_ADDTITLE_ADMIN_SUCCESS("You added title %d to %s with success"),
        COMMAND_ADDTITLE_PLAYER_SUCCESS("Admin %s gave you title %d"),
        COMMAND_SEND_SYNTAX("Syntax: //send <filename>"),
        COMMAND_SEND_MAPPING_NOT_FOUND("Mapping %s not found"),
        COMMAND_SEND_NO_PACKET("No packet to send"),
        CHANNEL_WORLD_DISABLED("The channel %s is disabled, please use channel %s or %s according to your faction"),
        CHANNEL_ALL_DISABLED("The custom channels are disabled"),
        CHANNEL_ALREADY_FIXED("Your channel is already fixed on %s"),
        CHANNEL_FIXED("Your chat is now fixed on %s"),
        CHANNEL_NOT_ALLOWED("You're not allowed to speak on this channel"),
        CHANNEL_FIXED_BOTH("Your chat is now fixed on %s and %s"),
        CHANNEL_UNFIX_HELP("Type %s unfix to release your chat"),
        CHANNEL_NOT_FIXED("Your chat isn't fixed on any channel"),
        CHANNEL_FIXED_OTHER("Your chat is not fixed on this channel, but on %s"),
        CHANNEL_RELEASED("Your chat has been released from %s channel"),
        CHANNEL_RELEASED_BOTH("Your chat has been released from %s and %s channels"),
        CHANNEL_BAN_ENDED("You're not anymore banned from chat channels"),
        CHANNEL_BAN_ENDED_FOR("The player %s is no more banned from chat channels"),
        CHANNEL_NAME_ASMOS("Asmodians"),
        CHANNEL_NAME_ELYOS("Elyos"),
        CHANNEL_NAME_WORLD("World"),
        CHANNEL_NAME_BOTH("Asmodians/Elyos"),
        CHANNEL_COMMAND_ASMOS("asmo"),
        CHANNEL_COMMAND_ELYOS("ely"),
        CHANNEL_COMMAND_WORLD("world"),
        CHANNEL_COMMAND_BOTH("both"),
        CHANNEL_BANNED("You can't use chat channels because %s banned you for following reason: %s, remaining time: %s"),
        COMMAND_MISSING_SKILLS_STIGMAS_ADDED("%d basic skills and %d stigma skills has been added to you"),
        COMMAND_MISSING_SKILLS_ADDED("%d basic skills has been added to you"),
        USER_COMMAND_DOES_NOT_EXIST("This user command does not exist"),
        COMMAND_XP_DISABLED("Your experience gain has been disabled. Type .xpon to re-enable"),
        COMMAND_XP_ALREADY_DISABLED("Your experience gain is already disabled"),
        COMMAND_XP_ENABLED("Your experience gain has been enabled"),
        COMMAND_XP_ALREADY_ENABLED("Your experience gain is already enabled"),
        COMMAND_ADDCUBE_SYNTAX("Syntax: //addcube <player name>"),
        COMMAND_ADDCUBE_ADMIN_SUCCESS("9 cube slots successfully added to player %s!"),
        COMMAND_ADDCUBE_PLAYER_SUCCESS("Admin %s gave you a cube expansion!"),
        COMMAND_ADDCUBE_FAILURE("Cube expansion cannot be added to %s!\nReason: player cube already fully expanded."),
        DREDGION_LEVEL_TOO_LOW("Your level is too low to enter the Dredgion."),

        COMMAND_EXCHANGETOLL_SYNTAX("Syntax: .exchangetoll [ap] amount\nThis command will exchange the specified amount of Kinah (or AP if specified) into Credits for use in the Ingameshop.\nCurrent Exchange Rates:\n%d AP per Credits\n%d Kinah per Credits"),
        COMMAND_EXCHANGETOLL_AP_SYNTAX("Syntax: .exchangetoll ap amount\nThis command will exchange the specified amount of AP into Credits for use in the Ingameshop.\nCurrent Exchange Rate:\n%d AP per Credits"),
        COMMAND_EXCHANGETOLL_KINAH_SYNTAX("Syntax: .exchangetoll amount\nThis command will exchange the specified amount of Kinah into Credits for use in the Ingameshop.\nCurrent Exchange Rate:\n%d Kinah per Credits"),
        COMMAND_EXCHANGETOLL_NOT_ENOUGH_AP("You do not have that many Abyss Points!"),
        COMMAND_EXCHANGETOLL_NOT_ENOUGH_KINAH("You do not have that much kinah!"),
        COMMAND_EXCHANGETOLL_NOT_GAINING_TOLL_VIA_AP("%d is not enough Abyss Points to convert into Credits (minimum: %d)."),
        COMMAND_EXCHANGETOLL_NOT_GAINING_TOLL_VIA_KINAH("%d is not enough Kinah to convert into Credits (minimum: %d)"),
        COMMAND_EXCHANGETOLL_SUCCESS("Exchange successful.  You have gained %d Credits.");

        private String fallbackMessage;

        private CustomMessageId (String fallbackMessage)
        {
                this.fallbackMessage = fallbackMessage;
        }

        public String getFallbackMessage()
        {
                return fallbackMessage;
        }
}
