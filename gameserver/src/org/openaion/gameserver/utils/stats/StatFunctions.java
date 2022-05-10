/*
 * This file is part of aion-unique <aion-unique.com>.
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
package org.openaion.gameserver.utils.stats;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.configs.main.FallDamageConfig;
import org.openaion.gameserver.controllers.attack.AttackStatus;
import org.openaion.gameserver.model.SkillElement;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Item;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.player.Equipment;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.state.CreatureState;
import org.openaion.gameserver.model.gameobjects.stats.CreatureGameStats;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.model.templates.item.WeaponType;
import org.openaion.gameserver.model.templates.stats.NpcRank;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import org.openaion.gameserver.skill.effect.EffectId;
import org.openaion.gameserver.utils.PacketSendUtility;


/**
 * @author ATracer
 * @author alexa026
 * @edit of damage calculations  kecimis
 */
public class StatFunctions
{
	private static Logger log = Logger.getLogger(StatFunctions.class);

	/**
	 * 
	 * @param player
	 * @param target
	 * @return XP reward from target
	 */
	public static long calculateSoloExperienceReward(Player player, Creature target)
	{
		int playerLevel = player.getCommonData().getLevel();
		int targetLevel = target.getLevel();

		int baseXP = ((Npc)target).getObjectTemplate().getStatsTemplate().getMaxXp();
		int xpPercentage =  XPRewardEnum.xpRewardFrom(targetLevel - playerLevel);
		
		return (int) Math.floor(baseXP * xpPercentage * player.getRates().getXpRate() / 100);
	}

	/**
	 * 
	 * @param player
	 * @param target
	 * @return
	 */
	public static long calculateGroupExperienceReward(int maxLevelInRange, Creature target)
	{
		int targetLevel = target.getLevel();

		int baseXP = ((Npc)target).getObjectTemplate().getStatsTemplate().getMaxXp();
		int xpPercentage =  XPRewardEnum.xpRewardFrom(targetLevel - maxLevelInRange);

		return (int) Math.floor(baseXP * xpPercentage / 100);
	}

	/**
	 * ref: http://www.aionsource.com/forum/mechanic-analysis/42597-character-stats-xp-dp-origin-gerbator-team-july-2009-a.html
	 * 
	 * @param player
	 * @param target
	 * @return DP reward from target
	 */

	public static int calculateSoloDPReward(Player player, Creature target) 
	{
		int playerLevel = player.getCommonData().getLevel();
		int targetLevel = target.getLevel();
		NpcRank npcRank = ((Npc) target).getObjectTemplate().getRank();

		//TODO: fix to see monster Rank level, NORMAL lvl 1, 2 | ELITE lvl 1, 2 etc..
		//look at: http://www.aionsource.com/forum/mechanic-analysis/42597-character-stats-xp-dp-origin-gerbator-team-july-2009-a.html
		int baseDP = targetLevel * calculateRankMultipler(npcRank);

		int xpPercentage =  XPRewardEnum.xpRewardFrom(targetLevel - playerLevel);
		return (int) Math.floor(baseDP * xpPercentage * player.getRates().getXpRate() / 100);

	}
	
	/**
	 * 
	 * @param player
	 * @param target
	 * @return AP reward
	 */
	public static int calculateSoloAPReward(Player player, Creature target) 
	{
		int playerLevel = player.getCommonData().getLevel();
		int targetLevel = target.getLevel();								
		int percentage =  XPRewardEnum.xpRewardFrom(targetLevel - playerLevel);
		return (int) Math.floor(10 * percentage * player.getRates().getApNpcRate() / 100);
	}
	
	/**
	 * 
	 * @param maxLevelInRange
	 * @param target
	 * @return
	 */
	public static int calculateGroupAPReward(int maxLevelInRange, Creature target)
	{
		int targetLevel = target.getLevel();
		NpcRank npcRank = ((Npc) target).getObjectTemplate().getRank();								

		//TODO: fix to see monster Rank level, NORMAL lvl 1, 2 | ELITE lvl 1, 2 etc..
		int baseAP = 10 + calculateRankMultipler(npcRank) - 1;

		int apPercentage =  XPRewardEnum.xpRewardFrom(targetLevel - maxLevelInRange);

		return (int) Math.floor(baseAP * apPercentage / 100);
	}
	
	/**
	 * 
	 * @param defeated
	 * @param winner
	 * @return Points Lost in PvP Death
	 */
	public static int calculatePvPApLost(Player defeated, Player winner)
	{
		int pointsLost = Math.round(defeated.getAbyssRank().getRank().getPointsLost() * defeated.getRates().getApPlayerRate());

		// Level penalty calculation
		int difference = winner.getLevel() - defeated.getLevel();

		if(difference > 4)
		{
			pointsLost = Math.round(pointsLost * 0.1f);
		}
		else
		{
			switch(difference)
			{
				case 3:
					pointsLost = Math.round(pointsLost * 0.85f);
					break;
				case 4:
					pointsLost = Math.round(pointsLost * 0.65f);
					break;
			}
		}
		return pointsLost;
	}

	/**
	 * 
	 * @param defeated
	 * @param winner
	 * @return Points Gained in PvP Kill
	 */
	public static int calculatePvpApGained(Player defeated, int maxRank, int maxLevel)
	{
		int pointsGained = Math.round(defeated.getAbyssRank().getRank().getPointsGained());

		// Level penalty calculation
		int difference = maxLevel - defeated.getLevel();

		if(difference > 4)
		{
			pointsGained = Math.round(pointsGained * 0.1f);
		}
		else if(difference < -3)
		{
			pointsGained = Math.round(pointsGained * 1.3f);
		}
		else
		{
			switch(difference)
			{
				case 3:
					pointsGained = Math.round(pointsGained * 0.85f);
					break;
				case 4:
					pointsGained = Math.round(pointsGained * 0.65f);
					break;
				case -2:
					pointsGained = Math.round(pointsGained * 1.1f);
					break;
				case -3:
					pointsGained = Math.round(pointsGained * 1.2f);
					break;
			}
		}

		// Abyss rank penalty calculation
		int winnerAbyssRank = maxRank;
		int defeatedAbyssRank = defeated.getAbyssRank().getRank().getId();
		int abyssRankDifference = winnerAbyssRank - defeatedAbyssRank;

		if(winnerAbyssRank <= 7 && abyssRankDifference > 0)
		{
			float penaltyPercent = abyssRankDifference * 0.05f;			

			pointsGained -= Math.round(pointsGained * penaltyPercent);
		}

		return pointsGained;
	}
	
	/**
	 * 
	 * @param player
	 * @param target
	 * @return DP reward
	 */
	public static int calculateGroupDPReward(Player player, Creature target)
	{
		int playerLevel = player.getCommonData().getLevel();
		int targetLevel = target.getLevel();
		NpcRank npcRank = ((Npc) target).getObjectTemplate().getRank();								

		//TODO: fix to see monster Rank level, NORMAL lvl 1, 2 | ELITE lvl 1, 2 etc..
		int baseDP = targetLevel * calculateRankMultipler(npcRank);

		int xpPercentage =  XPRewardEnum.xpRewardFrom(targetLevel - playerLevel);

		return (int) Math.floor(baseDP * xpPercentage * player.getRates().getGroupXpRate() / 100);
	}	
	
	/**
	 * Hate based on BOOST_HATE stat
	 * Now used only from skills, probably need to use for regular attack
	 * 
	 * @param creature
	 * @param value
	 * @return
	 */
	public static int calculateHate(Creature creature, int value) 
	{
		return Math.round(value * creature.getGameStats().getCurrentStat(StatEnum.BOOST_HATE) / 100f);
	}

	/**
	 * 
	 * @param player
	 * @param target
	 * @return Damage made to target (-hp value)
	 */
	public static int calculateBaseDamageToTarget(Creature attacker, Creature target)
	{
		return calculatePhysicDamageToTarget(attacker,target, 0, 0);
	}

	/**
	 * 
	 * @param player
	 * @param target
	 * @param skillDamages
	 * @return Damage made to target (-hp value)
	 */
	public static int calculatePhysicDamageToTarget(Creature attacker, Creature attacked, int skillDamages, int bonusDamages)
	{
		CreatureGameStats<?> ags = attacker.getGameStats();

		int resultDamage = 0;

		if (attacker instanceof Player)
		{
			int min = ags.getCurrentStat(StatEnum.MAIN_MIN_DAMAGES);
			int max = ags.getCurrentStat(StatEnum.MAIN_MAX_DAMAGES);
			int min2 = ags.getCurrentStat(StatEnum.OFF_MIN_DAMAGES);
			int max2 = ags.getCurrentStat(StatEnum.OFF_MAX_DAMAGES);
			
			//weapon with higher average should be taken into account for skills
			if (skillDamages > 0)
			{
				if(((min + max)/2) < ((min2 + max2)/2))
				{
					min = min2;
					max = max2;
				}
			}
			
			int average = Math.round((min + max)/2);
			
			Equipment equipment = ((Player)attacker).getEquipment();

			WeaponType weaponType = equipment.getMainHandWeaponType();

			if(weaponType != null)
			{
				if(average < 1)
				{
					average = 1;
					log.warn("Weapon stat MIN_MAX_DAMAGE resulted average zero in main-hand calculation");
					log.warn("Weapon ID: " + String.valueOf(equipment.getMainHandWeapon().getItemTemplate().getTemplateId()));
					log.warn("MIN_DAMAGE = " + String.valueOf(min));
					log.warn("MAX_DAMAGE = " + String.valueOf(max));
				}
				
				int base = Math.round(Rnd.get(min,max) * ags.getCurrentStat(StatEnum.POWER)* 0.01f);
				
				resultDamage = base + ags.getStatBonus(StatEnum.MAIN_HAND_PHYSICAL_ATTACK) + skillDamages + bonusDamages;

			}
			else //if hand attack
			{
				int base = Rnd.get(16,20);
				resultDamage = Math.round(base * (ags.getCurrentStat(StatEnum.POWER) * 0.01f));
			}
			
			if(attacker.isInState(CreatureState.POWERSHARD))
			{
				Item mainHandPowerShard = equipment.getMainHandPowerShard();
				if(mainHandPowerShard != null)
				{
					resultDamage += mainHandPowerShard.getItemTemplate().getWeaponBoost();

					equipment.usePowerShard(mainHandPowerShard, 1);
				}
			}
		}
		else if(attacker instanceof Summon)
		{
			int baseDamage = ags.getCurrentStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK);
			// 10% range for summon attack
			int max = ((baseDamage * attacker.getLevel()) / 10);
			int min = Math.round(max * 0.9f);
			resultDamage += Rnd.get(min, max);
		}
		else
		{
			NpcRank npcRank = ((Npc) attacker).getObjectTemplate().getRank();
			double multipler = calculateRankMultipler(npcRank);
			double hpGaugeMod = 1+(((Npc) attacker).getObjectTemplate().getHpGauge()/10);
			int baseDamage = ags.getCurrentStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK);
			int max = (int)((baseDamage * multipler * hpGaugeMod) + ((baseDamage*attacker.getLevel())/10));
			int min = max - ags.getCurrentStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK);		
			resultDamage += Rnd.get(min, max);
		}

		// physical defense
		resultDamage -= Math.round(attacked.getGameStats().getCurrentStat(StatEnum.PHYSICAL_DEFENSE) * 0.10f);
		
		if (resultDamage<=0)
			resultDamage=1;

		return resultDamage;
	}
	
	/**
	 * 
	 * @param attacker
	 * @param target
	 * @param skillDamages
	 * @return Damage made to target (-hp value)
	 */
	public static int calculateMagicalAttackToTarget(Creature attacker, Creature attacked, SkillElement element)
	{
		CreatureGameStats<?> ags = attacker.getGameStats();

		int resultDamage = 0;

		if (attacker instanceof Player)
		{
			int min = ags.getCurrentStat(StatEnum.MAIN_MIN_DAMAGES);
			int max = ags.getCurrentStat(StatEnum.MAIN_MAX_DAMAGES);

			Equipment equipment = ((Player)attacker).getEquipment();


			int base = Math.round(Rnd.get(min,max) * ags.getCurrentStat(StatEnum.KNOWLEDGE)* 0.01f);
			
			resultDamage = base + ags.getStatBonus(StatEnum.MAGICAL_ATTACK);
			
			if(attacker.isInState(CreatureState.POWERSHARD))
			{
				Item mainHandPowerShard = equipment.getMainHandPowerShard();
				if(mainHandPowerShard != null)
				{
					resultDamage += mainHandPowerShard.getItemTemplate().getWeaponBoost();

					equipment.usePowerShard(mainHandPowerShard, 1);
				}
			}
			
		}
				
		// magical resistance
		resultDamage = Math.round(resultDamage * (1 - attacked.getGameStats().getMagicalDefenseFor(element) / 1000f));
		
		if (resultDamage<=0)
			resultDamage=1;
		
		return resultDamage;
	}

	/**
	 * 
	 * @param attacker
	 * @param target
	 * @return
	 */
	public static int calculateOffHandPhysicDamageToTarget(Creature attacker, Creature attacked)
	{
		CreatureGameStats<?> ags = attacker.getGameStats();

		int min = ags.getCurrentStat(StatEnum.OFF_MIN_DAMAGES);
		int max = ags.getCurrentStat(StatEnum.OFF_MAX_DAMAGES);
		int average = Math.round((min + max)/2);

		Equipment equipment = ((Player)attacker).getEquipment();
		
		if(average < 1)
		{
			average = 1;
			log.warn("Weapon stat MIN_MAX_DAMAGE resulted average zero in off-hand calculation");
			log.warn("Weapon ID: " + String.valueOf(equipment.getOffHandWeapon().getItemTemplate().getTemplateId()));
			log.warn("MIN_DAMAGE = " + String.valueOf(min));
			log.warn("MAX_DAMAGE = " + String.valueOf(max));
		}

		int base = Rnd.get(min,max);
		
		int damage = base + ags.getStatBonus(StatEnum.OFF_HAND_PHYSICAL_ATTACK);

		if(attacker.isInState(CreatureState.POWERSHARD))
		{
			Item offHandPowerShard = equipment.getOffHandPowerShard();
			if(offHandPowerShard != null)
			{
				damage += offHandPowerShard.getItemTemplate().getWeaponBoost();
				equipment.usePowerShard(offHandPowerShard, 1);
			}
		}

		int dualEffect = ((Player)attacker).getEffectController().getDualEffect();
		if (dualEffect == 0)
		{
			log.warn("Missing dualeffect for player "+((Player)attacker).getName()+" possible hack? or bug?");
			dualEffect = 25;
		}
		
		if(Rnd.get(0, 100) < 25)
			damage *= (dualEffect * 0.01f);

		// physical defense
		damage -= Math.round(attacked.getGameStats().getCurrentStat(StatEnum.PHYSICAL_DEFENSE) * 0.10f);
		
		if (damage<=0)
			damage=1;

		return damage;
	}


	/**
	 * @param player
	 * @param target
	 * @param skillEffectTemplate
	 * @param dot -whether its damage over time - dot is not multiplied by knowledge
	 * 
	 * @return HP damage to target
	 */
	public static int calculateMagicDamageToTarget(Creature speller, Creature effected, int baseDamages, int bonusDamages, SkillElement element, boolean dot)
	{
		CreatureGameStats<?> sgs = speller.getGameStats();
		
		int totalBoostMagicalSkill = sgs.getCurrentStat(StatEnum.BOOST_MAGICAL_SKILL);
		
		int knowledge = 0;
		
		if (dot)
			knowledge = 100;
		else
			knowledge = sgs.getCurrentStat(StatEnum.KNOWLEDGE);
		
		int damages = Math.round(baseDamages * ((knowledge / 100f) + (totalBoostMagicalSkill / 1000f)));
		
		//add damage from actionmodifiers
		damages += bonusDamages;

		//elemental resistance
		damages = Math.round(damages * (1 - effected.getGameStats().getMagicalDefenseFor(element) / 1000f));
		
		if (damages<=0) {
			damages=1;
		}

		return damages;
	}

	/**
	 * 
	 * @param npcRank
	 * @return
	 */
	public static int calculateRankMultipler(NpcRank npcRank)
	{
		//FIXME: to correct formula, have any reference?
		int multipler;
		switch(npcRank) 
		{
			case JUNK: 
				multipler = 2;
				break;
			case NORMAL: 
				multipler = 2;
				break;
			case ELITE:
				multipler = 3;
				break;
			case HERO: 
				multipler = 4;
				break;
			case LEGENDARY: 
				multipler = 5;
				break;
			default: 
				multipler = 1;
		}

		return multipler;
	}

	/**
	 *  Calculates DODGE chance
	 *  
	 * @param attacker
	 * @param attacked
	 * @param accMod
	 * @return int
	 */
	public static int calculatePhysicalDodgeRate(Creature attacker, Creature attacked, int accMod)
	{
		//check attack status = BLIND
		if (attacker.getObserveController().checkAttackerStatus(AttackStatus.DODGE))
			return 100;
		//check always dodge
		if(attacked.getObserveController().checkAttackStatus(AttackStatus.DODGE))
			return 100;
		if(attacker.getEffectController().isAbnormalSet(EffectId.BLIND))
		    return 100;			
		int accurancy;

		if(attacker instanceof Player && ((Player) attacker).getEquipment().getOffHandWeaponType() != null)
			accurancy = Math.round((attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY) + attacker
				.getGameStats().getCurrentStat(StatEnum.OFF_HAND_ACCURACY)) / 2);
		else
			accurancy = attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY);

		//add bonus stat from effecttemplate
		accurancy += accMod;
		
		int dodgeRate = (attacked.getGameStats().getCurrentStat(StatEnum.EVASION) - accurancy) / 10;
		
		// maximal dodge rate
		if(dodgeRate > 30)
			dodgeRate = 30;

		if(dodgeRate <= 0)
			return 1;

		return dodgeRate;
	}
	
	/**
	 *  Calculates PARRY chance
	 *  
	 * @param attacker
	 * @param attacked
	 * @return int
	 */
	public static int calculatePhysicalParryRate(Creature attacker, Creature attacked)
	{
		//check always parry
		if(attacked.getObserveController().checkAttackStatus(AttackStatus.PARRY))
			return 100;
		
		int accuracy;
		
		if(attacker instanceof Player && ((Player) attacker).getEquipment().getOffHandWeaponType() != null)
			accuracy = Math.round((attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY) + attacker
				.getGameStats().getCurrentStat(StatEnum.OFF_HAND_ACCURACY)) / 2);
		else
			accuracy = attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY);

		int parryRate = (attacked.getGameStats().getCurrentStat(StatEnum.PARRY) - accuracy) / 10;
		// maximal parry rate
		if(parryRate > 40)
			parryRate = 40;

		if(parryRate <= 0)
			return 1;

		return parryRate;
	}
	
	/**
	 *  Calculates BLOCK chance
	 *  
	 * @param attacker
	 * @param attacked
	 * @return int
	 */
	public static int calculatePhysicalBlockRate(Creature attacker, Creature attacked)
	{
		//check always block
		if(attacked.getObserveController().checkAttackStatus(AttackStatus.BLOCK))
			return 100;
		
		int accuracy;

		if(attacker instanceof Player && ((Player) attacker).getEquipment().getOffHandWeaponType() != null)
			accuracy = Math.round((attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY) + attacker
				.getGameStats().getCurrentStat(StatEnum.OFF_HAND_ACCURACY)) / 2);
		else
			accuracy = attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY);

		int blockRate = (attacked.getGameStats().getCurrentStat(StatEnum.BLOCK) - accuracy) / 10;
		// maximal block rate
		if(blockRate > 50)
			blockRate = 50;

		if(blockRate <= 0)
			return 1;

		return blockRate;
	}
	
	/**
	 *  Calculates PHYSICAL CRITICAL chance
	 * @param attacker
	 * @param attacked
	 * @float criticalProb
	 * @return double
	 */
	public static double calculatePhysicalCriticalRate(Creature attacker, Creature attacked, float criticalProb)
	{
		int critical;
		
		// check always critical
		if(attacker.getObserveController().checkAttackerStatus(AttackStatus.CRITICAL))
			return 100;

		if(attacker instanceof Player && ((Player) attacker).getEquipment().getOffHandWeaponType() != null)
			critical = Math.round((attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_CRITICAL) + attacker
				.getGameStats().getCurrentStat(StatEnum.OFF_HAND_CRITICAL)) / 2); 
		else
			critical = attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_CRITICAL); 
			
		critical = Math.round(critical * criticalProb - attacked.getGameStats().getCurrentStat(StatEnum.PHYSICAL_CRITICAL_RESIST));
		
		double criticalRate;

		if(critical <= 440)
			criticalRate = critical * 0.1f;
		else if(critical <= 600)
			criticalRate = (440 * 0.1f) + ((critical - 440) * 0.05f);
		else
			criticalRate = (440 * 0.1f) + (160 * 0.05f) + ((critical - 600) * 0.02f);
		// minimal critical rate
		if(criticalRate < 1)
			criticalRate = 1;

		return criticalRate;
	}
	
	/**
	 *  Calculates MAGICAL CRITICAL chance
	 *  
	 * @param attacker
	 * @param attacked
	 * @param criticalProb
	 * @return double
	 */
	public static double calculateMagicalCriticalRate(Creature attacker, Creature attacked, float criticalProb)
	{
		int critical;
		
		critical = Math.round(attacker.getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL) * criticalProb - attacked.getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL_RESIST)); 
		
		double criticalRate;

		if(critical <= 440)
			criticalRate = critical * 0.1f;
		else if(critical <= 600)
			criticalRate = (440 * 0.1f) + ((critical - 440) * 0.05f);
		else
			criticalRate = (440 * 0.1f) + (160 * 0.05f) + ((critical - 600) * 0.02f);
		// minimal critical rate
		if(criticalRate < 1)
			criticalRate = 1;

		return criticalRate;
	}
	
	/**
	 *  Calculates RESIST chance
	 *  
	 * @param attacker
	 * @param attacked
	 * @param accMod
	 * @return int
	 */
	public static int calculateMagicalResistRate(Creature attacker, Creature attacked, int accMod)
	{		
		if(attacked.getObserveController().checkAttackStatus(AttackStatus.RESIST))
			return 100;
		
		int stat_res = attacked.getGameStats().getCurrentStat(StatEnum.MAGICAL_RESIST);
		int stat_acc = attacker.getGameStats().getCurrentStat(StatEnum.MAGICAL_ACCURACY);

		int attackerLevel = attacker.getLevel();
		int targetLevel = attacked.getLevel();

		//add bonus stat from effecttemplate
		stat_acc += accMod;
		
		int resist = (stat_res - stat_acc) / 10;
		
		if ((targetLevel - attackerLevel) > 2)
		 resist += (targetLevel - attackerLevel - 2) * 10;

		if(resist <= 0)//cant resist
			return 0;
		else if(resist > 95)//hardcap 95%
			return 95;
		else
			return resist;
	}

	/**
	 * Calculates the fall damage
	 * 
	 * @param player
	 * @param distance
	 * @return True if the player is forced to his bind location.
	 */
	public static boolean calculateFallDamage(Player player, float distance)
	{
		if(player.isInvul())
		{
			return false;
		}

		if(distance >= FallDamageConfig.MAXIMUM_DISTANCE_DAMAGE)
		{
			player.getController().onStopMove();
			player.getFlyController().onStopGliding();
			player.getController().onDie(player);
			
			if (player.getKisk() != null)
				player.getReviveController().kiskRevive();
			else
				player.getReviveController().bindRevive();
			return true;
		}
		else if(distance >= FallDamageConfig.MINIMUM_DISTANCE_DAMAGE)
		{
			float dmgPerMeter = player.getLifeStats().getMaxHp() * FallDamageConfig.FALL_DAMAGE_PERCENTAGE / 100f;
			int damage = (int) (distance * dmgPerMeter);

			player.getLifeStats().reduceHp(damage, player);
			PacketSendUtility.sendPacket(player, new SM_ATTACK_STATUS(player, SM_ATTACK_STATUS.TYPE.FALL_DAMAGE, -damage));
		}

		return false;
	}

}
