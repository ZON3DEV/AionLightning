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
package admincommands;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.dao.PlayerAppearanceDAO;
import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.PlayerAppearance;
import org.openaion.gameserver.model.templates.preset.PresetTemplate;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;


/**
 * @author Rolandas
 *
 */
public class ApplyPreset extends AdminCommand
{
	public ApplyPreset()
	{
		super("preset");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_APPLY_PRESET)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		
		if (params.length == 0 || params.length > 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //preset <name>");
			return;
		}
		
		VisibleObject target = admin.getTarget();
		Player player = null;
	
		if (target == null)
			player = admin;
		else if (!(target instanceof Player))
		{
			PacketSendUtility.sendMessage(admin, "Presets can be applied only on players!");
			return;		
		}
		else
		{
			player = (Player)target;
		}
		
		String presetName = params[0];

		PresetTemplate template = DataManager.CUSTOM_PRESET_DATA.getPresetTemplate(presetName);
		if (template == null)
		{
			PacketSendUtility.sendMessage(admin, "No such preset!");
			return;
		}
		if (template.getGender().ordinal() != player.getCommonData().getGender().ordinal() ||
			template.getRace().ordinal() != player.getCommonData().getRace().ordinal())
		{
			PacketSendUtility.sendMessage(admin, "Preset can not be applied on current gender or race!");
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

		DAOManager.getDAO(PlayerAppearanceDAO.class).store(player);

		player.clearKnownlist();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		player.updateKnownlist();
	}
	
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
