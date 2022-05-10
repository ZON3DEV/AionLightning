package org.openaion.gameserver.model.gameobjects.player;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author ginho1
 */
public class EmotionList
{
	private LinkedHashMap<Integer, Emotion> emotions;
	private Player owner;

	public EmotionList()
	{
	    this.emotions = new LinkedHashMap<Integer, Emotion>();
	    this.owner = null;
	}

	public void setOwner(Player owner)
	{
		this.owner = owner;
	}

	public Player getOwner()
	{
		return owner;
	}

	public boolean add(int id, long date, long expires_time)
	{
		if(!emotions.containsKey(id))
		{
			emotions.put(id, new Emotion(id, date, expires_time));
			return true;
		}
		return false;
	}

	public void remove(int id)
	{
		if(emotions.containsKey(id))
		{
			emotions.remove(id);
		}
	}

	public Emotion get(int id)
	{
		if(emotions.containsKey(id))
			return emotions.get(id);

		return null;
	}

	public boolean canAdd(int id)
	{
		if(emotions.containsKey(id))
			return false;

		return true;
	}

	public int size()
	{
		return emotions.size();
	}

	public Collection<Emotion> getEmotions()
	{

		return emotions.values();
	}
}
