/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.PersistentState;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import org.openaion.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;


/**
 * @author loleron
 */
 
public class Dye extends AdminCommand
{

	public Dye()
	{
		super("dye");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_DYE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		Player target = null;
		VisibleObject creature = admin.getTarget();

		if (admin.getTarget() instanceof Player)
		{
			target = (Player) creature;
		}

		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "You should select a player as target first!");
			return;
		}

		if (params.length == 0 || params.length > 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //dye <dye color | hex color | remove>");
			return;
		}

		String color = "";
		if (params.length == 2) { 
			if (params[1].equalsIgnoreCase("petal")) { color = params[0]; }
			else { color = params[0] + " " + params[1]; }
		}
		else { color = params[0]; }

		int rgb = 0;
		int bgra = 0;

			 if (color.equalsIgnoreCase("turquoise"))		{ color = "198d94"; }	//169200001, 169201001
		else if (color.equalsIgnoreCase("blue"))			{ color = "1f87f5"; }	//169200002, 169201002
		else if (color.equalsIgnoreCase("brown"))			{ color = "66250e"; }	//169200003, 169201003
		else if (color.equalsIgnoreCase("purple"))			{ color = "c38df5"; }	//169200004, 169201004
		else if (color.equalsIgnoreCase("red"))				{ color = "c22626"; }	//169200005, 169201005, 169220001, 169230001, 169231001
		else if (color.equalsIgnoreCase("white"))			{ color = "ffffff"; }	//169200006, 169201006, 169220002, 169231002
		else if (color.equalsIgnoreCase("black"))			{ color = "000000"; }	//169200007, 169201007, 169230008, 169231008
		else if (color.equalsIgnoreCase("orange"))			{ color = "e36b00"; }	//169201008, 169220004, 169230009, 169231009
		else if (color.equalsIgnoreCase("dark purple"))		{ color = "440b9a"; }	//169201009, 169220005, 169230007, 169231003
		else if (color.equalsIgnoreCase("pink"))			{ color = "d60b7e"; }	//169201010, 169220006, 169230010, 169231010
		else if (color.equalsIgnoreCase("mustard"))			{ color = "fcd251"; }	//169201011, 169220007, 169230004, 169231004
		else if (color.equalsIgnoreCase("green tea"))		{ color = "61bb4f"; }	//169201012, 169220008, 169230003, 169231005
		else if (color.equalsIgnoreCase("green olive"))		{ color = "5f730e"; }	//169201013, 169220009, 169230005, 169231006
		else if (color.equalsIgnoreCase("dark blue"))		{ color = "14398b"; }	//169201014, 169220010, 169230006, 169231007
		else if (color.equalsIgnoreCase("light purple"))	{ color = "80185d"; }	//169230011
		else if (color.equalsIgnoreCase("wiki"))			{ color = "85e831"; }	//169240001
		else if (color.equalsIgnoreCase("omblic"))			{ color = "ff5151"; } 	//169240002
		else if (color.equalsIgnoreCase("meon"))			{ color = "afaf26"; }	//169240003
		else if (color.equalsIgnoreCase("ormea"))			{ color = "ffaa11"; }	//169240004
		else if (color.equalsIgnoreCase("tange"))			{ color = "bd5fff"; }	//169240005
		else if (color.equalsIgnoreCase("ervio"))			{ color = "3bb7fe"; }	//169240006
		else if (color.equalsIgnoreCase("lunime"))			{ color = "c7af27"; }	//169240007
		else if (color.equalsIgnoreCase("vinna"))			{ color = "052775"; }	//169240008
		else if (color.equalsIgnoreCase("kirka"))			{ color = "ca84ff"; }	//169240009
		else if (color.equalsIgnoreCase("brommel"))			{ color = "c7af27"; }	//169240010
		else if (color.equalsIgnoreCase("pressa"))			{ color = "ff9d29"; }	//169240011
		else if (color.equalsIgnoreCase("merone"))			{ color = "8df598"; }	//169240012
		else if (color.equalsIgnoreCase("kukar"))			{ color = "ffff96"; }	//169240013
		else if (color.equalsIgnoreCase("leopis"))			{ color = "31dfff"; }	//169240014

		try
		{
			rgb = Integer.parseInt(color, 16);
			bgra = 0xFF | ((rgb & 0xFF) << 24) | ((rgb & 0xFF00) << 8) | ((rgb & 0xFF0000) >>> 8);
		}

		catch (NumberFormatException e)
		{
			if (!color.equalsIgnoreCase("remove")) {
				PacketSendUtility.sendMessage(admin, color + " is not a valid color parameter!");
				return;
			}
		}

		for (Item targetItem : target.getEquipment().getEquippedItemsWithoutStigma())
		{
			if (color.equals("remove"))
			{
				targetItem.setItemColor(0);
			}
			else
			{
				targetItem.setItemColor(bgra);
			}
			PacketSendUtility.sendPacket(target, new SM_UPDATE_ITEM(targetItem));
		}
		PacketSendUtility.broadcastPacket(target, new SM_UPDATE_PLAYER_APPEARANCE(target.getObjectId(), target.getEquipment().getEquippedItemsWithoutStigma()), true);
		target.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
		if (target.getObjectId() != admin.getObjectId()) { PacketSendUtility.sendMessage(target, "You have been dyed by " + admin.getName() + "!"); }
		PacketSendUtility.sendMessage(admin, "Dyed " + target.getName() + " successfully!");
	}
}
