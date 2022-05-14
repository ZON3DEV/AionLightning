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

package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.controllers.attack.AttackResult;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.List;

/**
 * @author -Nemesiss-, Sweetkr
 */
public class SM_ATTACK extends AionServerPacket {

	private int attackno;
	private int time;
	private int type;
	private List<AttackResult> attackList;
	private Creature attacker;
	private Creature target;

	public SM_ATTACK(Creature attacker, Creature target, int attackno, int time, int type, List<AttackResult> attackList) {
		this.attacker = attacker;
		this.target = target;
		this.attackno = attackno;// empty
		this.time = time;// empty
		this.type = type;// empty
		this.attackList = attackList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(attacker.getObjectId());
		writeC(attackno); // unknown
		writeH(time); // unknown
		writeC(0);
		writeC(type); // 0, 1, 2
		writeD(target.getObjectId());

		int attackerMaxHp = attacker.getLifeStats().getMaxHp();
		int attackerCurrHp = attacker.getLifeStats().getCurrentHp();
		int targetMaxHp = target.getLifeStats().getMaxHp();
		int targetCurrHp = target.getLifeStats().getCurrentHp();

		writeC((int) (100f * targetCurrHp / targetMaxHp)); // target %hp
		writeC((int) (100f * attackerCurrHp / attackerMaxHp)); // attacker %hp

		// TODO refactor attack controller
		switch (attackList.get(0).getAttackStatus().getId()) // Counter skills
		{
			case 196: // case CRITICAL_BLOCK 4.5
				// case -60: // case CRITICAL_BLOCK old 4.0
			case 4: // case BLOCK
				writeH(32);
				break;
			case 194: // case CRITICAL_PARRY 4.5
				// case -62: // case CRITICAL_PARRY old 4.0
			case 2: // case PARRY
				writeH(64);
				break;
			case 192: // case CRITICAL_DODGE 4.5
				// case -64: // case CRITICAL_DODGE old 4.0
			case 0: // case DODGE
				writeH(128);
				break;
			case 198: // case CRITICAL_RESIST 4.5
				// case -58: // case PHYSICAL_CRITICAL_RESIST old 4.0
			case 6: // case RESIST
				writeH(256); // need more info becuz sometimes 0
				break;
			default:
				writeH(0);
				break;
		}
		// setting counter skill from packet to have the best synchronization of
		// time with client
		if (target instanceof Player) {
			if (attackList.get(0).getAttackStatus().isCounterSkill()) {
				((Player) target).setLastCounterSkill(attackList.get(0).getAttackStatus());
			}
		}

		writeH(0);

		// TODO! those 2h (== d) up is some kind of very weird flag...
		// writeD(attackFlag);
		/*
		 * if(attackFlag & 0x10A0F != 0) { writeF(0); writeF(0); writeF(0); }
		 * if(attackFlag & 0x10010 != 0) { writeC(0); } if(attackFlag & 0x10000
		 * != 0) { writeD(0); writeD(0); }
		 */
		writeC(attackList.size());
		for (AttackResult attack : attackList) {
			writeD(attack.getDamage());
			writeC(attack.getAttackStatus().getId());

			byte shieldType = (byte) attack.getShieldType();
			writeC(shieldType);

			/**
			 * shield Type: 1: reflector 2: normal shield 8: protect effect (ex.
			 * skillId: 417 Bodyguard) TODO find out 4
			 */
			switch (shieldType) {
				case 0:
				case 2:
					break;
				case 8:
				case 10:
					writeD(attack.getProtectorId()); // protectorId
					writeD(attack.getProtectedDamage()); // protected damage
					writeD(attack.getProtectedSkillId()); // skillId
					break;
				default:
					writeD(attack.getProtectorId()); // protectorId
					writeD(attack.getProtectedDamage()); // protected damage
					writeD(attack.getProtectedSkillId()); // skillId
					writeD(attack.getReflectedDamage()); // reflect damage
					writeD(attack.getReflectedSkillId()); // skill id
					writeD(0); // unk
					writeD(0); // unk
					break;
			}
		}
		writeC(0);
	}
}
