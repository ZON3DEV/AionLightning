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

package admincommands;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author xXMashUpXx
 *
 */
public class Attrbonus extends AdminCommand implements StatOwner {
	/**
	 * @param alias
	 */
	public Attrbonus() {
		super("attrbonus");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aionlightning.gameserver.utils.chathandlers.ChatCommand#execute(com
	 * .aionlightning.gameserver.model.gameobjects.player.Player,
	 * java.lang.String[])
	 */
	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ATTRBONUS_MESSAGE1));
			return;
		}

		int parameter = 0;
		try {
			parameter = Integer.parseInt(params[1]);
		} catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ATTRBONUS_MESSAGE2));
			return;
		}

		if (params[0].equalsIgnoreCase("speed")) {
			if (parameter < 1000) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SPEED, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("maxhp") || params[0].equalsIgnoreCase("hp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAXHP, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("hpregen")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.REGEN_HP, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("maxmp") || params[0].equalsIgnoreCase("mp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAXMP, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("mpregen")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.REGEN_MP, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("flyspeed")) {
			if (parameter < 1000) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.FLY_SPEED, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("maxdp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAXDP, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("ARALL")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.ABNORMAL_RESISTANCE_ALL, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("allpara")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.ALLRESIST, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("strvit")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.STRVIT, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("knowil")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.KNOWIL, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("agidex")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.AGIDEX, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("str")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.POWER, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("vit")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.HEALTH, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("agi")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.ACCURACY, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("dex")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.AGILITY, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("kno")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.KNOWLEDGE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("wil")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.WILL, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("ElementalDefendWater")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.WATER_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("ElementalDefendAir")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.WIND_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("ElementalDefendEarth")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.EARTH_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("ElementalDefendFire")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.FIRE_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("FPRegen")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.REGEN_FP, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("physicaldefend")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.PHYSICAL_DEFENSE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("magicalAttack")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAGICAL_ATTACK, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("magicalResist")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAGICAL_RESIST, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("attackDelay")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.ATTACK_SPEED, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("dodge")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.EVASION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("parry")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.PARRY, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("block")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.BLOCK, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("critical")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.PHYSICAL_CRITICAL, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("hitCount")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.HIT_COUNT, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("attackRange")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.ATTACK_RANGE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("magicalCritical")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAGICAL_CRITICAL, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("concentration")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.CONCENTRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arPoison")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.POISON_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arBleed")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.BLEED_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arBleed")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.BLEED_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arParalyze")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.PARALYZE_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arSleep")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SLEEP_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arRoot")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.ROOT_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arBlind")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.BLIND_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arCharm")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.CHARM_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arDisease")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.DISEASE_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arSilence")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SILENCE_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arFear")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.FEAR_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arConfuse")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.CONFUSE_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arStun")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.STUN_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arPerification")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.PERIFICATION_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arStumble")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.STUMBLE_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arStagger")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.STAGGER_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arOpenAreial")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.OPENAREIAL_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arSnare")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SNARE_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arSlow")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SLOW_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("arSpin")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SPIN_RESISTANCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Poison Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.POISON_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Bleed Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.BLEED_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Paralyze Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.PARALYZE_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Sleep Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SLEEP_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Root Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.ROOT_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Blind Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.BLIND_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Charm Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.CHARM_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Disease Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.DISEASE_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Silence Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SILENCE_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Fear Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.FEAR_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Curse Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.CURSE_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Confuse Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.CONFUSE_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Stun Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.STUN_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Perification Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.PERIFICATION_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Stumble Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.STUMBLE_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Stagger Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.STAGGER_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("OpenAreial Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.OPENAREIAL_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Snare Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SNARE_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Slow Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SLOW_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("Spin Arp")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.SPIN_RESISTANCE_PENETRATION, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("MagicalSkillBoost")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.BOOST_MAGICAL_SKILL, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("MagicalHitAccuracy")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAGICAL_ACCURACY, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("BoostCastingTime")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.BOOST_CASTING_TIME, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("HealSkillBoost")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.HEAL_BOOST, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("PhysicalCriticalReduceRate")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.PHYSICAL_CRITICAL_RESIST, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("MagicalCriticalReduceRate")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAGICAL_CRITICAL_RESIST, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("PhysicalCriticalDamageReduce")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.PHYSICAL_CRITICAL_DAMAGE_REDUCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("MagicalCriticalDamageReduce")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAGICAL_CRITICAL_DAMAGE_REDUCE, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("magicalDefend")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAGICAL_DEFEND, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		} else if (params[0].equalsIgnoreCase("MagicalSkillBoostResist")) {
			if (parameter < 99999999) {
				List<IStatFunction> functions = new ArrayList<IStatFunction>();
				functions.add(new StatChangeFunction(StatEnum.MAGIC_SKILL_BOOST_RESIST, parameter));
				admin.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
			}
		}
	}

	class StatChangeFunction extends StatFunction {
		static final int speed = 6000;
		static final int flyspeed = 9000;
		static final int maxDp = 4000;
		int modifier = 1;

		StatChangeFunction(StatEnum stat, int modifier) {
			this.stat = stat;
			this.modifier = modifier;
		}

		@Override
		public void apply(Stat2 stat) {
			switch (this.stat) {
				case SPEED:
					stat.setBase(speed + (speed * modifier) / 100);
					break;
				case FLY_SPEED:
					stat.setBase(flyspeed + (flyspeed * modifier) / 100);
					break;
				case POWER:
					short modifierPower = (short) modifier;
					stat.setBase(Math.round(modifierPower));
					break;
				case MAXHP:
					float modifierHp = (float) modifier;
					stat.setBase(Math.round(modifierHp));
					break;
				case MAXMP:
					float modifierMp = (float) modifier;
					stat.setBase(Math.round(modifierMp));
					break;
				case REGEN_HP:
					int baseHp = stat.getOwner().getLevel() + 3;
					stat.setBase(baseHp *= modifier / 100f);
					break;
				case REGEN_MP:
					int baseMp = stat.getOwner().getLevel() + 8;
					stat.setBase(baseMp *= modifier / 100f);
					break;
				case MAXDP:
					stat.setBase(maxDp + (maxDp * modifier) / 100);
					break;
				case ALLRESIST:
					short modifierALLR = (short) modifier;
					stat.setBase(Math.round(modifierALLR));
					break;
				case ABNORMAL_RESISTANCE_ALL:
					short modifierAbnormalALLR = (short) modifier;
					stat.setBase(Math.round(modifierAbnormalALLR));
					break;
				case STRVIT:
					short modifierStrvit = (short) modifier;
					stat.setBase(Math.round(modifierStrvit));
					break;
				case KNOWIL:
					short modifierKnowil = (short) modifier;
					stat.setBase(Math.round(modifierKnowil));
					break;
				case AGIDEX:
					short modifierAgidex = (short) modifier;
					stat.setBase(Math.round(modifierAgidex));
					break;
				case HEALTH:
					short modifierHealth = (short) modifier;
					stat.setBase(Math.round(modifierHealth));
					break;
				case ACCURACY:
					short modifierAccuracy = (short) modifier;
					stat.setBase(Math.round(modifierAccuracy));
					break;
				case AGILITY:
					short modifierAgility = (short) modifier;
					stat.setBase(Math.round(modifierAgility));
					break;
				case KNOWLEDGE:
					short modifierKnow = (short) modifier;
					stat.setBase(Math.round(modifierKnow));
					break;
				case WILL:
					short modifierWill = (short) modifier;
					stat.setBase(Math.round(modifierWill));
					break;
				case WATER_RESISTANCE:
					short modifierWaterRes = (short) modifier;
					stat.setBase(Math.round(modifierWaterRes));
					break;
				case WIND_RESISTANCE:
					short modifierWindRes = (short) modifier;
					stat.setBase(Math.round(modifierWindRes));
					break;
				case EARTH_RESISTANCE:
					short modifierEarthRes = (short) modifier;
					stat.setBase(Math.round(modifierEarthRes));
					break;
				case FIRE_RESISTANCE:
					short modifierFireRes = (short) modifier;
					stat.setBase(Math.round(modifierFireRes));
					break;
				case REGEN_FP:
					stat.setBase(Math.round(modifier));
					break;
				case PHYSICAL_DEFENSE:
					stat.setBase(Math.round(modifier));
					break;
				case MAGICAL_ATTACK:
					short modifierMAttack = (short) modifier;
					stat.setBase(Math.round(modifierMAttack));
					break;
				case MAGICAL_RESIST:
					short modifierMResist = (short) modifier;
					stat.setBase(Math.round(modifierMResist));
					break;
				case ATTACK_SPEED:
					short modifierASpeed = (short) modifier;
					stat.setBase(Math.round(modifierASpeed / 2));
					break;
				case EVASION:
					short modifierEvasion = (short) modifier;
					stat.setBase(Math.round(modifierEvasion));
					break;
				case PARRY:
					short modifierParry = (short) modifier;
					stat.setBase(Math.round(modifierParry));
					break;
				case BLOCK:
					short modifierBlock = (short) modifier;
					stat.setBase(Math.round(modifierBlock));
					break;
				case PHYSICAL_CRITICAL:
					short modifierPCrit = (short) modifier;
					stat.setBase(Math.round(modifierPCrit));
					break;
				case HIT_COUNT:
					short modifierHCount = (short) modifier;
					stat.setBase(Math.round(modifierHCount));
					break;
				case ATTACK_RANGE:
					float modifierARange = (float) modifier;
					stat.setBase(Math.round(modifierARange));
					break;
				case MAGICAL_CRITICAL:
					short modifierMCrit = (short) modifier;
					stat.setBase(Math.round(modifierMCrit));
					break;
				case CONCENTRATION:
					short modifierConcentration = (short) modifier;
					stat.setBase(Math.round(modifierConcentration));
					break;
				case POISON_RESISTANCE:
					short modifierPResist = (short) modifier;
					stat.setBase(Math.round(modifierPResist));
					break;
				case BLEED_RESISTANCE:
					short modifierBResist = (short) modifier;
					stat.setBase(Math.round(modifierBResist));
					break;
				case PARALYZE_RESISTANCE:
					short modifierPAResist = (short) modifier;
					stat.setBase(Math.round(modifierPAResist));
					break;
				case SLEEP_RESISTANCE:
					short modifierSResist = (short) modifier;
					stat.setBase(Math.round(modifierSResist));
					break;
				case ROOT_RESISTANCE:
					short modifierRResist = (short) modifier;
					stat.setBase(Math.round(modifierRResist));
					break;
				case BLIND_RESISTANCE:
					short modifierBLResist = (short) modifier;
					stat.setBase(Math.round(modifierBLResist));
					break;
				case CHARM_RESISTANCE:
					short modifierCResist = (short) modifier;
					stat.setBase(Math.round(modifierCResist));
					break;
				case DISEASE_RESISTANCE:
					short modifierDResist = (short) modifier;
					stat.setBase(Math.round(modifierDResist));
					break;
				case SILENCE_RESISTANCE:
					short modifierSIResist = (short) modifier;
					stat.setBase(Math.round(modifierSIResist));
					break;
				case FEAR_RESISTANCE:
					short modifierFResist = (short) modifier;
					stat.setBase(Math.round(modifierFResist));
					break;
				case CURSE_RESISTANCE:
					short modifierCUResist = (short) modifier;
					stat.setBase(Math.round(modifierCUResist));
					break;
				case CONFUSE_RESISTANCE:
					short modifierCOResist = (short) modifier;
					stat.setBase(Math.round(modifierCOResist));
					break;
				case STUN_RESISTANCE:
					short modifierSTResist = (short) modifier;
					stat.setBase(Math.round(modifierSTResist));
					break;
				case PERIFICATION_RESISTANCE:
					short modifierPEResist = (short) modifier;
					stat.setBase(Math.round(modifierPEResist));
					break;
				case STUMBLE_RESISTANCE:
					short modifierSTUResist = (short) modifier;
					stat.setBase(Math.round(modifierSTUResist));
					break;
				case STAGGER_RESISTANCE:
					short modifierSTAResist = (short) modifier;
					stat.setBase(Math.round(modifierSTAResist));
					break;
				case OPENAREIAL_RESISTANCE:
					short modifierOResist = (short) modifier;
					stat.setBase(Math.round(modifierOResist));
					break;
				case SNARE_RESISTANCE:
					short modifierSNResist = (short) modifier;
					stat.setBase(Math.round(modifierSNResist));
					break;
				case SLOW_RESISTANCE:
					short modifierSLResist = (short) modifier;
					stat.setBase(Math.round(modifierSLResist));
					break;
				case SPIN_RESISTANCE:
					short modifierSPResist = (short) modifier;
					stat.setBase(Math.round(modifierSPResist));
					break;
				case POISON_RESISTANCE_PENETRATION:
					short modifierPRP = (short) modifier;
					stat.setBase(Math.round(modifierPRP));
					break;
				case BLEED_RESISTANCE_PENETRATION:
					short modifierBRP = (short) modifier;
					stat.setBase(Math.round(modifierBRP));
					break;
				case PARALYZE_RESISTANCE_PENETRATION:
					short modifierPARP = (short) modifier;
					stat.setBase(Math.round(modifierPARP));
					break;
				case SLEEP_RESISTANCE_PENETRATION:
					short modifierSRP = (short) modifier;
					stat.setBase(Math.round(modifierSRP));
					break;
				case ROOT_RESISTANCE_PENETRATION:
					short modifierRRP = (short) modifier;
					stat.setBase(Math.round(modifierRRP));
					break;
				case BLIND_RESISTANCE_PENETRATION:
					short modifierBLRP = (short) modifier;
					stat.setBase(Math.round(modifierBLRP));
					break;
				case CHARM_RESISTANCE_PENETRATION:
					short modifierCRP = (short) modifier;
					stat.setBase(Math.round(modifierCRP));
					break;
				case DISEASE_RESISTANCE_PENETRATION:
					short modifierDRP = (short) modifier;
					stat.setBase(Math.round(modifierDRP));
					break;
				case SILENCE_RESISTANCE_PENETRATION:
					short modifierSIRP = (short) modifier;
					stat.setBase(Math.round(modifierSIRP));
					break;
				case FEAR_RESISTANCE_PENETRATION:
					short modifierFRP = (short) modifier;
					stat.setBase(Math.round(modifierFRP));
					break;
				case CURSE_RESISTANCE_PENETRATION:
					short modifierCURP = (short) modifier;
					stat.setBase(Math.round(modifierCURP));
					break;
				case CONFUSE_RESISTANCE_PENETRATION:
					short modifierCORP = (short) modifier;
					stat.setBase(Math.round(modifierCORP));
					break;
				case STUN_RESISTANCE_PENETRATION:
					short modifierSTRP = (short) modifier;
					stat.setBase(Math.round(modifierSTRP));
					break;
				case PERIFICATION_RESISTANCE_PENETRATION:
					short modifierPERP = (short) modifier;
					stat.setBase(Math.round(modifierPERP));
					break;
				case STUMBLE_RESISTANCE_PENETRATION:
					short modifierSTURP = (short) modifier;
					stat.setBase(Math.round(modifierSTURP));
					break;
				case STAGGER_RESISTANCE_PENETRATION:
					short modifierSTARP = (short) modifier;
					stat.setBase(Math.round(modifierSTARP));
					break;
				case OPENAREIAL_RESISTANCE_PENETRATION:
					short modifierORP = (short) modifier;
					stat.setBase(Math.round(modifierORP));
					break;
				case SNARE_RESISTANCE_PENETRATION:
					short modifierSNRP = (short) modifier;
					stat.setBase(Math.round(modifierSNRP));
					break;
				case SLOW_RESISTANCE_PENETRATION:
					short modifierSLRP = (short) modifier;
					stat.setBase(Math.round(modifierSLRP));
					break;
				case SPIN_RESISTANCE_PENETRATION:
					short modifierSPRP = (short) modifier;
					stat.setBase(Math.round(modifierSPRP));
					break;
				case BOOST_MAGICAL_SKILL:
					short modifierBMSkill = (short) modifier;
					stat.setBase(Math.round(modifierBMSkill));
					break;
				case MAGICAL_ACCURACY:
					short modifierMAccuracy = (short) modifier;
					stat.setBase(Math.round(modifierMAccuracy));
					break;
				case BOOST_CASTING_TIME:
					short modifierBCTime = (short) modifier;
					stat.setBase(Math.round(modifierBCTime));
					break;
				case HEAL_BOOST:
					short modifierHBoost = (short) modifier;
					stat.setBase(Math.round(modifierHBoost));
					break;
				case PHYSICAL_CRITICAL_RESIST:
					short modifierPCResist = (short) modifier;
					stat.setBase(Math.round(modifierPCResist));
					break;
				case MAGICAL_CRITICAL_RESIST:
					short modifierMCResist = (short) modifier;
					stat.setBase(Math.round(modifierMCResist));
					break;
				case PHYSICAL_CRITICAL_DAMAGE_REDUCE:
					short modifierPCDReduce = (short) modifier;
					stat.setBase(Math.round(modifierPCDReduce));
					break;
				case MAGICAL_CRITICAL_DAMAGE_REDUCE:
					short modifierMCDReduce = (short) modifier;
					stat.setBase(Math.round(modifierMCDReduce));
					break;
				case MAGICAL_DEFEND:
					stat.setBase(Math.round(modifier));
					break;
				case MAGIC_SKILL_BOOST_RESIST:
					short modifierMSBResist = (short) modifier;
					stat.setBase(Math.round(modifierMSBResist));
					break;
				default:
					break;
			}
		}

		@Override
		public int getPriority() {
			return 60;
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_ATTRBONUS_MESSAGE1));
	}
}
