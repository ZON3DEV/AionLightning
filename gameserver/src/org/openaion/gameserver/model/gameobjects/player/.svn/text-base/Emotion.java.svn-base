package org.openaion.gameserver.model.gameobjects.player;

/**
 * @author ginho1
 *
 */
public class Emotion
{
	private int emotionId;
	private long emotion_date = 0;
	private long emotion_expires_time = 0;

	public Emotion(int emotionId, long emotion_date, long emotion_expires_time)
	{
		this.emotionId = emotionId;
		this.emotion_date = emotion_date;
		this.emotion_expires_time = emotion_expires_time;
	}

	public int getEmotionId()
	{
		return emotionId;
	}

	public long getEmotionDate()
	{
		return emotion_date;
	}

	public long getEmotionExpiresTime()
	{
		return emotion_expires_time;
	}

	public void setEmotionId(int emotionId)
	{
		this.emotionId = emotionId;
	}

	public long getEmotionTimeLeft()
	{
		if(emotion_expires_time == 0)
			return 0;

		long timeLeft = (emotion_date + ((emotion_expires_time )  * 1000L)) - System.currentTimeMillis();
		if(timeLeft < 0)
			timeLeft = 0;

		return timeLeft /1000L ;
	}

	public void setEmotionDate(long emotion_date)
	{
		this.emotion_date = emotion_date;
	}

	public void setEmotionExpiresTime(long emotion_expires_time)
	{
		this.emotion_expires_time = emotion_expires_time;
	}
}