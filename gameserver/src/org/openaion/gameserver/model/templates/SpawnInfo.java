package org.openaion.gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ambrosius
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpawnInfo")
public class SpawnInfo
{		
	@XmlAttribute(name = "x", required = true)
	private float	x;
	@XmlAttribute(name = "y", required = true)
	private float	y;
	@XmlAttribute(name = "z", required = true)
	private float	z;
	
	@XmlAttribute(name = "h", required = true)
	private byte	h;
	
	@XmlAttribute(name = "worldId", required = false)
	private int		worldId;
	
	@XmlAttribute(name = "npcId", required = false)
	private int		npcId;

	public int getNpcId()
	{
		return npcId;
	}

	public int getWorldId()
	{
		return worldId;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public float getZ()
	{
		return z;
	}

	public byte getH()
	{
		return h;
	}
	
}
