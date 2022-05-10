package org.openaion.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.SkillElement;

 
/**
 * @author kecimis
 */
@XmlType(name = "attack_type")
@XmlEnum
public enum EAttackType
{
	PHYSICAL(false),
	MAGICAL_WATER(true),
	MAGICAL_FIRE(true);
	
	private boolean	magic;

	private EAttackType(boolean magic)
	{
		this.magic = magic;
	}

	public String value()
	{
		return name();
	}

	public static EAttackType fromValue(String v)
	{
		return valueOf(v);
	}

	/**
	 * @return Returns the magic.
	 */
	public boolean isMagic()
	{
		return magic;
	}
	
	public SkillElement getElement()
	{
		switch (this)
		{
			case MAGICAL_WATER: 
				return SkillElement.WATER;
			case MAGICAL_FIRE:
				return SkillElement.FIRE;
		}
		return SkillElement.NONE;
	}
	
	public static EAttackType fromElement(SkillElement element)
	{
		switch (element)
		{
			case WATER: 
				return MAGICAL_WATER; 
			case FIRE: 
				return MAGICAL_FIRE;
		}
		
		return PHYSICAL;
	}
	
}