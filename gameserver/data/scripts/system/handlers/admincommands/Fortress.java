package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.configs.main.SiegeConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.siege.SiegeLocation;
import org.openaion.gameserver.model.siege.SiegeRace;
import org.openaion.gameserver.model.siege.SiegeType;
import org.openaion.gameserver.services.SiegeService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author ginho1
 */
public class Fortress extends AdminCommand
{
	/**
	 * Constructor
	 */
	public Fortress()
	{
		super("fortress");
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
			PacketSendUtility.sendMessage(admin, "syntax //fortress <capture | list>");
			return;
		}

		String cmd = params[0].toLowerCase();
		if (("capture").startsWith(cmd))
		{
			processCapture(admin, params);
		}
		else if (("list").startsWith(cmd))
		{
			processList(admin, params);
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "syntax //fortress <capture | list>");
			return;
		}
	}
	
	private void processList(Player admin, String[] params)
	{
		String msg = "[Siege Locations]\n";

		for(SiegeLocation loc : SiegeService.getInstance().getSiegeLocations().values())
		{
			if(loc.getSiegeType() == SiegeType.ARTIFACT || loc.getSiegeType() == SiegeType.FORTRESS)
			{
				if(params.length > 1)
				{
					String filter = params[1].toLowerCase();

					if(filter.startsWith("fortress") && loc.getSiegeType() == SiegeType.ARTIFACT)
						continue;

					if(filter.startsWith("artifact") && loc.getSiegeType() == SiegeType.FORTRESS)
						continue;
				}

				msg += "- " + loc.getSiegeType().name() +
					" (" + String.valueOf(loc.getLocationId()) +
					") - " + loc.getRace().name() + " (" +
					String.valueOf(loc.getLegionId()) +
					")\n";

				if (msg.length() > 500)
				{
					PacketSendUtility.sendMessage(admin, msg);
					msg = "";
				}
			}
		}
		PacketSendUtility.sendMessage(admin, msg);
	}

	private void processCapture(Player admin, String[] params)
	{
		if (params.length < 3 || params.length > 4)
		{
			PacketSendUtility.sendMessage(admin, "//fortress <location id> <race> [legion id]");
			return;
		}

		int locationId;
		try
		{
			locationId = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "<location id> must be an integer.");
			return;
		}

		SiegeLocation sLoc = SiegeService.getInstance().getSiegeLocation(locationId);

		if (sLoc == null)
		{
			PacketSendUtility.sendMessage(admin, "<location id> does not exist: " + locationId);
			return;
		}

		SiegeRace race = SiegeRace.BALAUR;

		final String raceName = params[2].toLowerCase();
		if (raceName.startsWith(("ely")))
		{
			race = SiegeRace.ELYOS;
		}
		else if (raceName.startsWith("asmo"))
		{
			race = SiegeRace.ASMODIANS;
		}
		else if (raceName.startsWith("balaur"))
		{
			race = SiegeRace.BALAUR;
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "<race> must be: elyos, asmos, or balaur.");
			return;
		}
		
		int legionId = 0;
		if (params.length == 4)
		{
			try
			{
				legionId = Integer.parseInt(params[3]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "[legion id] must be an integer.");
				return;
			}
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
				SiegeService.getInstance().spawnLocation(fortressId, siegeRace, "PEACE");
			}
		}, 5000);
	}
}
