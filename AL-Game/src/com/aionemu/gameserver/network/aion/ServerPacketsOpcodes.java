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

package com.aionemu.gameserver.network.aion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.aionemu.gameserver.network.aion.serverpackets.*;

/**
 * This class is holding opcodes for all server packets. It's used only to have
 * all opcodes in one place
 *
 * @author Luno, alexa026, ATracer, avol, orz, cura
 */
public class ServerPacketsOpcodes {

	private static Map<Class<? extends AionServerPacket>, Integer> opcodes = new HashMap<Class<? extends AionServerPacket>, Integer>();

	static {
		Set<Integer> idSet = new HashSet<Integer>();

		addPacketOpcode(SM_VERSION_CHECK.class, 0x00, idSet); // 4.5
		addPacketOpcode(SM_STATS_INFO.class, 0x01, idSet); // 4.5
		addPacketOpcode(SM_STATUPDATE_HP.class, 0x03, idSet); // 4.5
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x04, idSet); // 4.5
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x05, idSet); // 4.5
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x06, idSet); // 4.5
		addPacketOpcode(SM_DP_INFO.class, 0x07, idSet); // 4.5
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x08, idSet); // 4.5
		addPacketOpcode(SM_NPC_ASSEMBLER.class, 0x0A, idSet); // 4.5
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x0B, idSet); // 4.5
		addPacketOpcode(SM_LEGION_TABS.class, 0x0C, idSet); // 4.5
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x0D, idSet); // 4.5
		addPacketOpcode(SM_NPC_INFO.class, 0x0E, idSet); // 4.5
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x0F, idSet); // 4.5
		addPacketOpcode(SM_FORTRESS_INFO.class, 0xF3, idSet); // 4.5
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x11, idSet); // 4.5
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x14, idSet); // 4.5
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x15, idSet); // 4.5
		addPacketOpcode(SM_DELETE.class, 0x16, idSet); // 4.5
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x17, idSet); // 4.5
		addPacketOpcode(SM_MESSAGE.class, 0x18, idSet); // 4.5
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x19, idSet); // 4.5
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x1A, idSet); // 4.5
		addPacketOpcode(SM_INVENTORY_ADD_ITEM.class, 0x1B, idSet); // 4.5
		addPacketOpcode(SM_DELETE_ITEM.class, 0x1C, idSet); // 4.5
		addPacketOpcode(SM_INVENTORY_UPDATE_ITEM.class, 0x1D, idSet); // 4.5
		addPacketOpcode(SM_UI_SETTINGS.class, 0x1E, idSet); // 4.5
		addPacketOpcode(SM_PLAYER_STANCE.class, 0x1F, idSet); // 4.5
		addPacketOpcode(SM_PLAYER_INFO.class, 0x20, idSet); // 4.5
		addPacketOpcode(SM_CASTSPELL.class, 0x21, idSet); // 4.5
		addPacketOpcode(SM_GATHER_STATUS.class, 0x22, idSet); // 4.5
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x23, idSet); // 4.5
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x24, idSet); // 4.5
		addPacketOpcode(SM_EMOTION.class, 0x25, idSet); // 4.5
		addPacketOpcode(SM_GAME_TIME.class, 0x26, idSet); // 4.5
		addPacketOpcode(SM_TIME_CHECK.class, 0x27, idSet); // 4.5
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x28, idSet); // 4.5
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x29, idSet); // 4.5
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x2A, idSet); // 4.5
		addPacketOpcode(SM_CASTSPELL_RESULT.class, 0x2B, idSet); // 4.5
		addPacketOpcode(SM_SKILL_LIST.class, 0x2C, idSet); // 4.5
		addPacketOpcode(SM_SKILL_REMOVE.class, 0x2D, idSet); // 4.5
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x2E, idSet); // 4.5
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x31, idSet); // 4.5
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x32, idSet); // 4.5
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x33, idSet); // 4.5
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x34, idSet); // 4.5
		addPacketOpcode(SM_ATTACK.class, 0x36, idSet); // 4.5
		addPacketOpcode(SM_MOVE.class, 0x37, idSet); // 4.5
		addPacketOpcode(SM_HEADING_UPDATE.class, 0x39, idSet); // 4.5
		addPacketOpcode(SM_TRANSFORM.class, 0x3A, idSet); // 4.5
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x3C, idSet); // 4.5
		addPacketOpcode(SM_SELL_ITEM.class, 0x3E, idSet); // 4.5
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x41, idSet); // 4.5
		addPacketOpcode(SM_WEATHER.class, 0x43, idSet); // 4.5
		addPacketOpcode(SM_PLAYER_STATE.class, 0x44, idSet); // 4.5
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x46, idSet); // 4.5
		addPacketOpcode(SM_QUEST_LIST.class, 0x47, idSet); // 4.5
		addPacketOpcode(SM_KEY.class, 0x48, idSet); // 4.5
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x49, idSet); // 4.5
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x4A, idSet); // 4.5
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x4B, idSet); // 4.5
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x4D, idSet); // 4.5
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x4E, idSet); // 4.5
		addPacketOpcode(SM_EMOTION_LIST.class, 0x4F, idSet); // 4.5
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x51, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_EDIT.class, 0x52, idSet); // 4.5
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x53, idSet); // 4.5
		addPacketOpcode(SM_SERIAL_KILLER.class, 0x54, idSet); // 4.5
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x55, idSet); // 4.5
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x56, idSet); // 4.5
		addPacketOpcode(SM_CAPTCHA.class, 0x57, idSet); // 4.5
		addPacketOpcode(SM_RENAME.class, 0x58, idSet); // 4.5
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x59, idSet); // 4.5
		addPacketOpcode(SM_GROUP_INFO.class, 0x5A, idSet); // 4.5
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x5B, idSet); // 4.5
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x62, idSet); // 4.5
		addPacketOpcode(SM_CHAT_WINDOW.class, 0x63, idSet); // 4.5// //4.5
		addPacketOpcode(SM_PET.class, 0x65, idSet); // 4.5
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x67, idSet); // 4.5
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x68, idSet); // 4.5
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x69, idSet); // 4.5
		addPacketOpcode(SM_LEGION_INFO.class, 0x6E, idSet); // 4.5
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x6F, idSet); // 4.5
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x70, idSet); // 4.5
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x71, idSet); // 4.5
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x72, idSet); // 4.5
		// addPacketOpcode(SM_ATTACK_STATUS_MESSAGE.class, 0x73, idSet); //4.5 TODO
		addPacketOpcode(SM_HOUSE_REGISTRY.class, 0x74, idSet); // 4.5
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x77, idSet); // 4.5
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x79, idSet); // 4.5
		// addPacketOpcode(SM_RIFT_STATUS.class, 0x8C, idSet); //4.5
		addPacketOpcode(SM_AUTO_GROUP.class, 0x7A, idSet); // 4.5
		addPacketOpcode(SM_QUEST_COMPLETED_LIST.class, 0x7B, idSet); // 4.5
		addPacketOpcode(SM_QUEST_ACTION.class, 0x7C, idSet); // 4.5
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x7F, idSet); // 4.5
		addPacketOpcode(SM_PING_RESPONSE.class, 0x80, idSet); // 4.5
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x82, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_SCRIPTS.class, 0x83, idSet); // 4.5
		addPacketOpcode(SM_FRIEND_LIST.class, 0x84, idSet); // 4.5
		addPacketOpcode(SM_PRIVATE_STORE.class, 0x86, idSet); // 4.5
		addPacketOpcode(SM_GROUP_LOOT.class, 0x87, idSet); // 4.5
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0x88, idSet); // 4.5
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0x89, idSet); // 4.5
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0x8A, idSet); // 4.5
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0x8B, idSet); // 4.5
		addPacketOpcode(SM_INSTANCE_INFO.class, 0x8D, idSet); // 4.5
		addPacketOpcode(SM_PONG.class, 0x8E, idSet); // 4.5
		addPacketOpcode(SM_KISK_UPDATE.class, 0x90, idSet); // 4.5
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0x91, idSet); // 4.5
		addPacketOpcode(SM_BROKER_SERVICE.class, 0x92, idSet); // 4.5
		addPacketOpcode(SM_MOTION.class, 0x94, idSet); // 4.5
		// addPacketOpcode(SM_95.class, 0x95, idSet); //4.5 // TODO
		addPacketOpcode(SM_TRADE_IN_LIST.class, 0x97, idSet); // 4.5
		// addPacketOpcode(SM_RELOG_RELATED.class, 0x98, idSet); //4.5 // TODO
		// addPacketOpcode(SM_BROKER_REGISTRATION_SERVICE.class, 0x93, idSet); //4.5
		// addPacketOpcode(SM_BROKER_SETTLED_LIST.class, 0x95, idSet); //4.5
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x9A, idSet); // 4.5
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x99, idSet); // 4.5
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x9B, idSet); // 4.5
		addPacketOpcode(SM_TRANSFORM_IN_SUMMON.class, 0x9C, idSet); // 4.5
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9D, idSet); // 4.5
		addPacketOpcode(SM_LEGION_EDIT.class, 0x9E, idSet); // 4.5
		addPacketOpcode(SM_TOLL_INFO.class, 0x9F, idSet); // 4.5
		addPacketOpcode(SM_DRAWING_TOOL.class, 0xA0, idSet); // 4.5
		addPacketOpcode(SM_MAIL_SERVICE.class, 0xA1, idSet); // 4.5
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0xA2, idSet); // 4.5
		addPacketOpcode(SM_WINDSTREAM.class, 0xA3, idSet); // 4.5
		addPacketOpcode(SM_WINDSTREAM_ANNOUNCE.class, 0xA4, idSet); // 4.5
		addPacketOpcode(SM_FIND_GROUP.class, 0xA6, idSet); // 4.5
		addPacketOpcode(SM_REPURCHASE.class, 0xA7, idSet); // 4.5
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xA8, idSet); // 4.5
		addPacketOpcode(SM_WAREHOUSE_ADD_ITEM.class, 0xA9, idSet); // 4.5
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xAA, idSet); // 4.5
		addPacketOpcode(SM_WAREHOUSE_UPDATE_ITEM.class, 0xAB, idSet); // 4.5
		addPacketOpcode(SM_IN_GAME_SHOP_CATEGORY_LIST.class, 0xAC, idSet); // 4.5
		addPacketOpcode(SM_IN_GAME_SHOP_LIST.class, 0xAD, idSet); // 4.5
		addPacketOpcode(SM_IN_GAME_SHOP_ITEM.class, 0xAE, idSet); // 4.5
		addPacketOpcode(SM_ICON_INFO.class, 0xAF, idSet); // 4.5
		addPacketOpcode(SM_TITLE_INFO.class, 0xB0, idSet); // 4.5
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0xB1, idSet); // 4.5
		// addPacketOpcode(_SM_UNK2.class, 0xB3, idSet); //4.5 // TODO
		// addPacketOpcode(SM_BROKER_REGISTERED_LIST.class, 0xB1, idSet); //4.5
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xB4, idSet); // 4.5
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB5, idSet); // 4.5
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB6, idSet); // 4.5
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB7, idSet); // 4.5
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB8, idSet); // 4.5
		addPacketOpcode(SM_DUEL.class, 0xB9, idSet); // 4.5
		addPacketOpcode(SM_PET_EMOTE.class, 0xBB, idSet); // 4.5
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBF, idSet); // 4.5
		addPacketOpcode(SM_DIE.class, 0xC1, idSet); // 4.5
		addPacketOpcode(SM_RESURRECT.class, 0xC2, idSet); // 4.5
		addPacketOpcode(SM_FORCED_MOVE.class, 0xC3, idSet); // 4.5
		addPacketOpcode(SM_TELEPORT_MAP.class, 0xC4, idSet); // 4.5
		addPacketOpcode(SM_USE_OBJECT.class, 0xC5, idSet); // 4.5
		addPacketOpcode(SM_EMOTION_SWITCH.class, 0xC6, idSet); // 4.5
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xC7, idSet); // 4.5
		addPacketOpcode(SM_CHARACTER_LIST.class, 0xC8, idSet); // 4.5
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0xC9, idSet); // 4.5
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0xCA, idSet); // 4.5
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xCB, idSet); // 4.5
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0xCC, idSet); // 4.5
		addPacketOpcode(SM_LOOT_STATUS.class, 0xCD, idSet); // 4.5
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xCE, idSet); // 4.5
		addPacketOpcode(SM_RECIPE_LIST.class, 0x0CF, idSet); // 4.5
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0xD0, idSet); // 4.5
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xD1, idSet); // 4.5
		addPacketOpcode(SM_SIEGE_LOCATION_STATE.class, 0xD2, idSet); // 4.5
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0xD3, idSet); // 4.5
		addPacketOpcode(SM_LEGION_SEND_EMBLEM.class, 0xD5, idSet); // 4.5
		addPacketOpcode(SM_LEGION_SEND_EMBLEM_DATA.class, 0xD6, idSet); // 4.5
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD7, idSet); // 4.5
		addPacketOpcode(SM_REGION_INFO.class, 0xD8, idSet); // 4.5
		addPacketOpcode(SM_PLAYER_REGION.class, 0xD9, idSet); // 4.5
		addPacketOpcode(SM_SHIELD_EFFECT.class, 0xDA, idSet); // 4.5
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0xDC, idSet); // 4.5
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDE, idSet); // 4.5
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0xDF, idSet); // 4.5
		addPacketOpcode(SM_BLOCK_LIST.class, 0xE0, idSet); // 4.5
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xE1, idSet); // 4.5
		addPacketOpcode(SM_TOWNS_LIST.class, 0xE2, idSet); // 4.5
		addPacketOpcode(SM_FRIEND_STATUS.class, 0xE3, idSet); // 4.5
		addPacketOpcode(SM_CHANNEL_INFO.class, 0xE5, idSet); // 4.5
		addPacketOpcode(SM_CHAT_INIT.class, 0xE6, idSet); // 4.5
		addPacketOpcode(SM_MACRO_LIST.class, 0xE7, idSet); // 4.5
		addPacketOpcode(SM_MACRO_RESULT.class, 0xE8, idSet); // 4.5
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0xE9, idSet); // 4.5
		addPacketOpcode(SM_BIND_POINT_INFO.class, 0xEB, idSet); // 4.5
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0xEC, idSet); // 4.5
		addPacketOpcode(SM_ABYSS_RANK.class, 0xED, idSet); // 4.5
		addPacketOpcode(SM_PETITION.class, 0xEF, idSet); // 4.5
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0xF0, idSet); // 4.5
		addPacketOpcode(SM_LEARN_RECIPE.class, 0xF1, idSet); // 4.5
		addPacketOpcode(SM_RECIPE_DELETE.class, 0xF2, idSet); // 4.5
		addPacketOpcode(SM_FLY_TIME.class, 0xF4, idSet); // 4.5
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0xF5, idSet); // 4.5
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF6, idSet); // 4.5
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0xF7, idSet); // 4.5
		addPacketOpcode(SM_SHOW_BRAND.class, 0xF9, idSet); // 4.5
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xFA, idSet); // 4.5
		addPacketOpcode(SM_PRICES.class, 0xFC, idSet); // 4.5
		addPacketOpcode(SM_TRADELIST.class, 0xFD, idSet); // 4.5
		addPacketOpcode(SM_RECONNECT_KEY.class, 0xFF, idSet); // 4.5
		addPacketOpcode(SM_INSTANCE_STAGE_INFO.class, 0x8C, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_BIDS.class, 0x100, idSet); // 4.5
		addPacketOpcode(SM_RECEIVE_BIDS.class, 0x103, idSet); // 4.5
		//addPacketOpcode(SM_SERVER_TRANSFER.class, 0x105, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_PAY_RENT.class, 0x106, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_OWNER_INFO.class, 0x107, idSet); // 4.5
		addPacketOpcode(SM_OBJECT_USE_UPDATE.class, 0x108, idSet); // 4.5
		addPacketOpcode(SM_PACKAGE_INFO_NOTIFY.class, 0x10A, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_OBJECT.class, 0x10C, idSet); // 4.5
		addPacketOpcode(SM_DELETE_HOUSE_OBJECT.class, 0x10D, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_OBJECTS.class, 0x10E, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_RENDER.class, 0x10F, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_UPDATE.class, 0x3D, idSet); // 4.5
		addPacketOpcode(SM_DELETE_HOUSE.class, 0x110, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_ACQUIRE.class, 0x113, idSet); // 4.5
		addPacketOpcode(SM_GROUP_DATA_EXCHANGE.class, 0xB2, idSet); // 4.5
		addPacketOpcode(SM_INSTANCE_COUNT_INFO.class, 0x93, idSet); // 4.5
		addPacketOpcode(SM_FAST_TRACK.class, 0x96, idSet); // 4.5
		addPacketOpcode(SM_SERVER_IDS.class, 0x114, idSet); // 4.5
		addPacketOpcode(SM_MARK_FRIENDLIST.class, 0x117, idSet); // 4.5
		addPacketOpcode(SM_DISPUTE_LAND.class, 0x11B, idSet); // 4.5
		addPacketOpcode(SM_HOUSE_TELEPORT.class, 0xDD, idSet); // 4.5
		addPacketOpcode(SM_CHALLENGE_LIST.class, 0x118, idSet); // 4.5
		addPacketOpcode(SM_STATS_STATUS_UNK.class, 0x196, idSet); // 4.5
		addPacketOpcode(SM_ACCOUNT_ACCESS_PROPERTIES.class, 0xEE, idSet); // 4.5
		addPacketOpcode(SM_MEGAPHONE.class, 0x11D, idSet); // 4.5
		addPacketOpcode(SM_SECURITY_TOKEN.class, 0x112, idSet); // 4.5
		addPacketOpcode(SM_RIDE_ROBOT.class, 0x5C, idSet); // 4.5
		addPacketOpcode(SM_QUEST_REPEAT.class, 0x122, idSet); // 4.5
		addPacketOpcode(SM_FAST_TRACK_MOVE.class, 0x105, idSet);
		addPacketOpcode(SM_AFTER_TIME_CHECK.class, 0x124, idSet); // 4.6.2 checked
		// addPacketOpcode(SM_UNK_4_5.class, 0x123, idSet); //4.5
		// addPacketOpcode(SM_FIRST_SHOW_DECOMPOSABLE.class, 0x11C, idSet); //4.5
		// addPacketOpcode(SM_SECONDARY_SHOW_DECOMPOSABLE.class, 0x11E, idSet); //4.5
		// addPacketOpcode(SM_STATE_UNK.class, 0xFE, idSet); //4.5
		// addPacketOpcode(SM_FB_UNK.class, 0xFB, idSet); //4.5
		// addPacketOpcode(SM_A5_UNK.class, 0xA5, idSet); //4.5
		// addPacketOpcode(SM_BD_UNK.class, 0xBD, idSet); //4.5
		// addPacketOpcode(SM_DB_UNK.class, 0xDB, idSet); //4.5
		// addPacketOpcode(SM_104_UNK.class, 0x104, idSet); //4.5
		addPacketOpcode(SM_SELECT_ITEM_LIST.class, 0x11C, idSet); // 4.6.2 checked
        addPacketOpcode(SM_SELECT_ITEM_ADD.class, 0x11E, idSet); // 4.6.2 checked
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet); // 4.5
	}

	static int getOpcode(Class<? extends AionServerPacket> packetClass) {
		Integer opcode = opcodes.get(packetClass);
		if (opcode == null) {
			throw new IllegalArgumentException("There is no opcode for " + packetClass + " defined.");
		}

		return opcode;
	}

	private static void addPacketOpcode(Class<? extends AionServerPacket> packetClass, int opcode, Set<Integer> idSet) {
		if (opcode < 0) {
			return;
		}

		if (idSet.contains(opcode)) {
			throw new IllegalArgumentException(String.format("There already exists another packet with id 0x%02X", opcode));
		}

		idSet.add(opcode);
		opcodes.put(packetClass, opcode);
	}
}
