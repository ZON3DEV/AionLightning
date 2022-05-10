/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.gameserver.model.gameobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.templates.item.ItemRace;
import org.openaion.gameserver.model.templates.survey.SurveyItem;


/**
 *
 * @author ginho1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SurveyTemplate", propOrder = { "title", "message", "select_text", "item" })
public class Survey
{
	@XmlTransient
	private int survey_id;
	
	@XmlTransient
	private int player_id;
	
	@XmlElement(required = true)
	private String title;
	
    @XmlElement(required = true)
	private String message;
    
    @XmlElement(name="select", required = true)
	private String select_text;
    
    @XmlElement(name="item", required = true)
	private SurveyItem item;
    
    @XmlAttribute(name = "level", required = true)
	private int level;
    
    @XmlAttribute(name = "race")
	private ItemRace race = ItemRace.ALL;
    
	@XmlTransient
	private long itemExistTime;
	
	@XmlTransient
	private int itemTradeTime;

	public Survey()
	{
	}
	
	public Survey(int survey_id, int player_id, String title, String message, String select_text, int itemId, int itemCount, long itemExistTime, int itemTradeTime)
	{
		this.survey_id = survey_id;
		this.player_id = player_id;
		this.title= title;
		this.message = message;
		this.select_text = select_text;
		this.item = new SurveyItem(itemId, itemCount);
		this.itemExistTime = itemExistTime;
		this.itemTradeTime = itemTradeTime;
	}

	public int getSurveyId()
	{
		return survey_id;
	}

	public int getPlayerId()
	{
		return player_id;
	}

	public String getTitle()
	{
		return title;
	}

	public String getMessage()
	{
		return message;
	}

	public String getSelectText()
	{
		return select_text;
	}

	public int getItemId()
	{
		return item.getId();
	}

	public int getItemCount()
	{
		return item.getCount();
	}
	
	public long getItemExistTime()
	{
		return itemExistTime;
	}
	
	public int getItemTradeTime()
	{
		return itemTradeTime;
	}

	/**
	 * @return the level
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * @return the race
	 */
	public Race getRace()
	{
		if (race == ItemRace.ALL)
			return Race.PC_ALL;
		else if (race == ItemRace.ASMODIANS)
			return Race.ASMODIANS;
		else
			return Race.ELYOS;
	}
}