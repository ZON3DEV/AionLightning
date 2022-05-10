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
package org.openaion.gameserver.model.templates.pet;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.openaion.commons.utils.Rnd;


/**
 * @author Rolandas
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PetRewards", propOrder = { "results" })
@XmlSeeAlso({ org.openaion.gameserver.model.templates.pet.PetFlavour.Food.class })

public class PetRewards 
{
	@XmlElement(name="result")
    protected List<PetRewards.Result> results;
    
    @XmlAttribute(name = "type", required = true)
    protected FoodType type;
    
    @XmlAttribute(name = "loved")
    protected boolean loved = false;

    public List<PetRewards.Result> getResults()
    {
        if (results == null)
            results = new ArrayList<PetRewards.Result>();
        return this.results;
    }
    
    /*
     * Returns results with price = -1 (additionally rewarded, like during events)
     */
    public List<PetRewardDescription> getAdditionalRewards()
    {
    	List<PetRewardDescription> results = new ArrayList<PetRewardDescription>();
    	for (PetRewardDescription descr : getResults())
    	{
    		if (descr.getPrice() == -1)
    			results.add(descr);
    	}
    	return results;
    }
    
    public PetRewardDescription getRandomReward()
    {
    	for (PetRewardDescription descr : getResults())
    	{
    		if (descr.getChance() == 0)
    			continue;
   			if(Rnd.get(100) <= descr.getChance())
    			return descr;
    	}
    	return null;
    }    

    public FoodType getType()
    {
        return type;
    }

    public boolean isLoved() 
    {
        return loved;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Result extends PetRewardDescription
    {
    }
}
