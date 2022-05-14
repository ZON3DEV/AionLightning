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

package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class MembershipConfig {

	@Property(key = "gameserver.instances.title.requirement", defaultValue = "10")
	public static byte INSTANCES_TITLE_REQ;
	@Property(key = "gameserver.instances.race.requirement", defaultValue = "10")
	public static byte INSTANCES_RACE_REQ;
	@Property(key = "gameserver.instances.level.requirement", defaultValue = "10")
	public static byte INSTANCES_LEVEL_REQ;
	@Property(key = "gameserver.instances.group.requirement", defaultValue = "10")
	public static byte INSTANCES_GROUP_REQ;
	@Property(key = "gameserver.instances.quest.requirement", defaultValue = "10")
	public static byte INSTANCES_QUEST_REQ;
	@Property(key = "gameserver.instances.cooldown", defaultValue = "10")
	public static byte INSTANCES_COOLDOWN;

	@Property(key = "gameserver.instances.item.requirement", defaultValue = "10")
	public static byte INSTANCES_ITEM_REQ;

	@Property(key = "gameserver.store.wh.all", defaultValue = "10")
	public static byte STORE_WH_ALL;
	@Property(key = "gameserver.store.accountwh.all", defaultValue = "10")
	public static byte STORE_AWH_ALL;
	@Property(key = "gameserver.store.legionwh.all", defaultValue = "10")
	public static byte STORE_LWH_ALL;
	@Property(key = "gameserver.trade.all", defaultValue = "10")
	public static byte TRADE_ALL;
	@Property(key = "gameserver.disable.soulbind", defaultValue = "10")
	public static byte DISABLE_SOULBIND;
	@Property(key = "gameserver.remodel.all", defaultValue = "10")
	public static byte REMODEL_ALL;
	@Property(key = "gameserver.emotions.all", defaultValue = "10")
	public static byte EMOTIONS_ALL;
	@Property(key = "gameserver.quest.stigma.slot", defaultValue = "10")
	public static byte STIGMA_SLOT_QUEST;
	@Property(key = "gameserver.soulsickness.disable", defaultValue = "10")
	public static byte DISABLE_SOULSICKNESS;
	@Property(key = "gameserver.autolearn.stigma", defaultValue = "10")
	public static byte STIGMA_AUTOLEARN;
	@Property(key = "gameserver.quest.limit.disable", defaultValue = "10")
	public static byte QUEST_LIMIT_DISABLED;
	@Property(key = "gameserver.titles.additional.enable", defaultValue = "10")
	public static byte TITLES_ADDITIONAL_ENABLE;
	@Property(key = "gameserver.character.additional.enable", defaultValue = "10")
	public static byte CHARACTER_ADDITIONAL_ENABLE;
	@Property(key = "gameserver.advanced.friendlist.enable", defaultValue = "10")
	public static byte ADVANCED_FRIENDLIST_ENABLE;
	@Property(key = "gameserver.character.additional.count", defaultValue = "8")
	public static byte CHARACTER_ADDITIONAL_COUNT;
	@Property(key = "gameserver.advanced.friendlist.size", defaultValue = "90")
	public static int ADVANCED_FRIENDLIST_SIZE;

	/**
	 * Membership Tags
	 */
	@Property(key = "gameserver.membership.tag.display", defaultValue = "true")
	public static boolean PREMIUM_TAG_DISPLAY;
	@Property(key = "gameserver.membership.tag.vip", defaultValue = "\uE02E %s")
	public static String TAG_VIP;
	@Property(key = "gameserver.membership.tag.premium", defaultValue = "\uE02D %s")
	public static String TAG_PREMIUM;

	/**
	 * Online Bonus
	 */
	@Property(key = "gameserver.onlinebonus.enable", defaultValue = "true")
	public static boolean ONLINE_BONUS_ENABLE;
	@Property(key = "gameserver.onlinebonus.item", defaultValue = "186000127")
	public static int ONLINE_BONUS_ITEM;
	@Property(key = "gameserver.onlinebonus.count", defaultValue = "1")
	public static int ONLINE_BONUS_COUNT;
	@Property(key = "gameserver.onlinebonus.time", defaultValue = "60")
	public static int ONLINE_BONUS_TIME;
	/**
	 * AP Online Bonus
	 */
	@Property(key = "gameserver.ap.onlinebonus.enable", defaultValue = "true")
	public static boolean AP_ONLINE_BONUS_ENABLE;
	@Property(key = "gameserver.ap.onlinebonus.time", defaultValue = "60")
	public static int AP_ONLINE_BONUS_TIME;
	@Property(key = "gameserver.ap.onlinebonus.count", defaultValue = "1000")
	public static int AP_ONLINE_BONUS_COUNT;
	/**
	 * Toll Online Bonus
	 */
	@Property(key = "gameserver.toll.onlinebonus.enable", defaultValue = "true")
	public static boolean TOLL_ONLINE_BONUS_ENABLE;
	@Property(key = "gameserver.toll.onlinebonus.time", defaultValue = "60")
	public static int TOLL_ONLINE_BONUS_TIME;
	@Property(key = "gameserver.toll.onlinebonus.count", defaultValue = "1000")
	public static int TOLL_ONLINE_BONUS_COUNT;
}
