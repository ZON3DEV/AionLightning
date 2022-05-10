/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by Kill3r
 */
public class DelayedSkill2Effect extends EffectTemplate {

    @XmlAttribute(name = "skill_id")
    protected int skilliD;
    @XmlAttribute(name = "time_delay_to_hit")
    protected int delaytohit;


    @Override
    public void applyEffect(Effect effect){
        effect.addToEffectedController();
    }

    @Override
    public void startEffect(final Effect effect){
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                // apply effect
                if(effect.getEffected().getEffectController().hasAbnormalEffect(3160)){
                    SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skilliD);
                    Effect e = new Effect(effect.getEffector(), effect.getEffected(), template, template.getLvl(), 0);
                    e.initialize();
                    e.applyEffect();
                }
            }
        }, delaytohit);
    }

    @Override
    public void endEffect(Effect effect){

    }
}