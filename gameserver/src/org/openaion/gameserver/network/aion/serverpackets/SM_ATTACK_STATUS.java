/**
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;


/**
 * 
 * @author alexa026
 * @author ATracer
 * 
 */
public class SM_ATTACK_STATUS extends AionServerPacket
{
    private Creature creature;
    private TYPE type;
    private int skillId;
    private int value;
    private int logId;
 
    
    public static enum TYPE
    {
    	NATURAL_HP(3),
    	USED_HP(4),//when skill uses hp as cost parameter
    	REGULAR(5),
    	HP(7),//or damage
    	DELAYDAMAGE(10),
    	FALL_DAMAGE(17),
    	HEALED_MP(19), 
    	ABSORBED_MP(20),
    	MP(21),
    	NATURAL_MP(22),
    	FP_RINGS(23),
    	FP(25),//fp pot
    	NATURAL_FP(26);
    	
    	private int value;
    	
    	private TYPE(int value)
    	{
    		this.value = value;
    	}
    	
    	public int getValue()
    	{
    		return this.value;
    	}
    }
	
    public SM_ATTACK_STATUS(Creature creature, TYPE type, int value, int skillId, int logId)
    {
    	this.creature = creature;
		this.type = type;
		this.skillId = skillId;
		this.value = value;
    	this.logId = logId;
    }
    
    public SM_ATTACK_STATUS(Creature creature, TYPE type, int value)
    {
    	this.creature = creature;
		this.type = type;
		this.skillId = 0;
		this.value = value;
    	this.logId = 170;
    }
    
	/**
	 * {@inheritDoc} ddchcc
	 */
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{		
		writeD(buf, creature.getObjectId());
		writeD(buf, value);
		writeC(buf, type.getValue());
		writeC(buf, creature.getLifeStats().getHpPercentage());
		writeH(buf, skillId);
		writeH(buf, logId);
		
		// logId
		//depends on effecttemplate
		//spellattack(hp) 1
		//poison(hp) 25
		//delaydamage(hp) 95
		//bleed(hp) 26
		//mp regen(natural_mp) 170
		//hp regen(natural_hp) 170 
		//fp pot(fp) 170
		// prochp(hp) 170
		// procmp(mp) 170
		//SpellAtkDrainInstantEffect(absorbed_mp) 24(refactoring shard)
		//mpovertime(mp) 4
		//hpovertime(hp) 3
		//spellatkdrain(hp) 130
		// falldmg (17) 170
		//mpheal (19) 170
		//hp as cost parameter(4) logId 170
		/**
		 * TODO
		 * attack status is send even when dont need to
		 * figure out types
		 * figure out procatkinstant
		 * figure out magiccounter attack
		 */
	}	
}
