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

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.services.InstanceService;
import org.openaion.gameserver.services.TeleportService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;
import org.openaion.gameserver.world.WorldMapInstance;
import org.openaion.gameserver.world.WorldMapType;

/**
 * Admin moveto command
 * 
 * @author Dwarfpicker, Kamui
 */

public class GoTo extends AdminCommand
{

	/**
	 * Constructor.
	 */
	public GoTo()
	{
		super("goto");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_GOTO)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //goto <location name>");
			return;
		}

		if (params[0].toLowerCase().equals("poeta"))
		{
			TeleportService.teleportTo(admin, WorldMapType.POETA.getId(), 806, 1242, 119, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Poeta.");
		}

		else if (params[0].toLowerCase().equals("melponeh"))
		{
			TeleportService.teleportTo(admin, WorldMapType.POETA.getId(), 426, 1740, 119, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Melponeh Campsite.");
		}

		else if (params[0].toLowerCase().equals("verteron"))
		{
			TeleportService.teleportTo(admin, WorldMapType.VERTERON.getId(), 1643, 1500, 119, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Verteron.");
		}

		else if (params[0].toLowerCase().equals("cantas"))
		{
			TeleportService.teleportTo(admin, WorldMapType.VERTERON.getId(), 2384, 788, 102, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Cantas Coast.");
		}

		else if (params[0].toLowerCase().equals("ardus"))
		{
			TeleportService.teleportTo(admin, WorldMapType.VERTERON.getId(), 2333, 1817, 193, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Ardus Shrine.");
		}

		else if (params[0].toLowerCase().equals("pilgrims"))
		{
			TeleportService.teleportTo(admin, WorldMapType.VERTERON.getId(), 2063, 2412, 274, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Pilgrims Respite.");
		}

		else if (params[0].toLowerCase().equals("tolbas"))
		{
			TeleportService.teleportTo(admin, WorldMapType.VERTERON.getId(), 1291, 2206, 142, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Tolbas Vilage.");
		}

		else if (params[0].toLowerCase().equals("eltnen"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ELTNEN.getId(), 343, 2724, 264, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Eltnen.");
		}

		else if (params[0].toLowerCase().equals("golden"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ELTNEN.getId(), 688, 431, 332, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Golden Bough Garrison.");
		}

		else if (params[0].toLowerCase().equals("eltnenobs"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ELTNEN.getId(), 1779, 883, 422, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Eltnen Observatory.");
		}

		else if (params[0].toLowerCase().equals("novan"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ELTNEN.getId(), 947, 2215, 252, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Novan's Crossing.");
		}

		else if (params[0].toLowerCase().equals("agairon"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ELTNEN.getId(), 1921, 2045, 361, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Agairon Village.");
		}

		else if (params[0].toLowerCase().equals("kuriullu"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ELTNEN.getId(), 2411, 2724, 361, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Kuriullu Outpost.");
		}

		else if (params[0].toLowerCase().equals("theobomos"))
		{
			TeleportService.teleportTo(admin, WorldMapType.THEOMOBOS.getId(), 1398, 1557, 31, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Theobomos.");
		}

		else if (params[0].toLowerCase().equals("jamanok"))
		{
			TeleportService.teleportTo(admin, WorldMapType.THEOMOBOS.getId(), 458, 1257, 127, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Jamanok Inn.");
		}

		else if (params[0].toLowerCase().equals("meniherk"))
		{
			TeleportService.teleportTo(admin, WorldMapType.THEOMOBOS.getId(), 1396, 1560, 31, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Meniherk Excavation Camp.");
		}

		else if (params[0].toLowerCase().equals("obsvillage"))
		{
			TeleportService.teleportTo(admin, WorldMapType.THEOMOBOS.getId(), 2234, 2284, 50, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Observatory Village.");
		}

		else if (params[0].toLowerCase().equals("josnack"))
		{
			TeleportService.teleportTo(admin, WorldMapType.THEOMOBOS.getId(), 901, 2774, 62, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Josnack's Vigil.");
		}

		else if (params[0].toLowerCase().equals("anangke"))
		{
			TeleportService.teleportTo(admin, WorldMapType.THEOMOBOS.getId(), 2681, 847, 138, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Anangke Excavation Camp.");
		}

		else if (params[0].toLowerCase().equals("heiron"))
		{
			TeleportService.teleportTo(admin, WorldMapType.HEIRON.getId(), 2540, 343, 411, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Heiron.");
		}

		else if (params[0].toLowerCase().equals("heironobs"))
		{
			TeleportService.teleportTo(admin, WorldMapType.HEIRON.getId(), 1423, 1334, 175, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Heiron Observatory.");
		}

		else if (params[0].toLowerCase().equals("senemonea"))
		{
			TeleportService.teleportTo(admin, WorldMapType.HEIRON.getId(), 971, 686, 135, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Senemonea's Campsite.");
		}

		else if (params[0].toLowerCase().equals("jeiaparan"))
		{
			TeleportService.teleportTo(admin, WorldMapType.HEIRON.getId(), 1635, 2693, 115, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Jeiaparan Village.");
		}

		else if (params[0].toLowerCase().equals("changarnerk"))
		{
			TeleportService.teleportTo(admin, WorldMapType.HEIRON.getId(), 916, 2256, 157, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Changarnerk Campsite.");
		}

		else if (params[0].toLowerCase().equals("kishar"))
		{
			TeleportService.teleportTo(admin, WorldMapType.HEIRON.getId(), 1999, 1391, 118, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Kishar Observation Post.");
		}

		else if (params[0].toLowerCase().equals("arbolu"))
		{
			TeleportService.teleportTo(admin, WorldMapType.HEIRON.getId(), 170, 1662, 120, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Arbolu's Haven.");
		}

		else if (params[0].toLowerCase().equals("sanctum"))
		{
			TeleportService.teleportTo(admin, WorldMapType.SANCTUM.getId(), 1322, 1511, 568, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Sanctum.");
		}

		else if (params[0].toLowerCase().equals("ishalgen"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ISHALGEN.getId(), 529, 2449, 281, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Ishalgen.");
		}

		else if (params[0].toLowerCase().equals("anturoon"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ISHALGEN.getId(), 940, 1707, 259, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Anturoon Sentry Post.");
		}

		else if (params[0].toLowerCase().equals("altgard"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ALTGARD.getId(), 1748, 1807, 254, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Altgard.");
		}

		else if (params[0].toLowerCase().equals("basfelt"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ALTGARD.getId(), 1903, 696, 260, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Basfelt Village.");
		}

		else if (params[0].toLowerCase().equals("trader"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ALTGARD.getId(), 2680, 1024, 311, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Trader's Berth.");
		}

		else if (params[0].toLowerCase().equals("impetusiom"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ALTGARD.getId(), 2643, 1658, 324, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Heart Of Impetusiom.");
		}

		else if (params[0].toLowerCase().equals("altgardobs"))
		{
			TeleportService.teleportTo(admin, WorldMapType.ALTGARD.getId(), 1468, 2560, 299, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Altgard Observatory.");
		}

		else if (params[0].toLowerCase().equals("morheim"))
		{
			TeleportService.teleportTo(admin, WorldMapType.MORHEIM.getId(), 308, 2274, 449, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Morheim.");
		}

		else if (params[0].toLowerCase().equals("desert"))
		{
			TeleportService.teleportTo(admin, WorldMapType.MORHEIM.getId(), 634, 900, 360, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Desert Garrison.");
		}

		else if (params[0].toLowerCase().equals("slag"))
		{
			TeleportService.teleportTo(admin, WorldMapType.MORHEIM.getId(), 1772, 1662, 197, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Slag Bulwark.");
		}

		else if (params[0].toLowerCase().equals("kellan"))
		{
			TeleportService.teleportTo(admin, WorldMapType.MORHEIM.getId(), 1070, 2486, 239, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Kellan's Cabin.");
		}

		else if (params[0].toLowerCase().equals("alsig"))
		{
			TeleportService.teleportTo(admin, WorldMapType.MORHEIM.getId(), 2387, 1742, 102, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Alsig Village.");
		}

		else if (params[0].toLowerCase().equals("morheimobs"))
		{
			TeleportService.teleportTo(admin, WorldMapType.MORHEIM.getId(), 2794, 1122, 171, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Morheim Observatory.");
		}

		else if (params[0].toLowerCase().equals("halabana"))
		{
			TeleportService.teleportTo(admin, WorldMapType.MORHEIM.getId(), 2346, 2219, 127, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Halabana Outpost.");
		}

		else if (params[0].toLowerCase().equals("brusthonin"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BRUSTHONIN.getId(), 2917, 2421, 15, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Brusthonin.");
		}

		else if (params[0].toLowerCase().equals("baltasar"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BRUSTHONIN.getId(), 1413, 2013, 51, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Baltasar Hill Village.");
		}

		else if (params[0].toLowerCase().equals("lollu"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BRUSTHONIN.getId(), 840, 2016, 307, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Lollu Overlook.");
		}

		else if (params[0].toLowerCase().equals("edge"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BRUSTHONIN.getId(), 1523, 374, 231, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Edge of Torment.");
		}

		else if (params[0].toLowerCase().equals("bubu"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BRUSTHONIN.getId(), 526, 848, 76, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to BuBu Village.");
		}

		else if (params[0].toLowerCase().equals("settlers"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BRUSTHONIN.getId(), 2917, 2417, 15, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Settlers Campsite.");
		}

		else if (params[0].toLowerCase().equals("beluslan"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BELUSLAN.getId(), 398, 400, 222, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Beluslan.");
		}

		else if (params[0].toLowerCase().equals("besfer"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BELUSLAN.getId(), 533, 1866, 262, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Besfer Refugee Camp.");
		}

		else if (params[0].toLowerCase().equals("kidorun"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BELUSLAN.getId(), 1243, 819, 260, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Kidorun's Campsite.");
		}

		else if (params[0].toLowerCase().equals("redmane"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BELUSLAN.getId(), 2358, 1241, 470, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Red Mane Cavern.");
		}

		else if (params[0].toLowerCase().equals("kistenian"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BELUSLAN.getId(), 1942, 513, 412, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Camp Kistenian.");
		}

		else if (params[0].toLowerCase().equals("hoarfrost"))
		{
			TeleportService.teleportTo(admin, WorldMapType.BELUSLAN.getId(), 2431, 2063, 579, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Hoarfrost Shelter.");
		}

		else if  (params[0].toLowerCase().equals("pandaemonium"))
		{
			TeleportService.teleportTo(admin, WorldMapType.PANDAEMONIUM.getId(), 1679, 1400, 195, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Pandaemonium.");
		}

		else if (params[0].toLowerCase().equals("reshanta"))
		{
			TeleportService.teleportTo(admin, 400010000, 951, 936, 1667, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Reshanta - Isle of Roots.");
		}

		else if (params[0].toLowerCase().equals("abyss1"))
		{
			TeleportService.teleportTo(admin, 400010000, 2867, 1034, 1528, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Latis Plazza bottom Elyos.");
		}

		else if (params[0].toLowerCase().equals("abyss2"))
		{
			TeleportService.teleportTo(admin, 400010000, 1078, 2839, 1636, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Russet Plazza bottom Asmodians.");
		}

		else if (params[0].toLowerCase().equals("abyss3"))
		{
			TeleportService.teleportTo(admin, 400010000, 1596, 2952, 2943, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Top Asmodians.");
		}

		else if (params[0].toLowerCase().equals("abyss4"))
		{
			TeleportService.teleportTo(admin, 400010000, 2054, 660, 2843, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Top Elyos.");
		}

		else if (params[0].toLowerCase().equals("divinefortress") || params[0].toLowerCase().equals("divine"))
		{
			TeleportService.teleportTo(admin, 400010000, 2130, 1925, 2322, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Divine Fortress.");
		}

		else if (params[0].toLowerCase().equals("indratufortress") || params[0].toLowerCase().equals("indratu"))
		{
			TeleportService.teleportTo(admin, 310090000, getInstanceId(310090000, admin), 562, 335, 1015, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Indratu Fortress.");
		}

		else if (params[0].toLowerCase().equals("azoturanfortress") || params[0].toLowerCase().equals("azoturan"))
		{
			TeleportService.teleportTo(admin, 310100000, getInstanceId(310100000, admin), 458, 428, 1039, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Azoturan Fortress.");
		}

		else if (params[0].toLowerCase().equals("karamatis0"))
		{
			TeleportService.teleportTo(admin, 300010000, getInstanceId(300010000, admin), 270, 200, 206, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Karamatis 0.");
		}

		else if (params[0].toLowerCase().equals("karamatis1"))
		{
			TeleportService.teleportTo(admin, 300010000, getInstanceId(300010000, admin), 270, 200, 206, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Karamatis 1.");
		}

		else if (params[0].toLowerCase().equals("karamatis2"))
		{
			TeleportService.teleportTo(admin, 300010000, getInstanceId(300010000, admin), 270, 200, 206, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Karamatis 2.");
		}

		else if (params[0].toLowerCase().equals("aerdina"))
		{
			TeleportService.teleportTo(admin, 300010000, getInstanceId(300010000, admin), 270, 200, 206, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Aerdina.");
		}

		else if (params[0].toLowerCase().equals("gerania"))
		{
			TeleportService.teleportTo(admin, 300010000, getInstanceId(300010000, admin), 270, 200, 206, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Gerania.");
		}

		else if (params[0].toLowerCase().equals("biolab") || params[0].toLowerCase().equals("aetherlab"))
		{
			TeleportService.teleportTo(admin, 310050000, getInstanceId(310050000, admin), 225, 244, 133, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Bio Experiment Laboratory.");
		}

		else if (params[0].toLowerCase().equals("sliver"))
		{
			TeleportService.teleportTo(admin, 310070000, getInstanceId(310070000, admin), 247, 249, 1392, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Sliver of Darkness.");
		}

		else if (params[0].toLowerCase().equals("sanctumarena"))
		{
			TeleportService.teleportTo(admin, 310080000, getInstanceId(310080000, admin), 275, 242, 159, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Sanctum Underground Arena.");
		}

		else if (params[0].toLowerCase().equals("trinielarena"))
		{
			TeleportService.teleportTo(admin, 320090000, getInstanceId(320090000, admin), 275, 239, 159, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Triniel Underground Arena.");
		}

		else if (params[0].toLowerCase().equals("ataxiar1"))
		{
			TeleportService.teleportTo(admin, 320010000, getInstanceId(320010000, admin), 229, 237, 206, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Narsass 1.");
		}

		else if (params[0].toLowerCase().equals("ataxiar2"))
		{
			TeleportService.teleportTo(admin, 320020000, getInstanceId(320020000, admin), 229, 237, 206, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Narsass 2.");
		}

		else if (params[0].toLowerCase().equals("bregirun"))
		{
			TeleportService.teleportTo(admin, 320030000, getInstanceId(320030000, admin), 264, 214, 210, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Bregirun.");
		}

		else if (params[0].toLowerCase().equals("nidalber"))
		{
			TeleportService.teleportTo(admin, 320040000, getInstanceId(320040000, admin), 264, 214, 210, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Nidalber.");
		}

		else if (params[0].toLowerCase().equals("skytemple"))
		{
			TeleportService.teleportTo(admin, 320050000, getInstanceId(320050000, admin), 177, 229, 536, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Sky Temple of Arkanis.");
		}

		else if (params[0].toLowerCase().equals("firetemple"))
		{
			TeleportService.teleportTo(admin, 320100000, getInstanceId(320100000, admin), 144, 312, 123, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Fire Temple.");
		}

		else if (params[0].toLowerCase().equals("space"))
		{
			TeleportService.teleportTo(admin, 320070000, getInstanceId(320070000, admin), 246, 246, 125, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Space of Destiny.");
		}

		else if (params[0].toLowerCase().equals("prison1"))
		{
			TeleportService.teleportTo(admin, 510010000, 256, 256, 49, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to LF Prison.");
		}

		else if (params[0].toLowerCase().equals("prison2"))
		{
			TeleportService.teleportTo(admin, 520010000, 256, 256, 49, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to DF Prison.");
		}

		else if (params[0].toLowerCase().equals("test1"))
		{
			TeleportService.teleportTo(admin, 900100000, 196, 187, 20, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Test Giant Monster.");
		}

		else if (params[0].toLowerCase().equals("test2"))
		{
			TeleportService.teleportTo(admin, 900020000, 144, 136, 20, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Test Basic.");
		}

		else if (params[0].toLowerCase().equals("test3"))
		{
			TeleportService.teleportTo(admin, 900030000, 228, 171, 49, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Test Server.");
		}

		else if (params[0].toLowerCase().equals("steelrake"))
		{
			TeleportService.teleportTo(admin, 300100000, 237, 505, 949, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Steel Rake.");
		}

		else if (params[0].toLowerCase().equals("inggison"))
		{
			TeleportService.teleportTo(admin, 210050000, 1335, 276, 590, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Inggison.");
		}
		else if (params[0].toLowerCase().equals("ufob"))
		{
			TeleportService.teleportTo(admin, 210050000, 382, 951, 460, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Underground Fortress Observation Base.");
		}
		else if (params[0].toLowerCase().equals("soteria"))
		{
			TeleportService.teleportTo(admin, 210050000, 2713, 1477, 382, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Soteria Sanctuary Village.");
		}
		else if (params[0].toLowerCase().equals("hanarkand"))
		{
			TeleportService.teleportTo(admin, 210050000, 1892, 1748, 327, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Hanarkand Plain.");
		}

		else if (params[0].toLowerCase().equals("gelkmaros"))
		{
			TeleportService.teleportTo(admin, 220070000, 1763, 2911, 554, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Gelkmaros.");
		}
		else if (params[0].toLowerCase().equals("subterranea"))
		{
			TeleportService.teleportTo(admin, 220070000, 2503, 2147, 464, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Subterranea Observation Post.");
		}
		else if (params[0].toLowerCase().equals("rhonnam"))
		{
			TeleportService.teleportTo(admin, 220070000, 845, 1737, 354, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Rhonnam Refugee Village.");
		}
		else if (params[0].toLowerCase().equals("kaisinel"))
		{
			TeleportService.teleportTo(admin, 110020000, 2155, 1567, 1205, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Cloister of Kaisinel.");
		}
		else if (params[0].toLowerCase().equals("marchutan"))
		{
			TeleportService.teleportTo(admin, 120020000, 1557, 1429, 266, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Convent of Marchutan.");
		}
		else if (params[0].toLowerCase().equals("udas"))
		{
			TeleportService.teleportTo(admin, 300150000, getInstanceId(300150000, admin), 637, 657, 134, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Udas Temple.");
		}
		else if (params[0].toLowerCase().equals("silentera"))
		{
			TeleportService.teleportTo(admin, 600010000, 583, 767, 300, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Silentera Canyon.");
		}
		else if (params[0].toLowerCase().equals("adma"))
		{
			TeleportService.teleportTo(admin, 320130000, getInstanceId(320130000, admin), 450, 200, 168, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Adma Stronghold.");
		}
		else if (params[0].toLowerCase().equals("sulfur"))
		{
			TeleportService.teleportTo(admin, 300060000, getInstanceId(300060000, admin), 462, 345, 163, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Sulfur tree nest.");
		}
		else if (params[0].toLowerCase().equals("asteria"))
		{
			TeleportService.teleportTo(admin, 300050000, getInstanceId(300050000, admin), 469, 568, 202, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Asteria chamber.");
		}
		else if (params[0].toLowerCase().equals("rightwing"))
		{
			TeleportService.teleportTo(admin, 300090000, getInstanceId(300090000, admin), 263, 386, 103, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Right Wing Chamber.");
		}
		else if (params[0].toLowerCase().equals("leftwing"))
		{
			TeleportService.teleportTo(admin, 300080000, getInstanceId(300080000, admin), 672, 606, 321, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Left Wing Chamber.");
		}
		else if (params[0].toLowerCase().equals("miren"))
		{
			TeleportService.teleportTo(admin, 300130000, getInstanceId(300130000, admin), 527, 120, 176, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Miren Chamber.");
		}
		else if (params[0].toLowerCase().equals("kysis"))
		{
			TeleportService.teleportTo(admin, 300120000, getInstanceId(300120000, admin), 528, 121, 176, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Kysis Chamber.");
		}
		else if (params[0].toLowerCase().equals("krotan"))
		{
			TeleportService.teleportTo(admin, 300140000, getInstanceId(300140000, admin), 528, 109, 176, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Krotan Chamber.");
		}
		else if (params[0].toLowerCase().equals("roah"))
		{
			TeleportService.teleportTo(admin, 300070000, getInstanceId(300070000, admin), 504, 396, 94, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Chamber of Roah.");
		}
		else if (params[0].toLowerCase().equals("alquimia"))
		{
			TeleportService.teleportTo(admin, 320110000, getInstanceId(320110000, admin), 603, 527, 200, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Alquimia Research Center.");
		}
		else if (params[0].toLowerCase().equals("nochsana"))
		{
			TeleportService.teleportTo(admin, 300030000, getInstanceId(300030000, admin), 513, 668, 331, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Nochsana Training Camp.");
		}
		else if (params[0].toLowerCase().equals("draupnir"))
		{
			TeleportService.teleportTo(admin, 320080000, getInstanceId(320080000, admin), 491, 373, 622, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Draupnir Cave.");
		}
		else if (params[0].toLowerCase().equals("darkpoeta"))
		{
			TeleportService.teleportTo(admin, 300040000, getInstanceId(300040000, admin), 1214, 412, 140, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Dark Poeta.");
		}
		else if (params[0].toLowerCase().equals("fireroom"))
		{
			TeleportService.teleportTo(admin, 320050000, getInstanceId(320050000, admin), 102, 24, 611, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Sky Temple Fire Room.");
		}
		else if (params[0].toLowerCase().equals("theolab"))
		{
			TeleportService.teleportTo(admin, 310110000, getInstanceId(310110000, admin), 477, 201, 170, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Theobomos Lab.");
		}
		else if (params[0].toLowerCase().equals("steelrake"))
		{
			TeleportService.teleportTo(admin, 300100000, getInstanceId(300100000, admin), 237, 506, 948, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Steel Rake.");
		}
		else if (params[0].toLowerCase().equals("steelrakelower"))
		{
			TeleportService.teleportTo(admin, 300100000, getInstanceId(300100000, admin), 283, 453, 903, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Steel Rake lower deck.");
		}
		else if (params[0].toLowerCase().equals("steelrakemiddle"))
		{
			TeleportService.teleportTo(admin, 300100000, getInstanceId(300100000, admin), 283, 453, 953, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Steel Rake middle deck.");
		}
		else if (params[0].toLowerCase().equals("taloc"))
		{
			TeleportService.teleportTo(admin, 300190000, getInstanceId(300190000, admin), 200, 214, 1099, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Taloc's Hollow.");
		}
		else if (params[0].toLowerCase().equals("haramel"))
		{
			TeleportService.teleportTo(admin, 300200000, getInstanceId(300200000, admin), 176, 21, 144, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Haramel.");
		}
		else if (params[0].toLowerCase().equals("beshmundir"))
		{
			TeleportService.teleportTo(admin, 300170000, getInstanceId(300170000, admin), 1477, 237, 243, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Beshmundir Temple.");
		}
		else if (params[0].toLowerCase().equals("udaslower"))
		{
			TeleportService.teleportTo(admin, 300160000, getInstanceId(300160000, admin), 1146, 277, 116, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Lower Udas Temple.");
		}
		else if (params[0].toLowerCase().equals("splinter"))
		{
			TeleportService.teleportTo(admin, 300220000, getInstanceId(300220000, admin), 704, 153, 453, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Abyssal Splinter.");
                }
                else if (params[0].toLowerCase().equals("kaisinel2")) 
           	{	
                        TeleportService.teleportTo(admin, 110070000, 504, 287, 128, 0);
            		PacketSendUtility.sendMessage(admin, "Teleported to Kaisinel Academy");
       		} 
		else if (params[0].toLowerCase().equals("marchutan2"))
            	{	
                        TeleportService.teleportTo(admin, 120080000, 575, 248, 94, 0);
            		PacketSendUtility.sendMessage(admin, "Teleported to Marchutan Priory");
        	} 
		else if (params[0].toLowerCase().equals("esoterrace"))
           	{	
                        TeleportService.teleportTo(admin, 300250000, 363, 535, 325, 0);
           		PacketSendUtility.sendMessage(admin, "Teleported to Esoterrace");
       		} 
		else if (params[0].toLowerCase().equals("empyream"))
           	{	
                        TeleportService.teleportTo(admin, 300300000, 1787, 797, 470, 0);
            		PacketSendUtility.sendMessage(admin, "Teleported to Empyream Crucible");
		}
		else if (params[0].toLowerCase().equals("kromede"))
		{
			TeleportService.teleportTo(admin, 300230000, getInstanceId(300230000, admin), 248, 244, 189, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Kromede Trial.");
		}
		else if (params[0].toLowerCase().equals("dredgion"))
		{
			TeleportService.teleportTo(admin, 300110000, getInstanceId(300110000, admin), 414, 193, 431, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Dredgion Instance.");
		}
		else if (params[0].toLowerCase().equals("chantra"))
		{
			TeleportService.teleportTo(admin, 300210000, getInstanceId(300210000, admin), 414, 193, 431, 0);
			PacketSendUtility.sendMessage(admin, "Teleported to Chantra Dredgion.");
		}
		else 
			PacketSendUtility.sendMessage(admin, "Target location was not found!");
	}
	
	int getInstanceId(int worldId, Player player)
	{
		if (player.getWorldId() == worldId)
		{
			WorldMapInstance registeredInstance = InstanceService.getRegisteredInstance(worldId, player.getObjectId());
			if (registeredInstance != null)
				return registeredInstance.getInstanceId();
		}
		WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(worldId);
		InstanceService.registerPlayerWithInstance(newInstance, player);
		return newInstance.getInstanceId();
	}
}
