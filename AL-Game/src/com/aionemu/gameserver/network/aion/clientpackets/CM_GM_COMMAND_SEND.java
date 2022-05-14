/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gm.GmPanelCommands;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.gmhandler.*;
import com.aionemu.gameserver.network.aion.gmhandler.CmdLevelUpDown.LevelUpDownState;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Magenik
 * @author Antraxx
 * @author Ever
 */
public class CM_GM_COMMAND_SEND extends AionClientPacket {

	private String cmd = "";
	private String params = "";
	private Player admin;

	public CM_GM_COMMAND_SEND(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		admin = getConnection().getActivePlayer();
		String clientCmd = readS();

		int index = clientCmd.indexOf(" ");

		// System.out.println("GMCMD: " + clientCmd);
		// System.out.println("index: " + index);

		cmd = clientCmd;
		if (index >= 0) {
			cmd = clientCmd.substring(0, index).toUpperCase();
			params = clientCmd.substring(index + 1);
		}
		// System.out.println("cmd   : " + cmd);
		// System.out.println("params: " + params);
	}

	@Override
	protected void runImpl() {
		if (admin == null) {
			return;
		}

		// check accesslevel - not needed but to be sure
		if (admin.getAccessLevel() < AdminConfig.GM_PANEL) {
			return;
		}

		switch (GmPanelCommands.getValue(cmd)) {
			case REMOVE_SKILL_DELAY_ALL:
				// new CmdRemoveSkillDelayAll(admin); // Buggy
				break;
			case ITEMCOOLTIME:
				new CmdItemCoolTime(admin);
				break;
			case ATTRBONUS:
				new CmdAttrBonus(admin, params);
				break;
			case TELEPORTTO:
				new CmdTeleportTo(admin, params);
				break;
			case TELEPORT_TO_NAMED:
				new CmdTeleportToNamed(admin, params);
				break;
			case RESURRECT:
				new CmdResurrect(admin, "");
				break;
			case INVISIBLE:
				new CmdInvisible(admin, "");
				break;
			case VISIBLE:
				new CmdVisible(admin, "");
				break;
			case LEVELDOWN:
				new CmdLevelUpDown(admin, params, LevelUpDownState.DOWN);
				break;
			case LEVELUP:
				new CmdLevelUpDown(admin, params, LevelUpDownState.UP);
				break;
			case WISHID:
				new CmdWishId(admin, params);
				break;
			case DELETECQUEST:
				new CmdDeleteQuest(admin, params);
				break;
			case GIVETITLE:
				new CmdGiveTitle(admin, params);
				break;
			case DELETE_ITEMS:
				PacketSendUtility.sendMessage(admin, "Invalid command: " + cmd.toString());
				break;
			case CHANGECLASS:
				new CmdChangeClass(admin, params);
				break;
			case CLASSUP:
				new CmdChangeClass(admin, params);
				break;
			case SETINVENTORYGROWTH:
				new CmdCube(admin, params);
				break;
            case ADDSKILL:
                new CmdAddSkill(admin, params);
                break;
            case WISH:
                new CmdWish(admin, params);
                break;
            case DELETESKILL:
                new CmdDeleteSkill(admin, params);
                break;
            case ENDQUEST:
                new CmdDeleteQuest(admin, params);
                break;
            case FREEFLY:
                PacketSendUtility.sendMessage(admin, "Free Fly Enabled!");
                break;
            case ADDQUEST:
                //new CmdAddQuest(admin, params);
                //break;
			case SKILLPOINT:
			case COMBINESKILL:
			case ENCHANT100:
				PacketSendUtility.sendMessage(admin, "Invalid command: " + cmd.toString());
				break;
			default:
				PacketSendUtility.sendMessage(admin, "Invalid command: " + cmd.toString());
				break;
		}
	}
}
