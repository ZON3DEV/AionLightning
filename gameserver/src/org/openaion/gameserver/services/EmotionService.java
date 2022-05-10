package org.openaion.gameserver.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openaion.commons.database.dao.DAOManager;
import org.openaion.gameserver.dao.PlayerEmotionListDAO;
import org.openaion.gameserver.model.gameobjects.player.Emotion;
import org.openaion.gameserver.model.gameobjects.player.EmotionList;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION_LIST;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author ginho1
 *
 */
public class EmotionService
{
	public static boolean isExpired(long expires_time, long date)
	{
		if(expires_time > 0)
		{
			long timeLeft = (date + (expires_time * 1000L)) - System.currentTimeMillis();
			if(timeLeft < 0)
			{
				return true;
			}
		}
		return false;
	}

	public static void removeEmotion(int playerId, int emotionId)
	{
		DAOManager.getDAO(PlayerEmotionListDAO.class).removeEmotion(playerId, emotionId);
	}

	public static void removeExpiredEmotions(Player player)
	{
		EmotionList emotionList = player.getEmotionList();
		List<Integer> delEmotions = new ArrayList<Integer>();
		boolean removed = false;

		for(Emotion emotion : emotionList.getEmotions())
		{
			if(EmotionService.isExpired(emotion.getEmotionExpiresTime(), emotion.getEmotionDate()))
			{
				delEmotions.add(emotion.getEmotionId());
			}
		}

		Iterator<Integer> iterator = delEmotions.iterator();
		while(iterator.hasNext())
		{
			int emotionId = iterator.next();
			removeEmotion(player.getObjectId(), emotionId);
			emotionList.remove(emotionId);
			removed = true;
		}

		if(removed)
		{
			PacketSendUtility.sendPacket(player, new SM_EMOTION_LIST(player));
			PacketSendUtility.sendMessage(player, "The usage time of emotion has expired.");
		}
	}
}