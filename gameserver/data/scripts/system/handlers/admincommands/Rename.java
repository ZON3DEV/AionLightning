package admincommands;

import java.util.Iterator;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.dao.PlayerDAO;
import org.openaion.gameserver.model.gameobjects.player.Friend;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.modifiers.Executor;
import org.openaion.gameserver.network.aion.serverpackets.SM_LEGION_UPDATE_MEMBER;
import org.openaion.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.services.PlayerService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.Util;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;
import org.openaion.gameserver.world.World;


/**
 * @author Sylar
 * @Updated By Kamui
 */

public class Rename extends AdminCommand
{
	public Rename()
	{
		super("rename");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{

		if (admin.getAccessLevel() < AdminConfig.COMMAND_RENAME)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params.length < 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //rename <player name> <new player name>");
			return;
		}

		final Player player = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (player == null)
		{
			PacketSendUtility.sendPacket(admin, SM_SYSTEM_MESSAGE.PLAYER_IS_OFFLINE(params[0]));
			return;
		}

		if (!PlayerService.isValidName(params[1]))
		{
			PacketSendUtility.sendPacket(admin, new SM_SYSTEM_MESSAGE(1400151));
			return;
		}

		if (!PlayerService.isFreeName(params[1]))
		{
			PacketSendUtility.sendPacket(admin, new SM_SYSTEM_MESSAGE(1400155));
			return;
		}

		player.getCommonData().setName(params[1]);
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		Iterator<Friend> knownFriends = player.getFriendList().iterator();

		DAOManager.getDAO(PlayerDAO.class).storePlayer(player);

		player.getKnownList().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player p)
			{
				PacketSendUtility.sendPacket(p, new SM_PLAYER_INFO(player, player.isEnemyPlayer(p)));
				return true;
			}
		}, true);
		
		while (knownFriends.hasNext())
		{
			Friend nextObject = knownFriends.next();
			if (nextObject.getPlayer() != null)
			{
				if (nextObject.getPlayer().isOnline())
					PacketSendUtility.sendPacket(nextObject.getPlayer(), new SM_PLAYER_INFO(player, false));
			}
		}

		if (player.isLegionMember())
		{
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
		}
		PacketSendUtility.sendMessage(player, "You have been renamed to [" + params[1] + "] by " + admin.getName());
		PacketSendUtility.sendMessage(admin, "Player " + params[0] + " has been renamed to " + params[1]);
	}
}
