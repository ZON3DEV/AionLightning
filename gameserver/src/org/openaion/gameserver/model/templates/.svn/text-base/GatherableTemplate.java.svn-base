/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.templates.gather.Materials;


/**
 * @author ATracer, HellBoy
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gatherableTemplates", propOrder = {"materials", "extraMaterials"})
public class GatherableTemplate extends VisibleObjectTemplate
{
	@XmlElement(name = "materials", required = true)
	protected Materials materials;
	@XmlElement(name = "extra_materials")
	protected Materials extraMaterials;

	@XmlAttribute
	protected int harvestSkill = 30002;
	@XmlAttribute
	protected int charLevel;
	@XmlAttribute
	protected int requiredItem;
	@XmlAttribute
	protected int skillLevel;
	@XmlAttribute
	protected int harvestCount = 3;
	@XmlAttribute
	protected String sourceType;
	@XmlAttribute
	protected int nameId;
	@XmlAttribute
	protected int id;
	@XmlAttribute
	protected int checkType = 0;
	@XmlAttribute
	protected int eraseValue = 0;

	/**
	 * Gets the value of the materials property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Materials }
	 *	 
	 */
	public Materials getMaterials()
	{
		return materials;
	}
	
	/**
	 * Gets the value of the extraMaterials property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link extraMaterials }
	 *	 
	 */
	public Materials getExtraMaterials()
	{
		return extraMaterials;
	}
	
	/**
	 * Gets the value of the id property.  
	 */
	@Override
	public int getTemplateId()
	{
		return id;
	}

	/**
	 * Gets the value of the harvestSkill property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public int getHarvestSkill()
	{
		return harvestSkill;
	}
	
	/**
	 * Gets the value of the charLevel property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public int getCharLevel()
	{
		return charLevel;
	}
	
	/**
	 * Gets the itemId of the requiredItem property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public int getRequiredItem()
	{
		return requiredItem;
	}

	/**
	 * Gets the value of the skillLevel property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public int getSkillLevel()
	{
		return skillLevel;
	}

	/**
	 * Gets the value of the harvestCount property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public int getHarvestCount()
	{
		return harvestCount;
	}

	/**
	 * Gets the value of the sourceType property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link String }
	 *	 
	 */
	public String getSourceType()
	{
		return sourceType;
	}

	/**
	 * @return the nameId
	 */
	@Override
	public int getNameId()
	{
		return nameId;
	}
	
	/**
	 * @return the checkType
	 */
	public int getCheckType()
	{
		return checkType;
	}
	
	/**
	 * @return the eraseValue
	 */
	public int getEraseValue()
	{
		return eraseValue;
	}
	
	/**
	 * @return the name
	 */
	@Override
	public String getName()
	{
		String name = "";
		return name;
	}
}
