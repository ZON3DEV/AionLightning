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
package org.openaion.gameserver.model.templates.survey;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SurveyItem")
public class SurveyItem
{
    @XmlAttribute(name = "id", required = true)
    protected int id;
    
    @XmlAttribute(name = "count", required = true)
    protected int count;
    
	public SurveyItem()
	{
	}
    
    /**
	 * @param itemId
	 * @param itemCount
	 */
	public SurveyItem(int itemId, int itemCount)
	{
		this.id = itemId;
		this.count = itemCount;
	}

	public int getId() 
    {
        return id;
    }
    
    public int getCount() 
    {
        return count;
    }
}
