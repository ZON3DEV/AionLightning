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

package com.aionemu.gameserver.skillengine.periodicaction;

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Created by Kill3r
 */
public class MpUseByValPeriodicAction extends PeriodicAction {

    @XmlAttribute(name = "value")
    protected int value;

    @Override
    public void act(final Effect effect){
        Creature effected = effect.getEffected();

        int requiredMP = value;
        if(effected.getLifeStats().getCurrentMp() < requiredMP){
            effect.endEffect();
            return;
        }
        if(effect.getSkillId() == 3063){
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    effect.endEffect();
                }
            },10000);

        }
        final Player player = (Player) effect.getEffected();
        //Kinetic Battery
        if(effect.getSkillId() == 3766 || effect.getSkillId() == 3767 || effect.getSkillId() == 3768 || effect.getSkillId() == 3769 || effect.getSkillId() == 3770){
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    effect.endEffect();
                }
            },120000);

            if(player.getRobotId() == 0){
                effect.endEffect();
            }
        }
        //Kinetic Bulwark
        if(effect.getSkillId() == 3771 || effect.getSkillId() == 3772 || effect.getSkillId() == 3773 || effect.getSkillId() == 3774){
            final ActionObserver observer = new ActionObserver(ObserverType.SKILLUSE){
                @Override
                public void skilluse(Skill skill){
                    if(player.getEffectController().hasAbnormalEffect(3766) || player.getEffectController().hasAbnormalEffect(3767) || player.getEffectController().hasAbnormalEffect(3768) || player.getEffectController().hasAbnormalEffect(3769) || player.getEffectController().hasAbnormalEffect(3770)){
                        effect.endEffect();
                    }
                }
            };
            player.getObserveController().addObserver(observer);
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    player.getObserveController().removeObserver(observer);
                    effect.endEffect();
                }
            },30000);
            if(player.getRobotId() == 0){
                player.getObserveController().removeObserver(observer);
                effect.endEffect();
            }
        }

        effected.getLifeStats().reduceMp(value);
    }
}
