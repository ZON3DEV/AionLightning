/*
 * This file is part of aion-unique <aion-unique.org>.
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
package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.geo.GeoEngine;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.model.DashParam;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.DashParam.DashType;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.world.World;


/**
 * @author Sylar
 * @edit kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RandomMoveLocEffect")
public class RandomMoveLocEffect extends EffectTemplate
{
	
	@XmlAttribute(name = "direction")
	private String direction;
	
	@XmlAttribute(name = "value")
	private int value;
	
	@Override
	public void applyEffect(Effect effect)
	{
        DashParam dash = effect.getDashParam();
        World.getInstance().updatePosition((Player)effect.getEffected(), dash.getX(), dash.getY(), dash.getZ(), (byte)dash.getHeading(), false);
	}
	
	@Override
	public void calculate(Effect effect)
	{
		if (!(effect.getEffected() instanceof Player))
			return;
		
        Player player = (Player)effect.getEffected();
        double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
        float x = player.getX();
        float y = player.getY();
        float z = player.getZ();
        int worldId = player.getWorldId();
        
        float x2 = 0;
        float y2 = 0;
        float lastSee = 0;
        float lastNonSee = 0;
                    
        if(direction.equals("FRONT"))
        {
            x2 = (float)(x + (value * Math.cos(radian)));
            y2 = (float)(y + (value * Math.sin(radian)));
            
            //if can see the final point then just port
            this.setDashParam(effect, x2, y2, z + 2);
            super.calculate(effect);
            return;
        } else if(direction.equals("BACK")) {
            x2 = (float)(x + (value * Math.cos(Math.PI+radian)));
            y2 = (float)(y + (value * Math.sin(Math.PI+radian)));
            
            //if can see the final point then just port
            this.setDashParam(effect, x2, y2, z + 2);
            super.calculate(effect);
            
            float temp = 0;
            
            while (lastNonSee - lastSee >= 0.5)
            {
                temp = (lastNonSee - lastSee)/2 + lastSee;
                x2 = (float)(x + (temp * Math.cos(Math.PI+radian)));
                y2 = (float)(y + (temp * Math.sin(Math.PI+radian)));
                
                lastNonSee = temp;
            }
            
            //set final coordinates
            x2 = (float)(x + (lastSee * Math.cos(Math.PI+radian)));
            y2 = (float)(y + (lastSee * Math.sin(Math.PI+radian)));
        }
        else
        {
            Logger.getLogger(RandomMoveLocEffect.class).error("Cannot move without direction");
            return;
        }

        this.setDashParam(effect, x2, y2, z + 2);
		
		super.calculate(effect);
	}
	
	private void setDashParam(Effect effect, float x, float y, float z)
	{
		Player player = (Player)effect.getEffected();
		Logger.getLogger(RandomMoveLocEffect.class).debug(player.getObjectId() + " moving " + value +"m direction "+direction.toString());
		Logger.getLogger(RandomMoveLocEffect.class).debug(player.getObjectId() + " x: "+x+" y: "+y+" z: "+z);
		effect.setDashParam(new DashParam(DashType.RANDOMMOVELOC, x, y, z, player.getHeading()));
	}
}
