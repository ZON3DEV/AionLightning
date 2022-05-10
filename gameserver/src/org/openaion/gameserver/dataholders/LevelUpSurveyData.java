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
package org.openaion.gameserver.dataholders;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.Survey;
import org.openaion.gameserver.model.gameobjects.player.Player;


/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "surveys" })
@XmlRootElement(name = "levelup_surveys")

public class LevelUpSurveyData
{
	@XmlElement(name = "survey")
    protected List<Survey> surveys;
    
    public List<Survey> getSurveys() 
    {
        if (surveys == null)
            surveys = new ArrayList<Survey>();
        return this.surveys;
    }
    
    public Survey getSurvey(Player player)
    {
    	for (Survey survey : getSurveys())
    	{
    		if (player.getLevel() == survey.getLevel() &&
    			(player.getCommonData().getRace() == survey.getRace() || survey.getRace() == Race.PC_ALL))
    			return survey;
    	}
    	return null;
    }
    
    public int size()
    {
    	return getSurveys().size();
    }
}
