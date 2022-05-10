package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.model.gameobjects.player.Emotion;
import org.openaion.gameserver.model.gameobjects.player.EmotionList;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.services.EmotionService;


/**
 *
 * @author ginho1
 *
 */
public class SM_EMOTION_LIST extends AionServerPacket
{
	private EmotionList	emotionList;

	public SM_EMOTION_LIST(Player player)
	{
		this.emotionList = player.getEmotionList();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, 0x00);

		if(CustomConfig.RETAIL_EMOTIONS)
		{
			Player player = emotionList.getOwner();
			EmotionService.removeExpiredEmotions(player);
			
			writeH(buf, emotionList.size());

			for(Emotion emotion : emotionList.getEmotions())
			{
				writeH(buf, emotion.getEmotionId());
				writeD(buf, (int) emotion.getEmotionTimeLeft());
			}
		}else{
			writeH(buf, 66);
			for (int i = 0; i < 66; i++)
			{
				writeH(buf, 64 + i);
				writeD(buf, 0x00);
			}
		}
	}
} 