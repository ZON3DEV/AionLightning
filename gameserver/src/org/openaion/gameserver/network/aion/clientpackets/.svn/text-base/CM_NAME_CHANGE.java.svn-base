/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 *     Aion-unique is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Aion-unique is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.network.aion.clientpackets;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.LegionDAO;
import org.openaion.gameserver.dao.PlayerAppearanceDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.itemengine.actions.CosmeticAction;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.player.Friend;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerAppearance;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.model.templates.item.ItemCategory;
import org.openaion.gameserver.model.templates.preset.PresetTemplate;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.serverpackets.SM_LEGION_UPDATE_MEMBER;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.LegionService;
import org.openaion.gameserver.services.PlayerService;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author Sylar, ginho1
 */
public class CM_NAME_CHANGE extends AionClientPacket 
{
	private static final Logger log = Logger.getLogger(CM_NAME_CHANGE.class);

	private int action;
	private int itemObjId;
	private String newName;

	public CM_NAME_CHANGE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		action = readC(); // 0: Change Char Name / 1: Change Legion Name / 2: Change Hair and Skin Color
		readC();
		readH();
		itemObjId = readD();

		if(action != 2)
			newName = readS();
	}

	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		Item ticket = player.getInventory().getItemByObjId(itemObjId);
		if (ticket == null)
			return;

		switch(action)
		{
			// Change Player Name
			case 0:
				if(!PlayerService.isValidName(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400151));
					return;
				}
				if(player.getName().equals(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400153));
					return;
				}
				if(!PlayerService.isFreeName(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400155));
					return;
				}
				if (ticket.getItemTemplate().getItemCategory() != ItemCategory.CHANGE_CHARACTER_NAME)
				{
					log.info("[AUDIT] " + player.getName() + " Trying to change name without ticket.");
					return;
				}
				if(!player.getInventory().removeFromBagByObjectId(itemObjId, 1))
					return;

				player.getCommonData().setName(newName);
				PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
				Iterator<Friend> knownFriends = player.getFriendList().iterator();
				player.getKnownList().doOnAllPlayers(new Executor<Player>(){
					@Override
					public boolean run (Player p)
					{
						PacketSendUtility.sendPacket(p, new SM_PLAYER_INFO(player, player.isEnemyPlayer(p)));
						return true;
					}
					}, true);

				while(knownFriends.hasNext())
				{
					Friend nextObject = knownFriends.next();
					if(nextObject.getPlayer() != null)
					{
						if(nextObject.getPlayer().isOnline())
							PacketSendUtility.sendPacket(nextObject.getPlayer(), new SM_PLAYER_INFO(player, false));
					}
				}
				if(player.isLegionMember())
				{
					PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
				}
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400157, newName));

			break;
			// Change Legion Name
			case 1:
				if(!player.isLegionMember())
					return;
				if(!LegionService.getInstance().isValidName(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400152));
					return;
				}
				if(player.getLegion().getLegionName().equals(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400154));
					return;
				}
				if(DAOManager.getDAO(LegionDAO.class).isNameUsed(newName))
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400156));
					return;
				}

				if (ticket.getItemTemplate().getItemCategory() != ItemCategory.CHANGE_LEGION_NAME)
				{
					log.info("[AUDIT] " + player.getName() + " Trying to change legion name without ticket.");
					return;
				}
				if(!player.getInventory().removeFromBagByObjectId(itemObjId, 1))
					return;

				LegionService.getInstance().setLegionName(player.getLegion(), newName, true);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400158, newName));

			break;
			// Change Hair, Skin, Voice, Decoration, Tattoo etc
			case 2:
				if (ticket.getItemTemplate().getActions() == null ||
					ticket.getItemTemplate().getActions().getCosmeticActions() == null)
					return;
				
				if(!player.getInventory().removeFromBagByObjectId(itemObjId, 1))
					return;

				CosmeticAction ca = ticket.getItemTemplate().getActions().getCosmeticActions().get(0);

				// first apply preset and then overrides
				if (ca.getPresetName() != null)
				{
					PresetTemplate template = DataManager.CUSTOM_PRESET_DATA.getPresetTemplate(ca.getPresetName());
					if (template == null)
						return;
					if (template.getGender().ordinal() != player.getCommonData().getGender().ordinal() ||
						template.getRace().ordinal() != player.getCommonData().getRace().ordinal())
					{
						log.info("[AUDIT] " + player.getName() + " Trying to change appearance without ticket.");
						return;
					}
					PlayerAppearance.loadDetails(player.getPlayerAppearance(), template.getDetail());
					if (template.getFaceType() > 0)
						player.getPlayerAppearance().setFace(template.getFaceType());
					if (template.getHairType() > 0)
						player.getPlayerAppearance().setHair(template.getHairType());
					if (template.getHairRGB() != null)
						player.getPlayerAppearance().setHairRGB(getDyeColor(template.getHairRGB()));
					if (template.getLipsRGB() != null)
						player.getPlayerAppearance().setLipRGB(getDyeColor(template.getLipsRGB()));
					if (template.getSkinRGB() != null)
						player.getPlayerAppearance().setSkinRGB(getDyeColor(template.getSkinRGB()));
					if (template.getHeight() > 0)
						player.getPlayerAppearance().setHeight(template.getHeight());
				}
				
				if (ca.getEyesColor() != null)
					player.getPlayerAppearance().setEyeRGB(getDyeColor(ca.getEyesColor()));
				if (ca.getFaceColor() != null)
					player.getPlayerAppearance().setSkinRGB(getDyeColor(ca.getFaceColor()));
				if (ca.getHairColor() != null)
					player.getPlayerAppearance().setHairRGB(getDyeColor(ca.getHairColor()));
				if (ca.getLipsColor() != null)
					player.getPlayerAppearance().setLipRGB(getDyeColor(ca.getLipsColor()));
				if (ca.getFaceType() > 0)
					player.getPlayerAppearance().setFace(ca.getFaceType());
				if (ca.getHairType() > 0)
					player.getPlayerAppearance().setHair(ca.getHairType());
				if (ca.getMakeupType() > 0)
					player.getPlayerAppearance().setDecoration(ca.getMakeupType());
				if (ca.getTattooType() > 0)
					player.getPlayerAppearance().setTattoo(ca.getTattooType());
				if (ca.getVoiceType() > 0)
					player.getPlayerAppearance().setVoice(ca.getVoiceType());

				DAOManager.getDAO(PlayerAppearanceDAO.class).store(player);

				player.clearKnownlist();
				PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
				player.updateKnownlist();
			break;
		}
	}

	/**
	 * @param color
	 * @return integer value in BGR
	 */
	private int getDyeColor(String hexRGB)
	{
		int rgb = Integer.parseInt(hexRGB, 16);
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = (rgb >> 0) & 0xFF;
		int bgr = (blue << 16) | (green << 8) | (red << 0);
		return bgr;
	}
}
