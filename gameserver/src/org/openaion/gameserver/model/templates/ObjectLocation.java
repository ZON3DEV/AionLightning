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
@XmlType(name = "ObjectLocation")
public class ObjectLocation
{		
	@XmlAttribute(name = "xe", required = true)
	private float	xe;
	@XmlAttribute(name = "ye", required = true)
	private float	ye;
	@XmlAttribute(name = "ze", required = true)
	private float	ze;
	
	@XmlAttribute(name = "he", required = true)
	private byte	he;
	
	@XmlAttribute(name = "xa", required = true)
	private float	xa;
	@XmlAttribute(name = "ya", required = true)
	private float	ya;
	@XmlAttribute(name = "za", required = true)
	private float	za;
	
	@XmlAttribute(name = "ha", required = true)
	private byte	ha;

	public float getXe()
	{
		return xe;
	}

	public float getYe()
	{
		return ye;
	}

	public float getZe()
	{
		return ze;
	}

	public byte getHe()
	{
		return he;
	}

	public float getXa()
	{
		return xa;
	}

	public float getYa()
	{
		return ya;
	}

	public float getZa()
	{
		return za;
	}

	public byte getHa()
	{
		return ha;
	}
}
