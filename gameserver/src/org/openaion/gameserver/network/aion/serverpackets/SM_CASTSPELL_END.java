package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.Skill;


/**
 * 
 * @author alexa026, Sweetkr, kecimis
 * 
 */
public class SM_CASTSPELL_END extends AionServerPacket
{
	private Skill		skill;

	public SM_CASTSPELL_END(Skill skill)
	{
		this.skill = skill;
	}
	/**
	 * {@inheritDoc}
	 */

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, skill.getEffector().getObjectId());
		writeC(buf, skill.getTargetType());
		switch(skill.getTargetType())
		{
			case 0:
				writeD(buf, skill.getFirstTarget().getObjectId());
				break;
			case 1:
				writeF(buf, skill.getX());
				writeF(buf, skill.getY());
				writeF(buf, skill.getZ() + 0.4f);
				break;
			case 3:
				writeD(buf, 0);
				break;
		}
		writeH(buf, skill.getSkillTemplate().getSkillId());
		writeC(buf, skill.getSkillLevel());
		writeD(buf, skill.getSkillTemplate().getCooldown());
		writeH(buf, skill.getTime()); // time, from CM_CASTSPELL packet
		writeC(buf, 0); // unk

		/**
		 * 32 : chain skill (counter too)
		 * 16 : no damage to all target like dodge, resist or effect size is 0
		 * 0 : regular
		 */
		//TODO sniff this
		if (skill.getChainSuccess())
			writeH(buf, 32);
		else if (skill.getEffects().isEmpty())
			writeH(buf, 16);
		else	
			writeH(buf, 0);
			
		/**
		 * Dash Type
		 * 
		 * 1 : teleport to back (1463)
		 * 2 : dash (816)
		 * 4 : assault (803)
		 */
		if (skill.getDashParam() == null)
			writeC(buf, 0);
		else
		{
			writeC(buf, skill.getDashParam().getType());
			switch(skill.getDashParam().getType())
			{
				case 1:
				case 2:
				case 4:
					writeC(buf, skill.getDashParam().getHeading());
					writeF(buf, skill.getDashParam().getX());
					writeF(buf, skill.getDashParam().getY());
					writeF(buf, skill.getDashParam().getZ());
					break;
				default:
					break;
			}
		}

		writeH(buf, skill.getEffects().size());
					
		for(Effect effect : skill.getEffects())
		{
			int effectorMaxHp = effect.getEffector().getLifeStats().getMaxHp();//effector
			int effectorCurrHp = effect.getEffector().getLifeStats().getCurrentHp();//effector
			if (effect.getEffected() == null)//point skill
			{
				writeD(buf, 0);//effectedObjectId
				writeC(buf, 0);//unk
				writeC(buf, 0); // target %hp
				writeC(buf, 100 * effectorCurrHp / effectorMaxHp);// attacker %hp
			}
			else
			{
				writeD(buf, effect.getEffected().getObjectId());//effectedObjectId
				writeC(buf, 0); // unk

				int effectedMaxHp = effect.getEffected().getLifeStats().getMaxHp();//effected
				int effectedCurrHp = effect.getEffected().getLifeStats().getCurrentHp();//effected
				
				//retail send actual hp after dmg is applied
				if (effect.getReserved1() > 0)
				{	
					if (effectedCurrHp - effect.getReserved1() <= 0)
						effectedCurrHp = 0;
					else
						effectedCurrHp = effectedCurrHp - effect.getReserved1();
				}
				
				
				writeC(buf, 100 * effectedCurrHp / effectedMaxHp); // effected %hp
				writeC(buf, 100 * effectorCurrHp / effectorMaxHp); // effector %hp
			}
			
			writeC(buf, effect.getSpellStatus().getId());
			writeC(buf, 16); // unk
			writeH(buf, 0x00); // unk 2.5
			writeC(buf, effect.getCarvedSignet()); // current carve signet count			

			/**
			 * Spell Status
			 * 
			 * 0 : can be knockback(simplerooteffect)
			 * 1 : stumble 
			 * 2 : stagger 
			 * 4 : open aerial 
			 * 8 : close aerial 
			 * 16 : spin 
			 * 32 : block 
			 * 64 : parry 
			 * 128 : dodge
			 * 256 : resist
			 */
			switch(effect.getSpellStatus().getId())
			{
				case 1:
				case 2:
				case 4:
				case 8:
					writeF(buf, effect.getEffected().getX());
					writeF(buf, effect.getEffected().getY());
					writeF(buf, effect.getEffected().getZ() + 0.4f);
					break;
				case 16:
					writeC(buf, effect.getEffected().getHeading());
					break;
				default:
					break;
			}

			writeC(buf, 1); // unk always 1
			if (effect.isMpHeal())
				writeC(buf, 1); // be 1 - when use Mana Treatment
			else
				writeC(buf, 0);
			writeD(buf, effect.getReserved1()); // damage
			writeC(buf, effect.getAttackStatus().getId());//attackstatus
			writeC(buf, effect.getShieldType());//shieldtype

			switch(effect.getShieldType())
			{
				case 0:
				case 2:
					break;
				default:
					writeD(buf, 0x00);
					writeD(buf, 0x00);
					writeD(buf, 0x00);
					writeD(buf, effect.getReflectorDamage()); // reflect damage
					writeD(buf, effect.getReflectorSkillId()); // skill id
					break;
			}
			
		}
	 //skill = null;
	}
}
