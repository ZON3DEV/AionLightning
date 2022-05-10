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
package admincommands;

import java.util.Collection;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.dao.PetitionDAO;
import org.openaion.gameserver.dao.PlayerDAO;
import org.openaion.gameserver.model.PetitionType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.services.MailService;
import org.openaion.gameserver.services.PetitionService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;
import org.openaion.gameserver.world.World;


/**
 * @author Sylar 
 */
public class Petition extends AdminCommand
{
	public Petition()
	{
		super("petition");
	}

	@Override
	public int getSplitSize()
	{
		return 3;
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_PETITION)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		// Send ticket general info
		if (params == null || params.length == 0)
		{
			Collection<org.openaion.gameserver.model.Petition> petitions = PetitionService.getInstance().getRegisteredPetitions();
			synchronized (petitions)
			{
				org.openaion.gameserver.model.Petition[] petitionsArray = petitions.toArray(new org.openaion.gameserver.model.Petition[0]);
				PacketSendUtility.sendMessage(admin, petitionsArray.length + " unprocessed petitions.");
				if (petitionsArray.length < 5)
				{
					PacketSendUtility.sendMessage(admin, "== " + petitionsArray.length + " active petition(s) to reply ==");
					for(int i=0; i < petitionsArray.length; i++)
					{
						PacketSendUtility.sendMessage(admin, petitionsArray[i].getPetitionId() + " | " + petitionsArray[i].getTitle());
					}
				}
				else
				{
					PacketSendUtility.sendMessage(admin, "== 5 active petitions to reply ==");
					for (int i=0; i < 5; i++)
					{
						PacketSendUtility.sendMessage(admin, petitionsArray[i].getPetitionId() + " | " + petitionsArray[i].getTitle());
					}
				}
			}
			return;
		}

		int petitionId = 0;

		try
		{
			petitionId = Integer.parseInt(params[0]);
		}
		catch (NumberFormatException nfe)
		{
			PacketSendUtility.sendMessage(admin, "Invalid petition id.");
			return;
		}

		org.openaion.gameserver.model.Petition petition = DAOManager.getDAO(PetitionDAO.class).getPetitionById(petitionId);

		if (petition == null)
		{
			PacketSendUtility.sendMessage(admin, "There is no petition with id #" + petitionId);
			return;
		}

		String petitionPlayer = "";
		boolean isOnline;

		if (World.getInstance().findPlayer(petition.getPlayerObjId()) != null)
		{
			petitionPlayer = World.getInstance().findPlayer(petition.getPlayerObjId()).getName();
			isOnline = true;
		}
		else
		{
			petitionPlayer = DAOManager.getDAO(PlayerDAO.class).getPlayerNameByObjId(petition.getPlayerObjId());
			isOnline = false;
		}

		StringBuilder message = new StringBuilder();

		// Read petition
		if (params.length == 1)
		{
			message.append("== Petition #" + petitionId + " ==\n");
			message.append("Player: " + petitionPlayer + " (");
			if (isOnline)
				message.append("Online");
			else
				message.append("Offline");
			message.append(")\n");
			message.append("Type: " + getHumanizedValue(petition.getPetitionType()) + "\n");
			message.append("Title: " + petition.getTitle() + "\n");
			message.append("Text: " + petition.getContentText() + "\n");
			message.append("= Additional Data =\n");
			message.append(getFormattedAdditionalData(petition.getPetitionType(), petition.getAdditionalData()));
		}
		// Delete
		else if (params.length == 2 && params[1].equals("delete"))
		{
			PetitionService.getInstance().deletePetition(petition.getPlayerObjId());
			PacketSendUtility.sendMessage(admin, "Petition #" + petitionId + " deleted.");
		}
		// Reply
		else if (params.length == 3 && params[1].equals("reply"))
		{
			String replyMessage = params[2];
			if (replyMessage.equals(""))
			{
				PacketSendUtility.sendMessage(admin, "You must specify a reply to that petition");
				return;
			}
			MailService.getInstance().sendMail(admin, petitionPlayer, "GM-Re:" + petition.getTitle(), replyMessage, 0, 0, 0, false, true);
			PetitionService.getInstance().setPetitionReplied(petitionId);
			PacketSendUtility.sendMessage(admin, "Your reply has been sent to " + petitionPlayer + ". Petition is now closed.");
		}
		PacketSendUtility.sendMessage(admin, message.toString());
	}

	private String getHumanizedValue(PetitionType type)
	{
		String result = "";
		switch (type)
		{
			case CHARACTER_STUCK:
				result = "Character Stuck";
				break;
			case CHARACTER_RESTORATION:
				result = "Character Restoration";
				break;
			case BUG:
				result = "Bug";
				break;
			case QUEST:
				result = "Quest";
				break;
			case UNACCEPTABLE_BEHAVIOR:
				result = "Unacceptable Behavior";
				break;
			case SUGGESTION:
				result = "Suggestion";
				break;
			case INQUIRY:
				result = "Inquiry about the game";
				break;
			default:
				result = "Unknown";
		}
		return result;
	}

	private String getFormattedAdditionalData(PetitionType type, String additionalData)
	{
		String result = "";
		switch(type)
		{
			case CHARACTER_STUCK:
				result = "Character Location: " + additionalData;
				break;
			case CHARACTER_RESTORATION:
				result = "Category: " + additionalData;
				break;
			case BUG:
				String[] bugData = additionalData.split("/");
				result = "Time Occured: " + bugData[0] + "\n";
				result += "Zone and Coords: " + bugData[1];
				if (bugData.length > 2)
					result += "\nHow to Replicate: " + bugData[2];
				break;
			case QUEST:
				result = "Quest Title: " + additionalData;
				break;
			case UNACCEPTABLE_BEHAVIOR:
				String[] bData = additionalData.split("/");
				result = "Time Occured: " + bData[0] + "\n";
				result += "Character Name: " + bData[1] + "\n";
				result += "Category: " + bData[2];
				break;
			case SUGGESTION:
				result = "Category: " + additionalData;
				break;
			case INQUIRY:
				result = "Petition Category: " + additionalData;
				break;
			default:
				result = additionalData;
		}
		return result;
	}
}
