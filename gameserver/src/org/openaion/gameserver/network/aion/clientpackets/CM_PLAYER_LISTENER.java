package org.openaion.gameserver.network.aion.clientpackets;

import java.util.Calendar;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.DredgionInstanceService;
import org.openaion.gameserver.services.EmotionService;
import org.openaion.gameserver.services.HTMLService;
import org.openaion.gameserver.services.TitleService;
import java.util.Calendar;


/**
 *
 * @author ginho1
 *
 */
public class CM_PLAYER_LISTENER extends AionClientPacket
{
	/*
	 * this CM is send every five minutes by client.
	 */
	public CM_PLAYER_LISTENER(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
	}

	/**c
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();

		if(player == null)
			return;

		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

		TitleService.checkPlayerTitles(player);

		if(CustomConfig.RETAIL_EMOTIONS)
			EmotionService.removeExpiredEmotions(player);

		if(CustomConfig.ENABLE_SURVEYS)
			HTMLService.onPlayerLogin(player);

		//send dredgion instance entry
		if(CustomConfig.ENABLE_DREDGION){		
    		if((hour >= 0 && hour <= 1) || (hour >= 12 && hour <= 13) || (hour >= 20 && hour <= 21))
    		{
    			if(!player.getReceiveEntry())
    			{
    				DredgionInstanceService.getInstance().sendDredgionEntry(player);
    				player.setReceiveEntry(true);
    			}
    		}
		}
	}
}
