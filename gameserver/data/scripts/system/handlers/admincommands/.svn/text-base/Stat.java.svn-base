package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.CreatureGameStats;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;

public class Stat extends AdminCommand
{
	public Stat()
	{
		super("status");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_STAT)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (admin.getTarget() == null)
		{
			PacketSendUtility.sendMessage(admin, "You have to select a target");
			return;
		}

		VisibleObject target = admin.getTarget();

		if (!(target instanceof Creature))
		{
			PacketSendUtility.sendMessage(admin, "Your target is not a Creature");
			return;
		}

		Creature cTarget = (Creature)target;

		PacketSendUtility.sendMessage(admin, ">>> Stats information about "+cTarget.getClass().getSimpleName()+" \""+cTarget.getName()+"\"");
		if (cTarget.getGameStats() != null)
		{
			CreatureGameStats<?> cgs = cTarget.getGameStats();
			for (int i=0; i<StatEnum.values().length; i++)
			{
				if (cgs.getCurrentStat(StatEnum.values()[i])!=0)
				{
					PacketSendUtility.sendMessage(admin, StatEnum.values()[i]+": "+cgs.getBaseStat(StatEnum.values()[i])+" ("+cgs.getStatBonus(StatEnum.values()[i])+")");
				}
			}
		}
		PacketSendUtility.sendMessage(admin, ">>> End of stats information");
	}
}
