package admincommands;

import org.openaion.gameserver.configs.administration.AdminConfig;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION_LIST;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.chathandlers.AdminCommand;
import org.openaion.gameserver.utils.i18n.CustomMessageId;
import org.openaion.gameserver.utils.i18n.LanguageHandler;


/**
 * @author ginho1
 * 
 */
public class AddEmotion extends AdminCommand
{

	public AddEmotion()
	{
		super("addemotion");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDTITLE)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
			return;
		}

		if((params.length < 1) || (params.length > 2))
		{
			PacketSendUtility.sendMessage(admin, "sintax: //addemotion <emotion id> [expire time]");
			return;
		}

		int emotionId = Integer.parseInt(params[0]);

		if(emotionId < 0 || emotionId > 120)
		{
			PacketSendUtility.sendMessage(admin, "Invalid <emotion id> [0-120]");
			return;
		}

		VisibleObject target = admin.getTarget();

		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "No target selected");
			return;
		}

		if (target instanceof Player)
		{
			Player player = (Player) target;

			boolean sucess = false;

			try
			{
				if(params.length == 2)
				{
					long expireMinutes = Long.parseLong(params[1]);
					sucess = player.getEmotionList().add(emotionId, System.currentTimeMillis(), (expireMinutes * 60L));
					PacketSendUtility.sendPacket(player, new SM_EMOTION_LIST(player));
				}else{
					sucess = player.getEmotionList().add(emotionId, System.currentTimeMillis(), 0);
					PacketSendUtility.sendPacket(player, new SM_EMOTION_LIST(player));
				}
			}
			catch (NumberFormatException ex)
			{
				PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.INTEGER_PARAMETER_REQUIRED));
				return;
			}

			if(sucess)
			{
				PacketSendUtility.sendMessage(admin, "Emotion added!");
			}else{
				PacketSendUtility.sendMessage(admin, "You can't add this emotion.");
			}
		}
	}
}
